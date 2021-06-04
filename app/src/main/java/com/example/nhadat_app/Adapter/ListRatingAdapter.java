package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.Rating;
import com.example.nhadat_app.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRatingAdapter extends RecyclerView.Adapter<ListRatingAdapter.Rating> {
    private List<com.example.nhadat_app.Model.Rating> list;
    private Context context;

    public ListRatingAdapter(List<com.example.nhadat_app.Model.Rating> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NotNull
    @Override
    public Rating onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View v=layoutInflater.inflate(R.layout.ratingcard, parent, false);
        return new Rating(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  Rating holder, int position) {
        com.example.nhadat_app.Model.Rating a=list.get(position);
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("username", a.getNamedg());
        query.findInBackground(((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(holder.img);
                    holder.username.setText(as.getString("fullname"));
                }
            }
        }));
        holder.cmt.setText(a.getCmt());
        holder.rd.setRating(a.getRate());

        String s= Calendar.getInstance().getTime()+"";
        String[] arr=s.split(" ");
        String date=arr[2]+" "+arr[1]+" "+arr[5]+" "+arr[3];
        System.out.println(a.getDate());
        String[] arr2=(a.getDate()+"").split(" ");
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

        }catch (ParseException |NullPointerException es){
            System.out.println(es.getMessage());
        }
        System.out.println("d3 "+d3+" d4 "+d4 +" mini "+dff);
        long kq= TimeUnit.MILLISECONDS.toMinutes(dff);
        System.out.println(kq);
        kq=Math.abs(kq);
        if(kq<=59){
            holder.date.setText(kq+" phút trước");
        }
        else if(kq>59){
            if(kq/60>0 && kq/60<=23){
                holder.date.setText((long) kq/60+" giờ trước");
            }
            else if(kq/60>23){
                {
                    if(kq/(60*24) > 0 && kq/(60*24) <=30){
                        holder.date.setText((long)kq/(60*24)+" ngày trước");
                    }
                    else{
                        holder.date.setText((long)kq/(60*24*30)+" tháng trước");
                    }
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class Rating extends RecyclerView.ViewHolder{
        private CircleImageView img;
        private TextView username, cmt, date;
        private RatingBar rd;
        public Rating(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.ratecard_img);
            username=itemView.findViewById(R.id.card_fullname);
            cmt=itemView.findViewById(R.id.card_cmt);
            date=itemView.findViewById(R.id.card_date);
            rd=itemView.findViewById(R.id.card_rate);
        }
    }
}
