package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionUserDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.vo.AchievementDataVO;
import com.mall4j.cloud.distribution.vo.DistributionUserAchievementVO;
import com.mall4j.cloud.distribution.vo.DistributionUserSimpleInfoVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销员信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionUserMapper {

	/**
	 * 获取分销员管理列表
	 * @param distributionUserDTO 搜索参数
	 * @return 分销员信息列表
	 */
	List<DistributionUserVO> listDistributionUser(@Param("distributionUser") DistributionUserDTO distributionUserDTO);

	/**
	 * 根据分销员信息id获取分销员信息
	 *
	 * @param distributionUserId 分销员信息id
	 * @return 分销员信息
	 */
	DistributionUser getByDistributionUserId(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 保存分销员信息
	 * @param distributionUser 分销员信息
	 */
	void save(@Param("distributionUser") DistributionUser distributionUser);

	/**
	 * 更新分销员信息
	 * @param distributionUser 分销员信息
	 */
	void update(@Param("distributionUser") DistributionUser distributionUser);

	/**
	 * 根据分销员信息id删除分销员信息
	 * @param distributionUserId
	 */
	void deleteById(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 分页获取分销业绩统计信息
	 * @param distributionUserDTO 分销员信息
	 * @param userMobile 分销员手机号
	 * @return
	 */
    List<DistributionUserAchievementVO> achievementPage(@Param("distributionUserDTO") DistributionUserDTO distributionUserDTO, @Param("userMobile") String userMobile);

    /**
	 * 根据分销员卡号获取分销员信息
	 * @param cardNo 卡号
	 * @return 分销员信息
	 */
    DistributionUserVO getByCardNo(@Param("cardNo") String cardNo);

	/**
	 * 根据分销员userId获取分销员信息
	 * @param userId 用户id
	 * @return 分销员信息
	 */
    DistributionUserVO getByUserId(@Param("userId") Long userId);

	/**
	 * 根据分销员id清除他的下级关系
	 * @param distributionUserId 被永久封禁的分销员id
	 */
	void updateParentIdById(@Param("distributionUserId")Long distributionUserId);

	/**
	 * 修改分销员状态
	 * @param distributionUser 修改信息
	 */
	void updateStatus(@Param("distributionUser") DistributionUser distributionUser);

	/**
	 * 获取分销员列表
	 * @param identityCardNumber 分销员身份证信息
	 * @param userMobile 手机号
	 * @return 分销员列表
	 */
    List<DistributionUser> getDistributionUserByIdCardNumberAndUserMobile(@Param("identityCardNumber") String identityCardNumber, @Param("userMobile") String userMobile);

    /**
	 * 分销员业绩数据
	 * @param distributionUserId 分销员id
	 * @return 分销员业绩数据
	 */
    AchievementDataVO getAchievementDataById(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 获取精简版分销员数据列表
	 * @param parentDistributionUserId 上级分销员id
	 * @return 精简版分销员分页数据
	 */
    List<DistributionUserSimpleInfoVO> getPageDistributionUserSimpleInfoByParentUserId(@Param("parentDistributionUserId") Long parentDistributionUserId);
}
