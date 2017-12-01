package com.abba9ja.modo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abba9ja.modo.data.BlogContract;
import com.abba9ja.modo.data.LangPreferences;
import com.abba9ja.modo.sync.BlogSyncUtils;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BlogAdapter.BlogAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String[] MAIN_BLOG_PROJECTION = {
            BlogContract.BlogEntry.COLUMN_ID,
            BlogContract.BlogEntry.COLUMN_TITLE,
            BlogContract.BlogEntry.COLUMN_DESC,
            BlogContract.BlogEntry.COLUMN_IMAGE,
            BlogContract.BlogEntry.COLUMN_USERNAME,
            BlogContract.BlogEntry.COLUMN_USER_EMAIL,
            BlogContract.BlogEntry.COLUMN_USERID,
            BlogContract.BlogEntry.COLUMN_USERPIC,
            BlogContract.BlogEntry.COLUMN_PTIME,
            BlogContract.BlogEntry.COLUMN_ITEM_PRICE,
            BlogContract.BlogEntry.COLUMN_NEGO,
    };

    public static final int INDEX_COLUMN_ID = 0;
    public static final int INDEX_COLUMN_TITLE = 1;
    public static final int INDEX_COLUMN_DESC = 2;
    public static final int INDEX_COLUMN_IMAGE = 3;
    public static final int INDEX_COLUMN_USERNAME = 4;
    public static final int INDEX_COLUMN_USER_EMAIL = 5;
    public static final int INDEX_COLUMN_USERID = 6;
    public static final int INDEX_COLUMN_USERPIC = 7;
    public static final int INDEX_COLUMN_PTIME = 8;
    public static final int INDEX_COLUMN_ITEM_PRICE = 9;
    public static final int INDEX_COLUMN_NEGO = 10;


    private static final int BLOG_LOADER_ID = 55;
    private int mPostion = RecyclerView.NO_POSITION;

    private static final String EXTRA_IDP_RESPONSE = "extra_idp_response";
    private static final String EXTRA_SIGNED_IN_CONFIG = "extra_signed_in_config";


    private ImageView profile_pic;
    private TextView tvname, tvemail, testing;

    private RecyclerView mBlogList;
    private BlogAdapter mBlogAdapter;

    private IdpResponse mIdpResponse;
    private SignedInConfig mSignedInConfig;
    ProgressBar prgBar;

    public static Intent createIntent(
            Context context,
            IdpResponse idpResponse,
            SignedInConfig signedInConfig) {

        Intent startIntent = new Intent();
        if (idpResponse != null) {
            startIntent.putExtra(EXTRA_IDP_RESPONSE, idpResponse);
        }

        return startIntent.setClass(context, MainActivity.class)
                .putExtra(EXTRA_SIGNED_IN_CONFIG, signedInConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LangSelector.createIntent(this));
            finish();
            return;
        }

        mIdpResponse = getIntent().getParcelableExtra(EXTRA_IDP_RESPONSE);
        mSignedInConfig = getIntent().getParcelableExtra(EXTRA_SIGNED_IN_CONFIG);
        this.setContentView(R.layout.activity_main);
        if (LangPreferences.isHausa(this)){
            setLangaugeActivity("ha");
        }else if (LangPreferences.isEnglish(this)){
            setLangaugeActivity("en");
        }
        else if (LangPreferences.isIgbo(this)){
            setLangaugeActivity("ig");
        }
        else if (LangPreferences.isYoruba(this)){
            setLangaugeActivity("yo");
        }
        //testing = (TextView) findViewById(R.id.testing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBlogAdapter = new BlogAdapter(this, this);
        mBlogList = (RecyclerView) findViewById(R.id.blogRecycler);
        prgBar = (ProgressBar) findViewById(R.id.prgbar);
        mBlogList.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        mBlogList.setLayoutManager(layoutManager);
        mBlogList.setItemAnimator(new DefaultItemAnimator());
        mBlogList.setAdapter(mBlogAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent postActivity = new Intent(MainActivity.this, PostActivity.class);
                startActivity(postActivity);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);

        profile_pic = (ImageView) hView.findViewById(R.id.profile_pic);
        tvname = (TextView) hView.findViewById(R.id.name);
        tvemail = (TextView) hView.findViewById(R.id.email);
        populateProfile();

        BlogSyncUtils.startImmediateSync(this);
        getSupportLoaderManager().initLoader(BLOG_LOADER_ID, null, this);


        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prgBar.setVisibility(View.VISIBLE);
        BlogSyncUtils.startImmediateSync(this);
        getSupportLoaderManager().restartLoader(BLOG_LOADER_ID, null, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BlogSyncUtils.startImmediateSync(this);
        getSupportLoaderManager().restartLoader(BLOG_LOADER_ID, null, this);
    }

    //@OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LangSelector.createIntent(MainActivity.this));
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
                            startActivity(LangSelector.createIntent(MainActivity.this));
                            finish();
                        } else{
                            //showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case BLOG_LOADER_ID: {
                /* URI for all rows of movie data in our popular table */
                    Uri blogQueryUri = BlogContract.BlogEntry.CONTENT_URI;
                /* Sort order: Ascending by id */
                String sortOrder = BlogContract.BlogEntry._ID + " DESC";

                return new CursorLoader(this,
                        blogQueryUri,
                        MAIN_BLOG_PROJECTION,
                        null,
                        null,
                        sortOrder);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        prgBar.setVisibility(View.GONE);
        mBlogAdapter.swapCursor(data);
        if (mPostion == RecyclerView.NO_POSITION) mPostion = 0;
        //mBlogList.smoothScrollToPosition(mPostion);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mBlogAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String[] bpost) {
        Context context = this;
        Class destinationClass = ScrollingActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, bpost);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (LangPreferences.isHausa(this)){
            setLangaugeActivity("ha");
        }else if (LangPreferences.isEnglish(this)){
            setLangaugeActivity("en");
        }
        else if (LangPreferences.isIgbo(this)){
            setLangaugeActivity("ig");
        }
        else if (LangPreferences.isYoruba(this)){
            setLangaugeActivity("yo");
        }


    }


    public void setLangaugeActivity(String langauge){
        String languageToLoad = langauge; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        MainActivity.this.onRestart();
    }

    public static final class SignedInConfig implements Parcelable {
        int logo;
        int theme;
        List<AuthUI.IdpConfig> providerInfo;
        String tosUrl;
        boolean isCredentialSelectorEnabled;
        boolean isHintSelectorEnabled;

        public SignedInConfig(int logo,
                              int theme,
                              List<AuthUI.IdpConfig> providerInfo,
                              String tosUrl,
                              boolean isCredentialSelectorEnabled,
                              boolean isHintSelectorEnabled) {
            this.logo = logo;
            this.theme = theme;
            this.providerInfo = providerInfo;
            this.tosUrl = tosUrl;
            this.isCredentialSelectorEnabled = isCredentialSelectorEnabled;
            this.isHintSelectorEnabled = isHintSelectorEnabled;
        }

        SignedInConfig(Parcel in) {
            logo = in.readInt();
            theme = in.readInt();
            providerInfo = new ArrayList<>();
            in.readList(providerInfo, AuthUI.IdpConfig.class.getClassLoader());
            tosUrl = in.readString();
            isCredentialSelectorEnabled = in.readInt() != 0;
            isHintSelectorEnabled = in.readInt() != 0;
        }

        public static final Creator<SignedInConfig> CREATOR = new Creator<SignedInConfig>() {
            @Override
            public SignedInConfig createFromParcel(Parcel in) {
                return new SignedInConfig(in);
            }

            @Override
            public SignedInConfig[] newArray(int size) {
                return new SignedInConfig[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(logo);
            dest.writeInt(theme);
            dest.writeList(providerInfo);
            dest.writeString(tosUrl);
            dest.writeInt(isCredentialSelectorEnabled ? 1 : 0);
            dest.writeInt(isHintSelectorEnabled ? 1 : 0);
        }
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("application/vnd.android.package-archive");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, getString(R.string.share_app_via)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }if (id == R.id.action_reload) {
            prgBar.setVisibility(View.VISIBLE);
            BlogSyncUtils.startImmediateSync(this);
            getSupportLoaderManager().restartLoader(BLOG_LOADER_ID, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)  {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.myAccount) {
            startActivity(new Intent(MainActivity.this,  SignedIn.class));
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(MainActivity.this,  AppSuggestions.class));
        } else if (id == R.id.nav_langauge) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        } else if (id == R.id.nav_fblike) {
            onClickLikeUs();
        }else if (id == R.id.nav_view_guide) {
            startActivity(new Intent(MainActivity.this,  WelcomeActivity.class));
        } else if (id == R.id.nav_send) {
            shareApplication();
        }else if (id == R.id.nav_my_post) {
            startActivity(new Intent(MainActivity.this,  MyPost.class));
        }else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this,  About.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onClickLikeUs(){
        final String urlFb = "fb://page/196694920901606";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If a Facebook app is installed, use it. Otherwise, launch
        // a browser
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/@abba9ja.Modo-196694920901606/";
            intent.setData(Uri.parse(urlBrowser));
        }
        startActivity(intent);
    }

}
