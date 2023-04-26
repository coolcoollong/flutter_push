import 'dart:ffi';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_push_platform_interface.dart';

/// An implementation of [FlutterPushPlatform] that uses method channels.
class MethodChannelFlutterPush extends FlutterPushPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_push');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  setMethodCallHandler(Future<dynamic> Function(MethodCall call)? handler) {
    return methodChannel.setMethodCallHandler(handler ?? _methodCallHandler);
  }

  static Future<void> _methodCallHandler(MethodCall call) async {
    switch (call.method) {
      case 'onRegisterSucceed':
        print("onRegisterSucceed________${call.arguments.runtimeType}");
        print("onRegisterSucceed________${call.arguments}");
        if (call.arguments is Map) {
          print(
              "onRegisterSucceed________${call.arguments['platformName']}  ${call.arguments['regId']}");
        }
        break;
      default:
    }
  }

  @override
  Future<List<Object?>?> getShareAudio() async {
    if (Platform.isIOS == true) {
      final audio =
          await methodChannel.invokeMethod<List<Object?>>('getShareAudio');
      return audio;
    } else {
      return [];
    }
  }

  @override
  initPush() async {
    await methodChannel.invokeMethod<String>('initPush');
  }

  @override
  Future<bool>  isRegisteredForRemoteNotifications() async {
    bool? isRegisteredForRemoteNotifications =
    await methodChannel.invokeMethod<bool>('isRegisteredForRemoteNotifications');
    return  isRegisteredForRemoteNotifications??false;
  }

  @override
  requestPush() async{
    await methodChannel.invokeMethod<String>('requestPush');
  }
  @override
  unRegisterPush() async{
    await methodChannel.invokeMethod<String>('unRegisterPush');
  }

  @override
  turnOffPush() async{
    // TODO: implement turnOffPush
    await methodChannel.invokeMethod<String>('turnOffPush');
  }

  @override
  turnOnPush() async{
    // TODO: implement turnOnPush
    await methodChannel.invokeMethod<String>('turnOnPush');
  }

}
