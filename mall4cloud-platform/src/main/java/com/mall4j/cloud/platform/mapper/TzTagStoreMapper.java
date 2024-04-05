package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.platform.model.TzTagStaff;
import com.mall4j.cloud.platform.model.TzTagStore;
import com.mall4j.cloud.platform.vo.TzTagSimpleVO;
import com.mall4j.cloud.platform.vo.TzTagStaffVO;
import com.mall4j.cloud.platform.vo.TzTagStoreVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签关联门店
 *
 * @author gmq
 * @date 2022-09-13 12:01:58
 */
public interface TzTagStoreMapper extends BaseMapper<TzTagStore> {

	/**
	 * 获取标签关联门店列表
	 * @return 标签关联门店列表
	 */
	List<TzTagStoreVO> listByTagId(@Param("tagId")Long tagId);

	List<TzTagSimpleVO> listTagByStoreId(@Param("storeId")Long storeId);
}
