package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.ChatUser;
import com.example.nhadat_app.R;
import com.example.nhadat_app.ViewProfile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.messageUser> {
    private Context context;
    private List<ChatUser> list;
    private String objectId;
    public ListMessageAdapter(Context context, List<ChatUser> list, String objectId) {
        Collections.sort(list, new Comparator<ChatUser>() {
            @Override
            public int compare(ChatUser o1, ChatUser o2) {
                return o2.getCreateAt().compareTo(o1.getCreateAt());
            }
        });
        this.context = context;
        this.list = list;
        this.objectId=objectId;
    }

    public void addData(List<ChatUser> list){
        this.list=list;
    }

    @NotNull
    @Override
    public messageUser onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.cardchat, parent, false);
        return new messageUser(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull messageUser holder, int position) {
        ChatUser a=list.get(position);
        if(ParseUser.getCurrentUser().getObjectId().equalsIgnoreCase(a.getUser_send())==true
        && a.getUser_receiver().equalsIgnoreCase(objectId)==true){
            holder.send.setText(a.getMessage());
            holder.rec.setBackgroundColor(Color.TRANSPARENT);
            System.out.println(a.getUser_send()+" : "+a.getMessage());
        }
        else if(a.getUser_send().equalsIgnoreCase(objectId)==true &&
                a.getUser_receiver().equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())==true){
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.whereEqualTo("objectId", a.getUser_send());
            query.findInBackground(((objects, e) -> {
                if(e==null){
                    for(ParseUser as:objects){
                        Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(holder.img);
                    }
                }
            }));
            holder.rec.setText(a.getMessage());
            holder.send.setBackgroundColor(Color.TRANSPARENT);
            System.out.println(a.getUser_send()+" : "+a.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class messageUser extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView img;
        private TextView send, rec;
        public messageUser(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgReceiver);
            send=itemView.findViewById(R.id.txtSend);
            rec=itemView.findViewById(R.id.txtReceiver);

            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.imgReceiver){
                ParseQuery<ParseUser> query=ParseUser.getQuery();
                query.whereEqualTo("objectId", objectId);
                query.findInBackground(((objects, e) -> {
                    if(e==null){
                        for(ParseUser as:objects){
                            Intent intent=new Intent(context, ViewProfile.class);
                            intent.putExtra("name", as.getUsername());
                            context.startActivity(intent);
                        }
                    }
                }));
            }
        }
    }
}
