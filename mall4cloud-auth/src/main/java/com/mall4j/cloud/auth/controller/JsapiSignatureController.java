package com.mall4j.cloud.auth.controller;

import com.mall4j.cloud.auth.config.WxConfig;
import com.mall4j.cloud.common.exception.LuckException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2021/7/31
 */
@RequestMapping("/wx/jsapi")
@Api(tags = "jsapi签名")
@RestController
public class JsapiSignatureController {

    @Autowired
    private WxConfig wxConfig;

    @PostMapping("/createJsapiSignature")
    @ApiOperation(value = "生成jsapi签名", notes = "根据url生成当前页面的jsapi签名")
    public ResponseEntity<WxJsapiSignature> createJsapiSignature(@RequestBody String url) {
        WxJsapiSignature jsapiSignature;
        try {
            jsapiSignature = wxConfig.getWxMpService().createJsapiSignature(url);
        } catch (WxErrorException e) {
            throw new LuckException(e.getError().toString());
        }
        return ResponseEntity.ok(jsapiSignature);
    }
}
