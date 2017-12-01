package com.abba9ja.modo;

/**
 * Created by Abba9ja on 11/22/2017.
 */
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * We couldn't come up with a good name for this class. Then, we realized
 * that this lesson is about RecyclerView.
 *
 * RecyclerView... Recycling... Saving the planet? Being green? Anyone?
 * #crickets
 *
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 *
 * If you don't like our puns, we named this Adapter GreenAdapter because its
 * contents are green.
 */
public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ArticleViewHolder> {


    private static final String TAG = commentAdapter.class.getSimpleName();

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */

    private List<Comment> commentList;
    private Context context;

    public Context getContext() {
        return context;
    };
    final private ListItemClickListener mOnClickListener;



    public commentAdapter(List<Comment> commentList, ListItemClickListener mOnClickListener) {
        this.commentList = commentList;
        this.mOnClickListener = mOnClickListener;
    }
    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }



    /**
     * Cache of the children views for a list item.
     */
    class ArticleViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        public ImageView image;
        TextView tvname, tvPost, tvPostTime;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.post_profile_pic);
            tvname = (TextView) itemView.findViewById(R.id.post_user_name);
            tvPost = (TextView) itemView.findViewById(R.id.dpost_desc);
            tvPostTime = (TextView) itemView.findViewById(R.id.post_time);

            itemView.setOnClickListener(this);
        }


        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }


    /**
     *
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
        int layoutIdForListItem = R.layout.comment_rowlist;

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
        Comment comment = commentList.get(position);

          Uri uri =  Uri.parse(comment.getUser_pic());
            Glide.with(getContext()).load(uri)
                    .placeholder(R.color.colorAccent)
                    .override(50, 50)
                    .crossFade()
                    .centerCrop()
                    .into(holder.image);

        String name = comment.getUser_name();
        holder.tvname.setText(name);

        String post = comment.getUser_post_coment();
        holder.tvPost.setText(post);

        String ptime = comment.getPost_time();
        holder.tvPostTime.setText(ptime);


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
        return commentList.size();
    }
}