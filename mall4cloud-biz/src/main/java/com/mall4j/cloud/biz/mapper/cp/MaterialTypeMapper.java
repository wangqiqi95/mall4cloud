package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialTypeDTO;
import com.mall4j.cloud.biz.model.cp.MaterialType;
import com.mall4j.cloud.biz.vo.cp.MaterialTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材分类表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialTypeMapper {

	/**
	 * 获取素材分类表列表
	 * @return 素材分类表列表
	 */
	List<MaterialType> list();

	/**
	 * 根据素材分类表id获取素材分类表
	 *
	 * @param id 素材分类表id
	 * @return 素材分类表
	 */
	MaterialType getById(@Param("id") Long id);

	/**
	 * 保存素材分类表
	 * @param materialType 素材分类表
	 */
	void save(@Param("materialType") MaterialType materialType);

	/**
	 * 更新素材分类表
	 * @param materialType 素材分类表
	 */
	void update(@Param("materialType") MaterialType materialType);

	/**
	 * 根据素材分类表id删除素材分类表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
	/**
	 * 查询一级分类
	 * @return List<MaterialType>
	 */
    List<MaterialType> listParent();
	/**
	 * 查询二级分类
	 * @param parentId 父类型id
	 * @return List<MaterialType>
	 */
	List<MaterialType> listChildren(@Param("parentId")Long parentId);
	/**
	 * 返回一级类型 包含 子类型的list
	 * @return List<MaterialType>
	 */
	List<MaterialTypeVO> listParentContainChildren( );

	/**
	 * 查询二级分类ID
	 * @param parentId 父类型id
	 * @return
	 */
	List<Integer> checkMaterial(@Param("parentId")Integer parentId);

}
