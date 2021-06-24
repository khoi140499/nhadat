package com.example.nhadat_app.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Gallery;
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
import com.example.nhadat_app.DangTinActivity.DangTIn_DanhMuc;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private FragmentDashboardBinding binding;
    private RelativeLayout re_2;
    private LinearLayout re_1;
    private Button btnSigIn;
    private LinearLayout lj;
    private Uri filepath;public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //set id
        re_1=root.findViewById(R.id.fragment_sigin);
        re_2=root.findViewById(R.id.fragment_dangtin);
        btnSigIn=root.findViewById(R.id.fragment_btnsigin);

        //set lisstener
        setListener();
        if(ParseUser.getCurrentUser()!=null){
            re_1.setVisibility(View.GONE);
            Intent a=new Intent(getContext(), DangTIn_DanhMuc.class);
            startActivity(a);
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
        }
    }
}