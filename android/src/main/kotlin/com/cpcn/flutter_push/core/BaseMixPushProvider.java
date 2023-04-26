package com.cpcn.flutter_push.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public abstract class BaseMixPushProvider {
    public abstract void register(Context context, RegisterType type);

    public abstract void unRegister(Context context);

    public abstract String getRegisterId(Context context);

    public abstract void turnOnPush(Context context);

    public abstract void turnOffPush(Context context);
    /**
     * @return 推送SDK是否支持当前设备
     */
    public abstract boolean isSupport(Context context);

    public abstract boolean isTurnOnPush();
    protected String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = appInfo.metaData.getString(name);
            if (value != null) {
                return value.toString().replace("UNIFIEDPUSH-", "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        MixPushPassThroughReceiver receiver =new MixPushPassThroughReceiver(){

            @Override
            public void onRegisterSucceed(Context context, MixPushPlatform platform) {

            }

            @Override
            public void onReceiveMessage(Context context, MixPushMessage message) {

            }
        };
        MixPushClient.getInstance().setPassThroughReceiver(receiver);
        return null;
    }

    public abstract String getPlatformName();
}