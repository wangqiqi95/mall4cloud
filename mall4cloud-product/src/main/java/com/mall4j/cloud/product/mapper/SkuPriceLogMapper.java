package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.product.dto.SkuPriceLogDTO;
import com.mall4j.cloud.api.product.vo.SoldSkuPriceLogExcelVO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.product.dto.SkuPriceLogParamDTO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuPriceLog;
import com.mall4j.cloud.product.vo.SkuPriceLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
public interface SkuPriceLogMapper extends BaseMapper<SkuPriceLog> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<SkuPriceLogVO> list(@Param("dto") SkuPriceLogParamDTO dto);

	List<SoldSkuPriceLogExcelVO> soldExcelList(@Param("dto") SkuPriceLogParamDTO dto,@Param("pageAdapter") PageAdapter pageAdapter);

	int soldCount(@Param("dto") SkuPriceLogParamDTO dto);

}
