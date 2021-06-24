package com.example.nhadat_app.DangTinActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.nhadat_app.MainActivity;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.example.nhadat_app.UpdateItem;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nhadat_app.databinding.ActivityDangTinMoTaBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DangTin_MoTa extends AppCompatActivity implements View.OnClickListener {
    private EditText txt1, txt2;
    private TextView txtCount1, txtCount2;
    private Button btn1, btn2;
    private ProgressDialog dialog;
    private List list=new ArrayList();
    private TinDang td;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_mo_ta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setID();
        setListener();
    }

    private void setID(){
        txt1=findViewById(R.id.mota_tieude);
        txt2=findViewById(R.id.mota_txt);
        txtCount1=findViewById(R.id.count_tt);
        txtCount2=findViewById(R.id.count_mt);
        btn1=findViewById(R.id.mota_button);
        btn2=findViewById(R.id.mota_view);
        dialog=new ProgressDialog(this);
    }

    private void setListener(){
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtCount1.setText(txt1.getText().toString().length()+"/50");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtCount2.setText(txt2.getText().toString().length()+"/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.mota_view){
            Intent s=getIntent();
            String[] arr=(Calendar.getInstance().getTime()+"").split(" ");
            String danhmuc=s.getStringExtra("danhmuc");
            String tinh=s.getStringExtra("tinh");
            String huyen=s.getStringExtra("huyen");
            String xa=s.getStringExtra("xa");
            String huongnha=s.getStringExtra("huongnha");
            String phaply=s.getStringExtra("phaply");
            String dientich=s.getStringExtra("dientich");
            String gia=s.getStringExtra("gia");
            ArrayList<String> list=s.getStringArrayListExtra("image");
            String tieude=txt1.getText().toString();
            String mota=txt2.getText().toString();
            String date=arr[2]+" "+arr[1]+" "+arr[5]+" at "+arr[3]+" UTC";
            td=new TinDang(ParseUser.getCurrentUser().getUsername(), danhmuc, tinh, huyen, xa,
                    Integer.parseInt(dientich), Long.parseLong(gia), phaply, huongnha, tieude,
                    mota, 0, date);
            Intent a=new Intent(this, UpdateItem.class);
            a.putExtra("type", "update");
            a.putExtra("object", td.toString());
            a.putStringArrayListExtra("list", s.getStringArrayListExtra("image"));
            startActivity(a);
        }
        else if(v.getId()==R.id.mota_button){
            if(txt1.getText().toString().isEmpty()){
                txt1.setError("Trống");
            }
            else if(txt2.getText().toString().isEmpty()){
                txt2.setError("Trống");
            }
            else{
                Intent s=getIntent();
                String[] arr=(Calendar.getInstance().getTime()+"").split(" ");
                String danhmuc=s.getStringExtra("danhmuc");
                String tinh=s.getStringExtra("tinh");
                String huyen=s.getStringExtra("huyen");
                String xa=s.getStringExtra("xa");
                String huongnha=s.getStringExtra("huongnha");
                String phaply=s.getStringExtra("phaply");
                String dientich=s.getStringExtra("dientich");
                String gia=s.getStringExtra("gia");
                ArrayList<String> list=s.getStringArrayListExtra("image");
                String tieude=txt1.getText().toString();
                String mota=txt2.getText().toString();
                String date=arr[2]+" "+arr[1]+" "+arr[5]+" at "+arr[3]+" UTC";
                td=new TinDang(ParseUser.getCurrentUser().getUsername(), danhmuc, tinh, huyen, xa,
                        Integer.parseInt(dientich), Long.parseLong(gia), phaply, huongnha, tieude,
                        mota, 0, date);

                ParseObject object=new ParseObject("postin");
                object.put("name", ParseUser.getCurrentUser().getUsername());
                object.put("tittle", tieude);
                object.put("danhmuc", danhmuc);
                object.put("tinh", tinh);
                object.put("huyen", huyen);
                object.put("xa", xa);
                object.put("dientich", dientich);
                object.put("gia", gia);
                object.put("huongnha", huongnha);
                object.put("phaply", phaply);
                object.put("mota",mota);
                object.put("luotxem", 0);
                object.put("tinhtrang", "chưa duyệt");
                object.put("timeUp", date);

                object.saveInBackground(e1-> {
                    if(e1==null){
                        for(String as:list){
                            try {
                                ParseObject objects=new ParseObject("ImagePost");
                                Bitmap chosenImage = MediaStore.Images.Media.getBitmap(
                                        DangTin_MoTa.this.getContentResolver(),
                                        Uri.fromFile(new File(as.toString())));
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                chosenImage.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                ParseFile parseFile = new ParseFile
                                        (UUID.randomUUID().toString()+".jpg", bytes);
                                objects.put("post_id", object);
                                objects.put("img", parseFile);
                                objects.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                        }
                                        else{
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                e.getMessage();
                            }
                        }
                    }
                    else{
                        System.out.println(e1.getMessage());
                    }
                });
                if(list.size()!=0){
                    AlertDialog.Builder b = new AlertDialog.
                            Builder(DangTin_MoTa.this);
                    b.setTitle("Tạo tin đăng mới");
                    b.setMessage("Bạn có thể tạo 1 tin đăng hoặc trở về trang chủ");
                    b.setPositiveButton("Tạo mới",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(DangTin_MoTa.this,
                                            DangTIn_DanhMuc.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                    b.setNegativeButton("Trang chủ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(DangTin_MoTa.this,
                                            MainActivity.class);
                                    intent.putExtra("type", "yes");
                                    startActivity(intent);
                                }
                            });
                    AlertDialog ax=b.create();
                    ax.show();
                }
            }
        }
    }
}