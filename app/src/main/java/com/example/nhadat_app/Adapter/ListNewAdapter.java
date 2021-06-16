package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.example.nhadat_app.TTTinDang;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListNewAdapter extends RecyclerView.Adapter<ListNewAdapter.listNew> {
    private ArrayList<TinDang> list;
    private Context context;
    private ParseUser as;
    public ListNewAdapter(ArrayList<TinDang> list, Context context, ParseUser as) {
        this.list = list;
        this.context = context;
        this.as=as;
    }

    @Override
    public ListNewAdapter.listNew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.cardmost, parent, false);
        return new listNew(v);
    }

    @Override
    public void onBindViewHolder(@NonNull listNew holder, int position) {
        TinDang a=list.get(position);
        holder.tittle.setText(a.getTieuDe());
        if(a.getGia()/1000000>0 && a.getGia()/1000000<=999){
            holder.gia.setText((long)a.getGia()/1000000+" triệu - "+a.getDienTich()+"m2");
        }
        else if(a.getGia()/1000000000>0){
            holder.gia.setText((long)a.getGia()/1000000000+" tỉ - "+a.getDienTich()+"m2");
        }
        Picasso.get().load(a.getImg1()).into(holder.img);
        holder.luotxem.setText(a.getLuotxem()+"");
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class listNew extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView tittle;
        private TextView gia;
        private Button luotxem;
        public listNew(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.most_img);
            tittle=itemView.findViewById(R.id.most_tittle);
            luotxem=itemView.findViewById(R.id.most_count);
            gia=itemView.findViewById(R.id.most_cost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            TinDang a=list.get(pro);
                ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
                query.getInBackground(a.getIdl(), ((object, e) -> {
                    if(e==null){
                        object.put("luotxem", a.getLuotxem()+1);
                        object.saveInBackground(e1 -> {
                            Intent as=new Intent(context, TTTinDang.class);
                            as.putExtra("object", a.toString());
                            as.putExtra("type", "yes");
                            context.startActivity(as);
                        });
                    }
                }));
            }
    }
}
