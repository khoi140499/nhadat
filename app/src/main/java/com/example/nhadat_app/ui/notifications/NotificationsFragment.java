package com.example.nhadat_app.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.R;
import com.example.nhadat_app.SignIn;
import com.example.nhadat_app.fragmentmanager.ViewPagerAdapter;
import com.example.nhadat_app.databinding.FragmentNotificationsBinding;
import com.google.android.material.tabs.TabLayout;
import com.parse.Parse;
import com.parse.ParseUser;

public class NotificationsFragment extends Fragment implements View.OnClickListener {
    private FragmentNotificationsBinding binding;
    private TabLayout tab;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private LinearLayout li1, ln2;
    private Button btn;
    private SQLiteDatabase db;
    private String category;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //set id
        tab=root.findViewById(R.id.tab);
        viewPager=root.findViewById(R.id.viewPager);
        li1=root.findViewById(R.id.linear_manager);
        ln2=root.findViewById(R.id.linear_mn);
        btn=root.findViewById(R.id.btn_dangnhap);
        db=new SQLiteDatabase(getContext());
        getBack4App();

        //set tab
        adapter=new ViewPagerAdapter(getActivity().getSupportFragmentManager(),
                ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);

        ///check dang nhap
        setListener();
        checkAccount();

        return root;
    }

    //set listener
    private void setListener(){
        btn.setOnClickListener(this);
    }

    //check account
    private void checkAccount(){
        if(ParseUser.getCurrentUser()!=null){
            li1.setVisibility(View.GONE);
            ln2.setVisibility(View.VISIBLE);
        }
        else{
            li1.setVisibility(View.VISIBLE);
            ln2.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //back4app
    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(getContext())
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    //on click
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dangnhap:{
                Intent a=new Intent(getActivity(), SignIn.class);
                a.putExtra("activity", "manager");
                startActivity(a);
            }
        }
    }
}