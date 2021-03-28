package com.stikom.skkm_mahasiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stikom.skkm_mahasiswa.Config.Config;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class profileFragment extends Fragment {
    ImageView imageView;
    Button logout;
    TextView txtname,txtnim,txtAbout,txtAlamat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        String nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        String alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
        String jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");
        String foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");

        txtname=(TextView)view.findViewById(R.id.txtnama);
        txtnim=(TextView)view.findViewById(R.id.txtnim);
        txtAbout=(TextView)view.findViewById(R.id.txtAbout);
        txtAlamat=(TextView)view.findViewById(R.id.txtAlamat);
        imageView=(ImageView) view.findViewById(R.id.imageView);

        txtname.setText(nama);
        txtnim.setText(nim+" - "+jurusan);
        txtAlamat.setText(alamat);


        if(foto.equals("")||foto==null ) {
            System.out.println("FOTO KOSONG");
        }else{
            Picasso.get().load(Config.URL_FOTO+foto) .transform(new CropCircleTransformation()).into(imageView);
        }

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

       SharedPreferences settings = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
       settings.edit().clear().commit();

       Intent intent = new Intent( this.getActivity(), LoginActivity.class);
       startActivity(intent);

    }
}