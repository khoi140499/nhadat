package com.example.nhadat_app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.example.nhadat_app.TTTinDang;
import com.example.nhadat_app.UpdateItem;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListTinDangAdapter extends RecyclerView.Adapter<ListTinDangAdapter.tinDang> {
    private ArrayList<TinDang> list;
    private Context context;

    public ListTinDangAdapter(ArrayList<TinDang> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public tinDang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.layout_tin, parent, false);
        return new tinDang(v);
    }

    @Override
    public void onBindViewHolder(@NonNull tinDang holder, int position) {
        TinDang a=list.get(position);
        Picasso.get().load(a.getImg1()).into(holder.img);
        holder.tittle.setText(a.getTieuDe());
        if(a.getGia()/1000000>0 && a.getGia()/1000000<=999){
            holder.gia.setText((long)a.getGia()/1000000+" triệu");
        }
        else if(a.getGia()/1000000000>0){
            holder.gia.setText((long)a.getGia()/1000000000+" tỉ");
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class tinDang extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView tittle;
        private TextView gia;
        private ImageButton btn;
        public tinDang(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_layouttin);
            tittle=itemView.findViewById(R.id.txt_tittle);
            gia=itemView.findViewById(R.id.txt_layouttin_gia);
            btn=itemView.findViewById(R.id.layout_btnmenu);
            btn.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            TinDang as=list.get(pro);
            if (v.getId() == R.id.layout_btnmenu) {
                PopupMenu popupMenu=new PopupMenu(context, btn);
                popupMenu.getMenuInflater().inflate(R.menu.menuitem, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.btnchinhsua){
                            Intent a=new Intent(context, UpdateItem.class);
                            a.putExtra("object", as.toString());
                            context.startActivity(a);
                        }
                        if(item.getItemId() == R.id.btnxoa){
                            ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
                            query.getInBackground(as.getIdl(), ((object, e) -> {
                                if(e==null){
                                    list.remove(pro);
                                    notifyDataSetChanged();
                                }
                                object.saveInBackground();
                            }));
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
            else{
                Intent a=new Intent(context, TTTinDang.class);
                a.putExtra("object", as.toString());
                context.startActivity(a);
            }
        }
    }
}
