package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 素材 互动雷达标签DTO
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
@Data
public class CpMaterialLableDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("浏览多少秒开始统计")
    private Integer interactionSecond;

    @ApiModelProperty("互动达标记录标签id")
    private String radarLabalId;

    /**
     * TODO 多标签处理
     * [{\"tagId\":\"20231116093837293125460740000001\",\"tagName\":\"test\"},{\"tagId\":\"20231116093903227125460740000002\",\"tagName\":\"test2\",\"tagValues\":[\"枚举1\"]}]
     */
    @ApiModelProperty("互动达标记录标签name")
    private String radarLabalName;

    @ApiModelProperty("标签值 多值的话用逗号隔开")
    private String radarLableValue;

}
