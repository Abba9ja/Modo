package com.abba9ja.modo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abba9ja.modo.R;

/**
 * Created by Abba9ja on 11/25/2017.
 */

public class LangPreferences {

    public static boolean isEnglish(Context context) {
        //Return true if the user's preference for Sort order is High Rated,
        // false otherwise
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLangauge = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_langauge_english);
        String preferredSort = prefs.getString(keyForLangauge,defaultLanguage);
        String english = context.getString(R.string.pref_langauge_english);
        boolean userPrefersEnglish;
        if(english.equals(preferredSort)){
            userPrefersEnglish = true;
        }else {
            userPrefersEnglish = false;
        }

        return userPrefersEnglish;

    }

    public static boolean isHausa(Context context) {

        // false otherwise
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLangauge = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_langauge_hausa);
        String preferredSort = prefs.getString(keyForLangauge,defaultLanguage);
        String hausa = context.getString(R.string.pref_langauge_hausa);
        boolean userPrefersHausa;
        if(hausa.equals(preferredSort)){
            userPrefersHausa = true;
        }else {
            userPrefersHausa = false;
        }

        return userPrefersHausa;

    }

    public static boolean isYoruba(Context context) {

        // false otherwise
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLangauge = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_langauge_yoruba);
        String preferredSort = prefs.getString(keyForLangauge,defaultLanguage);
        String yoruba = context.getString(R.string.pref_langauge_yoruba);
        boolean userPrefersYoruba;
        if(yoruba.equals(preferredSort)){
            userPrefersYoruba = true;
        }else {
            userPrefersYoruba = false;
        }

        return userPrefersYoruba;

    }

    public static boolean isIgbo(Context context) {

        // false otherwise
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLangauge = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_langauge_igbo);
        String preferredSort = prefs.getString(keyForLangauge,defaultLanguage);
        String igbo = context.getString(R.string.pref_langauge_igbo);
        boolean userPrefersIgbo;
        if(igbo.equals(preferredSort)){
            userPrefersIgbo = true;
        }else {
            userPrefersIgbo = false;
        }

        return userPrefersIgbo;

    }


}
