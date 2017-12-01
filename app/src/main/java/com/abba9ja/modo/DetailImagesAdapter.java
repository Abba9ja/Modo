package com.abba9ja.modo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Abba9ja on 11/27/2017.
 */

public class DetailImagesAdapter extends RecyclerView.Adapter<DetailImagesAdapter.ArticleViewHolder> {


    private List<PhotoHelper> pictureList;
    private Context context;

    public Context getContext() {
        return context;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ArticleViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.imageView2);

        }
    }

    public DetailImagesAdapter(List<PhotoHelper> pictureList) {
        this.pictureList = pictureList;
    }

    @Override
    public DetailImagesAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_images_rowlist, parent, false);

        return new DetailImagesAdapter.ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailImagesAdapter.ArticleViewHolder holder, int position) {
        PhotoHelper photoHelper = pictureList.get(position);

        Uri uri =  Uri.parse(photoHelper.getImageUri());

        Glide.with(getContext()).load(uri)
                .placeholder(R.color.colorAccent)
                .crossFade()
                .centerCrop()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
