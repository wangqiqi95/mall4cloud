package com.mall4j.cloud.biz.util;

import com.alibaba.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationUtil {
    /**
     * 获取实体类对应的ExcelProperty的value值。并存储为Map<Integer,String>的形式
     *
     * @param clazz
     * @return
     */
    public static Map<Integer, String> getFieldAnnotationValue(Class clazz) {
        // 获取所有字段
        Field[] fields = clazz.getDeclaredFields();
        // 存放元素对应的数据
        Map<Integer, String> resultMap = new HashMap();
        int i = 0;
        for (Field field : fields) {
            // 是否引用ApiModelProperty注解
            boolean bool = field.isAnnotationPresent(ExcelProperty.class);
            if (bool) {
                String[] value = field.getAnnotation(ExcelProperty.class).value();
                resultMap.put(i++, String.join(",", value));
            }
        }
        return resultMap;
    }
}
