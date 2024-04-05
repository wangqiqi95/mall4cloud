package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.StaffBatchSendCpMsgVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-03-18
 */
public interface StaffBatchSendCpMsgMapper extends BaseMapper<StaffBatchSendCpMsg> {

    List<StaffBatchSendCpMsgVO> getCpDataBySendStatus();

}
