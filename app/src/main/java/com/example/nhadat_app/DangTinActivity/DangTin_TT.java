package com.example.nhadat_app.DangTinActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nhadat_app.R;

public class DangTin_TT extends AppCompatActivity{
    private EditText txt1, txt2;
    private TextView tv1;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_tt);

        setID();
        listener();
    }

    private void setID(){
        txt1=findViewById(R.id.tt_gia);
        txt2=findViewById(R.id.tt_dientich);
        tv1=findViewById(R.id.tt_gt);
        btn=findViewById(R.id.tt_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void listener(){
        txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txt1.getText().toString().isEmpty()){
                    tv1.setText("");
                }
                else{
                    Long kq=Long.parseLong(txt1.getText().toString());
                    if(kq/1000>0 && kq/1000<=999){
                        tv1.setText((long)kq/1000+" nghìn đồng");
                    }
                    else{
                        if(kq/1000000>0 && kq/1000000<=999){
                            tv1.setText((long)kq/1000000+" triệu đồng");
                        }
                        else{
                            if(kq/1000000000>0){
                                tv1.setText((long)kq/1000000000+" tỉ đồng");
                            }
                        }
                    }
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=getIntent();
                Intent a=new Intent(DangTin_TT.this, DangTin_GT.class);
                a.putExtra("gia", txt1.getText().toString());
                a.putExtra("dientich", txt2.getText().toString());
                a.putExtra("danhmuc", s.getStringExtra("danhmuc"));
                a.putExtra("tinh", s.getStringExtra("tinh"));
                a.putExtra("huyen", s.getStringExtra("huyen"));
                a.putExtra("xa", s.getStringExtra("xa"));
                a.putExtra("image", s.getStringArrayListExtra("image"));
                startActivity(a);
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