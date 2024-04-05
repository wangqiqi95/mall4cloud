package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "我的奖品返回实体")
public class DrawAwardVO implements Serializable {
    private Integer id;
    private String prizeName;
    private Integer lotteryDrawId;
    private String activityName;
    private Integer lotteryDrawPrizeId;
    private Date awardTime;
    private Integer awardType;
    private String mobile;
    private Integer status;
    private String userAddr;
    private String phone;
    private String logisticsNo;
    private String prizeImgs;
    private String company;
    private Long provinceId;;
    private Long cityId;
    private Long areaId;
    private String userName;
    private String addr;

    /**
     *   奖品规则
     */
    private String prizeRule;
}
