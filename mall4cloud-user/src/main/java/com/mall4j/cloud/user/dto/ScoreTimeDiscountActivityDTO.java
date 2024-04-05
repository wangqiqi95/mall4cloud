package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 积分限时折扣DTO
 *
 * @author gmq
 * @date 2022-07-11 15:11:39
 */
@Data
public class ScoreTimeDiscountActivityDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("活动名称")
    private String title;

    @ApiModelProperty("周期类型：0-单周 1-每周 2-每月")
    private Integer timeType;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("开始时间")
    private String startTimeStr;

    @ApiModelProperty("结束时间")
    private String endTimeStr;

    @ApiModelProperty("按月：开始天")
    private Integer startDay;

    @ApiModelProperty("按月：结束天")
    private Integer endDay;

    @ApiModelProperty("周(多个用英文逗号分隔开:1,2,3,4,5,6,7)")
    private String weeks;

    @ApiModelProperty("状态 0 未启用 1已启用")
    private Integer status;

    @ApiModelProperty("是否保存且启用：true false")
    private boolean btnSaveOen;

	@ApiModelProperty("兑换券")
	private List<ScoreTimeDiscountActivityItemDTO> itemDTOS;

}
