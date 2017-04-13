package com.ultratweaker.utils;

import java.io.File;

/**
 * Created by sumit on 20/12/16.
 */

public class config {
    public static final String LGE_TOUCH_DT2W = "/sys/devices/virtual/input/lge_touch/dt_wake_enabled";
    public static final String LGE_TOUCH_CORE_DT2W = "/sys/module/lge_touch_core/parameters/doubletap_to_wake";
    public static final String LGE_TOUCH_GESTURE = "/sys/devices/virtual/input/lge_touch/touch_gesture";
    public static final String ANDROID_TOUCH_DT2W = "/sys/android_touch/doubletap2wake";
    public static final String ANDROID_TOUCH2_DT2W = "/sys/android_touch2/doubletap2wake";
    public static final String TOUCH_PANEL_DT2W = "/proc/touchpanel/double_tap_enable";
    public static final String DT2W_WAKEUP_GESTURE = "/sys/devices/virtual/input/input1/wakeup_gesture";
    public static final String DT2W_ENABLE = "/sys/devices/platform/s3c2440-i2c.3/i2c-3/3-004a/dt2w_enable";
    public static final String DT2W_WAKE_GESTURE = "/sys/devices/platform/spi-tegra114.2/spi_master/spi2/spi2.0/input/input0/wake_gesture";
    public static final String DT2W_WAKE_GESTURE_2 = "/sys/devices/soc.0/f9924000.i2c/i2c-2/2-0070/input/input0/wake_gesture";
    public static final String DT2W_FT5X06 = "/sys/bus/i2c/drivers/ft5x06_i2c/5-0038/d2w_switch";

    public static final String TPanel (){
        if (new File(LGE_TOUCH_DT2W).exists()) {
            return LGE_TOUCH_DT2W;
        } else if (new File(LGE_TOUCH_CORE_DT2W).exists()) {
            return LGE_TOUCH_CORE_DT2W;
        } else if (new File(LGE_TOUCH_GESTURE).exists()) {
            return LGE_TOUCH_GESTURE;
        } else if (new File(ANDROID_TOUCH_DT2W).exists()) {
            return ANDROID_TOUCH_DT2W;
        } else if (new File(ANDROID_TOUCH2_DT2W).exists()) {
            return ANDROID_TOUCH2_DT2W;
        } else if (new File(TOUCH_PANEL_DT2W).exists()) {
            return TOUCH_PANEL_DT2W;
        } else if (new File(DT2W_WAKEUP_GESTURE).exists()) {
            return DT2W_WAKEUP_GESTURE;
        } else if (new File(DT2W_ENABLE).exists()) {
            return DT2W_ENABLE;
        } else if (new File(DT2W_WAKE_GESTURE).exists()) {
            return DT2W_WAKE_GESTURE;
        } else if (new File(DT2W_WAKE_GESTURE_2).exists()) {
            return DT2W_WAKE_GESTURE_2;
        } else if (new File(DT2W_FT5X06).exists()) {
            return DT2W_FT5X06;
        }
        return null;
    }
}