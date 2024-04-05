package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.MaterialStore;

import java.util.List;

/**
 * 素材商店表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialStoreService {


	/**
	 * 保存素材商店表
	 * @param materialStore 素材商店表
	 */
	void save(MaterialStore materialStore);


	/**
	 * 根据素材id删除
	 * @param id 素材id
	 */
	void deleteByMatId(Long id);

	/**
	 * 根据素材id查询列表
	 * @param matId 素材id
	 * @return List<MaterialStore>
	 */
    List<MaterialStore> listByMatId(Long matId);
}
