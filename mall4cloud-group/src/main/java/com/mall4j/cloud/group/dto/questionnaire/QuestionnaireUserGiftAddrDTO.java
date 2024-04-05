package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户奖品配送地址DTO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:11:00
 */
@Data
public class QuestionnaireUserGiftAddrDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("手机")
    private String mobile;

	@ApiModelProperty("物流公司")
	private String logisticsCompany;

	@ApiModelProperty("物流单号")
	private String logisticsNumber;
}
