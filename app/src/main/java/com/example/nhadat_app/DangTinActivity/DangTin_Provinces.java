package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.R;

import java.util.ArrayList;

public class DangTin_Provinces extends AppCompatActivity implements View.OnClickListener {
    private Button btn;
    private TextView btnTinh, btnQuan, btnPhuong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_provinces);

        setID();
        setListener();
    }

    private void setID(){
        btn=findViewById(R.id.province_btn);
        btnTinh=findViewById(R.id.dangtin_tinhthanh);
        btnQuan=findViewById(R.id.dangtin_quanhuyen);
        btnPhuong=findViewById(R.id.dangtin_phuongxa);
        btnTinh.setTag("yes");
        btnQuan.setTag("yes");
        btn.setVisibility(View.VISIBLE);
    }

    private void setListener(){
        btn.setOnClickListener(this);
        btnTinh.setOnClickListener(this);
        btnQuan.setOnClickListener(this);
        btnPhuong.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 90 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnTinh.setText(data.getStringExtra("kq"));
                    btnTinh.setTag("no");
                }
            });
        }
        else if(requestCode == 91 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnTinh.setText(data.getStringExtra("kq-tinh"));
                    btnTinh.setTag("no");
                    btnQuan.setText(data.getStringExtra("kq"));
                    btnQuan.setTag("no");
                }
            });
        }
        else if(requestCode == 92 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnPhuong.setText(data.getStringExtra("kq"));
                    btnTinh.setText(data.getStringExtra("kq-tinh"));
                    btnTinh.setTag("no");
                    btnQuan.setText(data.getStringExtra("kq-quan"));
                    btnQuan.setTag("no");
                    btn.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dangtin_tinhthanh:{
                Intent a=new Intent(this, ChooseAdress.class);
                a.putExtra("type", "tinh");
                startActivityForResult(a, 90);
                break;
            }
            case R.id.dangtin_quanhuyen:{
                if(btnTinh.getTag().toString().
                        equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn tỉnh, thành", Toast.LENGTH_LONG).show();
                }else{
                    Intent a=new Intent(this, ChooseAdress.class);
                    a.putExtra("type", "quan");
                    a.putExtra("tinh", btnTinh.getText().toString());
                    startActivityForResult(a, 91);
                }
                break;
            }
            case R.id.dangtin_phuongxa:{
                if(btnTinh.getTag().toString().equalsIgnoreCase("yes")==true &&
                btnQuan.getTag().toString().equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn tỉnh, thành và quận huyện", Toast.LENGTH_LONG).show();
                }
                else if(btnTinh.getTag().toString().
                        equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn tỉnh, thành", Toast.LENGTH_LONG).show();
                }
                else if(btnQuan.getTag().toString().equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn quận, huyện", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent a=new Intent(this, ChooseAdress.class);
                    a.putExtra("type", "phuong");
                    a.putExtra("quan", btnQuan.getText().toString());
                    a.putExtra("tinh", btnTinh.getText().toString());
                    startActivityForResult(a, 92);
                }
                break;
            }
            case R.id.province_btn:{
                Intent s=getIntent();
                ArrayList<String> list=s.getStringArrayListExtra("image");
                Intent a=new Intent(this, DangTin_TT.class);
                a.putStringArrayListExtra("image", list);
                a.putExtra("danhmuc", s.getStringExtra("danhmuc"));
                a.putExtra("tinh", btnTinh.getText().toString());
                a.putExtra("huyen", btnQuan.getText().toString());
                a.putExtra("xa", btnPhuong.getText().toString());
                startActivity(a);
                break;
            }
        }
    }

}