package com.stikom.skkm_mahasiswa.Config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stikom.skkm_mahasiswa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter  extends BaseAdapter {
    private LayoutInflater inflater;
    private List<adapterControl> appList;
    private ArrayList<adapterControl> arraylist;

    public ListViewAdapter(Context context, List<adapterControl> appList) {
        this.appList = appList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(appList);
    }

    public class ViewHolder {
        TextView TextTitle, TextSubtitle,ImageIcon;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public adapterControl getItem(int position) {
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
            view = inflater.inflate(R.layout.row_data_skkm, null);

            holder.TextTitle = view.findViewById(R.id.txttgl);
            holder.TextSubtitle = view.findViewById(R.id.txtjudul);
            holder.ImageIcon = view.findViewById(R.id.txtpoin);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        holder.TextTitle.setText(appList.get(position).getdate());
        holder.TextSubtitle.setText(appList.get(position).getjudul());
        holder.ImageIcon.setText(appList.get(position).getpoin());

        return view;
    }

    //Filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appList.clear();
        if (charText.length() == 0) {
            appList.addAll(arraylist);
        } else {
            for (adapterControl an : arraylist) {
                if (an.getkategori().toLowerCase(Locale.getDefault()).contains(charText)) {
                    appList.add(an);
                }
            }
        }
        notifyDataSetChanged();
    }
}
