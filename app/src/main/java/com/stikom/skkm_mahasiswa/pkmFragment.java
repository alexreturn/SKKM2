package com.stikom.skkm_mahasiswa;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stikom.skkm_mahasiswa.BEM.DataMahsiswaActivity;
import com.stikom.skkm_mahasiswa.BEM.DetailUserActivity;
import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class pkmFragment extends Fragment {

    private Dialog customDialog;
    TextView txtname,txtnim,JumlahSKKM,JumlahPOIN;
    ImageButton btnSeminar,btnSKKM,btnBOBOT,btnMaster;
    View view;
    Button button2,button3;
    private String JSON_STRING;
    String nama_user,alamat,jurusan,nama,nimuser,foto,LEVEL,semester,dataPKM;


    TextView txtJudul,txtJenis,txtDosen,txtTgl,txtKet;
    int panjangdata=0;
    String id, judul,jenis, nim_ketua,idPembimbing,doc,email,noTelp,keterangan,tanggal;
    String status="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initCustomDialog();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        nimuser = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
        jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");
        foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");
        semester = sharedPreferences.getString(Config.semester_SHARED_PREF, "null");
        dataPKM = sharedPreferences.getString(Config.panjangdata_SHARED_PREF, "null");


        getPKM();

        if( dataPKM.equals("0")) {
            view = inflater.inflate(R.layout.fragment_pkm, container, false);
//            button2 = (Button) view.findViewById(R.id.button2);
//            button2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("http://lldikti7.ristekdikti.go.id/uploadpengumuman/Buku-Pedoman-PKM-2020.pdf"));
//                    getActivity().startActivity(i);
//
//                }
//            });
//            button3 = (Button) view.findViewById(R.id.button3);
//            button3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), daftarPKMActivity.class);
//                    startActivity(intent);
//
//                }
//            });
        }else if(dataPKM.equals("1")){
            view = inflater.inflate(R.layout.fragment_pkm_validasi, container, false);

             txtJudul=(TextView)view.findViewById(R.id.txtJudul);
             txtJenis=(TextView)view.findViewById(R.id.txtJenis);
             txtDosen=(TextView)view.findViewById(R.id.txtDosen);
             txtTgl=(TextView)view.findViewById(R.id.txtTgl);
             txtKet=(TextView)view.findViewById(R.id.txtKet);

        }else{
            view = inflater.inflate(R.layout.fragment_pkm_gagal, container, false);
        }





        return view;
    }
    private void initCustomDialog(){
        customDialog = new Dialog(getActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_tambah_data);
        customDialog.setCancelable(false);
    }


    private void showPKM() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            panjangdata= result.length();
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                System.out.println("PANJANG "+result.length());
                id = jo.getString("id");
                judul = jo.getString("judul");
                jenis = jo.getString("jenis");
                nim_ketua = jo.getString("nim_ketua");
                idPembimbing = jo.getString("idPembimbing");
                doc = jo.getString("doc");
                email = jo.getString("email");
                noTelp = jo.getString("noTelp");
                status = jo.getString("status");
                keterangan = jo.getString("keterangan");
                tanggal = jo.getString("tanggal");

            }
            if(panjangdata==1) {
                txtJudul.setText(judul);
                txtJenis.setText(jenis);
                if (idPembimbing == "0") {

                    txtDosen.setText("-");
                } else {
                    txtDosen.setText("wayan");
                }
                txtTgl.setText(tanggal);
                txtKet.setText(keterangan);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPKM(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showPKM();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_PKM+nimuser);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

}