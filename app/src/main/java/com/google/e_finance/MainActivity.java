package com.google.e_finance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import java.io.File;
import java.io.FileOutputStream;

import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView txt_id, txt_username;
    String email, nama, id_user;
    SharedPreferences sharedpreferences;

    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_ID = "id_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        txt_id = (TextView) header.findViewById(R.id.v_txtemail);
        txt_username = (TextView) header.findViewById(R.id.v_txtnama);
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        email = getIntent().getStringExtra(TAG_EMAIL);
        nama = getIntent().getStringExtra(TAG_NAMA);
        id_user = getIntent().getStringExtra(TAG_ID);

        txt_id.setText(email);
        txt_username.setText(nama);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_pendapatan,
                R.id.nav_pengeluaran,
                R.id.nav_laporan,
                R.id.nav_setting,
                R.id.nav_insert_pendapatan)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void exportPendapatan(View view){
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("NO,TANGGAL PENDAPATAN,JUMLAH,KETERANGAN");
        for(int i = 0; i<5; i++){
            data.append("\n"+String.valueOf(i+1)+","+String.valueOf(i*i)+","+String.valueOf("Rp." + "10000")+","+ String.valueOf("Blalabla"));
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("Laporan Pendapatan.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting

            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "Laporan Pendapatan.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "EXCEL DATA"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void exportPengeluaran(View view){
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Time,Distance");
        for(int i = 0; i<5; i++){
            data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}