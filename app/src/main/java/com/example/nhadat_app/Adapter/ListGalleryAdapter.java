package com.example.nhadat_app.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.nhadat_app.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListGalleryAdapter extends RecyclerView.Adapter<ListGalleryAdapter.gallery> {
    private Context context;
    private List<String> list;
    private ArrayList<String> list1;

    public ListGalleryAdapter(Context context, List<String> list, ArrayList<String> list1) {
        this.context = context;
        this.list = list;
        this.list1 = list1;
    }

    @NotNull
    @Override
    public gallery onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.imagecard, parent, false);
        return new gallery(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull gallery holder, int position) {
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

    public class gallery extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private CheckBox ck;
        public gallery(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imagecard);
            ck=itemView.findViewById(R.id.checker);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(ck.isChecked()==true){
                ck.setChecked(false);
                list1.remove(list.get(getLayoutPosition()));
            }
            else if(ck.isChecked()==false){
                ck.setChecked(true);
                list1.add(list.get(getLayoutPosition()));
            }
        }
    }
}
