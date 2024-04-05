package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.MomentsSendRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import com.mall4j.cloud.biz.vo.cp.DistributionMomentsSendRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 朋友圈 员工发送记录表
 *
 * @author FrozenWatermelon
 * @date 2023-11-03 14:22:45
 */
public interface DistributionMomentsSendRecordMapper {

	/**
	 * 获取朋友圈 员工发送记录表列表
	 * @return 朋友圈 员工发送记录表列表
	 */
	List<DistributionMomentsSendRecord> list();
	/**
	 * 获取朋友圈 员工发送记录表列表
	 * @return 朋友圈 员工发送记录表列表
	 */
	List<DistributionMomentsSendRecordVO> page(@Param("request")MomentsSendRecordPageDTO request);

	/**
	 * 根据朋友圈 员工发送记录表id获取朋友圈 员工发送记录表
	 *
	 * @param id 朋友圈 员工发送记录表id
	 * @return 朋友圈 员工发送记录表
	 */
	DistributionMomentsSendRecord getById(@Param("id") Long id);

	/**
	 * 保存朋友圈 员工发送记录表
	 * @param distributionMomentsSendRecord 朋友圈 员工发送记录表
	 */
	void save(@Param("distributionMomentsSendRecord") DistributionMomentsSendRecord distributionMomentsSendRecord);

	/**
	 * 更新朋友圈 员工发送记录表
	 * @param distributionMomentsSendRecord 朋友圈 员工发送记录表
	 */
	void update(@Param("distributionMomentsSendRecord") DistributionMomentsSendRecord distributionMomentsSendRecord);

	/**
	 * 根据朋友圈 员工发送记录表id删除朋友圈 员工发送记录表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    void deleteByMomentId(@Param("momentId") Long momentId);

	/**
	 * 根据朋友圈id和导购id查询发送记录
	 * @param momentsId
	 * @param staffId
	 * @return
	 */
    DistributionMomentsSendRecord getByMomentIdAndStaffId(@Param("momentsId")Long momentsId,@Param("staffId") Long staffId);

	void doSend(@Param("id")Long id,@Param("jobId") String jobId);

	List<DistributionMomentsSendRecord> getMomentTaskResult();

	void publish(@Param("qiwei_user_id")String qiwei_user_id,@Param("momentsId") Long momentsId);

	List<DistributionMomentsSendRecord> getMomentCommentsList();

	void updateMomentComment(@Param("id")Long id, @Param("commentNum")int commentNum, @Param("likeNum")int likeNum);

    List<DistributionMomentsSendRecord> getByMomentId(@Param("momentsId")Long momentsId);

    void sendSccess(@Param("momentsId")Long momentsId,@Param("sendSccessIds")List<Long> sendSccessIds);
}
