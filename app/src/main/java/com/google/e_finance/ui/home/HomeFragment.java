package com.google.e_finance.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.e_finance.Login;
import com.google.e_finance.MainActivity;
import com.google.e_finance.R;
import com.google.e_finance.Register;
import com.google.e_finance.app.AppController;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeFragment extends Fragment {

    SharedPreferences sharedpreferences;

    int success;

    private String url = "https://e-finance7.000webhostapp.com/Api/home/";

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    public final static String TAG_PENDAPATAN = "pendapatan";
    public final static String TAG_TOTAL = "total";
    public final static String TAG_PENGELUARAN = "pengeluaran";
    public final static String TAG_TOTAL2 = "total2";
    public final static String TAG_AKTIF = "aktif";
    public final static String TAG_KETERANGAN = "keterangan";
    public static final String TAG_ID = "id_user";

    String tag_json_obj = "json_obj_req", id_user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView txtpendapatan = root.findViewById(R.id.txt_pendapatan);
        final TextView txttotal = root.findViewById(R.id.total_pendapatan);
        final TextView txtpengeluaran = root.findViewById(R.id.txt_pengeluaran);
        final TextView txttotal2 = root.findViewById(R.id.total_pengeluaran);
        final TextView txtsisa = root.findViewById(R.id.txt_sisa);
        final TextView txtaktif = root.findViewById(R.id.txt_aktivitas);
        final TextView txtketerangan = root.findViewById(R.id.txt_keterangan);
        final PieChartView pieChartView = root.findViewById(R.id.chart);

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = getActivity().getIntent().getStringExtra(TAG_ID);

        StringRequest strReq = new StringRequest(Request.Method.GET, url + id_user, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    // Check for error node in json
                    if (success == 1) {
                        Integer pendapatan = jObj.getInt(TAG_PENDAPATAN);
                        Integer total = jObj.getInt(TAG_TOTAL);
                        Integer pengeluaran = jObj.getInt(TAG_PENGELUARAN);
                        Integer total2 = jObj.getInt(TAG_TOTAL2);
                        String aktif = jObj.getString(TAG_AKTIF);
                        String keterangan = jObj.getString(TAG_KETERANGAN);

                        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
                        otherSymbols.setDecimalSeparator(',');
                        otherSymbols.setGroupingSeparator('.');
                        DecimalFormat formatter = new DecimalFormat("#,###.00", otherSymbols);
                        String get_pendapatan = formatter.format(pendapatan);
                        txtpendapatan.setText(String.valueOf(get_pendapatan));
                        String get_total = formatter.format(total);
                        txttotal.setText(String.valueOf(get_total));
                        String get_pengeluaran = formatter.format(pengeluaran);
                        txtpengeluaran.setText(String.valueOf(get_pengeluaran));
                        String get_total2 = formatter.format(total2);
                        txttotal2.setText(String.valueOf(get_total2));
                        String get_sisa = formatter.format(total-total2);
                        txtsisa.setText(String.valueOf(get_sisa));
                        txtaktif.setText(aktif);
                        txtketerangan.setText(keterangan);

                        List pieData = new ArrayList<>();
                        pieData.add(new SliceValue(total, Color.parseColor("#1cc88a")).setLabel("Pendapatan: 35%"));
                        pieData.add(new SliceValue(total2, Color.parseColor("#e74a3b")).setLabel("Pengeluaran: 15%"));
                        pieData.add(new SliceValue((total-total2), Color.parseColor("#36b9cc")).setLabel("Sisa: 50%"));

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(false).setValueLabelTextSize(14);
                        pieChartData.setHasCenterCircle(true);

                        pieChartView.setPieChartData(pieChartData);


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

        return root;
    }
}