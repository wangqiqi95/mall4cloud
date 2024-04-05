package com.mall4j.cloud.common.exception;

import cn.hutool.core.util.ObjectUtil;

/**
 * @luzhengxiang
 * @create 2022-03-10 6:49 PM
 **/
public class Assert {
    public static void faild(String msg) {
        throw new LuckException(msg);
    }

    public static void isNull(Object o,String msg) {

        if (ObjectUtil.isEmpty(o)){
            throw new LuckException(msg);
        }

    }

    public static void notNull(Object o,String msg) {
        if (o != null){
            throw new LuckException(msg);
        }

    }

    public static void isTrue(boolean o,String msg) {
        if (o){
            throw new LuckException(msg);
        }
    }
}
