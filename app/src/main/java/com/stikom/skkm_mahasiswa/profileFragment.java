package com.stikom.skkm_mahasiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stikom.skkm_mahasiswa.Config.Config;

public class profileFragment extends Fragment {

    Button logout;
    TextView txtname,txtnim,txtAbout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        String nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        String alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
        String jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");

        txtname=(TextView)view.findViewById(R.id.txtnama);
        txtnim=(TextView)view.findViewById(R.id.txtnim);
        txtAbout=(TextView)view.findViewById(R.id.txtAbout);

        txtname.setText(nama);
        txtnim.setText(nim+" - "+jurusan);

        logout=(Button)view.findViewById(R.id.btnlogout);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });

        txtAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }



   public void logout(){
       SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
       editor.putString(Config.NIM_SHARED_PREF, "");
       editor.putString(Config.password_SHARED_PREF, "");
       editor.commit();

       Intent intent = new Intent( this.getActivity(), LoginActivity.class);
       startActivity(intent);

    }
}