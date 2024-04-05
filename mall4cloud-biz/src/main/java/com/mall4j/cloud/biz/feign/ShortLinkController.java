package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.WXShortLinkDTO;
import com.mall4j.cloud.api.biz.feign.ShortLinkClient;
import com.mall4j.cloud.biz.model.WeixinShortlink;
import com.mall4j.cloud.biz.service.WeixinShortlinkService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链生成Feign实现类
 */
@RestController
@Slf4j
@AllArgsConstructor
public class ShortLinkController implements ShortLinkClient {

    private final MapperFacade mapperFacade;
    private final WeixinShortlinkService weixinShortlinkService;

    @Override
    public ServerResponseEntity<String> generateShortLink(WXShortLinkDTO dto) {
        WeixinShortlink weixinShortlink = mapperFacade.map(dto, WeixinShortlink.class);
        weixinShortlink.setId(null);
        return weixinShortlinkService.saveTo(weixinShortlink);
    }
}
