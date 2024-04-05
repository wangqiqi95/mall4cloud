package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
@TableName(value = "lottery_draw_activity_stock_log")
public class LotteryDrawActivityStockLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("活动id")
    private Integer lotteryDrawId;
    @ApiModelProperty("奖品名称")
    private String prizeName;
    @ApiModelProperty("操作类型 1增加 2减少")
    private Integer optType;
    @ApiModelProperty("操作数量")
    private Integer optNum;
    @ApiModelProperty("操作人id")
    private Long optUserId;
    @ApiModelProperty("操作人名称")
    private String optUserName;
    @ApiModelProperty("操作时间")
    private Date optTime;
}
