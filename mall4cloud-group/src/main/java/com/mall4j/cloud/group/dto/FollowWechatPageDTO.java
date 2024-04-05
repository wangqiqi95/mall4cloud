package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class FollowWechatPageDTO extends PageDTO implements Serializable {
    private String activityName;
    private Integer activityStatus;
    private String shopIds;
}
