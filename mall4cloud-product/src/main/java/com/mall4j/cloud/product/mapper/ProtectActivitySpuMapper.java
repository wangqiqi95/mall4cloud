package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.dto.ProtectActivitySpuPageDTO;
import com.mall4j.cloud.product.model.ProtectActivitySpu;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.vo.ProtectActivitySpuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
public interface ProtectActivitySpuMapper extends BaseMapper<ProtectActivitySpu> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<ProtectActivitySpuVO> pageList(@Param("spuPageDTO") ProtectActivitySpuPageDTO spuPageDTO);

	List<ProtectActivitySpu> getListBySpus(@Param("spuIds") List<Long> spuIds);

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return
	 */
	ProtectActivitySpu getById(@Param("id") Long id);

	void deleteBatch(@Param("ids") List<Long> ids);
}
