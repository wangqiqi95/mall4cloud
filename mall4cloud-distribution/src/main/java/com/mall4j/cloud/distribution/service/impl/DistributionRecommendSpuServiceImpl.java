package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuUpdateDTO;
import com.mall4j.cloud.distribution.mapper.DistributionRecommendSpuMapper;
import com.mall4j.cloud.distribution.model.DistributionRecommendSpu;
import com.mall4j.cloud.distribution.service.DistributionCommissionService;
import com.mall4j.cloud.distribution.service.DistributionRecommendSpuService;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuExcelVO;
import com.mall4j.cloud.distribution.vo.DistributionRecommendSpuVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
@Service
@Slf4j
public class DistributionRecommendSpuServiceImpl implements DistributionRecommendSpuService {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private DistributionRecommendSpuMapper distributionRecommendSpuMapper;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private DistributionCommissionService distributionCommissionService;
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public PageVO<DistributionRecommendSpuVO> page(PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        PageVO<DistributionRecommendSpuVO> pageVO = new PageVO<>();
        PageVO<SpuCommonVO> esPageVO = productSearch(distributionRecommendSpuQueryDTO, pageDTO);
        Long total = Objects.isNull(esPageVO) ? 0l : esPageVO.getTotal();
        pageVO.setTotal(total);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        if (Objects.equals(0L, total)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }
        List<SpuCommonVO> spuCommonVOList = esPageVO.getList();
        List<Long> spuIdList = spuCommonVOList.stream().map(SpuCommonVO :: getSpuId).collect(Collectors.toList());
        List<DistributionRecommendSpu> recommendSpuList=distributionRecommendSpuMapper.listBySpuIdList(spuIdList);

        //取重复(取end_time最久的时间) 查数据需要end_time排序倒叙
        TreeSet<DistributionRecommendSpu> treeSet=new TreeSet<DistributionRecommendSpu>(new Comparator<DistributionRecommendSpu>() {
            @Override
            public int compare(DistributionRecommendSpu o1, DistributionRecommendSpu o2) {
                String key1=o1.getSpuId().toString();
                String key2=o2.getSpuId().toString();
                int re=  key1.compareTo(key2);
                return re;
            }
        });
        treeSet.addAll(recommendSpuList);
        List<DistributionRecommendSpu> recommendSpuListCompare = new ArrayList<>(treeSet);

        Map<Long, DistributionRecommendSpu> recommendSpuMap = recommendSpuListCompare.stream().collect(Collectors.toMap(DistributionRecommendSpu :: getSpuId, Function.identity()));
        Map<Long, DistributionCommissionRateVO> distributionCommissionRateVOMap = null;
        if (distributionRecommendSpuQueryDTO.isShowCommissionRate()) {
            DistributionCommissionRateDTO distributionCommissionRateDTO = new DistributionCommissionRateDTO();
            distributionCommissionRateDTO.setStoreId(distributionRecommendSpuQueryDTO.getStoreId());
            distributionCommissionRateDTO.setSpuIdList(spuIdList);
            distributionCommissionRateVOMap = distributionCommissionService.getDistributionCommissionRate(distributionCommissionRateDTO);
        }
        pageVO.setList(buildDistributionRecommendSpuVOList(spuCommonVOList, recommendSpuMap, distributionCommissionRateVOMap));
        return pageVO;

    }

//    @Override
//    public PageVO<DistributionRecommendSpuVO> page(PageDTO pageDTO, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
//        PageVO<DistributionRecommendSpuVO> pageVO = new PageVO<>();
//        PageVO<SpuCommonVO> esPageVO = productSearch(distributionRecommendSpuQueryDTO, pageDTO);
//        Long total = Objects.isNull(esPageVO) ? 0l : esPageVO.getTotal();
//        pageVO.setTotal(total);
//        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
//        if (Objects.equals(0L, total)) {
//            pageVO.setList(Collections.emptyList());
//            return pageVO;
//        }
//        List<SpuCommonVO> spuCommonVOList = esPageVO.getList();
//        List<Long> spuIdList = spuCommonVOList.stream().map(SpuCommonVO :: getSpuId).collect(Collectors.toList());
//        Map<Long, DistributionRecommendSpu> recommendSpuMap = distributionRecommendSpuMapper.listBySpuIdList(spuIdList)
//                .stream().collect(Collectors.toMap(DistributionRecommendSpu :: getSpuId, Function.identity()));
//        Map<Long, DistributionCommissionRateVO> distributionCommissionRateVOMap = null;
//        if (distributionRecommendSpuQueryDTO.isShowCommissionRate()) {
//            DistributionCommissionRateDTO distributionCommissionRateDTO = new DistributionCommissionRateDTO();
//            distributionCommissionRateDTO.setStoreId(distributionRecommendSpuQueryDTO.getStoreId());
//            distributionCommissionRateDTO.setSpuIdList(spuIdList);
//            distributionCommissionRateVOMap = distributionCommissionService.getDistributionCommissionRate(distributionCommissionRateDTO);
//        }
//        pageVO.setList(buildDistributionRecommendSpuVOList(spuCommonVOList, recommendSpuMap, distributionCommissionRateVOMap));
//        return pageVO;
//
//    }

