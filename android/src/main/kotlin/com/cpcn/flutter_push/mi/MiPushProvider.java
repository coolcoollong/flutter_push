package com.cpcn.flutter_push.mi;

import android.content.Context;

import com.cpcn.flutter_push.core.BaseMixPushProvider;
import com.cpcn.flutter_push.core.RegisterType;
import com.cpcn.flutter_push.core.MixPushClient;
import com.cpcn.flutter_push.core.MixPushHandler;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

public class MiPushProvider extends BaseMixPushProvider {
    public static final String MI = "mi";
    public static String TAG = "MiPushProvider";
    MixPushHandler handler = MixPushClient.getInstance().getHandler();
    static RegisterType registerType;

    static Boolean isTurnOnPush =false;

    @Override
    public void register(Context context, RegisterType type) {
        MiPushProvider.registerType = type;
        String appId = getMetaData(context, "MI_APP_ID");
        String appKey = getMetaData(context, "MI_APP_KEY");
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable throwable) {
                handler.getLogger().log(TAG, content, throwable);
            }

            @Override
            public void log(String content) {
                handler.getLogger().log(TAG, content);
            }
        };
        Logger.setLogger(context, newLogger);
        MiPushClient.registerPush(context.getApplicationContext(), appId, appKey);
    }

    @Override
    public void unRegister(Context context) {
        MiPushClient.unregisterPush(context.getApplicationContext());
    }

    @Override
    public String getRegisterId(Context context) {
        return MiPushClient.getRegId(context);
    }

    @Override
    public void turnOnPush(Context context) {
        MiPushClient.enablePush(context);


        MiPushClient.UPSTurnCallBack callBack =new MiPushClient.UPSTurnCallBack() {
            @Override
            public void onResult(MiPushClient.CodeResult codeResult) {

                handler.getLogger().log(TAG, "turnOnPush_____getResultCode"+codeResult.getResultCode() +codeResult.toString());

            }
        } ;
        MiPushClient.turnOnPush(context,callBack);
        isTurnOnPush =true;
    }

    @Override
    public void turnOffPush(Context context) {
        MiPushClient.disablePush(context);
        MiPushClient.UPSTurnCallBack callBack =new MiPushClient.UPSTurnCallBack() {
            @Override
            public void onResult(MiPushClient.CodeResult codeResult) {

                handler.getLogger().log(TAG, "turnOffPush_____getResultCode"+codeResult.getResultCode() +codeResult.toString());

            }
        } ;
        MiPushClient.turnOffPush(context,callBack);
        isTurnOnPush=false;
    }


    @Override
    public boolean isSupport(Context context) {
        return true;
    }

    @Override
    public boolean isTurnOnPush() {
        return isTurnOnPush;
    }

//    @Override
//    public void setAlias(Context context, String alias) {
//        MiPushClient.setAlias(context, alias, null);
//    }

    @Override
    public String getPlatformName() {
        return MiPushProvider.MI;
    }
}
