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

import com.example.nhadat_app.Model.Ward;
import com.example.nhadat_app.R;
import com.example.nhadat_app.ui.home.HomeFragment;
import com.example.nhadat_app.Model.province;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListWardAdapter extends RecyclerView.Adapter<ListWardAdapter.province> {
    private Context context;
    private Button lu;
    private LinearLayout li;
    private ScrollView lk;
    private List<Ward> list;

    public ListWardAdapter(Context context, ArrayList<Ward> list, Button lu, LinearLayout li, ScrollView lk) {
        this.context = context;
        this.list=list;
        this.lk=lk;
        this.lu=lu;
        this.li=li;
    }

    public void setData(List<Ward> list){
        this.list=list;
    }

    @Override
    public province onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.listds, parent, false);
        return new province(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull province holder, int position) {
        Ward as=list.get(position);
        holder.txt.setText(as.getWard());
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class province extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txt;
        public province(@NonNull  View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txt=itemView.findViewById(R.id.txt_HT);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            lu.setText(list.get(pro).getWard());
            li.setVisibility(View.GONE);
            lk.setVisibility(View.VISIBLE);
        }
    }
}
