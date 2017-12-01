package com.abba9ja.modo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrollingActivity extends AppCompatActivity{

    private static final String TAG = ScrollingActivity.class.getSimpleName();;
    Cursor dCusor;
    String[] position;
    View content_comment;
    LinearLayout llbuttons;
    TextView post_user_name;
    Uri uuri;
    String title;
    String upid, idid, iemail, price, nego, ptime;
    ImageView dpimage;
    TextView tvTitle, tvDesc, tvPtime, tvPrice, tvNego;
    String name;
    String post_image1;
    String img;

    List<Comment> commentHelperList = new ArrayList<>();

    Comment comment = new Comment();

    private List<PhotoHelper> photoHelperList = new ArrayList<>();
    PhotoHelper photoHelper = new PhotoHelper();

    RecyclerView mRecyclerView;
    DetailImagesAdapter mDAdapter;

    private ProgressDialog mProgress;

    private DatabaseReference mDatabase, mmDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LangSelector.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View content_scrolling = (View) findViewById(R.id.content_scrolling);
        content_comment= (View) findViewById(R.id.content_comment);
        llbuttons= (LinearLayout) findViewById(R.id.llbuttons);
        post_user_name = (TextView) content_scrolling.findViewById(R.id.post_user_name);
        tvTitle = (TextView) content_scrolling.findViewById(R.id.dpost_title);
        tvDesc = (TextView) content_scrolling.findViewById(R.id.dpost_desc);
        tvPtime = (TextView) content_scrolling.findViewById(R.id.post_time);
        tvPrice = (TextView) content_scrolling.findViewById(R.id.dpost_price);
        tvNego = (TextView) content_scrolling.findViewById(R.id.dpost_nego);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                position = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

                img = position[3];
                title = position[1];
                name = position[4];
                upid = position[7];
                String desc = position[2];
                idid = position[6];
                ptime = position[8];
                iemail = position[5];
                price = position[9];
                nego = position[10];

                uuri =  Uri.parse(upid);

                ImageView duimage = (ImageView) content_scrolling.findViewById(R.id.post_profile_pic);
                this.setTitle(title);

                Glide.with(this).load(uuri)
                        .placeholder(R.color.colorAccent)
                        .override(80, 80)
                        .crossFade()
                        .centerCrop()
                        .into(duimage);

                post_user_name.setText(name);
                tvTitle.setText(title);
                tvDesc.setText(desc);
                tvPtime.setText(ptime);
                tvPrice.setText(price);
                tvNego.setText(nego);

            }


            mRecyclerView = (RecyclerView) findViewById(R.id.rcpost_images);
            mDAdapter = new DetailImagesAdapter(photoHelperList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mDAdapter);


            final String post_id = title + idid;
            mProgress = new ProgressDialog(this);

            mmDb = FirebaseDatabase.getInstance().getReference("Bid"+title+idid);
            mDatabase = FirebaseDatabase.getInstance().getReference("ModoIt"+title+idid);
            // Reference the recycler view with a call to findViewById


            Button mod_it = (Button) findViewById(R.id.btn_modo_it);
            Button start_a_chat = (Button) findViewById(R.id.btn_start_a_chat);
            Button btn_bid = (Button) findViewById(R.id.btn_bid);

            btn_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ScrollingActivity.this);
                    View promptsView = li.inflate(R.layout.bit_input_layout, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ScrollingActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.etbidamonut);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Bid",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                           String userAmount = userInput.getText().toString();
                                           if (!TextUtils.isEmpty(userAmount)){
                                                startPostingBit(userAmount, img, title, name, upid, ptime, price, nego, idid);
                                           }else{
                                               Toast.makeText(ScrollingActivity.this, "NOT a valid amount", Toast.LENGTH_SHORT);
                                           }

                                        }
                                    })
                            .setNegativeButton("View Bits",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            startViewingBit("viewBits", img, title, name, upid, ptime, price, nego, idid);
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });


            ImageButton btn_cancel_commentl = (ImageButton) content_comment.findViewById(R.id.ibCancel);
            ImageButton ibsendcoment = (ImageButton) content_comment.findViewById(R.id.ibsendcoment);

            start_a_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailAddress = iemail;
                    String message = "";
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailAddress });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Your Modo Post "+tvTitle.getText().toString()+ " on "+ tvPtime.getText().toString());
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(intent, getString(R.string.send_mail_to_modos)));
                    }else{
                        Toast.makeText(ScrollingActivity.this, R.string.no_email_app_fount, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mod_it.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(ScrollingActivity.this);
                    View promptsView = li.inflate(R.layout.modo_layouot, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ScrollingActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.etyourmodoval);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Modo it",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            String userAmount = userInput.getText().toString();
                                            if (!TextUtils.isEmpty(userAmount)){
                                                startPostingModo(userAmount, img, title, name, upid, ptime, price, nego, idid);
                                            }else{
                                                Toast.makeText(ScrollingActivity.this, "NOT a valid modo", Toast.LENGTH_SHORT);
                                            }

                                        }
                                    })
                            .setNegativeButton("View Modos",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            startViewingModo("viewBits", img, title, name, upid, ptime, price, nego, idid);
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            });

            btn_cancel_commentl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        String cuserid = currentUser.getUid().toString().trim();
        if (cuserid.equalsIgnoreCase(idid)) {
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.INVISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(ScrollingActivity.this);
                View promptsView = li.inflate(R.layout.deleting_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ScrollingActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.tvdeletingval);
                userInput.setText(getString(R.string.yourpost)+ getString(R.string.on)+ title);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton(R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        Query dlelteQuery = ref.child("Blog").orderByChild("post_time").equalTo(ptime);
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

            }
        });
    }

    private void startViewingModo(String viewBits, String img, String title, String name, String upid, String ptime, String price, String nego, String idid) {
        String bitCommentArray[] = new String[]{img, title, name, upid, ptime, price, nego, idid};
        Context context = this;
        Class destinationClass = ModoItActivity.class;
        Intent intentToStartBitActivity = new Intent(context, destinationClass);
        intentToStartBitActivity.putExtra(Intent.EXTRA_TEXT, bitCommentArray);
        startActivity(intentToStartBitActivity);
    }

    private void startPostingModo(String userAmount, String img, String title, String name, String upid, String ptime, String price, String nego, String idid) {

        String currentTime = getCTime();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getUid().toString();
        String user_name = user.getDisplayName().toString();
        String email_address = user.getEmail();
        String user_pic = user.getPhotoUrl().toString();
        DatabaseReference newPost = mDatabase.push();
        newPost.child("user_id").setValue(user_id);
        newPost.child("user_name").setValue(user_name);
        newPost.child("user_pic").setValue(user_pic);
        newPost.child("mod_it").setValue(userAmount);
        newPost.child("bit_range").setValue(price);
        newPost.child("modo_time").setValue(getCTime());
        newPost.child("email_address").setValue(email_address);

        String modoCommentArray[] = new String[]{img, title, name, upid, ptime, price, nego, idid};
        Context context = this;
        Class destinationClass = ModoItActivity.class;
        Intent intentToStartBitActivity = new Intent(context, destinationClass);
        intentToStartBitActivity.putExtra(Intent.EXTRA_TEXT, modoCommentArray);
        startActivity(intentToStartBitActivity);
    }

    private void startViewingBit(String viewBit, String post_image1, String title, String name, String upid, String ptime,String price, String nego, String post_user_id) {
        String bitCommentArray[] = new String[]{post_image1, title, name, upid, ptime, price, nego, post_user_id};
        Context context = this;
        Class destinationClass = BitActivity.class;
        Intent intentToStartBitActivity = new Intent(context, destinationClass);
        intentToStartBitActivity.putExtra(Intent.EXTRA_TEXT, bitCommentArray);
        startActivity(intentToStartBitActivity);
    }

    private void startPostingBit(String userAmount, String post_image1, String title, String name, String upid, String ptime, String price, String nego, String post_user_id) {
        String currentTime = getCTime();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getUid().toString();
        String user_name = user.getDisplayName().toString();
        String email_address = user.getEmail();
        String user_pic = user.getPhotoUrl().toString();
        DatabaseReference newPost = mmDb.push();
        newPost.child("user_id").setValue(user_id);
        newPost.child("user_name").setValue(user_name);
        newPost.child("user_pic").setValue(user_pic);
        newPost.child("bid_amount").setValue("₦: "+userAmount);
        newPost.child("bit_range").setValue(price);
        newPost.child("bid_time").setValue(getCTime());
        newPost.child("email_address").setValue(email_address);

        String bitCommentArray[] = new String[]{post_image1, title, name, upid, ptime, price, nego, post_user_id};
        Context context = this;
        Class destinationClass = BitActivity.class;
        Intent intentToStartBitActivity = new Intent(context, destinationClass);
        intentToStartBitActivity.putExtra(Intent.EXTRA_TEXT, bitCommentArray);
        startActivity(intentToStartBitActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> list = new ArrayList<String>();
        Pattern regex = Pattern.compile("\\[([^\\]]*)\\]");
        Matcher regexMatcher = regex.matcher(img);
        while (regexMatcher.find()) {
            list.add(regexMatcher.group(1));
        }

        for (int i = 0; i < list.size(); i++){
            PhotoHelper photoHelper = new PhotoHelper(String.valueOf(list.get(i)));
            photoHelperList.add(photoHelper);
        }
        mDAdapter.notifyDataSetChanged();
    }

    public static String getCTime() {
        String currentDateAndTime;
        currentDateAndTime =  DateFormat.getDateTimeInstance().format(new Date());
        return currentDateAndTime;
    }




    private void setShareIntent() {
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(dpimage);
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        // Construct a ShareIntent with link to image
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hea WhatsUp, Check Out this Post: "+ tvTitle.getText().toString() + "'\n' @Modo app ->Post on: "
                +tvPtime.getText().toString() +"'\n' "+ tvDesc.getText().toString() );
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        // Launch share menu
        startActivity(Intent.createChooser(shareIntent, getString(R.string.shar_modo_post)));
    }
    // Returns the URI path to the Bitmap displayed in  backdrop
    private Uri getLocalBitmapUri(ImageView poster) {
        Drawable drawable ;
        Bitmap bmp;
        drawable = poster.getDrawable();
        bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) poster.getDrawable()).getBitmap();
        } else {
            return null;
        }

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Shared_Movie" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        MenuItem menuDelete = (MenuItem) findViewById(R.id.action_delete);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_share) {
            setShareIntent();
        }
        if (id == R.id.action_lovr) {
            Snackbar.make(item.getActionView(), R.string.liked, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }


        return super.onOptionsItemSelected(item);
    }
}
