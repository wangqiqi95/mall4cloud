package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *  客户加入客群对象
 */
@Data
public class UserJoinCustGroupVO {

    @ApiModelProperty("邀请者的名称")
    private String invitorUserName;
    @ApiModelProperty("邀请者的UserId")
    private String invitorUserId;
    @ApiModelProperty("群的名称")
    private String groupName;
    @ApiModelProperty("1 - 由群成员邀请入群（直接邀请入群）2 - 由群成员邀请入群（通过邀请链接入群）3 - 通过扫描群二维码入群")
    private Integer joinScene;

    @ApiModelProperty("入群时间")
    private Date createTime;
    @ApiModelProperty("最近联系时间")
    private Date lastContactTime;

    @ApiModelProperty("群id")
    private String chatId;
}
