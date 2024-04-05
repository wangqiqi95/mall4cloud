package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MaterialBrowsePageDTO {

    @ApiModelProperty("id")
    @NotNull(message = "素材名称不能为空")
    private Long  id;

    @ApiModelProperty("开始时间")
    private String  createTimeStart;

    @ApiModelProperty("结束时间")
    private String  createTimeEnd;

    @ApiModelProperty("客户昵称")
    private String  nickName;
    @ApiModelProperty("手机号")
    private String  phone;
    @ApiModelProperty("标签")
    private String  lable;

    @ApiModelProperty("标签ID集合")
    private List<String> labalIds;
}
