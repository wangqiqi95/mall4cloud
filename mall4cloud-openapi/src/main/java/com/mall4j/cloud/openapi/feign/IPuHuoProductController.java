package com.mall4j.cloud.openapi.feign;

import com.mall4j.cloud.api.openapi.feign.IPuHuoFeignClient;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.openapi.config.IphParams;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：对接爱铺货 商品操作
 */
@RestController
public class IPuHuoProductController implements IPuHuoFeignClient {

    @Autowired
    HttpServletRequest request;

    /**
     * 爱铺货 appsecret
     */
    @Value("${auth.token.iph.appsecret}")
    public String iphAppsecret;


    @Override
    public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto) {
        commonReqDto.setIphAppsecret(iphAppsecret);
        return IPuHuoProductHandle.getInstance().productAll(commonReqDto, request);
    }


}
