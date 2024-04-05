package com.mall4j.cloud.api.delivery.bo;

import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.common.vo.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * 运费项BO
 */
@Data
public class TransfeeBO extends BaseVO {
	private static final long serialVersionUID = 1L;
	
	//运费项id
	private Long transfeeId;
	
	//运费模板id
	private Long transportId;
	
	//续件数量
	private Double continuousPiece;
	
	//首件数量
	private Double firstPiece;
	
	//续件费用
	private Long continuousFee;
	
	//首件费用
	private Long firstFee;
	
	//指定条件运费城市项
	private List<AreaVO> cityList;
}
