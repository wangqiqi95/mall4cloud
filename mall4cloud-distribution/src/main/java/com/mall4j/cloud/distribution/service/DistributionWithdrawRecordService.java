package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawRecordVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 佣金管理-佣金提现记录
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
public interface DistributionWithdrawRecordService {

    /**
     * 分页获取佣金管理-佣金提现记录列表
     *
     * @param pageDTO 分页参数
     * @return 佣金管理-佣金提现记录列表分页数据
     */
    PageVO<DistributionWithdrawRecordVO> page(PageDTO pageDTO, DistributionWithdrawRecordDTO distributionWithdrawRecordDTO);

    /**
     * 根据佣金管理-佣金提现记录id获取佣金管理-佣金提现记录
     *
     * @param id 佣金管理-佣金提现记录id
     * @return 佣金管理-佣金提现记录
     */
    DistributionWithdrawRecord getById(Long id);

    /**
     * 保存佣金管理-佣金提现记录
     *
     * @param distributionWithdrawRecord 佣金管理-佣金提现记录
     */
    void save(DistributionWithdrawRecord distributionWithdrawRecord);

    /**
     * 更新佣金管理-佣金提现记录
     *
     * @param distributionWithdrawRecord 佣金管理-佣金提现记录
     */
    void update(DistributionWithdrawRecord distributionWithdrawRecord);

    /**
     * 根据佣金管理-佣金提现记录id删除佣金管理-佣金提现记录
     *
     * @param id 佣金管理-佣金提现记录id
     */
    void deleteById(Long id);

    /**
     * 申请提现
     *
     * @param distributionWithdrawRecordDTO 申请提现信息
     */
    void applyWithdraw(DistributionWithdrawRecordDTO distributionWithdrawRecordDTO);

    /**
     * 处理提现申请
     *
     * @param distributionWithdrawRecordDTO  提现审核
     */
    Boolean processWithdraw(DistributionWithdrawRecordDTO distributionWithdrawRecordDTO);

    void successAfterWithdraw(DistributionWithdrawRecord record);

    /**
     * 根据申请时间范围查询提现记录
     */
    List<DistributionWithdrawRecord> listWithdrawRecordByTime(Integer identityType, Long userId, Date startTime, Date endTime);


    DistributionWithdrawRecord getRecordByNo(String withdrawOrderNo);

    void withdrawRecordExcel(HttpServletResponse response, DistributionWithdrawRecordDTO dto);

    /**
     * 根据申请时间范围查询提现记录
     */
    List<DistributionWithdrawRecord> listWithdrawRecordByStatus(Integer status);

    void applyHistoryWithdraw(DistributionWithdrawRecordDTO distributionWithdrawRecordDTO);

    Long getWithdrawNeedRefundAmount(Integer identityType, Long userId);

    void updateWithdrawRecordBankCard(String applyId, String cardNo);
}
