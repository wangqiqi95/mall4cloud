package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.PromoterResp;
import com.mall4j.cloud.api.biz.feign.ChannelsPromoterFeignClient;
import com.mall4j.cloud.biz.service.channels.LeaguePromoterService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 视频号达人
 * @Author axin
 * @Date 2023-05-06 13:45
 **/
@RestController
public class ChannelsPromoterFeignController implements ChannelsPromoterFeignClient {

    @Autowired
    private LeaguePromoterService leaguePromoterService;

    @Override
    public ServerResponseEntity<PromoterResp> getPromoterByFinderId(String finderId) {
        return ServerResponseEntity.success(leaguePromoterService.getPromoterByFinderId(finderId));
    }
}
