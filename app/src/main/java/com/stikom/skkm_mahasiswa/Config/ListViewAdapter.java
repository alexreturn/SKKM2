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
    private ArrayList<adapterControl> myList;
    private ArrayList<adapterControl> arraylist=null;

    public ListViewAdapter(Context context, List<adapterControl> appList) {
        this.appList = appList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(appList);
    }

    public class ViewHolder {
        TextView txtno,TextTitle, TextSubtitle,ImageIcon,Textpartisipasi,Texttingkat,Textstatus;
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

            holder.txtno = view.findViewById(R.id.txtno);
            holder.TextTitle = view.findViewById(R.id.txttgl);
            holder.TextSubtitle = view.findViewById(R.id.txtjudul);
            holder.Textpartisipasi = view.findViewById(R.id.txtpartisipasi);
            holder.Texttingkat = view.findViewById(R.id.txttingkat);
            holder.TextSubtitle = view.findViewById(R.id.txtjudul);
            holder.ImageIcon = view.findViewById(R.id.txtpoin);
            holder.Textstatus = view.findViewById(R.id.txtstatus);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        holder.txtno.setText(position+1+"");
        holder.TextTitle.setText(appList.get(position).getdate());
        holder.TextSubtitle.setText(appList.get(position).getjudul());
        holder.ImageIcon.setText(appList.get(position).getpoin());

        holder.Textpartisipasi.setText(appList.get(position).getpartisipasi());
        holder.Texttingkat.setText(appList.get(position).gettingkat());
        if(appList.get(position).getstatus().equals("1")){
            holder.Textstatus.setText("Menunggu Validasi");
        }else if(appList.get(position).getstatus().equals("2")){
            holder.Textstatus.setText("Valid");
        }else{
            holder.Textstatus.setText("Menunggu Validasi BEM");
        }

        return view;
    }

    //Filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appList.clear();
        System.out.println("PANAJGN"+charText.length());
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
