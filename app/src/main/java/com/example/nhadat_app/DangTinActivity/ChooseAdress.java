package com.example.nhadat_app.DangTinActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nhadat_app.Adapter.ListDistineAdapter;
import com.example.nhadat_app.Adapter.ListProvincesAdapter;
import com.example.nhadat_app.Adapter.ListWardAdapter;
import com.example.nhadat_app.Model.Distin;
import com.example.nhadat_app.Model.Ward;
import com.example.nhadat_app.Model.province;
import com.example.nhadat_app.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChooseAdress extends AppCompatActivity {
    private RecyclerView re;
    private ListProvincesAdapter provin;
    private ListDistineAdapter distin;
    private ListWardAdapter ward;
    private ArrayList<province> list;
    private ArrayList<Distin> list1;
    private ArrayList<Ward> list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_adress);
        re=findViewById(R.id.recycle_ds);
        list1=new ArrayList<>();
        list=new ArrayList<>();
        list2=new ArrayList<>();
    }

    //get province
    private void getData(String table, Button btn){
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
        for(ParseObject as:ls){
            list.add(new province(String.valueOf(as.getInt("code")), as.getString("name")));
        }
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        provin=new ListProvincesAdapter(this, list, btn, lj, scr);
        re.setAdapter(provin);
    }

    //get distin
    private void getDataDistin(String table, String name, Button btn){
        String id="";
        for(province as:list){
            if(as.getProvince().equalsIgnoreCase(name)==true){
                id=as.getProvinceID();
                System.out.println("id "+id);
            }
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        try {
            System.out.println("count "+query.count());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.whereEqualTo("province_code", Integer.parseInt(id));
        query.setLimit(2000);
        query.findInBackground((objects, e) -> {
            if(e==null){
                checkDistin(objects, btnDis);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //check distin
    private void checkDistin(List<ParseObject> lit, Button btn){
        ArrayList<Distin> ar=new ArrayList<>();
        HashSet<String> hs=new HashSet<>();
        for(ParseObject as:lit){
            ar.add(new Distin(String.valueOf(as.getInt("province_code")), String.valueOf(as.getInt("district_code")),
                    as.getString("district_name")));
        }
        for(Distin as:ar){
            hs.add(as.getDistin());
        }

        for(Distin as:ar){
            list1.add(new Distin(as.getDistinID(),as.getDistin()));
        }

        ar.removeAll(ar);
        for(String as:hs){
            ar.add(new Distin(as.toString()));
        }
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        distin=new ListDistineAdapter(getContext(), ar, btn, lj, scr);
        re.setAdapter(distin);
    }

    //get ward
    private void getDataWard(String table, String name, Button btn){
        lj.setVisibility(View.VISIBLE);
        scr.setVisibility(View.GONE);
        String id="";
        for(Distin as:list1){
            if(as.getDistin().equalsIgnoreCase(name)==true){
                id=as.getDistinID();
            }
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        query.setLimit(2000);
        query.whereEqualTo("district_code", Integer.parseInt(id));
        query.findInBackground((objects, e) -> {
            if(e==null){
                checkWard(objects, btnWard);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //check ward
    private void checkWard(List<ParseObject> lit, Button btn){
        HashSet<String> hs=new HashSet<>();
        ArrayList<Ward> ar=new ArrayList<>();

        for(ParseObject as:lit){
            ar.add(new Ward(String.valueOf(as.getInt("distric_code")),
                    as.getString("ward_name")));
        }

        for(Ward as:ar){
            hs.add(as.getWard());
        }

        ar.removeAll(ar);
        for(String as:hs){
            ar.add(new Ward(as.toString()));
        }
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        ward=new ListWardAdapter(getContext(), ar, btn, lj, scr);
        re.setAdapter(ward);
    }
}