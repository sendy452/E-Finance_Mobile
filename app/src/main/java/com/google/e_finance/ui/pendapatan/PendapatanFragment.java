package com.google.e_finance.ui.pendapatan;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.e_finance.R;

public class PendapatanFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pendapatan, container, false);
        final TextView textView = root.findViewById(R.id.jumlah);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_button, menu);
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
}