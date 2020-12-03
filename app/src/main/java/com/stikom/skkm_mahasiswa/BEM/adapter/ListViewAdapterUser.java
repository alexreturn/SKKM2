package com.stikom.skkm_mahasiswa.BEM.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stikom.skkm_mahasiswa.Config.adapterControl;
import com.stikom.skkm_mahasiswa.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapterUser extends BaseAdapter {
    private LayoutInflater inflater;
    private List<adapterControlUser> appList;
    private ArrayList<adapterControlUser> arraylist;

    public ListViewAdapterUser(Context context, List<adapterControlUser> appList) {
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
    public adapterControlUser getItem(int position) {
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
            view = inflater.inflate(R.layout.row_data_user, null);

            holder.TextTitle = view.findViewById(R.id.txtnim);
            holder.TextSubtitle = view.findViewById(R.id.txtnama);
            holder.ImageIcon = view.findViewById(R.id.txtjurusan);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        holder.TextTitle.setText(appList.get(position).getnim());
        holder.TextSubtitle.setText(appList.get(position).getnama());
        holder.ImageIcon.setText(appList.get(position).getjurusan());

        return view;
    }

    //Filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appList.clear();
        if (charText.length() == 0) {
            appList.addAll(arraylist);
        } else {
            for (adapterControlUser an : arraylist) {
                if (an.getnim().toLowerCase(Locale.getDefault()).contains(charText)) {
                    appList.add(an);
                }
            }
        }
        notifyDataSetChanged();
    }
}
