package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频号4.0品牌资质表
 *
 */
@Data
@TableName("channels_brand_qualification")
public class ChannelsBrandQualification implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 品牌库中的品牌编号
	 */
	private String brandId;
	///**
	// * 品牌商标中文名
	// */
	//private String chName;
	///**
	// * 品牌商标英文名
	// */
	//private String enName;
	
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
	 * 商标注册证的url集合, R标时必填, 限制最多传1张
	 */
	private String registerCertificationPics;
	/**
	 * 变更/续展证明的file_id集合, 限制最多传5张
	 */
	private String renewCertifications;
	/**
	 * 变更/续展证明的url集合, 限制最多传5张
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
	 * 商标注册申请受理书url集合, TM标时必填, 限制最多传1张
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
	 * 品牌销售授权书的url集合, 授权品牌必填, 限制最多传9张
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
	 * 品牌权利人证件照的url集合, 限制最多传2张
	 */
	private String brandOwnerIdPhotoPics;
	/**
	 * 审核单ID
	 */
	private String auditId;
	/**
	 * 状态 (3 撤回品牌审核, 4 审核成功, 5 审核失败, 7 品牌资质被系统撤销)
	 */
	private Integer status;
	/**
	 * 审核原因
	 */
	private String reason;
	/**
	 * 创建人 userID
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 是否删除 0 否 1是
	 */
	private Integer isDeleted;

}
