package com.example.nhadat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.nhadat_app.Adapter.ListProvincesAdapter;
import com.example.nhadat_app.Adapter.ListWardAdapter;
import com.example.nhadat_app.DB.SQLiteDatabase;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class UpdateItem extends AppCompatActivity implements View.OnClickListener {
    private Button btnPro, btnDis, btnWard;
    private Button btnSigIn, btnDangTin;
    private ImageButton btnBack;
    private SQLiteDatabase db;
    private String category="";
    private LinearLayout lj;
    private ListProvincesAdapter provin;
    private ListDistineAdapter distin;
    private ListWardAdapter ward;
    private RecyclerView re;
    private ScrollView scr;
    private ArrayList<province> list;
    private ArrayList<Distin> list1;
    private ImageView im1, img2;
    private ArrayList<Ward> list2;
    private StorageTask uploadTask;
    private FirebaseStorage store;
    private StorageReference storageReference;
    private TextView txt, txt2;
    private Uri filepath;
    private Spinner sp;
    private String id;
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
        scr=findViewById(R.id.scrollud);
        txtDienTich=findViewById(R.id.txt_dientichud);
        txtGia=findViewById(R.id.txt_giaud);
        txtHuongDat=findViewById(R.id.txt_huongud);
        txtMota=findViewById(R.id.txt_motaud);
        txtPhapLy=findViewById(R.id.txt_phaplyud);
        txtTieuDe=findViewById(R.id.txt_tieudeud);
        lj=findViewById(R.id.lozz);
        btnPro=findViewById(R.id.btn_tinhthanhud);
        txt2=findViewById(R.id.pro_ime1ud);
        txt=findViewById(R.id.pro_imeud);
        btnDis=findViewById(R.id.btn_quanud);
        sp=findViewById(R.id.sp_danhmucupdate);
        btnWard=findViewById(R.id.btn_phuongud);
        db=new SQLiteDatabase(this);
        re=findViewById(R.id.recycle_dsud);
        btnDangTin=findViewById(R.id.btn_dangtinup);
        list=new ArrayList<>();
        store=FirebaseStorage.getInstance();
        storageReference=store.getReference();
        list1=new ArrayList<>();
        im1=findViewById(R.id.img_update1);
        img2=findViewById(R.id.img_update2);
        btnBack=findViewById(R.id.btnupdateback);
    }

    private void setListener(){
        btnDis.setOnClickListener(this);
        btnPro.setOnClickListener(this);
        btnWard.setOnClickListener(this);
        img2.setOnClickListener(this);
        im1.setOnClickListener(this);
        btnDangTin.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void receiverData(){
        Intent a=getIntent();
        String s=a.getStringExtra("object");
        System.out.println(s);
        String[] arr=s.split("noikho");
        TinDang tinDang=new TinDang(arr[14],arr[0], arr[1], arr[2], arr[3], arr[4], Integer.parseInt(arr[5]),
                Long.parseLong(arr[6]), arr[7], arr[8], arr[9], arr[10], Integer.parseInt(arr[11]),
                Uri.parse(arr[12]), Uri.parse(arr[13]));
        Picasso.get().load(tinDang.getImg1()).into(im1);
        Picasso.get().load(tinDang.getImg2()).into(img2);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tinhthanhud:{
                getData("province", btnPro);
                break;
            }
            case R.id.btn_quanud:{
                getDataDistin("distin",btnPro.getText().toString(), btnDis);
                break;
            }
            case R.id.btn_phuongud:{
                getDataWard("ward", btnDis.getText().toString(), btnWard);
                break;
            }
            case R.id.img_update1:{
                chooseImage(21);
                break;
            }
            case R.id.img_update2:{
                chooseImage(20);
                break;
            }
            case R.id.btn_dangtinup:{
                dangTin();
                break;
            }
            case R.id.btnupdateback:{
                finish();
                break;
            }
        }
    }

    //dang tin
    private void dangTin(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("postin");
        query.getInBackground(id, ((object, e) -> {
            if(e==null){
                object.put("name", ParseUser.getCurrentUser().getUsername());
                object.put("tittle", txtTieuDe.getText().toString());
                object.put("img1", txt.getText().toString());
                object.put("img2", txt2.getText().toString());
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

                object.saveInBackground();
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            }
        }));
    }

    //get province
    private void getData(String table, Button btn){
        lj.setVisibility(View.VISIBLE);
        scr.setVisibility(View.GONE);
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        query.findInBackground((objects, e) -> {
            if(e==null){
                initTodoList(objects, btn);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //get distin
    private void getDataDistin(String table, String name, Button btn){
        lj.setVisibility(View.VISIBLE);
        scr.setVisibility(View.GONE);
        String id="";
        for(province as:list){
            if(as.getProvince().equalsIgnoreCase(name)==true){
                id=as.getProvinceID();
                System.out.println("id "+id);
            }
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        try {
            System.out.println("count "+query.count());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.whereEqualTo("province_code", Integer.parseInt(id));
        query.setLimit(2000);
        query.findInBackground((objects, e) -> {
            if(e==null){
                checkDistin(objects, btnDis);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //check distin
    private void checkDistin(List<ParseObject> lit, Button btn){
        ArrayList<Distin> ar=new ArrayList<>();
        HashSet<String> hs=new HashSet<>();
        for(ParseObject as:lit){
            ar.add(new Distin(String.valueOf(as.getInt("province_code")), String.valueOf(as.getInt("district_code")),
                    as.getString("district_name")));
        }
        for(Distin as:ar){
            hs.add(as.getDistin());
        }

        for(Distin as:ar){
            list1.add(new Distin(as.getDistinID(),as.getDistin()));
        }

        ar.removeAll(ar);
        for(String as:hs){
            ar.add(new Distin(as.toString()));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        distin=new ListDistineAdapter(this, ar, btn, lj, scr);
        re.setAdapter(distin);
    }

    //get ward
    private void getDataWard(String table, String name, Button btn){
        lj.setVisibility(View.VISIBLE);
        scr.setVisibility(View.GONE);
        String id="";
        for(Distin as:list1){
            if(as.getDistin().equalsIgnoreCase(name)==true){
                id=as.getDistinID();
            }
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery(table);
        query.setLimit(2000);
        query.whereEqualTo("district_code", Integer.parseInt(id));
        query.findInBackground((objects, e) -> {
            if(e==null){
                checkWard(objects, btnWard);
            }
            else{
                System.out.println("error "+e.getMessage());
            }
        });
    }

    //check ward
    private void checkWard(List<ParseObject> lit, Button btn){
        HashSet<String> hs=new HashSet<>();
        ArrayList<Ward> ar=new ArrayList<>();

        for(ParseObject as:lit){
            ar.add(new Ward(String.valueOf(as.getInt("distric_code")),
                    as.getString("ward_name")));
        }

        for(Ward as:ar){
            hs.add(as.getWard());
        }

        ar.removeAll(ar);
        for(String as:hs){
            ar.add(new Ward(as.toString()));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        ward=new ListWardAdapter(this, ar, btn, lj, scr);
        re.setAdapter(ward);
    }

    //adpater recycle
    private void initTodoList(List<ParseObject> ls, Button btn) {
        for(ParseObject as:ls){
            list.add(new province(String.valueOf(as.getInt("code")), as.getString("name")));
        }
        re.setLayoutManager(new LinearLayoutManager(this));
        provin=new ListProvincesAdapter(this, list, btn, lj, scr);
        re.setAdapter(provin);
    }

    //chon image
    private void chooseImage(int pick){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 21 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                im1.setImageBitmap(bitmap);
                uploadImage(txt);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(requestCode == 20 && resultCode == RESULT_OK
                && data !=null && data.getData()!=null){
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                img2.setImageBitmap(bitmap);
                uploadImage(txt2);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(TextView txt){
        if(filepath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Đang tải...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            uploadTask=ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    txt.setText(task.getResult().toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Đang tải.. "+(int)progress+"%");
                        }
                    });
        }
    }
}