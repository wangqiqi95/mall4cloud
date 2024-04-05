package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "commodity_pool")
public class CommodityPool implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long activityId;
    private Long commodityId;
    private Date beginTime;
    private Date endTime;
    private Integer activityChannel;
    /**
     * 门店id(-1全部门店/否则为具体门店id)
     */
    private Long storeId;
    /**
     * 0正常 1删除
     */
    private Integer delFlag;
}
