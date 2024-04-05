package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.dto.SpuSkuRDTO;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.cache.constant.FlowCacheNames;
import com.mall4j.cloud.common.cache.constant.GroupCacheNames;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.GroupActivitySpuVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.constant.GroupActivityStatusEnum;
import com.mall4j.cloud.group.dto.ActivityCommodityAddDTO;
import com.mall4j.cloud.group.dto.AppGroupActivityDTO;
import com.mall4j.cloud.group.dto.GroupActivityDTO;
import com.mall4j.cloud.group.dto.GroupSkuDTO;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.group.mapper.GroupActivityMapper;
import com.mall4j.cloud.group.mapper.GroupStoreMapper;
import com.mall4j.cloud.group.model.GroupActivity;
import com.mall4j.cloud.group.model.GroupSku;
import com.mall4j.cloud.group.model.GroupStore;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.GroupActivityVO;
import com.mall4j.cloud.group.vo.GroupSkuVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountShopVO;
import com.mall4j.cloud.group.vo.app.AppGroupActivityVO;
import com.mall4j.cloud.group.vo.app.AppGroupSkuVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拼团活动表
 *
 * @author YXF
 * @date 2021-03-20 10:39:31
 */
@Slf4j
@Service
public class GroupActivityServiceImpl implements GroupActivityService {

    @Autowired
    private GroupActivityMapper groupActivityMapper;
    @Autowired
    private GroupTeamService groupTeamService;
    @Autowired
    private GroupOrderService groupOrderService;
    @Autowired
    private GroupSkuService groupSkuService;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private OfflineHandleEventFeignClient offlineHandleEventFeignClient;
    @Autowired
    private GroupStoreMapper groupStoreMapper;
    @Resource
    private ActivityCommodityBizService activityCommodityBizService;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    public PageVO<GroupActivityVO> page(PageDTO pageDTO, GroupActivityDTO groupActivityDTO) {
        return PageUtil.doPage(pageDTO, () -> groupActivityMapper.list(groupActivityDTO));
    }

    @Override
    public PageVO<GroupActivityVO> platformPage(PageDTO pageDTO, GroupActivityDTO groupActivityDTO) {
        PageVO<GroupActivityVO> page = PageUtil.doPage(pageDTO, () -> groupActivityMapper.list(groupActivityDTO));
        /*Set<Long> shopIdSet = page.getList().stream().map(GroupActivityVO::getShopId).collect(Collectors.toSet());
        ServerResponseEntity<List<ShopDetailVO>> responseEntity = shopDetailFeignClient.listByShopIds(new ArrayList<>(shopIdSet));
        if (CollUtil.isEmpty(responseEntity.getData())) {
            return page;
        }*/

        List<Long> groupActivityIdList = page.getList().stream().map(GroupActivityVO::getGroupActivityId).collect(Collectors.toList());
        QueryWrapper<GroupStore> queryWrapper = new QueryWrapper<>();
        if (groupActivityIdList != null && groupActivityIdList.size() > 0) {
            queryWrapper.in("group_activity_id", groupActivityIdList);
        }
        List<GroupStore> groupStoreList = groupStoreMapper.selectList(queryWrapper);
        Map<Long, List<GroupStore>> groupStoreMap = groupStoreList.stream().collect(Collectors.groupingBy(GroupStore::getGroupActivityId));
        // Map<Long, String> shopMap = responseEntity.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, ShopDetailVO::getShopName));
        for (GroupActivityVO groupActivity : page.getList()) {
            if (groupActivity.getLimitStoreType() == 1 && groupStoreMap.containsKey(groupActivity.getGroupActivityId())) {
                groupActivity.setLimitStoreIdList(groupStoreMap.get(groupActivity.getGroupActivityId()).stream()
                        .map(GroupStore::getStoreId).collect(Collectors.toList()));
            }
            // groupActivity.setShopName(shopMap.get(groupActivity.getShopId()));
        }
        return page;
    }

