package com.cpcn.flutter_push.vivo

import android.content.Context
import android.os.Build
import com.cpcn.flutter_push.core.*
import com.vivo.push.IPushActionListener
import com.vivo.push.PushClient
import com.vivo.push.util.VivoPushException
import java.util.*

class VivoPushProvider : BaseMixPushProvider() {


    private var handler: MixPushHandler = MixPushClient.getInstance().handler
    override fun register(context: Context?, type: RegisterType?) {
        handler.logger.log(TAG, "initialize")
        try {
            PushClient.getInstance(context).initialize()
            PushClient.getInstance(context).turnOnPush(IPushActionListener { state: Int ->
                // 开关状态处理， 0代表成功
                if (state == 0) {
                    isTurnOnPush=true;
                    handler.logger.log(TAG, "开启成功了")
                } else {
                    handler.logger.log(TAG, "开启失败$state")
                }
            })

            handler.logger.log(TAG, "get________regId")
            val regId: String? = PushClient.getInstance(context).regId
            handler.logger.log(TAG, "get________regId =$regId")
            // 有时候会出现没有回调 OpenClientPushMessageReceiver.onReceiveRegId 的情况,所以需要进行检测
            if (regId != null) {
                handler.logger.log(TAG, "regId != null")
                val mixPushPlatform = platformName?.let { MixPushPlatform(it, regId) }
                handler.logger.log(TAG, "onRegisterSucceed_____platformName${platformName}")
                handler.pushReceiver.onRegisterSucceed(context, mixPushPlatform)
            }
        } catch (e: VivoPushException) {
            handler.getLogger().log(TAG, "vivo 初始化失败", e)
        }
    }

    override fun unRegister(context: Context?) {
        PushClient.getInstance(context).turnOffPush {state: Int ->
            // 开关状态处理， 0代表成功
            if (state == 0) {
                isTurnOnPush=false;
                handler.logger.log(TAG, "关闭成功了")
            }
        }
    }

    override fun getRegisterId(context: Context?): String? {
        return PushClient.getInstance(context).regId
    }

    override fun turnOnPush(context: Context?) {

        PushClient.getInstance(context).turnOnPush(IPushActionListener { state: Int ->
            // 开关状态处理， 0代表成功
            if (state == 0) {
                isTurnOnPush=true;
                handler.logger.log(TAG, "开启成功了")
            } else {
                handler.logger.log(TAG, "开启失败$state")
            }
        })
    }

    override fun turnOffPush(context: Context?) {
        PushClient.getInstance(context).turnOffPush {state: Int ->
            // 开关状态处理， 0代表成功
            if (state == 0) {
                isTurnOnPush=false;
                handler.logger.log(TAG, "关闭成功了")
            }
        }
    }


    override fun isSupport(context: Context?): Boolean {
        if (Build.VERSION.SDK_INT < 15) {
            return false
        }
        val brand = Build.BRAND.lowercase(Locale.getDefault())
        val manufacturer = Build.MANUFACTURER.lowercase(Locale.getDefault())
        return if (manufacturer == "vivo" || brand.contains("vivo") || brand.contains("iqoo")) {
            PushClient.getInstance(context).isSupport()
        } else false
    }

    override fun getPlatformName(): String {
        return  "vivo";
    }

    override fun isTurnOnPush(): Boolean {

        return isTurnOnPush;
    }
    private var isTurnOnPush:Boolean=false
    companion object {
         val platformName = "vivo"
         var TAG = "v-i-v-o"

    }
}