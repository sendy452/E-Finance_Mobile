package com.google.e_finance.ui.laporan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.e_finance.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LaporanFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_laporan, container, false);
        return root;
    }
}
