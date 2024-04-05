package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.api.order.dto.DistributionJointVentureOrderSearchDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionApplyDTO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionSearchDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApply;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionApplyService;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 联营分佣申请controller
 *
 * @author Zhang Fan
 * @date 2022/8/5 15:00
 */
@Slf4j
@RestController("platformDistributionJointVentureCommissionApplyController")
@RequestMapping("/p/distribution_joint_venture_commission_apply")
@Api(tags = "平台端-联营分佣-分佣申请")
public class DistributionJointVentureCommissionApplyController {

    @Autowired
    private DistributionJointVentureCommissionStoreService distributionJointVentureCommissionStoreService;
    @Autowired
    private DistributionJointVentureCommissionApplyService distributionJointVentureCommissionApplyService;

    @GetMapping("/page")
    @ApiOperation(value = "获取联营分佣申请列表", notes = "分页获取联营分佣申请列表")
    public ServerResponseEntity<PageVO<DistributionJointVentureCommissionApply>> page(@Valid PageDTO pageDTO, DistributionJointVentureCommissionSearchDTO searchDTO) {
        PageVO<DistributionJointVentureCommissionApply> distributionJointVentureCommissionApplyPage = distributionJointVentureCommissionApplyService.page(pageDTO, searchDTO);
        return ServerResponseEntity.success(distributionJointVentureCommissionApplyPage);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "获取联营分佣申请", notes = "根据id获取联营分佣申请")
    public ServerResponseEntity<DistributionJointVentureCommissionApply> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionJointVentureCommissionApplyService.getById(id));
    }

    @Deprecated
    @PostMapping("/launchApplyWithOrder")
    @ApiOperation(value = "发起联营分佣申请", notes = "发起联营分佣申请")
    public ServerResponseEntity<Void> launchApplyWithOrder(@Valid @RequestBody DistributionJointVentureCommissionApplyDTO distributionJointVentureCommissionApplyDTO) {
        distributionJointVentureCommissionApplyService.launchApply(distributionJointVentureCommissionApplyDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/launchApply")
    @ApiOperation(value = "发起联营分佣申请", notes = "发起联营分佣申请")
    public ServerResponseEntity<Void> launchApply(@Valid @RequestBody DistributionJointVentureOrderSearchDTO searchDTO) {
        // 获取客户的门店列表
        if (CollectionUtils.isEmpty(searchDTO.getStoreIdList())) {
            List<Long> storeIdListByJointVentureId = distributionJointVentureCommissionStoreService.findStoreIdListByJointVentureId(searchDTO.getJointVentureCommissionCustomerId());
            if (CollectionUtils.isEmpty(storeIdListByJointVentureId)) {
                throw new LuckException("获取联营客户门店失败");
            }
            searchDTO.setStoreIdList(storeIdListByJointVentureId);
        }
//        distributionJointVentureCommissionApplyService.launchApply(searchDTO);
        distributionJointVentureCommissionApplyService.launchApplyV2(searchDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/auditApply")
    @ApiOperation(value = "审核联营分佣申请", notes = "审核联营分佣申请 1-审核通过 9-审核失败")
    public ServerResponseEntity<Void> auditApply(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        distributionJointVentureCommissionApplyService.auditApply(id, status);
        return ServerResponseEntity.success();
    }

    @PostMapping("/updateApplyBankCard")
    @ApiOperation(value = "更改提现记录银行卡信息", notes = "更改提现记录银行卡信息")
    public ServerResponseEntity<Void> updateApplyBankCard(@RequestParam("applyId") String applyId, @RequestParam("cardNo") String cardNo) {
        distributionJointVentureCommissionApplyService.updateApplyBankCard(applyId, cardNo);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "联营分佣申请记录导出")
    @GetMapping("/exportExcel")
    public ServerResponseEntity<Void> exportExcel(HttpServletResponse response, DistributionJointVentureCommissionSearchDTO searchDTO) {
        distributionJointVentureCommissionApplyService.exportExcel(response, searchDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation("联营分佣申请记录导出（下载中心）")
    @GetMapping("/exportExcelInDownloadCenter")
    public ServerResponseEntity exportDistributionOrder(DistributionJointVentureCommissionSearchDTO searchDTO) {
        try {
            distributionJointVentureCommissionApplyService.exportExcelInDownloadCenter(searchDTO);
            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        } catch (Exception e) {
            log.error("导出联营分佣申请记录错误: " + e.getMessage(), e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }
}
