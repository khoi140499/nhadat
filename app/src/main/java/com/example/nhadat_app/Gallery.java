package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.example.nhadat_app.Adapter.ListGalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {
    private RecyclerView re;
    private ListGalleryAdapter adapter;
    private String[] projection = {MediaStore.MediaColumns.DATA};
    private List<String> list;
    private ArrayList<String> list1;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        re=findViewById(R.id.imagechoose);
        btn=findViewById(R.id.btnAccep);
        list=new ArrayList<>();
        list1=new ArrayList<>();
        Cursor cursor = getContentResolver().query
                (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString
                    (cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            list.add(absolutePathOfImage);
        }
        cursor.close();
        re.setLayoutManager(new GridLayoutManager(this, 3));
        adapter=new ListGalleryAdapter(this, list, list1);
        re.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent();
                a.putStringArrayListExtra("list", list1);
                setResult(RESULT_OK,a );
                finish();
            }
        });

    }
}