package com.example.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Time;
import android.util.Log;

import com.example.xyzreader.R;
import com.example.xyzreader.remote.RemoteEndpointUtil;
import com.example.xyzreader.ui.ArticleListActivity;
import com.example.xyzreader.util.Utilies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time time = new Time();

        if (!Utilies.isNetworkAvailable(this)) {
            sendIntentMessage(getResources().getString(R.string.no_network));
            Log.w(TAG, getResources().getString(R.string.no_network));
            return;
        }

        sendIntentActionState(true);

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = ItemsContract.Items.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        try {
            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
            if (array == null) {
                sendIntentMessage(getResources().getString(R.string.server_down));
                Log.e(TAG, getResources().getString(R.string.server_down));
            }

            for (int i = 0; i < array.length(); i++) {
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                values.put(ItemsContract.Items.SERVER_ID, object.getString("id" ));
                values.put(ItemsContract.Items.AUTHOR, object.getString("author" ));
                values.put(ItemsContract.Items.TITLE, object.getString("title" ));
                values.put(ItemsContract.Items.BODY, object.getString("body" ));
                values.put(ItemsContract.Items.THUMB_URL, object.getString("thumb" ));
                values.put(ItemsContract.Items.PHOTO_URL, object.getString("photo" ));
                values.put(ItemsContract.Items.ASPECT_RATIO, object.getString("aspect_ratio" ));
                time.parse3339(object.getString("published_date"));
                values.put(ItemsContract.Items.PUBLISHED_DATE, time.toMillis(false));
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);

        } catch (JSONException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, getResources().getString(R.string.server_error));
            sendIntentMessage(getResources().getString(R.string.server_error));
        }

        sendIntentActionState(false);
    }

    private void sendIntentMessage(String message){
        Intent messageIntent = new Intent(ArticleListActivity.BROADCAST_ACTION_STATE_CHANGE);
        messageIntent.putExtra(ArticleListActivity.MESSAGE_KEY, message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageIntent);
    }

    private void sendIntentActionState(boolean actionState){
        Intent messageIntent = new Intent(ArticleListActivity.BROADCAST_ACTION_STATE_CHANGE);
        messageIntent.putExtra(ArticleListActivity.EXTRA_REFRESHING, actionState);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageIntent);
    }
}
