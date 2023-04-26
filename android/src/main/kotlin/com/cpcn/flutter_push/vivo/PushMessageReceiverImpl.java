package com.cpcn.flutter_push.vivo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cpcn.flutter_push.core.MixPushClient;
import com.cpcn.flutter_push.core.MixPushHandler;
import com.cpcn.flutter_push.core.MixPushPlatform;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

import java.util.Stack;

public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    MixPushHandler handler = MixPushClient.getInstance().getHandler();
    @Override
    public void onReceiveRegId(Context context, String regId) {
         try {
            String aa=VivoPushProvider.Companion.getTAG();
             Log.e("onReceiveRegId","aa________"+aa);

             String bb=VivoPushProvider.Companion.getPlatformName();
             Log.e("onReceiveRegId","aa________"+aa);

             MixPushPlatform mixPushPlatform = new MixPushPlatform(bb, regId);
             handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
         }catch (Exception e){
             Log.e("onReceiveRegId","error_______"+e.getMessage());
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
        super.onTransmissionMessage(context, unvarnishedMessage);
        Toast.makeText(context, " 收到透传通知： " + unvarnishedMessage.getMessage(), Toast.LENGTH_LONG).show();
        Log.d("OpenClientPushMessageReceiverImpl", " onTransmissionMessage= " + unvarnishedMessage.getMessage());
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage unvarnishedMessage) {
        super.onNotificationMessageClicked(context, unvarnishedMessage);
        Toast.makeText(context, " 收到通知点击回调： " + unvarnishedMessage.toString(), Toast.LENGTH_LONG).show();
        Log.d("OpenClientPushMessageReceiverImpl", " onTransmissionMessage= " + unvarnishedMessage.toString());
    }
}
