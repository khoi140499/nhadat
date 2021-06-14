package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.Follow;
import com.example.nhadat_app.Model.TinDang;
import com.example.nhadat_app.R;
import com.example.nhadat_app.ViewProfile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListFollowAdapter extends RecyclerView.Adapter<ListFollowAdapter.listFollow> {
    private Context context;
    private List<Follow> list;

    public ListFollowAdapter(Context context, List<Follow> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public listFollow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.followcard, parent, false);
        return new listFollow(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull listFollow holder, int position) {
        Follow a=list.get(position);
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereEqualTo("username", a.getUser_id());
        query.findInBackground((objects, e) -> {
            if(e==null){
                for(ParseUser as:objects){
                    Picasso.get().load(Uri.parse(as.getString("imgurl"))).into(holder.img);
                    holder.txt.setText(as.getString("fullname"));
                }
            }
        });

        if(ParseUser.getCurrentUser()!=null){
            ParseQuery<ParseObject> query1=ParseQuery.getQuery("follow");
            query1.whereEqualTo("user_id",ParseUser.getCurrentUser().getUsername());
            query1.whereEqualTo("user_following", a.getUser_following());
            query1.findInBackground((objects, e) -> {
                if(e==null){
                    if(objects.size()>0){
                        holder.btn.setTag("unfr");
                        holder.btn.setImageResource(R.drawable.ic_baseline_person_remove_24);
                    }
                    else{
                        holder.btn.setTag("fr");
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

    public class listFollow extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView img;
        private TextView txt;
        private ImageButton btn;
        public listFollow(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.followcard_img);
            txt=itemView.findViewById(R.id.followcard_fullname);
            btn=itemView.findViewById(R.id.followcard_addfl);
            btn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pro=getLayoutPosition();
            Follow a=list.get(pro);
            if(v.getId()==R.id.followcard_addfl){
                if(ParseUser.getCurrentUser()==null){
                    Toast.makeText(context, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
                }
                else if(ParseUser.getCurrentUser().getUsername().equalsIgnoreCase(a.getUser_id())==true){
                    Toast.makeText(context, "Không thể thực hiện", Toast.LENGTH_LONG).show();
                }
                else{
                    try{
                        if(btn.getTag().toString().equalsIgnoreCase("fr")==true){
                            ParseObject object=new ParseObject("follow");
                            object.put("user_id", ParseUser.getCurrentUser().getUsername());
                            object.put("user_following", a.getUser_following());
                            object.saveInBackground(e -> {
                                if(e==null){
                                    btn.setTag("unfr");
                                    btn.setImageResource(R.drawable.ic_baseline_person_remove_24);
                                    JSONObject data=new JSONObject();
                                    try {
                                        data.put("alert", ParseUser.getCurrentUser()
                                                .getString("fullname")+" vừa theo dõi bạn");
                                        data.put("title", "Nhà đất");
                                    } catch (JSONException es) {
                                        throw new IllegalArgumentException("unexpected parsing error", es);
                                    }

                                    ParsePush push = new ParsePush();
                                    push.setChannel(a.getUser_following()+"follow");
                                    push.setData(data);
                                    push.sendInBackground();
                                }
                            });
                        }
                        else if(btn.getTag().toString().equalsIgnoreCase("unfr")==true){
                            ParseQuery<ParseObject> query=ParseQuery.getQuery("follow");
                            query.whereEqualTo("user_id", ParseUser.getCurrentUser().getUsername());
                            query.whereEqualTo("user_following", a.getUser_following());
                            query.findInBackground(((objects, e) -> {
                                if(e==null){
                                    for(ParseObject aas:objects){
                                        ParseQuery<ParseObject> query1=ParseQuery.getQuery("follow");
                                        query1.getInBackground(aas.getObjectId(), ((object, e1) -> {
                                            if(e1==null){
                                                object.deleteInBackground(e2 -> {
                                                    if(e2==null){
                                                        btn.setTag("fr");
                                                        btn.setImageResource(R.drawable.ic_baseline_person_add_24);
                                                    }
                                                });
                                            }
                                        }));
                                    }
                                }
                            }));
                        }
                    }catch (NullPointerException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            else{
                Intent as=new Intent(context, ViewProfile.class);
                as.putExtra("name", a.getUser_id());
                context.startActivity(as);
            }
        }
    }
}
