package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "app-签到信息实体")
public class SignActivityAppVO implements Serializable {
    private Integer id;
    private Integer signNoticeSwitch;
    private String activityName;
    private String activityRule;
    private Date activityBeginTime;
    private Date activityEndTime;
    private Integer signDay;
    private Integer maxGetTimes;
    private Integer seriesSignSwitch;
    private List<Date> signDates;
    private List<SignRewardVO> rewards;
}
