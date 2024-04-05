package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 微信二维码表VO
 *
 * @author gmq
 * @date 2022-01-28 22:25:17
 */
@Data
public class WeixinQrcodePutDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @NotEmpty
    @ApiModelProperty("作业名称")
    private String actionInfo;

    @NotEmpty
    @ApiModelProperty("触点公众号")
    private String appId;

//    @NotEmpty
    @ApiModelProperty("触点门店(多个用英文逗号,分隔开)")
    private String storeId;

    @NotEmpty
    @ApiModelProperty("二维码中间logo")
    private String logoUrl;

    @NotNull
    @ApiModelProperty("有效期类型： 1永久 2临时")
    private Integer isExpire;

    @NotNull
    @ApiModelProperty("触点类型：0员工/1自定义")
    private Integer contactType;

    @ApiModelProperty("自定义触点内容")
    private String contactStr;

}
