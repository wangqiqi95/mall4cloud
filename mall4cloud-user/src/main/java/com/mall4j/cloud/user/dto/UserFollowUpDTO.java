package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 填写跟进记录参数对象
 */
@Data
public class UserFollowUpDTO {
    @ApiModelProperty("好友关联关系记录id")
    private Long followUpId;
    @ApiModelProperty("好友关联关系记录id")
    private Long relationId;
    @ApiModelProperty("跟进记录文案")
    private String content;

    @ApiModelProperty("引用图片地址")
    private List<String> imgUrl;
    @ApiModelProperty("引用订单号")
    private String orderId;
    @ApiModelProperty("引用会话记录ids")
    private List<String> chatIds;
    @ApiModelProperty("引用员工ids")
    private List<String> staffIds;
    @ApiModelProperty("数据来源 0管理端 1导购端")
    private Integer origin;
    @ApiModelProperty("添加人名称")
    private String createName;

}
