package com.example.nhadat_app.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Adapter.ListDistineAdapter;
import com.example.nhadat_app.Adapter.ListProvincesAdapter;
import com.example.nhadat_app.Adapter.ListWardAdapter;
import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.Model.Distin;
import com.example.nhadat_app.Model.Ward;
import com.example.nhadat_app.Model.province;
import com.example.nhadat_app.R;
import com.example.nhadat_app.SignIn;
import com.example.nhadat_app.databinding.FragmentDashboardBinding;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private FragmentDashboardBinding binding;
    private RelativeLayout re_1, re_2;
    private Button btnPro, btnDis, btnWard;
    private Button btnSigIn, btnDangTin;
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
    private EditText txtGia, txtTieuDe, txtDienTich, txtMota, txtHuongDat, txtPhapLy;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //set id
        re_1=root.findViewById(R.id.fragment_sigin);
        re_2=root.findViewById(R.id.fragment_dangtin);
        scr=root.findViewById(R.id.scroll);
        txtDienTich=root.findViewById(R.id.txt_dientich);
        txtGia=root.findViewById(R.id.txt_gia);
        txtHuongDat=root.findViewById(R.id.txt_huong);
        txtMota=root.findViewById(R.id.txt_mota);
        txtPhapLy=root.findViewById(R.id.txt_phaply);
        txtTieuDe=root.findViewById(R.id.txt_tieude);
        btnSigIn=root.findViewById(R.id.fragment_btnsigin);
        lj=root.findViewById(R.id.lozz);
        btnPro=root.findViewById(R.id.btn_tinhthanh);
        txt2=root.findViewById(R.id.pro_ime1);
        txt=root.findViewById(R.id.pro_ime);
        btnDis=root.findViewById(R.id.btn_quan);
        sp=root.findViewById(R.id.sp_danhmuc);
        btnWard=root.findViewById(R.id.btn_phuong);
        db=new SQLiteDatabase(getContext());
        re=root.findViewById(R.id.recycle_ds);
        btnDangTin=root.findViewById(R.id.btn_dangtin);
        list=new ArrayList<>();
        store=FirebaseStorage.getInstance();
        storageReference=store.getReference();
        list1=new ArrayList<>();
        im1=root.findViewById(R.id.img_dtin1);
        img2=root.findViewById(R.id.img_dtin2);

        //khoi tao spinner
        List ar=new ArrayList<>();
        ar.add("Mua bán");
        ar.add("Cho thuê");
        ar.add("Dự án");
        ArrayAdapter<String> arctr=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ar);
        arctr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arctr);

        //set lisstener
        setListener();
        if(ParseUser.getCurrentUser()!=null){
            re_1.setVisibility(View.GONE);
            re_2.setVisibility(View.VISIBLE);
        }
        else{
            re_1.setVisibility(View.VISIBLE);
            re_2.setVisibility(View.GONE);
        }
        return root;
    }

    private void setListener(){
        btnSigIn.setOnClickListener(this);
        btnDis.setOnClickListener(this);
        btnPro.setOnClickListener(this);
        btnWard.setOnClickListener(this);
        img2.setOnClickListener(this);
        im1.setOnClickListener(this);
        btnDangTin.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_btnsigin:{
                Intent a=new Intent(getActivity(), SignIn.class);
                a.putExtra("activity", "dangtin");
                startActivity(a);
                break;
            }
            case R.id.btn_tinhthanh:{
                getData("province", btnPro);
                break;
            }
            case R.id.btn_quan:{
                getDataDistin("distin",btnPro.getText().toString(), btnDis);
                break;
            }
            case R.id.btn_phuong:{
                getDataWard("ward", btnDis.getText().toString(), btnWard);
                break;
            }
            case R.id.img_dtin1:{
                chooseImage(21);
                break;
            }
            case R.id.img_dtin2:{
                chooseImage(20);
                break;
            }
            case R.id.btn_dangtin:{
                dangTin();
                break;
            }
        }
    }

    //dang tin
    private void dangTin(){
        ParseObject object=new ParseObject("postin");
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
        object.put("luotxem", 0);
        object.put("tinhtrang", "chưa duyệt");
        String[] arr=(Calendar.getInstance().getTime()+"").split(" ");
        object.put("timeUp", arr[2]+" "+arr[1]+" "+arr[5]+" at "+arr[3]+" UTC");

        object.saveInBackground(e -> {
            if(e==null){
                txtTieuDe.setText("");
                txt.setText("");
                txt2.setText("");
                btnPro.setText("");
                btnDis.setText("");
                btnWard.setText("");
                txtTieuDe.setText("");
                txtTieuDe.setText("");
                txtTieuDe.setText("");
                txtTieuDe.setText("");
                txtTieuDe.setText("");
                im1.setImageResource(R.drawable.circle);
                img2.setImageResource(R.drawable.circle);
                Toast.makeText(getContext(), "Đăng tin thành công", Toast.LENGTH_LONG).show();
            }
        });
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

    //adpater recycle
    private void initTodoList(List<ParseObject> ls, Button btn) {
        for(ParseObject as:ls){
            list.add(new province(String.valueOf(as.getInt("code")), as.getString("name")));
        }
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        provin=new ListProvincesAdapter(getContext(), list, btn, lj, scr);
        re.setAdapter(provin);
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
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        distin=new ListDistineAdapter(getContext(), ar, btn, lj, scr);
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
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        ward=new ListWardAdapter(getContext(), ar, btn, lj, scr);
        re.setAdapter(ward);
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filepath);
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filepath);
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
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
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