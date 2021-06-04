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
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.Model.province;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

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
                getData("Mua bán");
            }
            else if(s.equalsIgnoreCase("chothue")==true){
                getData("Cho thuê");
            }
            else if(s.equalsIgnoreCase("duan")==true){
                getData("Dự án");
            }
        }catch (NullPointerException e){}
    }

    private void getData(String s){

        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.whereEqualTo("danhmuc", s);
        query.whereEqualTo("tinhtrang", "duyệt");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                viewData(objects);
            }
        }));
    }

    private void viewData(List<ParseObject> objects){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for (ParseObject as:objects){
            tinDangs.add(new com.example.nhadat_app.Model.TinDang(as.getString("name"),
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
                    setDataRecycleView(tinh, dm, "gia", gia1, gia2);
                }
                else if(sxgia.equalsIgnoreCase("Lượt xem")==true){
                    setDataRecycleView(tinh, dm, "luotxem", gia1, gia2);
                }
                break;
            }
            case R.id.filter_back:{
                ln.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void setDataRecycleView(String tinh, String danhmuc, String sxgia,
                                    String gia1, String gia2){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.whereEqualTo("tinhtrang", "duyệt");
        query.orderByDescending(sxgia);
        query.findInBackground(((objects, e) -> {
            if(e==null){
                filterData(objects, danhmuc, tinh, gia1, gia2);
            }
            else{
                Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void filterData(List<ParseObject> objects, String danhmuc, String tinh,
                            String gia1, String gia2){
        ArrayList<TinDang> td=new ArrayList<>();
        for (ParseObject as:objects){
            if(as.getString("tinh").equalsIgnoreCase(tinh)==true &&
                    as.getString("danhmuc").equalsIgnoreCase(danhmuc)==true&&
                    Long.parseLong(as.getString("gia"))>=Long.parseLong(gia1) &&
                    Long.parseLong(as.getString("gia"))<=Long.parseLong(gia2)){
                td.add(new com.example.nhadat_app.Model.TinDang(as.getString("name"),
                        as.getString("danhmuc"), as.getString("tinh"),
                        as.getString("huyen"), as.getString("xa"),
                        Integer.parseInt(as.getString("dientich")),
                        Long.parseLong(as.getString("gia")), as.getString("phaply"),
                        as.getString("huongnha"), as.getString("tittle"),
                        as.getString("mota"), as.getInt("luotxem"),
                        Uri.parse(as.getString("img1")), Uri.parse(as.getString("img2")),
                        as.getString("timeUp")));
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
        provincesAdapter=new ListProvincesAdapter(this, list, btn, ln1, sc);
        reds.setAdapter(provincesAdapter);
    }
}