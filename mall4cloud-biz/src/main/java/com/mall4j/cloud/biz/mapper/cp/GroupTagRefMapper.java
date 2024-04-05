package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.GroupTagRef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author hwy
 * @date 2022-02-16 12:01:07
 */
public interface GroupTagRefMapper extends BaseMapper<GroupTagRef> {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<GroupTagRef> list();

	/**
	 * 根据id获取
	 *
	 * @param groupId id
	 * @return
	 */
	List<GroupTagRef> getByGroupId(@Param("groupId") String groupId);


	/**
	 * 根据id删除
	 * @param groupId
	 */
	void deleteByGroupId(@Param("groupId") String groupId);
}
