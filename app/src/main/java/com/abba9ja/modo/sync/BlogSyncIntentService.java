package com.abba9ja.modo.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Abba9ja on 11/20/2017.
 */

public class BlogSyncIntentService extends IntentService {


    public BlogSyncIntentService() {
        super("BlogSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BlogSyncTask.syncBlog(this);
    }
}
