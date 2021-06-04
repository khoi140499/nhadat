package com.example.nhadat_app.ImageSlider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nhadat_app.Model.ImageSlide;
import com.example.nhadat_app.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageAdapter extends SliderViewAdapter<ImageAdapter.SliderAdapterViewHolder> {

    private List<ImageSlide> list;

    public ImageAdapter(Context context, List<ImageSlide> list) {
        this.list = list;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View infla= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide, null);
        return new SliderAdapterViewHolder(infla);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
        final ImageSlide sliderItem = list.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}
