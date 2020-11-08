package com.stikom.skkm_mahasiswa;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.TextView;

        import com.stikom.skkm_mahasiswa.Config.Config;

public class homeFragment extends Fragment {

    TextView txtname,txtnim;
    ImageButton btnSeminar,btnSKKM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
        String nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        String alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
        String jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");

        txtname=(TextView)view.findViewById(R.id.txtnama);
        txtnim=(TextView)view.findViewById(R.id.txtnim);

        txtname.setText(nama);
        txtnim.setText(nim+" - "+jurusan);


        btnSeminar=(ImageButton)view.findViewById(R.id.btnSeminar);
        btnSKKM=(ImageButton)view.findViewById(R.id.btnSKKM);


        btnSeminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), SeminarActivity.class);
                startActivity(intent);
            }
        });
        btnSKKM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), SkkmActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}