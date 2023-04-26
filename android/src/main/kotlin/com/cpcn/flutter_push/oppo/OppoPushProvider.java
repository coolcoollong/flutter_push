package com.cpcn.flutter_push.oppo;


import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.cpcn.flutter_push.core.BaseMixPushProvider;
import com.cpcn.flutter_push.core.MixPushClient;
import com.cpcn.flutter_push.core.MixPushHandler;
import com.cpcn.flutter_push.core.MixPushPlatform;
import com.cpcn.flutter_push.core.RegisterType;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.heytap.msp.push.callback.ISetAppNotificationCallBackService;

public class OppoPushProvider extends BaseMixPushProvider {
    public static final String OPPO = "oppo";
    public static final String TAG = OPPO;

    static Boolean isTurnOnPush = false;
    private MixPushHandler handler = MixPushClient.getInstance().getHandler();

    @Override
    public void register(Context context, RegisterType type) {
        String appSecret = getMetaData(context, "OPPO_APP_SECRET");
        String appKey = getMetaData(context, "OPPO_APP_KEY");


        handler.getLogger().log(TAG, "register______appSecret =" + appSecret + "appKey =" + appKey);
        HeytapPushManager.init(context, MixPushClient.debug);
        HeytapPushManager.register(context, appKey, appSecret, new MyCallBackResultService(context.getApplicationContext()));
        String registerID = HeytapPushManager.getRegisterID();
        if (registerID != null) {
            isTurnOnPush = true;
            MixPushPlatform mixPushPlatform = new MixPushPlatform(OppoPushProvider.OPPO, registerID);
            MixPushClient.getInstance().getHandler().getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
        }
    }

    @Override
    public void unRegister(Context context) {
        HeytapPushManager.unRegister();
        isTurnOnPush = false;
    }

    @Override
    public String getRegisterId(final Context context) {
        return HeytapPushManager.getRegisterID();
    }

    @Override
    public void turnOnPush(Context context) {

//        ISetAppNotificationCallBackService callBackService = new ISetAppNotificationCallBackService(
//
//        ) {
//            @Override
//            public void onSetAppNotificationSwitch(int i) {
//
//                handler.getLogger().log(TAG, "turnOnPush    i=" + i );
//                isTurnOnPush = true;
//            }
//        };
//        HeytapPushManager.enableAppNotificationSwitch(callBackService);
        HeytapPushManager.resumePush();

        HeytapPushManager.getPushStatus();
//        handler.getLogger().log(TAG, "turnOnPush  ______start" );
//        Handler handler = new Handler(); // 如果这个handler是在UI线程中创建的
//        handler.postDelayed(new Runnable() {  // 开启的runnable也会在这个handler所依附线程中运行，即主线程
//            @Override
//            public void run() {
//                HeytapPushManager.getPushStatus();
//                HeytapPushManager.getNotificationStatus();
//            }
//        }, 500);

    }

    @Override
    public void turnOffPush(Context context) {

        handler.getLogger().log(TAG, "turnOffPush  ______start" );
        handler.getLogger().log(TAG, "turnOffPush  ______pausePush" );
//        ISetAppNotificationCallBackService callBackService = new ISetAppNotificationCallBackService(
//
//        ) {
//            @Override
//            public void onSetAppNotificationSwitch(int i) {
//                handler.getLogger().log(TAG, "turnOffPush    i=" + i );
//
//                isTurnOnPush = false;
//            }
//        };

//
//        HeytapPushManager.disableAppNotificationSwitch(callBackService);

        HeytapPushManager.pausePush();
        HeytapPushManager.getPushStatus();



    }

    @Override
    public boolean isSupport(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        String brand = Build.BRAND.toLowerCase();
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (manufacturer.equals("oneplus") || manufacturer.equals("oppo") || brand.equals("oppo") || brand.equals("realme")) {
            HeytapPushManager.init(context, true);
            return HeytapPushManager.isSupportPush(context);
        }
        return false;
    }

    @Override
    public boolean isTurnOnPush() {
        handler.getLogger().log(TAG, "isTurnOnPush =" + isTurnOnPush );

        return isTurnOnPush;
    }

    @Override
    public String getPlatformName() {
        return OppoPushProvider.OPPO;
    }
}

class MyCallBackResultService implements ICallBackResultService {
    Context context;
    MixPushHandler handler = MixPushClient.getInstance().getHandler();


    public MyCallBackResultService(Context context) {
        this.context = context;
    }

    @Override
    public void onRegister(int responseCode, String registerID) {
        handler.getLogger().log(OppoPushProvider.TAG, "onRegister responseCode = " + responseCode + ", registerID = " + registerID);
        MixPushPlatform mixPushPlatform = new MixPushPlatform(OppoPushProvider.OPPO, registerID);
        handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
    }

    @Override
    public void onUnRegister(int responseCode) {

    }

    @Override
    public void onSetPushTime(int responseCode, String pushTime) {

    }

    @Override
    public void onGetPushStatus(int responseCode, int status) {
        if(status==0) {
            OppoPushProvider.isTurnOnPush = true;
        }else{
            OppoPushProvider.isTurnOnPush = false;
        }
        handler.getLogger().log(OppoPushProvider.TAG, "onGetPushStatus responseCode = " + responseCode + ", status = " + status);
    }

    @Override
    public void onGetNotificationStatus(int responseCode, int status) {
        handler.getLogger().log(OppoPushProvider.TAG, "onGetNotificationStatus responseCode = " + responseCode + ", status = " + status);
    }

    @Override
    public void onError(int i, String s) {
        handler.getLogger().log(OppoPushProvider.TAG, "onGetPushStatus errorCode = " + i + ", error = " + s);
    }
}




