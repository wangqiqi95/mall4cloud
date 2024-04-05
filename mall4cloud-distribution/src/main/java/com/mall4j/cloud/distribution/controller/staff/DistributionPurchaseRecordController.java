package com.mall4j.cloud.distribution.controller.staff;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.cache.constant.DistributionCacheNames;
import com.mall4j.cloud.common.cache.constant.OrderCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.distribution.dto.DistributionPurchaseRecordDTO;
import com.mall4j.cloud.distribution.dto.PurchaseRankingDTO;
import com.mall4j.cloud.distribution.model.DistributionPurchaseRecord;
import com.mall4j.cloud.distribution.service.DistributionPurchaseRecordService;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import com.mall4j.cloud.distribution.vo.DistributionPurchaseRecordVO;
import io.seata.common.util.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分销推广-加购记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Slf4j
@RestController("staffDistributionPurchaseRecordController")
@RequestMapping("/s/distribution_purchase_record")
@Api(tags = "导购小程序-分销推广-加购记录")
public class DistributionPurchaseRecordController {

    @Autowired
    private DistributionPurchaseRecordService distributionPurchaseRecordService;

    @Autowired
    private StoreFeignClient storeFeignClient;


	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-加购记录列表", notes = "分页获取分销推广-加购记录列表")
	public ServerResponseEntity<PageVO<DistributionPurchaseRecordVO>> page(@Valid PageDTO pageDTO, DistributionPurchaseRecordDTO dto) {
        dto.setShareId(AuthUserContext.get().getUserId());
        dto.setShareType(1);
		PageVO<DistributionPurchaseRecordVO> distributionPurchaseRecordPage = distributionPurchaseRecordService.page(pageDTO, dto);
		return ServerResponseEntity.success(distributionPurchaseRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-加购记录", notes = "根据id获取分销推广-加购记录")
    public ServerResponseEntity<DistributionPurchaseRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionPurchaseRecordService.getById(id));
    }

    @GetMapping("/stat")
    @ApiOperation(value = "分享推广-加购记录统计", notes = "分享推广-下单记录统计")
    public ServerResponseEntity<DistributionPromotionStatVO> stat() {
        DistributionPromotionStatVO distributionPromotionStatVO = distributionPurchaseRecordService.stat(1,
                AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(distributionPromotionStatVO);
    }


    @ApiOperation("加购商品排行榜")
    @GetMapping("/pagePurchaseRanking")
    public ServerResponseEntity<PageVO<PurchaseRankingDTO>> pagePurchaseRanking(@Valid PageDTO pageDTO, DistributionPurchaseRecordDTO dto){
        dto.setStoreType(Optional.ofNullable(dto.getStoreType()).orElse(1));
        List<Long> storeIds = new ArrayList<>();
        if (dto.getStoreType() == 1){
            storeIds.add(AuthUserContext.get().getStoreId());
        } else if (dto.getStoreType() == 2){
            ServerResponseEntity<Long> areaId = storeFeignClient.getAreaOrgByStore(AuthUserContext.get().getStoreId());
            if (areaId.isSuccess() && null != areaId.getData()){
                ServerResponseEntity<List<StoreVO>> entity = storeFeignClient.listByOrgId(areaId.getData());
                if (entity.isSuccess()){
                    if (CollectionUtils.isEmpty(entity.getData())){
                        return ServerResponseEntity.success(new PageVO<>());
                    }
                    storeIds.addAll(entity.getData().stream().map(StoreVO::getStoreId).distinct().collect(Collectors.toList()));
                }
            }
        }
        dto.setStoreIds(storeIds);

        String dataKey=JSON.toJSONString(pageDTO)+JSON.toJSONString(dto);
        log.info("加购商品排行榜 dataKey--> {}",dataKey);
        String rediskey= DistributionCacheNames.DISTRIBUTION_PURCHASE_RANKING + DigestUtil.md5Hex(dataKey);
        log.info("加购商品排行榜 rediskey--> {}",rediskey);
        if(RedisUtil.hasKey(rediskey)){
            log.info("加购商品排行榜 读取缓存--> {}",rediskey);
            ServerResponseEntity<PageVO<PurchaseRankingDTO>> serverResponse = JSONObject.parseObject(RedisUtil.get(rediskey),ServerResponseEntity.class);
            return serverResponse;
        }

        dto.setStartDate(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 0));
        dto.setEndDate(DateUtils.getMonthFirstOrLastDay(new java.util.Date(), 1));

        PageVO<PurchaseRankingDTO> pageVO = distributionPurchaseRecordService.pagePurchaseRanking(pageDTO, dto);

        RedisUtil.set(rediskey,JSONObject.toJSONString(pageVO),600L);
	    return ServerResponseEntity.success(pageVO);
    }

}
