package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.mall4j.cloud.api.openapi.utils.MD5Util;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 类描述：对接公共请求参数
 */
public class CommonReqDto implements Serializable {

    private static final long serialVersionUID = 4777951676848788227L;
    /**
     * 不同功能的接口需要传的method的值不同
     */
    @ApiModelProperty(value = "方法名称", required = true)
    private String method;

    /**
     * 商城给爱铺货的
     */
    @ApiModelProperty(value = "应用编号", required = true)
    private String appKey;

    /**
     * 用户店铺访问令牌
     */
    @ApiModelProperty(value = "访问令牌", required = true)
    private String token;

    /**
     * 各接口请求参数以json格式合并
     */
    @ApiModelProperty(value = "私有请求参数json格式串", required = true)
    private String bizcontent;

    /**
     * 商户请求参数的签名串
     */
    @ApiModelProperty(value = "签名串", required = true)
    private String sign;

    private String iphAppsecret;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBizcontent() {
        return bizcontent;
    }

    public void setBizcontent(String bizcontent) {
        this.bizcontent = bizcontent;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setIphAppsecret(String iphAppsecret) {
        this.iphAppsecret = iphAppsecret;
    }

    @Override
    public String toString() {
        return "CommonReqDto{" + "method='" + method + '\'' + ", appKey='" + appKey + '\'' + ", token='" + token + '\'' + ", bizcontent='" + bizcontent + '\''
                + ", sign='" + sign + '\'' + '}';
    }


    public String buildBizContent() {
        PropertyPreFilters filter = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filter.addFilter();
        String[] excludeProperties = { "method", "appKey", "token", "bizcontent", "sign", "iphAppsecret" };
        excludefilter.addExcludes(excludeProperties);
        return JSONObject.toJSONString(this, excludefilter);
    }

    public boolean verifySign() {
        if (StringUtils.isNotBlank(this.getSign())) {
            StringBuilder builder = new StringBuilder();
            builder.append(iphAppsecret);
            builder.append("appkey").append(this.getAppKey()).append("bizcontent").append(this.getBizcontent()).append("method").append(this.getMethod()).append("token").append(this.getToken());
            builder.append(iphAppsecret);
            String encode = MD5Util.encode(builder.toString().toLowerCase()).toLowerCase();
            if (this.getSign().equals(encode)) {
                return true;
            }
        }
        return true;
    }

    public String getIphAppsecret() {
        return iphAppsecret;
    }
}
