package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.nhadat_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListIMAdapter extends RecyclerView.Adapter<ListIMAdapter.image> {
    private Context context;
    private ArrayList<String> list;

    public ListIMAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public image onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.imageshow, parent, false);
        return new image(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull image holder, int position) {
        String s=list.get(position);
        Glide.with(context)
                .load(list.get(position))
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if(list.size()>0){
            return list.size();
        }
        return 0;
    }

    public class image extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private ImageButton btn;
        public image(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.show_img);
            btn=itemView.findViewById(R.id.img_rm);
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            list.remove(list.get(getLayoutPosition()));
            notifyDataSetChanged();
        }
    }
}
