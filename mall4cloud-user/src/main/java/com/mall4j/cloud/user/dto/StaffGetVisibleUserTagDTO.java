package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Peter_Tan
 * @date 2023/02/13
 */
@Data
public class StaffGetVisibleUserTagDTO {

    @ApiModelProperty("会员编号")
    private String vipCode;

}
