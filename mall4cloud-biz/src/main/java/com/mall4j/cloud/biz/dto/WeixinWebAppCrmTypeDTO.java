package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 微信公众号表DTO
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 10:13:40
 */
@Data
public class WeixinWebAppCrmTypeDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("")
    private String id;

    @NotNull
    @ApiModelProperty("crm: 公众号类型 0成人公众号 1儿童公众号 2lifestyle公众号 3sport公众号")
    private Integer crmType;
}
