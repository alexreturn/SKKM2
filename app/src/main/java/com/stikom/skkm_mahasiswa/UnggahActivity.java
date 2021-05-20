package com.stikom.skkm_mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.util.Output;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stikom.skkm_mahasiswa.BEM.DataMahsiswaActivity;
import com.stikom.skkm_mahasiswa.BEM.DetailUserActivity;
import com.stikom.skkm_mahasiswa.BEM.adapter.ListViewAdapterBem;
import com.stikom.skkm_mahasiswa.BEM.adapter.ListViewAdapterUser;
import com.stikom.skkm_mahasiswa.BEM.adapter.adapterControlBem;
import com.stikom.skkm_mahasiswa.BEM.adapter.adapterControlUser;
import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.ListViewAdapter;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;
import com.stikom.skkm_mahasiswa.Config.adapterControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UnggahActivity extends AppCompatActivity implements TextWatcher {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ListViewAdapterUser listViewAdapterUser;
    String custom="false";
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    ArrayAdapter<String> adapter2;
    TextView txtNAMA,txtNIM,textView10,namaJenis,namaSubJenis,namaTingkat,jumlahPoin;
    private String nimm;
    ImageButton btnBack;
    com.stikom.skkm_mahasiswa.BEM.adapter.ListViewAdapterBem ListViewAdapterBem;

    ArrayList<String> arrayListSpinBidang = null;
    ArrayList<String> arrayListSpinKegiatan = null;
    ArrayList<String> arrayListSpinTingkat = null;

    ArrayList<String> arrayListSpinBidangID = null;
    ArrayList<String> arrayListSpinKegiatanID = null;
    ArrayList<String> arrayListSpinTingkatID = null;


    ArrayList<String> arrayListSpin = null;
    ArrayList<String> arrayListSpinID = null;

//    private ArrayList<String> arrayListSpin;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private Dialog customDialog;
    EditText userJudul,editTextPoin;
    Spinner spinnerKedudukan, spinnerKegiatan,spinnerTingkat,spinnerBidang;
    ImageView imageFoto;
    Button btnsimpan;
    LinearLayout LinearText,LinearSpinner,linePKM;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap,storebitmap;

    private ArrayList<String> arrayListrrr;
    private AutoCompleteTextView pencarianJudul;
    String INIidJenis= null , INIidSubJenis= null ,INIidKedudukan= null ,INIidTingkat= null ,INIidkegiatan= null ;


    String idBidang="";
    String idKegiatan="";
    String idTingkat="";

    String JenisFile="foto";
    String inikegiatan="";

    //////////////////////////////PKM///////////////////
    TextView filename;
    EditText txtjudul,txtanggota,txtanggota1,txtanggota2,txtanggota3;
    Spinner spinnerJenis;
    Button btnDokument;
    String nama,nimuser;
    LinearLayout ViewKelompok;
    int widthkelompok=150;
    int jumlahklik=0;
    String TampungAngota="";
    private static final int FILE_SELECT_CODE = 0;
    String URLFileName,NAMAfilenamess;
    ProgressDialog dialog = null;
    int serverResponseCode = 0;
    ////PKM////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unggah);

        SharedPreferences sharedPreferences = UnggahActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nimm = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        nimuser = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        ViewKelompok=(LinearLayout) findViewById(R.id.ViewKelompok);
        txtjudul=(EditText)findViewById(R.id.txtjudul);

        filename=(TextView) findViewById(R.id.filename);
        txtanggota=(EditText)findViewById(R.id.txtanggota);
        txtanggota1=(EditText)findViewById(R.id.txtanggota1);
        txtanggota2=(EditText)findViewById(R.id.txtanggota2);
        txtanggota3=(EditText)findViewById(R.id.txtanggota3);
        txtanggota1.setVisibility(View.INVISIBLE);
        txtanggota2.setVisibility(View.INVISIBLE);
        txtanggota3.setVisibility(View.INVISIBLE);
        spinnerJenis=(Spinner)findViewById(R.id.spinnerJenis);
        btnDokument=(Button)findViewById(R.id.btnDokument);
        btnDokument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        txtanggota.setText(nimuser);
        txtanggota.setEnabled(false);

        ImageButton btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        spinnerKedudukan = findViewById(R.id.spinnerKedudukan);
        spinnerKedudukan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                try{

                    Object item = parent.getItemAtPosition(pos);
                    System.out.println("pilihan "+item);
                    String currentString = String.valueOf(item);
                    String[] separated = currentString.split("-");

                    String iniid = arrayListSpinID.get(pos);//This will be the student id.
                    INIidKedudukan= iniid;
                    getBobot(iniid);
                    System.out.println("pisah "+iniid);
                }catch (Exception asd){

                    System.out.println("error coy "+asd);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pencarianJudul=findViewById(R.id.autoCompleteTextView);
        pencarianJudul.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    pencarianJudul.showDropDown();

            }
        });

        pencarianJudul.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pencarianJudul.showDropDown();
                return false;
            }
        });
        pencarianJudul.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                namaJenis.setText(list.get(pos).get(Config.TAG_EMP_KEGIATAN_namaJenis));
                namaSubJenis.setText(list.get(pos).get(Config.TAG_EMP_KEGIATAN_namaSubJenis));
                namaTingkat.setText(list.get(pos).get(Config.TAG_EMP_KEGIATAN_namaTingkat));

                String idjenusnya=list.get(pos).get(Config.TAG_EMP_KEGIATAN_idJenis);
                String idsubjenusnya=list.get(pos).get(Config.TAG_EMP_KEGIATAN_idSubJenis);
                String idtingkatnya=list.get(pos).get(Config.TAG_EMP_KEGIATAN_idTingkat);
                String idkegiatanya=list.get(pos).get(Config.TAG_EMP_KEGIATAN_id);
                custom=list.get(pos).get(Config.TAG_EMP_KEGIATAN_custom);
                System.out.println("CUSTOM coy "+custom);
                cekStatus();
                INIidJenis= idjenusnya;
                INIidSubJenis= idsubjenusnya;
                INIidTingkat= idtingkatnya ;

                INIidkegiatan=idkegiatanya;

                getKedudukan(idjenusnya,idsubjenusnya);
                Toast.makeText(UnggahActivity.this, adapter2.getItem(pos).toString()+list.get(pos).get(Config.TAG_EMP_KEGIATAN_tanggalMulai), Toast.LENGTH_SHORT).show();
            }
        });

        textView10=findViewById(R.id.textView10);
        namaJenis=findViewById(R.id.namaJenis);
        namaSubJenis=findViewById(R.id.namaSubJenis);
        namaTingkat=findViewById(R.id.namaTingkat);
        jumlahPoin=findViewById(R.id.jumlahPoin);

        LinearText=findViewById(R.id.LinearText);
        LinearSpinner=findViewById(R.id.LinearSpinner);
        linePKM=findViewById(R.id.linePKM);


        spinnerBidang=findViewById(R.id.spinnerBidang);
        spinnerKegiatan=findViewById(R.id.spinnerKegiatan);
        spinnerTingkat=findViewById(R.id.spinnerTingkat);

        spinnerBidang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                try{
                    String iniid = arrayListSpinBidangID.get(pos);
                    getSubBidang(iniid);
                    System.out.println("INI Bidang coy "+iniid);
                    idBidang=iniid;
                    getKedudukan(idBidang,idKegiatan);
                }catch (Exception asd){
                    System.out.println("error coy "+asd);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerKegiatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                try{
                    String kegiatan = arrayListSpinKegiatan.get(pos);
                    String iniid = arrayListSpinKegiatanID.get(pos);
                    idKegiatan=iniid;
                    inikegiatan=kegiatan;
                    System.out.println(kegiatan+" INI Kegiatan coy "+iniid);
                    if(idKegiatan=="6" || idKegiatan.equals("6")){
                        ViewGroup.LayoutParams params3 = linePKM.getLayoutParams();
                        params3.height =   LinearLayout.LayoutParams.WRAP_CONTENT;
                        linePKM.setLayoutParams(params3);
                    }else{
                        ViewGroup.LayoutParams params3 = linePKM.getLayoutParams();
                        params3.height =  0;
                        linePKM.setLayoutParams(params3);
                    }

                    getKedudukan(idBidang,idKegiatan);
                }catch (Exception asd){
                    System.out.println("error coy "+asd);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerTingkat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                try{
                    String iniid = arrayListSpinTingkatID.get(pos);
                    idTingkat=iniid;
                    System.out.println("INI TINGKAT coy "+iniid);
                    getKedudukan(idBidang,idKegiatan);

                }catch (Exception asd){
                    System.out.println("error coy "+asd);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        LinearSpinner.setVisibility(LinearLayout.VISIBLE);

        imageFoto=findViewById(R.id.imageFoto);
        imageFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (UnggahActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else if (UnggahActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                    }else{
                        selectImage(UnggahActivity.this);
                    }
                }

            }
        });

        btnsimpan=findViewById(R.id.btnsimpan);
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(INIidkegiatan.equals("")|| (INIidkegiatan.equals(null))){
                    Toast.makeText(UnggahActivity.this, "Judul SKKM harap Diisi", Toast.LENGTH_LONG).show();
                }else if (bitmap==null) {
                    Toast.makeText(UnggahActivity.this, "Foto SKKM harap Diisi", Toast.LENGTH_LONG).show();
                }else if (jumlahPoin.getText().toString().equals("0")){
                    AlertDialog alertDialog = new AlertDialog.Builder(UnggahActivity.this).create();
                    alertDialog.setTitle("Data SKKM salah");
                    alertDialog.setMessage("Silahkan cek kembali data SKKM!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Toast.makeText(UnggahActivity.this, "Data SKKM salah", Toast.LENGTH_LONG).show();
                }else{
                    if((custom=="true" || custom.equals("true")) && !idKegiatan.equals("13") ) {
                        System.out.println(inikegiatan+"SATU");
                        simpanSKKMCustom();
                    }else if(idKegiatan=="6" || idKegiatan.equals("6")){
                        simpanSKKMCustomPKM();
                        System.out.println(inikegiatan+"DUA");
//                        simpanPKM();
                    }else{
                        System.out.println("TIGA");
                        simpanSKKM();
                    }

                }
            }
        });
        cekStatus();

        getJSONKegiatan();
    }

     public void cekStatus(){
        if(custom=="true" || custom.equals("true") ){
            ViewGroup.LayoutParams params = LinearText.getLayoutParams();
            params.height =  0;
            LinearText.setLayoutParams(params);

            ViewGroup.LayoutParams params2 = LinearSpinner.getLayoutParams();
            params2.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearSpinner.setLayoutParams(params2);
            getBidang();
            getTingkat();
        }else{
            ViewGroup.LayoutParams params = LinearText.getLayoutParams();
            params.height =   LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearText.setLayoutParams(params);

            ViewGroup.LayoutParams params2 = LinearSpinner.getLayoutParams();
            params2.height =  0;
            LinearSpinner.setLayoutParams(params2);
        }

     if(idKegiatan=="6" || idKegiatan.equals("6")){
         ViewGroup.LayoutParams params3 = linePKM.getLayoutParams();
         params3.height =   LinearLayout.LayoutParams.WRAP_CONTENT;
         linePKM.setLayoutParams(params3);
     }else{
         ViewGroup.LayoutParams params3 = linePKM.getLayoutParams();
         params3.height =  0;
         linePKM.setLayoutParams(params3);
     }
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Method ini dipanggil sebelum edittext selesai diubah
        Toast.makeText(getApplicationContext(),"Text Belum Diubah", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Method ini dipanggil saat text pada edittext sedang diubah
//        Output.setText("Data Output : "+pencarianJudul.getText());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //Method ini dipanggil setelah edittext selesai diubah
        Toast.makeText(getApplicationContext(),"Text Selesai Diubah", Toast.LENGTH_SHORT).show();
    }
//////////////// get Kegiatan //////////////////



    private void showKegiatan()  {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);


            arrayListrrr = new ArrayList<String>();
             for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.KEY_EMP_KEGIATAN_id);
                String jenisUser = jo.getString(Config.KEY_EMP_KEGIATAN_jenisUser);
                String idUser = jo.getString(Config.KEY_EMP_KEGIATAN_idUser);
                String idJenis = jo.getString(Config.KEY_EMP_KEGIATAN_idJenis);
                String idSubJenis = jo.getString(Config.KEY_EMP_KEGIATAN_idSubJenis);
                String idTingkat = jo.getString(Config.KEY_EMP_KEGIATAN_idTingkat);
                String namaKegiatan = jo.getString(Config.KEY_EMP_KEGIATAN_namaKegiatan);
                String tanggalMulai = jo.getString(Config.KEY_EMP_KEGIATAN_tanggalMulai);
                String tanggalSelesai = jo.getString(Config.KEY_EMP_KEGIATAN_tanggalSelesai);
                String tanggalPembuatan = jo.getString(Config.KEY_EMP_KEGIATAN_tanggalPembuatan);
                String keterangan = jo.getString(Config.KEY_EMP_KEGIATAN_keterangan);
                String namaJenis = jo.getString(Config.KEY_EMP_KEGIATAN_namaJenis);
                String namaSubJenis = jo.getString(Config.KEY_EMP_KEGIATAN_namaSubJenis);
                String namaTingkat = jo.getString(Config.KEY_EMP_KEGIATAN_namaTingkat);
                 String namacustom = jo.getString(Config.KEY_EMP_KEGIATAN_custom);


                 HashMap<String, String> employees = new HashMap<>();
                 employees.put(Config.TAG_EMP_KEGIATAN_id, id);
                 employees.put(Config.TAG_EMP_KEGIATAN_jenisUser, jenisUser);
                 employees.put(Config.TAG_EMP_KEGIATAN_idUser, idUser);
                 employees.put(Config.TAG_EMP_KEGIATAN_idJenis, idJenis);
                 employees.put(Config.TAG_EMP_KEGIATAN_idSubJenis, idSubJenis);
                 employees.put(Config.TAG_EMP_KEGIATAN_idTingkat, idTingkat);
                 employees.put(Config.TAG_EMP_KEGIATAN_namaKegiatan, namaKegiatan);
                 employees.put(Config.TAG_EMP_KEGIATAN_tanggalMulai, tanggalMulai);
                 employees.put(Config.TAG_EMP_KEGIATAN_tanggalSelesai, tanggalSelesai);
                 employees.put(Config.TAG_EMP_KEGIATAN_tanggalPembuatan, tanggalPembuatan);
                 employees.put(Config.TAG_EMP_KEGIATAN_keterangan, keterangan);
                 employees.put(Config.TAG_EMP_KEGIATAN_namaJenis, namaJenis);
                 employees.put(Config.TAG_EMP_KEGIATAN_namaSubJenis, namaSubJenis);
                 employees.put(Config.TAG_EMP_KEGIATAN_namaTingkat, namaTingkat);
                 employees.put(Config.TAG_EMP_KEGIATAN_custom, namacustom);
                 list.add(employees);

                System.out.println("KEGIATAHBBBB == "+namaKegiatan);
                arrayListrrr.add(namaKegiatan);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrayListrrr);
        pencarianJudul.setAdapter(adapter2);
        pencarianJudul.setThreshold(1);
