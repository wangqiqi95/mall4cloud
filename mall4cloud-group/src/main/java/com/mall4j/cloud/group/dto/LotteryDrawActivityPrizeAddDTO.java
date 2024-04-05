package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "抽奖活动奖品配置信息添加实体")
public class LotteryDrawActivityPrizeAddDTO implements Serializable {
    @ApiModelProperty("抽奖活动id")
    private Integer lotteryDrawId;

    @ApiModelProperty("中奖率 单位千分之")
    private Integer winningRate;


    @ApiModelProperty("奖品发放总量 1 按日限制发送总量 2 按累计总量限制发送总量")
    private Integer prizeGrantTotal;

    @ApiModelProperty("奖品发放总量")
    private Integer prizeTotal;


    @ApiModelProperty("发放对象 0 所有人都可使用 1 按照会员等级设置奖品")
    private Integer grantTarget;

    private List<LotteryDrawActivityPrizeDTO> prizes;
}
