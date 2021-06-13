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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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
        private TextView txt;
        public chatUser(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.chatuser_img);
            txt=itemView.findViewById(R.id.chatuser_fullname);
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
