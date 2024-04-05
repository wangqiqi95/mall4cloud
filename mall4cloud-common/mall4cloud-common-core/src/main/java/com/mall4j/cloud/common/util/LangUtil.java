package com.mall4j.cloud.common.util;


import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.i18n.I18nMessage;

import java.util.Map;
import java.util.Objects;

/**
 * 语言工具类
 * @author YXF
 * @date 2021/05/14
 */
public class LangUtil {

    /**
     * 获取语言对应的值
     *
     * @param langMap
     * @return
     */
    public static String getLangValue(Map<Integer, String> langMap){
        Integer lang = I18nMessage.getLang();
        if (Objects.nonNull(langMap.get(lang))) {
            return langMap.get(lang);
        }
        return langMap.get(Constant.DEFAULT_LANG);
    }

    /**
     * 获取语言对应的值
     *
     * @param langMap
     * @param lang
     * @return
     */
    public static String getLangValue(Map<Integer, String> langMap, Integer lang){
        if (Objects.nonNull(langMap.get(lang))) {
            return langMap.get(lang);
        }
        return langMap.get(Constant.DEFAULT_LANG);
    }

}
