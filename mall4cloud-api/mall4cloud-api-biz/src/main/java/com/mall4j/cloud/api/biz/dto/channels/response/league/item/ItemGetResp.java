package com.mall4j.cloud.api.biz.dto.channels.response.league.item;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * @Description 联盟商品详情
 * @Author axin
 * @Date 2023-02-13 15:18
 **/
@Data
public class ItemGetResp extends BaseResponse {
    /**
     * 推广商品信息
     */
    private Item item;
}
