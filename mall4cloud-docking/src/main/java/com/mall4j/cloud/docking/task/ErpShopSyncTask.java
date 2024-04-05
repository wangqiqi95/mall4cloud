package com.mall4j.cloud.docking.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.delivery.feign.AreaFeignClient;
import com.mall4j.cloud.api.delivery.vo.AreaVO;
import com.mall4j.cloud.api.docking.skq_erp.dto.GetShopInfoDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushStoreDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.StoreIntegralRankReqDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.StoreIntegralRankRespDto;
import com.mall4j.cloud.api.docking.skq_erp.vo.ShopInfoVo;
import com.mall4j.cloud.api.docking.skq_erp.vo.StdPageResult;
import com.mall4j.cloud.api.platform.dto.StoreAddDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StdStoreVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdShopService;
import com.mall4j.cloud.docking.utils.StdClients;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RestController
@RequestMapping("/ua/erp/shop")
public class ErpShopSyncTask {

    private static final Logger log = LoggerFactory.getLogger(ErpShopSyncTask.class);

    @Autowired
    IStdShopService stdShopService;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    AreaFeignClient areaFeignClient;

    private final static String STORE_INTEGRAL_RANK="mall4cloud_docking:STORE_INTEGRAL_RANK_";

    @Resource
    private RedisTemplate redisTemplate;

    @XxlJob("erpShopSyncTask")
    @GetMapping("sync")
    public void erpShopSyncTask() {
        log.info("开始执行中台门店同步任务》》》》》》》》》》》》》》》》》》》》》");
        GetShopInfoDto getShopInfoDto = new GetShopInfoDto();
        getShopInfoDto.setPage(1);
        getShopInfoDto.setPageSize(200);
        ServerResponseEntity<StdPageResult<ShopInfoVo>> shopInfo = stdShopService.getShopInfo(getShopInfoDto);

        ServerResponseEntity<List<AreaVO>> areaResonse = areaFeignClient.allArea();

        Map<String, AreaVO> provinceMap = new HashMap<>();
        Map<String, AreaVO> cityMap = new HashMap<>();
        Map<String, AreaVO> areaMap = new HashMap<>();

        if (areaResonse != null && areaResonse.isSuccess() && areaResonse.getData().size() > 0) {
            List<AreaVO> areas = areaResonse.getData();
            Map<Integer, List<AreaVO>> allAreaMap = areas.stream().collect(Collectors.groupingBy(AreaVO::getLevel));

            //处理省的列表 去重复，单个名字只保留一个
            List<AreaVO> provinceList = allAreaMap.get(1).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            provinceMap = provinceList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //处理市的列表 去重复，单个名字只保留一个
            List<AreaVO> cityList = allAreaMap.get(2).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            cityMap = cityList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //处理区的列表 去重复，单个名字只保留一个
            List<AreaVO> areaList = allAreaMap.get(3).stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(AreaVO::getAreaName))), ArrayList::new));
            areaMap = areaList.stream().collect(Collectors.toMap(AreaVO::getAreaName, p -> p));

            //根据地区名称去重复
