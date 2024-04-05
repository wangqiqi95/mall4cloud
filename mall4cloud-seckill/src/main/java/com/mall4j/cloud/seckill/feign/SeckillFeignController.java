package com.mall4j.cloud.seckill.feign;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.seckill.feign.SeckillFeignClient;
import com.mall4j.cloud.api.seckill.vo.SeckillApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SekillActivitySpuVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.seckill.service.SeckillService;
import com.mall4j.cloud.seckill.vo.SeckillVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class SeckillFeignController implements SeckillFeignClient {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SpuFeignClient spuFeignClient;


    @Override
    public ServerResponseEntity<List<SekillActivitySpuVO>> seckillSpuListBySpuIds(Integer selectedLot, List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<SekillActivitySpuVO> groupActivitySpuList = seckillService.listBySelectLotAndSpuIds(selectedLot,spuIds);
        // 获取sku原价
        List<Long> skuIds = groupActivitySpuList.stream().map(SekillActivitySpuVO::getSkuId).collect(Collectors.toList());
        ServerResponseEntity<List<SkuVO>> skuResponse = spuFeignClient.listSkuPriceByIds(skuIds);
        if (skuResponse.isFail()) {
            throw new LuckException(skuResponse.getMsg());
        }
        if (CollUtil.isNotEmpty(skuResponse.getData())) {
            Map<Long, Long> skuMap = skuResponse.getData().stream().collect(Collectors.toMap(SkuVO::getSkuId, SkuVO::getPriceFee));
            for (SekillActivitySpuVO sekillActivitySpuVO : groupActivitySpuList) {
                sekillActivitySpuVO.setPriceFee(skuMap.get(sekillActivitySpuVO.getSkuId()));
            }
        }
        return ServerResponseEntity.success(groupActivitySpuList);
    }

    @Override
    public ServerResponseEntity<SeckillApiVO> getSeckillInfoById(Long activityId) {
        SeckillVO seckillVO = seckillService.getBySeckillId(activityId);

        return ServerResponseEntity.success(mapperFacade.map(seckillVO,SeckillApiVO.class));
    }

    @Override
    public ServerResponseEntity<Void> offlineSeckillBySpuIds(List<Long> spuIds) {
        seckillService.offlineSeckillBySpuIds(spuIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> offlineSeckillByShopId(Long shopId) {
        seckillService.offlineSeckillByShopId(shopId);
        return ServerResponseEntity.success();
    }
}
