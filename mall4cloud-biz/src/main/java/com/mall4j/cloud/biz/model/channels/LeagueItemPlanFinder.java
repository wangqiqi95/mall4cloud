package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * @Description 优选联盟推广计划达人
 * @Author axin
 * @Date 2023-02-20 14:55
 **/
@Data
@TableName("channels_league_item_plan_finder")
public class LeagueItemPlanFinder extends BaseModel {
    private Long id;

    /**
     * 计划id
     */
    private Long planId;

    /**
     * 达人id
     */
    private String finderId;
}