    @Override
    public DistributionRecommendSpuVO getById(Long id) {
        DistributionRecommendSpu distributionRecommendSpu = distributionRecommendSpuMapper.getById(id);
        DistributionRecommendSpuVO distributionRecommendSpuVO = mapperFacade.map(distributionRecommendSpu, DistributionRecommendSpuVO.class);
        ServerResponseEntity<SpuVO> spuResp = spuFeignClient.getById(distributionRecommendSpu.getSpuId());
        if (spuResp.isSuccess()) {
            SpuVO spuVO = spuResp.getData();
            distributionRecommendSpuVO.setSpuName(spuVO.getName());
            distributionRecommendSpuVO.setMainImgUrl(spuVO.getMainImgUrl());
        }
        return distributionRecommendSpuVO;
    }

    @Override
    public void save(DistributionRecommendSpuDTO distributionRecommendSpuDTO) {
        if (CollectionUtils.isEmpty(distributionRecommendSpuDTO.getSpuIdList())) {
            throw new LuckException("商品ID集合不能为空");
        }

//        if(distributionRecommendSpuDTO.getStatus()==1){
//            checkSpuClash(distributionRecommendSpuDTO);
//        }

        List<DistributionRecommendSpu> distributionRecommendSpuList = new ArrayList<>();
        distributionRecommendSpuDTO.getSpuIdList().stream().forEach(s-> {
            DistributionRecommendSpu distributionRecommendSpu = mapperFacade.map(distributionRecommendSpuDTO, DistributionRecommendSpu.class);
            distributionRecommendSpu.setId(null);
            distributionRecommendSpu.setSpuId(s);
            String limitStoreIds=null;
            if (distributionRecommendSpuDTO.getLimitStoreType() == 1 && CollectionUtil.isNotEmpty(distributionRecommendSpuDTO.getLimitStoreIdList())) {
                limitStoreIds = distributionRecommendSpuDTO.getLimitStoreIdList().stream().map(storeIdItem -> storeIdItem.toString()).collect(Collectors.joining(","));
            }
            distributionRecommendSpu.setLimitStoreIds(limitStoreIds);

            distributionRecommendSpuList.add(distributionRecommendSpu);
        });
        distributionRecommendSpuMapper.saveBatch(distributionRecommendSpuList);
    }

