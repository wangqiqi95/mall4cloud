package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/13
 */
@Data
public class GuideShareRecordStatistics {

    @ApiModelProperty("全部导购数量")
    private Integer totalStaffNum;

    @ApiModelProperty("有分享导购数量")
    private Integer shareStaffNum;

    @ApiModelProperty("无分享导购数量")
    private Integer notShareStaffNum;

}
