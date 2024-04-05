package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class LotteryAwardRecordListDTO extends PageDTO implements Serializable {
    @ApiModelProperty("活动id")
    private Integer id;
    @ApiModelProperty("是否中奖记录 0 未中奖记录 1中奖记录")
    private Integer awardFlag;
    @ApiModelProperty("奖品id")
    private Integer prizeId;
    @ApiModelProperty("用户信息")
    private String userInfo;
    @ApiModelProperty("活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date awardBeginTime;
    @ApiModelProperty("活动截止时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date awardEndTime;
}
