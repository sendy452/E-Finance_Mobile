package com.google.e_finance.ui.setting;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.e_finance.Login;
import com.google.e_finance.R;
import com.google.e_finance.app.AppController;
import com.google.e_finance.ui.pengeluaran.InsertData;
import com.google.e_finance.ui.pengeluaran.PengeluaranFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Laporkan extends Fragment {

    EditText txtemail, txtjudul, txtisi, id;
    SharedPreferences sharedpreferences;

    private String url = "https://e-finance7.000webhostapp.com/Api/prosesLaporkan/";
    private static final String TAG = Laporkan.class.getSimpleName();

    String tag_json_obj = "json_obj_req", username;
    public static final String TAG_USERNAME = "username";

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_insert_laporan, container, false);
        id = root.findViewById(R.id.inp_username);
        id.setVisibility(View.GONE);
        txtemail = root.findViewById(R.id.inp_email);
        txtjudul = root.findViewById(R.id.inp_judul);
        txtisi = root.findViewById(R.id.inp_isi);

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        username = getActivity().getIntent().getStringExtra(TAG_USERNAME);
        id.setText(username);
        Button btn_simpan = root.findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String email = txtemail.getText().toString();
                String judul = txtjudul.getText().toString();
                String isi = txtisi.getText().toString();
                String user = id.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.trim().length() > 0 && judul.trim().length() > 0 && isi.trim().length() > 0) {
                    if (!email.matches(emailPattern)) {
                        Toast.makeText(getContext(), "Format Email Salah", Toast.LENGTH_LONG).show();
                    } else {
                        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "Add Response: " + response.toString());
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    Log.e("Successfully Add!", jObj.toString());

                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "Sukses Lapor", Toast.LENGTH_LONG).show();

                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.nav_host_fragment, new SettingFragment());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
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

                            @Override
                            protected Map<String, String> getParams() {
                                // Posting parameters to login url
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("judul", judul);
                                params.put("isi", isi);
                                params.put("user", user);

                                return params;
                            }

                        };

                        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Gagal Lapor", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_kembali = root.findViewById(R.id.btn_cancel);
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new SettingFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
