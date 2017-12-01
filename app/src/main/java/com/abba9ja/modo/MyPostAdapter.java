package com.abba9ja.modo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

/**
 * Created by Abba9ja on 11/25/2017.
 */
public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ArticleViewHolder> {


    private static final String TAG = MyPostAdapter.class.getSimpleName();

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */

    private List<UserPost> userPostList;
    private Context context;

    public Context getContext() {
        return context;
    }

    ;
    final private ListItemClickListener mOnClickListener;



    public MyPostAdapter(List<UserPost> userPostList, ListItemClickListener mOnClickListener) {
        this.userPostList = userPostList;
        this.mOnClickListener = mOnClickListener;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(String[] bpost);
    }


    /**
     * Cache of the children views for a list item.
     */
    class ArticleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView pimage, uimage;
        public TextView tvname, tvtittle, tvTimeDate;
        public ExpandableTextView tvContent;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            pimage = (ImageView) itemView.findViewById(R.id.post_image);
            tvtittle = (TextView) itemView.findViewById(R.id.post_title);
            tvTimeDate = (TextView) itemView.findViewById(R.id.post_time);
            tvContent = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);

            itemView.setOnClickListener(this);
        }


        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            UserPost userPost = userPostList.get(clickedPosition);
            String colum_id = userPost.getTitle();
            String title =  userPost.getTitle();
            String desc =  userPost.getDesc();
            String img = userPost.getPost_image();
            String name = userPost.getUser_name();
            String email = userPost.getUser_email();
            String uid = userPost.getUser_id();
            String upic = userPost.getUser_pic();
            String ptime = userPost.getPost_time();
            String[] dpost = new String[]{colum_id, title, desc, img, name, email, uid, upic, ptime };
            mOnClickListener.onListItemClick(dpost);
        }
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ArticleViewHolder that holds the View for each list item
     */
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.mypostrowlist;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);

        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        UserPost userPost = userPostList.get(position);

        Uri uri = Uri.parse(userPost.getPost_image());
        Glide.with(getContext()).load(uri)
                .placeholder(R.color.colorAccent)
                .override(300, 350)
                .crossFade()
                .centerCrop()
                .into(holder.pimage);


        String title = userPost.getTitle();
        holder.tvtittle.setText(title);

        holder.tvContent.setText(userPost.getDesc());
        holder.tvTimeDate.setText(userPost.getPost_time());


        //Set values if given from database - for now they are placeholders in the list_item xml,
        // so this won't be implemented
    }


    /**
     * This method simply returns 10 items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return userPostList.size();
    }

}

