package com.cpcn.flutter_push

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.annotation.NonNull
import com.cpcn.flutter_push.MyMixPushReceiver
import com.cpcn.flutter_push.core.*
import com.vivo.push.IPushActionListener
import com.vivo.push.PushClient
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.ref.WeakReference


/** UnifiedPushPlugin */
class FlutterPushPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    lateinit var channel: MethodChannel
    private var activity: Activity? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_push")
        channel.setMethodCallHandler(this)
    }
//    turnOnPush      打开PUSH；
//
//    turnOffPush
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "initPush" -> {
                Log.i("onMethodCall", "initPush___________start")
                result.success("aa")
                initPush()
            }
            "getPlatformVersion" -> {
                result.success("getPlatformVersion")
            }
            "isRegisteredForRemoteNotifications" -> {
                result.success(isRegisteredForRemoteNotifications())
            }
            "requestPush" -> {
                result.success("aa")
               requestPush()
            }
            "unRegister" -> {
                result.success("aa")
                unRegisterPush()
            }
            "turnOnPush" -> {
                result.success("aa")
                turnOnPush()
            }
            "turnOffPush" -> {
                result.success("aa")
                turnOffPush()
            }



            else -> {

                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    companion object {
        const val WHAT_HINT_TEXT = 1000 //MSG_WHAT
    }


    private fun initPush() {

        MixPushClient.getInstance().setLogger(object : MixPushLogger {
            override fun log(tag: String, content: String, throwable: Throwable?) {
                Log.e(tag, content)
                throwable?.printStackTrace()
            }

            override fun log(tag: String, content: String) {
                Log.e(tag, content)
            }
        });
        MixPushClient.getInstance().setPushReceiver(MyMixPushReceiver(this))

        MixPushClient.getInstance().register(activity)
//        MixPushClient.getInstance().getRegisterId(activity, object : GetRegisterIdCallback() {
//            override fun callback(platform: MixPushPlatform?) {
//                Log.e("GetRegisterIdCallback", "notification $platform")
//            }
//        })
    }

    private fun isRegisteredForRemoteNotifications(): Boolean {

        return MixPushClient.getInstance().isRegisteredForRemoteNotifications
    }

    private fun requestPush() {
        MixPushClient.getInstance().register(activity)
    }
    private fun unRegisterPush() {

        MixPushClient.getInstance().unRegister(activity)
    }

    private fun turnOnPush() {

        MixPushClient.getInstance().turnOnPush(activity)
    }
    private fun turnOffPush() {

        MixPushClient.getInstance().turnOffPush(activity)
    }

    // actity 状态
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity

    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        activity = null
    }


    private fun initViVo() {
        Log.i("onMethodCall", "initViVo__________1111");

        if (PushClient.getInstance(activity).isSupport) {

            PushClient.getInstance(activity).initialize()
            Log.i("onMethodCall", "initViVo__________2222");
            PushClient.getInstance(activity).turnOnPush { state -> // TODO: 开关状态处理， 0代表成功
                Log.d("MainActivity.TAG", "turnOnPush state= $state")
                var regId = ""
                if (state == 0) {
                    Log.i("onMethodCall", "initViVo__________成功");
                    regId = PushClient.getInstance(activity).regId
                    Log.i("onMethodCall", "initViVo_________regId$regId");
                    var message = Message();
                    message.what = WHAT_HINT_TEXT;
                    message.obj = regId;
//                    weakHandler?.sendMessage(message);
                }
                Log.e("onMethodCall", "initViVo_________$state");
            }
        }

    }
}
