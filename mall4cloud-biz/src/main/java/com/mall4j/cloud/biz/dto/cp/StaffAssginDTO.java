package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StaffAssginDTO extends StaffAssginGroupDTO{

    @ApiModelProperty("替换的客户企业微信id")
    private List<String> custIds;

    @ApiModelProperty("客户分配类型  0 按客户导购分配   1指定客户")
    @NotNull(message = "客户分配类型不能为空")
    private Integer type;

    @ApiModelProperty("消息 ")
    @NotNull(message = "消息不能为空")
    private String  msg;

}
