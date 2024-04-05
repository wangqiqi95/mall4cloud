package com.mall4j.cloud.api.delivery.bo;

import com.mall4j.cloud.common.vo.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * 运费模板BO
 */
@Data
public class TransportBO extends BaseVO {
	private static final long serialVersionUID = 1L;
	
	//运费模板id
	private Long transportId;
	
	//运费模板名称
	private String transName;
	
	//店铺id
	private Long shopId;
	
	//收费方式（0 按件数,1 按重量 2 按体积）
	private Integer chargeType;
	
	//是否包邮 0:不包邮 1:包邮
	private Integer isFreeFee;
	
	//是否含有包邮条件 0 否 1是
	private Integer hasFreeCondition;
	
	//指定条件包邮项
	private List<TransfeeFreeBO> transFeeFrees;
	
	//运费项
	private List<TransfeeBO> transFees;
}
