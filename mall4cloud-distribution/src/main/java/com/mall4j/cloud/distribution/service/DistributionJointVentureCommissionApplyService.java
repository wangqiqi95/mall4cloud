package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderSearchDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionApplyDTO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionSearchDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApply;

import javax.servlet.http.HttpServletResponse;

/**
 * 联营分佣申请service
 *
 * @author Zhang Fan
 * @date 2022/8/5 15:07
 */
public interface DistributionJointVentureCommissionApplyService {

    /**
     * 获取联营分佣申请列表
     *
     * @param pageDTO
     * @param searchDTO
     * @return
     */
    PageVO<DistributionJointVentureCommissionApply> page(PageDTO pageDTO, DistributionJointVentureCommissionSearchDTO searchDTO);

    /**
     * 根据id获取联营分佣申请
     *
     * @param id
     * @return
     */
    DistributionJointVentureCommissionApply getById(Long id);

    /**
     * 发起联营分佣申请
     *
     * @param distributionJointVentureCommissionApplyDTO
     */
    void launchApply(DistributionJointVentureCommissionApplyDTO distributionJointVentureCommissionApplyDTO);

    /**
     * 发起联营分佣申请
     *
     * @param searchDTO
     */
    void launchApply(DistributionJointVentureOrderSearchDTO searchDTO);

    void launchApplyV2(DistributionJointVentureOrderSearchDTO searchDTO);

    /**
     * 审核联营分佣申请
     *
     * @param id
     * @param status 1成功 9失败
     */
    void auditApply(Long id, Integer status);

    void updateApplyBankCard(String applyId, String cardNo);

    void exportExcel(HttpServletResponse response, DistributionJointVentureCommissionSearchDTO searchDTO);

    void exportExcelInDownloadCenter(DistributionJointVentureCommissionSearchDTO searchDTO);
}
