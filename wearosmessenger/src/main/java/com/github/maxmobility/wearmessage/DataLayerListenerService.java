/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.maxmobility.wearmessage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens to DataItems and Messages from the local node.
 */
public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "DataLayerServiceL";

    private static final String START_ACTIVITY_PATH = "/activity";
    private static final String DATA_ITEM_RECEIVED_PATH = "/data-item-received";

    public static final String APP_DATA_PATH = "/app-data";
    public static final String APP_DATA_KEY = "app-data";
    public static final String WEAR_DATA_PATH = "/wear-data";
    public static final String WEAR_DATA_KEY = "wear-data";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: " + dataEvents);

        // Loop through the events.
        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            String path = uri.getPath();

            DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
            if (APP_DATA_PATH.equals(path)) {
                String value = dataMap.getString(APP_DATA_KEY);
                Log.d(TAG, "string data : " + value);
            } else {
                String value = dataMap.getString(WEAR_DATA_KEY);
                Log.d(TAG, "string data : " + value);
            }
        }
    }

    private void openApp(String packageName) {
        Log.d(TAG,"Starting openApp");
        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
        startActivity(launchIntent);
    }
    
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);
        Log.d(TAG, "Message Path: " + messageEvent.getData().toString());
        String message = new String(messageEvent.getData());
        Log.d(TAG, "Message: " + message);


        String action;
        String data;

        Log.d(TAG,"Splitting message");
        String[] parts = message.split(":", 0);

        action = parts[0];
        data = parts[1];
        Log.d(TAG, "action: " + action);
        Log.d(TAG, "data: " + data);

        // Check to see if the message is to start an activity or other things
        switch (action) {
            case START_ACTIVITY_PATH:
                openApp(data);
                break;
            default:
                break;

        }
    }
}
