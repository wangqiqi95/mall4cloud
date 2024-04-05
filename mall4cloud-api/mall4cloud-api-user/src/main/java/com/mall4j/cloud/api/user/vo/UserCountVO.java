package com.mall4j.cloud.api.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 好友统计VO
 *
 */
@Data
public class UserCountVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long userId;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工")
    private String name;

    @ApiModelProperty("好友新增")
    private Integer addUser;

    @ApiModelProperty("好友流失")
    private Integer lossUser;

    @ApiModelProperty("好友删除")
    private Integer delUser;
    @ApiModelProperty("总好友数")
    private Integer countUser;

    @ApiModelProperty("每月新增")
    private Integer monthCount;

    @ApiModelProperty("月份")
    private String mon;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("0-未知 1-男性 2-女性")
    private String sex;

    @ApiModelProperty("性别人数")
    private Integer sexCount;

    @ApiModelProperty("留存率")
    private String retained;
    @ApiModelProperty("新增客户群")
    private String addRoom;
    @ApiModelProperty("总客户群")
    private String countRoom;
    @ApiModelProperty("新增入群人数")
    private String addRoomPeople;

    //外部联系人性别 0-未知 1-男性 2-女性
    private Integer gender;
}
