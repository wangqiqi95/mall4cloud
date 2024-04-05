package com.mall4j.cloud.common.i18n;


import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.IOException;
import java.util.Locale;

/**
 * 多语言国际化消息工具类
 *
 * @author YXF
 */
public class I18nMessage {
    private static MessageSourceAccessor accessor;

//    private static final String BASE_FOLDE = "i18n";

    private static final String BASE_NAME = "i18n/messages";

    static{
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames(BASE_NAME);
        reloadableResourceBundleMessageSource.setCacheSeconds(5);
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        accessor = new MessageSourceAccessor(reloadableResourceBundleMessageSource);
    }

    /**
     * 获取一条语言配置信息
     *
     * @param message 配置信息属性名,eg: api.response.code.user.signUp
     * @return
     */
    public static String getMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return accessor.getMessage(message,locale);
        }catch (Exception e){
            return message;
        }

    }
    /**
     * 获取一条语言配置信息（后台管理）
     *
     * @return
     * @throws IOException
     */
    public static Integer getLang() {
        Locale locale = LocaleContextHolder.getLocale();
        return LanguageEnum.valueOf(locale).getLang();
    }

//    /**
//     * 获取一条语言配置信息(小程序、pc)
//     *
//     * @return
//     * @throws IOException
//     */
//    public static Integer getDbLang() {
//        Integer lang = getLang();
//        if (Objects.equals(lang, LanguageEnum.LANGUAGE_ZH_CN.getLang()) || Objects.isNull(lang)) {
//            return LanguageEnum.LANGUAGE_ZH_CN.getLang();
//        }
//        return lang;
//    }
}
