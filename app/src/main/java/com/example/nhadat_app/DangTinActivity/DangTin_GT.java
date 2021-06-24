package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nhadat_app.R;

public class DangTin_GT extends AppCompatActivity {
    private EditText txt1, txt2;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_gt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt1=findViewById(R.id.gt_huongnha);
        txt2=findViewById(R.id.gt_phaply);
        btn=findViewById(R.id.gt_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt1.getText().toString().isEmpty()){
                    txt1.setError("Trống");
                }
                else if(txt2.getText().toString().isEmpty()){
                    txt2.setError("Trống");
                }
                else{
                    Intent s=getIntent();
                    Intent a=new Intent(DangTin_GT.this, DangTin_MoTa.class);
                    a.putExtra("gia", s.getStringExtra("gia"));
                    a.putExtra("dientich", s.getStringExtra("dientich"));
                    a.putExtra("danhmuc", s.getStringExtra("danhmuc"));
                    a.putExtra("tinh", s.getStringExtra("tinh"));
                    a.putExtra("huyen", s.getStringExtra("huyen"));
                    a.putExtra("xa", s.getStringExtra("xa"));
                    a.putExtra("image", s.getStringArrayListExtra("image"));
                    a.putExtra("huongnha", txt1.getText().toString());
                    a.putExtra("phaply", txt2.getText().toString());
                    startActivity(a);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}