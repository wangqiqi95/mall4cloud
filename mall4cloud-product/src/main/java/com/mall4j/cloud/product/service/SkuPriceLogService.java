package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.product.dto.ESkuPriceLogDTO;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.SkuPriceLogParamDTO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuPriceLog;
import com.mall4j.cloud.product.vo.SkuPriceLogVO;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
public interface SkuPriceLogService extends IService<SkuPriceLog> {

	/**
	 * 分页获取列表
	 * @return 列表分页数据
	 */
	PageVO<SkuPriceLogVO> page(SkuPriceLogParamDTO skuPriceLogDTO);

	void soldExcel(Long downHistoryid,SkuPriceLogParamDTO skuPriceLogDTO);

	void executesavePriceLogs(List<SkuPriceLogDTO> skuPriceLogs);

	void savePriceLogs(ESkuPriceLogDTO eSkuPriceLogDTO);

}
