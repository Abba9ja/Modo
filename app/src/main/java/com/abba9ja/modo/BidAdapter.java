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

import java.util.List;

/**
 * Created by Abba9ja on 11/27/2017.
 */

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ArticleViewHolder> {


    private static final String TAG = BidAdapter.class.getSimpleName();

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */

    private List<BidModel> bidModelList;
    private Context context;

    public Context getContext() {
        return context;
    }

    ;
    final private ListItemClickListener mOnClickListener;


    public BidAdapter(List<BidModel> bidModelList, ListItemClickListener mOnClickListener) {
        this.bidModelList = bidModelList;
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

        public ImageView uimage;
        public TextView tvbidfor, tvbidrange, tvname, tvtime;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            uimage = (ImageView) itemView.findViewById(R.id.post_profile_pic);
            tvbidfor = (TextView) itemView.findViewById(R.id.bitfor);
            tvbidrange = (TextView) itemView.findViewById(R.id.bitrange);
            tvtime = (TextView) itemView.findViewById(R.id.rvbitpost_time);
            tvname = (TextView) itemView.findViewById(R.id.rvbitpost_user_name);

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
            BidModel bidModel = bidModelList.get(clickedPosition);
            String user_id = bidModel.getUser_id();
            String user_name = bidModel.getUser_name();
            String user_pic = bidModel.getUser_pic();
            String bid_amount = bidModel.getBid_amount();
            String bit_range = bidModel.getBit_range();
            String bid_time = bidModel.getBid_time();
            String email_address = bidModel.getEmail_address();
            String[] dpost = new String[]{ user_id,user_name,user_pic,bid_amount,bit_range,bid_time, email_address};
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
        BidModel bidModel = bidModelList.get(position);

        Uri uri =  Uri.parse(bidModel.getUser_pic());
        Glide.with(getContext()).load(uri)
                .placeholder(R.color.colorAccent)
                .override(50, 50)
                .crossFade()
                .centerCrop()
                .into(holder.uimage);

        String name = bidModel.getUser_name();
        holder.tvname.setText(name);

        String bidfor = bidModel.getBid_amount();
        holder.tvbidfor.setText(bidfor);

        String bidRang = bidModel.getBit_range();
        holder.tvbidrange.setText(bidRang);

        String ptime = bidModel.getBid_time();
        holder.tvtime.setText(ptime);

        //Set values if given from database - for now they are placeholders in the list_item xml,
        // so this won't be implemented*/
    }


    /**
     * This method simply returns 10 items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return bidModelList.size();
    }

}

