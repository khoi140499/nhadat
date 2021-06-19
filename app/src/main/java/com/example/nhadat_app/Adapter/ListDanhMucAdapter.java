package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.DangTinActivity.DangTin_Image;
import com.example.nhadat_app.DangTinActivity.DangTin_Provinces;
import com.example.nhadat_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListDanhMucAdapter extends RecyclerView.Adapter<ListDanhMucAdapter.danhMuc> {
    private Context context;
    private List<String> list;

    public ListDanhMucAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public danhMuc onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.listds, parent, false);
        return new danhMuc(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull danhMuc holder, int position) {
        String s=list.get(position);
        holder.txt.setText(s+"");
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class danhMuc extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt;
        public danhMuc(@NonNull @NotNull View itemView) {
            super(itemView);
            txt=itemView.findViewById(R.id.txt_HT);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pr=getLayoutPosition();
            String s=list.get(pr);
            Intent a=new Intent(context, DangTin_Image.class);
            a.putExtra("danhmuc", s);
            context.startActivity(a);
        }
    }
}
