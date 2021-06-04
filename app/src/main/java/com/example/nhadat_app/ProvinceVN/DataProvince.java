package com.example.nhadat_app.ProvinceVN;

import android.content.Context;

import com.example.nhadat_app.Model.Distin;
import com.example.nhadat_app.Model.Ward;
import com.example.nhadat_app.Model.province;
import com.example.nhadat_app.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;

public class DataProvince {
    private Context context;
    private ArrayList<province> list;
    private ArrayList<Distin> list1;
    private ArrayList<Ward> list2;
    public DataProvince() {
        getBack4App();

    }

    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(String.valueOf((R.string.back4app_app_id)))
                .clientKey(String.valueOf((R.string.back4app_client_key)))
                .server(String.valueOf(R.string.back4app_server_url))
                .build());
    }

    public ArrayList<province> getProvince(){
        ArrayList<province>  list=new ArrayList<>();

        return list;
    }

    private ArrayList<Distin> getDistin(){
        list1=new ArrayList<>();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("province");
        query.findInBackground((objects, e) -> {
            if(e==null){
                for(ParseObject as:objects){
                    Distin a=new Distin(as.getString("province_code"),
                            ("district_code"),
                            as.getString("district_name"));
                    list1.add(a);
                }
            }
        });
        query.cancel();
        return list1;
    }

    private ArrayList<Ward> getWard(){
        list2=new ArrayList<>();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ward");
        query.findInBackground((objects, e) -> {
            if(e==null){
                for(ParseObject as:objects){
                    Ward a=new Ward(as.getString("district_code"), as.getString("ward_name"));
                    list2.add(a);
                }
            }
        });
        query.cancel();
        return list2;
    }

    public HashSet<String> getDis(String name){
        HashSet<String> hs=new HashSet<>();
        String id="";
        for(province as:list){
            if(name.equalsIgnoreCase(as.getProvince())==true){
                id=as.getProvinceID();
            }
        }

        for (Distin as:list1){
            if(id.equalsIgnoreCase(String.valueOf(as.getProvinceID()))==true){
                hs.add(as.getDistin());
            }
        }
        return hs;
    }

    public HashSet<String> getWar(String name, String name1){
        HashSet<String> hs=new HashSet<>();
        String id="";
        String id1="";
        for (province as:list){
            if(name1.equalsIgnoreCase(as.getProvince())==true){
                id1=as.getProvinceID();
            }
        }
        for(Distin as:list1){
            if(name.equalsIgnoreCase(as.getDistin())==true && id1.equalsIgnoreCase(String.valueOf(as.getProvinceID()))==true){
                id=as.getDistinID();
            }
        }

        for(Ward as:list2){
            if(id.equalsIgnoreCase(as.getDistinID())==true){
                hs.add(as.getWard());
            }
        }
        return hs;
    }
}
