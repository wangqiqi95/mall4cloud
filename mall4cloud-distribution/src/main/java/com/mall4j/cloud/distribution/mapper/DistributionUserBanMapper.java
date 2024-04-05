package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionUserBanDTO;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.vo.DistributionUserBanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销封禁记录
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
public interface DistributionUserBanMapper {

	/**
	 * 获取分销封禁记录列表
	 * @param distributionUserBanDTO
	 * @return 分销封禁记录列表
	 */
	List<DistributionUserBanVO> list(@Param("distributionUserBanDTO") DistributionUserBanDTO distributionUserBanDTO);

	/**
	 * 根据分销封禁记录id获取分销封禁记录
	 *
	 * @param banId 分销封禁记录id
	 * @return 分销封禁记录
	 */
	DistributionUserBan getByBanId(@Param("banId") Long banId);

	/**
	 * 保存分销封禁记录
	 * @param distributionUserBan 分销封禁记录
	 */
	void save(@Param("distributionUserBan") DistributionUserBan distributionUserBan);

	/**
	 * 更新分销封禁记录
	 * @param distributionUserBan 分销封禁记录
	 */
	void update(@Param("distributionUserBan") DistributionUserBan distributionUserBan);

	/**
	 * 根据分销封禁记录id删除分销封禁记录
	 * @param banId
	 */
	void deleteById(@Param("banId") Long banId);

	/**
	 * 根据分销员id获取最新的封禁信息
	 * @param distributionUserId
	 * @return
	 */
    DistributionUserBanVO getLatestBanInfoByDistributionUserId(@Param("distributionUserId") Long distributionUserId);
}
