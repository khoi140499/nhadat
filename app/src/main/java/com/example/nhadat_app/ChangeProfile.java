package com.example.nhadat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.DB.SQLiteDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.parse.Parse;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfile extends AppCompatActivity implements View.OnClickListener{
    private ImageButton btnBack;
    private Button btnCapNhat, txtDate;
    private Spinner sp;
    private EditText txtFullName, txtAddress, txtPhone;
    private CircleImageView circleImageView;
    private SQLiteDatabase db;
    private TextView proimg;
    private StorageTask uploadTask;
    private FirebaseStorage store;
    private StorageReference storageReference;
    private final int PICK_IMAGE=71;
    private Uri filepath;
    private String imageurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        setID();
        setlistener();
    }

    private void setID(){
        btnBack=findViewById(R.id.chageprofile_back);
        btnCapNhat=findViewById(R.id.btn_update);
        txtAddress=findViewById(R.id.chprofile_address);
        txtPhone=findViewById(R.id.chprofile_phone);
        txtFullName=findViewById(R.id.chprofile_fullname);
        sp=findViewById(R.id.sp_gioitinh);
        circleImageView=findViewById(R.id.img_profile);
        txtDate=findViewById(R.id.chprofile_date);
        proimg=findViewById(R.id.pro_image);


        getBack4App();
        db=new SQLiteDatabase(this);
        setUser();
        store=FirebaseStorage.getInstance();
        storageReference=store.getReference();
    }

    //set user
    private void setUser(){
        txtFullName.setText(ParseUser.getCurrentUser().getString("fullname"));
        txtDate.setText(ParseUser.getCurrentUser().getString("date"));
        txtPhone.setText(ParseUser.getCurrentUser().getString("phone"));
        txtAddress.setText(ParseUser.getCurrentUser().getString("address"));

        List gioiTinh=new ArrayList();
        gioiTinh.add("Nam");
        gioiTinh.add("Nữ");
        gioiTinh.add("Không xác định");
        ArrayAdapter<String> arctr=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gioiTinh);
        arctr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arctr);

        sp.setSelection(gioiTinh.indexOf(ParseUser.getCurrentUser().getString("sex")));
        try {
            Picasso.get().load(Uri.parse(ParseUser.getCurrentUser().getString("imgurl"))).into(circleImageView);
        }
        catch (NullPointerException e){
            circleImageView.setImageResource(R.drawable.circle);
        }

    }

    //set su kien
    private void setlistener(){
        btnBack.setOnClickListener(this);
        btnCapNhat.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        txtDate.setOnClickListener(this);
    }


    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    //su kien onclick
    @Override
    public void onClick(View v) {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        switch (v.getId()){
            case R.id.chageprofile_back:{
                Intent a=new Intent(this, Profile.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                break;
            }

            case R.id.img_profile:{
                chooseImage();
                break;
            }

            case R.id.chprofile_date:{
                DatePickerDialog a=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year2, int month2, int dayOfMonth2) {
                        txtDate.setText(year2+"/"+(month2+1)+"/"+dayOfMonth2);
                    }
                }, year, month ,day);
                a.show();
                break;
            }

            case R.id.btn_update:{
                ParseUser current=ParseUser.getCurrentUser();
                if(current!=null){
                    if(txtFullName.getText().toString().isEmpty()){
                        txtFullName.setError("Chưa nhập tên đăng nhập");
                    }
                    else if(txtAddress.getText().toString().isEmpty()){
                        txtAddress.setError("Chưa nhập địa chỉ");
                    }
                    else if(txtPhone.getText().toString().length()<10 | txtPhone.getText().toString().length()>11){
                        txtPhone.setError("Không đúng định dạng số điện thoại");
                    }
                    else{
                        current.put("fullname", txtFullName.getText().toString());
                        current.put("sex", sp.getSelectedItem().toString());
                        current.put("address", txtAddress.getText().toString());
                        current.put("phone", txtPhone.getText().toString());
                        current.put("imgurl", proimg.getText().toString());
                        current.put("date", txtDate.getText().toString());

                        current.saveInBackground(e -> {
                            if(e==null){
                                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(this, "Cập nhật thất bại, lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
            }
        }
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                circleImageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
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
                                    proimg.setText(task.getResult().toString());
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