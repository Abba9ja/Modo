package com.abba9ja.modo;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Iterator;

public class SignedIn extends AppCompatActivity {

    private static final String TAG = SignedIn.class.getSimpleName();

    private static final String EXTRA_IDP_RESPONSE = "extra_idp_response";
    private static final String EXTRA_SIGNED_IN_CONFIG = "extra_signed_in_config";

    private ImageView profile_pic;
    private TextView tvname, tvemail, tvphone;
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

        setContentView(R.layout.activity_signed_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(getString(R.string.my_profile));

        profile_pic = (ImageView) findViewById(R.id.siIvProfilePic);
        Drawable[] layers = new Drawable[2];
        layers[0] = getResources().getDrawable(R.drawable.ic_photo_camera_black_100dp);
        layers[1] = getResources().getDrawable(R.drawable.profile_frame);

        //they exist in memory to convert it into something your device can use
        //u need to turn it into drawable onject
        layerDrawable = new LayerDrawable(layers);

        tvname = (TextView) findViewById(R.id.si_name_val);
        tvemail = (TextView) findViewById(R.id.si_email_val);
        tvphone = (TextView) findViewById(R.id.si_number_val);


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
                            startActivity(LangSelector.createIntent(SignedIn.this));
                            finish();
                        } else {
                            //showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LangSelector.createIntent(SignedIn.this));
                            finish();
                        } else{
                            //showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }


    @MainThread
    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .fitCenter()
                    .into(profile_pic);

        }else {
            profile_pic.setImageDrawable(layerDrawable);
        }

        tvemail.setText(
                TextUtils.isEmpty(user.getEmail()) ? getString(R.string.no_email) : user.getEmail());
        tvname.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.no_display_name) : user.getDisplayName());



        StringBuilder providerList = new StringBuilder(100);

        providerList.append(getString(R.string.providers_used));

        if (user.getProviders() == null || user.getProviders().isEmpty()) {
            providerList.append(getString(R.string.none));
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
        tvphone.setText(providerList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.profile_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }
}
