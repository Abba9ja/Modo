package com.abba9ja.modo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MessegingActivity extends AppCompatActivity {

    ImageView ivPostUserPic;
    TextView tvPostUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeging);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                String[] position = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

                String postUri = position[0];
                String postName = position[1];

                ImageView ivCpupic = (ImageView) findViewById(R.id.ivCpupic);
                TextView tvPostUsername = (TextView) findViewById(R.id.mpost_user_name);

                Uri uri =  Uri.parse(postUri);

                Glide.with(this).load(uri)
                        .placeholder(R.color.colorPrimary1)
                        .crossFade()
                        .centerCrop()
                        .into(ivCpupic);

               tvPostUsername.setText(postName);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messeging_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_phone) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
