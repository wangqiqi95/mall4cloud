package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserJourneysDTO {

    @ApiModelProperty("好友关联关系记录id")
    private Long relationId;

    @ApiModelProperty("查询数据类型 查询全部时此集合不需要传 1电话 2邮件 3企微会话 4短信 5跟进记录 6美洽 7修改跟进 8行为轨迹 ")
    private List<Integer> type;

    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    private String unionId;
}
