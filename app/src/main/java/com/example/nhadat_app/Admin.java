package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.Adapter.ListAdminAdapter;
import com.example.nhadat_app.Adapter.ListTinDangAdapter;
import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.Model.TinDang;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity implements View.OnClickListener {
    private TextView txt;
    private ToggleButton aSwitch;
    private RecyclerView re;
    private ImageButton btnLogOut;
    private ListAdapter adapter;
    private ListAdminAdapter adda2;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setId();
        getBack4App();
        setLister();
    }

    private void setId(){
        txt=findViewById(R.id.admin_text);
        aSwitch=findViewById(R.id.admin_onoff);
        re=findViewById(R.id.recycle_admin);
        btnLogOut=findViewById(R.id.btnlogout);
        db=new SQLiteDatabase(this);

        //khoi tao
        txt.setText(ParseUser.getCurrentUser().getUsername());
    }

    private void setLister(){
        btnLogOut.setOnClickListener(this);
        aSwitch.setOnClickListener(this);
    }

    private void setViewDataDuyet(List<ParseObject> list){
        ArrayList<TinDang> ar=new ArrayList<>();
        for(ParseObject as:list){
            ar.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),as.getString("name"),
                    as.getString("danhmuc"), as.getString("tinh"),
                    as.getString("huyen"), as.getString("xa"),
                    Integer.parseInt(as.getString("dientich")),
                    Long.parseLong(as.getString("gia")), as.getString("phaply"),
                    as.getString("huongnha"), as.getString("tittle"),
                    as.getString("mota"), as.getInt("luotxem"),

                    as.getString("timeUp")));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListAdapter(ar, this, ParseUser.getCurrentUser());
        re.setAdapter(adapter);
    }

    private void setViewDataChua(List<ParseObject> list){
        ArrayList<TinDang> ar=new ArrayList<>();
        for(ParseObject as:list){
            ar.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),as.getString("name"),
                    as.getString("danhmuc"), as.getString("tinh"),
                    as.getString("huyen"), as.getString("xa"),
                    Integer.parseInt(as.getString("dientich")),
                    Long.parseLong(as.getString("gia")), as.getString("phaply"),
                    as.getString("huongnha"), as.getString("tittle"),
                    as.getString("mota"), as.getInt("luotxem"),
                    as.getString("timeUp")));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        adda2=new ListAdminAdapter(ar, this);
        re.setAdapter(adda2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_onoff:{
                if(aSwitch.isChecked()==true){
                    ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
                    query.whereEqualTo("tinhtrang", "duyệt");
                    query.findInBackground(((objects, e) -> {
                        if(e==null){
                            setViewDataDuyet(objects);
                        }
                    }));
                }
                else{
                    ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
                    query.whereEqualTo("tinhtrang", "chưa duyệt");
                    query.findInBackground(((objects, e) -> {
                        if(e==null){
                            setViewDataChua(objects);
                        }
                    }));
                }
                break;
            }
            case R.id.btnlogout:{
                try {
                    db.xoaTK("Yes");
                    db.themTK("No");
                    ParseUser.logOut();
                    Intent a=new Intent(this, MainActivity.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }catch (NullPointerException e){}
                break;
            }
        }
    }

    //back4app
    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}