package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.nhadat_app.Adapter.ListFollowAdapter;
import com.example.nhadat_app.Adapter.ListFollowingAdapater;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Following extends AppCompatActivity {
    private RecyclerView re;
    private ListFollowingAdapater adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        setID();
        loadData();
    }

    private void setID(){
        re=findViewById(R.id.recycle_follows1);
    }

    private void loadData(){
        Intent a=getIntent();
        String s=a.getStringExtra("username");
        setData(s);
    }

    private void setData(String s){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("follow");
        query.whereEqualTo("user_id", s);
        query.findInBackground((objects, e) -> {
            if(e==null){
                setArrayList(objects);
            }
        });
    }

    private void setArrayList(List<ParseObject> list){
        List<com.example.nhadat_app.Model.Follow> list1=new ArrayList<>();
        for(ParseObject as:list){
            list1.add(new com.example.nhadat_app.Model.Follow(as.getObjectId(),
                    as.getString("user_id"), as.getString("user_following")));
        }

        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListFollowingAdapater(this, list1);
        re.setAdapter(adapter);
    }
}