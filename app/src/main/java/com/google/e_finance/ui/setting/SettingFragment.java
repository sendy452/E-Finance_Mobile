package com.google.e_finance.ui.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.e_finance.Login;
import com.google.e_finance.R;
import com.google.e_finance.Register;
import com.google.e_finance.app.AppController;
import com.google.e_finance.ui.home.HomeFragment;
import com.google.e_finance.ui.laporan.LaporanFragment;
import com.google.e_finance.ui.pengeluaran.PengeluaranFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

import static com.google.e_finance.MainActivity.TAG_EMAIL;
import static com.google.e_finance.MainActivity.TAG_ID;
import static com.google.e_finance.MainActivity.TAG_NAMA;

public class SettingFragment extends Fragment {

    Button btnOut;
    SharedPreferences sharedpreferences;
    int success;
    ConnectivityManager conMgr;

    private String url = "https://e-finance7.000webhostapp.com/Api/profil/";
    private String url2 = "https://e-finance7.000webhostapp.com/Api/update/";

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String TAG_SUCCESS = "status";
    private static final String TAG_NOHP = "noHP";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASS = "pass";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req", id_user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        setHasOptionsMenu(true);
        btnOut = root.findViewById(R.id.btnLogout);
        Button btnLapor = root.findViewById(R.id.btnLapor);
        btnLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new Laporkan());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        final EditText txtnama = root.findViewById(R.id.txtNama);
        final EditText txtnohp = root.findViewById(R.id.txtHp);
        final EditText txtemail = root.findViewById(R.id.txtEmail);
        final EditText txtusername = root.findViewById(R.id.txtUsername);
        final EditText txtpass = root.findViewById(R.id.txtPassword);

        conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = getActivity().getIntent().getStringExtra(TAG_ID);

        /** Mengambil data user dari DB */
        StringRequest strReq = new StringRequest(Request.Method.GET, url + id_user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    // Check for error node in json
                    if (success == 1) {

                        String nama = jObj.getString(TAG_NAMA);
                        String nohp = jObj.getString(TAG_NOHP);
                        String email = jObj.getString(TAG_EMAIL);
                        String username = jObj.getString(TAG_USERNAME);
                        String pass = jObj.getString(TAG_PASS);

                        txtnama.setText(nama);
                        txtnohp.setText(nohp);
                        txtemail.setText(email);
                        txtusername.setText(username);
                        txtpass.setText(pass);

                        Log.e("Successfully Get Data!", jObj.toString());

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

        /** Tombol untuk proses logout user yg sedang login*/
        btnOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_NAMA, null);
                editor.putString(TAG_EMAIL, null);
                editor.commit();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        /** Tombol untuk proses update data user yg login*/
        Button btnUp = root.findViewById(R.id.btnUpdate);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String nama = txtnama.getText().toString();
                String email = txtemail.getText().toString();
                String username = txtusername.getText().toString();
                String pass = txtpass.getText().toString();
                String noHP = txtnohp.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (username.trim().length() > 0 && pass.trim().length() > 0 &&
                        nama.trim().length() > 0 && email.trim().length() > 0) {
                    if (!email.matches(emailPattern)) {
                        Toast.makeText(getActivity().getApplicationContext() ,"Format Email Salah", Toast.LENGTH_LONG).show();
                    }else {
                        if (conMgr.getActiveNetworkInfo() != null
                                && conMgr.getActiveNetworkInfo().isAvailable()
                                && conMgr.getActiveNetworkInfo().isConnected()) {
                            StringRequest strReq = new StringRequest(Request.Method.POST, url2 + id_user, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "Update Response: " + response.toString());
                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        Log.e("Successfully Update!", jObj.toString());

                                        Toast.makeText(getActivity().getApplicationContext(),
                                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        // JSON error
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "Error: " + error.getMessage());
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }){

                                @Override
                                protected Map<String, String> getParams() {
                                    // Posting parameters to login url
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("username", username);
                                    params.put("password", pass);
                                    params.put("nama", nama);
                                    params.put("email", email);
                                    params.put("noHP", noHP);

                                    return params;
                                }

                            };

                            AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}
