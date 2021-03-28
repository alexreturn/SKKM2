package com.stikom.skkm_mahasiswa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
                    loading = ProgressDialog.show(daftarPKMActivity.this, "Proses Kirim Data...", "Wait...", false, false);
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
                    params.put("nim_ketua", anggota);
                    params.put("judul", judulPKM);
                    params.put("jenis_pkm", spinnerJenisPKM);
                    params.put("email", email);
                    params.put("no_tlp", tlp);
                    params.put("anggota", TampungAngota);
                    params.put("nama_file", judulPKM);


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
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),FILE_SELECT_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == FILE_SELECT_CODE){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    filename.setText(selectedFilePath);
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
//            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    filename.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(Config.simpanPKMproposal);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            filename.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(daftarPKMActivity.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(daftarPKMActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(daftarPKMActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
//            dialog.dismiss();
            return serverResponseCode;
        }

    }
}