    //商品去重：全部门店 部分门店
    private void checkSpuClash(DistributionRecommendSpuDTO spuDTO){
        List<DistributionRecommendSpu> recommendSpuList=distributionRecommendSpuMapper.listSpuIdListByParamClash(spuDTO.getStartTime(),spuDTO.getEndTime(),spuDTO.getSpuIdList(),spuDTO.getLimitStoreIdList());
        if(CollectionUtil.isNotEmpty(recommendSpuList)){
            List<String> errorLog=new ArrayList<>();
            List<Long> spuIdList = recommendSpuList.stream().map(DistributionRecommendSpu :: getSpuId).collect(Collectors.toList());
            ServerResponseEntity<List<SpuVO>> response=spuFeignClient.listGiftSpuBySpuIds(spuIdList);
            if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
                Map<Long, SpuVO> recommendSpuMap = response.getData()
                .stream().collect(Collectors.toMap(SpuVO::getSpuId, a -> a,(k1, k2)->k1));

                recommendSpuList.forEach(recommendSpu->{
                    if(recommendSpuMap.containsKey(recommendSpu.getSpuId())){
                        errorLog.add("商品货号"+recommendSpuMap.get(recommendSpu.getSpuId()).getSpuCode()
                                +"冲突,时间"+ DateUtil.format(recommendSpu.getStartTime(),"yyyy-MM-dd HH:mm:ss")
                                +"~"+DateUtil.format(recommendSpu.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
                    }
                });
            }
            if(errorLog.size()>0){
                throw new LuckException(errorLog.toString());
            }
        }
    }

    @Override
    public void update(DistributionRecommendSpuDTO distributionRecommendSpuDTO) {
        if (distributionRecommendSpuDTO.getId() == null) {
            throw new LuckException("ID不能为空");
        }
        DistributionRecommendSpu distributionRecommendSpu = distributionRecommendSpuMapper.getById(distributionRecommendSpuDTO.getId());
        if (Objects.isNull(distributionRecommendSpu)) {
            throw new LuckException("数据不存在");
        }
//        if(distributionRecommendSpuDTO.getStatus()==1){
//            checkSpuClash(distributionRecommendSpuDTO);
//        }
        distributionRecommendSpu.setStartTime(distributionRecommendSpuDTO.getStartTime());
        distributionRecommendSpu.setEndTime(distributionRecommendSpuDTO.getEndTime());
        distributionRecommendSpu.setLimitStoreType(distributionRecommendSpuDTO.getLimitStoreType());
        if (!CollectionUtils.isEmpty(distributionRecommendSpuDTO.getLimitStoreIdList())) {
            distributionRecommendSpu.setLimitStoreIds(distributionRecommendSpuDTO.getLimitStoreIdList().stream()
                    .map(s -> String.valueOf(s)).collect(Collectors.joining(",")));
        }
        distributionRecommendSpuMapper.update(distributionRecommendSpu);
    }

    @Override
    public void updateStatus(DistributionRecommendSpuUpdateDTO distributionRecommendSpuUpdateDTO) {

//        if(distributionRecommendSpuUpdateDTO.getStatus()==1){
//            distributionRecommendSpuUpdateDTO.getIdList().forEach(id->{
//                DistributionRecommendSpuVO recommendSpuVO=this.getById(id);
//                DistributionRecommendSpuDTO recommendSpuDTO=new DistributionRecommendSpuDTO();
//                recommendSpuDTO.setStartTime(recommendSpuVO.getStartTime());
//                recommendSpuDTO.setEndTime(recommendSpuVO.getEndTime());
//                List<Long> spuIds=new ArrayList<>();
//                spuIds.add(recommendSpuVO.getSpuId());
//                recommendSpuDTO.setSpuIdList(spuIds);
//                if(StrUtil.isNotBlank(recommendSpuVO.getLimitStoreIds())){
//                    List<Long> storeIds=Arrays.asList(recommendSpuVO.getLimitStoreIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
//                    recommendSpuDTO.setLimitStoreIdList(storeIds);
//                }
//                checkSpuClash(recommendSpuDTO);
//            });
//        }

        distributionRecommendSpuMapper.updateStatus(distributionRecommendSpuUpdateDTO.getIdList(),
                distributionRecommendSpuUpdateDTO.getStatus());
    }

    @Override
    public void export(HttpServletResponse response, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        List<DistributionRecommendSpuExcelVO> recommendSpuExcelVOList = new ArrayList<>();
        int maxPageSize=PageDTO.MAX_PAGE_SIZE;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(maxPageSize);
        pageDTO.setPageNum(PageDTO.DEFAULT_PAGE_NUM);
        PageVO<SpuCommonVO> esPageVO = productSearch(distributionRecommendSpuQueryDTO, pageDTO);
        if (!Objects.isNull(esPageVO)) {
            List<SpuCommonVO> spuCommonVOList = esPageVO.getList();
            List<Long> spuIdList = spuCommonVOList.stream().map(SpuCommonVO :: getSpuId).collect(Collectors.toList());
            List<DistributionRecommendSpu> recommendSpuList=distributionRecommendSpuMapper.listBySpuIdList(spuIdList);
            //取重复(取end_time最久的时间) 查数据需要end_time排序倒叙
            List<DistributionRecommendSpu> recommendSpuListCompare = compareTreeSetSpu(recommendSpuList);
            Map<Long, DistributionRecommendSpu> recommendSpuMap = recommendSpuListCompare.stream().collect(Collectors.toMap(DistributionRecommendSpu :: getSpuId, Function.identity()));
            recommendSpuExcelVOList = buildDistributionRecommendSpuVOList(spuCommonVOList, recommendSpuMap, null).stream()
                    .map(this :: buildDistributionRecommendSpuExcelVO).collect(Collectors.toList());
            if (esPageVO.getTotal() > maxPageSize) {
                // 总共可以分多少页
                Integer pages = esPageVO.getPages();
                for (int i = 2; i <= pages; i++) {
                    PageDTO page = new PageDTO();
                    page.setPageNum(i);
                    page.setPageSize(maxPageSize);
                    log.info("分销商品导出分页数据 i->{} pageNum->{} pageSize->{}",i,page.getPageNum(),page.getPageSize());
                    PageVO<SpuCommonVO> esPageVO2 = productSearch(distributionRecommendSpuQueryDTO, page);
                    List<SpuCommonVO> spuCommonVOList2 = esPageVO2.getList();
                    List<Long> spuIdLis2 = spuCommonVOList2.stream().map(SpuCommonVO :: getSpuId).collect(Collectors.toList());
                    List<DistributionRecommendSpu> recommendSpuList2=distributionRecommendSpuMapper.listBySpuIdList(spuIdLis2);
                    //取重复(取end_time最久的时间) 查数据需要end_time排序倒叙
                    List<DistributionRecommendSpu> recommendSpuListCompare2 = compareTreeSetSpu(recommendSpuList2);
                    Map<Long, DistributionRecommendSpu> recommendSpuMap2 = recommendSpuListCompare2.stream().collect(Collectors.toMap(DistributionRecommendSpu :: getSpuId, Function.identity()));
                    recommendSpuExcelVOList.addAll(buildDistributionRecommendSpuVOList(spuCommonVOList2, recommendSpuMap2, null).stream()
                            .map(this :: buildDistributionRecommendSpuExcelVO).collect(Collectors.toList()));
                }
            }
        }
        ExcelUtil.soleExcel(response, recommendSpuExcelVOList, DistributionRecommendSpuExcelVO.EXCEL_NAME, DistributionRecommendSpuExcelVO.MERGE_ROW_INDEX,
                DistributionRecommendSpuExcelVO.MERGE_COLUMN_INDEX, DistributionRecommendSpuExcelVO.class);
    }

    //去除重复
    private List<DistributionRecommendSpu> compareTreeSetSpu(List<DistributionRecommendSpu> recommendSpuList){
        TreeSet<DistributionRecommendSpu> treeSet=new TreeSet<DistributionRecommendSpu>(new Comparator<DistributionRecommendSpu>() {
            @Override
            public int compare(DistributionRecommendSpu o1, DistributionRecommendSpu o2) {
                String key1=o1.getSpuId().toString();
                String key2=o2.getSpuId().toString();
                int re=  key1.compareTo(key2);
                return re;
            }
        });
        treeSet.addAll(recommendSpuList);
        List<DistributionRecommendSpu> recommendSpuListCompare = new ArrayList<>(treeSet);
        return recommendSpuListCompare;
    }

//    @Override
//    public void export(HttpServletResponse response, DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
//        List<DistributionRecommendSpuExcelVO> recommendSpuExcelVOList = new ArrayList<>();
//        int maxPageSize=PageDTO.MAX_PAGE_SIZE;
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPageSize(maxPageSize);
//        pageDTO.setPageNum(PageDTO.DEFAULT_PAGE_NUM);
//        PageVO<SpuCommonVO> esPageVO = productSearch(distributionRecommendSpuQueryDTO, pageDTO);
//        if (!Objects.isNull(esPageVO)) {
//            List<SpuCommonVO> spuCommonVOList = esPageVO.getList();
//            List<Long> spuIdList = spuCommonVOList.stream().map(SpuCommonVO :: getSpuId).collect(Collectors.toList());
//            Map<Long, DistributionRecommendSpu> recommendSpuMap = distributionRecommendSpuMapper.listBySpuIdList(spuIdList)
//                    .stream().collect(Collectors.toMap(DistributionRecommendSpu :: getSpuId, Function.identity()));
//            recommendSpuExcelVOList = buildDistributionRecommendSpuVOList(spuCommonVOList, recommendSpuMap, null).stream()
//                    .map(this :: buildDistributionRecommendSpuExcelVO).collect(Collectors.toList());
//            if (esPageVO.getTotal() > maxPageSize) {
//                // 总共可以分多少页
//                Integer pages = esPageVO.getPages();
//                for (int i = 2; i <= pages; i++) {
//                    PageDTO page = new PageDTO();
//                    page.setPageNum(i);
//                    page.setPageSize(maxPageSize);
//                    log.info("分销商品导出分页数据 i->{} pageNum->{} pageSize->{}",i,page.getPageNum(),page.getPageSize());
//                    PageVO<SpuCommonVO> esPageVO2 = productSearch(distributionRecommendSpuQueryDTO, page);
//                    List<SpuCommonVO> spuCommonVOList2 = esPageVO2.getList();
//                    List<Long> spuIdLis2 = spuCommonVOList2.stream().map(SpuCommonVO :: getSpuId).collect(Collectors.toList());
//                    Map<Long, DistributionRecommendSpu> recommendSpuMap2 = distributionRecommendSpuMapper.listBySpuIdList(spuIdLis2)
//                            .stream().collect(Collectors.toMap(DistributionRecommendSpu :: getSpuId, Function.identity()));
//                    recommendSpuExcelVOList.addAll(buildDistributionRecommendSpuVOList(spuCommonVOList2, recommendSpuMap2, null).stream()
//                            .map(this :: buildDistributionRecommendSpuExcelVO).collect(Collectors.toList()));
//                }
//            }
//        }
//        ExcelUtil.soleExcel(response, recommendSpuExcelVOList, DistributionRecommendSpuExcelVO.EXCEL_NAME, DistributionRecommendSpuExcelVO.MERGE_ROW_INDEX,
//                DistributionRecommendSpuExcelVO.MERGE_COLUMN_INDEX, DistributionRecommendSpuExcelVO.class);
//    }

    @Override
    public List<Long> listSpuIdListByParam(DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO) {
        return distributionRecommendSpuMapper.listSpuIdListByParam(distributionRecommendSpuQueryDTO);
    }

    private DistributionRecommendSpuExcelVO buildDistributionRecommendSpuExcelVO(DistributionRecommendSpuVO distributionRecommendSpuVO) {
        DistributionRecommendSpuExcelVO distributionRecommendSpuExcelVO = mapperFacade.map(distributionRecommendSpuVO, DistributionRecommendSpuExcelVO.class);
        distributionRecommendSpuExcelVO.setLimitStore("全部门店");
        if (distributionRecommendSpuVO.getLimitStoreType() == 1) {
            distributionRecommendSpuExcelVO.setLimitStore(String.format("%s个门店",distributionRecommendSpuVO.getLimitStoreCount()));
        }
        distributionRecommendSpuExcelVO.setStatus(distributionRecommendSpuVO.getStatus() == 0 ? "禁用" : "启用");
        return distributionRecommendSpuExcelVO;
    }

    private List<DistributionRecommendSpuVO> buildDistributionRecommendSpuVOList(List<SpuCommonVO> spuCommonVOList,
                                                                                 Map<Long, DistributionRecommendSpu> recommendSpuMap,
                                                                                 Map<Long, DistributionCommissionRateVO> distributionCommissionRateVOMap) {
        return spuCommonVOList.stream().map(spuCommonVO -> {
            DistributionRecommendSpu distributionRecommendSpu = recommendSpuMap.get(spuCommonVO.getSpuId());
            DistributionRecommendSpuVO distributionRecommendSpuVO = mapperFacade.map(distributionRecommendSpu,
                    DistributionRecommendSpuVO.class);
            if (distributionRecommendSpu.getLimitStoreType() == 1 && StrUtil.isNotBlank(distributionRecommendSpu.getLimitStoreIds())) {
                distributionRecommendSpuVO.setLimitStoreCount(Stream.of(distributionRecommendSpu.getLimitStoreIds()
                        .split(",")).collect(Collectors.toList()).size());
                distributionRecommendSpuVO.setLimitStoreIdList(Arrays.asList(distributionRecommendSpu.getLimitStoreIds().split(",")));
            }
            log.info("分页获取分销推广-推荐商品列表 spuCode【{}】 priceFee【{}】 marketPriceFee【{}】",spuCommonVO.getSpuCode(),spuCommonVO.getPriceFee(),spuCommonVO.getMarketPriceFee());
            distributionRecommendSpuVO.setSpuId(spuCommonVO.getSpuId());
            distributionRecommendSpuVO.setSpuCode(spuCommonVO.getSpuCode());
            distributionRecommendSpuVO.setSpuName(spuCommonVO.getSpuName());
            distributionRecommendSpuVO.setMainImgUrl(spuCommonVO.getMainImgUrl());
            distributionRecommendSpuVO.setPriceFee(spuCommonVO.getPriceFee());
            distributionRecommendSpuVO.setMarketPriceFee(spuCommonVO.getMarketPriceFee());
            distributionRecommendSpuVO.setSpuCode(spuCommonVO.getSpuCode());
            if (distributionCommissionRateVOMap != null && distributionCommissionRateVOMap.containsKey(spuCommonVO.getSpuId())) {
                DistributionCommissionRateVO distributionCommissionRateVO = distributionCommissionRateVOMap.get(spuCommonVO.getSpuId());
                distributionRecommendSpuVO.setShareRate(distributionCommissionRateVO.getShareRate());
                distributionRecommendSpuVO.setGuideRate(distributionCommissionRateVO.getGuideRate());
            }
            return distributionRecommendSpuVO;
        }).collect(Collectors.toList());
    }

    private PageVO<SpuCommonVO> productSearch(DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO, PageDTO pageDTO) {
        List<Long> spuIdList = distributionRecommendSpuMapper.listSpuIdListByParam(distributionRecommendSpuQueryDTO);
        if (!CollectionUtils.isEmpty(spuIdList)) {
            ProductSearchDTO productSearch = mapperFacade.map(distributionRecommendSpuQueryDTO, ProductSearchDTO.class);
            productSearch.setSpuIds(spuIdList);
            productSearch.setPageNum(pageDTO.getPageNum());
            productSearch.setPageSize(pageDTO.getPageSize());
            log.info("分销商品查询参数:{}", JSONObject.toJSONString(productSearch));
            ServerResponseEntity<PageVO<SpuCommonVO>> responseEntity = productFeignClient.commonSearch(productSearch);
            if (responseEntity.isSuccess()) {
                log.info("分销商品查询数据：{}",JSONObject.toJSONString(responseEntity.getData()));
                return responseEntity.getData();
            }
        }
        return null;
    }
}
