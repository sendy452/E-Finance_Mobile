package com.google.e_finance.ui.pengeluaran;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.e_finance.Login;
import com.google.e_finance.R;
import com.google.e_finance.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.google.e_finance.MainActivity.TAG_ID;

public class InsertData extends Fragment {

    private DatePickerDialog mDatePickerDialog;
    EditText tgl, jumlah, keterangan;
    SharedPreferences sharedpreferences;

    private String url = "https://e-finance7.000webhostapp.com/Api/insert_pengeluaran/";
    private static final String TAG = InsertData.class.getSimpleName();

    String tag_json_obj = "json_obj_req", id_user;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_insert, container, false);
        TextView jdl = root.findViewById(R.id.judul);
        jdl.setText("TAMBAH PENGELUARAN");
        Button btn_hapus = root.findViewById(R.id.btn_hapus);
        btn_hapus.setVisibility(View.GONE);
        EditText id = root.findViewById(R.id.inp_id);
        id.setVisibility(View.GONE);
        tgl = root.findViewById(R.id.inp_tgl);
        setDateTimeField();
        tgl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        jumlah = root.findViewById(R.id.inp_jumlah);
        keterangan = root.findViewById(R.id.inp_keterangan);

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = getActivity().getIntent().getStringExtra(TAG_ID);

        Button btn_simpan = root.findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tanggal = tgl.getText().toString();
                String jum = jumlah.getText().toString();
                String ket = keterangan.getText().toString();

                if (tanggal.trim().length() > 0 && jum.trim().length() > 0) {
                    StringRequest strReq = new StringRequest(Request.Method.POST, url + id_user, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "Add Response: " + response.toString());
                            try {
                                JSONObject jObj = new JSONObject(response);
                                Log.e("Successfully Add!", jObj.toString());

                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Sukses Tambah Data", Toast.LENGTH_LONG).show();
                                
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.nav_host_fragment, new PengeluaranFragment());
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
                    }){

                        @Override
                        protected Map<String, String> getParams() {
                            // Posting parameters to login url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("tgl_pengeluaran", tanggal);
                            params.put("jumlah", jum);
                            params.put("keterangan", ket);

                            return params;
                        }

                    };

                    AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_kembali = root.findViewById(R.id.btn_cancel);
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new PengeluaranFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                tgl.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
}
