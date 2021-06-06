package com.example.nhadat_app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.Distin;
import com.example.nhadat_app.R;
import com.example.nhadat_app.ui.home.HomeFragment;


import java.util.ArrayList;
import java.util.List;

public class ListDistineAdapter extends RecyclerView.Adapter<ListDistineAdapter.Distin> {
    private Context context;
    private Button lu;
    private LinearLayout li;
    private ScrollView lk;
    private List<com.example.nhadat_app.Model.Distin> list;

    public ListDistineAdapter(Context context, ArrayList<com.example.nhadat_app.Model.Distin> list, Button lu, LinearLayout li, ScrollView lk) {
        this.context = context;
        this.list=list;
        this.list=list;
        this.lk=lk;
        this.lu=lu;
        this.li=li;
    }

    public void setData(List<com.example.nhadat_app.Model.Distin> list){
        this.list=list;
    }
    @Override
    public Distin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.listds, parent, false);
        return new Distin(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Distin holder, int position) {
        com.example.nhadat_app.Model.Distin a=list.get(position);
        holder.txt.setText(a.getDistin());
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class Distin extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt;
        public Distin(@NonNull  View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txt=itemView.findViewById(R.id.txt_HT);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            lu.setText(list.get(pro).getDistin());
            li.setVisibility(View.GONE);
            lk.setVisibility(View.VISIBLE);
        }
    }
}
