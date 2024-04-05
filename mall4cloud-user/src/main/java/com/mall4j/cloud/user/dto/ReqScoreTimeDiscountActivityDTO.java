package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
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
public class ReqScoreTimeDiscountActivityDTO extends PageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动名称")
    private String title;

    @ApiModelProperty("状态 0 未启用 1已启用")
    private Integer status;

    @ApiModelProperty("积分兑换券活动id")
    private Long convertId;


}
