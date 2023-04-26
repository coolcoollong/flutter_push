

import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_push_method_channel.dart';

abstract class FlutterPushPlatform extends PlatformInterface {
  /// Constructs a FlutterPushPlatform.
  FlutterPushPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterPushPlatform _instance = MethodChannelFlutterPush();

  /// The default instance of [FlutterPushPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterPush].
  static FlutterPushPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterPushPlatform] when
  /// they register themselves.
  static set instance(FlutterPushPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  setMethodCallHandler(Future<dynamic> Function(MethodCall call)? handler){

  }

  Future<List<Object?>?> getShareAudio() {

    throw UnimplementedError('getShareAudio() has not been implemented.');
  }

  Future<bool> isRegisteredForRemoteNotifications() {

    throw UnimplementedError('getShareAudio() has not been implemented.');
  }
  initPush(){

  }
  requestPush(){

  }

  unRegisterPush(){

  }


  turnOnPush(){

  }

  turnOffPush(){

  }
}