//        pencarianJudul.setTextColor(Color.WHITE);


    }

    private void getJSONKegiatan(){
        SharedPreferences sharedPreferences = UnggahActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(UnggahActivity.this,"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading3.dismiss();
                JSON_STRING = s;
                showKegiatan();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.GET_KEGIATAN);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
//////////////// SIMPAN SKKM //////////////////

    public void simpanSKKM() {

        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UnggahActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                finish();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("idKegiatan", INIidkegiatan);
                params.put("idKedudukan", INIidKedudukan);

                params.put("idJenis", INIidJenis);
                params.put("idSubJenis", INIidSubJenis);
                params.put("idTingkat", INIidTingkat);

                params.put("nim", nimm);

                params.put("poin", jumlahPoin.getText().toString());
                if(bitmap==null){
                    params.put("sertifikat",null);
                }else{
                    params.put("sertifikat", getStringImage(bitmap));
                }

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.SIMPANSKKM, params);
                return res;
            }
        }
        TambahData ae = new TambahData();
        ae.execute();

    }
    public void simpanSKKMCustom() {

        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UnggahActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                finish();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("idKegiatan", INIidkegiatan);
                params.put("idKedudukan", INIidKedudukan);

                params.put("idJenis", idBidang);
                params.put("idSubJenis", idKegiatan);
                params.put("idTingkat", idTingkat);

                params.put("nim", nimm);
                params.put("poin", jumlahPoin.getText().toString());
                if(bitmap==null){
                    params.put("sertifikat",null);
                }else{
                    params.put("sertifikat", getStringImage(bitmap));
                }

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.SIMPANSKKM, params);
                return res;
            }
        }
        TambahData ae = new TambahData();
        ae.execute();

    }
    public void simpanSKKMCustomPKM() {
        TampungAngota="";
        final String judulPKM = txtjudul.getText().toString();
        final String anggota = txtanggota.getText().toString();
        final String anggota1 = txtanggota1.getText().toString();
        final String anggota2 = txtanggota2.getText().toString();
        final String anggota3 = txtanggota3.getText().toString();
        final String spinnerJenisPKM = spinnerJenis.getSelectedItem().toString();

        TampungAngota=TampungAngota+anggota;
        if(anggota1!="" ||anggota1.length()==9 ){
            TampungAngota=TampungAngota+","+anggota1;
        }
        if(anggota2!="" ||anggota2.length()==9 ){
            TampungAngota=TampungAngota+","+anggota2;
        }
        if(anggota3!="" ||anggota3.length()==9 ){
            TampungAngota=TampungAngota+","+anggota3;
        }
        if(judulPKM.equals("")){
            Toast.makeText(UnggahActivity.this, "Judul PKM tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota.length() <9){
            Toast.makeText(UnggahActivity.this, "NIM tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota1.length() <9 && jumlahklik==1){
            Toast.makeText(UnggahActivity.this, "NIM Anggota1 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota2.length() <9 && jumlahklik==2){
            Toast.makeText(UnggahActivity.this, "NIM Anggota2 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota3.length() <9 && jumlahklik==3){
            Toast.makeText(UnggahActivity.this, "NIM Anggota3 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(NAMAfilenamess=="" || NAMAfilenamess==null) {
            Toast.makeText(UnggahActivity.this, "Harap pilih file PKM", Toast.LENGTH_LONG).show();
        }else {
            class TambahData extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(UnggahActivity.this, "Proses Kirim Data...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    loading.dismiss();
                    simpanPKM();
//                finish();
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("idKegiatan", INIidkegiatan);
                    params.put("idKedudukan", INIidKedudukan);

                    params.put("idJenis", idBidang);
                    params.put("idSubJenis", idKegiatan);
                    params.put("idTingkat", idTingkat);

                    params.put("nim", nimm);
                    params.put("poin", jumlahPoin.getText().toString());
                    if (bitmap == null) {
                        params.put("sertifikat", null);
                    } else {
                        params.put("sertifikat", getStringImage(bitmap));
                    }

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.SIMPANSKKM, params);
                    return res;
                }
            }
            TambahData ae = new TambahData();
            ae.execute();
        }
    }
////////////////////////////////////////GET KEDUDUKAN /////////////////////////////////////////////////

    private void showKedudukan() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            arrayListSpin = new ArrayList<String>();
            arrayListSpinID = new ArrayList<String>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String namaKedudukan = jo.getString("namaKedudukan");

                arrayListSpin.add(namaKedudukan);
                arrayListSpinID.add(id);
            }
            spinnerKedudukan.setAdapter(new ArrayAdapter<String>(UnggahActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

            try {
               }catch (Exception asds){}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getKedudukan(final String idJenis, final String idSubJenis){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showKedudukan();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_KEDUDUKAN+idJenis+"&idSubJenis="+idSubJenis);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
////////////////////////////////////////GET BOBOT /////////////////////////////////////////////////

    private void showBobot() {
        jumlahPoin.setText("0");
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String poin = jo.getString("poin");

                jumlahPoin.setText(poin.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBobot(final String idkedudukan){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showBobot();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                if(custom=="true" || custom.equals("true") ) {
                     s = rh.sendGetRequest(Config.GET_BOBOT+idBidang+"&idSubJenis="+idKegiatan+"&idKedudukan="+idkedudukan+"&idTingkat="+idTingkat);
                }else{
                     s = rh.sendGetRequest(Config.GET_BOBOT+INIidJenis+"&idSubJenis="+INIidSubJenis+"&idKedudukan="+idkedudukan+"&idTingkat="+INIidTingkat);
                }
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showBidang() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            arrayListSpinBidang = new ArrayList<String>();
            arrayListSpinBidangID = new ArrayList<String>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String namaJenis = jo.getString("namaJenis");

                arrayListSpinBidang.add(namaJenis);
                arrayListSpinBidangID.add(id);
            }
            spinnerBidang.setAdapter(new ArrayAdapter<String>(UnggahActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpinBidang));

            try {
            }catch (Exception asds){}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBidang(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showBidang();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.getJenisKegiatan);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showSubBidang() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            arrayListSpinKegiatan = new ArrayList<String>();
            arrayListSpinKegiatanID = new ArrayList<String>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String namaJenis = jo.getString("namaSubJenis");

                arrayListSpinKegiatan.add(namaJenis);
                arrayListSpinKegiatanID.add(id);
            }
            spinnerKegiatan.setAdapter(new ArrayAdapter<String>(UnggahActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpinKegiatan));

            try {
            }catch (Exception asds){}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSubBidang(final String idKegiatan){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showSubBidang();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.getJenisSubKegiatan+idKegiatan);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showTingkat() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            arrayListSpinTingkat = new ArrayList<String>();
            arrayListSpinTingkatID = new ArrayList<String>();
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String namaJenis = jo.getString("namaTingkat");

                arrayListSpinTingkat.add(namaJenis);
                arrayListSpinTingkatID.add(id);
            }
            spinnerTingkat.setAdapter(new ArrayAdapter<String>(UnggahActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpinTingkat));

            try {
            }catch (Exception asds){}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTingkat(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showTingkat();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.getTingkat);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

////////////////////////////  GET IMAGE //////////////////////////////////////////////////////////////////////////

    private void selectImage(Context context) {
        JenisFile="foto";
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
System.out.println("INI APA? "+requestCode+" DAN "+resultCode+" DANINI "+data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null && (JenisFile=="foto" ||JenisFile.equals("foto"))) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        imageFoto.setImageBitmap(bitmap);
                    }else{
                        String URLFileName2 = data.getData().getLastPathSegment();
                        URLFileName=URLFileName2.substring(URLFileName2.lastIndexOf(":")+1);
                        NAMAfilenamess=URLFileName2.substring(URLFileName2.lastIndexOf("/")+1);
                        filename.setText(NAMAfilenamess);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bitmap= BitmapFactory.decodeFile(picturePath);
                                System.out.println("Ukuran 6"+picturePath);
                                imageFoto.setImageBitmap(bitmap);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
    public String getStringImage(Bitmap bmp) {

        JenisFile="foto";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         bmp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();


        long lengthbmp = imageBytes.length;


        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(UnggahActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(UnggahActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void slideDown(View view){
        widthkelompok=widthkelompok+150;
        jumlahklik=jumlahklik+1;
        if(jumlahklik==1){
            txtanggota1.setVisibility(View.VISIBLE);
            ViewKelompok.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,widthkelompok));
        }else if(jumlahklik==2){
            txtanggota2.setVisibility(View.VISIBLE);
            ViewKelompok.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,widthkelompok));
        }else if(jumlahklik==3){
            txtanggota3.setVisibility(View.VISIBLE);
            ViewKelompok.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,widthkelompok));
        }else{

        }
    }
    public void onSlideViewButtonClick(View view) {
        slideDown(ViewKelompok);
    }

    private void showFileChooser() {

        JenisFile="file";
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, FILE_SELECT_CODE);
    }
    public void simpanPKM() {
        TampungAngota="";
        final String judulPKM = txtjudul.getText().toString();
        final String anggota = txtanggota.getText().toString();
        final String anggota1 = txtanggota1.getText().toString();
        final String anggota2 = txtanggota2.getText().toString();
        final String anggota3 = txtanggota3.getText().toString();
        final String spinnerJenisPKM = spinnerJenis.getSelectedItem().toString();

        TampungAngota=TampungAngota+anggota;
        if(anggota1!="" ||anggota1.length()==9 ){
            TampungAngota=TampungAngota+","+anggota1;
        }
        if(anggota2!="" ||anggota2.length()==9 ){
            TampungAngota=TampungAngota+","+anggota2;
        }
        if(anggota3!="" ||anggota3.length()==9 ){
            TampungAngota=TampungAngota+","+anggota3;
        }
        if(judulPKM.equals("")){
            Toast.makeText(UnggahActivity.this, "Judul PKM tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota.length() <9){
            Toast.makeText(UnggahActivity.this, "NIM tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota1.length() <9 && jumlahklik==1){
            Toast.makeText(UnggahActivity.this, "NIM Anggota1 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota2.length() <9 && jumlahklik==2){
            Toast.makeText(UnggahActivity.this, "NIM Anggota2 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota3.length() <9 && jumlahklik==3){
            Toast.makeText(UnggahActivity.this, "NIM Anggota3 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(NAMAfilenamess=="" || NAMAfilenamess==null){
            Toast.makeText(UnggahActivity.this, "Harap pilih file PKM", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(UnggahActivity.this, "OK" + TampungAngota, Toast.LENGTH_LONG).show();
            class TambahData extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
//                    loading = ProgressDialog.show(daftarPKMActivity.this, "Proses Kirim Data...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    dialog = ProgressDialog.show(UnggahActivity.this, "", "Uploading file...", true);

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                }
                            });

                            uploadFile(URLFileName);

                        }
                    }).start();
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nim_ketua", nimuser);
                    params.put("judul", judulPKM);
                    params.put("jenis", spinnerJenisPKM);
                    params.put("doc", NAMAfilenamess);
                    params.put("anggota", TampungAngota);


                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.simpanPKMproposal, params);
                    return res;
                }
            }
            TambahData ae = new TambahData();
            ae.execute();
        }
    }
    @SuppressLint("LongLogTag")
    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
            return 0;
        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Config.upLoadServerUri);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
                        + fileName +   lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            Intent i =new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            Toast.makeText(UnggahActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {


                    }
                });


            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }
            return serverResponseCode;

        } // End else block
    }
}