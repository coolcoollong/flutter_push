package com.cpcn.flutter_push.core;

import androidx.annotation.Nullable;

public abstract class GetRegisterIdCallback {
    public abstract void callback(@Nullable MixPushPlatform platform);
}