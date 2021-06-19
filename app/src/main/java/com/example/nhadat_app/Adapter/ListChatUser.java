package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Chat;
import com.example.nhadat_app.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListChatUser extends RecyclerView.Adapter<ListChatUser.chatUser> {
    private Context context;
    private List<String> list;

    public ListChatUser(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public chatUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.chatusercard, parent, false);
        return new chatUser(v);
    }

    @Override
    public void onBindViewHolder(@NonNull chatUser holder, int position) {
        String s=list.get(position);
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("objectId", s);
        query.findInBackground((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(holder.img);
                    holder.txt.setText(as.getString("fullname"));
                }
            }
        });

        ParseQuery<ParseObject> query1=ParseQuery.getQuery("Message");
        query1.orderByDescending("createAt");
        query1.findInBackground((objects, e) -> {
            if (e == null) {
                loadData(objects, s, holder.txt1);
            }
        });

    }

    //load message into textview
    private void loadData(List<ParseObject> objects, String s, TextView txt){
        List<String> al=new ArrayList<>();
        for(ParseObject as:objects){
            if(as.getString("user_send").
                    equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true &&
                    as.getString("user_receiver").equalsIgnoreCase(s)==true ||
                    as.getString("user_send").equalsIgnoreCase(s)==true &&
                            as.getString("user_receiver").
                                    equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true){
                al.add(as.getString("user_send")+"noimessage"+as.getString("message"));
            }
        }

        if(al.size()>0){
            String t=al.get(al.size()-1).toString();
            String[] arr=t.split("noimessage");

            ParseQuery<ParseUser> query2=ParseUser.getQuery();
            query2.whereEqualTo("objectId", arr[0]);
            query2.findInBackground((object, e) -> {
                if(e==null){
                    for(ParseUser as:object){
                        if(as.getObjectId().equalsIgnoreCase
                                (ParseUser.getCurrentUser().getObjectId())==true){
                            txt.setText("Bạn: "+arr[1]);
                        }
                        else{
                            txt.setText(arr[1]);
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class chatUser extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView img;
        private TextView txt, txt1;
        public chatUser(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.chatuser_img);
            txt=itemView.findViewById(R.id.chatuser_fullname);
            txt1=itemView.findViewById(R.id.chatuser_messend);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            String a=list.get(pro);
            Intent as=new Intent(context, Chat.class);
            as.putExtra("objectId", a);
            context.startActivity(as);
        }
    }
}
