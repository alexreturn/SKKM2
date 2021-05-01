package com.stikom.skkm_mahasiswa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.FilePath;
import com.stikom.skkm_mahasiswa.Config.FileUtils;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;
import com.stikom.skkm_mahasiswa.R;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class daftarPKMActivity extends AppCompatActivity {
    TextView filename;
    EditText txtjudul,txtanggota,txtanggota1,txtanggota2,txtanggota3,txtEmail,txttlp;
    Spinner spinnerJenis;
    Button btnDokument,btnsimpan;
    String nama,nimuser,selectedFilePath;
    LinearLayout ViewKelompok;
    int widthkelompok=150;
    int jumlahklik=0;
    String TampungAngota="";
    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "okks ";
    String URLFileName,NAMAfilenamess;
    ProgressDialog dialog = null;
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_p_k_m);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        nimuser = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        ViewKelompok=(LinearLayout) findViewById(R.id.ViewKelompok);
        txtjudul=(EditText)findViewById(R.id.txtjudul);

        filename=(TextView) findViewById(R.id.filename);
        txtanggota=(EditText)findViewById(R.id.txtanggota);
        txtanggota1=(EditText)findViewById(R.id.txtanggota1);
        txtanggota2=(EditText)findViewById(R.id.txtanggota2);
        txtanggota3=(EditText)findViewById(R.id.txtanggota3);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txttlp=(EditText)findViewById(R.id.txttlp);
        txtanggota1.setVisibility(View.INVISIBLE);
        txtanggota2.setVisibility(View.INVISIBLE);
        txtanggota3.setVisibility(View.INVISIBLE);
        spinnerJenis=(Spinner)findViewById(R.id.spinnerJenis);
        btnsimpan=(Button)findViewById(R.id.btnsimpan);
        btnDokument=(Button)findViewById(R.id.btnDokument);
        btnDokument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanPKM();
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

    }

    public void simpanPKM() {
        TampungAngota="";
        final String judulPKM = txtjudul.getText().toString();
        final String email = txtEmail.getText().toString();
        final String tlp = txttlp.getText().toString();
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
            Toast.makeText(daftarPKMActivity.this, "Judul PKM tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(email.equals("")){
            Toast.makeText(daftarPKMActivity.this, "Email tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(tlp.equals("")){
            Toast.makeText(daftarPKMActivity.this, "Nomor telepon tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota.length() <9){
            Toast.makeText(daftarPKMActivity.this, "NIM tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota1.length() <9 && jumlahklik==1){
            Toast.makeText(daftarPKMActivity.this, "NIM Anggota1 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota2.length() <9 && jumlahklik==2){
            Toast.makeText(daftarPKMActivity.this, "NIM Anggota2 tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(anggota3.length() <9 && jumlahklik==3){
            Toast.makeText(daftarPKMActivity.this, "NIM Anggota3 tidak boleh kosong", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(daftarPKMActivity.this, "OK" + TampungAngota, Toast.LENGTH_LONG).show();
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
                    dialog = ProgressDialog.show(daftarPKMActivity.this, "", "Uploading file...", true);

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                }
                            });

                            uploadFile(URLFileName);

                        }
                    }).start();
//                Intent i =new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(i);
//                    finish();
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nim_ketua", nimuser);
                    params.put("judul", judulPKM);
                    params.put("jenis", spinnerJenisPKM);
                    params.put("email", email);
                    params.put("no_tlp", tlp);
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
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, FILE_SELECT_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       String URLFileName2 = data.getData().getLastPathSegment();
        URLFileName=URLFileName2.substring(URLFileName2.lastIndexOf(":")+1);
         NAMAfilenamess=URLFileName2.substring(URLFileName2.lastIndexOf("/")+1);
        System.out.println(NAMAfilenamess+"File Name: \n" + URLFileName + "\n");
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
                            Toast.makeText(daftarPKMActivity.this, "File Upload Complete.",
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