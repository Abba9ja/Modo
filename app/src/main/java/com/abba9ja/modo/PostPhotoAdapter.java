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
 * Created by Abba9ja on 11/12/2017.
 */

public class PostPhotoAdapter extends RecyclerView.Adapter<PostPhotoAdapter.ArticleViewHolder> {

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

    public PostPhotoAdapter(List<PhotoHelper> pictureList) {
        this.pictureList = pictureList;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imgcardlist, parent, false);

        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        PhotoHelper photoHelper = pictureList.get(position);

        Uri uri =  Uri.parse(photoHelper.getImageUri());

        Glide.with(getContext()).load(uri)
                .placeholder(R.color.colorAccent)
                .override(300, 300)
                .crossFade()
                .centerCrop()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}