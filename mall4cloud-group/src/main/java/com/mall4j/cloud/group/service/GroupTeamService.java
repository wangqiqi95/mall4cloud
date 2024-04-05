package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.GroupTeam;
import com.mall4j.cloud.group.vo.app.AppGroupTeamVO;
import com.mall4j.cloud.group.vo.GroupTeamVO;

import java.util.List;

/**
 * 拼团团队表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public interface GroupTeamService {

	/**
	 * 分页获取拼团团队表列表
	 * @param pageDTO 分页参数
	 * @return 拼团团队表列表分页数据
	 */
	PageVO<GroupTeam> page(PageDTO pageDTO);

	/**
	 * 根据拼团团队表id获取拼团团队表
	 *
	 * @param groupTeamId 拼团团队表id
	 * @return 拼团团队表
	 */
	GroupTeamVO getByGroupTeamId(Long groupTeamId);

	/**
	 * 保存拼团团队表
	 * @param groupTeam 拼团团队表
	 */
	void save(GroupTeam groupTeam);

	/**
	 * 更新拼团团队表
	 * @param groupTeam 拼团团队表
	 */
	void update(GroupTeam groupTeam);

	/**
	 * 根据拼团团队表id删除拼团团队表
	 * @param groupTeamId 拼团团队表id
	 */
	void deleteById(Long groupTeamId);

	/**
	 * 可加入的团列表
	 *
	 * @param groupActivityId
	 * @param showSize
	 * @return
	 */
    List<AppGroupTeamVO> listJoinGroup(Long groupActivityId, Integer showSize);

	/**
	 * 获取拼团团队信息
	 * @param groupTeamId
	 * @return
	 */
	AppGroupTeamVO getAppGroupTeam(Long groupTeamId);
}
