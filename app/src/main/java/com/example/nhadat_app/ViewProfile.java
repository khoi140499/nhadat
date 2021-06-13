package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.Model.TinDang;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView img;
    private ImageView imgview;
    private TextView txtUser, txtDate, txtAddress, txtCount, txtFollow, txtFollowing;
    private RecyclerView re;
    private ListAdapter adapter;
    private Button btnDanhGia, btnFollow, btnFolowing, btnFL;
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
        txtFollow=findViewById(R.id.txt_follow);
        txtFollowing=findViewById(R.id.txt_follwing);
        imgview=findViewById(R.id.viewprofile_imgbackground);
        img=findViewById(R.id.viewprofile_img);
        rd=findViewById(R.id.viewprofile_rating);
        txtUser=findViewById(R.id.viewprofile_fullname);
        txtDate=findViewById(R.id.viewprofile_date);
        txtAddress=findViewById(R.id.viewprofile_address);
        txtCount=findViewById(R.id.viewprofile_count);
        re=findViewById(R.id.recycle_viewprofile);
        btnDanhGia=findViewById(R.id.viewprofile_danhgia);
        btnFollow=findViewById(R.id.btn_follow);
        btnFolowing=findViewById(R.id.btn_following);
        btnFL=findViewById(R.id.viewprofile_follow);
    }

    private void setListener(){
        btnDanhGia.setOnClickListener(this);
        btnFL.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnFolowing.setOnClickListener(this);
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
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(imgview);
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

        ParseQuery<ParseObject> query2=ParseQuery.getQuery("follow");
        query2.whereEqualTo("user_id", name);
        query2.findInBackground(((objects, e) -> {
            if(e==null){
                txtFollowing.setText(objects.size()+"");
            }
        }));

        ParseQuery<ParseObject> query3=ParseQuery.getQuery("follow");
        query3.whereEqualTo("user_following", name);
        query3.findInBackground(((objects, e) -> {
            if(e==null){
                txtFollow.setText(objects.size()+"");
            }
        }));
        if(ParseUser.getCurrentUser()!=null){
            ParseQuery<ParseObject> query4=ParseQuery.getQuery("follow");
            query4.whereEqualTo("user_id", ParseUser.getCurrentUser().getUsername());
            query4.findInBackground(((objects, e) -> {
                if(e==null){
                    for(ParseObject as:objects){
                        if(as.getString("user_following").equalsIgnoreCase(name)==true){
                            btnFL.setText("Huỷ theo dõi");
                        }
                    }
                }
                else {
                    btnFL.setText("Theo dõi");
                }
            }));
        }
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
        switch (v.getId()){
            case R.id.viewprofile_danhgia:{
                Intent as=getIntent();
                String name=as.getStringExtra("name");
                Intent a=new Intent(this, Rating.class);
                a.putExtra("name", name);
                startActivity(a);
                break;
            }
            case R.id.viewprofile_follow:{
                Intent as=getIntent();
                String name=as.getStringExtra("name");
                String objectID;
                if(ParseUser.getCurrentUser()==null){
                    Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
                }
                else if(ParseUser.getCurrentUser().getUsername().equalsIgnoreCase(name)==true){
                    Toast.makeText(this, "Không thể thực hiện", Toast.LENGTH_LONG).show();
                }
                else{
                    if(btnFL.getText().toString().equalsIgnoreCase("Theo dõi")==true){
                        ParseObject object=new ParseObject("follow");
                        object.put("user_id", ParseUser.getCurrentUser().getUsername());
                        object.put("user_following", name);
                        object.saveInBackground(e -> {
                            if(e==null){
                                btnFL.setText("Huỷ theo dõi");
                                txtFollow.setText(Integer.parseInt(txtFollow.getText().toString())+1+" ");
                                JSONObject data=new JSONObject();
                                try {
                                    data.put("alert", ParseUser.getCurrentUser()
                                            .getString("fullname")+" vừa theo dõi bạn");
                                    data.put("title", "Nhà đất");
                                } catch (JSONException es) {
                                    throw new IllegalArgumentException("unexpected parsing error", es);
                                }

                                ParsePush push = new ParsePush();
                                push.setChannel(name+"follow");
                                push.setData(data);
                                push.sendInBackground();
                            }
                        });
                    }
                    else if(btnFL.getText().toString().equalsIgnoreCase("Huỷ theo dõi")==true){
                        ParseQuery<ParseObject> query=ParseQuery.getQuery("follow");
                        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getUsername());
                        query.whereEqualTo("user_following", name);
                        query.findInBackground(((objects, e) -> {
                            if(e==null){
                                for(ParseObject aas:objects){
                                    ParseQuery<ParseObject> query1=ParseQuery.getQuery("follow");
                                    query1.getInBackground(aas.getObjectId(), ((object, e1) -> {
                                        if(e1==null){
                                            object.deleteInBackground(e2 -> {
                                                if(e2==null){
                                                    btnFL.setText("Theo dõi");
                                                }
                                            });
                                        }
                                    }));
                                }
                            }
                        }));
                    }
                }
                break;
            }
            case R.id.btn_follow:{
                Intent a=new Intent(this, Follow.class);
                a.putExtra("type", "follow");
                startActivity(a);
                break;
            }
            case R.id.btn_following:{
                Intent a=new Intent(this, Follow.class);
                a.putExtra("type", "following");
                startActivity(a);
                break;
            }
        }

    }
}