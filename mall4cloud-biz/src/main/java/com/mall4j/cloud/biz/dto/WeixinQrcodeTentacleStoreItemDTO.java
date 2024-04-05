package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信触点门店列表详情表DTO
 *
 * @author tan
 * @date 2022-12-08 16:05:09
 */
@Data
public class WeixinQrcodeTentacleStoreItemDTO extends PageDTO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("触点列表ID(主表ID,跟子表ID只能二选一)")
	private String tentacleId;

	@ApiModelProperty("触点门店列表ID(子表ID,跟主表ID只能二选一)")
	private String tentacleStoreId;

	@ApiModelProperty("会员编号")
	private String vipCode;

	@ApiModelProperty("查看开始时间")
	private Date startTime;

	@ApiModelProperty("查看结束时间")
	private Date endTime;

}
