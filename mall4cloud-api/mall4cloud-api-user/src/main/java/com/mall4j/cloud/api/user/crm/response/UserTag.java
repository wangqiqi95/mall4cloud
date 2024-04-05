package com.mall4j.cloud.api.user.crm.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserTag {
    //标签Id
    @ApiModelProperty(value = "标签tagId",required = false)
    private String tagId;
    //标签名称
    @ApiModelProperty(value = "name",required = false)
    private String name;
    //多值标签值
    @ApiModelProperty(value = "tagValues",required = false)
    private List<String> tagValues;
    //打标去标标识位,true 打标，false 去标
    @ApiModelProperty(value = "打标去标标识位,true 打标，false 去标",required = false)
    private boolean mark = true;
    /**
     * 多值标签的处理方式
     * APPEND-新增, REPLACE-替换, REMOVE-移除一个, CLEAR-全部清除
     */
    @ApiModelProperty(value = "多值标签的处理方式: APPEND-新增, REPLACE-替换, REMOVE-移除一个, CLEAR-全部清除",required = false)
    private String operateTagType;

}
