package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 微信二维码表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
@Data
public class WeixinQrcodeCheckDTO {
    private static final long serialVersionUID = 1L;

	@NotEmpty
    @ApiModelProperty("")
    private String id;

	@NotEmpty
    @ApiModelProperty("状态： 0待审核 1审核通过")
    private Integer status;

}
