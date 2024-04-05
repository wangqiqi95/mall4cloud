package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VO
 *
 * @author lt
 * @date 2022-01-17
 */
@Data
@ApiModel("直播类目")
public class LiveCategoryVO extends BaseVO{

    @ApiModelProperty("类目ID")
    private Long categoryId;

    @ApiModelProperty("类目名称")
    private String categoryName;

	@ApiModelProperty("拒绝理由")
	private String rejectReason;

	@ApiModelProperty("审核状态 1:已通过,9:拒绝")
	private int status;

}
