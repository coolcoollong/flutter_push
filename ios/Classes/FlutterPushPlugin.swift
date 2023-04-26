import Flutter
import UIKit

public class FlutterPushPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_push", binaryMessenger: registrar.messenger())
        let instance = FlutterPushPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        
        switch call.method{
            
        case "getPlatformVersion":
            result("iOS " + UIDevice.current.systemVersion)
            break
    
        case "getShareAudio":
            let ud = UserDefaults.init(suiteName: "group.com.cpcn.creditrisk.xinfeng")
            let audioList:Array<Any>? = ud?.object(forKey: "kAudio") as? Array<Any>
            result(audioList)
             
            break
        case "isRegisteredForRemoteNotifications":

          let  isRegisteredForRemoteNotifications = UIApplication.shared.isRegisteredForRemoteNotifications;
            
            result(isRegisteredForRemoteNotifications)
                        break
        case "requestPush":

            requestPush()
                        break
        case "unRegisterPush":

            unRegisterPush();
                        break

        default:
            result("iOS " + UIDevice.current.systemVersion)
            
        }
    
        
    }
    
    func unRegisterPush(){
        
        UIApplication.shared.unregisterForRemoteNotifications();
//        UIApplication.shared.isRegisteredForRemoteNotifications;
    }
    
    func requestPush(){
        
        //ios10.0以后
         if #available(iOS 10.0, *){
                    let notifiCenter = UNUserNotificationCenter.current()
                    notifiCenter.delegate = self
                    notifiCenter.requestAuthorization(options: [.alert,.sound,.badge]) { (accepted, error) in

                        if !accepted {
                            print("用户不允许消息通知。")
                        }
                    }
                
                    UIApplication.shared.registerForRemoteNotifications()
               //ios8.0以后
                }else if #available(iOS 8.0, *){
                
                    UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: UIUserNotificationType(rawValue: UIUserNotificationType.alert.rawValue | UIUserNotificationType.sound.rawValue | UIUserNotificationType.badge.rawValue), categories: nil))
                    UIApplication.shared.registerForRemoteNotifications()
                }else{    //其他
                    
                    let type = UIRemoteNotificationType(rawValue: UIRemoteNotificationType.alert.rawValue | UIRemoteNotificationType.badge.rawValue | UIRemoteNotificationType.sound.rawValue)
                    UIApplication.shared.registerForRemoteNotifications(matching: type)
                }
    }
}
