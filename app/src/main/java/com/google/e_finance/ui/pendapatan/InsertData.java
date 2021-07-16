package com.google.e_finance.ui.pendapatan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.e_finance.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class InsertData extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_insert_pendapatan, container, false);
        Button btn_hapus = root.findViewById(R.id.btn_hapus);
        btn_hapus.setVisibility(View.GONE);
        Button btn_kembali = root.findViewById(R.id.btn_cancel);
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new PendapatanFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
