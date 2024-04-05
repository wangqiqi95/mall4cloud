package com.mall4j.cloud.user.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.api.user.dto.UserChangeServiceStoreDTO;
import com.mall4j.cloud.api.user.dto.UserDynamicCodeDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.DistributionUserVO;
import com.mall4j.cloud.api.user.vo.MemberOverviewVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserManagerVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.vo.StaffGetUserDetailByMaterialVO;
import com.mall4j.cloud.user.vo.StaffUserVo;
import com.mall4j.cloud.user.vo.UserDynamicCodeVO;
import com.mall4j.cloud.user.vo.UserVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
public interface UserService extends IService<User> {

	/**
	 * 分页获取用户表列表
	 * @param pageDTO 分页参数
	 * @return 用户表列表分页数据
	 */
	PageVO<UserApiVO> page(PageDTO pageDTO);

	/**
	 * 企微添加好友同时注册用户表信息，根据unionId
	 * @param unionId
	 */
	void createUserByUnionId( UserStaffCpRelation relation);

	void batchCreateUserByUnionId( List<UserStaffCpRelation> relation);

	/**
	 * 根据用户表id获取用户表
	 *
	 * @param userId 用户表id
	 * @return 用户表
	 */
	UserApiVO getByUserId(Long userId);

	UserApiVO getByUserIdMp(Long userId);


	UserApiVO getByUnionId(String unionId);

	UserApiVO getByOpenId(String openid);

	/**
	 * 更新用户表
	 * @param user 用户表
	 */
	void updateUser(User user);

	void updateUserToCrm(User user);

	/**
	 * 移除用户信息缓存
	 * @param userId 用户id
	 */
	void removeUserCacheByUserId(Long userId);

	/**
	 * 根据成长值范围，更新普通用户的等级
	 * @param level 更新至该等级
	 * @param minGrowth 最小成长值
	 * @param maxGrowth 最大成长值
	 */
	void updateUserLevel(Integer level, int minGrowth, Integer maxGrowth);

	/**
	 * 根据用户id列表，获取用户信息
	 * @param userIds
	 * @return
	 */
	List<UserApiVO> getUserByUserIds(List<Long> userIds);

	/**
	 * 校验用户是否存在
	 * @param phone
	 * @param unionId
	 * @return
	 */
	UserApiVO checkUserExist(String phone,String unionId);

	/**
	 * 根据用户id获取用户详细信息
	 * @param userId 用户id
	 * @return 用户详细信息
	 */
	UserApiVO getUserAndOpenIdsByUserId(Long userId);

    /**
     * 根据unionId获取用户详细信息
     * @param unionId
     * @return 用户详细信息
     */
    UserApiVO getUserAndOpenIdsByUserId(String unionId);

	/**
	 * 保存用户
	 * @param param 注册信息
	 * @param authSocial 社交账户信息
	 * @return uid
	 */
    Map<String,Long> save(UserRegisterDTO param, AuthSocialVO authSocial, User user);

	void update(UserRegisterDTO param, User user);

	/**
	 * 修改用户手机号
	 * @param userId 用户id
	 * @param mobile 手机号
	 */
	void updateUserMobile(Long userId, String mobile);

	/**
	 * 获取会员信息
	 * @param pageDTO 分页参数
	 * @param userManagerDTO 会员信息
	 * @return
	 */
    PageVO<UserManagerVO> getUserInfoPage(PageDTO pageDTO, UserManagerDTO userManagerDTO);

	/**
	 * 获取会员信息列表
	 * @param pageAdapter 分页参数
	 * @param userManagerDTO 会员信息
	 * @return 用户信息列表
	 */
//	List<UserManagerVO> getUserInfoList(PageDTO pageDTO, UserManagerDTO userManagerDTO);
	List<UserManagerVO> getUserInfoList(PageAdapter pageAdapter, UserManagerDTO userManagerDTO);


	/**
	 * 获用户信息
	 * @param userId 用户id
	 * @return
	 */
	UserManagerVO getUserInfo(Long userId);


	/**
	 * 将用户等级从用户扩展表同步到用户表里面
	 * @param userIds
	 */
	void updateUserLevelByUserExtensionAndUserIds(List<Long> userIds);

	/**
	 * 修改用户昵称、状态
	 * @param userDTO
	 */
    void updateUserInfo(UserDTO userDTO);

	/**
	 * 变更会员所属导购
	 * @param changeStaffDTO
	 */
	void changeUserStaff(ChangeStaffDTO changeStaffDTO);

					 /**
	 * 通过用户手机号查询多个用户信息
	 * @param phones 手机号列表
	 * @return 用户信息列表
	 */
    List<UserVO> getUserListByPhones(List<String> phones);

	/**
	 * 批量保存用户
	 * @param userList 用户信息集合
	 * @param userExtensionList 用户扩展信息列表
	 */
	void batchUser(List<User> userList, List<UserExtension> userExtensionList);

