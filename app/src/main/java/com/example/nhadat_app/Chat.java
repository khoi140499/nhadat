package com.example.nhadat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.Adapter.ListChatUser;
import com.example.nhadat_app.Adapter.ListMessageAdapter;
import com.example.nhadat_app.Model.ChatUser;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    private ParseLiveQueryClient parseLiveQueryClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setId();
        setListener();
        setBack4App();
        setDataProfile();
        loadAllMessage();
        liveQueryMessage();


    }
    //load all message from db
    private void loadAllMessage(){
        Intent a=getIntent();
        String s=a.getStringExtra("objectId");

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Message");
        query.orderByAscending("createAt");
        query.findInBackground((objects, e) -> {
            if(e==null){
                setListData(objects);
            }
        });
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ListMessageAdapter(this, users, s);
        re.setAdapter(adapter);
    }

    //set list data when query and filter message
    private void setListData(List<ParseObject> listData){
        Intent a=getIntent();
        String s=a.getStringExtra("objectId");
        List<ChatUser> chatUsers=new ArrayList<>();
        for(ParseObject as:listData){
            if(as.getString("user_send").
                    equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true &&
                    as.getString("user_receiver").equalsIgnoreCase(s)==true ||
                    as.getString("user_send").equalsIgnoreCase(s)==true &&
                            as.getString("user_receiver").
                                    equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true){
                users.add(new ChatUser(as.getString("user_send"),
                        as.getString("user_receiver"), as.getString("message"),
                        as.getCreatedAt()+""));
            }
        }
        sortList(users);
    }

    //sort list according createAt
    private void sortList(List<ChatUser> list){
        Collections.sort(list, new Comparator<ChatUser>() {
            @Override
            public int compare(ChatUser o1, ChatUser o2) {
                return o1.getCreateAt().compareTo(o2.getCreateAt());
            }
        });
    }


    //live query message realtime
    private void liveQueryMessage(){
        if(parseLiveQueryClient!=null){
            ParseQuery<ParseObject> query=ParseQuery.getQuery("Message");
            query.orderByAscending("createAt");
            SubscriptionHandling<ParseObject> subscriptionHandling=parseLiveQueryClient.subscribe(query);
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE,
                    new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                        @Override
                        public void onEvent(ParseQuery<ParseObject> query, ParseObject object) {
                            Handler handler=new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    addChat(object);
                                    sortList(users);
                                    re.scrollToPosition(0);
                                }
                            });
                        }
                    });
        }
    }


    //configure back4app
    private void setBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
        try {
            parseLiveQueryClient=ParseLiveQueryClient.Factory.getClient(new URI("wss://khoi1404.b4a.io/"));
        }catch (URISyntaxException r){
            System.out.println(r.getMessage());
        }
    }

    //add chat to db and recycleview
    private void addChat(ParseObject object){
        Intent a=getIntent();
        String s=a.getStringExtra("objectId");
        if(object.getString("user_send").equalsIgnoreCase(s)==true
                && object.getString("user_receiver").
                equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true){
            ChatUser user=new ChatUser(object.getString("user_send"),
                    object.getString("user_receiver"),
                    object.getString("message"), object.getCreatedAt()+"");
            users.add( user);
        }
        else if(object.getString("user_send").
                equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true
                && object.getString("user_receiver").equalsIgnoreCase(s)==true){
            ChatUser user=new ChatUser(object.getString("user_send"),
                    object.getString("user_receiver"),
                    object.getString("message"), object.getCreatedAt()+"");
            users.add(user);
        }
    }

    //initialization id
    private void setId() {
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

    //add onclick listener
    private void setListener(){
        btn.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnSms.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        img.setOnClickListener(this);
    }


    //load data profile to image and fullname
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


    //query data profile
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
                            send.setText("");
                            InputMethodManager imm= null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            }
                            imm.hideSoftInputFromWindow(send.getWindowToken(),0);
                            ParseQuery<ParseUser> query=ParseUser.getQuery();
                            query.whereEqualTo("objectId", s);
                            query.findInBackground((objects, e1) -> {
                                if(e1==null){
                                    for(ParseUser as:objects){
                                        JSONObject data=new JSONObject();
                                        try {
                                            data.put("alert",
                                                    as.getString("fullname")+": "
                                                    +send.getText().toString());
                                            data.put("title", "Nhà đất");
                                        } catch (JSONException es) {
                                            throw new IllegalArgumentException("unexpected parsing error", es);
                                        }

                                        ParsePush push = new ParsePush();
                                        push.setChannel(s+"message");
                                        push.setData(data);
                                        push.sendInBackground();
                                    }
                                }
                            });

                        }
                    });

                }
                break;
            }
        }
    }
}