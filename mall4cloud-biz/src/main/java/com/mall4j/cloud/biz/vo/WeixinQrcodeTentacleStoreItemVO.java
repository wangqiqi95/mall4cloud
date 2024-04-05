package com.mall4j.cloud.biz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 查询触点门店列表详情参数
 */
@Data
public class WeixinQrcodeTentacleStoreItemVO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;

	@ApiModelProperty("查看触点记录表ID")
	private String tentacleStoreId;

	@ApiModelProperty("触点名称")
	private String tentacleName;

	@ApiModelProperty("触点链接")
	private String qrcodePath;

	@ApiModelProperty("触点门店ID")
	private String storeId;

	@ApiModelProperty("触点门店编码")
	private String storeCode;

	@ApiModelProperty("触点门店名称")
	private String storeName;

	@ApiModelProperty("用户uniId")
	private String uniId;

	@ApiModelProperty("会员ID/会员编号")
	private String vipCode;

	@ApiModelProperty("会员名称/用户昵称")
	private String nickName;

	@ApiModelProperty("查看时间")
	private Date checkTime;

}
