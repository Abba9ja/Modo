package com.abba9ja.modo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.abba9ja.modo.fieldvalidation.RequiredFieldValidator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

public class PostActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;
    private TextView tvIndicator;
    private List<PhotoHelper> photoHelperList = new ArrayList<>();
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private EditText mPostTitle, mPostDesc, mItemPrice;
    private CheckBox chkNego;
    private  Uri mUri = null;
    private Button submitButton;
    private TextInputLayout iilPostTitle, iilPostDecs;
    private RequiredFieldValidator mTitleValidator, mDescValidator;
    private ProgressDialog mProgress;
    ArrayList<Image> mImages;



    PhotoHelper photoHelper = new PhotoHelper();

    RecyclerView mRecyclerView;
    PostPhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");


        mProgress = new ProgressDialog(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.imageRecycler);
        mPostTitle = (EditText) findViewById(R.id.etPostTitle);
        mPostDesc = (EditText) findViewById(R.id.etPostDesc);
        mItemPrice = (EditText) findViewById(R.id.item_price);
        submitButton = (Button) findViewById(R.id.submitButton);
        iilPostTitle = (TextInputLayout) findViewById(R.id.iilPostTitle);
        iilPostDecs = (TextInputLayout) findViewById(R.id.iilPostDecs);
        tvIndicator = (TextView) findViewById(R.id.tvIndicator);
        chkNego = (CheckBox)findViewById(R.id.chk_negoitable);

        mTitleValidator = new RequiredFieldValidator(iilPostTitle);
        mDescValidator = new RequiredFieldValidator(iilPostDecs);

        mPostTitle.setOnFocusChangeListener(this);
        mPostDesc.setOnFocusChangeListener(this);

        mAdapter = new PostPhotoAdapter(photoHelperList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {

                }else{
                    startPosting();
                }

            }
        });
    }



    public static String getCTime() {
        String currentDateAndTime;
        currentDateAndTime =  DateFormat.getDateTimeInstance().format(new Date());
        return currentDateAndTime;
    }

    public void showIndicator(){
        mRecyclerView.setVisibility(View.GONE);
        tvIndicator.setVisibility(View.VISIBLE);
    }

    public void hideIndicator(){
        mRecyclerView.setVisibility(View.VISIBLE);
        tvIndicator.setVisibility(View.GONE);
    }


    private void startPosting() {

        mProgress.setMessage(getString(R.string.posting_to_modo));

        final String titleVal = mPostTitle.getText().toString().trim();
        final String descVal = mPostDesc.getText().toString().trim();
        String item_price = mItemPrice.getText().toString();
        String nego = "Not Negotiable";

        final String Uname;
        final String UuId;
        final String Uemail;
        final String Upicurl;
        boolean titleValid = mTitleValidator.validate(titleVal);
        boolean descValid = mDescValidator.validate(descVal);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Upicurl = user.getPhotoUrl().toString();
        Uname = user.getDisplayName();
        UuId = user.getUid();
        Uemail = user.getEmail();
        if (mUri != null){
            if (titleValid && descValid){
                mProgress.show();
                StorageReference filepath = null;
                Random random = new Random();
                int ran = random.nextInt(100);

                if (item_price == " "){
                    item_price = "No price yet";
                }
                if (chkNego.isChecked()){
                    nego = "Negotiable";
                }
                String srand = ran+"";
                final ArrayList<Uri> arrUri = new ArrayList<>();
                final int[] imsize = {0};
                for (int i = 0; i < mImages.size(); i++) {
                    mUri = Uri.fromFile(new File(mImages.get(i).path));
                    filepath = mStorage.child("Blog_Images").child(mUri.getLastPathSegment()+srand);
                    final String finalItem_price = item_price;
                    final String finalNego = nego;
                    filepath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imsize[0] += 1;
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            arrUri.add(downloadUrl);
                            String postimages = "";
                            if (imsize[0] == mImages.size()){
                                DatabaseReference newPost = mDatabase.push();
                                for (int j = 0; j < arrUri.size(); j++){
                                    postimages += "["+arrUri.get(j)+"]";
                                }
                                newPost.child("p_image"+0).setValue(postimages);
                                newPost.child("title").setValue(titleVal);
                                newPost.child("desc").setValue(descVal);
                                newPost.child("user_name").setValue(Uname);
                                newPost.child("user_email").setValue(Uemail);
                                newPost.child("user_id").setValue(UuId);
                                newPost.child("user_pic").setValue(Upicurl);
                                newPost.child("post_time").setValue(getCTime());
                                newPost.child("item_price").setValue("₦: "+finalItem_price);
                                newPost.child("nego_stats").setValue("Status: "+ finalNego);
                                mProgress.dismiss();
                                startActivity(new Intent(PostActivity.this, MainActivity.class));
                            }
                        }
                    });
                }

            }
        }else {
            tvIndicator.setText(R.string.picture_required);
            tvIndicator.setTextColor(getResources().getColor(R.color.colorAccent2));

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            mImages = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            if (mImages.size() > 0){
                hideIndicator();
                for (int i = 0; i < mImages.size(); i++) {
                    mUri = Uri.fromFile(new File(mImages.get(i).path));

                    PhotoHelper photoHelper = new PhotoHelper(String.valueOf(mUri));
                    photoHelperList.add(photoHelper);

                    mAdapter.notifyDataSetChanged();
                /*txImageSelects.setText(txImageSelects.getText().toString().trim()
                        + "\n" +
                        String.valueOf(i + 1) + ". " + String.valueOf(uri));*/


                }
            }else{
                showIndicator();
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.postmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_photo:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Helper.checkPermissionForExternalStorage(PostActivity.this)) {
                        Helper.requestStoragePermission(PostActivity.this, READ_STORAGE_PERMISSION);
                    } else {
                        // opining custom gallery
                        Intent intent = new Intent(PostActivity.this, AlbumSelectActivity.class);
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
                        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                    }
                }else{
                    Intent intent = new Intent(PostActivity.this, AlbumSelectActivity.class);
                    intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
                    startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) return; // Only consider fields losing focus

        int id = view.getId();
        if (id == R.id.etPostTitle) {
            mTitleValidator.validate(mPostTitle.getText());
        } else if (id == R.id.etPostDesc) {
            mDescValidator.validate(mPostDesc.getText());
        }
    }
}
