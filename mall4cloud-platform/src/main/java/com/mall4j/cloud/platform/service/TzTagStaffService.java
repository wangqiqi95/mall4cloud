package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.model.TzTagStaff;
import com.mall4j.cloud.platform.vo.TzTagStaffVO;

import java.util.List;

/**
 * 标签关联员工
 *
 * @author gmq
 * @date 2022-09-13 12:01:45
 */
public interface TzTagStaffService extends IService<TzTagStaff> {

	/**
	 * 分页获取标签关联员工列表
	 * @param pageDTO 分页参数
	 * @return 标签关联员工列表分页数据
	 */
	PageVO<TzTagStaffVO> pageStaff(PageDTO pageDTO,Long tagId);

	List<Long> listByTagId(Long tagId);

	/**
	 * 根据标签关联员工id删除标签关联员工
	 * @param id 标签关联员工id
	 */
	void deleteById(Long id);

	void deleteByTagId(Long tagId);
}
