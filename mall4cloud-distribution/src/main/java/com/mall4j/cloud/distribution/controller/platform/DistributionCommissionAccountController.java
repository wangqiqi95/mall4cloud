package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountDTO;
import com.mall4j.cloud.distribution.listener.OrderSettlementConsumer;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccount;
import com.mall4j.cloud.distribution.service.DistributionCommissionAccountService;
import com.mall4j.cloud.distribution.service.DistributionOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 佣金管理-佣金账户
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
@RestController("platformDistributionCommissionAccountController")
@RequestMapping("/p/distribution_commission_account")
@Api(tags = "平台端-佣金管理-佣金账户")
public class DistributionCommissionAccountController {

    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private DistributionOrderService distributionOrderService;

    @Autowired
    private OrderSettlementConsumer orderSettlementConsumer;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金管理-佣金账户列表", notes = "分页获取佣金管理-佣金账户列表")
    public ServerResponseEntity<PageVO<DistributionCommissionAccountDTO>> page(@Valid PageDTO pageDTO, DistributionCommissionAccountDTO distributionCommissionAccountDTO) {
        PageVO<DistributionCommissionAccountDTO> distributionCommissionAccountPage = distributionCommissionAccountService.page(pageDTO, distributionCommissionAccountDTO);
        return ServerResponseEntity.success(distributionCommissionAccountPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金管理-佣金账户", notes = "根据id获取佣金管理-佣金账户")
    public ServerResponseEntity<DistributionCommissionAccount> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionCommissionAccountService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存佣金管理-佣金账户", notes = "保存佣金管理-佣金账户")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionCommissionAccountDTO distributionCommissionAccountDTO) {
        DistributionCommissionAccount distributionCommissionAccount = mapperFacade.map(distributionCommissionAccountDTO, DistributionCommissionAccount.class);
        distributionCommissionAccount.setId(null);
        distributionCommissionAccountService.save(distributionCommissionAccount);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金管理-佣金账户", notes = "更新佣金管理-佣金账户")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionCommissionAccountDTO distributionCommissionAccountDTO) {
        DistributionCommissionAccount distributionCommissionAccount = mapperFacade.map(distributionCommissionAccountDTO, DistributionCommissionAccount.class);
        distributionCommissionAccountService.update(distributionCommissionAccount);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金管理-佣金账户", notes = "根据佣金管理-佣金账户id删除佣金管理-佣金账户")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionCommissionAccountService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "佣金信息导出")
    @GetMapping("/sold_excel")
    public ServerResponseEntity<Void> commissionExcel(HttpServletResponse response, DistributionCommissionAccountDTO dto) {
        distributionCommissionAccountService.commissionExcel(response, dto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/test")
    public void test(@RequestBody PayNotifyBO payNotifyBO){
        distributionOrderService.paySuccessNotifyDistributionOrder(payNotifyBO);
    }

    @PostMapping("/test1")
    public void test1(@RequestBody PayNotifyBO payNotifyBO){
        orderSettlementConsumer.onMessage(payNotifyBO.getOrderIds());
    }
}
