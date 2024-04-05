package com.mall4j.cloud.distribution.controller.multishop;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.constant.DistributionSpuStatus;
import com.mall4j.cloud.distribution.service.DistributionSpuService;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.distribution.dto.DistributionSpuDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 分销商品关联信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@RestController("multishopDistributionSpuController")
@RequestMapping("/m/distribution_spu")
@Api(tags = "multishop-分销商品关联信息")
public class DistributionSpuController {

    @Autowired
    private DistributionSpuService distributionSpuService;

	@GetMapping
    @ApiOperation(value = "获取分销商品关联信息", notes = "根据distributionSpuId获取分销商品关联信息")
    public ServerResponseEntity<DistributionSpuVO> getByDistributionSpuId(@RequestParam Long distributionSpuId) {
        return ServerResponseEntity.success(distributionSpuService.getByDistributionSpuId(distributionSpuId));
    }

    @PostMapping
    @ApiOperation(value = "保存分销商品关联信息", notes = "保存分销商品关联信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionSpuDTO distributionSpuDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        distributionSpuDTO.setShopId(userInfoInTokenBO.getTenantId());
        distributionSpuDTO.setModifier(userInfoInTokenBO.getUserId());
        distributionSpuService.save(distributionSpuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销商品关联信息", notes = "更新分销商品关联信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionSpuDTO distributionSpuDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        distributionSpuDTO.setShopId(userInfoInTokenBO.getTenantId());
        distributionSpuDTO.setModifier(userInfoInTokenBO.getUserId());
        distributionSpuDTO.setState(null);
        distributionSpuService.update(distributionSpuDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销商品关联信息", notes = "根据分销商品关联信息id删除分销商品关联信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long distributionSpuId) {
	    Long shopId = AuthUserContext.get().getTenantId();
        distributionSpuService.deleteByIdAndShopId(distributionSpuId, shopId);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/batch_delete")
    @ApiOperation(value = "根据分销商品id批量删除分销商品关联信息", notes = "根据分销商品id批量删除分销商品关联信息")
    public ServerResponseEntity<Void> batchDelete(@RequestBody List<Long> distributionSpuIds) {
	    Long shopId = AuthUserContext.get().getTenantId();
	    distributionSpuService.batchDeleteByIdsAndShopId(distributionSpuIds, shopId);
	    return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event")
    @ApiOperation(value = "获取分销商品下线信息", notes = "获取分销商品下线信息")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@RequestParam(value = "distributionSpuId") Long distributionSpuId) {
        return ServerResponseEntity.success(distributionSpuService.getOfflineHandleEvent(distributionSpuId));
    }

    @PostMapping("/audit_apply")
    @ApiOperation(value = "违规分销商品提交重新上线申请", notes = "违规分销商品提交重新上线申请")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDTO) {
	    distributionSpuService.auditApply(offlineHandleEventDTO);
	    return ServerResponseEntity.success();
    }

    @PutMapping("/update_state")
    @ApiOperation(value = "上架或下架分销商品", notes = "上架或下架分销商品")
    public ServerResponseEntity<Void> updateState(@RequestParam(value = "distributionSpuId") Long distributionSpuId, @RequestParam(value = "state") Integer state) {
	    if (!Objects.equals(DistributionSpuStatus.OFF_SHELF.value(), state) && !Objects.equals(DistributionSpuStatus.PUT_SHELF.value(), state)) {
	        throw new LuckException("状态错误");
        }
	    distributionSpuService.updateState(distributionSpuId, state);
	    return ServerResponseEntity.success();
    }

}
