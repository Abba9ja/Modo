package com.abba9ja.modo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

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

public class MyPost extends AppCompatActivity implements MyPostAdapter.ListItemClickListener{

    private DatabaseReference mDatabase;
    RecyclerView mMyPosTRec;
    MyPostAdapter myPostAdapter;
    private ProgressDialog mProgress;

    List<UserPost> userPostList = new ArrayList<>();

    UserPost userPost = new UserPost();

    final String OMCOLUMN_TITLE = "title";
    final String OMCOLUMN_DESC = "desc";
    final String OMCOLUMN_IMAGE = "p_image0";
    final String OMCOLUMN_USERNAME = "user_name";
    final String OMCOLUMN_USER_EMAIL = "user_email";
    final String OMCOLUMN_USERID= "user_id";
    final String OMCOLUMN_USERPIC = "user_pic";
    final String OMCOLUMN_PTIME = "post_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LangSelector.createIntent(this));
            finish();
            return;
        }
        setContentView(R.layout.activity_my_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.setTitle(getString(R.string.my_post_title));
        mProgress = new ProgressDialog(this);

        mMyPosTRec = (RecyclerView) findViewById(R.id.mypostRecycler);

        // The linear layout manager will position list items in a vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMyPosTRec.setLayoutManager(layoutManager);


        // The adapter is responsible for displaying each item in the list
        myPostAdapter = new MyPostAdapter(userPostList,this);
        mMyPosTRec.setAdapter(myPostAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            }
        }).attachToRecyclerView(mMyPosTRec);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        populateMyPost(currentUser.getUid());
    }

    private void populateMyPost(String uid) {

        Query userpostqr = mDatabase.child("Blog").orderByChild("user_id").equalTo(uid);

        userpostqr.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){

                    if (data != null){
                        String blog_post_id, post_title, post_desc, post_img, post_name, post_email, psot_userid, post_userpic, post_time;
                        blog_post_id = post_title = post_desc = post_img = post_name =  post_email = psot_userid = post_userpic = post_time =  " ";
                        if (data.child(OMCOLUMN_TITLE).getValue() != null){
                            post_title = String.valueOf(data.child(OMCOLUMN_TITLE).getValue());
                        }
                        if (data.child(OMCOLUMN_DESC).getValue() != null){
                            post_desc = String.valueOf(data.child(OMCOLUMN_DESC).getValue());
                        }
                        if (data.child(OMCOLUMN_IMAGE).getValue() != null){
                            post_img = String.valueOf(data.child(OMCOLUMN_IMAGE).getValue());
                        }
                        if (data.child(OMCOLUMN_USERNAME).getValue() != null){
                            post_name = String.valueOf(data.child(OMCOLUMN_USERNAME).getValue());
                        }
                        if (data.child(OMCOLUMN_USER_EMAIL) != null){
                            post_email = String.valueOf(data.child(OMCOLUMN_USER_EMAIL).getValue());
                        }

                        if (data.child(OMCOLUMN_USERID).getValue() != null){
                            psot_userid = String.valueOf(data.child(OMCOLUMN_USERID).getValue());
                        }

                        if (data.child(OMCOLUMN_USERPIC).getValue() != null){
                            post_userpic = String.valueOf(data.child(OMCOLUMN_USERPIC).getValue());
                        }
                        if (data.child(OMCOLUMN_PTIME).getValue() != null){
                            post_time = String.valueOf(data.child(OMCOLUMN_PTIME).getValue());
                        }

                        UserPost userPost = new UserPost(post_title, post_title, post_desc, post_img
                        ,post_name, post_email, psot_userid, post_userpic, post_time);
                        userPostList.add(userPost);
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onListItemClick(String[] bpost) {
        Context context = this;
        Class destinationClass = ScrollingActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, bpost);
        startActivity(intentToStartDetailActivity);
    }
}
