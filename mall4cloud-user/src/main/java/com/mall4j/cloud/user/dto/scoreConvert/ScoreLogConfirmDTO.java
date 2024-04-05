package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 积分兑换
 *
 * @author shijing
 */

@Data
@ApiModel(description = "积分换物导入确认参数")
public class ScoreLogConfirmDTO {

    @ApiModelProperty(value = "记录id数组",required = true)
    private List<Long> ids;


}
