package com.google.e_finance.ui.home;

import android.graphics.Color;
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
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

import com.google.e_finance.R;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView1 = root.findViewById(R.id.txt_pendapatan);
        final TextView textView2 = root.findViewById(R.id.txt_pengeluaran);
        final PieChartView pieChartView = root.findViewById(R.id.chart);

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(35, Color.parseColor("#1cc88a")).setLabel("Pendapatan: 35%"));
        pieData.add(new SliceValue(15, Color.parseColor("#e74a3b")).setLabel("Pengeluaran: 15%"));
        pieData.add(new SliceValue(50, Color.parseColor("#36b9cc")).setLabel("Sisa: 50%"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(false).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true);

        textView1.setText("2000000");
        textView2.setText("100000");
        pieChartView.setPieChartData(pieChartData);

        return root;
    }
}