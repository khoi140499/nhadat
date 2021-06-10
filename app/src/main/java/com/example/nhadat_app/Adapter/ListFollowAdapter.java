package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhadat_app.Model.Follow;
import com.example.nhadat_app.R;

import org.jetbrains.annotations.NotNull;

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
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.followcard_addfl){
                int pro=getLayoutPosition();
                Follow a=list.get(pro);


            }
        }
    }
}
