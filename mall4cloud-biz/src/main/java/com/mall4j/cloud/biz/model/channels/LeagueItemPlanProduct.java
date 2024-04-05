package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * @Description 优选联盟推广计划商品
 * @Author axin
 * @Date 2023-02-20 14:56
 **/
@Data
@TableName("channels_league_item_plan_product")
public class LeagueItemPlanProduct extends BaseModel {
    private Long id;

    /**
     * 推广计划id
     */
    private Long planId;

    /**
     * 商品id(视频号商品id)
     */
    private String productId;

    /**
     * 商品分佣比例 0-90
     */
    private Integer ratio;

    /**
     * 状态 0启用 1禁用
     */
    private Integer status;

    /**
     * 特殊推广计划id
     */
    private String infoId;

    /**
     * 微信回传状态码
     */
    private Integer errCode;

    /**
     * 微信回传状态信息
     */
    private String errMsg;

}
