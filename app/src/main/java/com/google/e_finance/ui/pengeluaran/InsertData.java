package com.google.e_finance.ui.pengeluaran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.e_finance.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class InsertData extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_insert_pendapatan, container, false);
        final TextView judul = root.findViewById(R.id.judul);
        judul.setText("TAMBAH PENGELUARAN");
        Button btn_hapus = root.findViewById(R.id.btn_hapus);
        btn_hapus.setVisibility(View.GONE);

        return root;
    }
}
