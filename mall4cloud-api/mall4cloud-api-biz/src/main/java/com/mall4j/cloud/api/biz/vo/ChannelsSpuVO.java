package com.mall4j.cloud.api.biz.vo;

import lombok.Data;

/**
 * @date 2023/3/15
 */
@Data
public class ChannelsSpuVO {
    private Long spuId;

    private String spuCode;

    private Long outSpuId;

    private Long brandId;

    private String title;

    private String subTitle;

    private String headImgs;

    private Integer deliverMethod;

    private String freightTemplate;

    private String cats;

    private Integer status;

    private Integer editStatus;

    private Integer inWinodws;
}
