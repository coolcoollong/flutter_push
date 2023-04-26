package com.cpcn.flutter_push.huawei;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.cpcn.flutter_push.core.BaseMixPushProvider;
import com.cpcn.flutter_push.core.MixPushClient;
import com.cpcn.flutter_push.core.MixPushHandler;
import com.cpcn.flutter_push.core.MixPushPlatform;
import com.cpcn.flutter_push.core.RegisterType;
import com.huawei.agconnect.AGConnectOptionsBuilder;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.adapter.internal.AvailableCode;
import com.huawei.hms.api.HuaweiMobileServicesUtil;
import com.huawei.hms.common.ApiException;
import com.cpcn.flutter_push.core.BaseMixPushProvider;
import com.cpcn.flutter_push.core.MixPushPlatform;
import com.cpcn.flutter_push.core.RegisterType;
import com.cpcn.flutter_push.core.MixPushClient;
import com.cpcn.flutter_push.core.MixPushHandler;
import com.huawei.hms.push.HmsMessaging;

import java.lang.reflect.Method;

import static com.cpcn.flutter_push.core.MixPushClient.TAG;
import static com.huawei.hms.adapter.internal.AvailableCode.SERVICE_VERSION_UPDATE_REQUIRED;


public class HuaweiPushProvider extends BaseMixPushProvider {
    public static final String HUAWEI = "huawei";
    public static String regId;

    MixPushHandler handler = MixPushClient.getInstance().getHandler();
    static Boolean isTurnOnPush = false;

    @Override
    public void register(Context context, RegisterType type) {
        HmsMessaging.getInstance(context).turnOnPush();
        syncGetToken(context);
    }

    @Override
    public void unRegister(Context context) {

    }

    @Override
    public boolean isSupport(Context context) {
        if (Build.VERSION.SDK_INT < 17) {
            return false;
        }
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        handler.getLogger().log(TAG, "isSupport_____manufacturer" + manufacturer);

        if (!manufacturer.equals("huawei")&&!manufacturer.equals("honor")){
            return false;
        }
        int available = HuaweiMobileServicesUtil.isHuaweiMobileServicesAvailable(context);
        if (available != AvailableCode.SUCCESS) {
            handler.getLogger().log(TAG, "华为推送不可用 ErrorCode = " + available);
            return false;
        }
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
        return HuaweiPushProvider.HUAWEI;
    }

    private static Object lockObject = new Object();

    @Override
    public String getRegisterId(Context context) {


        try {
            // read from agconnect-services.json
            String appId = new AGConnectOptionsBuilder().build(context).getString("client/app_id");

            Log.e(TAG, "get token:appId" + appId);
//            String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
            String regId = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
            Log.e(TAG, "get token:regId =" + regId);
            MixPushClient.regId = regId;

            return  regId;
        } catch (ApiException e) {
            handler.getLogger().log(TAG, "hms get token failed " + e.toString() + " https://developer.huawei.com/consumer/cn/doc/development/HMSCore-References-V5/error-code-0000001050255690-V5", e);
            e.printStackTrace();
        }




//        long start = System.currentTimeMillis();
//
//        synchronized (lockObject) {
//
//            try {
//
//                System.out.println("主线程等待");
//
//                lockObject.wait();
//
//            } catch (InterruptedException e) {
//
//                e.printStackTrace();
//
//            }
//
//            System.out.println("主线程继续 --> 等待的时间：" + (System.currentTimeMillis() - start)+"regId"+regId);
//           return  regId;
//        }

        return null;
//        return null;
    }

    class WaitThread extends Thread {

        @Override

        public void run() {

//            //子线程等待2秒后唤醒lockObject锁
//            try {
//                // read from agconnect-services.json
//                String appId = new AGConnectOptionsBuilder().build(mContext).getString("client/app_id");
//
//                Log.e(TAG, "get token:appId" + appId);
////            String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
//                String regId = HmsInstanceId.getInstance(mContext).getToken(appId, "HCM");
//                Log.e(TAG, "get token:regId =" + regId);
//                MixPushClient.regId = regId;
//            } catch (ApiException e) {
//                handler.getLogger().log(TAG, "hms get token failed " + e.toString() + " https://developer.huawei.com/consumer/cn/doc/development/HMSCore-References-V5/error-code-0000001050255690-V5", e);
//                e.printStackTrace();
//            }


        }

    }

    @Override
    public void turnOnPush(Context context) {
        HmsMessaging.getInstance(context).turnOnPush();
        isTurnOnPush =true;
    }

    @Override
    public void turnOffPush(Context context) {
        HmsMessaging.getInstance(context).turnOffPush();
        isTurnOnPush =false;
    }


    private void syncGetToken(final Context context) {
        new Thread() {
            @Override
            public void run() {
                String regId = getRegisterId(context);
                if (!TextUtils.isEmpty(regId)) {
                    isTurnOnPush =true;
                    MixPushPlatform mixPushPlatform = new MixPushPlatform(HuaweiPushProvider.HUAWEI, regId);
                    MixPushClient.getInstance().getHandler().getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
                }
            }
        }.start();
    }
}
