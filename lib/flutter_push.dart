import 'package:flutter/services.dart';

import 'flutter_push_platform_interface.dart';

class FlutterPush {
  Future<String?> getPlatformVersion() {
    return FlutterPushPlatform.instance.getPlatformVersion();
  }

  init() {
    FlutterPushPlatform.instance.setMethodCallHandler(null);
  }

  initPush() {
    FlutterPushPlatform.instance.initPush();
  }

  Future<List<Object?>?> getShareAudio() async {
    return FlutterPushPlatform.instance.getShareAudio();
  }

  setMethodCallHandler(Future<dynamic> Function(MethodCall call) handler) {
    FlutterPushPlatform.instance.setMethodCallHandler(handler);
  }

  Future<bool?> isRegisteredForRemoteNotifications() {
    return FlutterPushPlatform.instance.isRegisteredForRemoteNotifications();
  }

  Future requestPush() {
    return FlutterPushPlatform.instance.requestPush();
  }

  Future unRegisterPush() {
    return FlutterPushPlatform.instance.unRegisterPush();
  }

  turnOffPush() {
    FlutterPushPlatform.instance.turnOffPush();
  }

  turnOnPush() {
    FlutterPushPlatform.instance.turnOnPush();
  }
}
