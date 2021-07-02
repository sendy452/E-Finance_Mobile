package com.google.e_finance.ui.pendapatan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.e_finance.R;

public class PendapatanFragment extends Fragment {

    private PendapatanViewModel pendapatanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pendapatanViewModel =
                new ViewModelProvider(this).get(PendapatanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pendapatan, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        pendapatanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}