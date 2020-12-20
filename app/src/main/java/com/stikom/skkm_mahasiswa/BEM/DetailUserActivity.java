package com.stikom.skkm_mahasiswa.BEM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.stikom.skkm_mahasiswa.BEM.adapter.ListViewAdapterBem;
import com.stikom.skkm_mahasiswa.BEM.adapter.adapterControlBem;
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
    private String JSON_STRING,ID_REF,nimm;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ImageButton btnBack;
    ListViewAdapterBem ListViewAdapterBem;
    ArrayList<adapterControlBem> arrayList = new ArrayList<>();
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
    private static final int REQUEST_WRITE_PERMISSION = 786;

    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap,storebitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        SharedPreferences sharedPreferences = DetailUserActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nimm = sharedPreferences.getString(Config.NIM_DETAIL_SHARED_PREF, "null");
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
                if(arrayList.isEmpty() & text.equals("Semua") ){

                    ListViewAdapterBem.filter("");
                }else{
                    if (text.equals("Semua")) {
                        ListViewAdapterBem.filter("");
                        lispiu.clearTextFilter();
                    } else {
                        ListViewAdapterBem.filter(text);
                    }
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

                finish();
            }
        });

        buttonTambah=(Button) findViewById(R.id.buttonTambah);
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();

            }
        });


        initCustomDialog();
        getJSONSKKM();
    }
    private void initCustomDialog(){
        customDialog = new Dialog(DetailUserActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_tambah_data);
        customDialog.setCancelable(true);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        userJudul=customDialog.findViewById(R.id.userJudul);
        editTextPoin=customDialog.findViewById(R.id.editTextPoin);
        textView10=customDialog.findViewById(R.id.textView10);
        textView10.setText(formattedDate);
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



        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDateDialog();
            }
        });


        imageFoto=customDialog.findViewById(R.id.imageFoto);
//        BitmapDrawable drawable = (BitmapDrawable) imageFoto.getDrawable();
//        storebitmap = drawable.getBitmap();
        imageFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (DetailUserActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else if (DetailUserActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                }else{
                    selectImage(DetailUserActivity.this);
                }

            }
        });

        btnsimpan=customDialog.findViewById(R.id.btnsimpan);
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userJudul.getText().toString().equals("")|| (userJudul.getText().toString().equals(null))){
                    Toast.makeText(DetailUserActivity.this, "Judul SKKM harap Diisi", Toast.LENGTH_LONG).show();
                }else if(textView10.getText().toString().equals("")|| (textView10.getText().toString().equals(null))){
                    Toast.makeText(DetailUserActivity.this, "Point Harap Diisi", Toast.LENGTH_LONG).show();
                }else if(textView10.getText().toString().equals("")|| (textView10.getText().toString().equals(null))) {
                    Toast.makeText(DetailUserActivity.this, "Point Harap Diisi", Toast.LENGTH_LONG).show();
                }else if (bitmap==null){
                    Toast.makeText(DetailUserActivity.this, "Foto SKKM harap Diisi", Toast.LENGTH_LONG).show();
                }else{
                    customDialog.dismiss();
                    simpanSKKM();
                }
            }
        });
    }

    private void selectImage(Context context) {
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

    public void simpanSKKM() {
        final String judulskkm = userJudul.getText().toString();
        final String jmlhpoin= editTextPoin.getText().toString();
        final String tglnya =textView10.getText().toString();
        final String kategorinya = spinnerKategori.getSelectedItem().toString();
        final String bidangnya = spinnerBidang.getSelectedItem().toString();
        final String partisipasinya = spinnerPastisipasi.getSelectedItem().toString();
        final String tingkatnya = spinnerTingkatan.getSelectedItem().toString();
        class TambahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(DetailUserActivity.this, "Proses Kirim Data...", "Wait...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
//                Intent i =new Intent(getApplicationContext(),DetailUserActivity.class);
//                startActivity(i);
                finish();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("nim_user", nimm);
                params.put("judul", judulskkm);
                params.put("poin", jmlhpoin);
                params.put("seminarDate", tglnya);
                params.put("kategori", kategorinya);
                params.put("bidang", bidangnya);
                params.put("partisipasi", partisipasinya);
                params.put("tingkat", tingkatnya);
                if(bitmap==null){
                    params.put("image",null);
                }else{
                    params.put("image", getStringImage(bitmap));
                }

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.SIMPANSKKM, params);
                return res;
            }
        }
        TambahData ae = new TambahData();
        ae.execute();

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

                adapterControlBem app = new adapterControlBem(jo.getString(Config.KEY_SKKM_id),
                        jo.getString(Config.KEY_SKKM_judul),
                        jo.getString(Config.KEY_SKKM_deskripsi),
                        jo.getString(Config.KEY_SKKM_image),
                        jo.getString(Config.KEY_SKKM_seminarDate),
                        date,
                        jo.getString(Config.KEY_SKKM_poin),
                        jo.getString(Config.KEY_SKKM_kategori),
                        jo.getString(Config.KEY_SKKM_status));
                arrayList.add(app);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListViewAdapterBem = new ListViewAdapterBem(this, arrayList);
        lispiu.setAdapter(ListViewAdapterBem);

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
                s = rh.sendGetRequest(Config.getSKKMbem+nim);
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

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                       bitmap = (Bitmap) data.getExtras().get("data");

                        imageFoto.setImageBitmap(bitmap);
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
                                bitmap=BitmapFactory.decodeFile(picturePath);
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