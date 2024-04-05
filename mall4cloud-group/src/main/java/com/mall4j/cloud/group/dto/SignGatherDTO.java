package com.mall4j.cloud.group.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SignGatherDTO implements Serializable {
    private Date activityBeginTime;
    private Date activityEndTime;
    private Integer activityId;
}
