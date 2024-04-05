package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.GroupPushTaskCpRelationBO;
import com.mall4j.cloud.user.dto.UserJourneysDTO;
import com.mall4j.cloud.user.dto.UserStaffCpRelationSetTagRequest;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.vo.CrmUserManagerVO;
import com.mall4j.cloud.user.vo.SoldUserStaffRelVo;
import com.mall4j.cloud.user.vo.UserJourneysVO;
import com.mall4j.cloud.user.vo.UserStaffCpRelationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
public interface UserStaffCpRelationService extends IService<UserStaffCpRelation> {


	/**
	 *
	 * 导购客户列表查询
	 * @param searchDTO
	 * @return
	 */
	PageVO<UserStaffCpRelationListVO> pageWithStaff( UserStaffCpRelationSearchDTO searchDTO);

	/**
	 * 导出客户
	 * @param searchDTO
	 * @return
	 */
	List<SoldUserStaffRelVo> soldStaffUser(UserStaffCpRelationSearchDTO searchDTO);

	List<UserStaffCpRelationListVO> getUserStaffRelBy( UserStaffCpRelationSearchDTO searchDTO);

	List<UserStaffCpRelationListVO> getUserStaffRelAll( UserStaffCpRelationSearchDTO searchDTO);

	UserStaffCpRelation getByStaffAndUser(Long staffId, Long userId);

	UserStaffCpRelation getByQiWeiStaffIdAndQiWeiUserId(String qiWeiStaffId, String qiWeiUserId);

	UserStaffCpRelation getByQiWeiUserId(String qiWeiUserId,Long staffId);

	void bindUserId(String userUnionId, Long userId,Long staffId);

	void bindStaffId(String qiWeiStaffId, Long staffId);

	/**
	 * 删除好友
	 * @param id
	 */
	void deleteById(Long id,String contactChangeType);

	/**
	 * 根据员工企业微信id和客户企业微信id查询客户信息
	 * @param qiWeiStaffId 员工的企业微信id
	 * @param qiWeiUserId 客户企业微信id
	 * @return
	 */
	UserStaffCpRelationListVO getUserInfoByQiWeiUserId(String qiWeiStaffId, String qiWeiUserId);

	/**
	 * 统计为加会员的用户数
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
	GroupPushTaskCpRelationBO getCpRelationDataByStaffCpUserIdAndVipCpUserId(Long staffId, Long userId);

	CrmUserManagerVO getCrmUserManagerVO(Long relationId);

    void setTag(UserStaffCpRelationSetTagRequest request);

	void setStage(UserStaffCpRelationSetTagRequest request);

	void removeStage(UserStaffCpRelationSetTagRequest request);

	List<UserJourneysVO> journeys(UserJourneysDTO request,Long adminStaffId);

    CrmUserManagerVO getMobileCrmUserManagerVO(Long relationId);

    PageVO<UserStaffCpRelationVO> pageStaffByUser(UserStaffCpRelationSearchDTO staffCpRelationSearchDTO);

	/**
	 * 员工离职
	 * @param staffIds
	 */
	void staffDimission(List<Long> staffIds);
}
