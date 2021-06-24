package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdminAdapter extends RecyclerView.Adapter<ListAdminAdapter.tinDang> {
    private ArrayList<TinDang> list;
    private Context context;

    public ListAdminAdapter(ArrayList<TinDang> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public tinDang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.card_chuaduyet, parent, false);
        return new tinDang(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  tinDang holder, int position) {
        TinDang a=list.get(position);
        Picasso.get().load(a.getList().get(0).toString()).into(holder.img);
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
        private ImageButton btnRemove, btnAccept;
        public tinDang(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.admin_img);
            tittle=itemView.findViewById(R.id.admin_tittle);
            gia=itemView.findViewById(R.id.admin_gia);
            btnAccept=itemView.findViewById(R.id.admin_accep);
            btnAccept.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            TinDang as=list.get(pro);
            if(v.getId() == R.id.admin_accep){
                ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
                query.getInBackground(as.getIdl(), ((object, e) -> {
                    if(e==null){
                        object.put("tinhtrang", "duyệt");
                        object.saveInBackground(e1 -> {
                            if(e1==null){
                                list.remove(list.get(pro));
                                notifyDataSetChanged();
                                JSONObject data=new JSONObject();
                                try {
                                    data.put("alert", "Tin đăng "+as.getTieuDe()+" được duyệt thành công");
                                    data.put("title", "Nhà đất");
                                } catch (JSONException es) {
                                    throw new IllegalArgumentException("unexpected parsing error", es);
                                }

                                ParsePush push = new ParsePush();
                                push.setChannel(as.getUserName()+"tindang");
                                push.setData(data);
                                push.sendInBackground();
                            }
                        });
                    }
                }));
            }
            else{
                Intent a=new Intent(context, TTTinDang.class);
                a.putExtra("object", as.toString());
                context.startActivity(a);
            }
        }
    }
}
