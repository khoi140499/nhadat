package com.example.nhadat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListAdapter;
import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.checknetwork.ConnectionNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.databinding.ActivityMainBinding;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private RelativeLayout re1, re2, reSearch;
    private SQLiteDatabase db;
    private CircleImageView btnProfile;
    private NavController navController;
    private SearchView sv;
    private LinearLayout ln;
    private RecyclerView re;
    private ProgressDialog progressDialog;
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db=new SQLiteDatabase(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

//        =======================================

        setID();
        setAnimationLayout();
        getBack4App();
        checkLayout();
        setImageCicrle();
        setListener();

    }

    //set id
    private void setID(){
        re1 = findViewById(R.id.re1);
        re2 = findViewById(R.id.re_home);
        btnProfile=findViewById(R.id.btn_profile);
        sv=findViewById(R.id.search_home);
        re=findViewById(R.id.recycle_search);
        sv.setClickable(false);
        reSearch=findViewById(R.id.bar_nav);
        progressDialog=new ProgressDialog(this);

        String s=db.getTK();
        if(s.equalsIgnoreCase("null")==true){
            db.themTK("No");
        }

    }

    //set iamge profile
    private void setImageCicrle(){
        try {
            Picasso.get().load(Uri.parse(ParseUser.getCurrentUser().getString("imgurl"))).into(btnProfile);
        }catch (NullPointerException e){
            btnProfile.setImageResource(R.drawable.male_user_30px);
        }
    }

    //back4app
    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        if(ParseUser.getCurrentUser()!=null){
            final ArrayList<String> channels = new ArrayList<>();
            channels.add(ParseUser.getCurrentUser().getUsername());
            channels.add(ParseUser.getCurrentUser().getUsername()+"tindang");
            channels.add(ParseUser.getCurrentUser().getUsername()+"follow");
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("GCMSenderId", "267196795826");
            installation.put("channels", channels);
            installation.saveInBackground();
        }
    }

    //su kien
    private void setListener(){
        btnProfile.setOnClickListener(this);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                reSearch.setLayoutParams(new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ParseQuery<ParseObject> query1y=ParseQuery.getQuery("postin");
                query1y.whereContains("tittle", query);
                query1y.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            setViewOnData(objects);
                        }
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        sv.findViewById(R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSearch.setLayoutParams(new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, 130));
            }
        });
    }


    //get data searchview
    private void setViewOnData(List<ParseObject> list){
        ArrayList<TinDang> tinDangs=new ArrayList<>();
        for(ParseObject as:list){
            if(as.getString("tinhtrang").equalsIgnoreCase("duyệt")==true){
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
        }
        adapter=new com.example.nhadat_app.Adapter.ListAdapter(tinDangs, MainActivity.this, ParseUser.getCurrentUser());
        re.setLayoutManager(new LinearLayoutManager(this));
        re.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //animation
    private void setAnimationLayout(){
        Animation ani= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        ani.setStartOffset(1200);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                check();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        re1.startAnimation(ani);
    }

    //check layout
    private void checkLayout(){
        try {
            Intent a=getIntent();
            if(a.getStringExtra("type").equalsIgnoreCase("yes")==true){
                re2.setVisibility(View.VISIBLE);
                re1.setVisibility(View.GONE);
            }
            else if(a.getStringExtra("type").equalsIgnoreCase("dtin")==true){
                re2.setVisibility(View.VISIBLE);
                re1.setVisibility(View.GONE);
                NavGraph navGraph=navController.getGraph();
                navGraph.setStartDestination(R.id.navigation_dashboard);
                navController.setGraph(navGraph);
            }
            else if(a.getStringExtra("type").equalsIgnoreCase("mnager")==true){
                re2.setVisibility(View.VISIBLE);
                re1.setVisibility(View.GONE);
                NavGraph navGraph=navController.getGraph();
                navGraph.setStartDestination(R.id.navigation_notifications);
                navController.setGraph(navGraph);
            }
            else if(a.getStringExtra("type").equalsIgnoreCase("message")==true){
                re2.setVisibility(View.VISIBLE);
                re1.setVisibility(View.GONE);
                NavGraph navGraph=navController.getGraph();
                navGraph.setStartDestination(R.id.navigation_message);
                navController.setGraph(navGraph);
            }
        }catch (NullPointerException e){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //check layout start
    private void check(){
        boolean check= ConnectionNetwork.isConnected();
        if(check==true){
            try {
                if(ParseUser.getCurrentUser()==null){
                    re2.setVisibility(View.VISIBLE);
                    re1.setVisibility(View.GONE);
                }
                else if(ParseUser.getCurrentUser()!=null){
                    if(ParseUser.getCurrentUser().getUsername().equalsIgnoreCase("khoi1404admin")==true){
                        Intent a=new Intent(this, Admin.class);
                        startActivity(a);
                    }
                    else{
                        re2.setVisibility(View.VISIBLE);
                        re1.setVisibility(View.GONE);
                    }
                }
            }catch (NullPointerException e){

            }
        }
        else{
            Intent a=new Intent(MainActivity.this, ErrorPage.class);
            startActivity(a);
        }
    }

    //onclick listener
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profile:{
                Intent a=new Intent(this, Profile.class);
                startActivity(a);
            }
        }
    }
}