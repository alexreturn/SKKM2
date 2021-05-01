package com.stikom.skkm_mahasiswa;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatDelegate;
        import androidx.fragment.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;
        import com.stikom.skkm_mahasiswa.BEM.DataMahsiswaActivity;
        import com.stikom.skkm_mahasiswa.Config.Config;
        import com.stikom.skkm_mahasiswa.Config.RequestHandler;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

        import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class homeFragment extends Fragment {

    TextView txtname,txtnim,JumlahSKKM,JumlahPOIN;
    ImageButton btnSeminar,btnSKKM,btnBOBOT,btnMaster;

    int panjangdata=0;

    private String JSON_STRING;
    String nama_user,alamat,jurusan,nama,nimuser,foto,LEVEL;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
         nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
         nimuser = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
         alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
         jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");
         foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");


        txtname=(TextView)view.findViewById(R.id.txtnama);
        txtnim=(TextView)view.findViewById(R.id.txtnim);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageView);

        txtname.setText(nama);
        txtnim.setText(nimuser+" - "+jurusan);
        if(foto.equals("")||foto==null ) {
            System.out.println("FOTO KOSONG");
        }else{
            Picasso.get().load(Config.URL_FOTO+foto) .transform(new CropCircleTransformation()).into(imageView);
        }
        getUser();
        getPKM();
        return view;
    }

    private void showUser() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
//            JSONArray result2 = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY2);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                nama_user = jo.getString("nama");
                alamat = jo.getString("alamat");
                jurusan = jo.getString("jurusan");
                foto = jo.getString("foto");

                txtname.setText(nama_user);
                txtnim.setText(nimuser+" - "+jurusan);


//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//               editor.putString(Config.nama_SHARED_PREF, nama_user);
//                editor.putString(Config.alamat_SHARED_PREF, alamat);
//                editor.putString(Config.jurusan_SHARED_PREF, jurusan);
//                editor.commit();
            }


//            for (int i = 0; i < result2.length(); i++) {
//                JSONObject jo = result2.getJSONObject(i);
//                String jmlskkm = jo.getString("jumlah");
//                String  jmlpoin = jo.getString("JumlahPOIN");
//
//                if(jmlpoin.equals(null) || jmlpoin.equals("null")){
//
//
//                }else{
//
//
//                }
//
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUser(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showUser();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.USER_URL+nimuser);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showPKM() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            panjangdata= result.length();


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString(Config.panjangdata_SHARED_PREF, String.valueOf(panjangdata));
                editor.commit();

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