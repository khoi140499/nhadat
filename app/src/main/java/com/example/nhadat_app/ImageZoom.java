package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.widget.ImageView;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ImageZoom extends AppCompatActivity {
    private ZoomageView img;
    private ScaleGestureDetector scaleGestureDetector;
    private float mscale=1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_zoom);
        img=findViewById(R.id.imagezoom);

        Intent a=getIntent();
        Picasso.get().load(Uri.parse(a.getStringExtra("uri"))).into(img);
    }
}