package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionUserDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.vo.*;

import java.util.List;

/**
 * 分销员信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionUserService {

	/**
	 * 分页获取分销员管理列表
	 * @param pageDTO 分页参数
	 * @param distributionUserDTO
	 * @return 分销员信息管理分页数据
	 */
	PageVO<DistributionUserVO> distributionUserPage(PageDTO pageDTO, DistributionUserDTO distributionUserDTO);

	/**
	 * 根据分销员信息id获取分销员信息
	 *
	 * @param distributionUserId 分销员信息id
	 * @return 分销员信息
	 */
	DistributionUser getByDistributionUserId(Long distributionUserId);

	/**
	 * 保存分销员信息
	 * @param distributionUser 分销员信息
	 */
	void save(DistributionUser distributionUser);

	/**
	 * 更新分销员信息
	 * @param distributionUser 分销员信息
	 */
	void update(DistributionUser distributionUser);

	/**
	 * 根据分销员信息id删除分销员信息
	 * @param distributionUserId 分销员信息id
	 */
	void deleteById(Long distributionUserId);

	/**
	 * 分页获取分销业绩统计信息
	 * @param pageDTO 分页参数
	 * @param distributionUserDTO 分销员信息
	 * @param userMobile 分销员手机号
	 * @return
	 */
	PageVO<DistributionUserAchievementVO> achievementPage(PageDTO pageDTO, DistributionUserDTO distributionUserDTO, String userMobile);

	/**
	 * 获取分销员用户信息
	 * @param userId 用户id
	 * @return 分销员用户信息
	 */
	DistributionUserVO getByUserId(Long userId);

	/**
	 * 移除根据分销员userId获取分销员信息的缓存
	 * @param userId 用户id
	 */
	void removeCacheByUserId(Long userId);


	/**
	 * 更新分销员状态以及封禁记录
	 * @param distributionUserBan 插入参数
	 */
	void updateDistributionStateAndBan(DistributionUserBan distributionUserBan);

	/**
	 * 获取分销员列表
	 * @param identityCardNumber 分销员身份证信息
	 * @param userMobile 手机号
	 * @return 分销员列表
	 */
    List<DistributionUser> getDistributionUserByIdCardNumberAndUserMobile(String identityCardNumber, String userMobile);

    /**
	 * 申请注册成为分销员
	 * @param distributionUser 分销员信息
	 */
	void registerDistributionUser(DistributionUser distributionUser);

	/**
	 * 分销员业绩数据
	 * @param distributionUserId 分销员id
	 * @return 分销员业绩数据
	 */
    AchievementDataVO getAchievementDataById(Long distributionUserId);

	/**
	 * 分页获取精简版分销员数据
	 * @param pageDTO 分页参数
	 * @param parentDistributionUserId 上级分销员id
	 * @return 精简版分销员分页数据
	 */
    PageVO<DistributionUserSimpleInfoVO> getPageDistributionUserSimpleInfoByParentUserId(PageDTO pageDTO, Long parentDistributionUserId);

	/**
	 * 根据分销员id获取最新的封禁信息
	 * @param distributionUserId
	 * @return
	 */
	DistributionUserBanVO getLatestBanInfoByDistributionUserId(Long distributionUserId);
}
