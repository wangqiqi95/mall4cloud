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
@TableName(value = "lottery_draw_activity_share")
public class LotteryDrawActivityShare implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("lottery_draw_id")
    private Integer lotteryDrawId;


    @ApiModelProperty("user_id")
    private Long userId;


    @ApiModelProperty("share_time")
    private Date shareTime;
}
