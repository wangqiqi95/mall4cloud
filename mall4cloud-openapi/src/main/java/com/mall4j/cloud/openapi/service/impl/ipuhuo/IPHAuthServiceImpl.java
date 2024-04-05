package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.openapi.bo.IPHCodeInfoBo;
import com.mall4j.cloud.api.openapi.bo.IPHInfoInTokenBO;
import com.mall4j.cloud.api.openapi.bo.TokenInfoBO;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GetAccessTokenDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GetCodeDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.RefreshTokenDto;
import com.mall4j.cloud.api.openapi.vo.TokenInfoVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.manager.TokenStore;
import com.mall4j.cloud.openapi.service.impl.IPHAuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service("iPhAuthService")
@RefreshScope
public class IPHAuthServiceImpl implements IPHAuthService {
    private final Logger logger = LoggerFactory.getLogger(IPHAuthServiceImpl.class);

    /**
     * 爱铺货 appkey
     */
    @Value("${auth.token.iph.appkey}")
    public String iphAppkey;

    /**
     * 爱铺货 appsecret
     */
    @Value("${auth.token.iph.appsecret}")
    public String iphAppsecret;

    /**
     * 是否后台跳转
     */
    @Value("${auth.token.iph.redirect:false}")
    public boolean redirect;

    @Autowired
    TokenStore tokenStore;

    @Override
    public ServerResponseEntity<String> code(GetCodeDto getCodeDto) {
        long start = System.currentTimeMillis();
        String code = tokenStore.storeCode(new IPHCodeInfoBo(getCodeDto.getState(), getCodeDto.getRedirecturi(), getCodeDto.getAppkey()));
        String state = getCodeDto.getState();
        String redirecturi = getCodeDto.getRedirecturi();
        boolean result = true;
        try {
            if (redirect) {
                if (StringUtils.isBlank(redirecturi)) {
                    return ServerResponseEntity.showFailMsg("redirecturi不能为空");
                }
                HttpUtil.post(new StringBuilder(redirecturi).append("?state=").append(state).append("&code=").append(code).toString(), "", 60);
            }
        } catch (HttpException e) {
            result = false;
        } catch (Exception e) {
            logger.error("进行授权回调异常", e);
            result = false;
        } finally {
            logger.info("爱铺货-获取Code请求结束，请求参数为：{}，生成code为：{}，执行结果为：{},共耗时:{}", getCodeDto, code, result, System.currentTimeMillis() - start);
        }
        if (!result) {
            code = tokenStore.storeCode(new IPHCodeInfoBo(getCodeDto.getState(), getCodeDto.getRedirecturi(), getCodeDto.getAppkey()));
        }
        return ServerResponseEntity.success(code);
    }

    @Override
    public JSONObject accessToken(GetAccessTokenDto getAccessTokenDto) {
        long start = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        String code = getAccessTokenDto.getCode();
        String appkey = getAccessTokenDto.getAppkey();
        String appsecret = getAccessTokenDto.getAppsecret();
        String redirecturi = getAccessTokenDto.getRedirecturi();

        try {
            if (!iphAppkey.equals(appkey)) {
                result.put("code", 40000);
                result.put("message", " appkey不正确");
                return result;
            }
            if (!iphAppsecret.equals(appsecret)) {
                result.put("code", 40000);
                result.put("message", "appsecret不正确");
                return result;
            }

            String infoByCode = tokenStore.getInfoByCode(getAccessTokenDto.getAppkey());
            if (StringUtils.isBlank(infoByCode)) {
                result.put("code", 40000);
                result.put("message", " code已过期或未授权");
                return result;
            }
            int colonIndex = infoByCode.indexOf(StrUtil.COLON);

            String realCode = "";
            String realRedirecturi = "";
            if (colonIndex != -1) {
                realCode = infoByCode.substring(0, colonIndex);
                realRedirecturi = infoByCode.substring(colonIndex + 1);
            }
            if (!code.equals(realCode)) {
                result.put("code", 40000);
                result.put("message", " code已过期或未授权");
                return result;
            }
            if (!redirecturi.equals(realRedirecturi)) {
                result.put("code", 40000);
                result.put("message", "redirecturi与获取 Authorization Code 时传递的 redirecturi不一致");
                return result;
            }
            TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(new IPHInfoInTokenBO());
            result.put("token", tokenInfoVO.getAccessToken());
            result.put("expiresin", tokenInfoVO.getExpiresIn());
            result.put("refreshtoken", tokenInfoVO.getRefreshToken());
        } finally {
            logger.info("爱铺货-调用Access Token结束，请求参数为:{},响应为：{}，共耗时：{}毫秒", getAccessTokenDto, result, System.currentTimeMillis() - start);
        }
        return result;
    }

    @Override
    public JSONObject refreshToken(RefreshTokenDto refreshTokenDto) {
        long start = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        String appkey = refreshTokenDto.getAppkey();
        String appsecret = refreshTokenDto.getAppsecret();

        try {
            if (!iphAppkey.equals(appkey)) {
                result.put("code", 40000);
                result.put("message", " appkey不正确");
                return result;
            }

            if (!iphAppsecret.equals(appsecret)) {
                result.put("code", 40000);
                result.put("message", "appsecret不正确");
                return result;
            }
            ServerResponseEntity<TokenInfoBO> tokenInfoBOServerResponseEntity = tokenStore.refreshToken(refreshTokenDto.getRefreshtoken(), false);
            if (!tokenInfoBOServerResponseEntity.isSuccess()) {
                result.put("code", 40000);
                result.put("message", "获取refresh失败");
            }
            TokenInfoBO tokenInfoBO = tokenInfoBOServerResponseEntity.getData();
            result.put("token", tokenInfoBO.getAccessToken());
            result.put("expiresin", tokenInfoBO.getExpiresIn());
            result.put("refreshtoken", tokenInfoBO.getRefreshToken());
        } finally {
            logger.info("爱铺货-调用Refresh Token结束，请求参数为:{},响应为：{}，共耗时：{}毫秒", refreshTokenDto, refreshTokenDto, System.currentTimeMillis() - start);
        }
        return result;
    }
}
