package com.mall4j.cloud.seckill.service.impl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.api.group.feign.MealCommodityPoolFeignClient;
import com.mall4j.cloud.api.group.feign.dto.CommodityPoolAddDTO;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.platform.vo.SysConfigApiVO;
import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.cache.constant.SeckillCacheNames;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.SekillActivitySpuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.seckill.constant.SeckillStatusEnum;
import com.mall4j.cloud.seckill.dto.SeckillDTO;
import com.mall4j.cloud.seckill.dto.SeckillSkuDTO;
import com.mall4j.cloud.seckill.mapper.SeckillMapper;
import com.mall4j.cloud.seckill.model.Seckill;
import com.mall4j.cloud.seckill.model.SeckillStore;
import com.mall4j.cloud.seckill.service.SeckillService;
import com.mall4j.cloud.seckill.service.SeckillSkuService;
import com.mall4j.cloud.seckill.service.SeckillStoreService;
import com.mall4j.cloud.seckill.vo.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 秒杀信息
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private OfflineHandleEventFeignClient offlineHandleEventFeignClient;

    @Autowired
    private SeckillSkuService seckillSkuService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient;

    @Autowired
    private SeckillStoreService seckillStoreService;
    @Autowired
    private MealCommodityPoolFeignClient mealCommodityPoolFeignClient;

    @Override
    public PageVO<SeckillVO> page(PageDTO pageDTO, SeckillDTO seckillDTO) {
        return PageUtil.doPage(pageDTO, () -> seckillMapper.list(seckillDTO));
    }

    @Override
    @Cacheable(cacheNames = SeckillCacheNames.SECKILL_BY_SECKILL_ID, key = "#seckillId")
    public SeckillVO getBySeckillId(Long seckillId) {
        return seckillMapper.getBySeckillId(seckillId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @GlobalTransactional(rollbackFor = Exception.class)
    public void saveSeckillAndItems(SeckillVO seckill, List<SeckillSkuDTO> seckillSkuList) {
        seckill.setStatus(StatusEnum.ENABLE.value());
        seckill.setIsDelete(StatusEnum.DISABLE.value());
        // 保存秒杀信息
        seckillMapper.save(seckill);
        seckillSkuList.forEach(seckillSku -> seckillSku.setSeckillId(seckill.getSeckillId()));

        // 保存门店信息
        seckillStoreService.deleteBySeckillId(seckill.getSeckillId());
        List<Long> storeIds=new ArrayList<>();
        if (seckill.getLimitStoreType() != null && seckill.getLimitStoreType() == 1
                && !CollectionUtils.isEmpty(seckill.getLimitStoreIdList())) {
            List<SeckillStore> seckillStoreList = seckill.getLimitStoreIdList().stream().map(s -> {
                SeckillStore seckillStore = new SeckillStore();
                seckillStore.setSeckillId(seckill.getSeckillId());
                seckillStore.setStoreId(s);

                storeIds.add(s);

                return seckillStore;
            }).collect(Collectors.toList());
            seckillStoreService.saveBatch(seckillStoreList);
        }

        // 保存秒杀sku信息
        seckillSkuService.saveBatch(seckillSkuList);

//        // 更新商品信息
//        Product product = new Product();
////        product.setSeckillActivityId(seckill.getSeckillId());
//        product.setActivityId(seckill.getSeckillId());
//        product.setProdType(ProdType.PROD_TYPE_SECKILL.value());
//        product.setProdId(seckill.getProdId());
//        productMapper.updateById(product);
//        // TODO 发送事件，清除掉如果是限时特惠中的可用商品
//        applicationContext.publishEvent(new RemoveDiscountProdByIdsEvent(Collections.singletonList(seckill.getProdId())));
        ArrayList<Long> spuIdList = new ArrayList<>();
        spuIdList.add(seckill.getSpuId());
        CommodityPoolAddDTO commodityPoolAddDTO = new CommodityPoolAddDTO();
        commodityPoolAddDTO.setActivityId(seckill.getSeckillId());
        commodityPoolAddDTO.setEndTime(seckill.getEndTime());
        commodityPoolAddDTO.setBeginTime(seckill.getStartTime());
        commodityPoolAddDTO.setSpuIdList(spuIdList);
        commodityPoolAddDTO.setShopIdList(storeIds);
        commodityPoolAddDTO.setActivityChannel(ActivityChannelEnums.SECKILL_ACTIVITY.getCode());
        ServerResponseEntity<String> stringServerResponseEntity = mealCommodityPoolFeignClient.commodityPoolAdd(commodityPoolAddDTO);
        if (stringServerResponseEntity.isFail()){
            throw new LuckException(stringServerResponseEntity.getMsg());
        }

        changeSpuType(seckill.getSeckillId(), seckill.getSpuId(), SpuType.SECKILL, null);
    }

    /**
     * 更改商品类型
     * @param seckillId 秒杀id
     * @param spuId 商品id
     * @param spuType 商品类型
     * @param status 商品状态
     */
    private void changeSpuType(Long seckillId, Long spuId, SpuType spuType, StatusEnum status){
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setSpuId(spuId);
        spuDTO.setActivityId(seckillId);
        if (Objects.nonNull(spuType)) {
            spuDTO.setSpuType(spuType.value());
        }
        if (Objects.nonNull(status)) {
            spuDTO.setStatus(status.value());
        }
        ServerResponseEntity<Void> spuResponse = spuFeignClient.changeSpuType(spuDTO);
        if (!Objects.equals(spuResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(spuResponse.getMsg());
        }
    }

    @Override
    public void deleteById(Long seckillId) {
        seckillMapper.deleteById(seckillId);
    }

    @Override
    @CacheEvict(cacheNames = SeckillCacheNames.SECKILL_BY_SECKILL_ID, key = "#seckillId")
    public void removeSeckillCacheById(Long seckillId) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void invalidById(Long seckillId,Long spuId) {
        SeckillVO seckillVO = new SeckillVO();
        // 失效
        seckillVO.setStatus(SeckillStatusEnum.DISABLE.value());
        seckillVO.setSeckillId(seckillId);
        seckillMapper.update(seckillVO);
        changeSpuType(0L,spuId, SpuType.NORMAL,null);

        //删除商品池，防止冲突
        CommodityPoolAddDTO commodityPoolAddDTO = new CommodityPoolAddDTO();
        commodityPoolAddDTO.setActivityId(seckillVO.getSeckillId());
        commodityPoolAddDTO.setActivityChannel(ActivityChannelEnums.SECKILL_ACTIVITY.getCode());
        ServerResponseEntity<String> stringServerResponseEntity = mealCommodityPoolFeignClient.commodityPoolRemove(commodityPoolAddDTO);
        if (stringServerResponseEntity.isFail()){
            throw new LuckException(stringServerResponseEntity.getMsg());
        }
    }

    @Override
    public void updateById(SeckillVO seckill) {
        seckillMapper.update(seckill);
    }

    @Override
    public void updateStatus(Long seckillId, SeckillStatusEnum status) {
        SeckillVO seckillVO = new SeckillVO();
        seckillVO.setStatus(status.value());
        seckillVO.setSeckillId(seckillId);
        seckillMapper.update(seckillVO);
    }

    @Override
    public OfflineHandleEventVO getOfflineHandleEvent(Long seckillId) {
        ServerResponseEntity<OfflineHandleEventVO> offlineHandleEventResponse =
                offlineHandleEventFeignClient.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.SECKILL.getValue(), seckillId);
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
        // 审核通过
        if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
            SeckillVO seckillVO = getBySeckillId(offlineHandleEventDto.getHandleId());
            updateStatus(seckillVO.getSeckillId(), SeckillStatusEnum.ENABLE);
            changeSpuType(seckillVO.getSeckillId(), seckillVO.getSpuId(), null, StatusEnum.ENABLE);
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDto.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            updateStatus(offlineHandleEventDto.getHandleId(), SeckillStatusEnum.OFFLINE);
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
        // 更新秒杀活动为待审核状态
        updateStatus(offlineHandleEventDto.getHandleId(), SeckillStatusEnum.WAIT_AUDIT);
    }

    @Override
    public AppSeckillVO pageBySelectLot(SeckillDTO seckillDTO) {
        AppSeckillVO appSeckillVO = new AppSeckillVO();
        List<SeckillVO> seckillList = seckillMapper.listBySelectLot(seckillDTO);
        if(CollectionUtil.isEmpty(seckillList)){
            return appSeckillVO;
        }
        List<SeckillSpuVO> seckillSpuList = new ArrayList<>();

        List<Long> idList = seckillList.stream().map(SeckillVO::getSpuId).collect(Collectors.toList());
        List<SpuSearchVO> spuSearchVOList = searchSpuFeignClient.listSpuBySpuIds(idList).getData();
        // 转成map方便操作
        Map<Long, SpuSearchVO> spuMap = spuSearchVOList.stream().collect(Collectors.toMap(SpuSearchVO::getSpuId, spuSearchVO -> spuSearchVO));
        for (SeckillVO seckillVO : seckillList) {
            if (!spuMap.containsKey(seckillVO.getSpuId())) {
                continue;
            }
            SpuSearchVO spuSearchVO = spuMap.get(seckillVO.getSpuId());
            // 如果为空则把最近的开始时间返回给前端
            if(Objects.isNull(appSeckillVO.getNextTime())){
                long startTime = DateUtil.offsetHour(seckillVO.getStartTime(), seckillVO.getSelectedLot()).getTime();
                long endTime = new DateTime(seckillVO.getEndTime()).getTime();
                // 如果小于当前时间则表示已经开始了，就表示已经开始了
                if(startTime > System.currentTimeMillis()) {
                    appSeckillVO.setNextTime(startTime - System.currentTimeMillis());
                }else{
                    appSeckillVO.setNextTime(System.currentTimeMillis() - endTime);
                }
                appSeckillVO.setSelectedLot(seckillVO.getSelectedLot());
            }
            SeckillSpuVO seckillSpuVO = new SeckillSpuVO();
            seckillSpuVO.setSeckillId(seckillVO.getSeckillId());
            seckillSpuVO.setMainImgUrl(spuSearchVO.getMainImgUrl());
            seckillSpuVO.setName(spuSearchVO.getSpuName());
            seckillSpuVO.setPriceFee(spuSearchVO.getPriceFee());
            seckillSpuVO.setReducePrice(spuSearchVO.getPriceFee() - seckillVO.getSeckillPrice());
            seckillSpuVO.setSeckillOriginStocks(seckillVO.getSeckillOriginStocks());
            seckillSpuVO.setSeckillTotalStocks(seckillVO.getSeckillTotalStocks());
            seckillSpuVO.setSpuId(seckillVO.getSpuId());
            seckillSpuVO.setSeckillPrice(seckillVO.getSeckillPrice());
            seckillSpuList.add(seckillSpuVO);
        }
        appSeckillVO.setSeckillSpuList(seckillSpuList);
        return appSeckillVO;
    }

    @Override
    @Cacheable(cacheNames = SeckillCacheNames.SECKILL_BY_SPU_ID, key = "#spuId", sync = true)
    public SeckillVO getBySpuId(Long spuId) {
        return seckillMapper.getBySpuId(spuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void offline(OfflineHandleEventDTO offlineHandleEventDto, Long spuId) {
        OfflineHandleEventDTO offlineHandleEvent = new OfflineHandleEventDTO();
        offlineHandleEvent.setOfflineReason(offlineHandleEventDto.getOfflineReason());
        offlineHandleEvent.setHandleType(OfflineHandleEventType.SECKILL.getValue());
        offlineHandleEvent.setHandleId(offlineHandleEventDto.getHandleId());
        ServerResponseEntity<Void> responseEntity = offlineHandleEventFeignClient.save(offlineHandleEvent);
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        // 更新活动状态为违规下架状态
        updateStatus(offlineHandleEventDto.getHandleId(), SeckillStatusEnum.OFFLINE);
        changeSpuType(offlineHandleEventDto.getHandleId(),spuId, SpuType.SECKILL, StatusEnum.DISABLE);
    }

    @Override
    public List<SekillActivitySpuVO> listBySelectLotAndSpuIds(Integer selectedLot, List<Long> spuIds) {
        return seckillMapper.listBySelectLotAndSpuIds(selectedLot,spuIds);
    }

    @Override
    public PageVO<SeckillAdminVO> listByShopId(PageDTO pageDTO, Long shopId, Integer type, Long specTime) {
        String config = configFeignClient.getConfig(ConfigNameConstant.SECKILL_TIME_CONFIG).getData();
        if(Objects.isNull(config)){
            throw new LuckException("请先完善秒杀时段配置");
        }
        SeckillConfigVO seckillConfigVO = Json.parseObject(config, SeckillConfigVO.class);
        List<Integer> seckillTimeList = seckillConfigVO.getSeckillTimeList();
        if(CollectionUtil.isEmpty(seckillTimeList)){
                PageVO<SeckillAdminVO> pageVO = new PageVO<>();
                pageVO.setPages(0);
                pageVO.setTotal(0L);
                return pageVO;
        }
        // 获取未开场时间,如果当前的秒杀场次已经结束，则为第二天的开始场次
        List<SeckillAdminVO> seckillList = new ArrayList<>();
        int isClose = 0;
        DateTime specDate = null;
        String specDateStr = null;
        // 如果未已经开始的活动列表
        if(type == 1){
            isClose = 2;
            specDate = DateUtil.offsetHour(new Date(), 1);
            specDateStr = specDate.toString();
        }
        List<SeckillAdminVO> adminListDb = seckillMapper.listByShopId(shopId, specDateStr,isClose);
        // 如果没有秒杀活动则统一设置为0
        Map<Date,SeckillAdminVO> seckillDbMap = adminListDb.stream().collect(Collectors.toMap(SeckillAdminVO::getDateTime,seckillAdminVO -> seckillAdminVO));
        // 0未开始，1已经开始
        if(type == 0) {
            seckillList = getUnStartSeckillList(seckillConfigVO,seckillDbMap,specTime);
        }else if(type == 1){
            seckillList = getStartSeckillList(seckillConfigVO,seckillDbMap);
        }
        PageVO<SeckillAdminVO> pageVO = new PageVO<>();
        int startNum = (pageDTO.getPageNum() - 1) * pageDTO.getPageSize();
        int endNum = pageDTO.getPageNum() * pageDTO.getPageSize();
        List<SeckillAdminVO> seckillAdminList = new ArrayList<>();
        // 进行分页
        for (int i = startNum; i < endNum; i++) {
            if(i + 1 > seckillList.size()){
                break;
            }
            SeckillAdminVO seckillAdminVO = seckillList.get(i);
            seckillAdminList.add(seckillAdminVO);
        }
        pageVO.setList(seckillAdminList);
        pageVO.setTotal((long) seckillList.size());
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    /**
     * 获取已经开始的秒杀信息
     * @param seckillConfigVO
     * @param seckillDbMap
     * @return
     */
    private List<SeckillAdminVO> getStartSeckillList(SeckillConfigVO seckillConfigVO, Map<Date, SeckillAdminVO> seckillDbMap) {
        ArrayList<SeckillAdminVO> seckillAdminList = new ArrayList<>();
        Date date = new Date();
        // 秒杀活动只持续2个小时所以找当前这个小时，及前一个开始的即可
        int hour = DateUtil.hour(date,true);

        // 当前天的起始时间
        DateTime beginTime = DateUtil.beginOfDay(new Date());
        for (Integer time : seckillConfigVO.getSeckillTimeList()) {
            // 判断一下时间
            if(hour != time && hour != time + 1){
                continue;
            }
            // 处理时间
            DateTime dateTime = DateUtil.offsetHour(beginTime, time);
            DateTime endTime = DateUtil.offsetHour(beginTime,time + 2);
            SeckillAdminVO seckillAdminVO = new SeckillAdminVO();
            seckillAdminVO.setJoinShopNum(0);
            seckillAdminVO.setProdNum(0);
            if(CollectionUtil.isNotEmpty(seckillDbMap) && seckillDbMap.containsKey(dateTime)){
                SeckillAdminVO seckillDb = seckillDbMap.get(dateTime);
                seckillAdminVO = mapperFacade.map(seckillDb,SeckillAdminVO.class);
            }
            seckillAdminVO.setSelectedLot(time);
            seckillAdminVO.setStartTime(dateTime);
            seckillAdminVO.setEndTime(endTime);
            seckillAdminVO.setCanJoin(0);
            seckillAdminList.add(seckillAdminVO);
        }
        return seckillAdminList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void saveSeckillTime(SysConfigApiVO config) {
        // 获取配置信息
        String timeValueDb = configFeignClient.getConfig(config.getParamKey()).getData();
        SeckillConfigVO seckillConfigDb = Json.parseObject(timeValueDb, SeckillConfigVO.class);
        SeckillConfigVO seckillConfig = Json.parseObject(config.getParamValue(), SeckillConfigVO.class);
        // 如果没有配置信息或者配置时段的数据，则只需要保存即可，无需回退所有的秒杀商品状态
        if(Objects.isNull(seckillConfigDb) || CollectionUtil.isEmpty(seckillConfigDb.getSeckillTimeList())){
            // 保存配置信息
            configFeignClient.saveOrUpdateSysConfig(config);
            return;
        }
        // 修改配置信息
        configFeignClient.saveOrUpdateSysConfig(config);
        List<Long> spuIds = new ArrayList<>();
        List<SeckillVO> seckillList = null;
        // 如果改变后为空，则移除所有的秒杀商品信息
        if(Objects.isNull(seckillConfig) || CollectionUtil.isEmpty(seckillConfig.getSeckillTimeList())){
            // 查询所有需要删除掉秒杀信息
            seckillList = seckillMapper.list(null);
        }else {
            // 对比移除了那些时段，将时段上的秒杀全部删除，并恢复商品为普通商品状态
            // 先获取所有移除的时段
            List<Integer> selectedLots = new ArrayList<>();
            for (Integer timeDb : seckillConfigDb.getSeckillTimeList()) {
                boolean alreadly = false;
                for (Integer time : seckillConfig.getSeckillTimeList()) {
                    if (Objects.equals(timeDb, time)) {
                        alreadly = true;
                        break;
                    }
                }
                if (!alreadly) {
                    selectedLots.add(timeDb);
                }
            }
            // 查询所有需要删除掉秒杀信息
            if(CollectionUtil.isNotEmpty(selectedLots)) {
                seckillList = seckillMapper.listBySelectLotList(selectedLots);
            }
        }
        if(CollectionUtil.isEmpty(seckillList)){
            return;
        }
        for (SeckillVO seckillVO : seckillList) {
            spuIds.add(seckillVO.getSpuId());
            seckillVO.setIsDelete(1);
        }
        // 删除所有的秒杀活动(逻辑删除)
        seckillMapper.updateListToDelete(seckillList);
        // 移除商品的秒杀信息，失效？
        for (Long spuId : spuIds) {
            changeSpuType(0L,spuId,SpuType.NORMAL,null);
        }
    }

    @Override
    public PageVO<SeckillSpuVO> listSeckillSpuByTime(PageDTO pageDTO, Long startTime, ProductSearchDTO productSearch) {
        // 获取秒杀批次及起始时间
        DateTime dateTime = new DateTime(startTime);
        int hour = dateTime.hour(true);
        productSearch.setSelectedLot(hour);
        productSearch.setSpuType(SpuType.SECKILL.value());
        productSearch.setActivityTime(DateUtil.beginOfDay(dateTime).getTime());
        productSearch.setPageSize(pageDTO.getPageSize());
        productSearch.setPageNum(pageDTO.getPageNum());
        // 秒杀详情，重新设计，秒杀中失效的商品也要显示，所以，不用从es中查询
//        DateTime activityStartTime = DateUtil.beginOfDay(dateTime);

        if (StrUtil.isNotBlank(productSearch.getKeyword())){
            List<String> strings = Arrays.asList(productSearch.getKeyword().split(","));
            List<Long> spuIds = strings.stream().map(Long::valueOf).collect(Collectors.toList());
            productSearch.setSpuIds(spuIds);
        }

        log.info("listSeckillSpuByTime param : {}", JSONObject.toJSONString(productSearch));
        long total = seckillMapper.countListBySearchParam(dateTime, productSearch);
        log.info("listSeckillSpuByTime total : {}", total);
        if (total < 1) {
            PageVO<SeckillSpuVO> pageVO = new PageVO<>();
            pageVO.setPages(0);
            pageVO.setTotal(total);
            return pageVO;
        }
        // 获取秒杀信息
        PageVO<SeckillSpuVO> seckillSpuListPage = PageUtil.doPage(pageDTO, () -> seckillMapper.listByStartTimeAndSelectedLot(productSearch,dateTime));
        List<SeckillSpuVO> seckillSpuList =  seckillSpuListPage.getList();
        // 获取秒杀关联商品ids
        List<Long> spuIds = seckillSpuList.stream().map(SeckillSpuVO::getSpuId).collect(Collectors.toList());
        // 获取商品的数据
        ServerResponseEntity<List<SpuVO>> spuResponse = spuFeignClient.listSpuBySpuIds(spuIds);
        if (!spuResponse.isSuccess()) {
            throw new LuckException(spuResponse.getMsg());
        }
        List<SpuVO> spuList = spuResponse.getData();
        Map<Long,SpuVO > spuDbMap = spuList.stream().collect(Collectors.toMap(SpuVO::getSpuId, spuVO -> spuVO));
        // 获取指定门店数据
        List<Long> seckilIdList = seckillSpuList.stream().map(SeckillSpuVO::getSeckillId).collect(Collectors.toList());
        List<SeckillStore> seckillStoreList = seckillStoreService.listBySeckillIdList(seckilIdList);
        Map<Long, List<SeckillStore>> seckillStoreMap = seckillStoreList.stream().collect(Collectors.groupingBy(SeckillStore :: getSeckillId));
        for (SeckillSpuVO seckillSpuVO : seckillSpuList) {
            SpuVO spuVO = spuDbMap.get(seckillSpuVO.getSpuId());
            if(Objects.isNull(spuVO)){
                seckillSpuVO.setName("商品已删除");
                seckillSpuVO.setShopName(seckillSpuVO.getShopName());
                seckillSpuVO.setPriceFee(0L);
            }else{
                seckillSpuVO.setName(spuVO.getName());
                seckillSpuVO.setShopName(spuVO.getShopName());
                seckillSpuVO.setPriceFee(spuVO.getPriceFee());
            }
            if (seckillStoreMap.containsKey(seckillSpuVO.getSeckillId())) {
                seckillSpuVO.setLimitStoreList(seckillStoreMap.get(seckillSpuVO.getSeckillId()).stream().map(SeckillStore :: getStoreId)
                        .collect(Collectors.toList()));
            }
        }
        return seckillSpuListPage;
    }

    /**
     * 根据店铺id获取数据库中存在的秒杀活动信息并转成map
     * @param seckillConfigVO 配置信息
     * @param seckillDbMap 秒杀数据库的信息map
     * @param specTime 当前选择时间
     * @return 秒杀活动list
     */
    private List<SeckillAdminVO> getUnStartSeckillList(SeckillConfigVO seckillConfigVO, Map<Date, SeckillAdminVO> seckillDbMap, Long specTime) {
        Date nowTime = new Date();
        int nowHour = DateUtil.hour(nowTime, true);
        int startHour = nowHour;
        List<Integer> seckillTimeList = seckillConfigVO.getSeckillTimeList();
        boolean conditions = Objects.nonNull(specTime) && specTime != 0;

        // 当前天的起始时间
        DateTime beginTime = conditions ? new DateTime(specTime) :DateUtil.beginOfDay(nowTime);
        List<SeckillAdminVO> adminList = new ArrayList<>();
        // 如果当前条件选择的时间小于当前日期，就直接返回了
        if(conditions && DateUtil.compare(beginTime, DateUtil.beginOfDay(nowTime)) < 0){
            return adminList;
        }
        // 是否为当天
        boolean isToday = Objects.equals(DateUtil.beginOfDay(nowTime), beginTime);
        // 获取未开场时间,如果当前的秒杀场次已经结束，则为第二天的开始场次
        boolean nextDay = true;
        for (Integer time : seckillTimeList) {
            if(startHour < time){
                startHour = time;
                nextDay = false;
                break;
            }
        }
        // 如果没有秒杀活动则统一设置为0
        for (int i = 0; i < Constant.MONTH_DAY; i++) {
            if(conditions && i > 0){
                break;
            }
            if(nextDay && i == 0){
                continue;
            }
            for (Integer time : seckillTimeList) {
                int canJoin = 1;
                // 如果是第一天或者最后一天的数据，需要判断一下时间
                if(i == 0){
                    // 如果是当天并且秒杀小时为(当前小时+1)则直接跳过
                    if(startHour > time && isToday) {
                        continue;
                    }
                    // 如果是当天并且为(当前小时+1)则返回不能添加
                    if(isToday && nowHour + 1 == time){
                        canJoin = 0;
                    }
                }
                // 处理时间
                DateTime dateTime = DateUtil.offsetHour(DateUtil.offsetDay(beginTime, i), time);
                DateTime endTime = DateUtil.offsetHour(DateUtil.offsetDay(beginTime, i), time + 2);
                SeckillAdminVO seckillAdminVO = new SeckillAdminVO();
                seckillAdminVO.setProdNum(0);
                seckillAdminVO.setJoinShopNum(0);
                if(CollectionUtil.isNotEmpty(seckillDbMap) && seckillDbMap.containsKey(dateTime)){
                    SeckillAdminVO seckillDb = seckillDbMap.get(dateTime);
                    seckillAdminVO = mapperFacade.map(seckillDb,SeckillAdminVO.class);
                }
                seckillAdminVO.setStartTime(dateTime);
                seckillAdminVO.setSelectedLot(time);
                seckillAdminVO.setEndTime(endTime);
                seckillAdminVO.setCanJoin(canJoin);
                adminList.add(seckillAdminVO);
            }
        }
        return adminList;
    }

    @Override
    public List<Seckill> listUnEndButNeedEndActivity() {
        return seckillMapper.listUnEndButNeedEndActivity();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void changeProdTypeBySeckillIdList(List<Seckill> seckillList) {
        seckillMapper.changeSeckillActivityStatusBySeckillIdList(seckillList);
        List<Long> spuIds = seckillList.stream().map(Seckill::getSpuId).collect(Collectors.toList());
        spuFeignClient.changeToNormalSpu(spuIds);
//        changeSpuType(offlineHandleEventDto.getHandleId(),spuId, SpuType.SECKILL, StatusEnum.DISABLE);
    }


    @Override
    public PageVO<SeckillAdminVO> listEndSeckillByShopId(PageDTO pageDTO, Long shopId, Long specTime) {
        DateTime specDate = Objects.nonNull(specTime) && specTime != 0 ? new DateTime(specTime) : null;
        String date = Objects.nonNull(specDate)? specDate.toString("yyyy-MM-dd") : null;
        PageVO<SeckillAdminVO> page = PageUtil.doPage(pageDTO, () -> seckillMapper.listByShopId(shopId,date, 1));
        for (SeckillAdminVO seckillAdminVO : page.getList()) {
            seckillAdminVO.setCanJoin(0);
        }
        return page;
    }

    @Override
    public Integer getJoinSeckillMerchantNum() {
        return seckillMapper.getJoinSeckillMerchantNum();
    }

    @Override
    public void offlineSeckillBySpuIds(List<Long> spuIds) {
        seckillMapper.offlineSeckillBySpuIds(spuIds);
        spuFeignClient.changeToNormalSpu(spuIds);
    }

    @Override
    public void offlineSeckillByShopId(Long shopId) {
        List<Long> spuIds = seckillMapper.listSpuIdIdByShopId(shopId);
        if (CollUtil.isEmpty(spuIds)) {
            return;
        }
        offlineSeckillBySpuIds(spuIds);
    }

    @Override
    public SeckillVO getBySeckillIdAndStoreId(Long seckillId, Long storeId) {
        return seckillMapper.getBySeckillIdAndStoreId(seckillId,storeId);
    }

}
