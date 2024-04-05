package com.mall4j.cloud.delivery.controller.admin;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.delivery.service.TransportService;
import com.mall4j.cloud.delivery.vo.TransportVO;
import com.mall4j.cloud.delivery.dto.TransportDTO;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 运费模板
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
@RestController("adminTransportController")
@RequestMapping("/mp/transport")
@Api(tags = "运费模板")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取运费模板列表", notes = "分页获取运费模板列表")
	public ServerResponseEntity<PageVO<TransportVO>> page(@Valid PageDTO pageDTO,TransportDTO transportDTO) {
        Long tenantId = AuthUserContext.get().getTenantId();
        transportDTO.setShopId(tenantId);
        PageVO<TransportVO> transports = transportService.page(pageDTO,transportDTO);
        return ServerResponseEntity.success(transports);
	}

	@GetMapping
    @ApiOperation(value = "获取运费模板", notes = "根据transportId获取运费模板")
    public ServerResponseEntity<TransportVO> getByTransportId(@RequestParam Long transportId) {
        TransportVO transportVo = transportService.getTransportAndAllItemsById(transportId);
        return ServerResponseEntity.success(transportVo);
    }

    @PostMapping
    @ApiOperation(value = "保存运费模板", notes = "保存运费模板")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TransportDTO transportDTO) {
        Long shopId = AuthUserContext.get().getTenantId();
        transportDTO.setShopId(shopId);

        transportService.insertTransportAndTransFee(transportDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新运费模板", notes = "更新运费模板")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TransportDTO transportDTO) {
        transportService.updateTransportAndTransFee(transportDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除运费模板", notes = "根据运费模板id删除运费模板")
    public ServerResponseEntity<Void> delete(@RequestParam Long transportId) {
        transportService.deleteTransportAndTransFeeAndTransCityById(transportId);
        // 删除运费模板的缓存
        transportService.removeTransportAndAllItemsCache(transportId);
        return ServerResponseEntity.success();
    }

    /**
     * 获取运费模板列表
     */
    @GetMapping("/list")
    public ServerResponseEntity<List<TransportVO>> list(@RequestParam(value = "shopId", required = false) Long shopId) {
        Long tenantId = AuthUserContext.get().getTenantId();
        if (Objects.equals(tenantId, Constant.PLATFORM_SHOP_ID) && Objects.nonNull(shopId)) {
            tenantId = shopId;
        }
        List<TransportVO> transports = transportService.listTransport(tenantId);
        return ServerResponseEntity.success(transports);
    }
    
    @GetMapping("/transportList")
    @ApiOperation(value = "视频号4.0获取运费模板列表", notes = "视频号4.0获取运费模板列表")
    public ServerResponseEntity<PageVO<TransportVO>> transportList(@Valid PageDTO pageDTO, TransportDTO transportDTO) {
        Long tenantId = AuthUserContext.get().getTenantId();
        transportDTO.setShopId(tenantId);
        PageVO<TransportVO> transports = transportService.transportList(pageDTO,transportDTO);
        return ServerResponseEntity.success(transports);
    }
}
