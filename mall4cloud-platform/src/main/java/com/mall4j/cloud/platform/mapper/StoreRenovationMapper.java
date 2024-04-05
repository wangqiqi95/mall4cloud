package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.platform.dto.StoreRenovationMouduldParamDTO;
import com.mall4j.cloud.platform.dto.StoreRenovationSearchDTO;
import com.mall4j.cloud.platform.model.StoreRenovation;
import com.mall4j.cloud.platform.vo.StoreRenovationMouduleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺装修信息
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
public interface StoreRenovationMapper extends BaseMapper<StoreRenovation> {

	/**
	 * 获取店铺装修信息列表
	 * @return 店铺装修信息列表
	 */
	List<StoreRenovation> list();

	/**
	 * 根据店铺装修信息id获取店铺装修信息
	 *
	 * @param renovationId 店铺装修信息id
	 * @return 店铺装修信息
	 */
	StoreRenovation getByRenovationId(@Param("renovationId") Long renovationId);

	/**
	 * 保存店铺装修信息
	 * @param storeRenovation 店铺装修信息
	 */
	void save(@Param("storeRenovation") StoreRenovation storeRenovation);

	/**
	 * 更新店铺装修信息
	 * @param storeRenovation 店铺装修信息
	 */
	void update(@Param("storeRenovation") StoreRenovation storeRenovation);

	/**
	 * 根据店铺装修信息id删除店铺装修信息
	 * @param renovationId
	 */
	void deleteById(@Param("renovationId") Long renovationId);

	/**
	 * 组件查询接口
	 * @param keyword
	 * @return
	 */
    List<StoreRenovationMouduleVO> moduleList(@Param("paramDTO") StoreRenovationMouduldParamDTO paramDTO);

	StoreRenovation getHomePage(@Param("storeId") Long storeId);

    Boolean updateStatus(@Param("renovationId") Long renovationId,@Param("status") int status);

	StoreRenovation getRenovation(@Param("homeStatus")  Integer homeStatus);

	List<StoreRenovation> listBySearchDTO(@Param("searchDTO") StoreRenovationSearchDTO searchDTO);

	int saveStoreRenovationItem(@Param("renovationId") Long renovationId, @Param("userId") Long userId);

	int selectStoreRenovationItemOfUserId(@Param("userId") Long userId);

}
