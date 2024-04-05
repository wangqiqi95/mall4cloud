package com.mall4j.cloud.group.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.OpenCommodityDTO;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountActivityMapper;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountSkuMapper;
import com.mall4j.cloud.group.service.CommodityPoolService;
import com.mall4j.cloud.group.service.TimeLimitedDiscountActivityService;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @luzhengxiang
 * @create 2022-03-12 5:12 PM
 **/
@Slf4j
@Api
@RestController
public class TimeDiscountFeignController  implements TimeDiscountFeignClient {

    @Autowired
    TimeLimitedDiscountActivityMapper discountActivityMapper;
    @Autowired
    TimeLimitedDiscountSkuMapper discountSkuMapper;
    @Autowired
    private TimeLimitedDiscountActivityService timeLimitedDiscountActivityService;
    @Autowired
    private CommodityPoolService commodityPoolService;

    @Override
    public ServerResponseEntity<List<TimeDiscountActivityVO>> convertActivityPrice(TimeDiscountActivityDTO params) {

        return ServerResponseEntity.success(timeLimitedDiscountActivityService.convertActivityPrice(params));

//        List<TimeDiscountActivityVO> skus = new ArrayList<>();
//        if (CollectionUtil.isEmpty(params.getSkuIds())){
//            return ServerResponseEntity.success(skus);
//        }
//        //审核状态：0待审核 1审核通过 2驳回
//        Integer checkStatus=1;//默认只查审核通过
//        //查询当前店铺是否有调价活动在开展。
//        List<TimeLimitedDiscountActivityVO> activityVOS1 = discountActivityMapper.currentActivity(params.getShopId(),checkStatus);
//        if(CollectionUtil.isEmpty(activityVOS1)){
//            log.info("----查询当前店铺是否有调价活动在开展 查询结果为空------>>>");
//        }
//        //如果 有符合条件的活动。查询商品列表中是否有参与活动的商品，返回对应的活动价格。
//        if(CollectionUtil.isNotEmpty(activityVOS1)){
//            List<Integer> activityids = activityVOS1.stream().map(TimeLimitedDiscountActivityVO::getId).collect(Collectors.toList());
//            skus = discountSkuMapper.selectBySkuIds(activityids,params.getSkuIds());
//        }
//        return ServerResponseEntity.success(skus);
    }

    @Override
    public ServerResponseEntity<List<TimeDiscountActivityVO>> convertActivityPricesNoFilter(TimeDiscountActivityDTO params) {
        return ServerResponseEntity.success(timeLimitedDiscountActivityService.convertActivityPricesNoFilter(params));
    }

    @Override
    public ServerResponseEntity<List<TimeDiscountActivityVO>> currentActivityBySpuId(TimeDiscountActivityDTO params) {
        return ServerResponseEntity.success(timeLimitedDiscountActivityService.currentActivityBySpuId(params));
    }

    @Override
    public ServerResponseEntity<List<Long>> getOpenCommoditys(OpenCommodityDTO openCommodityDTO) {
        return ServerResponseEntity.success(commodityPoolService.getOpenCommoditys(openCommodityDTO.getActivityChannels(),openCommodityDTO.getStoreId()));
    }
}
