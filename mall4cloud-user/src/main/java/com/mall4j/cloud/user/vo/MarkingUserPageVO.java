package com.mall4j.cloud.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MarkingUserPageVO {

    @ApiModelProperty(value = "主键")
    private Long userTagRelationId;

    @ApiModelProperty(value = "标签组分类名")
    private String parentGroupName;

    @ApiModelProperty(value = "标签组ID")
    private Long groupId;

    @ApiModelProperty(value = "标签组名")
    private String groupName;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty(value = "标签与标签组关联ID")
    private Long groupTagRelationId;

    @ApiModelProperty(value = "会员")
    private String vipCode;

    @ApiModelProperty(value = "创建人（打标人）")
    private Long createUser;

    @ApiModelProperty(value = "会员ID")
    private Long vipUserId;

    @ApiModelProperty(value = "会员昵称")
    private String vipNickName;

    @ApiModelProperty(value = "会员昵称")
    private String vipPhone;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "导购编号")
    private String staffNo;

    @ApiModelProperty(value = "导购昵称")
    private String staffNickName;

    @ApiModelProperty(value = "服务门店ID")
    private Long serviceStoreId;

    @ApiModelProperty(value = "服务门店名称")
    private String serviceStoreName;

    @ApiModelProperty(value = "服务门店编号")
    private String serviceStoreCode;

    @ApiModelProperty(value = "导购企微ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "客户企微ID")
    private String vipCpUserId;

}
