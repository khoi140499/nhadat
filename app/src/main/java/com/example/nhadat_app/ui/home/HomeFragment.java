package com.example.nhadat_app.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.ImageSlider.ImageAdapter;
import com.example.nhadat_app.ItemDanhMuc;
import com.example.nhadat_app.Model.ImageSlide;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.example.nhadat_app.databinding.FragmentHomeBinding;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private RecyclerView re;
    private ListAdapter adapter;
    private SliderView sliderView;
    private ProgressDialog progressDialog;
    private Button btn1, btn2, btn3;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        re=root.findViewById(R.id.home_recycle);
        sliderView = root.findViewById(R.id.slider);
        btn1=root.findViewById(R.id.home_muaban);
        btn2=root.findViewById(R.id.home_chothue);
        btn3=root.findViewById(R.id.home_duan);
        progressDialog=new ProgressDialog(getActivity());
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
        setListener();
        return root;
    }

    private void getData(){
        progressDialog.show();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.setLimit(2000);
        query.whereEqualTo("tinhtrang","duyệt");
        query.orderByDescending("luotxem");
        query.findInBackground(((objects, e) -> {
            if(e==null){
                progressDialog.dismiss();
                setAdapter(objects);
            }
        }));
    }

    private void setAdapter(List<ParseObject> list){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:list){
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
}