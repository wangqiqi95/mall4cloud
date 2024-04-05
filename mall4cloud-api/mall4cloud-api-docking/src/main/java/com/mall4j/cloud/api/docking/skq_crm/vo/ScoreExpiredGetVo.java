package com.mall4j.cloud.api.docking.skq_crm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ty
 * @ClassName ScoreExpiredGetVo
 * @description
 * @date 2022/10/27 11:41
 */
@Data
public class ScoreExpiredGetVo implements Serializable {

    @ApiModelProperty(value = "会员卡号")
    private String vipCode;

    @ApiModelProperty(value = "年度即将过期积分值")
    private Long pointValue;

}
