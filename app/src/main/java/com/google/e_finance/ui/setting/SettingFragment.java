package com.google.e_finance.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.e_finance.Login;
import com.google.e_finance.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.google.e_finance.MainActivity.TAG_EMAIL;
import static com.google.e_finance.MainActivity.TAG_ID;
import static com.google.e_finance.MainActivity.TAG_NAMA;

public class SettingFragment extends Fragment {

    Button btnOut;
    SharedPreferences sharedpreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        setHasOptionsMenu(true);
        sharedpreferences = this.getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        btnOut = root.findViewById(R.id.btnLogout);
        btnOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_NAMA, null);
                editor.putString(TAG_EMAIL, null);
                editor.commit();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
