package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * @Description 优选联盟达人
 * @Author axin
 * @Date 2023-02-14 18:22
 **/
@Data
@TableName("channels_league_promoter")
public class LeaguePromoter extends BaseModel {
    private Long id;

    /**
     * 达人id
     */
    private String finderId;

    /**
     * 达人名称
     */
    private String finderName;

    /**
     * 门店id
     */
    private Long storeId;
}
