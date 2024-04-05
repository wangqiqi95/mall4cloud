package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.TzStoreFilter;
import org.apache.ibatis.annotations.Param;

/**
 *  门店过滤表
 *
 * @author Casey
 * @date 2023/7/6 17:40
 */
public interface TzStoreFilterMapper {

	/**
	 * 根据id获取
	 *
	 * @param storeId id
	 * @return 
	 */
	TzStoreFilter getByStoreId(@Param("storeId") Long storeId);

	/**
	 * 根据id获取
	 *
	 * @param storeCode storeCode
	 * @return
	 */
	TzStoreFilter getBystoreCode(@Param("storeCode") String storeCode);

}
