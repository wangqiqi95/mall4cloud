package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收钱吧购买操作,会员账户集成参数实体
 */
@Data
public class PurchaseCrmAccountOption {

	/*
		1：financial_privilege（金融权益），
		2：coupon（券，如单品券、优惠券等），
		3：card（礼品卡），
		4：会员，
		5：wallet（礼品卡 & 储值账户）
	 */
	@ApiModelProperty("必传,权益类型")
	private String app_type;
	
	@ApiModelProperty("商户会员编号，商户CRM对接唯一标识,app_type=1（金融权益）下，member_sn与member_id至少填一个")
	private String member_sn;
	
	@ApiModelProperty("商户会员id，商户系统会员唯一标识外其它标识,app_type=1（金融权益）下，member_sn与member_id至少填一个")
	private String member_id;
	
	@ApiModelProperty("手机号，对接app_type=3时选用")
	private String mobile;
	
	@ApiModelProperty("微信公众平台用户标识，对接app_type为“3”或“5”时默认优先使用")
	private String wx_union_id;
	
	@ApiModelProperty("预先选择权益类型")
	private String preferred_class;
	
	@ApiModelProperty("预先选择权益编号")
	private String preferred_refer_id;

}
