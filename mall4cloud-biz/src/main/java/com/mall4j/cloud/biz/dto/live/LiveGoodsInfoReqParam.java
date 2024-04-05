package com.mall4j.cloud.biz.dto.live;

import com.mall4j.cloud.biz.dto.live.WxInterfaceInfo;
import lombok.Data;

@Data
public class LiveGoodsInfoReqParam extends WxInterfaceInfo {

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
        return "/add?access_token=";
    }

}
