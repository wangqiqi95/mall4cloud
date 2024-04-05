package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.vo.TzTagStoreDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.TzTagStoreDTO;
import com.mall4j.cloud.platform.model.TzTagStaff;
import com.mall4j.cloud.platform.model.TzTagStore;
import com.mall4j.cloud.platform.vo.TzTagSimpleVO;
import com.mall4j.cloud.platform.vo.TzTagStoreVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签关联门店
 *
 * @author gmq
 * @date 2022-09-13 12:01:58
 */
public interface TzTagStoreService extends IService<TzTagStore> {

	/**
	 * 分页获取标签关联门店列表
	 * @param pageDTO 分页参数
	 * @return 标签关联门店列表分页数据
	 */
	PageVO<TzTagStoreVO> pageStore(PageDTO pageDTO, Long tagId);

	List<Long> listByTagId(Long tagId);

	List<TzTagStoreDetailVO> listDetailByTagId(Long tagId);

	/**
	 * 获取门店关联标签
	 * @param storeId
	 * @return
	 */
	List<TzTagSimpleVO> listTagByStoreId(Long storeId);

	/**
	 * 门店管理-保存门店标签
	 * @param tzTagStoreDTO
	 */
	void saveStoreTags(TzTagStoreDTO tzTagStoreDTO);

	/**
	 * 根据标签关联门店id删除标签关联门店
	 * @param id 标签关联门店id
	 */
	void deleteById(Long id);

	void deleteByTagId(Long tagId);
}
