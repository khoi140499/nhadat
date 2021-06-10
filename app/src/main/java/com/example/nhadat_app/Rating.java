package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListRatingAdapter;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Rating extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView img;
    private TextView txtFullName, txtDG, txtCount;
    private Button btn1;
    private RecyclerView re;
    private ListRatingAdapter adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        setID();
        setListener();
        setView();
        getBack4App();
    }

    private void setID(){
        img=findViewById(R.id.rate_img);
        txtCount=findViewById(R.id.txt_count);
        txtFullName=findViewById(R.id.rate_fullname);
        btn1=findViewById(R.id.rating_btn2);
        txtDG=findViewById(R.id.dgia);
        re=findViewById(R.id.recycle_rating);
    }

    private void setView(){
        Intent a=getIntent();
        String name=a.getStringExtra("name");
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("username", name);
        query.findInBackground(((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(img);
                    txtFullName.setText(as.getString("fullname"));
                }
            }
        }));

        ParseQuery<ParseObject> query1=ParseQuery.getQuery("rating");
        query1.whereEqualTo("namepost", name);
        query1.findInBackground(((objects, e) -> {
            if(e==null){
                setDataRecycleView(objects);
            }
        }));

        ParseQuery<ParseObject> query2=ParseQuery.getQuery("rating");
        query2.whereEqualTo("namepost", name);
        query2.findInBackground(((objects, e) -> {
            if(e==null){
                calculatorRate(objects,txtCount);
            }
        }));
    }

    private void setDataRecycleView(List<ParseObject> list){
        ArrayList<com.example.nhadat_app.Model.Rating> ar=new ArrayList<>();
        for(ParseObject as:list){
            ar.add(new com.example.nhadat_app.Model.Rating(as.getString("namedg"),
                    as.getString("namepost"), (float)as.getDouble("rate"), as.getString("cmt"),
                    as.getString("timeUp")));
        }

        re.setLayoutManager(new LinearLayoutManager(this));
        adpater=new ListRatingAdapter(ar, this);
        re.setAdapter(adpater);
        adpater.notifyDataSetChanged();

        txtDG.setText("Tất cả đánh giá  - "+ar.size()+" đánh giá");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.rating_btn2){
            Intent as=getIntent();
            String name=as.getStringExtra("name");
            if(ParseUser.getCurrentUser()==null){
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
            }
            else if(ParseUser.getCurrentUser().getUsername().equalsIgnoreCase(name)==true){
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
            }
            else{
                Intent a=new Intent(this, WritingComment.class);
                a.putExtra("name", name);
                startActivity(a);
            }
        }
    }

    private void calculatorRate(List<ParseObject> list,TextView rd){
        float diem=0;
        for(ParseObject as:list){
            diem+=as.getDouble("rate");
        }
        System.out.println("diem "+diem/list.size());
        rd.setText(diem/list.size()+"");
    }

    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    private void setListener(){
        btn1.setOnClickListener(this);
    }
}