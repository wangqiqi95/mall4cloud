package com.mall4j.cloud.group.vo;

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
@ApiModel(value = "连签天数人数对应实体")
public class SignGatherSeriesVO implements Serializable {
    private Integer seriesDay;
    private Integer seriesUserNum;
}
