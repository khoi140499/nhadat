package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nhadat_app.ImageSlider.ImageAdapter;
import com.example.nhadat_app.ImageSlider.ImageSlideTT;
import com.example.nhadat_app.Model.ImageSlide;
import com.example.nhadat_app.Model.TinDang;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TTTinDang extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnBack;
    private TextView txtTittle, txtAd, txtGia, txtDienTich, txtMota, txtHuongNha, txtPhapLy
            ,txtUsername;
    private Button btnUpdate, btnPhone, btnSms, btnViwe;
    private CircleImageView imgUser;
    private SliderView sliderView;
    private LinearLayout ln;
    private RatingBar rd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tttin_dang);
        setID();
        checkLayout();
        setData();
        setListener();
    }

    private void setID(){
        btnBack=findViewById(R.id.tt_btnback);
        rd=findViewById(R.id.tt_rating);
        txtTittle=findViewById(R.id.tt_tittle);
        txtAd=findViewById(R.id.tt_diachia);
        txtDienTich=findViewById(R.id.tt_dt);
        txtGia=findViewById(R.id.tt_gia);
        txtHuongNha=findViewById(R.id.tt_huongnha);
        txtMota=findViewById(R.id.tt_mota);
        txtPhapLy=findViewById(R.id.tt_phaply);
        txtUsername=findViewById(R.id.tt_username);
        sliderView = findViewById(R.id.slidertt);
        imgUser=findViewById(R.id.tt_imageprofile);
        btnPhone=findViewById(R.id.tindang_phone);
        btnSms=findViewById(R.id.tindang_sms);
        ln=findViewById(R.id.ln2);
        btnViwe=findViewById(R.id.btnViewProfile);
    }

    private void setListener(){
        btnViwe.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnPhone.setOnClickListener(this);
        btnSms.setOnClickListener(this);
    }

    private void checkLayout(){
        try {
            Intent a=getIntent();
            String s=a.getStringExtra("type");
            if(s.equalsIgnoreCase("yes")==true){
                ln.setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException e){

        }

    }

    private void setData(){
        Intent a=getIntent();
        String s=a.getStringExtra("object");
        String[] arr=s.split("noikho");
        TinDang tinDang=new TinDang(arr[0], arr[1], arr[2], arr[3], arr[4], Integer.parseInt(arr[5]),
                Long.parseLong(arr[6]), arr[7], arr[8], arr[9], arr[10], Integer.parseInt(arr[11]),
                Uri.parse(arr[12]), Uri.parse(arr[13]));
        //image
        ArrayList<ImageSlide> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new ImageSlide(tinDang.getImg1()));
        sliderDataArrayList.add(new ImageSlide(tinDang.getImg2()));
        ImageSlideTT adapter = new ImageSlideTT(this, sliderDataArrayList);
//        sliderView.setAutoCycleDirection(SliderView.LAYOUT_MODE_CLIP_BOUNDS);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        //tittle, dt,...
        txtTittle.setText(tinDang.getTieuDe());
        if(tinDang.getGia()/1000000 >0 && tinDang.getGia()/1000000<=999){
            txtGia.setText((long)tinDang.getGia()/1000000+" triệu");
        }
        else if(tinDang.getGia()/1000000000 >0){
            txtGia.setText((long)tinDang.getGia()/1000000000+" tỉ");
        }

        txtAd.setText(tinDang.getXa()+", "+tinDang.getHuyen()+", "+tinDang.getTinh());
        txtPhapLy.setText(tinDang.getPhapLy());
        txtDienTich.setText(tinDang.getDienTich()+" m2");
        txtMota.setText(tinDang.getMoTa());
        txtHuongNha.setText(tinDang.getHuongNha());

        //image circle
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("username", tinDang.getUserName());
        query.findInBackground(((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(imgUser);
                    txtUsername.setText(as.getString("fullname"));
                }
            }
        }));

        ParseQuery<ParseObject> query1=ParseQuery.getQuery("rating");
        query1.whereEqualTo("namepost", tinDang.getUserName());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnViewProfile:{
                Intent a=getIntent();
                String s=a.getStringExtra("object");
                String[] arr=s.split("noikho");
                TinDang tinDang=new TinDang(arr[0], arr[1], arr[2], arr[3], arr[4], Integer.parseInt(arr[5]),
                        Long.parseLong(arr[6]), arr[7], arr[8], arr[9], arr[10], Integer.parseInt(arr[11]),
                        Uri.parse(arr[12]), Uri.parse(arr[13]));
                Intent as=new Intent(this, ViewProfile.class);
                as.putExtra("name", tinDang.getUserName());
                startActivity(as);
                break;
            }
            case R.id.tt_btnback:{
                finish();
                break;
            }
            case R.id.tindang_phone:{
                Intent a=getIntent();
                String s=a.getStringExtra("object");
                String[] arr=s.split("noikho");
                TinDang tinDang=new TinDang(arr[0], arr[1], arr[2], arr[3], arr[4], Integer.parseInt(arr[5]),
                        Long.parseLong(arr[6]), arr[7], arr[8], arr[9], arr[10], Integer.parseInt(arr[11]),
                        Uri.parse(arr[12]), Uri.parse(arr[13]));
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.whereEqualTo("username", tinDang.getUserName());
                query.findInBackground(((objects, e) -> {
                    if(e==null){
                        for(ParseObject as:objects){
                            setDATA(as.getString("phone"));
                        }
                    }
                }));

                break;
            }
            case R.id.tindang_sms:{
                Intent a=getIntent();
                String s=a.getStringExtra("object");
                String[] arr=s.split("noikho");
                TinDang tinDang=new TinDang(arr[0], arr[1], arr[2], arr[3], arr[4], Integer.parseInt(arr[5]),
                        Long.parseLong(arr[6]), arr[7], arr[8], arr[9], arr[10], Integer.parseInt(arr[11]),
                        Uri.parse(arr[12]), Uri.parse(arr[13]));
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.whereEqualTo("username", tinDang.getUserName());
                query.findInBackground(((objects, e) -> {
                    if(e==null){
                        for(ParseObject as:objects){
                            setSMS(as.getString("phone"), tinDang.getTieuDe());
                        }
                    }
                }));
                break;
            }
        }

    }
    private void setDATA(String phone){
        Intent as=new Intent(Intent.ACTION_DIAL);
        as.setData(Uri.parse("tel:"+phone));
        startActivity(as);
    }

    private void setSMS(String phone, String tieude){
        Intent as=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone));
        as.putExtra("sms_body",tieude);
        startActivity(as);
    }
}