//            areas = areas.stream().collect(Collectors.collectingAndThen(
//                    Collectors.toCollection(() -> new TreeSet<>(
//                            Comparator.comparing(
//                                    AreaVO::getAreaName))), ArrayList::new));
//            Map<Long, AreaVO> areaMap = areas.stream().collect(Collectors.toMap(AreaVO::getAreaId,p->p));
        }

        if (shopInfo != null && shopInfo.isSuccess()) {
            StdPageResult<ShopInfoVo> data = shopInfo.getData();
            this.save(data.getResult(), provinceMap, cityMap, areaMap);
            Integer totalPage = data.getTotalPage();
            for (Integer i = 2; i <= totalPage; i++) {
                getShopInfoDto.setPage(i);
                shopInfo = stdShopService.getShopInfo(getShopInfoDto);
                if (shopInfo != null && shopInfo.isSuccess()) {
                    this.save(shopInfo.getData().getResult(), provinceMap, cityMap, areaMap);
                }
            }
        }
        log.info("结束执行中台门店同步任务》》》》》》》》》》》》》》》》》》》》》");
    }

    private void save(List<ShopInfoVo> result, Map<String, AreaVO> provinceMap, Map<String, AreaVO> cityMap, Map<String, AreaVO> areaMap) {
        ArrayList<ShopInfoVo> collect = result.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ShopInfoVo::geteCode))), ArrayList::new));

        List<StoreAddDTO> storeAddDtoList = collect.stream().map(shopInfoVo -> {
            StoreAddDTO storeAddDTO = new StoreAddDTO();
            storeAddDTO.setStationName(shopInfoVo.geteName());
            storeAddDTO.setStoreCode(shopInfoVo.geteCode());
            storeAddDTO.setErpCode(shopInfoVo.getErpCode());
            storeAddDTO.setLinkman(shopInfoVo.getCustomer());
            shopInfoVo.getShopType();
            storeAddDTO.setType(1);
            storeAddDTO.setIsShow(0);
            storeAddDTO.setType(Integer.valueOf(shopInfoVo.getShopType()));
            storeAddDTO.setStorenature(shopInfoVo.getNature());
            storeAddDTO.setProvince(shopInfoVo.getCpProvince());
            storeAddDTO.setAddr(shopInfoVo.getAddress());
            if (provinceMap.get(shopInfoVo.getCpProvince()) != null) {
                storeAddDTO.setProvinceId(provinceMap.get(shopInfoVo.getCpProvince()).getAreaId());
            }
            storeAddDTO.setCity(shopInfoVo.getCpCity());
            if (cityMap.get(shopInfoVo.getCpCity()) != null) {
                storeAddDTO.setCityId(cityMap.get(shopInfoVo.getCpCity()).getAreaId());
            }
            storeAddDTO.setArea(shopInfoVo.getCpDistrict());
            if (areaMap.get(shopInfoVo.getCpDistrict()) != null) {
                storeAddDTO.setAreaId(areaMap.get(shopInfoVo.getCpDistrict()).getAreaId());
            }
            if(StrUtil.isNotBlank(shopInfoVo.getSTATE_NAME())){
                storeAddDTO.setSlcName(shopInfoVo.getSTATE_NAME().trim());
            }
            storeAddDTO.setStatus(0);//默认关闭
//            storeAddDTO.setStatus(Objects.equals("Y", shopInfoVo.getIsActive()) ? 1 : 0);
            storeAddDTO.setSecondOrgName(shopInfoVo.getDepartName());
//            storeAddDTO.setThirdOrgName(shopInfoVo.getBlockName());
            return storeAddDTO;
        }).collect(Collectors.toList());
        // TODO 待提供保存接口
        storeFeignClient.sync(storeAddDtoList);
    }


    @XxlJob("erpPushShopSyncTask")
    @GetMapping("erpPushShopSyncTask")
    public void erpPushShopSyncTask() {
        log.info("开始执行推送中台售卖店铺任务》》》》》》》》》》》》》》》》》》》》》");
        ServerResponseEntity<List<StdStoreVO>> response= storeFeignClient.pushStdStores();
        if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData()) && response.getData().size()>0){
            List<PushStoreDto> pushStoreDtos=new ArrayList<>();
            response.getData().forEach(stdStoreVO -> {
                PushStoreDto pushStoreDto=new PushStoreDto();
                pushStoreDto.setCode(stdStoreVO.getStoreCode());
                pushStoreDto.setPlatform("XXY");
                pushStoreDto.setFilterType(stdStoreVO.getType());
                pushStoreDtos.add(pushStoreDto);
            });
            stdShopService.pushShops(pushStoreDtos);
        }
        log.info("结束执行推送中台售卖店铺任务》》》》》》》》》》》》》》》》》》》》》");
    }

    @XxlJob("storeIntegralRank")
    public void queryStoreIntegralRank() {
        log.info("开始执行查询中台门店排行》》》》》》》》》》》》》》》》》》》》》");

        StoreIntegralRankReqDto lastMonthReq = new StoreIntegralRankReqDto();
        Date date = new Date();
        lastMonthReq.setMonth(DateUtil.format(DateUtil.offsetMonth(date, -1), "yyyy-MM"));
        lastMonthReq.setPageNum(1);
        lastMonthReq.setPageSize(10);
        String lastRedisKey=STORE_INTEGRAL_RANK +lastMonthReq.getPageSize()+ lastMonthReq.getMonth();
        BoundValueOperations<String,List<StoreIntegralRankRespDto>> lastBoundValueOperations = redisTemplate.boundValueOps(lastRedisKey);
        lastBoundValueOperations.expire(0,TimeUnit.SECONDS);
        ServerResponseEntity<List<StoreIntegralRankRespDto>> lastResponseEntity = stdShopService.queryStoreIntegralRank(lastMonthReq);
        if(lastResponseEntity.isSuccess()&& CollectionUtil.isNotEmpty(lastResponseEntity.getData())){
            lastBoundValueOperations.set(lastResponseEntity.getData());
        }


        StoreIntegralRankReqDto currentMonthReq = new StoreIntegralRankReqDto();
        currentMonthReq.setMonth(DateUtil.format(date, "yyyy-MM"));
        currentMonthReq.setPageNum(1);
        currentMonthReq.setPageSize(10);
        String currentRedisKey=STORE_INTEGRAL_RANK +currentMonthReq.getPageSize()+ currentMonthReq.getMonth();
        BoundValueOperations<String,List<StoreIntegralRankRespDto>> currentBoundValueOperations = redisTemplate.boundValueOps(currentRedisKey);
        currentBoundValueOperations.expire(0,TimeUnit.SECONDS);
        ServerResponseEntity<List<StoreIntegralRankRespDto>> currentResponseEntity = stdShopService.queryStoreIntegralRank(currentMonthReq);
        if(currentResponseEntity.isSuccess()&& CollectionUtil.isNotEmpty(currentResponseEntity.getData())){
            currentBoundValueOperations.set(currentResponseEntity.getData());
        }

        log.info("结束执行查询中台门店排行》》》》》》》》》》》》》》》》》》》》》");
    }
}
