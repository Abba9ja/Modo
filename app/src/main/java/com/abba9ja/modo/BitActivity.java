package com.abba9ja.modo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitActivity extends AppCompatActivity implements BidAdapter.ListItemClickListener{
    private static final String TAG = BitActivity.class.getSimpleName();
    String[] position;
    ImageView bit_post_image, post_profile_pic;
    TextView post_user_name, post_time, dpost_price ,dpost_nego, dpost_title;
    String username, posttime, postprice, postnego, posttitle, postimage, postprofile_pic, post_user_id;

    private DatabaseReference mDatabase;
    RecyclerView recyclerViewc;
    BidAdapter mAdapter;
    ProgressBar prgBar;

    List<BidModel> bidModelList = new ArrayList<>();

    BidModel bidModel = new BidModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LangSelector.createIntent(this));
            finish();
            return;
        }
        setContentView(R.layout.activity_bit);

        bit_post_image = (ImageView) findViewById(R.id.bit_post_image);
        post_profile_pic = (ImageView) findViewById(R.id.post_profile_pic);

        post_user_name = (TextView) findViewById(R.id.post_user_name);
        post_time = (TextView) findViewById(R.id.post_time);
        dpost_price = (TextView) findViewById(R.id.dpost_price);
        dpost_nego = (TextView) findViewById(R.id.dpost_nego);
        dpost_title = (TextView) findViewById(R.id.dpost_title);
        prgBar = (ProgressBar) findViewById(R.id.prgbar);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
               position = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

                username = position[2];
                posttime = position[4];
                postprice = position[5];
                postnego = position[6];
                posttitle = position[1];
                postimage = position[0];
                postprofile_pic = position[3];
                post_user_id = position[7];


                List<String> list = new ArrayList<String>();
                Pattern regex = Pattern.compile("\\[([^\\]]*)\\]");
                Matcher regexMatcher = regex.matcher(postimage);
                String post_image1 ="";

                while (regexMatcher.find()) {
                    list.add(regexMatcher.group(1));
                }
                if (list.size() > 0){
                    post_image1 = (list.get(0));
                }

                Uri postimageUri = Uri.parse(post_image1);

                Glide.with(this).load(postimageUri)
                        .placeholder(R.color.colorAccent)
                        .crossFade()
                        .centerCrop()
                        .into(bit_post_image);


                Uri postppuri = Uri.parse(postprofile_pic);

                Glide.with(this).load(postppuri)
                        .placeholder(R.color.colorAccent)
                        .override(40, 40)
                        .crossFade()
                        .centerCrop()
                        .into(post_profile_pic);

                post_user_name.setText(username);
                post_time.setText(posttime);
                dpost_price.setText(postprice);
                dpost_nego.setText(postnego);
                dpost_title.setText(posttitle);
            }
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Bid"+posttitle+post_user_id);
        this.setTitle(posttitle);

        recyclerViewc = (RecyclerView) findViewById(R.id.bit_recycler);

        // The linear layout manager will position list items in a vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewc.setLayoutManager(layoutManager);

        // The adapter is responsible for displaying each item in the list
        mAdapter = new BidAdapter(bidModelList,this);
        recyclerViewc.setAdapter(mAdapter);
        populateBid();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(BitActivity.this, "goinback to main activity", Toast.LENGTH_SHORT).show();
    }

    public void populateBid(){
        prgBar.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.getChildrenCount() < 1){
                        prgBar.setVisibility(View.GONE);
                    }
                    String user_id, user_name, user_pic, bid_amount, bit_range, bid_time, email_address;
                    user_id =  user_name = user_pic = bid_amount = bit_range = bid_time = email_address = "";
                    if (data.child("user_name").getValue() != null){
                        user_id = String.valueOf(data.child("user_id").getValue());
                    }

                    if (data.child("user_name").getValue() != null){
                        user_name = String.valueOf(data.child("user_name").getValue());
                    }

                    if (data.child("user_pic").getValue() != null){
                        user_pic = String.valueOf(data.child("user_pic").getValue());
                    }

                    if (data.child("bid_amount").getValue() != null){
                        bid_amount = String.valueOf(data.child("bid_amount").getValue());
                    }

                    if (data.child("bit_range").getValue() != null){
                        bit_range = String.valueOf(data.child("bit_range").getValue());
                    }

                    if (data.child("bid_time").getValue() != null){
                        bid_time = String.valueOf(data.child("bid_time").getValue());
                    }
                    if (data.child("email_address").getValue() != null){
                        email_address = String.valueOf(data.child("email_address").getValue());
                    }


                    BidModel bidModel = new BidModel(user_id,user_name,user_pic,bid_amount,bit_range,bid_time, email_address);
                    bidModelList.add(bidModel);
                    mAdapter.notifyDataSetChanged();
                    prgBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                prgBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onListItemClick(String[] bpost) {
        String user_id = bpost[0];
        String user_name = bpost[1];
        String user_pic = bpost[2];
        final String bid_amount = bpost[3];
        String bit_range = bpost[4];
        final String bid_time = bpost[5];
        final String email_address = bpost[6];
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String cuserid = currentUser.getUid().toString().trim();
            if (cuserid.equalsIgnoreCase(user_id)) {
                LayoutInflater li = LayoutInflater.from(BitActivity.this);
                View promptsView = li.inflate(R.layout.deleting_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BitActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.tvdeletingval);
                userInput.setText(getString(R.string.your)+bid_amount+getString(R.string.bid)+ getString(R.string.on)+posttitle);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton(R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        Query dlelteQuery = ref.child("Bid"+posttitle+post_user_id).orderByChild("bid_time").equalTo(bid_time);

                                        dlelteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e(TAG, "onCancelled", databaseError.toException());
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton(R.string.cancel_d,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }else if(cuserid.equalsIgnoreCase(post_user_id)){
                LayoutInflater li = LayoutInflater.from(BitActivity.this);
                View promptsView = li.inflate(R.layout.sold_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BitActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.tvsoldval);

                userInput.setText(getString(R.string.declarinf_sold_to)+user_name+getString(R.string.on)+ bid_amount+getString(R.string.of) +posttitle );
                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton(R.string.mail_winner,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String emailAddress = email_address;
                                        String message = "";
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailAddress });
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "You are declared one of the Winners on biding on "+posttitle+ " at "+ bid_amount);
                                        intent.putExtra(Intent.EXTRA_TEXT, message);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(Intent.createChooser(intent, getString(R.string.send_mail_to_modos)));
                                        }else{
                                            Toast.makeText(BitActivity.this, R.string.no_email_app_fount, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                })
                        .setNegativeButton(R.string.cancel_d,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        }


}
