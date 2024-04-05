package com.mall4j.cloud.biz.vo.channels;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelsBrandOneVO {
	
	/**
	 * 品牌库中的品牌编号
	 */
	private String brandId;
	
	/**
	 * 品牌名称
	 */
	private String brandName;
	
	/**
	 * 商标分类号, 取值范围1-45
	 */
	private String classificationNo;
	/**
	 * 商标类型, 取值1:R标; 2: TM标
	 */
	private Integer tradeMarkSymbol;
	/**
	 * 商标注册人, R标时必填
	 */
	private String registrant;
	/**
	 * 商标注册号, R标时必填
	 */
	private String registerNo;
	/**
	 * 商标注册有效期, 开始时间, 长期有效可不填
	 */
	private Date registerStartTime;
	/**
	 * 商标注册有效期, 结束时间, 长期有效可不填
	 */
	private Date registerEndTime;
	/**
	 * 商标是否长期有效,0-否 ,1-是
	 */
	private Integer registerIsPermanent;
	/**
	 * 商标注册证的file_id集合, R标时必填, 限制最多传1张
	 */
	private String registerCertifications;
	/**
	 * 商标注册证,图片url集合,R标时必填, 限制最多传1张
	 */
	private String registerCertificationPics;
	/**
	 * 变更/续展证明的file_id集合, 限制最多传5张
	 */
	private String renewCertifications;
	/**
	 * 更/续展证明,图片url集合,限制最多传5张
	 */
	private String renewCertificationPics;

	/**
	 * 商标申请受理时间, TM标时必填
	 */
	private Date acceptanceTime;
	/**
	 * 商标注册申请受理书file_id集合, TM标时必填, 限制最多传1张
	 */
	private String acceptanceCertification;
	/**
	 * 商标注册申请受理书图片url集合, TM标时必填, 限制最多传1张
	 */
	private String acceptanceCertificationPic;

	/**
	 * 商标申请号, TM标时必填
	 */
	private String acceptanceNo;
	/**
	 * 商标授权信息, 取值1:自有品牌; 2: 授权品牌
	 */
	private Long grantType;
	/**
	 * 品牌销售授权书的file_id集合, 授权品牌必填, 限制最多传9张
	 */
	private String grantCertifications;
	/**
	 * 品牌销售授权书的图片url集合, 授权品牌必填, 限制最多传9张
	 */
	private String grantCertificationPics;

	/**
	 * 授权级数, 授权品牌必填, 取值1-3
	 */
	private Long grantLevel;
	/**
	 * 授权有效期, 开始时间, 长期有效可不填
	 */
	private Date grantStartTime;
	/**
	 * 授权有效期, 结束时间, 长期有效可不填
	 */
	private Date grantEndTime;
	/**
	 * 授权是否长期有效,0-否 ,1-是
	 */
	private Long grantIsPermanent;
	/**
	 * 品牌权利人证件照的file_id集合, 限制最多传2张
	 */
	private String brandOwnerIdPhotos;
	/**
	 * 品牌权利人证件照的图片url集合, 限制最多传2张
	 */
	private String brandOwnerIdPhotoPics;
	
}
