package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.dto.StoreRenovationMouduldParamDTO;
import com.mall4j.cloud.platform.dto.StoreRenovationSearchDTO;
import com.mall4j.cloud.platform.model.StoreRenovation;
import com.mall4j.cloud.platform.vo.StoreRenovationMouduleVO;

/**
 * 店铺装修信息
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
public interface StoreRenovationService extends IService<StoreRenovation> {

	/**
	 * 分页获取店铺装修信息列表
	 * @param pageDTO 分页参数
	 * @return 店铺装修信息列表分页数据
	 */
	PageVO<StoreRenovation> page( StoreRenovationSearchDTO searchDTO);

	/**
	 * 根据店铺装修信息id获取店铺装修信息
	 *
	 * @param renovationId 店铺装修信息id
	 * @return 店铺装修信息
	 */
	StoreRenovation getByRenovationId(Long renovationId);

	/**
	 * 保存店铺装修信息
	 * @param storeRenovation 店铺装修信息
	 */
//	void save(StoreRenovation storeRenovation);

	/**
	 * 更新店铺装修信息
	 * @param storeRenovation 店铺装修信息
	 */
	void update(StoreRenovation storeRenovation);

	/**
	 * 根据店铺装修信息id删除店铺装修信息
	 * @param renovationId 店铺装修信息id
	 */
	void deleteById(Long renovationId);

    PageVO<StoreRenovationMouduleVO> modulePage(PageDTO pageDTO, StoreRenovationMouduldParamDTO keyword);

	StoreRenovation getHomepage(Long storeId);

    Boolean online(Long renovationId);

	Boolean offline(Long renovationId);

	StoreRenovation getRenovation(Integer homeStatus);

	void pushStoreRenovation();

	/**
	 * 新增店铺装修详情记录
	 * @param renovationId    店铺装修记录表ID
	 * @return
	 */
	ServerResponseEntity<String> saveStoreRenovationItem(Long renovationId);

}
