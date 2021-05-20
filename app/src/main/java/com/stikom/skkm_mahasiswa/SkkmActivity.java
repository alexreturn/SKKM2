package com.stikom.skkm_mahasiswa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.ListViewAdapter;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;
import com.stikom.skkm_mahasiswa.Config.adapterControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SkkmActivity extends AppCompatActivity {

    ListView lispiu;
    JSONArray result;
    String  jmlpoin;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ImageButton btnBack;
    ListViewAdapter listViewAdapter;
    ArrayList<adapterControl> arrayList = new ArrayList<>();
    Spinner spinner;
    String formattedDate;
    private List<adapterControl> appList;
    ArrayList<HashMap<String, String>> arrayListPrint = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayListSpin = null;

    Button generatePDFbtn;
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    Bitmap bmpcheck, scaledbmpcheck;
    private static final int PERMISSION_REQUEST_CODE = 200;

    String nim,nama,alamat,jurusan,foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skkm);
        SharedPreferences sharedPreferences = SkkmActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
        jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");
        foto = sharedPreferences.getString(Config.foto_SHARED_PREF, "null");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        formattedDate = df.format(c);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String text = spinner.getSelectedItem().toString();
                try{
                   if(arrayList.isEmpty()  & text.equals("SEMUA")) {
                       listViewAdapter.filter("");
                   }else{
                    if (text.equals("SEMUA")) {
                        listViewAdapter.filter("");
    //                    lispiu.clearTextFilter();

                    } else {
                        listViewAdapter.filter(text);
                    }

                   }
                }catch (Exception ss){

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


        /// buat PDF
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logostikomfull);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
        bmpcheck = BitmapFactory.decodeResource(getResources(), R.drawable.checkmark);
        scaledbmpcheck = Bitmap.createScaledBitmap(bmpcheck, 10, 10, false);
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
                generatePDF();
            }
        });
        //////////

        getKategori();
        getJSONSKKM();
        getUser();

    }
    private void showSKKM()  {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String date=jo.getString("tanggalPembuatan");
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

                adapterControl app = new adapterControl(jo.getString("id"),
                                jo.getString("namaKegiatan"),
                                jo.getString("keterangan"),
                                jo.getString("namaKedudukan"),
                                jo.getString("namaTingkat"),
                                jo.getString("sertifikat"),
                                jo.getString("tanggalMulai"),
                                date,
                                jo.getString("poin"),
                                jo.getString("namaJenis"),
                                jo.getString("status"),
                                jo.getString("persetujuan"));
                arrayList.add(app);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

            listViewAdapter = new ListViewAdapter(this, arrayList);
            lispiu.setAdapter(listViewAdapter);

    }


    private void getJSONSKKM(){
       class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(SkkmActivity.this,"Fetching Data","Wait...",false,false);
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

    private void showKategori() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            arrayListSpin = new ArrayList<String>();
            arrayListSpin.add("SEMUA");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
                String namaJenis = jo.getString("namaJenis");

                arrayListSpin.add(namaJenis);
            }
            spinner.setAdapter(new ArrayAdapter<String>(SkkmActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListSpin));

            try {
            }catch (Exception asds){}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getKategori(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showKategori();
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
    private void showUser() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONArray result2 = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY2);


            for (int i = 0; i < result2.length(); i++) {
                JSONObject jo = result2.getJSONObject(i);
                String jmlskkm = jo.getString("jumlah");
                jmlpoin = jo.getString("JumlahPOIN");
            }


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
                String s = rh.sendGetRequest(Config.USER_URL+nim);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePDF() {
int totalsskm=0;
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.navigationBarColor));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("Sistem Pencatatan Poin SKKM  ITB STIKOM BALI", 180, 100, title);
        canvas.drawText("SKKM Management", 180, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.navigationBarColor));
        title.setTextSize(10);
        canvas.drawText("Nama : "+nama, 80, 180, title);
        canvas.drawText("NIM : "+nim, 80, 195, title);
        canvas.drawText("Jurusan : "+jurusan, 80, 210, title);

        int jumlahAkademik=0;
        int jumlahMINAT=0;
        int jumlahORGANISASI=0;
        int jumlahKEGIATAN=0;
        for(int i=0; i< arrayList.size(); i++) {
            if (arrayList.get(i).getkategori().equals("AKADEMIK DAN ILMIAH")) {
                jumlahAkademik+=1;
            }
            if (arrayList.get(i).getkategori().equals("MINAT BAKAT, SENI, DAN OLAHRAGA")) {
                jumlahMINAT+=1;
            }
            if (arrayList.get(i).getkategori().equals("ORGANISASI DAN SOSIAL")) {
                jumlahORGANISASI+=1;
            }
            if (arrayList.get(i).getkategori().equals("KEGIATAN WAJIB")) {
                jumlahKEGIATAN+=1;
            }
        }

        System.out.println("jumlah akademik" +jumlahAkademik);
        System.out.println("jumlah MINAT" +jumlahMINAT);
        System.out.println("jumlah ORGANISASI" +jumlahORGANISASI);
        System.out.println("jumlah KEGIATAN" +jumlahKEGIATAN);
        int  lebarAkademik=0;
        int  lebarMinat=0;
        int  lebarOrganisasi=0;
        int  lebarKegiatan=0;
        for(int i=0; i< arrayList.size(); i++){
            if(arrayList.get(i).getkategori().equals("AKADEMIK DAN ILMIAH")) {
                if(lebarAkademik==0) {
//                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("AKADEMIK DAN ILMIAH", 80, 270 + (lebarAkademik * 30), title);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("NO", 90, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("KEGIATAN", 140, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("TINGKAT", 340, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("PARTISIPASI", 460, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("POIN", 560, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 610, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("BEM", 620, 305 + (lebarAkademik * 30), title);
                    canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 665, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("Kemahasis.", 660, 305 + (lebarAkademik * 30), title);
                }

                        lebarAkademik=lebarAkademik+1;
                    canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik* 30), paint);
                    canvas.drawText(String.valueOf(lebarAkademik), 90, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                    System.out.println("ini PANJANG " + arrayList.get(i).getjudul().length());
                    if (arrayList.get(i).getjudul().length() > 30) {
                        canvas.drawText(arrayList.get(i).getjudul().substring(0, 30), 140, 295 + (lebarAkademik * 30), title);
                        canvas.drawText(arrayList.get(i).getjudul().substring(30, arrayList.get(i).getjudul().length()), 140, 304 + (lebarAkademik * 30), title);
                    } else {
                        canvas.drawText(arrayList.get(i).getjudul(), 140, 300 + (lebarAkademik * 30), title);
                    }
                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText(arrayList.get(i).gettingkat(), 340, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText(arrayList.get(i).getpartisipasi(), 460, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText(arrayList.get(i).getpoin(), 570, 300 + (lebarAkademik* 30), title);
                    canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                    if (arrayList.get(i).getstatus().equals("1") || arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                        canvas.drawBitmap(scaledbmpcheck, 620, 290 + (lebarAkademik * 30), title);
                    } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                        canvas.drawBitmap(scaledbmpcheck, 620, 290 + (i * 30), title);
                    } else {
                        canvas.drawText("X", 620, 300 + (lebarAkademik * 30), title);
                    }
                    canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                    if (arrayList.get(i).getstatus().equals("1")) {
                        canvas.drawText("X", 675, 300 + (lebarAkademik * 30), title);
                    } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                        totalsskm += Integer.parseInt(arrayList.get(i).getpoin());
                        canvas.drawBitmap(scaledbmpcheck, 675, 290 + (lebarAkademik * 30), title);
                    } else {
                        canvas.drawText("X", 680, 300 + (lebarAkademik *30), title);
                    }
               }
        }
        if(jumlahMINAT!=0){
            lebarAkademik=lebarAkademik+2;
        }
        for(int i=0; i< arrayList.size(); i++){
            if(arrayList.get(i).getkategori().equals("MINAT BAKAT, SENI, DAN OLAHRAGA")) {
                if(lebarMinat==0) {
//                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("MINAT BAKAT, SENI, DAN OLAHRAGA", 80, 270 + (lebarAkademik * 30), title);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("NO", 90, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("KEGIATAN", 140, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("TINGKAT", 340, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("PARTISIPASI", 460, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("POIN", 560, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 610, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("BEM", 620, 305 + (lebarAkademik * 30), title);
                    canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 665, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("Kemahasis.", 660, 305 + (lebarAkademik * 30), title);
                }

                lebarAkademik=lebarAkademik+1;
                lebarMinat=lebarMinat+1;
                canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik* 30), paint);
                canvas.drawText(String.valueOf(lebarMinat), 90, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                System.out.println("ini PANJANG " + arrayList.get(i).getjudul().length());
                if (arrayList.get(i).getjudul().length() > 30) {
                    canvas.drawText(arrayList.get(i).getjudul().substring(0, 30), 140, 295 + (lebarAkademik * 30), title);
                    canvas.drawText(arrayList.get(i).getjudul().substring(30, arrayList.get(i).getjudul().length()), 140, 304 + (lebarAkademik * 30), title);
                } else {
                    canvas.drawText(arrayList.get(i).getjudul(), 140, 300 + (lebarAkademik * 30), title);
                }
                canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).gettingkat(), 340, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).getpartisipasi(), 460, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).getpoin(), 570, 300 + (lebarAkademik* 30), title);
                canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                if (arrayList.get(i).getstatus().equals("1") || arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    canvas.drawBitmap(scaledbmpcheck, 620, 290 + (lebarAkademik * 30), title);
                } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    canvas.drawBitmap(scaledbmpcheck, 620, 290 + (i * 30), title);
                } else {
                    canvas.drawText("X", 620, 300 + (lebarAkademik * 30), title);
                }
                canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                if (arrayList.get(i).getstatus().equals("1")) {
                    canvas.drawText("X", 675, 300 + (lebarAkademik * 30), title);
                } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    totalsskm += Integer.parseInt(arrayList.get(i).getpoin());
                    canvas.drawBitmap(scaledbmpcheck, 675, 290 + (lebarAkademik * 30), title);
                } else {
                    canvas.drawText("X", 680, 300 + (lebarAkademik *30), title);
                }
            }
        }
        if(jumlahORGANISASI!=0){
            lebarAkademik=lebarAkademik+2;
        }
        for(int i=0; i< arrayList.size(); i++){
            if(arrayList.get(i).getkategori().equals("ORGANISASI DAN SOSIAL")) {
                if(lebarOrganisasi==0) {
//                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("ORGANISASI DAN SOSIAL", 80, 270 + (lebarAkademik * 30), title);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("NO", 90, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("KEGIATAN", 140, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("TINGKAT", 340, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("PARTISIPASI", 460, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("POIN", 560, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 610, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("BEM", 620, 305 + (lebarAkademik * 30), title);
                    canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 665, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("Kemahasis.", 660, 305 + (lebarAkademik * 30), title);
                }

                lebarAkademik=lebarAkademik+1;
                lebarOrganisasi=lebarOrganisasi+1;
                canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik* 30), paint);
                canvas.drawText(String.valueOf(lebarOrganisasi), 90, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                System.out.println("ini PANJANG " + arrayList.get(i).getjudul().length());
                if (arrayList.get(i).getjudul().length() > 30) {
                    canvas.drawText(arrayList.get(i).getjudul().substring(0, 30), 140, 295 + (lebarAkademik * 30), title);
                    canvas.drawText(arrayList.get(i).getjudul().substring(30, arrayList.get(i).getjudul().length()), 140, 304 + (lebarAkademik * 30), title);
                } else {
                    canvas.drawText(arrayList.get(i).getjudul(), 140, 300 + (lebarAkademik * 30), title);
                }
                canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).gettingkat(), 340, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).getpartisipasi(), 460, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).getpoin(), 570, 300 + (lebarAkademik* 30), title);
                canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                if (arrayList.get(i).getstatus().equals("1") || arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    canvas.drawBitmap(scaledbmpcheck, 620, 290 + (lebarAkademik * 30), title);
                } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    canvas.drawBitmap(scaledbmpcheck, 620, 290 + (i * 30), title);
                } else {
                    canvas.drawText("X", 620, 300 + (lebarAkademik * 30), title);
                }
                canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                if (arrayList.get(i).getstatus().equals("1")) {
                    canvas.drawText("X", 675, 300 + (lebarAkademik * 30), title);
                } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    totalsskm += Integer.parseInt(arrayList.get(i).getpoin());
                    canvas.drawBitmap(scaledbmpcheck, 675, 290 + (lebarAkademik * 30), title);
                } else {
                    canvas.drawText("X", 680, 300 + (lebarAkademik *30), title);
                }
            }
        }
        if(jumlahKEGIATAN!=0){
            lebarAkademik=lebarAkademik+2;
        }
        for(int i=0; i< arrayList.size(); i++){
            if(arrayList.get(i).getkategori().equals("KEGIATAN WAJIB")) {
                if(lebarKegiatan==0) {
//                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("KEGIATAN WAJIB", 80, 270 + (lebarAkademik * 30), title);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("NO", 90, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("KEGIATAN", 140, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("TINGKAT", 340, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("PARTISIPASI", 460, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("POIN", 560, 300 + (lebarAkademik * 30), title);
                    canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 610, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("BEM", 620, 305 + (lebarAkademik * 30), title);
                    canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                    canvas.drawText("Validasi", 665, 295 + (lebarAkademik * 30), title);
                    canvas.drawText("Kemahasis.", 660, 305 + (lebarAkademik * 30), title);
                }

                lebarAkademik=lebarAkademik+1;
                lebarKegiatan=lebarKegiatan+1;
                canvas.drawRect(80, 280 + (lebarAkademik * 30), 130, 310 + (lebarAkademik* 30), paint);
                canvas.drawText(String.valueOf(lebarKegiatan), 90, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(130, 280 + (lebarAkademik * 30), 330, 310 + (lebarAkademik * 30), paint);
                System.out.println("ini PANJANG " + arrayList.get(i).getjudul().length());
                if (arrayList.get(i).getjudul().length() > 30) {
                    canvas.drawText(arrayList.get(i).getjudul().substring(0, 30), 140, 295 + (lebarAkademik * 30), title);
                    canvas.drawText(arrayList.get(i).getjudul().substring(30, arrayList.get(i).getjudul().length()), 140, 304 + (lebarAkademik * 30), title);
                } else {
                    canvas.drawText(arrayList.get(i).getjudul(), 140, 300 + (lebarAkademik * 30), title);
                }
                canvas.drawRect(330, 280 + (lebarAkademik * 30), 450, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).gettingkat(), 340, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(450, 280 + (lebarAkademik * 30), 550, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).getpartisipasi(), 460, 300 + (lebarAkademik * 30), title);
                canvas.drawRect(550, 280 + (lebarAkademik * 30), 600, 310 + (lebarAkademik * 30), paint);
                canvas.drawText(arrayList.get(i).getpoin(), 570, 300 + (lebarAkademik* 30), title);
                canvas.drawRect(600, 280 + (lebarAkademik * 30), 655, 310 + (lebarAkademik * 30), paint);
                if (arrayList.get(i).getstatus().equals("1") || arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    canvas.drawBitmap(scaledbmpcheck, 620, 290 + (lebarAkademik * 30), title);
                } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    canvas.drawBitmap(scaledbmpcheck, 620, 290 + (i * 30), title);
                } else {
                    canvas.drawText("X", 620, 300 + (lebarAkademik * 30), title);
                }
                canvas.drawRect(655, 280 + (lebarAkademik * 30), 720, 310 + (lebarAkademik * 30), paint);
                if (arrayList.get(i).getstatus().equals("1")) {
                    canvas.drawText("X", 675, 300 + (lebarAkademik * 30), title);
                } else if (arrayList.get(i).getstatus().equals("2") && arrayList.get(i).getpersetujuan().equals("SETUJU")) {
                    totalsskm += Integer.parseInt(arrayList.get(i).getpoin());
                    canvas.drawBitmap(scaledbmpcheck, 675, 290 + (lebarAkademik * 30), title);
                } else {
                    canvas.drawText("X", 680, 300 + (lebarAkademik *30), title);
                }
            }
        }
        canvas.drawText("TOTAL POIN : "+totalsskm, 620, 330 + (lebarAkademik * 30), title);

        canvas.drawText("Denpasar, "+formattedDate, 600, 430 + (lebarAkademik * 30), title);
        canvas.drawText("( Kordinator Kemahasiswaan )", 590, 500 + (lebarAkademik * 30), title);

        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        String penamaan="";
        if(spinner.getSelectedItem().toString().equals("SEMUA")){
            penamaan="";
        }else{
            penamaan=spinner.getSelectedItem().toString();
        }
        File file = new File(Environment.getExternalStorageDirectory(), "SKKM-"+nim+"-"+penamaan+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(SkkmActivity.this, "PDF file generated succesfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
            pdfDocument.close();

    }
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}