    @Override
    public GroupActivityVO getByGroupActivityId(Long groupActivityId) {
        return groupActivityMapper.getByGroupActivityId(groupActivityId);
    }

    @Override
    public GroupActivityVO getGroupActivityInfo(Long groupActivityId) {
        GroupActivityVO groupActivityVO = groupActivityMapper.getByGroupActivityId(groupActivityId);
        ServerResponseEntity<SpuVO> spuResponse = spuFeignClient.getSpuAndSkuBySpuId(groupActivityVO.getSpuId());
        groupActivityVO.setGroupSkuList(groupSkuService.listByGroupActivityId(groupActivityId));
        SpuVO spuVO = spuResponse.getData();
        // 商品不存在
        if (Objects.isNull(spuVO)) {
            groupActivityVO.setSpuId(null);
            return groupActivityVO;
        }
        Map<Long, GroupSkuVO> skuMap = groupActivityVO.getGroupSkuList().stream().collect(Collectors.toMap(GroupSkuVO::getSkuId, g -> g));
        for (SkuVO sku : spuVO.getSkus()) {
            GroupSkuVO groupSkuVO = skuMap.get(sku.getSkuId());
            if (groupSkuVO == null) {
                continue;
            }
            groupSkuVO.setSkuName(sku.getSkuName());
            groupSkuVO.setPrice(sku.getPriceFee());
            groupSkuVO.setSkuCode(sku.getSkuCode());
        }
        groupActivityVO.setMainImgUrl(spuVO.getMainImgUrl());
        groupActivityVO.setSpuName(spuVO.getName());

        //指定门店类型 0-所有门店 1-部分门店
        List<GroupStore> shops = groupActivityVO.getLimitStoreType()==1?groupStoreMapper.selectByActivityId(groupActivityId):null;
        List<Long> storeIds = CollectionUtil.isNotEmpty(shops)?shops.stream().map(temp -> temp.getStoreId()).collect(Collectors.toList()):null;
        groupActivityVO.setLimitStoreIdList(storeIds);
        return groupActivityVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void save(GroupActivityDTO groupActivityDTO) {
        // 判断是否是普通商品
        ServerResponseEntity<SpuVO> spuResponseEntity = spuFeignClient.getDetailById(groupActivityDTO.getSpuId());
        if (spuResponseEntity.isFail()) {
            throw new LuckException(spuResponseEntity.getMsg());
        }
        SpuVO spuVO = spuResponseEntity.getData();
//        if (!Objects.equals(spuVO.getSpuType(), SpuType.NORMAL.value())) {
//            throw new LuckException(ResponseEnum.DATA_ERROR);
//        }
        // 保存团购信息
        GroupActivity groupActivity = mapperFacade.map(groupActivityDTO, GroupActivity.class);
        // 默认为未启用状态
        groupActivity.setStatus(GroupActivityStatusEnum.DISABLE.value());
        groupActivity.setShopId(AuthUserContext.get().getTenantId());
        groupActivity.setPrice(getGroupSpuPrice(groupActivityDTO.getGroupSkuList()));
        groupActivityMapper.save(groupActivity);
        groupSkuService.batchSave(mapperFacade.mapAsList(groupActivityDTO.getGroupSkuList(), GroupSku.class), groupActivity.getGroupActivityId());

        changeSpuType(groupActivity.getGroupActivityId(), groupActivity.getSpuId(), SpuType.GROUP, StatusEnum.ENABLE);

        if (groupActivityDTO.getLimitStoreType() != null && groupActivityDTO.getLimitStoreType() == 1
                && !CollectionUtils.isEmpty(groupActivityDTO.getLimitStoreIdList())) {
            List<GroupStore> groupStoreList = groupActivityDTO.getLimitStoreIdList().stream().map(g -> {
                GroupStore groupStore = new GroupStore();
                groupStore.setGroupActivityId(groupActivity.getGroupActivityId());
                groupStore.setStoreId(g);
                return groupStore;
            }).collect(Collectors.toList());
            groupStoreMapper.deleteByGroupActivityId(groupActivity.getGroupActivityId());
            groupStoreMapper.saveBatch(groupStoreList);
        }
        //商品池限制
        ArrayList<Long> spuIdList = new ArrayList<>();
        spuIdList.add(groupActivity.getSpuId());

        if(Objects.nonNull(groupActivity.getStatus()) && groupActivity.getStatus()==1){
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList,
                    groupActivity.getStartTime(),
                    groupActivity.getEndTime(),
                    ActivityChannelEnums.GROUP_ACTIVITY.getCode(),
                    Long.valueOf(groupActivity.getGroupActivityId()),
                    groupActivityDTO.getLimitStoreIdList());
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }

//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList, groupActivity.getStartTime(), groupActivity.getEndTime(), ActivityChannelEnums.GROUP_ACTIVITY.getCode(), groupActivity.getGroupActivityId());
//        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
//            throw new LuckException("当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息", activityCommodityAddDTO);
//        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void update(GroupActivityDTO groupActivityDTO) {
        GroupActivityVO dbGroupActivity = groupActivityMapper.getByGroupActivityId(groupActivityDTO.getGroupActivityId());
        if (!Objects.equals(dbGroupActivity.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        groupActivityDTO.setSpuId(dbGroupActivity.getSpuId());
        GroupActivity groupActivity = mapperFacade.map(groupActivityDTO, GroupActivity.class);
        if (!Objects.equals(dbGroupActivity.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        groupActivity.setPrice(getGroupSpuPrice(groupActivityDTO.getGroupSkuList()));
        // 不更新的字段-预防非法修改
        groupActivity.setStatus(null);
        groupActivity.setSpuId(null);
        groupActivityMapper.update(groupActivity);
        // 更新团购商品信息
        changeSpuType(dbGroupActivity.getGroupActivityId(), dbGroupActivity.getSpuId(), null, null);
        // 状态为未启用可以更新sku
        if (Objects.equals(GroupActivityStatusEnum.DISABLE.value(), dbGroupActivity.getStatus())) {
            List<GroupSku> groupSkuList = mapperFacade.mapAsList(groupActivityDTO.getGroupSkuList(), GroupSku.class);
            groupSkuService.batchUpdate(groupSkuList, groupActivity.getGroupActivityId());
        }

        groupStoreMapper.deleteByGroupActivityId(groupActivity.getGroupActivityId());
        if (groupActivityDTO.getLimitStoreType() != null && groupActivityDTO.getLimitStoreType() == 1
                && !CollectionUtils.isEmpty(groupActivityDTO.getLimitStoreIdList())) {
            List<GroupStore> groupStoreList = groupActivityDTO.getLimitStoreIdList().stream().map(g -> {
                GroupStore groupStore = new GroupStore();
                groupStore.setGroupActivityId(groupActivity.getGroupActivityId());
                groupStore.setStoreId(g);
                return groupStore;
            }).collect(Collectors.toList());
            groupStoreMapper.deleteByGroupActivityId(groupActivity.getGroupActivityId());
            groupStoreMapper.saveBatch(groupStoreList);
        }

        //商品池限制
        ArrayList<Long> spuIdList = new ArrayList<>();
        spuIdList.add(groupActivity.getSpuId());

        if(Objects.nonNull(groupActivity.getStatus()) && groupActivity.getStatus()==1){//保存并且启用需要校验商品池
            ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList,
                    groupActivity.getStartTime(),
                    groupActivity.getEndTime(),
                    ActivityChannelEnums.GROUP_ACTIVITY.getCode(),
                    Long.valueOf(groupActivity.getGroupActivityId()),
                    groupActivityDTO.getLimitStoreIdList());
            log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
            if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
                String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
                throw new LuckException(msg, activityCommodityAddDTO);
            }
        }


//        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList, groupActivity.getStartTime(), groupActivity.getEndTime(), ActivityChannelEnums.GROUP_ACTIVITY.getCode(), groupActivity.getGroupActivityId());
//        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
//            throw new LuckException("当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息", activityCommodityAddDTO);
//        }
    }


    @Override
    public void updateStatus(Long groupActivityId, GroupActivityStatusEnum status) {
        GroupActivity groupActivity = new GroupActivity();
        groupActivity.setStatus(status.value());
        groupActivity.setGroupActivityId(groupActivityId);
        groupActivityMapper.update(groupActivity);

        //移除商品池商品
        if(status!=GroupActivityStatusEnum.ENABLE){
            activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(groupActivityId),ActivityChannelEnums.GROUP_ACTIVITY.getCode());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void invalidGroupActivity(Long groupActivityId, Long spuId) {

        updateStatus(groupActivityId, GroupActivityStatusEnum.EXPIRED);
        changeSpuType(groupActivityId, spuId, SpuType.NORMAL, StatusEnum.ENABLE);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(groupActivityId),ActivityChannelEnums.GROUP_ACTIVITY.getCode());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void deleteGroupActivity(Long groupActivityId, Long spuId) {
        changeSpuType(groupActivityId, spuId, SpuType.NORMAL, StatusEnum.ENABLE);
        updateStatus(groupActivityId, GroupActivityStatusEnum.DELETE);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(groupActivityId),ActivityChannelEnums.GROUP_ACTIVITY.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void activeGroupActivity(Long groupActivityId, Long spuId) {
        GroupActivityVO groupActivity = getByGroupActivityId(groupActivityId);
        //商品池限制
        ArrayList<Long> spuIdList = new ArrayList<>();
        spuIdList.add(groupActivity.getSpuId());

        //指定门店类型 0-所有门店 1-部分门店
        List<GroupStore> shops = groupActivity.getLimitStoreType()==1?groupStoreMapper.selectByActivityId(groupActivityId):null;
        List<Long> storeIds = CollectionUtil.isNotEmpty(shops)?shops.stream().map(temp -> temp.getStoreId()).collect(Collectors.toList()):null;

        ActivityCommodityAddDTO activityCommodityAddDTO = activityCommodityBizService.addActivityCommodity(spuIdList,
                groupActivity.getStartTime(),
                groupActivity.getEndTime(),
                ActivityChannelEnums.GROUP_ACTIVITY.getCode(),
                Long.valueOf(groupActivity.getGroupActivityId()),
                storeIds);
        log.info("活动商品数据:{}", JSONObject.toJSONString(activityCommodityAddDTO));
        if (CollectionUtil.isNotEmpty(activityCommodityAddDTO.getFailCommodityIds())) {
            String msg="当前活动中的部分活动商品和其他活动冲突，请重新编辑活动信息。"+activityCommodityAddDTO.getBackMsg();
            throw new LuckException(msg, activityCommodityAddDTO);
        }

        updateStatus(groupActivityId, GroupActivityStatusEnum.ENABLE);
        changeSpuType(groupActivityId, spuId, null, StatusEnum.ENABLE);
    }

    @Override
    public void offlineGroupActivity(Long groupActivityId) {
        GroupActivityVO groupActivity = getByGroupActivityId(groupActivityId);
        updateStatus(groupActivityId, GroupActivityStatusEnum.OFFLINE);
        changeSpuType(groupActivityId, groupActivity.getSpuId(), null, StatusEnum.DISABLE);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(groupActivityId),ActivityChannelEnums.GROUP_ACTIVITY.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void offline(OfflineHandleEventDTO offlineHandleEventDto) {
        OfflineHandleEventDTO offlineHandleEvent = new OfflineHandleEventDTO();
        offlineHandleEvent.setHandleId(offlineHandleEventDto.getHandleId());
        offlineHandleEvent.setHandleType(OfflineHandleEventType.GROUP_BUY.getValue());
        offlineHandleEvent.setOfflineReason(offlineHandleEventDto.getOfflineReason());
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.save(offlineHandleEvent);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新活动状态为下线状态
        offlineGroupActivity(offlineHandleEventDto.getHandleId());
    }

    @Override
    public OfflineHandleEventVO getOfflineHandleEvent(Long couponId) {
        ServerResponseEntity<OfflineHandleEventVO> offlineHandleEventResponse =
                offlineHandleEventFeignClient.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.GROUP_BUY.getValue(), couponId);
        return offlineHandleEventResponse.getData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void audit(OfflineHandleEventDTO offlineHandleEventDto) {
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.auditOfflineEvent(offlineHandleEventDto);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        Long groupActivityId = offlineHandleEventDto.getHandleId();
        GroupActivityVO dbGroupActivityVO = getByGroupActivityId(groupActivityId);
        // 审核通过
        if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
            GroupActivityVO groupActivityVO = getByGroupActivityId(offlineHandleEventDto.getHandleId());
            updateStatus(groupActivityId, GroupActivityStatusEnum.DISABLE);
            changeSpuType(groupActivityId, dbGroupActivityVO.getSpuId(), null, StatusEnum.ENABLE);
            // 清除缓存
            GroupActivityServiceImpl groupActivityService = (GroupActivityServiceImpl) AopContext.currentProxy();
            groupActivityService.removeCache(dbGroupActivityVO.getSpuId());
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            updateStatus(offlineHandleEventDto.getHandleId(), GroupActivityStatusEnum.OFFLINE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void auditApply(OfflineHandleEventDTO offlineHandleEventDto) {
        // 更新事件状态
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.updateToApply(offlineHandleEventDto);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新优惠券为待审核状态
        updateStatus(offlineHandleEventDto.getHandleId(), GroupActivityStatusEnum.WAIT_AUDIT);
    }

    @Override
    public AppGroupActivityVO getAppGroupActivityInfo(Long spuId, Long storeId) {

        GroupActivityServiceImpl groupActivityService = (GroupActivityServiceImpl) AopContext.currentProxy();

        // 从缓存获取团购的数据
        AppGroupActivityVO appGroupActivityVO = groupActivityService.getBySpuIdAndStoreId(spuId, storeId);

        if (Objects.isNull(appGroupActivityVO)) {
            return null;
        }
        setAppGroupActivityInfo(appGroupActivityVO, storeId);
        return appGroupActivityVO;
    }

    private void setAppGroupActivityInfo(AppGroupActivityVO appGroupActivityVO, Long storeId) {
        ServerResponseEntity<List<SkuAppVO>> skuResponse = skuFeignClient.listBySpuIdAndStoreId(appGroupActivityVO.getSpuId(), storeId);
        if (!Objects.equals(skuResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(skuResponse.getMsg());
        }

        //过滤R渠道不参与拼团活动价
        boolean isRSpu=false;
        log.info("前置--> R渠道商品不参与限时调价活动(仅限官店) storeId:{} {}",storeId,(storeId==1?"官店不参与执行剔除逻辑":"门店参与"));
        if(Objects.nonNull(storeId) && storeId==1){
            List<Long> spuIds=new ArrayList<>();
            spuIds.add(appGroupActivityVO.getSpuId());
            ServerResponseEntity<List<Long>> serverResponseEntity=spuFeignClient.isSpuSkuChannel(new SpuSkuRDTO(SpuChannelEnums.CHANNEL_R.getCode(),spuIds));
            if(serverResponseEntity.isSuccess()
                    && CollectionUtil.isNotEmpty(serverResponseEntity.getData())
                    && serverResponseEntity.getData().size()>0){
                log.info("拼团活动id【{}】商品id【{}】为R渠道 不参与",appGroupActivityVO.getGroupActivityId(),appGroupActivityVO.getSpuId());
                isRSpu=true;
            }
        }
        boolean isInviteStore=false;
        ServerResponseEntity<Boolean> inviteStoreResponse=storeFeignClient.isInviteStore(storeId);
        if(inviteStoreResponse.isSuccess() && inviteStoreResponse.getData()){
            isInviteStore=inviteStoreResponse.getData();
        }


        Map<Long, SkuAppVO> skuMap = skuResponse.getData().stream().collect(Collectors.toMap(SkuAppVO::getSkuId, s -> s));
        List<AppGroupSkuVO> groupSkuList = appGroupActivityVO.getGroupSkuList();

        for (AppGroupSkuVO appGroupSkuVO : groupSkuList) {
            // 这里面团购商品详情sku返回的格式和普通商品的格式返回一致，方便前端生成sku筛选的东西
            SkuAppVO skuAppVO = skuMap.get(appGroupSkuVO.getSkuId());
            if (skuAppVO == null) {
                continue;
            }
            appGroupSkuVO.setSkuName(skuAppVO.getSkuName());
            appGroupSkuVO.setProperties(skuAppVO.getProperties());
            appGroupSkuVO.setImgUrl(skuAppVO.getImgUrl());
            // 如果没有市场价，就用售价当作市场价
            appGroupSkuVO.setMarketPriceFee(skuAppVO.getPriceFee());
            appGroupSkuVO.setStock(skuAppVO.getStock());
            appGroupSkuVO.setSpuId(skuAppVO.getSpuId());
            appGroupSkuVO.setStatus(1);

            if(isInviteStore){//虚拟门店取价
                if(skuAppVO.getSkuProtectPrice()>0){//有保护价取保护价，否则取拼团活动价
                    appGroupSkuVO.setPriceFee(skuAppVO.getPriceFee());
                }
            }else{
                //R渠道商品不参与拼团活动价
                if(isRSpu){
                    appGroupSkuVO.setPriceFee(skuAppVO.getPriceFee());
                }else if(skuAppVO.getStoreSkuStock()<=0 || (skuAppVO.getStoreProtectPrice()>0 && skuAppVO.getStoreSkuStock()>0)){
                    //剔除门店无库存(取官店价格)、门店有库存且有保护价(取保护价) -> sku,不参与活动价
                    appGroupSkuVO.setPriceFee(skuAppVO.getPriceFee());
                }
            }

        }
        //R渠道商品不参与拼团活动价
        if(isRSpu){
            appGroupActivityVO.setPrice(groupSkuList.get(0).getPriceFee());
        }else{
            //获取sku最低价展示
            AppGroupSkuVO skuPriceDTO = groupSkuList.stream().min(Comparator.comparing(AppGroupSkuVO::getPriceFee)).get();
            appGroupActivityVO.setPrice(skuPriceDTO.getPriceFee());
        }
    }


    @Override
    @Cacheable(cacheNames = GroupCacheNames.GROUP_BY_SPU_KEY, key = "#spuId")
    public AppGroupActivityVO getBySpuId(Long spuId) {
        return groupActivityMapper.getBySpuId(spuId);
    }

    @Override
    @Cacheable(cacheNames = GroupCacheNames.GROUP_BY_SPU_KEY, key = "#spuId+':'+#actvityId")
    public AppGroupActivityVO getBySpuIdAndActivityid(Long spuId, Long actvityId) {
        return groupActivityMapper.getBySpuIdAndActivityId(spuId,actvityId);
    }

    @Override
    public List<GroupActivitySpuVO> groupSpuListBySpuIds(List<Long> spuIds) {
        return groupActivityMapper.groupSpuListBySpuIds(spuIds);
    }

    @Override
    public List<GroupActivity> listUnEndButNeedEndActivity() {
        return groupActivityMapper.listUnEndButNeedEndActivity();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void changeProdTypeByGroupActivityIdList(List<GroupActivity> groupActivityList) {
        // 失效团购活动
        groupActivityMapper.expiredGroupActivityByGroupActivityIdList(groupActivityList);
        List<Long> spuIds = groupActivityList.stream().map(GroupActivity::getSpuId).collect(Collectors.toList());
        spuFeignClient.changeToNormalSpu(spuIds);
    }

    @Override
    @CacheEvict(cacheNames = GroupCacheNames.GROUP_BY_SPU_KEY, key = "#spuId")
    public void removeCache(Long spuId) {

    }

    @Override
    public AppGroupActivityVO getAppGroupActivityByGroupActivityId(Long groupActivityId, Long storeId) {
        AppGroupActivityVO appGroupActivityVO = groupActivityMapper.getAppGroupActivityByGroupActivityId(groupActivityId);

        if (appGroupActivityVO.getLimitStoreType() != null && appGroupActivityVO.getLimitStoreType() == 1) {
            List<GroupStore> groupStoreList = groupStoreMapper.selectList(new LambdaQueryWrapper<GroupStore>()
                    .eq(GroupStore::getGroupActivityId, groupActivityId).eq(GroupStore::getStoreId, storeId));
            if (CollectionUtils.isEmpty(groupStoreList)) {
                return null;
            }
        }
        if (Objects.isNull(appGroupActivityVO)) {
            return null;
        }
        setAppGroupActivityInfo(appGroupActivityVO, storeId);
        return appGroupActivityVO;
    }

    @Override
    public List<AppGroupActivityVO> groupListByStoreIdAndActivityId(Long storeId, AppGroupActivityDTO appGroupActivityDTO) {
        List<AppGroupActivityVO> appGroupActivityVOS = new ArrayList<>();
        for (Long activityId : appGroupActivityDTO.getActivityIdList()) {
            AppGroupActivityVO appGroupActivityVO = getAppGroupActivityByGroupActivityId(activityId, storeId);
            if (Objects.nonNull(appGroupActivityVO)) {
                appGroupActivityVOS.add(appGroupActivityVO);
            }
        }
        return appGroupActivityVOS;
    }

    @Override
    public AppGroupActivityVO getBySpuIdAndStoreId(Long spuId, Long storeId) {
        return groupActivityMapper.getBySpuIdAndStoreId(spuId, storeId);
    }

    @Override
    public void offlineGroupBySpuIds(List<Long> spuIds) {
        groupActivityMapper.expiredGroupActivityBySpuIds(spuIds);
    }

    /**
     * 更改商品类型
     *
     * @param groupActivityId
     * @param spuId
     * @param spuType
     * @param status
     */
    private void changeSpuType(Long groupActivityId, Long spuId, SpuType spuType, StatusEnum status) {
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setSpuId(spuId);
        spuDTO.setActivityId(groupActivityId);
        if (Objects.nonNull(spuType)) {
            spuDTO.setSpuType(spuType.value());
        } else {
            spuDTO.setSpuType(SpuType.GROUP.value());
        }
        if (Objects.nonNull(status)) {
            spuDTO.setStatus(status.value());
        }
        ServerResponseEntity<Void> spuResponse = spuFeignClient.changeSpuType(spuDTO);
        if (!Objects.equals(spuResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(spuResponse.getMsg());
        }
    }

    /**
     * 获取sku中的最低价格
     *
     * @param groupSkuList
     * @return
     */
    private Long getGroupSpuPrice(List<GroupSkuDTO> groupSkuList) {
        Long minPrice = null;
        for (GroupSkuDTO groupSku : groupSkuList) {
            if (Objects.isNull(minPrice) || groupSku.getActPrice() < minPrice) {
                minPrice = groupSku.getActPrice();
            }
        }
        return minPrice;
    }
}
