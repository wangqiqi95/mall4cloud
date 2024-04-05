package com.mall4j.cloud.api.group.feign.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenCommodityDTO {

    private List<String> activityChannels;

    private Long storeId;


}
