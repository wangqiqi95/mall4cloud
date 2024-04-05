package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "app-连签奖励实体")
public class SignRewardVO implements Serializable {
    private Integer id;
    private Integer seriesSignDay;
    private String rewardName;
    private String rewardPicUrl;
    private Integer curGetTimes;
    private Boolean flag;
}
