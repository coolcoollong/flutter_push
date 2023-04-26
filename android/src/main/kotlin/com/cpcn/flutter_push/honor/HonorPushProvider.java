package com.cpcn.flutter_push.honor;

import static com.cpcn.flutter_push.core.MixPushClient.TAG;

import android.content.Context;
import android.os.Build;

import com.cpcn.flutter_push.core.BaseMixPushProvider;
import com.cpcn.flutter_push.core.MixPushClient;
import com.cpcn.flutter_push.core.MixPushHandler;
import com.cpcn.flutter_push.core.MixPushPlatform;
import com.cpcn.flutter_push.core.RegisterType;
import com.hihonor.push.sdk.HonorPushCallback;
import com.hihonor.push.sdk.HonorPushClient;

import java.lang.reflect.Method;


public class HonorPushProvider extends BaseMixPushProvider {
    public static final String HONOR = "honor";
    public static String regId;

    MixPushHandler handler = MixPushClient.getInstance().getHandler();
    static Boolean isTurnOnPush = false;

    @Override
    public void register(Context context, RegisterType type) {
        getRegisterId(context);
    }

    @Override
    public void unRegister(Context context) {
        HonorPushClient.getInstance().deletePushToken(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                turnOffPush(context);
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                // TODO: 注销PushToken失败
            }
        });
    }

    @Override
    public boolean isSupport(Context context) {
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        handler.getLogger().log(TAG, "isSupport_____manufacturer" + manufacturer);

        if (!manufacturer.equals("honor")) {
            return false;
        }
        boolean isSupport = HonorPushClient.getInstance().checkSupportHonorPush(context);
        if (!isSupport) {
            return false;
        }
        HonorPushClient.getInstance().init(context, true);

        return true;
//        return canHuaWeiPush();
    }

    @Override
    public boolean isTurnOnPush() {
        return isTurnOnPush;
    }

    /**
     * 判断是否可以使用华为推送
     */
    public static Boolean canHuaWeiPush() {
        int emuiApiLevel = 0;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return emuiApiLevel >= 5.0;
    }

    @Override
    public String getPlatformName() {
        return HonorPushProvider.HONOR;
    }

    @Override
    public String getRegisterId(Context context) {


        // 获取PushToken
        HonorPushClient.getInstance().getPushToken(new HonorPushCallback<String>() {
            @Override
            public void onSuccess(String pushToken) {
                regId =pushToken;
                MixPushPlatform mixPushPlatform = new MixPushPlatform(HonorPushProvider.HONOR, regId);
                MixPushClient.getInstance().getHandler().getPushReceiver().onRegisterSucceed(context, mixPushPlatform);

                turnOnPush(context);
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                handler.getLogger().log(TAG, "getRegisterId onFailure" + errorCode + errorString);
            }
        });


        return null;
//        return null;
    }


    @Override
    public void turnOnPush(Context context) {

        HonorPushClient.getInstance().turnOnNotificationCenter(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isTurnOnPush = true;
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                handler.getLogger().log(TAG, "turnOnPush onFailure" + errorCode + errorString);
            }
        });

    }

    @Override
    public void turnOffPush(Context context) {

        HonorPushClient.getInstance().turnOffNotificationCenter(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isTurnOnPush = false;
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                handler.getLogger().log(TAG, "turnOffPush onFailure" + errorCode + errorString);
            }
        });
    }


}
