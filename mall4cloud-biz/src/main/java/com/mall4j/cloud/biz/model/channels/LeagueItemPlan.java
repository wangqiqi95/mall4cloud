package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * @Description 优选联盟推广计划
 * @Author axin
 * @Date 2023-02-20 14:55
 **/
@Data
@TableName("channels_league_item_plan")
public class LeagueItemPlan extends BaseModel {
    private Long id;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 推广计划名称
     */
    private String name;

    /**
     * 推广类别 1普通推广 2定向推广 3专属推广
     */
    private Integer type;

    /**
     * 推广开始时间
     */
    private Date beginTime;

    /**
     * 推广结束时间
     */
    private Date endTime;

    /**
     * 是否永久推广 0否 1是
     */
    private Boolean isForerver;
}
