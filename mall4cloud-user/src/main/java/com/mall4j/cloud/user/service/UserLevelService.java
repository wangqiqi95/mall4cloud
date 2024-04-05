package com.mall4j.cloud.user.service;

import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserLevelDTO;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserLevelLog;
import com.mall4j.cloud.user.vo.UserLevelVO;
import com.mall4j.cloud.user.vo.UserVO;

import java.util.List;

/**
 * 会员等级表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserLevelService {

	/**
	 * 根据用户等级类型，获取会员等级表列表
	 *
	 * @param levelType 等级类型
	 * @return 会员等级表列表数据
	 */
	List<UserLevelVO> list(Integer levelType);

	/**
	 * 根据会员等级表id获取会员等级表
	 *
	 * @param userLevelId 会员等级表id
	 * @return 会员等级表
	 */
	UserLevelVO getByUserLevelId(Long userLevelId);

	/**
	 * 保存会员等级表
	 *
	 * @param userLevelDTO 会员等级数据
	 */
	void save(UserLevelDTO userLevelDTO);

	/**
	 * 更新会员等级表
	 *
	 * @param userLevelDTO 会员等级表
	 */
	void update(UserLevelDTO userLevelDTO);

	/**
	 * 根据会员等级表id删除会员等级表
	 *
	 * @param userLevelId 会员等级表id
	 */
	void deleteByUserLevelId(Long userLevelId);

	/**
	 * 更新用户会员等级
	 */
	void updateUserLevel();

	/**
	 * 批量修改会员成长值
	 *
	 * @param userAdminDTO 会员等级信息
	 */
	void batchUpdateGrowth(UserAdminDTO userAdminDTO);

	/**
	 * 批量修改会员积分
	 *
	 * @param userAdminDTO 会员等级信息
	 */
	void batchUserScore(UserAdminDTO userAdminDTO);

	/**
	 * 等级提升
	 *
	 * @param userLevels   多个等级，奖励发放时
	 * @param userLevelLog 等级日志,在购买付费会员时存在
	 * @param user         用户详细信息
	 * @param phone         用户手机号码
	 */
	void levelUp(List<UserLevelVO> userLevels, UserLevelLog userLevelLog, UserExtension user, String phone);

	/**
	 * 用户信息初始化
	 *  @param userExtension 用户扩展信息
	 * @param level         等级
     * @param levelType     等级类型
     * @param phone
     */
	void initUserInfoAndLevelInfo(UserExtension userExtension, Integer level, Integer levelType, String phone);

	/**
	 * 保存or修改会员等级表
	 *
	 * @param userLevelDTO 会员等级数据
	 */
	void saveOrUpdate(UserLevelDTO userLevelDTO);

	/**
	 * 根据会员类型与会员等级获取会员等级数据
	 *
	 * @param levelType 会员类型
	 * @param level     会员等级
	 * @return 会员等级数据
	 */
	UserLevelVO getOneByTypeAndLevel(Integer levelType, Integer level);

	/**
	 * 删除会员等级缓存
	 *
	 * @param userLevelId 会员等级表id
	 * @param levelType   会员类型
	 * @param level       会员等级
	 */
	void removeLevelCache(Long userLevelId, Integer levelType, Integer level);

	/**
	 * 删除会员等级列表缓存
	 * @param levelType
	 */
	void removeLevelListCache(Integer levelType);

	/**
	 * 删除单个会员等级缓存
	 * @param userLevelId 会员等级id
	 */
	void removeLevelById(Long userLevelId);
	/**
	 * 根据成长值提升/降低用户等级
	 * 只修改传入 levelType 的用户等级
	 * @param userIds 用户id列表
	 * @param levelType 会员类型必填，
	 * @return 每个会员等级变更日志
	 */
	List<UserLevelLog> batchLevelUpByGrowthAndUserIds(List<Long> userIds, Integer levelType);

	/**
	 * 增加用户成长值和积分
	 *
	 * @param growthPrice 增加的成长值
	 * @param score    增加的积分
	 * @param userId    用户id
	 * @param bizId    bizId
	 * @param userExtension    用户扩展信息
	 * @param type    1:等级 2：余额充值
	 */
	void addGrowthAndScore(double growthPrice, Long score, Long userId, Long bizId, UserExtension userExtension, Integer type);

	/**
	 * 订单退款后，成长值也需要回退的
	 * @param orderId 订单号
	 */
	void refundGrowth(Long orderId);

	/**
	 * 获取当前类型的最大等级
	 * @param levelType 等级类型
	 * @return 最大等级
	 */
	int getMaxLevel(Integer levelType);

	/**
	 * 获取当前用户作为普通会员的等级
	 * @param growth 用户当前成长值
	 * @return
	 */
	int getUserNormalLevel(Integer growth);

	/**
	 * 将付费会员变成普通会员
	 * 会员等级直接变成普通会员的成长值对应的等级
	 * @param users 付费会员用户
	 */
    void expireVipUsers(List<UserVO> users);

	/**
	 * 付费会员等级修改招募状态
	 * @param userLevelDTO 会员等级
	 */
    void updateRecruitStatus(UserLevelDTO userLevelDTO);

	/**
	 * 赠送注册积分
	 * @param userExtensionList 用户扩展信息集合
	 */
    void registerUserScore(List<UserExtension> userExtensionList);

	/**
	 * 导入用户发放对应等级的权益
	 * @param userExtensionList  用户扩展信息集合
	 */
    void registerUserSendRights(List<UserExtension> userExtensionList);
}
