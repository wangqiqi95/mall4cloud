package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 禁用人员列表参数
 *
 * @author shijing
 */

@Data
@ApiModel(description = "禁用人员列表参数")
public class PhoneNumListDTO {

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;
    @ApiModelProperty(value = "关联活动")
    private Long convertId;
    @ApiModelProperty(value = "手机号")
    private String phoneNum;

}
