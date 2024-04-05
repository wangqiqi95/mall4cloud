package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 微信触点二维码表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
@Data
public class WeixinQrcodeTentacleDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("触点链接")
    private String tentaclePath;

    @ApiModelProperty("接收邮箱")
    private String recevieEmail;

    @NotEmpty
	@ApiModelProperty("门店(多个英文,逗号分隔开)")
	private String storeIds;

    private Long downLoadHisId;
}
