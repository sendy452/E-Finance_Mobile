package com.google.e_finance.ui.pendapatan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.e_finance.Login;
import com.google.e_finance.R;
import com.google.e_finance.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PendapatanFragment extends Fragment {

    private String url = "https://e-finance7.000webhostapp.com/Api/pendapatan/";
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    String id_user;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id_user";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pendapatan, container, false);
        setHasOptionsMenu(true);

        mRecyclerview = root.findViewById(R.id.recyclerviewTemp);
        mItems = new ArrayList<>();

        loadJson();

        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterData(getActivity(), mItems);
        mRecyclerview.setAdapter(mAdapter);

        return root;
    }

    /**
     * Memuat kumpulan data Json dari database melalui API
     */
    private void loadJson() {

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id_user = getActivity().getIntent().getStringExtra(TAG_ID);

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, url + id_user, null, new Response.Listener<JSONArray>() {
            /** Apabila sukses memuat maka akan menjalankan function onResponse dan menampilkan keseluruhan data */
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Length: " + response.length());
                Log.d("volley", "response : " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ModelData md = new ModelData();
                        md.setIduser(String.valueOf(i+1));
                        md.setTgl(data.getString("tgl_pemasukan"));
                        md.setJumlah(data.getString("jumlah"));
                        md.setKeterangan(data.getString("keterangan"));
                        md.setId(data.getString("id_pemasukan"));
                        mItems.add(md);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Length: " + response.length());
                mAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    /** Apabila gagal memuat maka akan menjalankan onErrorResponse */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(reqData);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.btn_add);
        Drawable icon = getResources().getDrawable(R.drawable.add);
        icon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        MenuItem item2 = menu.findItem(R.id.btn_refresh);
        Drawable icon2 = getResources().getDrawable(R.drawable.refresh);
        icon2.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        item.setIcon(icon);
        item2.setIcon(icon2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btn_refresh:
                Fragment newFragment = new PendapatanFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            case R.id.btn_add:
                Fragment newFragment1 = new InsertData();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.nav_host_fragment, newFragment1);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}