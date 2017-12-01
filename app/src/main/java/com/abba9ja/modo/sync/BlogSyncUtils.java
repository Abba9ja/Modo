package com.abba9ja.modo.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Abba9ja on 11/20/2017.
 */

public class BlogSyncUtils {
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, BlogSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
