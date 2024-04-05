package com.mall4j.cloud.openapi.filter;

import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.api.openapi.bo.IPHInfoInTokenBO;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.common.handler.HttpHandler;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.manager.TokenStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class IphAuthFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(IphAuthFilter.class);
    @Autowired
    private HttpHandler httpHandler;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String accessToken = req.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = req.getParameter("token");
        }
        String requestURL = req.getRequestURL().toString();
        String requestURI = req.getRequestURI();
        if (StringUtils.isBlank(accessToken)) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }
        ServerResponseEntity<IPHInfoInTokenBO> userInfoByAccessToken = checkToken(accessToken);
        logger.info("IphAuthFilter校验token结束，requestURI:{},requestURL:{},CommonReqDto:{},accessToken:{},结果为:{}", requestURI, requestURL, getParams(request),
                    accessToken, userInfoByAccessToken);
        if (userInfoByAccessToken == null ? true : userInfoByAccessToken.isFail()) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }
        chain.doFilter(req, resp);
    }


    private ServerResponseEntity<IPHInfoInTokenBO> checkToken(String accessToken) {
        ServerResponseEntity<IPHInfoInTokenBO> userInfoByAccessToken = tokenStore.getUserInfoByAccessToken(accessToken, false);

        return userInfoByAccessToken;
    }

    private CommonReqDto getParams(ServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> paramMap = new HashMap<>();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                String key = stringEntry.getKey();
                String[] value = stringEntry.getValue();
                if (StringUtils.isNotBlank(key) && value != null && value.length > 0) {
                    paramMap.put(key, value[0]);
                }
            }
        }
        return BeanUtil.fillBeanWithMapIgnoreCase(paramMap, new CommonReqDto(), true);
    }

}
