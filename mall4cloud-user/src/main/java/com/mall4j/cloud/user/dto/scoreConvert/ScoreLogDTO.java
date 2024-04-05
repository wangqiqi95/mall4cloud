package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 积分兑换
 *
 * @author shijing
 */

@Data
@ApiModel(description = "积分换物兑换列表参数")
public class ScoreLogDTO {

    @ApiModelProperty(value = "记录id",required = true)
    private Long id;
    @ApiModelProperty(value = "发货物流公司",required = true)
    private String company;
    @ApiModelProperty(value = "发货物流单号",required = true)
    private String logisticsNo;

}
