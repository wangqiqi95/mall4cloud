package com.mall4j.cloud.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2022/01/08
 */
@Data
public class UserQueryDTO {

    @ApiModelProperty("搜索关键字")
    private String keywords;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("服务门店ID")
    private Long serviceStoreId;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("用户ID集合")
    private List<Long> userIds;

    @ApiModelProperty("用户ID集合")
    private List<String> vipcodes;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("统计类型 1日 2周 3月")
    private Integer countType;

    @ApiModelProperty("是否绑定导购")
    private boolean unBindStaff;

    @ApiModelProperty("性别 1-男 0-女")
    private Integer sex;

    @ApiModelProperty("等级条件 0-普通会员 1-付费会员")
    private Integer levelType;

    @ApiModelProperty("会员等级")
    private Integer level;

    @ApiModelProperty("x天回访过")
    private Integer visitDay;

    @ApiModelProperty("x天消费过")
    private Integer consumeDay;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("生日开始时间")
    private Date birthStartDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("生日结束时间")
    private Date birthEndDate;

    private Integer birthDateFlag;


}
