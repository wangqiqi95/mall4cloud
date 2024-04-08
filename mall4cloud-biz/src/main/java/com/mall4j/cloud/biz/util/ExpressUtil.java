package com.mall4j.cloud.biz.util;

public class ExpressUtil {
    public static void isTrue(boolean expression, Runnable runnable) {
        if (expression) {
            runnable.run();
        }
    }
}
