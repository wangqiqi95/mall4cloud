package com.mall4j.cloud.product.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.SkuProtectVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.dto.ZhlsCommodityReqDto;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.*;
import com.mall4j.cloud.product.vo.AppAttrVo;
import com.mall4j.cloud.product.vo.ZhlsCommodityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Slf4j
@RestController("appSpuController")
@RequestMapping("/ma/spu")
@Api(tags = "app-spu信息")
@RefreshScope
public class SpuController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SkuService skuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuBrowseLogService spuBrowseLogService;
    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;
    @Autowired
    private TagActivityService tagActivityService;
    @Autowired
    private SpuSkuPricingPriceService spuSkuPricingPriceService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private ZhlsCommodityService zhlsCommodityService;

    @Autowired
    private ProductConfigProperties productConfigProperties;

    @GetMapping("/prod_info")
    @ApiOperation(value = "商品详情信息", notes = "根据商品ID（prodId）获取商品信息")
    @ApiImplicitParam(name = "spuId", value = "商品ID", required = true, dataType = "Long")
    public ServerResponseEntity<SpuAppVO> prodInfo(@RequestParam("spuId") Long spuId, @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) {
        SpuAppVO spuAppVO = spuService.prodInfo(spuId, storeId);
        if (Objects.equals(spuAppVO.getSpuType(), SpuType.SCORE.value())) {
            throw new LuckException(ResponseEnum.SPU_NOT_EXIST);
        }
        List<SkuAppVO> skuAppVO = skuService.getSpuSkuInfo(spuAppVO.getSpuId(), storeId);
        if (CollectionUtil.isEmpty(skuAppVO)) {
            throw new LuckException(ResponseEnum.SPU_DOWN_STATUS);
        }
        //增加价取价逻辑(小程序配置的活动价)
        List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(skuAppVO,SpuSkuPriceDTO.class);
        List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
        if(activityVOList.size()>0){
            Map<Long, Long> timeDiscountSKuPrice = activityVOList.stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getPrice()));
            skuAppVO.forEach(skuAppVO1 -> {
                Long skuPrice = timeDiscountSKuPrice.get(skuAppVO1.getSkuId());
                if (skuPrice != null) {
                    skuAppVO1.setPriceFee(skuPrice);
                }
            });
            //增加商品会员日标识
            List<SkuTimeDiscountActivityVO> memberPriceSku=activityVOList.stream().filter(activityVO -> activityVO.isMemberPriceFlag()).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(memberPriceSku)){
                spuAppVO.setMemberPriceFlag(true);
                spuAppVO.setFriendlyCouponUseFlag(memberPriceSku.get(0).getFriendlyCouponUseFlag());
                spuAppVO.setFriendlyDiscountFlag(memberPriceSku.get(0).getFriendlyDiscountFlag());
            }
            //增加虚拟门店价标识
            List<SkuTimeDiscountActivityVO> invateStorePriceSku=activityVOList.stream().filter(activityVO -> activityVO.isInvateStorePriceFlag()).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(invateStorePriceSku)){
                spuAppVO.setInvateStorePriceFlag(true);
                spuAppVO.setFriendlyCouponUseFlag(invateStorePriceSku.get(0).getFriendlyCouponUseFlag());
                spuAppVO.setFriendlyDiscountFlag(invateStorePriceSku.get(0).getFriendlyDiscountFlag());
            }
        }

        spuAppVO.setSkus(skuAppVO);

        //设置活动价
        SkuAppVO skuAppVO1 = skuAppVO.stream().min(Comparator.comparing(SkuAppVO::getPriceFee)).get();
        spuAppVO.setPriceFee(skuAppVO1.getPriceFee());
        // spu活动列表
        spuAppVO.setSpuActivity(spuService.spuActivityBySpuId(spuAppVO.getShopId(), spuId));
        //设置营销标签
        spuAppVO.setTagActivity(tagActivityService.getTagBySpuId(spuAppVO.getSpuId(), storeId));
        return ServerResponseEntity.success(spuAppVO);
    }

    @GetMapping("/ua/prod_info_test")
    @ApiOperation(value = "商品详情信息test", notes = "商品详情信息test")
    @ApiImplicitParam(name = "spuId", value = "商品ID", required = true, dataType = "Long")
    public ServerResponseEntity<SpuAppVO> prodInfotest(@RequestParam("spuId") Long spuId, @RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId) {
        SpuAppVO spuAppVO = spuService.prodInfo(spuId, storeId);
        if (Objects.equals(spuAppVO.getSpuType(), SpuType.SCORE.value())) {
            throw new LuckException(ResponseEnum.SPU_NOT_EXIST);
        }
        List<SkuAppVO> skuAppVO = skuService.getSpuSkuInfo(spuAppVO.getSpuId(), storeId);

        //增加价取价逻辑(小程序配置的活动价)
        List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(skuAppVO,SpuSkuPriceDTO.class);
        List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
        if(activityVOList.size()>0){
            Map<Long, Long> timeDiscountSKuPrice = activityVOList.stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getPrice()));
            skuAppVO.forEach(skuAppVO1 -> {
                Long skuPrice = timeDiscountSKuPrice.get(skuAppVO1.getSkuId());
                if (skuPrice != null) {
                    skuAppVO1.setPriceFee(skuPrice);
                }
            });
            //增加商品会员日标识
            List<SkuTimeDiscountActivityVO> memberPriceSku=activityVOList.stream().filter(activityVO -> activityVO.isMemberPriceFlag()).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(memberPriceSku)){
                spuAppVO.setMemberPriceFlag(true);
                spuAppVO.setFriendlyCouponUseFlag(memberPriceSku.get(0).getFriendlyCouponUseFlag());
                spuAppVO.setFriendlyDiscountFlag(memberPriceSku.get(0).getFriendlyDiscountFlag());
            }
            //增加虚拟门店价标识
            List<SkuTimeDiscountActivityVO> invateStorePriceSku=activityVOList.stream().filter(activityVO -> activityVO.isInvateStorePriceFlag()).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(invateStorePriceSku)){
                spuAppVO.setInvateStorePriceFlag(true);
                spuAppVO.setFriendlyCouponUseFlag(invateStorePriceSku.get(0).getFriendlyCouponUseFlag());
                spuAppVO.setFriendlyDiscountFlag(invateStorePriceSku.get(0).getFriendlyDiscountFlag());
            }
        }

        spuAppVO.setSkus(skuAppVO);

        //设置活动价
        SkuAppVO skuAppVO1 = skuAppVO.stream().min(Comparator.comparing(SkuAppVO::getPriceFee)).get();
        spuAppVO.setPriceFee(skuAppVO1.getPriceFee());
        return ServerResponseEntity.success(spuAppVO);
    }

    @GetMapping("/prod_info/intscode")
    @ApiOperation(value = "扫一扫", notes = "根据sku69码查询商品id")
    public ServerResponseEntity<Long> prodInfoByIntscode(@RequestParam("intscode") String intscode) {
        //根据sku69码查询spuId
        Sku one = skuService.getOne(new LambdaQueryWrapper<Sku>().eq(Sku::getIntscode, intscode).eq(Sku::getStatus, 1), false);
        if (Objects.isNull(one)) {
            return ServerResponseEntity.showFailMsg("商品数据异常");
        }
        return ServerResponseEntity.success(one.getSpuId());
    }

    @PostMapping("/page")
    @ApiOperation(value = "C端分页接口查询", notes = "根据门店Id查询")
    public ServerResponseEntity<PageVO<SpuAppPageVO>> page(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId,
                                                           @RequestBody SpuPageSearchDTO searchDTO) {
        if(searchDTO.getStoreId()==null){
            searchDTO.setStoreId(storeId);
        }
        log.info("C端分页接口查询 storeId->【{}】 入参-> {}",searchDTO.getStoreId(), JSONObject.toJSON(searchDTO));
        PageVO<SpuAppPageVO> page = spuService.appPage(searchDTO);
        return ServerResponseEntity.success(page);
    }

    @GetMapping("/score_prod_info")
    @ApiOperation(value = "积分商品详情信息", notes = "根据商品ID（prodId）获取积分商品信息")
    @ApiImplicitParam(name = "spuId", value = "商品ID", required = true, dataType = "Long")
    public ServerResponseEntity<SpuAppVO> scoreProdInfo(@RequestParam("spuId") Long spuId) {
//        SpuAppVO spuAppVO = spuService.prodInfo(spuId, storeId);
//        if (!Objects.equals(spuAppVO.getSpuType(), SpuType.SCORE.value())) {
//            throw new LuckException(ResponseEnum.SPU_NOT_EXIST);
//        }
//        List<SkuAppVO> skuAppVO = skuService.getSpuDetailSkuInfo(spuAppVO.getSpuId(), storeId);
//        spuAppVO.setSkus(skuAppVO);
//        return ServerResponseEntity.success(spuAppVO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/sku_list")
    @ApiOperation(value = "sku信息", notes = "根据商品ID（prodId）单独获取sku信息")
    @ApiImplicitParam(name = "spuId", value = "商品ID", required = true, dataType = "Long")
    public ServerResponseEntity<List<SkuAppVO>> skuList(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId,
                                                          @RequestParam("spuId") Long spuId) {

//        return ServerResponseEntity.success(skuService.getSpuDetailSkuInfo(spuId, null));

        List<SkuAppVO> skuAppVO = skuService.getSpuSkuInfo(spuId, storeId);
        //增加价取价逻辑(小程序配置的活动价)
        List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(skuAppVO,SpuSkuPriceDTO.class);
        List<SkuTimeDiscountActivityVO> activityVOList=spuSkuPricingPriceService.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
        if(activityVOList.size()>0){
            Map<Long, Long> timeDiscountSKuPrice = activityVOList.stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getPrice()));
            skuAppVO.forEach(skuAppVO1 -> {
                Long skuPrice = timeDiscountSKuPrice.get(skuAppVO1.getSkuId());
                if (skuPrice != null) {
                    skuAppVO1.setPriceFee(skuPrice);
                }
            });
        }
        return ServerResponseEntity.success(skuAppVO);
    }

    @GetMapping("/recommend_list")
    @ApiOperation(value = "推荐商品列表", notes = "根据商品ID（prodId）获取商品信息")
    public ServerResponseEntity<PageVO<SpuCommonVO>> recommendList(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId, PageDTO pageDTO, SpuDTO spuDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        ProductSearchDTO productSearch = new ProductSearchDTO();
        Long primaryCategoryId = null;
        if (Objects.isNull(spuDTO.getSpuType())) {
            spuDTO.setSpuType(SpuType.NORMAL.value());
        }
        // 已登陆
        if (Objects.nonNull(userInfoInTokenBO)) {
            primaryCategoryId = spuBrowseLogService.recommendCategoryId(spuDTO.getSpuType());
        }
        // 已登陆但还没有数据，或未登陆
        boolean serCategoryId = (!Objects.equals(spuDTO.getSpuType(), SpuType.SCORE.value()) && Objects.isNull(primaryCategoryId)) && Objects.nonNull(spuDTO.getCategoryId());
        if (serCategoryId) {
            CategoryVO category = categoryService.getById(spuDTO.getCategoryId());
            if (Objects.nonNull(category)) {
                primaryCategoryId = category.getPrimaryCategoryId();
            }
        }
        //如果有商品id则过滤掉
        if (Objects.nonNull(spuDTO.getSpuId())) {
            List<Long> spuIds = new ArrayList<>();
            spuIds.add(spuDTO.getSpuId());
            productSearch.setSpuIdsExclude(spuIds);
        }
        if(Objects.isNull(productSearch.getStoreId())){
            productSearch.setStoreId(storeId);
        }
        productSearch.setPrimaryCategoryId(primaryCategoryId);
        productSearch.setPageNum(pageDTO.getPageNum());
        productSearch.setPageSize(pageDTO.getPageSize());
        productSearch.setSpuType(spuDTO.getSpuType());
        //只查询上架状态的数据。
        productSearch.setStatus(1);
        productSearch.setSort(2);
        PageVO<SpuCommonVO> page = spuService.commonPage(productSearch);

        return ServerResponseEntity.success(page);
    }

    @PostMapping("/ua/validSpus")
    @ApiOperation(value = "C端有效spus查询", notes = "根据门店Id查询")
    public ServerResponseEntity<List<Long>> validSpus(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId,
                                                           @RequestBody SpuPageSearchDTO searchDTO) {
        if(searchDTO.getStoreId()==null){
            searchDTO.setStoreId(storeId);
        }
        log.info("C端有效spus查询 storeId->【{}】 spu入参总数【{}】",searchDTO.getStoreId(),searchDTO.getSpuIds().size());
        List<Long> spus = spuService.validSpus(searchDTO);
        log.info("C端有效spus查询 storeId->【{}】 spu查询结果总数【{}】",searchDTO.getStoreId(),spus.size());
        return ServerResponseEntity.success(spus);
    }

    @PostMapping("/ua/listAppAttrValues")
    @ApiOperation(value = "C端属性筛选列表", notes = "C端属性筛选列表")
    public ServerResponseEntity<List<AppAttrVo>> listAppAttrValues(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId) {
        return ServerResponseEntity.success(attrService.listAppAttrValues(null));
    }

    @PostMapping("/zhls_recommend_list")
    @ApiOperation(value = "有数推荐商品列表", notes = "有数推荐商品列表")
    public ServerResponseEntity<ZhlsCommodityVO> zhls_recommend_list(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId,
                                                                     @RequestBody @Valid ZhlsCommodityReqDto reqDto) {
        if(productConfigProperties.getSyncZhlsData()){
            return ServerResponseEntity.success(zhlsCommodityService.zhlsRecommendList(storeId,reqDto));
        }else{
            PageDTO pageDTO = new PageDTO();
            pageDTO.setPageNum(reqDto.getPageNum());
            pageDTO.setPageSize(reqDto.getPageSize());
            ServerResponseEntity<PageVO<SpuCommonVO>> pageVOServerResponseEntity = recommendList(storeId, pageDTO, new SpuDTO());
            ZhlsCommodityVO zhlsCommodityVO = new ZhlsCommodityVO();
            PageVO<SpuCommonVO> pageVO = pageVOServerResponseEntity.getData();
            List<ZhlsCommodityVO.ZhlsCommodity> list = mapperFacade.mapAsList(pageVO.getList(), ZhlsCommodityVO.ZhlsCommodity.class);
            zhlsCommodityVO.setList(list);
            zhlsCommodityVO.setTotal(pageVO.getTotal());
            zhlsCommodityVO.setPages(pageVO.getPages());
            return ServerResponseEntity.success(zhlsCommodityVO);
        }
    }


}
