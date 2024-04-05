package com.mall4j.cloud.user.dto.scoreConvert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.License;
import lombok.Data;

import java.util.List;

/**
 * 删除禁用人员参数
 *
 * @author shijing
 */

@Data
@ApiModel(description = "删除禁用人员参数")
public class DeletePhoneDTO {

    @ApiModelProperty(value = "关联活动")
    private Long convertId;
    @ApiModelProperty(value = "手机号")
    private List<String> phoneNums;

}
