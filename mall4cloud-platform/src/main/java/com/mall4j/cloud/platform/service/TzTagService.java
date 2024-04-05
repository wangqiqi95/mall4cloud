package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.TzTagDTO;
import com.mall4j.cloud.platform.dto.TzTagQueryParamDTO;
import com.mall4j.cloud.platform.dto.TzTagStoreDTO;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.vo.TzTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店标签
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
public interface TzTagService extends IService<TzTag> {

	/**
	 * 分页获取门店标签列表
	 * @param pageDTO 分页参数
	 * @return 门店标签列表分页数据
	 */
	PageVO<TzTagVO> page(PageDTO pageDTO,TzTagQueryParamDTO paramDTO);

	/**
	 * 根据门店标签id获取门店标签
	 *
	 * @param tagId 门店标签id
	 * @return 门店标签
	 */
	TzTagVO getByTagId(Long tagId);

	/**
	 * 保存门店标签
	 * @param tzTag 门店标签
	 */
	void saveTo(TzTagDTO tzTag);

	/**
	 * 更新门店标签
	 * @param tzTag 门店标签
	 */
	void updateTo(TzTagDTO tzTag);

	void updateTagStatus(Long tagId,Integer status);

	/**
	 * 根据门店标签id删除门店标签
	 * @param tagId 门店标签id
	 */
	void deleteById(Long tagId);

	/**
	 * 获取员工标签
	 * @param staffId
	 * @return
	 */
	List<TzTagDetailVO> listTagByStaffId(@Param("staffId")Long staffId);
}
