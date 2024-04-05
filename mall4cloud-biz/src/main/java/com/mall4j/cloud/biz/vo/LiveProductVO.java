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
@ApiModel("直播商品")
public class LiveProductVO extends BaseVO{

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品主图")
    private String image;

    @ApiModelProperty("商品名称")
    private String name;

	@ApiModelProperty("库存总量")
	private int count;

	@ApiModelProperty("审核状态 0:初始值 1:编辑中 2:审核中 3:审核失败 4:审核成功")
	private String status;

	@ApiModelProperty("拒绝理由")
	private String rejectReason;

	@ApiModelProperty("当前商品上下架状态 0-初始值 5-上架 11-自主下架 13-违规下架/风控系统下架")
	private int onsale;

	@ApiModelProperty("店铺编号")
	private String shopCode;

	@ApiModelProperty("店铺编号")
	private String shopName;
}
