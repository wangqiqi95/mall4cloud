package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 自动拉群活码表DTO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpAutoGroupCodeSelectDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活码名称",required = false)
    private String codeName;

    @ApiModelProperty(value = "通过好友：0 自动通过， 1 手动通过",required = false)
    private Integer authType;

    @ApiModelProperty(value = "拉群方式：0企微群活码/1自建群活码",required = false)
    private Integer groupType;

    @ApiModelProperty(value = "分组id",required = false)
    private List<Long> groupIds;

    @ApiModelProperty(value = "接待人员",required = false)
    private List<Long> staffs;

    @ApiModelProperty("ids")
    private Long[] ids;



}
