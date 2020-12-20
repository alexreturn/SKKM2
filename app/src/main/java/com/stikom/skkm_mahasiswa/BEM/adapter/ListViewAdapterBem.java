package com.stikom.skkm_mahasiswa.BEM.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.stikom.skkm_mahasiswa.Config.adapterControl;
import com.stikom.skkm_mahasiswa.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterBem extends BaseAdapter {
    private LayoutInflater inflater;
    private List<adapterControlBem> appList;
    private ArrayList<adapterControlBem> arraylist;

    public ListViewAdapterBem(Context context, List<adapterControlBem> appList) {
        this.appList = appList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(appList);
    }

    public class ViewHolder {
        TextView TextTitle, TextSubtitle,ImageIcon;
        ImageView imageView2;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public adapterControlBem getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_data_skkm_bem, null);

            holder.TextTitle = view.findViewById(R.id.txttgl);
            holder.TextSubtitle = view.findViewById(R.id.txtjudul);
            holder.ImageIcon = view.findViewById(R.id.txtpoin);
            holder.imageView2=view.findViewById(R.id.imageView2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        holder.TextTitle.setText(appList.get(position).getdate());
        holder.TextSubtitle.setText(appList.get(position).getjudul());
        holder.ImageIcon.setText(appList.get(position).getpoin());

        if(appList.get(position).getstatus()=="1" || appList.get(position).getstatus().equals("1")){
            holder.imageView2.setBackground(ContextCompat.getDrawable(holder.imageView2.getContext(), R.drawable.stts_ijo));
        }else{
            holder.imageView2.setBackground(ContextCompat.getDrawable(holder.imageView2.getContext(), R.drawable.stts_merah));
        }

        return view;
    }

    //Filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appList.clear();
        if (charText.length() == 0) {
            appList.addAll(arraylist);
        } else {
            for (adapterControlBem an : arraylist) {
                if (an.getkategori().toLowerCase(Locale.getDefault()).contains(charText)) {
                    appList.add(an);
                }
            }
        }
        notifyDataSetChanged();
    }
}
