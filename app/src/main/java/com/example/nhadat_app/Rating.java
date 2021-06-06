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
    private TextView txtFullName, txtDG;
    private EditText txtCmt;
    private RatingBar rd;
    private Button btn;
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
        txtFullName=findViewById(R.id.rate_fullname);
        txtCmt=findViewById(R.id.rating_cmt);
        rd=findViewById(R.id.rating_1);
        btn=findViewById(R.id.rating_btn);
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
        if(v.getId()==R.id.rating_btn){
            if(ParseUser.getCurrentUser()==null){
                Toast.makeText(this,"Đăng nhập để thực hiện đánh giá", Toast.LENGTH_LONG).show();
            }
            else{
                Intent a=getIntent();
                String name=a.getStringExtra("name");
                ParseObject object=new ParseObject("rating");
                object.put("namedg", ParseUser.getCurrentUser().getUsername());
                object.put("namepost", name);
                object.put("rate", rd.getRating());
                object.put("cmt", txtCmt.getText().toString());
                String[] arr=(Calendar.getInstance().getTime()+"").split(" ");
                object.put("timeUp", arr[2]+" "+arr[1]+" "+arr[5]+" at "+arr[3]+" UTC");
                object.saveInBackground(e -> {
                    if(e==null){
                        Toast.makeText(this, "Đánh giá thành công", Toast.LENGTH_LONG).show();
                    }
                });
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.whereEqualTo("username", name);
                query.findInBackground(((objects, e) -> {
                    if(e==null){
                        for(ParseUser as:objects){
                            putNotification(name, "Bạn vừa nhận được đánh từ "+
                                    as.getString("fullname")+": "+txtCmt.getText().toString());
                        }
                    }
                }));

            }
        }
    }

    //put notification
    private void putNotification(String name, String ds){
        JSONObject data = new JSONObject();

        try {
            data.put("alert", ds);
            data.put("title", "Nhà đất");
        } catch (JSONException e) {
            throw new IllegalArgumentException("unexpected parsing error", e);
        }

        ParsePush push = new ParsePush();
        push.setChannel(name);
        push.setData(data);
        push.sendInBackground();
    }

    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    private void setListener(){
        btn.setOnClickListener(this);
    }
}