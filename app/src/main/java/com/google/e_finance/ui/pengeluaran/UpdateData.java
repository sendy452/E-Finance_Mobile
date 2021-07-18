package com.google.e_finance.ui.pengeluaran;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.e_finance.R;
import com.google.e_finance.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateData extends AppCompatActivity {

    private DatePickerDialog mDatePickerDialog;
    EditText tgl, jumlah, keterangan, id;
    Button btnsimpan;

    private static final String TAG = UpdateData.class.getSimpleName();

    private String url_update = "https://e-finance7.000webhostapp.com/Api/update_pengeluaran/";
    private String url_hapus = "https://e-finance7.000webhostapp.com/Api/hapus_pengeluaran/";

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        final TextView judul = findViewById(R.id.judul);
        judul.setText("TAMBAH PENGELUARAN");
        Button btn_hapus = findViewById(R.id.btn_hapus);

        Button btn_kembali = findViewById(R.id.btn_cancel);
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /** Mengambil data dari Pengeluaran */
        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);
        String intent_id = data.getStringExtra("id_pengeluaran");
        String intent_tgl = data.getStringExtra("tgl_pengeluaran");
        String intent_jumlah = data.getStringExtra("jumlah");
        String intent_keterangan = data.getStringExtra("keterangan");

        id = findViewById(R.id.inp_id);
        id.setVisibility(View.GONE);

        tgl = findViewById(R.id.inp_tgl);
        setDateTimeField();
        tgl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        jumlah = findViewById(R.id.inp_jumlah);
        keterangan = findViewById(R.id.inp_keterangan);
        btnsimpan = findViewById(R.id.btn_simpan);

        /** Apabila kondisi update terpenuhi atau =1 maka akan menampilkan detail data*/
        if (update == 1) {
            btnsimpan.setText("Update");
            judul.setText("UPDATE PENGELUARAN");
            id.setText(intent_id);
            tgl.setText(intent_tgl);
            jumlah.setText(intent_jumlah);
            keterangan.setText(intent_keterangan);
        }
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            /** Tombol simpan akan menjalankan function Update_data apabila kondisi update == 1
             *  jika tidak maka menjalankan function simpanData */
            @Override
            public void onClick(View view) {
                /** Update Data Pengeluaran*/
                StringRequest updateReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
                    /** Menjalankan function onResponse apabila berhasil merubah data*/
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.e("Successfully Get Data!", res.toString());

                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            /** Menjalankan function onErrorResponse apabila gagal atau error saat merubah data*/
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext() , "Gagal Update Data", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    /** Mengirim data sesuai dengan inputan */
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("id_pengeluaran", id.getText().toString());
                        map.put("tgl_pengeluaran", tgl.getText().toString());
                        map.put("jumlah", jumlah.getText().toString());
                        map.put("keterangan", keterangan.getText().toString());

                        return map;
                    }
                };
                AppController.getInstance().addToRequestQueue(updateReq);
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Update Data Pengeluaran*/
                StringRequest updateReq = new StringRequest(Request.Method.POST, url_hapus + intent_id, new Response.Listener<String>() {
                    /** Menjalankan function onResponse apabila berhasil merubah data*/
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(getApplicationContext() , "Sukses Hapus Data", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext() , "Sukses Hapus Data", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            /** Menjalankan function onErrorResponse apabila gagal atau error saat merubah data*/
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext() , "Gagal Hapus Data", Toast.LENGTH_SHORT).show();
                            }
                        });
                AppController.getInstance().addToRequestQueue(updateReq);
            }
        });
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

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
