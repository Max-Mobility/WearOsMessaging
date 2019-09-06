package com.github.maxmobility.wearmessage;


import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

public class Data {

    private static final String TAG = "Data";

    private static final String DATA_PATH = "/user-data";
    private static final String DATA_KEY = "data";
    private Context mContext;

    public Data(Context ctx) {
        mContext = ctx;
    }

    private static Asset toAsset(byte[] item) {
        Asset returnAsset = Asset.createFromBytes(item);
        return returnAsset;
    }

    public void sendData(String data) {
        Log.d(TAG, "Data to be sent: " + data);
        PutDataMapRequest dataMap = PutDataMapRequest.create(DATA_PATH);
        dataMap.getDataMap().putString(DATA_KEY, data);
        dataMap.getDataMap().putLong("time", new Date().getTime());
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();

        Task<com.google.android.gms.wearable.DataItem> dataItemTask = Wearable.getDataClient(mContext).putDataItem(request);

        dataItemTask.addOnSuccessListener(
                new OnSuccessListener<com.google.android.gms.wearable.DataItem>() {
                    @Override
                    public void onSuccess(com.google.android.gms.wearable.DataItem dataItem) {
                        Log.d(TAG, "Sending data was successful: " + dataItem);
                    }
                });
    }

}
