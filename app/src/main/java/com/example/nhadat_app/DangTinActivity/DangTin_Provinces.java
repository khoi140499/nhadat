package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nhadat_app.R;

public class DangTin_Provinces extends AppCompatActivity implements View.OnClickListener {
    private Button btn, btnTinh, btnQuan, btnPhuong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_provinces);


    }

    private void setID(){
        btn=findViewById(R.id.province_btn);
        btnTinh=findViewById(R.id.dangtin_tinhthanh);
        btnQuan=findViewById(R.id.dangtin_quanhuyen);
        btnPhuong=findViewById(R.id.dangtin_phuongxa);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dangtin_tinhthanh:{

                break;
            }
            case R.id.dangtin_phuongxa:{

                break;
            }
            case R.id.dangtin_quanhuyen:{

                break;
            }
            case R.id.province_btn:{

                break;
            }
        }
    }
}