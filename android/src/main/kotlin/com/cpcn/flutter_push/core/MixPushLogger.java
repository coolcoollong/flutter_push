package com.cpcn.flutter_push.core;

public interface MixPushLogger {
    void log(String tag, String content, Throwable throwable);

    void log(String tag, String content);
}
