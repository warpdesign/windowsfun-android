package com.warpdesign.windowsphonefun.interfaces;

import java.io.IOException;

public interface OnTaskCompletedListener {
    void onTaskCompleted(String xml);
    void onBeforeTask();
    void onNetworkError(IOException e);
}