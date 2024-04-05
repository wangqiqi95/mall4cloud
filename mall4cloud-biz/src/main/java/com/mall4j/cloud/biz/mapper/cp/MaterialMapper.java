package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.MaterialPageDTO;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.vo.cp.MaterialVO;
import com.mall4j.cloud.biz.vo.cp.MiniMaterialVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 素材表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
public interface MaterialMapper {

	/**
	 * 获取素材表列表
	 * @return 素材表列表
	 */
	List<MaterialVO> list(@Param("material") MaterialPageDTO request);

	List<Material> selectByIds(@Param("dto") MaterialPageDTO dto);

	/**
	 * 小程序端素材查询列表
	 * @return 素材表列表
	 */
	List<MiniMaterialVO> miniList(@Param("material") MaterialPageDTO request);

	List<MiniMaterialVO> selectRecomList(@Param("material") MaterialPageDTO request);

	/**
	 * 根据素材类型ID查询素材数量
	 * @return 素材类型ID
	 */
	Integer getMaterialCountByMatTypeId(@Param("matTypeIdList") List<Integer> matTypeIdList,@Param("status")Integer status);

	/**
	 * 根据素材表id获取素材表
	 *
	 * @param id 素材表id
	 * @return 素材表
	 */
	Material getById(@Param("id") Long id);

	/**
	 * 保存素材表
	 * @param material 素材表
	 */
	void save(@Param("material") Material material);

	/**
	 * 更新素材表
	 * @param material 素材表
	 */
	void update(@Param("material") Material material);

	/**
	 * 根据素材表id删除素材表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByIds(@Param("ids") List<Long> ids);

	/**
	 * 查询过期素材
	 * @return 素材表列表
	 */
	List<Long> getExpireMaterialIds();

	/**
	 * 更新素材表状态
	 * @param materialIds 素材表Id
	 */
	void updateMaterialStatusByIds(@Param("materialIds") List<Long> materialIds);

	void changeMenu(@Param("materialIds") List<Long> materialIds,@Param("matType") Integer matType);

	void use(@Param("id") Long id);
}
