package com.google.e_finance.ui.laporan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.e_finance.BuildConfig;
import com.google.e_finance.Login;
import com.google.e_finance.MainActivity;
import com.google.e_finance.R;
import com.google.e_finance.app.AppController;
import com.google.e_finance.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static com.google.e_finance.MainActivity.TAG_ID;

public class LaporanFragment extends Fragment {

    SharedPreferences sharedpreferences;

    private String url = "http://192.168.100.9:81/CI_E-Finance/Api/laporan/";

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String TAG_QUERY1 = "query1";
    private static final String TAG_JUMLAH1 = "jumlahmasuk";
    private static final String TAG_QUERY2 = "query2";
    private static final String TAG_JUMLAH2 = "jumlahkeluar";

    String tag_json_obj = "json_obj_req", id_user;

    private String url_pendapatan = "https://e-finance7.000webhostapp.com/Api/exportPendapatan/";
    private String url_pengeluaran = "https://e-finance7.000webhostapp.com/Api/exportPengeluaran/";
    ProgressDialog pdDialog;
    private static final int PERMISSION_REQUEST_CODE = 100;
    StringBuilder data;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_laporan, container, false);
        final TextView txtquery = root.findViewById(R.id.total_transMasuk);
        final TextView txttotal = root.findViewById(R.id.txt_totalPendapatan);
        final TextView txtquery2 = root.findViewById(R.id.total_transKeluar);
        final TextView txttotal2 = root.findViewById(R.id.txt_totalPengeluaran);

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = getActivity().getIntent().getStringExtra(TAG_ID);

        /** Mengambil data user dari DB */
        StringRequest strReq = new StringRequest(Request.Method.GET, url + id_user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    Integer query1 = jObj.getInt(TAG_QUERY1);
                    Integer total = jObj.getInt(TAG_JUMLAH1);
                    Integer query2 = jObj.getInt(TAG_QUERY2);
                    Integer total2 = jObj.getInt(TAG_JUMLAH2);

                    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
                    otherSymbols.setDecimalSeparator(',');
                    otherSymbols.setGroupingSeparator('.');
                    DecimalFormat formatter = new DecimalFormat("#,###.00", otherSymbols);
                    String get_total = formatter.format(total);
                    txttotal.setText(String.valueOf(get_total));
                    txtquery.setText(String.valueOf(query1));
                    String get_total2 = formatter.format(total2);
                    txttotal2.setText(String.valueOf(get_total2));
                    txtquery2.setText(String.valueOf(query2));


                    Log.e("Successfully Get Data!", jObj.toString());

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


        pdDialog = new ProgressDialog(getActivity());
        pdDialog.setMessage("Fetching Date...");
        pdDialog.setCancelable(false);
        final Button btn_dapat = root.findViewById(R.id.export_pendapatan);
        btn_dapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    FetchDataPendapatan(url_pendapatan+id_user);
                }else{
                    // If permission is not granted we will request for the Permission
                    requestPermission();
                }
            }
        });

        final Button btn_keluar = root.findViewById(R.id.export_pengeluaran);
        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    FetchDataPengeluaran(url_pengeluaran+id_user);
                }else{
                    // If permission is not granted we will request for the Permission
                    requestPermission();
                }
            }
        });

        return root;
    }

    private void FetchDataPendapatan(String url)
    {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //we get the successful in String response
                        Log.e("MY_DATA",response);
                        try{
                            pdDialog.dismiss();
                            if(response.equals("NONE"))
                            {
                                Toast.makeText(getContext(),"NO Data Found",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }else{
                                pdDialog.dismiss();
                                // In String response we get full data in a form of list
                                splitdataPendapatan(response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void splitdataPendapatan(String response) {
        System.out.println("GET DATA IS " + response);
        // response will have a @ symbol so that we can split individual user data
        String res_data[] = response.split("],\\[");
        //StringBuilder  to store the data
        data = new StringBuilder();

        //row heading to store in CSV file
        data.append("No.,Tanggal,Jumlah Pendapatan Rp.,Keterangan");

        for(int i = 0; i < res_data.length;i++){
            //then we split each user data using # symbol as we have in the response string
            final String[] each_user = res_data[i].split(",");
            data.append("\n"+ String.valueOf(i+1)+","+ each_user[1]+","+ each_user[2]+","+ each_user[3]);
        }
        CreateCSVPendapatan(data);
    }
    private void CreateCSVPendapatan(StringBuilder data) {
        try {
            //
            FileOutputStream out = getActivity().openFileOutput("Laporan Pendapatan"+".csv", Context.MODE_PRIVATE);

            //store the data in CSV file by passing String Builder data
            out.write(data.toString().getBytes());
            out.close();
            Context context = getContext().getApplicationContext();
            final File newFile = new File(Environment.getExternalStorageDirectory(),"Laporanku");
            if(!newFile.exists())
            {
                newFile.mkdir();
            }
            File file = new File(context.getFilesDir(),"Laporan Pendapatan"+".csv");
            Uri path = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            //once the file is ready a share option will pop up using which you can share
            // the same CSV from via Gmail or store in Google Drive
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Laporan Pendapatan");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent,"Excel Data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FetchDataPengeluaran(String url)
    {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //we get the successful in String response
                Log.e("MY_DATA",response);
                try{
                    pdDialog.dismiss();
                    if(response.equals("NONE"))
                    {
                        Toast.makeText(getContext(),"NO Data Found",Toast.LENGTH_LONG).show();
                        pdDialog.dismiss();
                    }else{
                        pdDialog.dismiss();
                        // In String response we get full data in a form of list
                        splitdataPengeluaran(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pdDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void splitdataPengeluaran(String response) {
        System.out.println("GET DATA IS " + response);
        // response will have a @ symbol so that we can split individual user data
        String res_data[] = response.split("],\\[");
        //StringBuilder  to store the data
        data = new StringBuilder();

        //row heading to store in CSV file
        data.append("No.,Tanggal,Jumlah Pengeluaran Rp.,Keterangan");

        for(int i = 0; i < res_data.length;i++){
            //then we split each user data using # symbol as we have in the response string
            final String[] each_user = res_data[i].split(",");
            data.append("\n"+ String.valueOf(i+1)+","+ each_user[1]+","+ each_user[2]+","+ each_user[3]);
        }
        CreateCSVPengeluaran(data);
    }
    private void CreateCSVPengeluaran(StringBuilder data) {
        try {
            //
            FileOutputStream out = getActivity().openFileOutput("Laporan Pengeluaran"+".csv", Context.MODE_PRIVATE);

            //store the data in CSV file by passing String Builder data
            out.write(data.toString().getBytes());
            out.close();
            Context context = getContext().getApplicationContext();
            final File newFile = new File(Environment.getExternalStorageDirectory(),"Laporanku");
            if(!newFile.exists())
            {
                newFile.mkdir();
            }
            File file = new File(context.getFilesDir(),"Laporan Pengeluaran"+".csv");
            Uri path = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            //once the file is ready a share option will pop up using which you can share
            // the same CSV from via Gmail or store in Google Drive
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Laporan Pengeluaran");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent,"Excel Data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // checking permission To WRITE
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    // request permission for WRITE Access
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
