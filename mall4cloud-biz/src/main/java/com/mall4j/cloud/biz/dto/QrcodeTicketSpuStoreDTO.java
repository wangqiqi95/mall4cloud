package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 二维码数据信息DTO
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
@Data
public class QrcodeTicketSpuStoreDTO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "小程序页面路径",required = true)
	private String path;
	private Integer width_y;
	@ApiModelProperty(value = "参数",required = true)
	private String scene;
	private Integer width;
	@ApiModelProperty(value = "门店id",required = true)
	private Long storeId;
	@ApiModelProperty(value = "门店编码",required = true)
	private String storeCode;
	@ApiModelProperty(value = "商品信息",required = true)
	private List<QrcodeTicketSpu> spuIds;

	private Long downLoadHisId;
}
