package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.MaterialStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材商店表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialStoreMapper {

	/**
	 * 保存素材商店表
	 * @param materialStore 素材商店表
	 */
	void save(@Param("materialStore") MaterialStore materialStore);
	/**
	 * 根据素材id删除
	 * @param matId 素材id
	 */
    void deleteByMatId(@Param("matId")Long matId);

	/**
	 * 查询商店列表
	 * @param store 查询条件
	 * @return List<MaterialStore>
	 */
	List<MaterialStore> list(@Param("materialStore")MaterialStore store);
}
