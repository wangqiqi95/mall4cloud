package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.MomentsSendRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsSendRecordVO;
import com.mall4j.cloud.biz.vo.cp.MomentSendRecordExcelVO;
import com.mall4j.cloud.biz.vo.cp.MomentSendRecordPageVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 朋友圈 员工发送记录表
 *
 * @author FrozenWatermelon
 * @date 2023-11-03 14:22:45
 */
public interface DistributionMomentsSendRecordService {

	/**
	 * 分页获取朋友圈 员工发送记录表列表
	 * @param pageDTO 分页参数
	 * @return 朋友圈 员工发送记录表列表分页数据
	 */
	MomentSendRecordPageVO page(PageDTO pageDTO, MomentsSendRecordPageDTO request);

	/**
	 * 根据朋友圈 员工发送记录表id获取朋友圈 员工发送记录表
	 *
	 * @param id 朋友圈 员工发送记录表id
	 * @return 朋友圈 员工发送记录表
	 */
	DistributionMomentsSendRecord getById(Long id);

	/**
	 * 保存朋友圈 员工发送记录表
	 * @param distributionMomentsSendRecord 朋友圈 员工发送记录表
	 */
	void save(DistributionMomentsSendRecord distributionMomentsSendRecord);

	/**
	 * 更新朋友圈 员工发送记录表
	 * @param distributionMomentsSendRecord 朋友圈 员工发送记录表
	 */
	void update(DistributionMomentsSendRecord distributionMomentsSendRecord);

	/**
	 * 根据朋友圈 员工发送记录表id删除朋友圈 员工发送记录表
	 * @param id 朋友圈 员工发送记录表id
	 */
	void deleteById(Long id);

	/**
	 * 根据朋友圈id删除发送记录
	 * @param id
	 */
	void deleteByMomentId(Long id);

	DistributionMomentsSendRecord getByMomentIdAndStaffId(Long momentsId, Long staffId);

	void doSend(Long id, String jobId);


	List<DistributionMomentsSendRecord> getMomentTaskResult();

//	void publish(Long id, String momentsId);

	List<DistributionMomentsSendRecord> getMomentCommentsList();

	void updateMomentComment(Long id, int commentNum, int likeNum);

	List<MomentSendRecordExcelVO> orderSoldExcelList(PageDTO pageDTO, MomentsSendRecordPageDTO request);

    List<DistributionMomentsSendRecord> getByMomentId(Long id);


}
