package com.stikom.skkm_mahasiswa.fragment;

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

import com.stikom.skkm_mahasiswa.AboutActivity;
import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.R;
import com.stikom.skkm_mahasiswa.SeminarActivity;


public class AllFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        return view;
    }
}