package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.Adapter.ListProvincesAdapter;
import com.example.nhadat_app.Model.ImagePost;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.Model.province;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ItemDanhMuc extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnBack;
    private EditText txt1, txt2;
    private Button btnLoc, btnAcc, btnTinh, btnb;
    private ListAdapter adapter;
    private ListProvincesAdapter provincesAdapter;
    private RecyclerView re, reds;
    private LinearLayout ln, ln1;
    private Spinner danhmuc, sapxep;
    private ScrollView sc;
    private List<ImagePost> imagePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_danh_muc);

        setID();
        setListener();
        receiverDate();
        setDataSpinner();
    }

    private void setID(){
        btnBack=findViewById(R.id.btnbackitem);
        txt1=findViewById(R.id.txt_gia1);
        txt2=findViewById(R.id.txt_gia2);
        btnLoc=findViewById(R.id.btn_filter);
        re=findViewById(R.id.recycle_item);
        danhmuc=findViewById(R.id.filter_danhmuc);
        sapxep=findViewById(R.id.filter_sapxep);
        btnAcc=findViewById(R.id.filter_accept);
        btnTinh=findViewById(R.id.filter_tinhthanh);
        reds=findViewById(R.id.recycle_filterds);
        ln=findViewById(R.id.linear_filter);
        ln1=findViewById(R.id.filter_ds);
        sc=findViewById(R.id.crolls);
        btnb=findViewById(R.id.filter_back);
        imagePosts=new ArrayList<>();
    }

    private void setListener(){
        btnBack.setOnClickListener(this);
        btnLoc.setOnClickListener(this);
        btnAcc.setOnClickListener(this);
        btnb.setOnClickListener(this);
        btnTinh.setOnClickListener(this);
    }

    private void setDataSpinner(){
        List<String> ar=new ArrayList<>();
        List<String> ar1=new ArrayList<>();
        ar.add("Mua bán");
        ar.add("Cho thuê");
        ar.add("Dự án");
        ar1.add("Lượt xem");
        ar1.add("Giá");
        ArrayAdapter<String> arctr=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ar);
        arctr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        danhmuc.setAdapter(arctr);

        ArrayAdapter<String> arctr2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ar1);
        arctr2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sapxep.setAdapter(arctr2);
    }

    private void receiverDate(){
        Intent a=getIntent();
        try {
            String s=a.getStringExtra("dmuc");
            if(s.equalsIgnoreCase("muaban")==true){
                getImage("Mua bán");
            }
            else if(s.equalsIgnoreCase("chothue")==true){
                getImage("Cho thuê");
            }
            else if(s.equalsIgnoreCase("duan")==true){
                getImage("Dự án");
            }
        }catch (NullPointerException e){}
    }

    //get all image from class ImagePost
    private void getImage(String s){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ImagePost");
        query.findInBackground((objects, e) -> {
            if(e==null){
                setListImage(objects, s);
            }
        });
    }

    //set list image with object id of class postin
    private void setListImage(List<ParseObject> list, String s) {
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
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.whereEqualTo("danhmuc", s);
        query.whereEqualTo("tinhtrang", "duyệt");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                viewData(objects, hashMap);
            }
        }));
    }

    private void viewData(List<ParseObject> objects, HashMap<String, List<String>> hs){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:objects){
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
        adapter=new ListAdapter(tinDangs, this, ParseUser.getCurrentUser());
        re.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnbackitem:{
                finish();
                break;
            }
            case R.id.btn_filter:{
                ln.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.filter_tinhthanh:{
                getData("province", btnTinh);
                break;
            }
            case R.id.filter_accept:{
                ln.setVisibility(View.GONE);
                String dm=danhmuc.getSelectedItem().toString();
                String sxgia=sapxep.getSelectedItem().toString();
                String tinh=btnTinh.getText().toString();
                String gia1=txt1.getText().toString();
                String gia2=txt2.getText().toString();

                if(sxgia.equalsIgnoreCase("Giá")==true){
                    getAllImageFilter(tinh, dm, "gia", gia1, gia2);
                }
                else if(sxgia.equalsIgnoreCase("Lượt xem")==true){
                    getAllImageFilter(tinh, dm, "luotxem", gia1, gia2);
                }
                break;
            }
            case R.id.filter_back:{
                ln.setVisibility(View.GONE);
                break;
            }
        }
    }

    //get image filter
    private void getAllImageFilter(String tinh, String danhmuc, String sxgia,
                                   String gia1, String gia2){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ImagePost");
        query.findInBackground((objects, e) -> {
            if(e==null){
                setDataRecycleView(objects, tinh, danhmuc, sxgia, gia1, gia2);
            }
        });
    }

    private void setDataRecycleView(List<ParseObject> list, String tinh, String danhmuc,
                                    String sxgia, String gia1, String gia2){
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

        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.whereEqualTo("tinhtrang", "duyệt");
        query.orderByDescending(sxgia);
        query.findInBackground(((objects, e) -> {
            if(e==null){
                filterData(objects, danhmuc, tinh, gia1, gia2, hashMap);
            }
            else{
                Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void filterData(List<ParseObject> objects, String danhmuc, String tinh,
                            String gia1, String gia2, HashMap<String, List<String>> hs){
        ArrayList<TinDang> td=new ArrayList<>();
        for (ParseObject as:objects){
            if(as.getString("tinh").equalsIgnoreCase(tinh)==true &&
                    as.getString("danhmuc").equalsIgnoreCase(danhmuc)==true&&
                    Long.parseLong(as.getString("gia"))>=Long.parseLong(gia1) &&
                    Long.parseLong(as.getString("gia"))<=Long.parseLong(gia2)){
                for(Map.Entry<String, List<String>> sa:hs.entrySet()){
                    if(as.getObjectId().equalsIgnoreCase(sa.getKey())==true){
                        td.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),
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
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListAdapter(td, this, ParseUser.getCurrentUser());
        re.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //get province
    private void getData(String table, Button btn){
        ln1.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        query.findInBackground((objects, e) -> {
            if(e==null){
                initTodoList(objects, btn);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //adpater recycle
    private void initTodoList(List<ParseObject> ls, Button btn) {
        ArrayList<province> list=new ArrayList<>();
        for(ParseObject as:ls){
            list.add(new province(String.valueOf(as.getInt("code")), as.getString("name")));
        }
        reds.setLayoutManager(new LinearLayoutManager(this));
        provincesAdapter=new ListProvincesAdapter(this, list);
        reds.setAdapter(provincesAdapter);
    }
}