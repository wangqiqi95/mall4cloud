package com.mall4j.cloud.api.delivery.bo;

import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.vo.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * 指定条件包邮项BO
 */
@Data
public class TransfeeFreeBO extends BaseVO {
	private static final long serialVersionUID = 1L;
	
	//指定条件包邮项id
	private Long transfeeFreeId;
	
	//运费模板id
	private Long transportId;
	
	//包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
	private Integer freeType;
	
	//需满金额
	private Long amount;
	
	//包邮x件/重量/体积
	private Double piece;
	
	//指定条件包邮城市项
	private List<AreaVO> freeCityList;
}
