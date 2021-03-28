package com.stikom.skkm_mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.stikom.skkm_mahasiswa.Config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UnggahActivity extends AppCompatActivity {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ListViewAdapterUser listViewAdapterUser;

    TextView txtNAMA,txtNIM,textView10;
    private String nimm;
    ImageButton btnBack;
    com.stikom.skkm_mahasiswa.BEM.adapter.ListViewAdapterBem ListViewAdapterBem;
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
        setContentView(R.layout.activity_unggah);

        SharedPreferences sharedPreferences = UnggahActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nimm = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        ImageButton btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        userJudul=findViewById(R.id.userJudul);
        textView10=findViewById(R.id.textView10);
        textView10.setText(formattedDate);
        spinnerKategori=findViewById(R.id.spinnerKategori);
        spinnerBidang=findViewById(R.id.spinnerBidang);
        spinnerPastisipasi=findViewById(R.id.spinnerPastisipasi);
        spinnerTingkatan=findViewById(R.id.spinnerTingkatan);

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String getKategori = spinnerKategori.getSelectedItem().toString();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);

                if(getKategori.equals("Ilmiah")){
                    adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Minat Bakat Dan Olahraga")){
                    adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Minat_Bakat_Dan_Olahraga, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Sosial dan Pengabdian Masyarakat")){
                    adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Sosial_dan_Pengabdian_Masyarakat, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Pengurus Oraganisasi Mahasiswa")){
                    adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Pengurus_Oraganisasi_Mahasiswa, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Kegiatan Wajib")){
                    adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Kegiatan_Wajib, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Prestasi")){
                    adapter = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Prestasi, android.R.layout.simple_spinner_item);
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
                ArrayAdapter<CharSequence>  adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);

                if(getKategori.equals("Ilmiah")){
                    adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Ilmiah, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Minat Bakat Dan Olahraga")){
                    adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Minat_Bakat_Dan_Olahraga, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Sosial dan Pengabdian Masyarakat")){
                    adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Sosial_dan_Pengabdian_Masyarakat, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Pengurus Oraganisasi Mahasiswa")){
                    adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Pengurus_Oraganisasi_Mahasiswa, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Kegiatan Wajib")){
                    adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Kegiatan_Wajib, android.R.layout.simple_spinner_item);
                }else if(getKategori.equals("Prestasi")){
                    adapter2 = ArrayAdapter.createFromResource(UnggahActivity.this, R.array.Prestasi, android.R.layout.simple_spinner_item);
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


        imageFoto=findViewById(R.id.imageFoto);
//        BitmapDrawable drawable = (BitmapDrawable) imageFoto.getDrawable();
//        storebitmap = drawable.getBitmap();
        imageFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (UnggahActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else if (UnggahActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                }else{
                    selectImage(UnggahActivity.this);
                }

            }
        });

        btnsimpan=findViewById(R.id.btnsimpan);
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userJudul.getText().toString().equals("")|| (userJudul.getText().toString().equals(null))){
                    Toast.makeText(UnggahActivity.this, "Judul SKKM harap Diisi", Toast.LENGTH_LONG).show();
                }else if (bitmap==null){
                    Toast.makeText(UnggahActivity.this, "Foto SKKM harap Diisi", Toast.LENGTH_LONG).show();
                }else{
                    simpanSKKM();
                }
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
        final String jmlhpoin= "0";
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
                                bitmap= BitmapFactory.decodeFile(picturePath);
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
}