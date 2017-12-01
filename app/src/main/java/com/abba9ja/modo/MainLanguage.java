package com.abba9ja.modo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.ButterKnife;

public class MainLanguage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    PrefLangMainActivtyManager prefLangMainActivtyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefLangMainActivtyManager = new PrefLangMainActivtyManager(this);
        if (!prefLangMainActivtyManager.isFirstTimeLaunch()) {
            lunchConitue();
            finish();
        }

        setContentView(R.layout.activity_main_language);

        ButterKnife.bind(this);

        spinner = (Spinner) findViewById(R.id.language_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.pref_langauge_options_main, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }

    private void lunchConitue() {
        prefLangMainActivtyManager.setFirstTimeLaunch(false);
        startActivity(new Intent(MainLanguage.this, LangSelector.class));
        finish();
    }

    private void setPreferenceSummary(String valueKey, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(valueKey, value);
        edit.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String spos = String.valueOf(parent.getItemAtPosition(position));
        if (spos != getString(R.string.pref_langauge_label_choose)){
            String valueKey = getString(R.string.pref_language_key);
            setPreferenceSummary(valueKey, spos);
            lunchConitue();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
