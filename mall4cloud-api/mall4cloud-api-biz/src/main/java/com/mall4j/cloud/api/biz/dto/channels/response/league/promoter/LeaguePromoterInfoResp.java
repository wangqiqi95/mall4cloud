package com.mall4j.cloud.api.biz.dto.channels.response.league.promoter;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * @Description 优选联盟达人信息详情
 * @Author axin
 * @Date 2023-02-13 15:03
 **/
@Data
public class LeaguePromoterInfoResp extends BaseResponse {
    private PromoterInfo promoter;
}
