package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.GroupTagRef;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 
 *
 * @author hwy
 * @date 2022-02-16 12:01:07
 */
public interface GroupTagRefService extends IService<GroupTagRef> {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<GroupTagRef> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param groupId id
	 * @return 
	 */
	List<GroupTagRef> getByGroupId(String groupId);

	/**
	 * 根据id删除
	 * @param groupId id
	 */
	void deleteByGroupId(String groupId);

	void saveTo(String groupId,List<String> tagIds);
}
