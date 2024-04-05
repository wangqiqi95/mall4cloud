package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.GroupTeam;
import com.mall4j.cloud.group.vo.GroupTeamVO;
import com.mall4j.cloud.group.vo.app.AppGroupTeamVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拼团团队表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public interface GroupTeamMapper {

	/**
	 * 获取拼团团队表列表
	 *
	 * @return 拼团团队表列表
	 */
	List<GroupTeam> list();

	/**
	 * 根据拼团团队表id获取拼团团队表
	 *
	 * @param groupTeamId 拼团团队表id
	 * @return 拼团团队表
	 */
	GroupTeamVO getByGroupTeamId(@Param("groupTeamId") Long groupTeamId);

	/**
	 * 保存拼团团队表
	 *
	 * @param groupTeam 拼团团队表
	 */
	void save(@Param("groupTeam") GroupTeam groupTeam);

	/**
	 * 更新拼团团队表
	 *
	 * @param groupTeam 拼团团队表
	 */
	void update(@Param("groupTeam") GroupTeam groupTeam);

	/**
	 * 根据拼团团队表id删除拼团团队表
	 *
	 * @param groupTeamId
	 */
	void deleteById(@Param("groupTeamId") Long groupTeamId);

	/**
	 * 可加入的团列表
	 *
	 * @param groupActivityId
	 * @param showSize
	 * @return
	 */
	List<AppGroupTeamVO> listJoinGroup(@Param("groupActivityId") Long groupActivityId, @Param("showSize") Integer showSize);

	/**
	 * 获取拼团团队信息
	 *
	 * @param groupTeamId
	 * @return
	 */
    AppGroupTeamVO getAppGroupTeam(@Param("groupTeamId") Long groupTeamId);

	/**
	 * 取消拼团队伍
	 *
	 * @param groupTeamId
	 */
	void cancelGroupTeam(@Param("groupTeamId") Long groupTeamId);

	/**
	 * 更新成成团状态
	 *
	 * @param groupTeam 团队
	 * @return 是否更新成功
	 */
    int updateToSuccess(@Param("groupTeam") GroupTeam groupTeam);

	/**
	 * 团购订单失败
	 *
	 * @param groupTeamId
	 * @return
	 */
	int updateToUnSuccess(@Param("groupTeamId") Long groupTeamId);
}
