package com.mall4j.cloud.common.i18n;


import com.mall4j.cloud.common.constant.Constant;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * RequestContextFilter 会传入默认的Locale，优先级(-105) 要比RequestContextFilter优先级高
 * @author FrozenWatermelon
 */
@Component
@Order(-104)
public class LuckLocaleChangeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String newLocale = request.getHeader(Constant.LOCALE);
        if (newLocale == null) {
            newLocale = LanguageEnum.LANGUAGE_ZH_CN.getLanguage();
        }
        LocaleContextHolder.setLocale(new Locale(newLocale));
        Locale locale = LocaleContextHolder.getLocale();
        filterChain.doFilter(request, response);
    }
}
