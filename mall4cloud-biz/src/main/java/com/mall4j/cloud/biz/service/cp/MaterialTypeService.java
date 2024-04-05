package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialTypeDTO;
import com.mall4j.cloud.biz.model.cp.MaterialType;
import com.mall4j.cloud.biz.vo.cp.MaterialTypeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;


/**
 * 素材分类表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialTypeService {


	/**
	 * 根据素材分类表id获取素材分类表
	 *
	 * @param id 素材分类表id
	 * @return 素材分类表
	 */
	MaterialType getById(Long id);

	/**
	 * 保存素材分类表
	 * @param materialType 素材分类表
	 */
	void save(MaterialType materialType);

	/**
	 * 更新素材分类表
	 * @param materialType 素材分类表
	 */
	void update(MaterialType materialType);

	/**
	 * 根据素材分类表id删除素材分类表
	 * @param id 素材分类表id
	 */
	void deleteById(Long id);

	/**
	 * 查询一级分类
	 * @param id 父类型id
	 * @return List<MaterialType>
	 */
	List<MaterialType> listParent();

	/**
	 * 查询二级分类
	 * @param parentId 父类型id
	 * @return List<MaterialType>
	 */
	List<MaterialType> listChildren(Long parentId);

	/**
	 * 返回一级类型 包含 子类型的list
	 * @param parentId 父类型id
	 * @return List<MaterialType>
	 */
	List<MaterialTypeVO> listParentContainChildren();

	/**
	 * 校验数据是否允许删除
	 * @param materialTypeDTO 素材分类表入参
	 */
	Integer checkMaterial(MaterialTypeDTO materialTypeDTO);

}
