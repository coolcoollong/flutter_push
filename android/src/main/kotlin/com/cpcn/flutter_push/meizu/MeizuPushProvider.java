package com.cpcn.flutter_push.meizu;

import android.content.Context;
import android.os.Build;

import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.cpcn.flutter_push.core.BaseMixPushProvider;
import com.cpcn.flutter_push.core.RegisterType;

public class MeizuPushProvider extends BaseMixPushProvider {
    public static final String MEIZU = "meizu";
    private String appId;
    private String appKey;
    private String pushId;

    static Boolean isTurnOnPush = false;
    @Override
    public void register(Context context, RegisterType type) {
        appId = getMetaData(context, "MEIZU_APP_ID");
        appKey = getMetaData(context, "MEIZU_APP_KEY");
        PushManager.register(context, appId, appKey);


    }

    @Override
    public void unRegister(Context context) {

        PushManager.unRegister(context, appId, appKey);
        isTurnOnPush=false;
    }

    @Override
    public String getRegisterId(Context context) {
        pushId =PushManager.getPushId(context);
        return pushId;
    }

    @Override
    public void turnOnPush(Context context) {
        PushManager.switchPush(context,appId,appKey,pushId,true);
        isTurnOnPush=true;
    }

    @Override
    public void turnOffPush(Context context) {

        PushManager.switchPush(context,appId,appKey,pushId,false);
        isTurnOnPush=false;
    }


    @Override
    public boolean isSupport(Context context) {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (manufacturer.equals("meizu")) {
            return MzSystemUtils.isBrandMeizu(context);
        }
        return false;
    }

    @Override
    public boolean isTurnOnPush() {
        return false;
    }

    @Override
    public String getPlatformName() {
        return MeizuPushProvider.MEIZU;
    }
}
