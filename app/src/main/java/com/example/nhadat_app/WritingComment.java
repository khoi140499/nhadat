package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class WritingComment extends AppCompatActivity implements View.OnClickListener {
    private EditText txtCmt;
    private RatingBar rd;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_comment);

        setID();
    }

    private void setID(){
        txtCmt=findViewById(R.id.rating_cmt);
        rd=findViewById(R.id.rating_1);
        btn=findViewById(R.id.rating_btn);
    }

    private void setListener(){
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent as=getIntent();
        String name=as.getStringExtra("name");
        if(v.getId()==R.id.rating_btn){
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
                        for(ParseUser ass:objects){
                            putNotification(name, "Bạn vừa nhận được đánh từ "+
                                    ass.getString("fullname")+": "+txtCmt.getText().toString());
                        }
                    }
                }));
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
}