package com.mall4j.cloud.biz.vo.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 超时记录VO
 *
 */
@Data
public class TimeOutLogVO {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("员工姓名")
    private String staffName;
    @ApiModelProperty("员工id")
    private String staffId;
    @ApiModelProperty("客户姓名")
    private String userName;
    @ApiModelProperty("客户id")
    private String userId;
    /**
     * 记录状态，1表示超时，0表示最新记录
     */
    @ApiModelProperty("记录状态")
    private String status;
    @ApiModelProperty("超时触发时间")
    private Date triggerTime;
    private Date sendTime;
    @ApiModelProperty("累计次数")
    private Integer sumCount;
    @ApiModelProperty("新增次数")
    private Integer addCount;
    @ApiModelProperty("触发人数")
    private Integer countPeople;
    @ApiModelProperty("日期")
    private String dayTime;
    private String roomId;
    private Long timeOutId;
    private String ruleName;
}
