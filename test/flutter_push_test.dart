import 'dart:ffi';

import 'package:flutter/src/services/message_codec.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_push/flutter_push.dart';
import 'package:flutter_push/flutter_push_platform_interface.dart';
import 'package:flutter_push/flutter_push_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterPushPlatform
    with MockPlatformInterfaceMixin
    implements FlutterPushPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  initPush() {
    // TODO: implement initPush
    throw UnimplementedError();
  }

  @override
  setMethodCallHandler(Future Function(MethodCall call)? handler) {
    // TODO: implement setMethodCallHandler
    throw UnimplementedError();
  }

  @override
  Future<List<String>?> getShareAudio() {
    // TODO: implement getShareAudio
    throw UnimplementedError();
  }

  @override
  Future<bool> isRegisteredForRemoteNotifications() {
    // TODO: implement isRegisteredForRemoteNotifications
    throw UnimplementedError();
  }

  @override
  requestPush() {
    // TODO: implement requestPush
    throw UnimplementedError();
  }

  @override
  unRegisterPush() {
    // TODO: implement unRegisterPush
    throw UnimplementedError();
  }

  @override
  turnOffPush() {
    // TODO: implement turnOffPush
    throw UnimplementedError();
  }

  @override
  turnOnPush() {
    // TODO: implement turnOnPush
    throw UnimplementedError();
  }


}

void main() {
  final FlutterPushPlatform initialPlatform = FlutterPushPlatform.instance;

  test('$MethodChannelFlutterPush is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterPush>());
  });

  test('getPlatformVersion', () async {
    FlutterPush flutterPushPlugin = FlutterPush();
    MockFlutterPushPlatform fakePlatform = MockFlutterPushPlatform();
    FlutterPushPlatform.instance = fakePlatform;

    expect(await flutterPushPlugin.getPlatformVersion(), '42');
  });
}
