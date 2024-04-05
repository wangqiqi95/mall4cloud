package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerDTO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerSearchDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 平台端-联营分佣-联营客户
 *
 * @author Zhang Fan
 * @date 2022/8/4 10:27
 */
@RestController("platformDistributionJointVentureCommissionCustomerController")
@RequestMapping("/p/distribution_joint_venture_commission_customer")
@Api(tags = "平台端-联营分佣-联营客户")
public class DistributionJointVentureCommissionCustomerController {

    @Autowired
    private DistributionJointVentureCommissionCustomerService distributionJointVentureCommissionCustomerService;

    @GetMapping("/page")
    @ApiOperation(value = "获取联营客户列表", notes = "分页获取联营客户列表")
    public ServerResponseEntity<PageVO<DistributionJointVentureCommissionCustomer>> page(@Valid PageDTO pageDTO, DistributionJointVentureCommissionCustomerSearchDTO distributionJointVentureCommissionCustomerSearchDTO) {
        PageVO<DistributionJointVentureCommissionCustomer> distributionJointVentureCommissionCustomerPage = distributionJointVentureCommissionCustomerService.page(pageDTO, distributionJointVentureCommissionCustomerSearchDTO);
        return ServerResponseEntity.success(distributionJointVentureCommissionCustomerPage);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "获取联营客户", notes = "根据id获取联营客户")
    public ServerResponseEntity<DistributionJointVentureCommissionCustomer> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionJointVentureCommissionCustomerService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存联营客户", notes = "保存联营客户")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO) {
        saveOrUpdateCheck(distributionJointVentureCommissionCustomerDTO);
        distributionJointVentureCommissionCustomerService.save(distributionJointVentureCommissionCustomerDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新联营客户", notes = "更新联营客户")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO) {
        saveOrUpdateCheck(distributionJointVentureCommissionCustomerDTO);
        distributionJointVentureCommissionCustomerService.update(distributionJointVentureCommissionCustomerDTO);
        return ServerResponseEntity.success();
    }


    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新联营客户状态", notes = "更新联营客户状态")
    public ServerResponseEntity<Void> updateStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        distributionJointVentureCommissionCustomerService.updateStatus(id, status);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除联营客户", notes = "根据联营客户id删除联营客户")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionJointVentureCommissionCustomerService.deleteById(id);
        return ServerResponseEntity.success();
    }

    private void saveOrUpdateCheck(DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO) {
        if (distributionJointVentureCommissionCustomerDTO.getLimitStoreType() == 1) {
            if (CollectionUtils.isEmpty(distributionJointVentureCommissionCustomerDTO.getLimitStoreIdList())) {
                throw new LuckException("适用门店未指定门店");
            }
        }
        DistributionJointVentureCommissionCustomer byIdCard = distributionJointVentureCommissionCustomerService.getByIdCard(distributionJointVentureCommissionCustomerDTO.getIdCard());
        if (byIdCard != null && (byIdCard.getIdCard() != null && !byIdCard.getId().equals(distributionJointVentureCommissionCustomerDTO.getId()))) {
            throw new LuckException("身份证号码已存在");
        }
    }
}
