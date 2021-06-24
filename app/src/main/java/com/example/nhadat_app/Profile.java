package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.Model.ImagePost;
import com.example.nhadat_app.Model.TinDang;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private Button back, btnDanhGia;
    private Button btnUpdate, btnLogOut, btnSignIn, btnSignUp, btnFollow, btnFollowing;
    private String loin;
    private TextView fullname, email, phone, address, date, follow, following;
    private RelativeLayout re_1, re_2;
    private SQLiteDatabase db;
    private String category="";
    private CircleImageView img;
    private RecyclerView re;
    private ListAdapter adapter;
    private ImageView imageView;
    private RatingBar rd;
    private List<ImagePost> imagePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setID();
        setListener();
        imagePosts=new ArrayList<>();
        setViewData();
        if(ParseUser.getCurrentUser()==null){
            re_1.setVisibility(View.GONE);
            re_2.setVisibility(View.VISIBLE);
        }
        else{
            re_1.setVisibility(View.VISIBLE);
            re_2.setVisibility(View.GONE);
            try {
                fullname.setText(ParseUser.getCurrentUser().getString("fullname"));
                String[] arr = ParseUser.getCurrentUser().getCreatedAt().toString().split(" ");
                date.setText(arr[2] + " " + arr[1] + " " + arr[5]);
                email.setText(ParseUser.getCurrentUser().getString("email"));
                phone.setText(ParseUser.getCurrentUser().getString("phone"));
                address.setText(ParseUser.getCurrentUser().getString("address"));
                Picasso.get().load(Uri.parse(ParseUser.getCurrentUser().
                                    getString("imgurl"))).into(img);
                Picasso.get().load(Uri.parse(ParseUser.getCurrentUser().
                        getString("imgurl"))).into(imageView);

                ParseQuery<ParseObject> query1=ParseQuery.getQuery("rating");
                query1.whereEqualTo("namepost", ParseUser.getCurrentUser().getUsername());
                query1.findInBackground(((objects, e) -> {
                    if(e==null){
                        calculatorRate(objects,rd);
                    }
                }));
            }
            catch (NullPointerException e){
                System.out.println(e.getMessage());
            }
        }
    }

    //tinh rating
    private void calculatorRate(List<ParseObject> list,RatingBar rd){
        float diem=0;
        for(ParseObject as:list){
            diem+=as.getDouble("rate");
        }
        rd.setRating(diem/list.size());
    }

    private void setID(){
        rd=findViewById(R.id.profile_rate);
        btnDanhGia=findViewById(R.id.profile_danhgia);
        imageView=findViewById(R.id.imagebackground);
        fullname=findViewById(R.id.profile_fullname);
        email=findViewById(R.id.profile_email);
        phone=findViewById(R.id.profile_phone);
        address=findViewById(R.id.profile_address);
        date=findViewById(R.id.profile_date);
        btnUpdate=findViewById(R.id.chage_profile1);
        btnLogOut=findViewById(R.id.logout);
        btnSignIn=findViewById(R.id.chage_signin);
        btnSignUp=findViewById(R.id.profile_signup);
        re_1=findViewById(R.id.re_btnchange);
        img=findViewById(R.id.image_profilee);
        re_2=findViewById(R.id.re_buttonsign);
        re=findViewById(R.id.recycle_tinluu);
        follow=findViewById(R.id.profile_follow);
        following=findViewById(R.id.profile_following);
        btnFollow=findViewById(R.id.profile_btnfollow);
        btnFollowing=findViewById(R.id.profile_btnfollowing);
    }

    private void setListener(){
        btnLogOut.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnDanhGia.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnFollowing.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setViewData(){
        if(ParseUser.getCurrentUser()!=null){
            getImage();

            ParseQuery<ParseObject> query2=ParseQuery.getQuery("follow");
            query2.whereEqualTo("user_id", ParseUser.getCurrentUser().getUsername());
            query2.findInBackground(((objects, e) -> {
                if(e==null){
                    following.setText(objects.size()+"");
                }
            }));

            ParseQuery<ParseObject> query3=ParseQuery.getQuery("follow");
            query3.whereEqualTo("user_following", ParseUser.getCurrentUser().getUsername());
            query3.findInBackground(((objects, e) -> {
                if(e==null){
                    follow.setText(objects.size()+"");
                }
            }));
        }
    }


    //get all image from class ImagePost
    private void getImage(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ImagePost");
        query.findInBackground((objects, e) -> {
            if(e==null){
                setListImage(objects);
            }
        });
    }

    //set list image with object id of class postin
    private void setListImage(List<ParseObject> list) {
        HashSet<String> hs=new HashSet<>();
        for (ParseObject as:list){
            imagePosts.add(new ImagePost(as.getParseObject("post_id"),
                    as.getParseFile("img").getUrl()+""));
        }

        for(ImagePost as:imagePosts){
            hs.add(as.getPost_id().getObjectId());
        }
        HashMap<String, List<String>> hashMap=new HashMap<String, List<String>>();
        List<String> a;
        for(String ass:hs){
            a=new ArrayList<>();
            for(ParseObject as:list){
                if(ass.toString().equalsIgnoreCase(as.getParseObject("post_id").getObjectId())==true){
                    a.add(as.getParseFile("img").getUrl()+"");
                }
            }
            hashMap.put(ass.toString(), a);
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery("SavePostin");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(((objects, e) -> {
            if(e==null){
                for(ParseObject as:objects){
                    ParseQuery<ParseObject> query1=ParseQuery.getQuery("postin");
                    query1.whereEqualTo("objectId", as.getString("tinDang"));
                    query1.findInBackground(((objects1, e1) -> {
                        if(e1==null){
                            setView(objects1, hashMap);
                        }
                    }));
                }
            }
        }));
    }

    private void setView(List<ParseObject> list, HashMap<String, List<String>> hs){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:list){
            for(Map.Entry<String, List<String>> sa:hs.entrySet()){
                if(as.getObjectId().equalsIgnoreCase(sa.getKey())==true){
                    tinDangs.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),
                            as.getString("name"), as.getString("danhmuc"),
                            as.getString("tinh"), as.getString("huyen"),
                            as.getString("xa"), Integer.parseInt(as.getString("dientich")),
                            Long.parseLong(as.getString("gia")), as.getString("phaply"),
                            as.getString("huongnha"), as.getString("tittle"),
                            as.getString("mota"), as.getInt("luotxem"),
                            as.getString("timeUp"), (ArrayList<String>) sa.getValue()));
                }
            }
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new com.example.nhadat_app.Adapter.ListAdapter(tinDangs,
                this, ParseUser.getCurrentUser());
        re.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            case R.id.profile_danhgia:{
                if(ParseUser.getCurrentUser()!=null){
                    Intent a=new Intent(this, Rating.class);
                    a.putExtra("name", ParseUser.getCurrentUser().getUsername());
                    startActivity(a);
                }
                break;
            }
            case R.id.chage_profile1:{
                Intent a=new Intent(this, ChangeProfile.class);
                startActivity(a);
                break;
            }
            case R.id.chage_signin:{
                Intent a=new Intent(this, SignIn.class);
                a.putExtra("activity", "profile");
                startActivity(a);
                break;
            }
            case R.id.profile_signup:{
                Intent a=new Intent(this, SignUp.class);
                a.putExtra("activity", "profile");
                startActivity(a);
                break;
            }
            case R.id.profile_btnfollow:{
                if(ParseUser.getCurrentUser()!=null){
                    Intent a=new Intent(this, Follow.class);
                    a.putExtra("username", ParseUser.getCurrentUser().getUsername());
                    startActivity(a);
                }
                break;
            }
            case R.id.profile_btnfollowing:{
                if(ParseUser.getCurrentUser()!=null){
                    Intent a=new Intent(this, Following.class);
                    a.putExtra("username", ParseUser.getCurrentUser().getUsername());
                    startActivity(a);
                }
                break;
            }
            case R.id.logout:{
                ParseUser.logOutInBackground(e -> {
                    if(e==null){
                        db.xoaTK("Yes");
                        db.themTK("No");
                        re_1.setVisibility(View.GONE);
                        re_2.setVisibility(View.VISIBLE);
                        fullname.setText("");
                        date.setText("");
                        email.setText("");
                        phone.setText("");
                        address.setText("");
                        img.setImageResource(R.drawable.circle);
                        imageView.setImageResource(R.drawable.backgrou);
                    } });
                break;
            }
        }
    }
}