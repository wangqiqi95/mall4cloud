package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTagOperationVO {


    @ApiModelProperty(value = "主键")
    private Long userTagOperationId;

    @ApiModelProperty(value = "操作类型，1新增，2删除")
    private Integer operationState;

    @ApiModelProperty(value = "操作人")
    private Long doUser;

    @ApiModelProperty(value = "被操作人")
    private String beVipCode;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签ID")
    private String tagName;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

    @ApiModelProperty(value = "标签组ID")
    private String groupName;

    @ApiModelProperty(value = "标签组分类ID")
    private Long parentGroupId;

    @ApiModelProperty(value = "标签组分类")
    private String parentGroupName;


}
