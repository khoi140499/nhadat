package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.nhadat_app.checknetwork.ConnectionNetwork;

public class ErrorPage extends AppCompatActivity {
    private RelativeLayout re, reLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_page);

        re=findViewById(R.id.reError);
        reLoad=findViewById( R.id.reLoad_errror );
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check= ConnectionNetwork.isConnected();
                reLoad.setVisibility( View.VISIBLE );
                if(check==true){
                    Intent a=new Intent(ErrorPage.this, MainActivity.class);
                    a.putExtra( "error", true );
                    startActivity(a);
                }
            }
        });
    }
}