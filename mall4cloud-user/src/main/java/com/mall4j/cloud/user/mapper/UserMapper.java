package com.mall4j.cloud.user.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.MemberOverviewVO;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserManagerVO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.vo.UserVO;
import com.mall4j.cloud.user.vo.VeekerVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 获取用户表列表
	 *
	 * @return 用户表列表
	 */
	List<UserVO> listUser();

	/**
	 * 根据用户表id获取用户表
	 *
	 * @param userId 用户表id
	 * @return 用户表
	 */
	UserApiVO getByUserId(@Param("userId") Long userId);

	/**
	 * 根据用户表unionId获取用户表
	 * @param unionId
	 * @return
	 */
	UserApiVO getByUnionId(@Param("unionId") String unionId);

	UserApiVO getByOpenId(@Param("openId") String openId);

	/**
	 * 保存用户表
	 *
	 * @param user 用户表
	 */
	void saveUser(@Param("user") User user);

	/**
	 * 更新用户表
	 *
	 * @param user 用户表
	 */
	void updateUser(@Param("user") User user);

	/**
	 * 更新用户表
	 *
	 * @param user 用户表
	 */
	long updateByCrm(@Param("user") User user);


	/**
	 * 根据成长值范围，更新普通用户的等级
	 *
	 * @param level     更新至该等级
	 * @param minGrowth 最小成长值
	 * @param maxGrowth 最大成长值
	 */
    void updateUserLevel(@Param("level") Integer level, @Param("minGrowth") int minGrowth, @Param("maxGrowth") Integer maxGrowth);

	/**
	 * 根据用户id列表，获取用户信息
	 *
	 * @param userIds
	 * @return
	 */
	List<UserApiVO> getUserByUserIds(@Param("userIds") List<Long> userIds);

	UserApiVO checkUserExist(@Param("phone") String phone,@Param("unionId") String unionId);



	/**
	 * 根据查询用户的条件，查询用户数量
	 *
	 * @param userManagerDTO 用户查询条件
	 * @return 数量
	 */
    Long countUserPageByParam(@Param("userManagerDTO") UserManagerDTO userManagerDTO);

	/**
	 * 根据查询条件，获取会员信息
	 *
	 * @param userManagerDTO 用户查询条件
	 * @param pageAdapter
	 * @return 会员管理的会员信息列表
	 */
	List<UserManagerVO> listUserByParam(@Param("userManagerDTO") UserManagerDTO userManagerDTO, @Param("pageAdapter") PageAdapter pageAdapter);

	/**
	 * 将用户等级从用户扩展表同步到用户表里面
	 *
	 * @param userIds
	 */
    void updateUserLevelByUserExtensionAndUserIds(@Param("userIds") List<Long> userIds);

	/**
	 * 更新用户等级，通过购买会员
	 *
	 * @param userId     用户id
	 * @param afterLevel 购买后的等级
	 * @param date       会员过期时间
	 */
    void updateUserLevelByBuyVip(@Param("userId") Long userId, @Param("afterLevel") Integer afterLevel, @Param("date") Date date);

	/**
	 * 通过用户手机号查询多个用户信息
	 * @param phones 手机号列表
	 * @return 用户信息列表
	 */
    List<UserVO> getUserListByPhones(@Param("phones") List<String> phones);

	/**
	 * 批量保存用户
	 * @param users 用户列表
	 * @return 影响行数
	 */
    int saveBatch(@Param("users") List<User> users);

	/**
	 * 获取时间端类注册的用户
	 * @param status 用户状态
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 用户列表
	 */
    List<UserVO> getUserByCreateTimeRange(@Param("status") Integer status, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 根据时间范围获取会员人数统计信息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	MemberOverviewVO getUserCountInfo(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据手机号码查用户数量
	 * @param mobile 手机号码
	 * @return 数量
	 */
	int countUserByMobile(@Param("mobile") String mobile);

	/**
	 * 根据等级查询属于该等级的用户数
	 * @param level 等级
	 * @param levelType 类型
	 * @return 用户数
	 */
	int countUserByLevel(@Param("level") Integer level,@Param("levelType") Integer levelType);

	/**
	 * 获取截至日期之前的所有付费会员信息
	 * @param endOfDay 截至日期
	 * @return
	 */
    List<UserVO> selectMemberByEndTime(@Param("endOfDay") DateTime endOfDay);

	/**
	 * 将会员的类型以及等级，重用户扩展表同步掉user表
	 * @param userIds 用ids
	 * @return
	 */
    int updateUserTypeLevelByUserExtensionAndUserIds(@Param("userIds") List<Long> userIds);

	/**
	 * 分页查询微客列表
	 * @param queryDTO
	 * @param pageAdapter
	 * @return
	 */
	List<VeekerVO> listVeekerByParam(@Param("queryDTO") VeekerQueryDTO queryDTO, @Param("pageAdapter") PageAdapter pageAdapter);

	/**
	 * 统计微客数量
	 * @param queryDTO 微客查询条件
	 * @return 数量
	 */
	Long countVeekerPageByParam(@Param("queryDTO") VeekerQueryDTO queryDTO);


	/**
	 * 批量更新微客状态
	 *
	 * @param userIds
	 * @param veekerStatus
	 */
	void batchUpdateVeekerStatusByUserIds(@Param("userIds") List<Long> userIds, @Param("veekerStatus") Integer veekerStatus);

	/**
	 * 批量更新微客审核状态
	 *
	 * @param userIds
	 * @param veekerAuditStatus
	 */
	void batchUpdateVeekerAuditStatusByUserIds(@Param("userIds") List<Long> userIds, @Param("veekerAuditStatus") Integer veekerAuditStatus);

	/**
	 * 微客解绑导购
	 *
	 * @param userId
	 */
	void unbindStaff(@Param("userId") Long userId);

    List<UserApiVO> listByStaff(@Param("queryDTO") UserQueryDTO queryDTO);

	UserApiVO getUserByMobile(@Param("mobile") String mobile);

	List<UserApiVO> getUserByStoreId(@Param("storeId") Long storeId);

	List<UserApiVO> getUserByStaffId(@Param("staffId") Long staffId);

	UserApiVO getUserByVipCode(@Param("vipCode") String vipCode);

	void batchBindStaff(@Param("userIds") List<Long> userIds, @Param("staffId") Long staffId);

	void updateVipcodeByUserId(@Param("userId") Long userId,@Param("vipcode") String vipcode);

	void updateUnionIdByUserId(@Param("userId") Long userId,@Param("unionId") String unionId);

	List<Long> getBirthdayUser();

	List<UserApiVO> findWeekerByKeyword(@Param("keyword") String keyword);

	// 群发任务场景，导购查询当前主任务下的触达用户信息
	// List<UserApiVO> staffScreenUserForTask(@Param("dto") StaffSelectUserForTaskDTO staffSelectUserForTaskDTO);
	IPage<UserApiVO> staffScreenUserForTask(@Param("page") IPage<UserApiVO> page, @Param("dto") StaffSelectUserForTaskDTO staffSelectUserForTaskDTO);

	// 标签场景，导购查询用户信息
	List<UserApiVO> staffScreenUserForTag(@Param("dto") StaffSelectUserDTO staffSelectUserDTO);

	/**
     * 查询重置表最大的手机号
     */
    String findMaxPhone();

    /**
     * 插入临时表
     */
    int insertRemoveLog(@Param("maxPhone") String maxPhone, @Param("userId") Long userId);

    /**
     * 更新user表
     */
    int updLogoutUser(@Param("maxPhone") String maxPhone, @Param("userId") Long userId);

    /**
     * 更新Account表
     */
    int updLogoutAccount(@Param("maxPhone") String maxPhone, @Param("userId") Long userId);

    /**
     * 更新Social表
     */
    int updLogoutSocial(@Param("maxPhone") String maxPhone, @Param("userId") Long userId);

     /**
     * 删除用户收件地址
     */
    int deleteUserAddr(@Param("userId") Long userId);

}
