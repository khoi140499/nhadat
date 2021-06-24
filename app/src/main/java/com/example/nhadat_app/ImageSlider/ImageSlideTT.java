package com.example.nhadat_app.ImageSlider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nhadat_app.ImageZoom;
import com.example.nhadat_app.Model.ImageSlide;
import com.example.nhadat_app.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSlideTT extends SliderViewAdapter<ImageSlideTT.SliderAdapterViewHolder> {
    private List<String> list;
    private Context context;
    public ImageSlideTT(Context context, List<String> list) {
        this.list = list;
        this.context=context;
    }
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View infla= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide, null);
        return new ImageSlideTT.SliderAdapterViewHolder(infla);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
        String sliderItem = list.get(position);
        Glide.with(viewHolder.itemView)
                .load(Uri.parse(sliderItem))
                .fitCenter()
                .into(viewHolder.imageViewBackground);
//        Picasso.get().load(Uri.parse(sliderItem)).into(viewHolder.imageViewBackground);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent as=new Intent(context, ImageZoom.class);
                as.putExtra("uri", sliderItem);
                context.startActivity(as);
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder{
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }

    }
}
