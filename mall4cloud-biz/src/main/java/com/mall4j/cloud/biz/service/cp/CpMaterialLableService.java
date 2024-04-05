package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.CpMaterialLable;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 素材 互动雷达标签
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
public interface CpMaterialLableService {

	/**
	 * 分页获取素材 互动雷达标签列表
	 * @param pageDTO 分页参数
	 * @return 素材 互动雷达标签列表分页数据
	 */
	PageVO<CpMaterialLable> page(PageDTO pageDTO);

	/**
	 * 根据素材 互动雷达标签id获取素材 互动雷达标签
	 *
	 * @param id 素材 互动雷达标签id
	 * @return 素材 互动雷达标签
	 */
	CpMaterialLable getById(Long id);

	/**
	 * 保存素材 互动雷达标签
	 * @param cpMaterialLable 素材 互动雷达标签
	 */
	void save(CpMaterialLable cpMaterialLable);

	/**
	 * 更新素材 互动雷达标签
	 * @param cpMaterialLable 素材 互动雷达标签
	 */
	void update(CpMaterialLable cpMaterialLable);

	/**
	 * 根据素材 互动雷达标签id删除素材 互动雷达标签
	 * @param id 素材 互动雷达标签id
	 */
	void deleteById(Long id);

	void deleteByMatId(Long id);

	List<CpMaterialLable> listByMatId(Long id);

}
