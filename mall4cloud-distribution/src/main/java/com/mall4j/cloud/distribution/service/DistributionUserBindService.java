package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserBindDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.model.DistributionUserBind;
import com.mall4j.cloud.distribution.vo.DistributionUserBindInfoVO;
import com.mall4j.cloud.distribution.vo.DistributionUserBindVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import org.apache.poi.ss.formula.functions.T;

/**
 * 分销员绑定关系
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public interface DistributionUserBindService {

	/**
	 * 分页获取分销员绑定关系列表
	 * @param pageDTO 分页参数
	 * @param distributionUserBindDTO
	 * @return 分销员绑定关系列表分页数据
	 */
	PageVO<DistributionUserBindVO> page(PageDTO pageDTO, DistributionUserBindDTO distributionUserBindDTO);

	/**
	 * 根据分销员绑定关系id获取分销员绑定关系
	 *
	 * @param bindId 分销员绑定关系id
	 * @return 分销员绑定关系
	 */
	DistributionUserBind getByBindId(Long bindId);

	/**
	 * 保存分销员绑定关系
	 * @param distributionUserBind 分销员绑定关系
	 */
	void save(DistributionUserBind distributionUserBind);

	/**
	 * 更新分销员绑定关系
	 * @param distributionUserBind 分销员绑定关系
	 */
	void update(DistributionUserBind distributionUserBind);

	/**
	 * 根据分销员绑定关系id删除分销员绑定关系
	 * @param bindId 分销员绑定关系id
	 */
	void deleteById(Long bindId);

	/**
	 * 根据分享人的卡号，判断该用户是否能与该分享人进行绑定
	 * @param shareUser 分享人
	 * @param userId
	 * @param type 0 扫码 1 下单
	 * @return
	 */
    ServerResponseEntity<DistributionUser> bindDistribution(DistributionUser shareUser, Long userId, int type);

	/**
	 * 获取绑定用户的列表
	 * @param pageDTO 分页信息
	 * @param userId 用户id
	 * @return 绑定用户的列表
	 */
	PageVO<DistributionUserBindInfoVO> bindUserList(PageDTO pageDTO, Long userId);

	/**
	 * 获取分销员绑定关系
	 * @param userId 用户id
	 * @param state  当前绑定关系状态
	 * @return
	 */
	DistributionUserBind getUserBindByUserIdAndState(Long userId, Integer state);
}
