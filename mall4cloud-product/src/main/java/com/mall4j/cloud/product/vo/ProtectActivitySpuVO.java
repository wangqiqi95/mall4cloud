package com.mall4j.cloud.product.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VO
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
@Data
public class ProtectActivitySpuVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("商品货号")
    private String spuCode;

	@ApiModelProperty("spu名称")
	private String spuName;

	@ApiModelProperty("商品介绍主图")
	private String mainImgUrl;

	@ApiModelProperty("商品介绍主图 多个图片逗号分隔")
	private String imgUrls;

    @ApiModelProperty("电商保护价")
    private String protectPrice;

    @ApiModelProperty("0生效 1失效")
    private Integer status;
    private String statusStr;

    @ApiModelProperty("执行状态：0待审核 1待执行 2进行中 3已结束 4驳回")
    private Integer exStatus;
    private String exStatusStr;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

}
