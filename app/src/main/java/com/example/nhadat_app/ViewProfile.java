package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.Model.TinDang;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView img;
    private TextView txtUser, txtDate, txtAddress, txtCount;
    private RecyclerView re;
    private ListAdapter adapter;
    private Button btnDanhGia;
    private RatingBar rd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        setID();
        setListener();
        setViewData();
    }

    private void setID(){
        img=findViewById(R.id.viewprofile_img);
        rd=findViewById(R.id.viewprofile_rating);
        txtUser=findViewById(R.id.viewprofile_fullname);
        txtDate=findViewById(R.id.viewprofile_date);
        txtAddress=findViewById(R.id.viewprofile_address);
        txtCount=findViewById(R.id.viewprofile_count);
        re=findViewById(R.id.recycle_viewprofile);
        btnDanhGia=findViewById(R.id.viewprofile_danhgia);
    }

    private void setListener(){
        btnDanhGia.setOnClickListener(this);
    }

    private void setViewData(){
        Intent a=getIntent();
        String name=a.getStringExtra("name");
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("username", name);
        query.findInBackground(((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    txtUser.setText(as.getString("fullname"));
                    String[] arr=(as.getCreatedAt()+"").split(" ");
                    txtDate.setText(arr[2] + " " + arr[1] + " " + arr[5]);
                    txtAddress.setText(as.getString("address"));
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(img);
                }
            }
        }));
        setViewRecycleView(name);

        ParseQuery<ParseObject> query1=ParseQuery.getQuery("rating");
        query1.whereEqualTo("namepost", name);
        query1.findInBackground(((objects, e) -> {
            if(e==null){
                calculatorRate(objects,rd);
            }
        }));
    }

    private void calculatorRate(List<ParseObject> list,RatingBar rd){
        float diem=0;
        for(ParseObject as:list){
            diem+=as.getDouble("rate");
        }
        rd.setRating(diem/list.size());
    }
    private void setViewRecycleView(String name){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.whereEqualTo("name", name);
        query.findInBackground(((objects, e) -> {
            if(e==null){
                setList(objects);
            }
        }));
    }

    private void setList(List<ParseObject> list){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:list){
            tinDangs.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),as.getString("name"),
                    as.getString("danhmuc"), as.getString("tinh"),
                    as.getString("huyen"), as.getString("xa"),
                    Integer.parseInt(as.getString("dientich")),
                    Long.parseLong(as.getString("gia")), as.getString("phaply"),
                    as.getString("huongnha"), as.getString("tittle"),
                    as.getString("mota"), as.getInt("luotxem"),
                    Uri.parse(as.getString("img1")), Uri.parse(as.getString("img2")),
                    as.getString("timeUp")));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new com.example.nhadat_app.Adapter.ListAdapter(tinDangs, this, ParseUser.getCurrentUser());
        re.setAdapter(adapter);

        txtCount.setText("Tin đang hiển thị - "+tinDangs.size()+" tin");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.viewprofile_danhgia){
            Intent as=getIntent();
            String name=as.getStringExtra("name");
            Intent a=new Intent(this, Rating.class);
            a.putExtra("name", name);
            startActivity(a);
        }
    }
}