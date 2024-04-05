package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 视频号4.0查询品牌列表DTO
 */
@Data
public class ChannelsBrandDTO {
	
	@ApiModelProperty(value = "品牌库中的品牌编号", required = true)
	@NotBlank(message = "品牌编号不能为空")
	private String brandId;

	//@ApiModelProperty(value = "品牌商标中文名")
	//private String chName;
	//
	//@ApiModelProperty(value = "品牌商标英文名")
	//private String enName;
	
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	
	@ApiModelProperty(value = "商标分类号", required = true)
	@NotBlank(message = "商标分类号不能为空")
	private String classificationNo;
	
	@ApiModelProperty(value = "商标类型, 取值1:R标; 2: TM标", required = true)
	private Integer tradeMarkSymbol;
	
	@ApiModelProperty("商标注册人, R标时必填")
	private String registrant;
	
	@ApiModelProperty("商标注册号, R标时必填")
	private String registerNo;
	
	@ApiModelProperty("商标注册有效期, 开始时间, 长期有效可不填")
	private Date registerStartTime;
	
	@ApiModelProperty("商标注册有效期, 结束时间, 长期有效可不填")
	private Date registerEndTime;
	
	@ApiModelProperty("商标是否长期有效,0-否 ,1-是")
	private Integer registerIsPermanent;
	
	@ApiModelProperty("商标注册证的file_id集合, R标时必填, 限制最多传1张")
	private String registerCertifications;
	
	@ApiModelProperty("商标注册证的url集合, R标时必填, 限制最多传1张")
	private String registerCertificationPics;

	@ApiModelProperty("变更/续展证明的file_id集合, 限制最多传5张")
	private String renewCertifications;
	
	@ApiModelProperty("变更/续展证明的url集合, 限制最多传5张")
	private String renewCertificationPics;
	
	@ApiModelProperty("商标申请受理时间, TM标时必填")
	private Date acceptanceTime;
	
	@ApiModelProperty("商标注册申请受理书file_id集合, TM标时必填, 限制最多传1张")
	private String acceptanceCertification;
	
	@ApiModelProperty("商标注册申请受理书url集合, TM标时必填, 限制最多传1张")
	private String acceptanceCertificationPic;
	
	@ApiModelProperty("商标申请号, TM标时必填")
	private String acceptanceNo;
	
	@ApiModelProperty("商标授权信息, 取值1:自有品牌; 2: 授权品牌")
	private Integer grantType;
	
	@ApiModelProperty("品牌销售授权书的file_id集合, 授权品牌必填, 限制最多传9张")
	private String grantCertifications;
	
	@ApiModelProperty("品牌销售授权书的url集合, 授权品牌必填, 限制最多传9张")
	private String grantCertificationPics;
	
	@ApiModelProperty("授权级数, 授权品牌必填, 取值1-3")
	private Integer grantLevel;
	
	@ApiModelProperty("授权有效期, 开始时间, 长期有效可不填")
	private Date grantStartTime;
	
	@ApiModelProperty("授权有效期, 结束时间, 长期有效可不填")
	private Date grantEndTime;
	
	@ApiModelProperty("授权是否长期有效,0-否 ,1-是")
	private Integer grantIsPermanent;
	
	@ApiModelProperty("品牌权利人证件照的file_id集合, 限制最多传2张")
	private String brandOwnerIdPhotos;
	
	@ApiModelProperty("品牌权利人证件照的url集合, 限制最多传2张")
	private String brandOwnerIdPhotoPics;
}
