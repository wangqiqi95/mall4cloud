package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionStoreDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionStore;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 平台端-联营分佣-联营门店
 *
 * @author Zhang Fan
 * @date 2022/8/4 14:24
 */
@RestController("platformDistributionJointVentureCommissionStoreController")
@RequestMapping("/p/distribution_joint_venture_commission_store")
@Api(tags = "平台端-联营分佣-联营门店")
public class DistributionJointVentureCommissionStoreController {

    @Autowired
    private DistributionJointVentureCommissionStoreService distributionJointVentureCommissionStoreService;

    @GetMapping("/findStoreIdList")
    @ApiOperation(value = "联营分佣下适用门店id集合", notes = "通过联营分佣id查询适用门店id集合")
    public ServerResponseEntity<List<Long>> findStoreIdList(@RequestParam Long jointVentureId) {
        return ServerResponseEntity.success(distributionJointVentureCommissionStoreService.findStoreIdListByJointVentureId(jointVentureId));
    }

    @GetMapping("/findStoreList")
    @ApiOperation(value = "联营分佣下适用门店集合", notes = "通过联营分佣id查询适用门店集合")
    public ServerResponseEntity<List<DistributionJointVentureCommissionStore>> findStoreList(@RequestParam Long jointVentureId) {
        return ServerResponseEntity.success(distributionJointVentureCommissionStoreService.findStoreListByJointVentureId(jointVentureId));
    }

    @PostMapping("/save")
    @ApiOperation(value = "佣金联营分佣添加门店", notes = "佣金联营分佣添加门店")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionJointVentureCommissionStoreDTO distributionJointVentureCommissionStoreDTO) {
        distributionJointVentureCommissionStoreService.save(distributionJointVentureCommissionStoreDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "佣金联营分佣删除门店", notes = "佣金联营分佣删除门店")
    public ServerResponseEntity<Void> delete(@RequestParam Long jointVentureId, @RequestParam Long storeId) {
        distributionJointVentureCommissionStoreService.deleteByJointVentureIdAndStoreId(jointVentureId, storeId);
        return ServerResponseEntity.success();
    }
}