	/**
	 * 获取时间端类注册的用户
	 * @param status 用户状态
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 用户列表
	 */
    List<UserVO> getUserByCreateTimeRange(Integer status, Date startDate, Date endDate);

	/**
	 * 根据结束时间获取客户人数概况
	 * @param endTime
	 * @return
	 */
	MemberOverviewVO getUserCountInfo(Date endTime);

	/**
	 * 根据用户手机号查询用户数量
	 * @param mobile 手机号码
	 * @return 用户数量
	 */
	Integer countUserByMobile(String mobile);

	/**
	 * 根据等级查询属于该等级的用户数
	 * @param level 等级
	 * @param levelType 类型
	 * @return 用户数
	 */
	Integer countUserByLevel(Integer level,Integer levelType);

	/**
	 * 获取截至日期之前的所有付费会员信息
	 * @param endOfDay 截至日期
	 * @return
	 */
    List<UserVO> selectMemberByEndTime(DateTime endOfDay);

	/**
	 * 将会员的类型以及等级，重用户扩展表同步掉user表
	 * @param userIds 用ids
	 */
	void updateUserTypeLevelByUserExtensionAndUserIds(List<Long> userIds);

	PageVO<UserApiVO> pageUserByStaff(PageDTO pageDTO, UserQueryDTO queryDTO);

	PageVO<UserApiVO> pageCouponUserByStaff(PageDTO pageDTO, QueryHasCouponUsersRequest queryHasCouponUsersRequest);

	List<UserApiVO> listUserByStaff(UserQueryDTO queryDTO);

    int countStaffUser(UserQueryDTO queryDTO);

	List<StaffUserVo> listStaffUser(PageDTO pageDTO, UserQueryDTO queryDTO);

	UserApiVO getUserByMobile(String mobile);

	List<UserApiVO> getUserByStoreId(Long storeId);

	List<UserApiVO> getUserByStaffId(Long staffId);

	UserApiVO getUserByVipCode(String vipCode);

	DistributionUserVO countUserNum(DistributionUserQueryDTO distributionUserQueryDTO);

	List<UserBindStaffDTO> bindStaff(UserBindStaffDTO userBindStaffDTO);

	List<Long> unBindStaffByStaffId(Long staffId);

	List<Long> findUserIdListByStaffId(Long staffId);

	ServerResponseEntity<Void> updateUserScore(UpdateScoreDTO param);

	ServerResponseEntity<Void> logoutUser(Long userId);

	ServerResponseEntity<Void> deleteUserAddr(Long userId);

    Long saveTest(UserRegisterDTO param);

    String setBorrowLivingRoomId(String openId, String livingRoomId);

    String getBorrowLivingRoomId(Long userId);

    List<User> getAllVipCodeIsNullUser();


	void updateUnionId(Long userId,String unionId);

	List<Long> getBirthdayUser();

	void changeServiceStore(UserChangeServiceStoreDTO userChangeServiceStoreDTO);

    void fixUserServiceStore(List<User> users);

    List<UserApiVO> findWeekerByKeyword(String keyword);

	/**
	 * 导购筛选查询我的会员
	 * @param staffSelectUserDTO 导购筛选查询我的会员入参
	 * @return
	 */
	PageVO<UserApiVO> staffSelectUser(StaffSelectUserDTO staffSelectUserDTO);

	/**
	 * 导购端群发任务页面导购筛选查询我的会员
	 * @param staffSelectUserForTaskDTO 导购端群发任务页面导购筛选查询我的会员
	 * @return
	 */
	PageVO<UserApiVO> staffScreenUserForTask(StaffSelectUserForTaskDTO staffSelectUserForTaskDTO);

	/**
	 * 素材中心场景-导购获取用户信息
	 * @param userId 用户ID
	 * @return
	 */
	StaffGetUserDetailByMaterialVO staffGetUserDetailByMaterialVO(Long userId);


	/**
	 * 获取动态会员码
	 * @param userId
	 * @return
	 */
	ServerResponseEntity<UserDynamicCodeVO> getDynamicCode(Long userId);

	/**
	 * 通过POS端动态会员码获取vipcode
	 * @param dynamicCodeDTO
	 * @return
	 */
	ServerResponseEntity<String> decryptDynamicCode(UserDynamicCodeDTO dynamicCodeDTO);

	/**
	 * 是否开启rec推荐
	 * @param userId
	 * @return
	 */
    boolean isRecEnabled(Long userId);

	void recSwitch(Long userId);

	/**
	 * 根据unionId列表，获取用户信息
	 * @param unionIdList
	 * @return
	 */
	List<UserApiVO> getUserByUnionIdList(List<String> unionIdList);

	/**
	 * 清除会员导购信息
	 * @param userId 会员ID
	 */
	void cleanUserBindStaff(Long userId);

	ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> getScoreDetail(Integer pageNo, Integer pageSize, Long userId);

}
