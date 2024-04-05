package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.CpMaterialLable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材 互动雷达标签
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
public interface CpMaterialLableMapper {

	/**
	 * 获取素材 互动雷达标签列表
	 * @return 素材 互动雷达标签列表
	 */
	List<CpMaterialLable> list();

	/**
	 * 根据素材 互动雷达标签id获取素材 互动雷达标签
	 *
	 * @param id 素材 互动雷达标签id
	 * @return 素材 互动雷达标签
	 */
	CpMaterialLable getById(@Param("id") Long id);

	/**
	 * 保存素材 互动雷达标签
	 * @param cpMaterialLable 素材 互动雷达标签
	 */
	void save(@Param("cpMaterialLable") CpMaterialLable cpMaterialLable);

	/**
	 * 更新素材 互动雷达标签
	 * @param cpMaterialLable 素材 互动雷达标签
	 */
	void update(@Param("cpMaterialLable") CpMaterialLable cpMaterialLable);

	/**
	 * 根据素材 互动雷达标签id删除素材 互动雷达标签
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByMatId(@Param("matId") Long matId);

	List<CpMaterialLable> listByMatId(@Param("matId") Long matId);
}
