package com.example.nhadat_app.DangTinActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

        checkIntent();
    }

    //check data receiver from intent dangtin_provinces
    private void checkIntent(){
        Intent a=getIntent();
        String type=a.getStringExtra("type");
        try {
            if(type.equalsIgnoreCase("tinh")==true){
                getData("province");
            }
            else if(type.equalsIgnoreCase("quan")==true){
                String tinh=a.getStringExtra("tinh");
                getDataDistin("distin", tinh);
            }
            else if(type.equalsIgnoreCase("phuong")==true){
                String tinh=a.getStringExtra("tinh");
                String quan=a.getStringExtra("quan");
                getDataWard("ward", quan, tinh);
            }
        }catch (NullPointerException e){
            e.getMessage();
        }
    }
    //get province
    private void getData(String table){
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        query.findInBackground((objects, e) -> {
            if(e==null){
                initTodoList(objects);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //adpater recycle
    private void initTodoList(List<ParseObject> ls) {
        for(ParseObject as:ls){
            list.add(new province(String.valueOf(as.getInt("code")), as.getString("name")));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        provin=new ListProvincesAdapter(this, list);
        re.setAdapter(provin);
    }

    //get distin
    private void getDataDistin(String table, String name){
        String id="";
        ParseQuery<ParseObject> query1=ParseQuery.getQuery("province");
        query1.findInBackground((objects, e) -> {
            if(e==null){
                getIDProvinc(objects, name, name);
            }
        });
    }

    //get id province
    private void getIDProvinc(List<ParseObject> lis3t, String name, String tinh){
        String id="";
        for(ParseObject as:lis3t){
            list.add(new province(as.getNumber("code")+"", as.getString("name")));
        }

        for(province as:list){
            if(as.getProvince().equalsIgnoreCase(name)==true){
                id=as.getProvinceID();
            }
        }

        ParseQuery<ParseObject> query=ParseQuery.getQuery("distin");
        query.whereEqualTo("province_code", Integer.parseInt(id));
        query.setLimit(2000);
        query.findInBackground((objects, e) -> {
            if(e==null){
                checkDistin(objects, tinh);
            }
        });
    }

    //check distin
    private void checkDistin(List<ParseObject> lit, String tinh){
        ArrayList<Distin> ar=new ArrayList<>();
        HashSet<String> hs=new HashSet<>();
        for(ParseObject as:lit){
            ar.add(new Distin(String.valueOf(as.getInt("province_code")),
                    String.valueOf(as.getInt("district_code")),
                    as.getString("district_name")));
        }
        for(Distin as:ar){
            hs.add(as.getDistin());
        }
        ar.clear();
        for(String as:hs){
            ar.add(new Distin(as.toString()));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        distin=new ListDistineAdapter(this, ar, tinh);
        re.setAdapter(distin);
    }

    //get ward
    private void getDataWard(String table, String name, String tinh){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("distin");
        query.whereEqualTo("district_name", name);
        query.findInBackground((objects, e) -> {
            if(e==null){
                getListDistin(objects, name, tinh);
            }
        });
    }

    //getListDistin
    private void getListDistin(List<ParseObject> objects, String quan, String tinh){
        int id=0;
        for(ParseObject as:objects){
            id= (int) as.getNumber("district_code");
            break;
        }
        ParseQuery<ParseObject> query1=ParseQuery.getQuery("ward");
        query1.setLimit(2000);
        query1.whereEqualTo("district_code", id);
        query1.findInBackground((object, e) -> {
            if(e==null){
                checkWard(object, quan, tinh);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //check ward
    private void checkWard(List<ParseObject> lit, String quan, String tinh){
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
        re.setLayoutManager(new LinearLayoutManager(this));
        ward=new ListWardAdapter(this, ar, quan, tinh);
        re.setAdapter(ward);
    }
}