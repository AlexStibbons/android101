package com.example.proto_korzo.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.proto_korzo.Utils;

public class FaveChangeBroadcast extends BroadcastReceiver {

    private final String IF_FAVE_CHANGE = Utils.IF_FAVE_CHANGED;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (IF_FAVE_CHANGE.equals(intent.getAction())) {

            Toast.makeText(context, "Change data / refresh fragments", Toast.LENGTH_SHORT).show();
        }

    }
}
