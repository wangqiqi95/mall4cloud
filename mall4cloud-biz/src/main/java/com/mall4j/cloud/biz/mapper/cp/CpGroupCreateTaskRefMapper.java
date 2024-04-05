package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTaskRef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2024-01-19 15:49:54
 */
public interface CpGroupCreateTaskRefMapper extends BaseMapper<CpGroupCreateTaskRef> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<CpGroupCreateTaskRef> listByGroupId(@Param("groupId") String groupId);


	/**
	 * 根据id删除
	 */
	void deleteByGroupId(@Param("groupId") String groupId);
}
