package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description 优选联盟推广计划商品
 * @Author axin
 * @Date 2023-02-20 14:56
 **/
@Getter
@Setter
@TableName("channels_league_item_product")
public class LeagueItemProduct extends BaseModel {
    private Long id;

    /**
     * 商品id(视频号商品id)
     */
    private String outProductId;

    /**
     * 特殊推广计划id
     */
    private String infoId;

    private Long spuId;

    /**
     * spu编码
     */
    private String spuCode;

    /**
     * 商品分佣比例 0-90
     */
    private Integer ratio;

    /**
     * 状态 0启用 1禁用
     */
    private Integer status;

    /**
     * 类型1普通推广 2定向推广 3专属推广
     */
    private Integer type;

    /**
     * '推广开始时间'
     */
    private Date beginTime;

    /**
     * '推广结束时间'
     */
    private Date endTime;

    /**
     * 是否永久推广
     */
    private Boolean forerver;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 修改人
     */
    private String updatePerson;


}
