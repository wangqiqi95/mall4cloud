
package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author lt
 * @date 2020/12/30
 */
@Data
@ApiModel("直播品牌")
public class LiveBrandDTO {

	@ApiModelProperty(value = "营业执照或组织机构代码证，图片url")
	@NotBlank(message = "营业执照或组织机构代码证不能为空")
	private String license;

	@ApiModelProperty(value = "认证审核类型 1:国内品牌申请-R标 2:国内品牌申请-TM标 3:海外品牌申请-R标 4:海外品牌申请-TM标")
	@NotNull(message = "认证审核类型不能为空")
	private Integer brandAuditType;

	@ApiModelProperty(value = "品牌经营类型 1:自有品牌 2:代理品牌 3:无品牌")
	@NotNull(message = "品牌经营类型不能为空")
	private Integer brandManagementType;

	@ApiModelProperty(value = "商标/品牌词")
	@NotBlank(message = "商标/品牌词不能为空")
	private String brandWording;

	@ApiModelProperty(value = "商品产地是否进口 1:是 2:否")
	@NotNull(message = "商品产地是否进口不能为空")
	private Integer commodityOriginType;

	@ApiModelProperty(value = "中华人民共和国海关进口货物报关单，图片url")
	private String importedGoodsForm;

	@ApiModelProperty(value = "销售授权书（如商持人为自然人，还需提供有其签名的身份证正反面扫描件)，图片url")
	private String saleAuthorization;

	@ApiModelProperty(value = "商标申请人姓名")
	private String trademarkApplicant;

	@ApiModelProperty("商标申请时间, yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date trademarkApplicationTime;

	@ApiModelProperty("商标有效期，yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date trademarkAuthorizationPeriod;

	@ApiModelProperty("商标变更证明，图片url")
	private String trademarkChangeCertificate;

	@ApiModelProperty("商标注册人姓名")
	private String trademarkRegistrant;

	@ApiModelProperty("商标注册号/申请号")
	private String trademarkRegistrantNu;

	@ApiModelProperty("商标注册申请受理通知书，图片url")
	private String trademarkRegistrationApplication;

	@ApiModelProperty("商标注册证书，图片url")
	private String trademarkRegistrationCertificate;

	@ApiModelProperty("商标分类 TrademarkType")
	private String trademarkType;

	@ApiModelProperty(value = "商品使用场景,1:视频号，3:订单中")
	@NotNull(message = "商品使用场景,1:视频号，3:订单中")
	private List<Integer> sceneGroupList;
}
