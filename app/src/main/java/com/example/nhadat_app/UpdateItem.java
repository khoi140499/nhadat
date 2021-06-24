package com.example.nhadat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListDistineAdapter;
import com.example.nhadat_app.Adapter.ListIMAdapter;
import com.example.nhadat_app.Adapter.ListProvincesAdapter;
import com.example.nhadat_app.Adapter.ListWardAdapter;
import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.DangTinActivity.ChooseAdress;
import com.example.nhadat_app.DangTinActivity.DangTIn_DanhMuc;
import com.example.nhadat_app.DangTinActivity.DangTin_Image;
import com.example.nhadat_app.DangTinActivity.DangTin_MoTa;
import com.example.nhadat_app.Model.Distin;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.Model.Ward;
import com.example.nhadat_app.Model.province;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class UpdateItem extends AppCompatActivity implements View.OnClickListener {
    private TextView btnPro, btnDis, btnWard;
    private Button btnUpload, btnDangTin;
    private ImageButton btnBack, btnAdd;
    private RecyclerView re;
    private TextView txt, txt2;
    private Spinner sp;
    private String id;
    private ListIMAdapter adapter;
    private ArrayList<String> listImage=new ArrayList<>();
    private EditText txtGia, txtTieuDe, txtDienTich, txtMota, txtHuongDat, txtPhapLy;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        setID();
        setListener();
        receiverData();
    }

    private void setID(){
        re=findViewById(R.id.recycle_updateimage);
        btnAdd=findViewById(R.id.update_addimage);
        txtDienTich=findViewById(R.id.txt_dientichud);
        txtGia=findViewById(R.id.txt_giaud);
        txtHuongDat=findViewById(R.id.txt_huongud);
        txtMota=findViewById(R.id.txt_motaud);
        txtPhapLy=findViewById(R.id.txt_phaplyud);
        txtTieuDe=findViewById(R.id.txt_tieudeud);
        btnPro=findViewById(R.id.btn_tinhthanhud);
        txt2=findViewById(R.id.pro_ime1ud);
        txt=findViewById(R.id.pro_imeud);
        btnDis=findViewById(R.id.btn_quanud);
        sp=findViewById(R.id.sp_danhmucupdate);
        btnWard=findViewById(R.id.btn_phuongud);
        btnDangTin=findViewById(R.id.btn_dangtinup);
        btnUpload=findViewById(R.id.update_Dangtin);
        btnBack=findViewById(R.id.btnupdateback);
        btnPro.setTag("yes");
        btnDis.setTag("yes");
    }

    private void setListener(){
        btnDis.setOnClickListener(this);
        btnPro.setOnClickListener(this);
        btnWard.setOnClickListener(this);
        btnDangTin.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    private void receiverData(){
        Intent a=getIntent();
        String s=a.getStringExtra("object");
        listImage=a.getStringArrayListExtra("list");
        String[] arr=s.split("noikho");
        String sa=a.getStringExtra("type");
        TinDang tinDang=new TinDang(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5],
                Integer.parseInt(arr[6]), Long.parseLong(arr[7]), arr[8], arr[9], arr[10], arr[11],
                Integer.parseInt(arr[12]), arr[13], listImage);

        LinearLayoutManager linearLayoutManager=new
                LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        re.setLayoutManager(linearLayoutManager);
        adapter=new ListIMAdapter(this, listImage);
        re.setAdapter(adapter);

        btnPro.setText(tinDang.getTinh());
        btnDis.setText(tinDang.getHuyen());
        btnWard.setText(tinDang.getXa());
        txtDienTich.setText(tinDang.getDienTich()+"");
        txtGia.setText(tinDang.getGia()+"");
        txtHuongDat.setText(tinDang.getHuongNha());
        txtPhapLy.setText(tinDang.getPhapLy());
        txtTieuDe.setText(tinDang.getTieuDe());
        txtMota.setText(tinDang.getMoTa());

        List<String> ar=new ArrayList<>();
        ar.add("Mua bán");
        ar.add("Cho thuê");
        ar.add("Dự án");

        ArrayAdapter<String> arctr=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ar);
        arctr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arctr);
        id=tinDang.getIdl();
        sp.setSelection(ar.indexOf(tinDang.getDanhMuc()));
        if(sa.equalsIgnoreCase("update")==true){
            btnUpload.setVisibility(View.VISIBLE);
            btnDangTin.setVisibility(View.GONE);
        }
        else{
            btnUpload.setVisibility(View.GONE);
            btnDangTin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tinhthanhud:{
                Intent a=new Intent(this, ChooseAdress.class);
                a.putExtra("intent", "update");
                a.putExtra("type", "tinh");
                startActivityForResult(a, 80);
                break;
            }
            case R.id.btn_quanud:{
                if(btnDis.getTag().toString().
                        equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn tỉnh, thành", Toast.LENGTH_LONG).show();
                }else{
                    Intent a=new Intent(this, ChooseAdress.class);
                    a.putExtra("intent", "update");
                    a.putExtra("type", "quan");
                    a.putExtra("tinh", btnPro.getText().toString());
                    startActivityForResult(a, 81);
                }
                break;
            }
            case R.id.btn_phuongud:{
                if(btnPro.getTag().toString().equalsIgnoreCase("yes")==true &&
                        btnDis.getTag().toString().equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn tỉnh, thành và quận huyện", Toast.LENGTH_LONG).show();
                }
                else if(btnPro.getTag().toString().
                        equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn tỉnh, thành", Toast.LENGTH_LONG).show();
                }
                else if(btnDis.getTag().toString().equalsIgnoreCase("yes")==true){
                    Toast.makeText(this,
                            "Bạn chưa chọn quận, huyện", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent a=new Intent(this, ChooseAdress.class);
                    a.putExtra("intent", "update");
                    a.putExtra("type", "phuong");
                    a.putExtra("quan", btnDis.getText().toString());
                    a.putExtra("tinh", btnPro.getText().toString());
                    startActivityForResult(a, 82);
                }
                break;
            }
            case R.id.btn_dangtinup:{
                dangTin();
                break;
            }
            case R.id.update_Dangtin:{
                updateTD();
                break;
            }
            case R.id.update_addimage:{
                Intent a=new Intent(this, Gallery.class);
                startActivityForResult(a, 10);
                break;
            }
            case R.id.btnupdateback:{
                finish();
                break;
            }
        }
    }

    private void updateTD(){
        ParseObject object=new ParseObject("postin");
        object.put("name", ParseUser.getCurrentUser().getUsername());
        object.put("tittle", txtTieuDe.getText().toString());
        object.put("danhmuc", sp.getSelectedItem().toString());
        object.put("tinh", btnPro.getText().toString());
        object.put("huyen", btnDis.getText().toString());
        object.put("xa", btnWard.getText().toString());
        object.put("dientich", txtDienTich.getText().toString());
        object.put("gia", txtGia.getText().toString());
        object.put("huongnha", txtHuongDat.getText().toString());
        object.put("phaply", txtPhapLy.getText().toString());
        object.put("luotxem", 0);
        object.put("mota", txtMota.getText().toString());
        object.put("tinhtrang", "chưa duyệt");
        String[] arr=(Calendar.getInstance().getTime()+"").split(" ");
        String date=arr[2]+" "+arr[1]+" "+arr[5]+" at "+arr[3]+" UTC";
        object.put("timeUp", date);
        object.saveInBackground(e1-> {
            if(e1==null){
                for(String as:listImage){
                    try {
                        ParseObject objects=new ParseObject("ImagePost");
                        Bitmap chosenImage = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(),
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
        AlertDialog.Builder b = new AlertDialog.
                Builder(this);
        b.setTitle("Tạo tin đăng mới");
        b.setMessage("Bạn có thể tạo 1 tin đăng hoặc trở về trang chủ");
        b.setPositiveButton("Tạo mới",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(UpdateItem.this,
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
                        Intent intent=new Intent(UpdateItem.this,
                                MainActivity.class);
                        intent.putExtra("type", "yes");
                        startActivity(intent);
                    }
                });
        AlertDialog ax=b.create();
        ax.show();
    }

    //dang tin
    private void dangTin(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.getInBackground(id, ((object, e) -> {
            if(e==null){
                object.put("name", ParseUser.getCurrentUser().getUsername());
                object.put("tittle", txtTieuDe.getText().toString());
                object.put("danhmuc", sp.getSelectedItem().toString());
                object.put("tinh", btnPro.getText().toString());
                object.put("huyen", btnDis.getText().toString());
                object.put("xa", btnWard.getText().toString());
                object.put("dientich", txtDienTich.getText().toString());
                object.put("gia", txtGia.getText().toString());
                object.put("huongnha", txtHuongDat.getText().toString());
                object.put("phaply", txtPhapLy.getText().toString());
                object.put("mota", txtMota.getText().toString());
                object.put("tinhtrang", "chưa duyệt");

                object.saveInBackground(e1 -> {
                    if(e1==null){
                        ParseQuery<ParseObject> query1=ParseQuery.getQuery("ImagePost");
                        query1.getInBackground(id, (object1, e2) -> {
                            if(e2==null){
                                object1.deleteInBackground(e3 -> {
                                    if(e3==null){
                                        for(String as:listImage){
                                            try {
                                                ParseObject objects=new ParseObject("ImagePost");
                                                Bitmap chosenImage = MediaStore.Images.Media.getBitmap(
                                                        this.getContentResolver(),
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
                                            } catch (IOException es) {
                                                es.getMessage();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 80 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnPro.setText(data.getStringExtra("kq"));
                    btnPro.setTag("no");
                }
            });
        }
        else if(requestCode == 81 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnPro.setText(data.getStringExtra("kq-tinh"));
                    btnPro.setTag("no");
                    btnDis.setText(data.getStringExtra("kq"));
                    btnDis.setTag("no");
                }
            });
        }
        else if(requestCode == 82 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnWard.setText(data.getStringExtra("kq"));
                    btnPro.setText(data.getStringExtra("kq-tinh"));
                    btnPro.setTag("no");
                    btnDis.setText(data.getStringExtra("kq-quan"));
                    btnDis.setTag("no");
                }
            });
        }
        else if(requestCode == 10 && resultCode==RESULT_OK && data!=null){
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for(String as:data.getStringArrayListExtra("list")){
                        listImage.add(as.toString());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}