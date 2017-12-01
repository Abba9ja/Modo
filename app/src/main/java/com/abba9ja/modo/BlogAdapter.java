package com.abba9ja.modo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Abba9ja on 11/19/2017.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogAdapterViewHolder>{


    private Context mcontext;
    private Cursor mCursor;
    public void setContext(Context context) {
        this.mcontext = context;
    }

    private final BlogAdapterOnClickHandler mClickHandler;



    public interface BlogAdapterOnClickHandler {
        void onClick(String[] bpost);
    }

    public BlogAdapter(@NonNull Context context, BlogAdapterOnClickHandler clickHandler){
        mcontext = context;
        mClickHandler = clickHandler;
    }

    public Context getContext() {
        return mcontext;
    }



    @Override
    public BlogAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setContext(parent.getContext());
        int layoutIdForListItem = R.layout.blog_rowlist;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent,
                shouldAttachToParentImmediately);

        return new BlogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlogAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String colum_id = mCursor.getString(MainActivity.INDEX_COLUMN_ID);
        String title = mCursor.getString(MainActivity.INDEX_COLUMN_TITLE);
        String desc = mCursor.getString(MainActivity.INDEX_COLUMN_DESC);
        String img = mCursor.getString(MainActivity.INDEX_COLUMN_IMAGE);
        String name = mCursor.getString(MainActivity.INDEX_COLUMN_USERNAME);
        String email = mCursor.getString(MainActivity.INDEX_COLUMN_USER_EMAIL);
        String uid = mCursor.getString(MainActivity.INDEX_COLUMN_USERID);
        String upic = mCursor.getString(MainActivity.INDEX_COLUMN_USERPIC);
        String ptime = mCursor.getString(MainActivity.INDEX_COLUMN_PTIME);
        String item_price = mCursor.getString(MainActivity.INDEX_COLUMN_ITEM_PRICE);
        String nego_stats = mCursor.getString(MainActivity.INDEX_COLUMN_NEGO);


        List<String> list = new ArrayList<String>();
        Pattern regex = Pattern.compile("\\[([^\\]]*)\\]");
        Matcher regexMatcher = regex.matcher(img);
        String post_image1 =img;

        while (regexMatcher.find()) {
            list.add(regexMatcher.group(1));
        }
        if (list.size() > 0){
            post_image1 = (list.get(0));
        }

        Uri uri =  Uri.parse(post_image1);
        Uri uuri =  Uri.parse(upic);

        Glide.with(getContext()).load(uri)
                .placeholder(R.color.colorAccent)
                .override(300, 350)
                .crossFade()
                .centerCrop()
                .into(holder.pimage);

        Glide.with(getContext()).load(uuri)
                .placeholder(R.color.colorAccent)
                .override(50, 50)
                .crossFade()
                .centerCrop()
                .into(holder.uimage);


        holder.tvtittle.setText(title);
        holder.tvname.setText(name);
        holder.tvContent.setText(desc);
        holder.tvTimeDate.setText(ptime);
        holder.tvPrice.setText(item_price);
        holder.tvNego.setText(nego_stats);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }


    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class BlogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView pimage, uimage;
        public TextView tvname, tvtittle, tvTimeDate, tvPrice, tvNego;
        public ExpandableTextView tvContent;

        public BlogAdapterViewHolder(View itemView) {
            super(itemView);

            pimage = (ImageView) itemView.findViewById(R.id.post_image);
            uimage = (ImageView) itemView.findViewById(R.id.post_profile_pic);
            tvname = (TextView) itemView.findViewById(R.id.post_user_name);
            tvtittle = (TextView) itemView.findViewById(R.id.post_title);
            tvTimeDate = (TextView) itemView.findViewById(R.id.post_time);
            tvPrice = (TextView) itemView.findViewById(R.id.post_price);
            tvNego = (TextView) itemView.findViewById(R.id.post_nego_stats);
            tvContent = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String colum_id = mCursor.getString(MainActivity.INDEX_COLUMN_ID);
            String title = mCursor.getString(MainActivity.INDEX_COLUMN_TITLE);
            String desc = mCursor.getString(MainActivity.INDEX_COLUMN_DESC);
            String img = mCursor.getString(MainActivity.INDEX_COLUMN_IMAGE);
            String name = mCursor.getString(MainActivity.INDEX_COLUMN_USERNAME);
            String email = mCursor.getString(MainActivity.INDEX_COLUMN_USER_EMAIL);
            String uid = mCursor.getString(MainActivity.INDEX_COLUMN_USERID);
            String upic = mCursor.getString(MainActivity.INDEX_COLUMN_USERPIC);
            String ptime = mCursor.getString(MainActivity.INDEX_COLUMN_PTIME);
            String item_price = mCursor.getString(MainActivity.INDEX_COLUMN_ITEM_PRICE);
            String nego_stats = mCursor.getString(MainActivity.INDEX_COLUMN_NEGO);
            String[] dpost = new String[]{colum_id, title, desc, img, name, email, uid, upic, ptime, item_price, nego_stats };
            mClickHandler.onClick(dpost);
        }
    }
}
