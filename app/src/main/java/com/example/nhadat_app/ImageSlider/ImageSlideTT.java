package com.example.nhadat_app.ImageSlider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nhadat_app.ImageZoom;
import com.example.nhadat_app.Model.ImageSlide;
import com.example.nhadat_app.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSlideTT extends SliderViewAdapter<ImageSlideTT.SliderAdapterViewHolder> {
    private List<ImageSlide> list;
    private Context context;
    public ImageSlideTT(Context context, List<ImageSlide> list) {
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
        ImageSlide sliderItem = list.get(position);
        Picasso.get().load(sliderItem.getUrl1()).into(viewHolder.imageViewBackground);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent as=new Intent(context, ImageZoom.class);
                as.putExtra("uri", sliderItem.getUrl1()+"");
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
