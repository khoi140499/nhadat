package com.example.nhadat_app.Adapter;

import android.content.Context;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.messageUser> {
    private Context context;
    private List<ChatUser> list;

    public ListMessageAdapter(Context context, List<ChatUser> list) {
        this.context = context;
        this.list = list;
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
        if(ParseUser.getCurrentUser().getObjectId().equalsIgnoreCase(a.getUser_send())==true){
            holder.send.setText(a.getMessage());
            holder.rec.setBackgroundColor(Color.WHITE);
        }
        else{
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.whereEqualTo("objectId", a.getUser_receiver());
            query.findInBackground(((objects, e) -> {
                if(e==null){
                    for(ParseUser as:objects){
                        Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(holder.img);
                        holder.rec.setText(a.getMessage());
                        holder.send.setVisibility(View.GONE);
                    }
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class messageUser extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView send, rec;
        public messageUser(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgReceiver);
            send=itemView.findViewById(R.id.txtSend);
            rec=itemView.findViewById(R.id.txtReceiver);
        }
    }
}
