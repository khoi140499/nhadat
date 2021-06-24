package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.listA> {
    private ArrayList<TinDang> list;
    private Context context;
    private ParseUser as;
    public ListAdapter(ArrayList<TinDang> list, Context context, ParseUser as) {
        this.list = list;
        this.context = context;
        this.as=as;
    }

    @Override
    public listA onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.card, parent, false);
        return new listA(v);
    }

    @Override
    public void onBindViewHolder(@NonNull listA holder, int position) {
        TinDang a=list.get(position);
        holder.tittle.setText(a.getTieuDe());
        holder.diaChi.setText(a.getTinh());
        if(a.getGia()/1000000>0 && a.getGia()/1000000<=999){
            holder.gia.setText((long)a.getGia()/1000000+" triệu");
        }
        else if(a.getGia()/1000000000>0){
            holder.gia.setText((long)a.getGia()/1000000000+" tỉ");
        }
        List<String> ln=a.getList();
        Picasso.get().load(ln.get(0).toString()).into(holder.img);
        holder.txt.setText(a.getList().size()+"");
        String s=Calendar.getInstance().getTime()+"";
        String[] arr=s.split(" ");
        String date=arr[2]+" "+arr[1]+" "+arr[5]+" "+arr[3];
        System.out.println(a.getDate());
        String[] arr2=a.getDate().split(" ");
        String date2=arr2[0]+" "+arr2[1]+" "+arr2[2]+" "+arr2[4];

        SimpleDateFormat format=new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat format2=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d1=null;
        Date d2=null;
        String d3=null;
        String d4=null;
        Long dff = null;
        Date date1=null;
        Date date3=null;
        try {
            d1=format.parse(date);
            d2=format.parse(date2);
            d3=format1.format(d1);
            d4=format1.format(d2);
            date1=format2.parse(d3);
            date3=format2.parse(d4);
            dff= date3.getTime()- date1.getTime();

        }catch (ParseException|NullPointerException e){
            System.out.println(e.getMessage());
        }
        System.out.println("d3 "+d3+" d4 "+d4 +" mini "+dff);
        long kq=TimeUnit.MILLISECONDS.toMinutes(dff);
        System.out.println(kq);
        kq=Math.abs(kq);
        if(kq<=59){
            holder.thoigian.setText(kq+" phút trước");
        }
        else if(kq>59){
            if(kq/60>0 && kq/60<=23){
                holder.thoigian.setText((long) kq/60+" giờ trước");
            }
            else if(kq/60>23){
                {
                    if(kq/(60*24) > 0 && kq/(60*24) <=7){
                        holder.thoigian.setText((long)kq/(60*24)+" ngày trước");
                    }
                    else{
                        if(kq/(60*24*7)>0 && kq/(60*24*7)<=4){
                            holder.thoigian.setText((long)kq/(60*24*7)+" tuần trước");
                        }
                        else{
                            holder.thoigian.setText((long)kq/(60*24*30)+" tháng trước");
                        }
                    }
                }
            }
        }
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("username", a.getUserName());
        query.findInBackground(((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(holder.imgprofile);
                    holder.username.setText(as.getString("fullname"));
                }
            }
        }));


        if(as==null){
            holder.save.setImageResource(R.drawable.heart_20px);
        }
        else{
            ParseQuery<ParseObject> query1=ParseQuery.getQuery("SavePostin");
            query1.whereEqualTo("username", as.getUsername());
            query1.findInBackground(((objects, e) -> {
                if(e==null){
                    for(ParseObject ass:objects){
                        if(ass.getString("tinDang").equalsIgnoreCase(a.getIdl())==true){
                            holder.save.setImageResource(R.drawable.heart1_20px);
                            holder.save.setTag("success");
                        }
                    }
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class listA extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt;
        private ImageView img;
        private TextView tittle;
        private TextView gia;
        private CircleImageView imgprofile;
        private TextView username;
        private TextView thoigian;
        private TextView diaChi;
        private ImageButton save;
        public listA(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.card_img);
            tittle=itemView.findViewById(R.id.card_tittle);
            thoigian=itemView.findViewById(R.id.card_time);
            gia=itemView.findViewById(R.id.card_gia);
            imgprofile=itemView.findViewById(R.id.card_profile);
            username=itemView.findViewById(R.id.card_username);
            diaChi=itemView.findViewById(R.id.card_add);
            save=itemView.findViewById(R.id.save);
            txt=itemView.findViewById(R.id.image_count);
            save.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            TinDang a=list.get(pro);
            if(v.getId()==R.id.save){
                if(as==null){
                    Toast.makeText(context, "Đăng nhập trước khi thực hiện lưu tin", Toast.LENGTH_LONG).show();
                }
                else if(as.getUsername().equalsIgnoreCase(a.getUserName())==true){
                    Toast.makeText(context, "Đây là tin đăng của bạn", Toast.LENGTH_LONG).show();
                }
                else if(save.getTag()!=null && save.getTag().equals("success")==true) {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("SavePostin");
                    query1.whereEqualTo("username", as.getUsername());
                    query1.findInBackground(((objects, e) -> {
                        if (e == null) {
                            for (ParseObject ass : objects) {
                                if (ass.getString("tinDang").equalsIgnoreCase(a.getIdl()) == true) {
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("SavePostin");
                                    query.getInBackground(ass.getObjectId(), (objecta, e1) -> {
                                        if (e1 == null) {
                                            objecta.deleteInBackground((e2 -> {
                                                if (e2 == null) {
                                                    Toast.makeText(context, "Hủy thành công", Toast.LENGTH_LONG).show();
                                                    save.setImageResource(R.drawable.heart_20px);
                                                }
                                            }));
                                        }
                                    });
                                }
                            }
                        }
                    }));
                }
                else{
                    ParseObject object=new ParseObject("SavePostin");
                    object.put("username", as.getUsername());
                    object.put("tinDang", a.getIdl());

                    object.saveInBackground(e -> {
                        if(e==null){
                            save.setImageResource(R.drawable.heart1_20px);
                            save.setTag("success");
                            Toast.makeText(context, "Lưu thành công", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else{
                ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
                query.getInBackground(a.getIdl(), ((object, e) -> {
                    if(e==null){
                        object.put("luotxem", a.getLuotxem()+1);
                        object.saveInBackground(e1 -> {
                            Intent as=new Intent(context, TTTinDang.class);
                            as.putExtra("object", a.toString());
                            as.putStringArrayListExtra("list", a.getList());
                            as.putExtra("type", "yes");
                            context.startActivity(as);
                        });
                    }
                }));
            }
        }
    }

}
