package com.example.nhadat_app.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.Adapter.ListNewAdapter;
import com.example.nhadat_app.ImageSlider.ImageAdapter;
import com.example.nhadat_app.ItemDanhMuc;
import com.example.nhadat_app.Model.Category;
import com.example.nhadat_app.Model.ImageSlide;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.example.nhadat_app.databinding.FragmentHomeBinding;
import com.google.android.material.card.MaterialCardView;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private RecyclerView re, reMost;
    private ListAdapter adapter;
    private SliderView sliderView;
    private MaterialCardView btn1, btn2, btn3;
    private TextView txt1, txt2, txt3;
    private ImageView img1, img2, img3;
    private ListNewAdapter listNewAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        System.out.println("createView");
        View root = binding.getRoot();
        reMost=root.findViewById(R.id.recycle_hozizontal);
        re=root.findViewById(R.id.home_recycle);
        sliderView = root.findViewById(R.id.slider);
        btn1=root.findViewById(R.id.home_muaban);
        btn2=root.findViewById(R.id.home_chothue);
        btn3=root.findViewById(R.id.home_duan);
        img1=root.findViewById(R.id.img_muaban);
        img2=root.findViewById(R.id.img_chothue);
        img3=root.findViewById(R.id.img_duan);
        txt1=root.findViewById(R.id.txt_muaban);
        txt2=root.findViewById(R.id.txt_chothue);
        txt3=root.findViewById(R.id.txt_duan);
        ArrayList<ImageSlide> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new ImageSlide(R.drawable.ban1));
        sliderDataArrayList.add(new ImageSlide(R.drawable.ban2));
        sliderDataArrayList.add(new ImageSlide(R.drawable.bann2));

        ImageAdapter adapter = new ImageAdapter(getContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        getBack4App();
        getData();
        setDataTop();
        setCategory();
        setListener();
        return root;
    }

    //get data most view
    private void setDataTop(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.setLimit(10);
        query.whereEqualTo("tinhtrang","duyệt");
        query.orderByDescending("luotxem");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                getDataTop(objects);
            }
        }));
    }

    //set data most view to recycle view
    private void getDataTop(List<ParseObject> objects){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:objects){
            tinDangs.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),as.getString("name"),
                    as.getString("danhmuc"), as.getString("tinh"),
                    as.getString("huyen"), as.getString("xa"),
                    Integer.parseInt(as.getString("dientich")),
                    Long.parseLong(as.getString("gia")), as.getString("phaply"),
                    as.getString("huongnha"), as.getString("tittle"),
                    as.getString("mota"), as.getInt("luotxem"),
                    Uri.parse(as.getString("img1")), Uri.parse(as.getString("img2")),
                    as.getString("timeUp")));
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        reMost.setLayoutManager(linearLayoutManager);
        listNewAdapter=new ListNewAdapter(tinDangs, getContext(), ParseUser.getCurrentUser());
        reMost.setAdapter(listNewAdapter);
    }

    //get data new
    private void getData(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.setLimit(15);
        query.whereEqualTo("tinhtrang","duyệt");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                setAdapter(objects);
            }
        }));
    }

    //set data new to recycle view
    private void setAdapter(List<ParseObject> list){
        Collections.sort(list, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject o1, ParseObject o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:list){
            System.out.println(as.getString("tittle"));
            tinDangs.add(new com.example.nhadat_app.Model.TinDang(as.getObjectId(),as.getString("name"),
                    as.getString("danhmuc"), as.getString("tinh"),
                    as.getString("huyen"), as.getString("xa"),
                    Integer.parseInt(as.getString("dientich")),
                    Long.parseLong(as.getString("gia")), as.getString("phaply"),
                    as.getString("huongnha"), as.getString("tittle"),
                    as.getString("mota"), as.getInt("luotxem"),
                    Uri.parse(as.getString("img1")), Uri.parse(as.getString("img2")),
                    as.getString("timeUp")));
        }
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new com.example.nhadat_app.Adapter.ListAdapter(tinDangs, getContext(), ParseUser.getCurrentUser());
        re.setAdapter(adapter);
    }

    //set up category
    private void setCategory(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Category");
        query.orderByDescending("creatAt");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                setCT(objects);
            }
        }));
    }

    //set view category
    private void setCT(List<ParseObject> objects){
        List<Category> list=new ArrayList<>();
        for(ParseObject as:objects){
            list.add(new Category(as.getString("name"), as.getParseFile("img").getUrl()+""));
        }
        txt1.setText(list.get(0).getName()+"");
        Picasso.get().load(Uri.parse(list.get(0).getUrl())).into(img1);

        txt2.setText(list.get(2).getName()+"");
        Picasso.get().load(Uri.parse(list.get(2).getUrl())).into(img2);

        txt3.setText(list.get(1).getName()+"");
        Picasso.get().load(Uri.parse(list.get(1).getUrl())).into(img3);
    }

    //back4app
    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(getContext())
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setListener(){
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_chothue:{
                Intent a=new Intent(getContext(), ItemDanhMuc.class);
                a.putExtra("dmuc", "chothue");
                startActivity(a);
                break;
            }
            case R.id.home_duan:{
                Intent a=new Intent(getContext(), ItemDanhMuc.class);
                a.putExtra("dmuc", "duan");
                startActivity(a);
                break;
            }
            case R.id.home_muaban:{
                Intent a=new Intent(getContext(), ItemDanhMuc.class);
                a.putExtra("dmuc", "muaban");
                startActivity(a);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        setDataTop();
        setCategory();
    }
}