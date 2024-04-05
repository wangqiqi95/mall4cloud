package com.mall4j.cloud.group.feign;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.api.group.feign.MealCommodityPoolFeignClient;
import com.mall4j.cloud.api.group.feign.dto.CommodityPoolAddDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ActivityCommodityAddDTO;
import com.mall4j.cloud.group.service.ActivityCommodityBizService;
import com.mall4j.cloud.group.service.CommodityPoolService;
import io.swagger.annotations.Api;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api
public class MealCommodityPoolFeignController implements MealCommodityPoolFeignClient {


    @Resource
    private ActivityCommodityBizService activityCommodityBizService;

    @Override
    public ServerResponseEntity<String> commodityPoolAdd(CommodityPoolAddDTO poolAddDTO) {
        //删除当前活动保存的商品池
        activityCommodityBizService.removeActivityCommodityByActivityId(poolAddDTO.getActivityId(), poolAddDTO.getActivityChannel());

        List<Long> spuIdList = poolAddDTO.getSpuIdList();
        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList,
                poolAddDTO.getBeginTime(),
                poolAddDTO.getEndTime(),
                poolAddDTO.getActivityChannel(),
                poolAddDTO.getActivityId(),
                poolAddDTO.getShopIdList());
        log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
            String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
            throw new LuckException(msg, activityCommodityAddDTO);
        }

//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList, poolAddDTO.getBeginTime(), poolAddDTO.getEndTime(), poolAddDTO.getActivityChannel(),poolAddDTO.getActivityId());
//        List<String> failCommodityIds = activityCommodityAddDTO.getFailCommodityIds();
//        if (CollectionUtil.isNotEmpty(failCommodityIds)) {
//            return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR, "当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息");
//        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<String> commodityPoolRemove(CommodityPoolAddDTO poolAddDTO) {
        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(poolAddDTO.getActivityId(),poolAddDTO.getActivityChannel());
        return ServerResponseEntity.success();
    }
}
