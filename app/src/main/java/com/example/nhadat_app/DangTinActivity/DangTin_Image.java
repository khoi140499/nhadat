package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nhadat_app.R;

public class DangTin_Image extends AppCompatActivity {
    private CardView cv;
    private RecyclerView re;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cv=findViewById(R.id.dangtin_imagechooser);
        re=findViewById(R.id.recycle_image);
        btn=findViewById(R.id.dangtin_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(DangTin_Image.this, DangTin_Provinces.class);
                startActivity(a);
            }
        });

        Intent a=getIntent();
        Toast.makeText(this, a.getStringExtra("danhmuc"), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}