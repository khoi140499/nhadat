package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.nhadat_app.Adapter.ListDanhMucAdapter;
import com.example.nhadat_app.R;

import java.util.ArrayList;
import java.util.List;

public class DangTIn_DanhMuc extends AppCompatActivity {
    private RecyclerView re;
    private ListDanhMucAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_danh_muc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        re=findViewById(R.id.recycle_danhmuc);
        List<String> ar=new ArrayList<>();
        ar.add("Mua bán");
        ar.add("Cho thuê");
        ar.add("Dự án");
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListDanhMucAdapter(this, ar);
        re.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}