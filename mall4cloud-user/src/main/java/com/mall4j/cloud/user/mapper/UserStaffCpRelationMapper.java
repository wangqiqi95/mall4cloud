package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserCountVO;
import com.mall4j.cloud.api.user.vo.UserRelactionAddWayVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.cp.CountStaffRelByStatesVO;
import com.mall4j.cloud.user.bo.GroupPushTaskCpRelationBO;
import com.mall4j.cloud.user.dto.UserCountDTO;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.api.user.vo.SelectUserCountVO;
import com.mall4j.cloud.user.vo.UserRelCountDataVO;
import com.mall4j.cloud.user.vo.UserStaffCpRelationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
public interface UserStaffCpRelationMapper extends BaseMapper<UserStaffCpRelation> {

	/**
	 * 查询导购下客户列表
	 * @param searchDTO
	 * @return
	 */
	List<UserStaffCpRelationListVO> pageWithStaff(@Param("searchDTO") UserStaffCpRelationSearchDTO searchDTO);
//
//	/**
//	 * 保存
//	 * @param userStaffCpRelation
//	 */
//	void save(@Param("userStaffCpRelation") UserStaffCpRelation userStaffCpRelation);
//
//	/**
//	 * 更新
//	 * @param userStaffCpRelation
//	 */
//	void update(@Param("userStaffCpRelation") UserStaffCpRelation userStaffCpRelation);


    UserStaffCpRelation getByStaffAndUser(@Param("staffId") Long staffId, @Param("userId") Long userId);

	UserStaffCpRelation getByQiWeiStaffIdAndQiWeiUserId(@Param("qiWeiStaffId") String qiWeiStaffId, @Param("qiWeiUserId") String qiWeiUserId);

	UserStaffCpRelation getByQiWeiUserId(@Param("qiWeiUserId") String qiWeiUserId,@Param("staffId")Long staffId);

	List<UserStaffCpRelation> listByUserIdList(@Param("userIdList") List<Long> userIdList);

	void bindUserId(@Param("userUnionId") String userUnionId, @Param("userId") Long userId);

	void bindStaffId(@Param("qiWeiStaffId") String qiWeiStaffId, @Param("staffId") Long staffId);

    void deleteById(@Param("id")Long id);
	/**
	 * 根据员工企业微信id和客户企业微信id查询客户信息
	 * @param qiWeiStaffId 员工的企业微信id
	 * @param qiWeiUserId 客户企业微信id
	 * @return
	 */
    UserStaffCpRelationListVO getUserInfoByQiWeiUserId(@Param("qiWeiStaffId")String qiWeiStaffId, @Param("qiWeiUserId")String qiWeiUserId);

	/**
	 * 统计未加会员的数量
	 * @return
	 */
    List<CountNotMemberUsersVO> countNotMemberUsers();

	/**
	 * 统计还未加企业微信好友的数量
	 * @return
	 */
	List<CountNotMemberUsersVO> countNotQiWeiUsers();

	/**
	 * 根据导购和用户的ID判断是否存在好友关系，进行校验
	 * @param staffId 导购ID
	 * @param userId 会员ID
	 * @return
	 */
	GroupPushTaskCpRelationBO getCpRelationDataByStaffCpUserIdAndVipCpUserId(@Param("staffId")Long staffId, @Param("userId")Long userId);


	List<UserStaffCpRelationListVO> lastOneListByUserIdList(@Param("userIdList") List<Long> userIdList);

	/**
	 * 根据渠道统计添加好友数
	 * @param states
	 * @return
	 */
	List<CountStaffRelByStatesVO> selectCountByStates(@Param("states") List<String> states);

    void setStage(@Param("id")Long id, @Param("stageId")Long stageId);

	void removeUserStage(@Param("ids")List<Long> ids);
		/**
	 * 查询员工下的客户总数
	 * @param dto
	 * @return
	 */
	List<SelectUserCountVO> getUserCount(@Param("dto")UserCountDTO dto);

	/**
	 * 查询被用户删除的员工
	 * @param dto
	 * @return
	 */
	List<SelectUserCountVO> getUserAndChangeType(@Param("dto")UserCountDTO dto);


	List<UserCountVO> getUserSexCount(@Param("dto")UserCountDTO dto);

	List<UserCountVO> getChartCount(@Param("dto")UserCountDTO dto);

	List<UserRelactionAddWayVO> selectUserRelactionAddWays(@Param("dto")UserCountDTO dto);

	UserRelCountDataVO selectUserRelCountDataVO(@Param("dto")UserCountDTO dto);

	List<UserStaffCpRelationVO> getByUserUnionId(@Param("userUnionId")String userUnionId);

	Integer getCountByUserUnionId(@Param("userUnionId")String userUnionId);
}
