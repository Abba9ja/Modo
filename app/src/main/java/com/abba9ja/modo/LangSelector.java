package com.abba9ja.modo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.abba9ja.modo.data.LangPreferences;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LangSelector extends AppCompatActivity {



    public static Intent createIntent(Context context) {
        return new Intent(context, LangSelector.class);
    }

    private static final String UNCHANGED_CONFIG_VALUE = "CHANGE-ME";
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";

    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.root2)
    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        ButterKnife.bind(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startSignedInActivity(null);
            finish();
            return;
        }

    }


    public void signIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())
                        .setLogo(getSelectedLogo())
                        .setAvailableProviders(getSelectedProviders())
                        .setTosUrl(getSelectedTosUrl())
                        .setPrivacyPolicyUrl(getSelectedPrivacyPolicyUrl())
                        .setIsSmartLockEnabled(true)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }

    private void startSignedInActivity(IdpResponse response) {
        startActivity(
                MainActivity.createIntent(
                        this,
                        response,
                        new MainActivity.SignedInConfig(
                                getSelectedLogo(),
                                getSelectedTheme(),
                                getSelectedProviders(),
                                getSelectedTosUrl(),
                                true,
                                true)));
    }


    public void setLangaugeActivity(String langauge){
        String languageToLoad = langauge; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_lang_selector);
    }

    public void guideClick(View view){
        startActivity(new Intent(LangSelector.this,  WelcomeActivity.class));
        finish();
    }



    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


    @MainThread
    @StyleRes
    private int getSelectedTheme() {
        return R.style.AppTheme;
    }

    @MainThread
    @DrawableRes
    private int getSelectedLogo() {
        return R.drawable.ic_modo;
    }

    @MainThread
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        selectedProviders.add(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                        .setPermissions(getGooglePermissions())
                        .build());

        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());


        selectedProviders.add(
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                        .setPermissions(getFacebookPermissions())
                        .build());

        return selectedProviders;
    }

    @MainThread
    private List<String> getFacebookPermissions() {
        List<String> result = new ArrayList<>();
        result.add("user_friends");
        result.add("user_photos");
        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //spinner.setAdapter(adapter);
    }

    @MainThread
    private List<String> getGooglePermissions() {
        List<String> result = new ArrayList<>();

        result.add("https://www.googleapis.com/auth/youtube.readonly");
        result.add(Scopes.DRIVE_FILE);

        return result;
    }

    @MainThread
    private String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;

    }

    @MainThread
    private String getSelectedPrivacyPolicyUrl() {
        return GOOGLE_PRIVACY_POLICY_URL;
    }

    public void langContinue(View view) {
        signIn();
    }
}
