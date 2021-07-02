package com.google.e_finance.ui.pengeluaran;

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

public class PengeluaranFragment extends Fragment {

    private PengeluaranViewModel pengeluaranViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pengeluaranViewModel =
                new ViewModelProvider(this).get(PengeluaranViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pengeluaran, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        pengeluaranViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}