package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListIMAdapter;
import com.example.nhadat_app.Gallery;
import com.example.nhadat_app.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DangTin_Image extends AppCompatActivity {
    private ImageButton cv;
    private RecyclerView re;
    private Button btn;
    private ListIMAdapter adapter;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cv=findViewById(R.id.dangtin_imagechooser);
        re=findViewById(R.id.recycle_image);
        btn=findViewById(R.id.dangtin_button);
        btn.setVisibility(View.VISIBLE);
        list=new ArrayList<>();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=getIntent();
                if(list.size()==0){
                    Toast.makeText(DangTin_Image.this, "Bạn phải chọn ít nhất 1 ảnh",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Intent a=new Intent(DangTin_Image.this, DangTin_Provinces.class);
                    a.putExtra("danhmuc", s.getStringExtra("danhmuc"));
                    a.putStringArrayListExtra("image", list);
                    startActivity(a);
                }
            }
        });
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(DangTin_Image.this, Gallery.class);
                startActivityForResult(a, 90);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==90 && resultCode==RESULT_OK && data!=null){
                    list=data.getStringArrayListExtra("list");
                    LinearLayoutManager linearLayoutManager=new
                            LinearLayoutManager(DangTin_Image.this);
                    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    re.setLayoutManager(linearLayoutManager);
                    adapter=new ListIMAdapter(DangTin_Image.this, list);
                    re.setAdapter(adapter);

        }
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