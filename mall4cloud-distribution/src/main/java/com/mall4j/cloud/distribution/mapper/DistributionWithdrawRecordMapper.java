package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 佣金管理-佣金提现记录
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
public interface DistributionWithdrawRecordMapper {

	/**
	 * 获取佣金管理-佣金提现记录列表
	 * @return 佣金管理-佣金提现记录列表
	 */
	List<DistributionWithdrawRecordVO> list(@Param("distributionWithdrawRecordDTO") DistributionWithdrawRecordDTO distributionWithdrawRecordDTO);

	/**
	 * 根据佣金管理-佣金提现记录id获取佣金管理-佣金提现记录
	 *
	 * @param id 佣金管理-佣金提现记录id
	 * @return 佣金管理-佣金提现记录
	 */
	DistributionWithdrawRecord getById(@Param("id") Long id);

	/**
	 * 保存佣金管理-佣金提现记录
	 * @param distributionWithdrawRecord 佣金管理-佣金提现记录
	 */
	void save(@Param("distributionWithdrawRecord") DistributionWithdrawRecord distributionWithdrawRecord);

	/**
	 * 更新佣金管理-佣金提现记录
	 * @param distributionWithdrawRecord 佣金管理-佣金提现记录
	 */
	void update(@Param("distributionWithdrawRecord") DistributionWithdrawRecord distributionWithdrawRecord);

	/**
	 * 根据佣金管理-佣金提现记录id删除佣金管理-佣金提现记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据申请时间范围查询提现记录
	 */
	List<DistributionWithdrawRecord> listWithdrawRecordByTime(@Param("identityType") Integer identityType, @Param("userId") Long userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    DistributionWithdrawRecord getRecordByNo(@Param("withdrawOrderNo") String withdrawOrderNo);

	List<DistributionWithdrawRecord> listWithdrawRecordByStatus(@Param("status") Integer status);

    DistributionWithdrawRecord getByApplyId(@Param("applyId") String applyId);

    boolean isExistWithdrawFailRecord(@Param("userId") Long userId, @Param("identityType") Integer identityType);
}
