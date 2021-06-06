package com.example.nhadat_app.fragmentmanager;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListTinDangAdapter;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TinAn extends Fragment {
    private RecyclerView re;
    private ListTinDangAdapter td;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_tin_an, container, false);
        re=v.findViewById(R.id.recycle_tinan);
        getBack4App();
        getData();
        return v;
    }

    private void getData() throws NullPointerException{
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.whereEqualTo("name", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("tinhtrang", "ẩn");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                getViewList(objects);
            }
        }));
    }

    private void getViewList(List<ParseObject> objects){
        ArrayList<TinDang> ar=new ArrayList<>();
        for(ParseObject as:objects){
            ar.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),as.getString("name"),
                    as.getString("danhmuc"), as.getString("tinh"),
                    as.getString("huyen"), as.getString("xa"),
                    Integer.parseInt(as.getString("dientich")),
                    Long.parseLong(as.getString("gia")), as.getString("phaply"),
                    as.getString("huongnha"), as.getString("tittle"),
                    as.getString("mota"), as.getInt("luotxem"),
                    Uri.parse(as.getString("img1")), Uri.parse(as.getString("img2"))));
        }
            re.setLayoutManager(new LinearLayoutManager(getContext()));
            td=new ListTinDangAdapter(ar, getContext());
            re.setAdapter(td);
    }

    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(getContext())
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}