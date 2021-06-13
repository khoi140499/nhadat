package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListChatUser;
import com.example.nhadat_app.Adapter.ListMessageAdapter;
import com.example.nhadat_app.Model.ChatUser;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView re;
    private EditText send;
    private ImageButton btn;
    private ListMessageAdapter adapter;
    private List<ChatUser> users;
    private CircleImageView img;
    private ImageButton btnInfo, btnCall, btnSms;
    private TextView fullname;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setId();
        setListener();
        setAdapter();
        setDataProfile();
        refreshMessage();
    }

    private void refreshMessage(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Message");
        query.setLimit(50);
        query.orderByAscending("createAt");
        query.findInBackground((objects, e) -> {
            if(e==null){
                setListData(objects);
            }
        });
    }

    private void setListData(List<ParseObject> listData){
        Intent a=getIntent();
        String s=a.getStringExtra("objectId");
        List<ChatUser> chatUsers=new ArrayList<>();
        for(ParseObject as:listData){
            if(as.getString("user_send").
                    equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true &&
            as.getString("user_receiver").equalsIgnoreCase(s)==true
            ){
                users.add(new ChatUser(as.getString("user_send"),
                        as.getString("user_receiver"), as.getString("message")));
                adapter.notifyDataSetChanged();
            }
            if(as.getString("user_send").equalsIgnoreCase(s)==true &&
                    as.getString("user_receiver").
                            equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true){
                users.add(new ChatUser(as.getString("user_send"),
                        as.getString("user_receiver"), as.getString("message")));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setId(){
        send=findViewById(R.id.txt_send);
        re=findViewById(R.id.recycle_message);
        btn=findViewById(R.id.btn_send);
        fullname=findViewById(R.id.mess_fullname);
        btnInfo=findViewById(R.id.mess_info);
        btnCall=findViewById(R.id.mess_call);
        btnSms=findViewById(R.id.mess_sms);
        img=findViewById(R.id.mess_imgprofile);
        users=new ArrayList<>();
    }

    private void setAdapter(){
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListMessageAdapter(this, users);
        re.setAdapter(adapter);
    }

    private void setListener(){
        btn.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnSms.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        img.setOnClickListener(this);
    }

    private void setDataProfile(){
        try {
            Intent a=getIntent();
            String s=a.getStringExtra("objectId");
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.whereEqualTo("objectId", s);
            query.findInBackground(((objects, e) -> {
                if(e==null){
                    for(ParseUser as:objects){
                        Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(img);
                        fullname.setText(as.getString("fullname"));
                    }
                }
            }));
        }catch (NullPointerException e){}
    }

    private void getDataProfile(String action, String type){
        try {
            Intent a=getIntent();
            String s=a.getStringExtra("objectId");
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.whereEqualTo("objectId", s);
            query.findInBackground(((objects, e) -> {
                if(e==null){
                    for(ParseUser as:objects){
                        if(action==null && type==null){
                            Intent intent=new Intent(this, ViewProfile.class);
                            intent.putExtra("name", as.getUsername());
                            startActivity(intent);
                        }
                        else if(action.equalsIgnoreCase(Intent.ACTION_DIAL)==true){
                            Intent intent=new Intent(action);
                            intent.setData(Uri.parse("tel:"+as.getString(type)));
                            startActivity(intent);
                        }
                        else if(action.equalsIgnoreCase(Intent.ACTION_SENDTO)==true){
                            Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+as.getString(type)));
                            intent.putExtra("sms_body"," ");
                            startActivity(intent);
                        }
                    }
                }
            }));
        }catch (NullPointerException e){}
    }

    @Override
    public void onClick(View v) {
        Intent a=getIntent();
        String s=a.getStringExtra("objectId");
        switch (v.getId()){
            case R.id.mess_call:{
                getDataProfile(Intent.ACTION_DIAL, "phone");
                break;
            }
            case R.id.mess_sms:{
                getDataProfile(Intent.ACTION_SENDTO, "phone");
                break;
            }
            case R.id.mess_info:{
               getDataProfile(null,null);
                break;
            }
            case R.id.mess_imgprofile:{
                getDataProfile(null,null);
                break;
            }
            case R.id.btn_send:{
                if(send.getText().toString().isEmpty()){
                    send.setError("Empty message");
                }
                else{
                    ParseObject object=new ParseObject("Message");
                    object.put("user_send", ParseUser.getCurrentUser().getObjectId());
                    object.put("user_receiver", s);
                    object.put("message", send.getText().toString());
                    object.saveInBackground(e -> {
                        if(e==null){
                            ChatUser as=new ChatUser(ParseUser.getCurrentUser().getObjectId(), "oasdasda",
                                    send.getText().toString());
                            users.add(as);
                            adapter.notifyDataSetChanged();
                            send.setText("");
                        }
                    });

                }
                break;
            }
        }
    }
}