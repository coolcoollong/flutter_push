package com.cpcn.flutter_push

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.cpcn.flutter_push.core.MixPushReceiver
import com.cpcn.flutter_push.core.MixPushMessage
import com.cpcn.flutter_push.core.MixPushPlatform


import org.json.JSONObject
import java.lang.ref.WeakReference

class MyMixPushReceiver
/**
 * 重载构造函数, 还可以声明其他的构造函数.
 */ public constructor(
    /**
     * 标题
     */
    private var plugin: FlutterPushPlugin,

    ) : MixPushReceiver() {

    private val weakHandler by lazy { this?.let { WeakReferenceHandler(plugin) } }

    //static + 弱引用
    class WeakReferenceHandler(obj: FlutterPushPlugin) : Handler(Looper.getMainLooper()) {
        private val mRef: WeakReference<FlutterPushPlugin> = WeakReference(obj)


        override fun handleMessage(msg: Message) {
            mRef.get()?.run {
                when (msg.what) {
                    //可以直接访问Activity中的变量

                    FlutterPushPlugin.WHAT_HINT_TEXT -> {

                        mRef.get()?.channel?.invokeMethod(
                            "onRegisterSucceed",
                            msg.obj

                        )

                    }
                    else -> print("ee")
                }
            }
        }
    }


    override fun onRegisterSucceed(context: Context, platform: MixPushPlatform) {

        var message = Message()
        message.what = FlutterPushPlugin.WHAT_HINT_TEXT;
        message.obj = mapOf("platformName" to platform.platformName, "regId" to platform.regId)
        weakHandler?.sendMessage(message);

        Log.e("onRegisterSucceed______", "platform____$platform")
    }

    override fun onNotificationMessageClicked(context: Context, message: MixPushMessage) {
        var intent: Intent? = null
        if (message.payload == null) {
            // 启动APP
            intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        } else {
            val json = JSONObject(message.payload)
            val url = json.optString("url", "")
            if (url == "") {
                // 启动APP
                intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            } else {
                val uri = Uri.parse(json.getString("url"))
                // 打开浏览器
//                if (uri.scheme!!.contains("http")) {
//                    intent = Intent(context, WebViewActivity::class.java).putExtra("url", url)
//                } else if (uri.path == "/user") {
//                    val userId = uri.getQueryParameter("userId")
//                    intent = Intent(context, UserActivity::class.java).putExtra("userId", userId)
//                }
            }
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}