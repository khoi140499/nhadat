package com.example.nhadat_app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListProvincesAdapter extends RecyclerView.Adapter<ListProvincesAdapter.province> {
    private Context context;
    private Button lu;
    private LinearLayout li;
    private ScrollView lk;
    private List<com.example.nhadat_app.Model.province> list;

    public ListProvincesAdapter(Context context, ArrayList<com.example.nhadat_app.Model.province> list,
                                Button lu, LinearLayout li, ScrollView lk) {
        this.context = context;
        this.list=list;
        this.lk=lk;
        this.lu=lu;
        this.li=li;
    }

    public void setData(List<com.example.nhadat_app.Model.province> list){
        this.list=list;
    }

    @Override
    public ListProvincesAdapter.province onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.listds, parent, false);
        return new ListProvincesAdapter.province(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListProvincesAdapter.province holder, int position) {
        com.example.nhadat_app.Model.province as=list.get(position);
        holder.txt.setText(as.getProvince());
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
            txt=itemView.findViewById(R.id.txt_HT);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            lu.setText(list.get(pro).getProvince());
            li.setVisibility(View.GONE);
            lk.setVisibility(View.VISIBLE);
        }
    }
}
