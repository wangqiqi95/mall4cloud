package com.mall4j.cloud.biz.dto.live;

import com.mall4j.cloud.biz.dto.live.WxInterfaceInfo;
import lombok.Data;

/**
 * 修改直播间商品请求参数
 * @author yami
 */
@Data
public class UpdateLiveGoodsInfoReqParam extends WxInterfaceInfo {

    private String coverImgUrl;

    private String name;

    private Integer priceType;

    private Double price;

    private Double price2;

    private String url;

    private String requestUrl;

    /**
     *  微信商品id
     */
    private Long goodsId;

    @Override
    public String getRequestUrl() {
        return "/wxaapi/broadcast/goods/update?access_token=";
    }

}
