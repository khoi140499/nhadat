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
import com.example.nhadat_app.Model.ImagePost;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TinAn extends Fragment {
    private RecyclerView re;
    private ListTinDangAdapter td;
    private List<ImagePost> imagePosts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_tin_an, container, false);
        re=v.findViewById(R.id.recycle_tinan);
        imagePosts=new ArrayList<>();
        getBack4App();
        getImage();
        return v;
    }

    //get all image from class ImagePost
    private void getImage(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ImagePost");
        query.findInBackground((objects, e) -> {
            if(e==null){
                setListImage(objects);
            }
        });
    }

    //set list image with object id of class postin
    private void setListImage(List<ParseObject> list) {
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
        query.whereEqualTo("name", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("tinhtrang", "ẩn");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                getViewList(objects, hashMap);
            }
        }));
    }

    private void getViewList(List<ParseObject> objects, HashMap<String, List<String>> hs){
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
            re.setLayoutManager(new LinearLayoutManager(getContext()));
            td=new ListTinDangAdapter(tinDangs, getContext());
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