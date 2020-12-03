package com.stikom.skkm_mahasiswa.BEM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.ListViewAdapter;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;
import com.stikom.skkm_mahasiswa.Config.adapterControl;
import com.stikom.skkm_mahasiswa.LoginActivity;
import com.stikom.skkm_mahasiswa.MainActivity;
import com.stikom.skkm_mahasiswa.R;
import com.stikom.skkm_mahasiswa.SkkmActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetailUserActivity extends AppCompatActivity {
    TextView txtNAMA,txtNIM,textView10;
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ImageButton btnBack;
    ListViewAdapter listViewAdapter;
    ArrayList<adapterControl> arrayList = new ArrayList<>();
    Spinner spinner;
    Button buttonTambah;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private Dialog customDialog;
    EditText userJudul,editTextPoin;
    Spinner spinnerKategori,spinnerBidang,spinnerPastisipasi,spinnerTingkatan;
    ImageView imageFoto;
    Button btnsimpan;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        SharedPreferences sharedPreferences = DetailUserActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nimm = sharedPreferences.getString(Config.NIM_DETAIL_SHARED_PREF, "null");
        String namaa = sharedPreferences.getString(Config.NAMA_DETAIL_SHARED_PREF, "null");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        txtNAMA = (TextView) findViewById(R.id.txtNAMA);
        txtNIM = (TextView) findViewById(R.id.txtNIM);
        txtNAMA.setText(namaa);
        txtNIM.setText(nimm);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String text = spinner.getSelectedItem().toString();
                if (text.equals("Semua")) {
                    listViewAdapter.filter("a");
                    lispiu.clearTextFilter();
                } else {
                    listViewAdapter.filter(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        lispiu = (ListView)findViewById(R.id.listview);
        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent( DetailUserActivity.this, DataMahsiswaActivity.class);
                startActivity(intent);
            }
        });

        buttonTambah=(Button) findViewById(R.id.buttonTambah);
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();

            }
        });

        getJSONSKKM();

        initCustomDialog();
    }
    private void initCustomDialog(){
        customDialog = new Dialog(DetailUserActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_tambah_data);
        customDialog.setCancelable(true);

        userJudul=customDialog.findViewById(R.id.userJudul);
        editTextPoin=customDialog.findViewById(R.id.editTextPoin);

        spinnerKategori=customDialog.findViewById(R.id.spinnerKategori);
        spinnerBidang=customDialog.findViewById(R.id.spinnerBidang);
        spinnerPastisipasi=customDialog.findViewById(R.id.spinnerPastisipasi);
        spinnerTingkatan=customDialog.findViewById(R.id.spinnerTingkatan);

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String getKategori = spinnerKategori.getSelectedItem().toString();
                ArrayAdapter<CharSequence>  adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);

                if(getKategori.equals("Ilmiah")){
                    adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Minat Bakat Dan Olahraga")){
                    adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Minat_Bakat_Dan_Olahraga, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Sosial dan Pengabdian Masyarakat")){
                    adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Sosial_dan_Pengabdian_Masyarakat, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Pengurus Oraganisasi Mahasiswa")){
                    adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Pengurus_Oraganisasi_Mahasiswa, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Kegiatan Wajib")){
                    adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Kegiatan_Wajib, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Prestasi")){
                    adapter = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Prestasi, android.R.layout.simple_spinner_item);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBidang.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerBidang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String getKategori = spinnerBidang.getSelectedItem().toString();
                ArrayAdapter<CharSequence>  adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);

                if(getKategori.equals("Ilmiah")){
                    adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Minat Bakat Dan Olahraga")){
                    adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Minat_Bakat_Dan_Olahraga, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Sosial dan Pengabdian Masyarakat")){
                    adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Sosial_dan_Pengabdian_Masyarakat, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Pengurus Oraganisasi Mahasiswa")){
                    adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Pengurus_Oraganisasi_Mahasiswa, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Kegiatan Wajib")){
                    adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Kegiatan_Wajib, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Prestasi")){
                    adapter2 = ArrayAdapter.createFromResource(DetailUserActivity.this, R.array.Prestasi, android.R.layout.simple_spinner_item);
                }
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPastisipasi.setAdapter(adapter2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        textView10=customDialog.findViewById(R.id.textView10);
        textView10.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
               showDateDialog();
            }
        });


        imageFoto=customDialog.findViewById(R.id.imageFoto);
        imageFoto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                if (DetailUserActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        btnsimpan=customDialog.findViewById(R.id.btnsimpan);
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
//                TambahLogTanaman();
            }
        });


    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView10.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showSKKM()  {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String date=jo.getString(Config.KEY_SKKM_tanggal);
                SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date newDate= null;
                try {
                    newDate = spf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                spf= new SimpleDateFormat("dd-MM-yyyy");
                date = spf.format(newDate);
                System.out.println(date);

                adapterControl app = new adapterControl(jo.getString(Config.KEY_SKKM_id),
                        jo.getString(Config.KEY_SKKM_judul),
                        jo.getString(Config.KEY_SKKM_deskripsi),
                        jo.getString(Config.KEY_SKKM_image),
                        jo.getString(Config.KEY_SKKM_seminarDate),
                        date,
                        jo.getString(Config.KEY_SKKM_poin),
                        jo.getString(Config.KEY_SKKM_kategori));
                arrayList.add(app);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listViewAdapter = new ListViewAdapter(this, arrayList);
        lispiu.setAdapter(listViewAdapter);

    }
    private void getJSONSKKM(){
        SharedPreferences sharedPreferences = DetailUserActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String nim = sharedPreferences.getString(Config.NIM_DETAIL_SHARED_PREF, "null");
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(DetailUserActivity.this,"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading3.dismiss();
                JSON_STRING = s;
                showSKKM();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.SKKM_URL+nim);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");

            imageFoto.setImageBitmap(bitmap);


        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         bmp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();


        long lengthbmp = imageBytes.length;

        System.out.println("Ukuran 6"+lengthbmp);

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
                Toast.makeText(DetailUserActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(DetailUserActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}