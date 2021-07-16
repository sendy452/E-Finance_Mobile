package com.google.e_finance;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.e_finance.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn_register;
    TextView Gologin;
    EditText txt_username, txt_password, txt_nama, txt_nohp, txt_email;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = "http://192.168.100.9:81/CI_E-Finance/Api/register/";

    private static final String TAG = Register.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        Gologin = (TextView) findViewById(R.id.login);
        btn_register = (Button) findViewById(R.id.btnRegis);
        txt_username = (EditText) findViewById(R.id.txtUsername);
        txt_password = (EditText) findViewById(R.id.txtPassword);
        txt_nama = (EditText) findViewById(R.id.txtNama);
        txt_email = (EditText) findViewById(R.id.txtEmail);
        txt_nohp = (EditText)  findViewById(R.id.txtHp);

        Gologin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(Register.this, Login.class);
                finish();
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String pass = txt_password.getText().toString();
                String nama = txt_nama.getText().toString();
                String email = txt_email.getText().toString();
                String noHP = txt_nohp.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (username.trim().length() > 0 && pass.trim().length() > 0 &&
                        nama.trim().length() > 0 && email.trim().length() > 0) {
                    if (!email.matches(emailPattern)) {
                        Toast.makeText(getApplicationContext() ,"Format Email Salah", Toast.LENGTH_LONG).show();
                    }else {
                        if (conMgr.getActiveNetworkInfo() != null
                                && conMgr.getActiveNetworkInfo().isAvailable()
                                && conMgr.getActiveNetworkInfo().isConnected()) {
                            checkRegister(username, pass, nama, email, noHP);
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkRegister(final String username, final String pass, final String nama, final String email, final String noHP) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Register ...");
        pDialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                pDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    pDialog.setCancelable(false);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        txt_username.setText("");
                        txt_password.setText("");
                        txt_nama.setText("");
                        txt_email.setText("");
                        txt_nohp.setText("");

                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                    } else {
                        pDialog.setCancelable(true);
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    pDialog.setCancelable(true);
                    Toast.makeText(getApplicationContext(), "Username is Already Use", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", pass);
                params.put("nama", nama);
                params.put("email", email);
                params.put("nohp", noHP);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(Register.this, Login.class);
        finish();
        startActivity(intent);
    }
}