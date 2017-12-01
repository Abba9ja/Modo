package com.abba9ja.modo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.email.fieldvalidators.EmailFieldValidator;
import com.firebase.ui.auth.ui.email.fieldvalidators.RequiredFieldValidator;
import com.firebase.ui.auth.ui.phone.CountryListSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

public class EditProfile1 extends AppCompatActivity implements View.OnFocusChangeListener {

    private static final String TAG = SignedIn.class.getSimpleName();

    private static final String EXTRA_IDP_RESPONSE = "extra_idp_response";
    private static final String EXTRA_SIGNED_IN_CONFIG = "extra_signed_in_config";


    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private Uri mUri = null;
    private ProgressDialog mProgress;

    private EmailFieldValidator mEmailFieldValidator;
    private RequiredFieldValidator mNameValidator;
    ArrayList<Image> mImages;
    ImageView profile_pic, add_photo;
    TextInputLayout TILName, TILEmailAdd;
    EditText etName, etEmailladd, phone_number;
    CountryListSpinner country_list;

    LayerDrawable layerDrawable;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LangSelector.createIntent(this));
            finish();
            return;
        }
        setContentView(R.layout.activity_edit_profile1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String cUserId = currentUser.getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(cUserId);

        TILName = (TextInputLayout) findViewById(R.id.TILName);
        TILEmailAdd = (TextInputLayout) findViewById(R.id.TILEmailAdd);

        profile_pic = (ImageView) findViewById(R.id.siIvProfilePic);
        add_photo = (ImageView) findViewById(R.id.add_photo);
        Drawable[] layers = new Drawable[2];
        layers[0] = getResources().getDrawable(R.drawable.ic_photo_camera_black_100dp);
        layers[1] = getResources().getDrawable(R.drawable.profile_frame);

        //they exist in memory to convert it into something your device can use
        //u need to turn it into drawable onject
        layerDrawable = new LayerDrawable(layers);
        etName = (EditText) findViewById(R.id.etName);
        etEmailladd = (EditText) findViewById(R.id.etEmailladd);
        phone_number = (EditText) findViewById(R.id.phone_number);
        country_list = (CountryListSpinner) findViewById(R.id.country_list);

        mProgress = new ProgressDialog(this);

        mNameValidator = new RequiredFieldValidator(TILName);
        mEmailFieldValidator = new EmailFieldValidator(TILEmailAdd);

        etEmailladd.setOnFocusChangeListener(this);
        etName.setOnFocusChangeListener(this);

        populateProfile();
    }


    //@OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LangSelector.createIntent(EditProfile1.this));
                            finish();
                        } else {
                            //showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        validateAndRegisterUser();
    }


    private void validateAndRegisterUser() {
        String name = etName.getText().toString();
        String email = etEmailladd.getText().toString();

        boolean emailValid = mEmailFieldValidator.validate(email);
        boolean nameValid = mNameValidator.validate(name);
        String phoneValid = phone_number.getText().toString();

        if (emailValid && nameValid) {
            //getDialogHolder().showLoadingDialog(R.string.fui_progress_dialog_signing_up);
            registerUser(email, name, phoneValid, mUri);
        }
    }

    private void registerUser(final String email, final String name, final String phone_number, final Uri uri) {
        mProgress.setMessage("Updating Modo account ...");
        mProgress.show();
        if (uri != null){

        }else {
            Toast.makeText(this, "Profile pic required", Toast.LENGTH_SHORT).show();
        }
    }


    @MainThread
    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .fitCenter()
                    .into(profile_pic);
            mUri = user.getPhotoUrl();
            hideIndicator();
        }else {
            profile_pic.setImageDrawable(layerDrawable);
        }
        etEmailladd.setText(
                TextUtils.isEmpty(user.getEmail()) ? " " : user.getEmail());
        etName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? " " : user.getDisplayName());
        phone_number.setText(
                TextUtils.isEmpty(user.getPhoneNumber()) ? " " : user.getPhoneNumber());

        StringBuilder providerList = new StringBuilder(100);

        providerList.append("Providers used: ");

        if (user.getProviders() == null || user.getProviders().isEmpty()) {
            providerList.append("none");
        } else {
            Iterator<String> providerIter = user.getProviders().iterator();
            while (providerIter.hasNext()) {
                String provider = providerIter.next();
                if (GoogleAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Google");
                } else if (FacebookAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Facebook");
                } else if (EmailAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Password");
                } else {
                    providerList.append(provider);
                }

                if (providerIter.hasNext()) {
                    providerList.append(", ");
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.edprofile_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            signOut();
        }
        if (id == R.id.action_done) {
            validateAndRegisterUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (id == R.id.etName) {
            mNameValidator.validate(etName.getText());
        } else if (id == R.id.etEmailladd) {
            mEmailFieldValidator.validate(etEmailladd.getText());
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

                    Glide.with(this).load(mUri)
                            .placeholder(R.color.colorAccent1)
                            .override(100, 100)
                            .crossFade()
                            .centerCrop()
                            .into(profile_pic);
                /*txImageSelects.setText(txImageSelects.getText().toString().trim()
                        + "\n" +
                        String.valueOf(i + 1) + ". " + String.valueOf(uri));*/
                }
            }else{
                showIndicator();
            }
        }
    }

    private void hideIndicator() {
        add_photo.setVisibility(View.INVISIBLE);
    }
    private void showIndicator() {
        add_photo.setVisibility(View.VISIBLE);
    }

    public void add_photo(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Helper.checkPermissionForExternalStorage(EditProfile1.this)) {
                Helper.requestStoragePermission(EditProfile1.this, READ_STORAGE_PERMISSION);
            } else {
                // opining custom gallery
                Intent intent = new Intent(EditProfile1.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        }else{
            Intent intent = new Intent(EditProfile1.this, AlbumSelectActivity.class);
            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
            startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
        }
    }
}
