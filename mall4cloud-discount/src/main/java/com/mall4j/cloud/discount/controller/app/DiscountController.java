package com.mall4j.cloud.discount.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.discount.constant.DiscountRule;
import com.mall4j.cloud.api.dto.EsPageDTO;
import com.mall4j.cloud.api.feign.SearchSpuFeignClient;
import com.mall4j.cloud.api.product.dto.SpuListDTO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.discount.constant.SuitableProdTypeEnum;
import com.mall4j.cloud.discount.dto.AppDiscountListDTO;
import com.mall4j.cloud.discount.service.DiscountService;
import com.mall4j.cloud.discount.vo.DiscountItemVO;
import com.mall4j.cloud.discount.vo.DiscountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 满减满折优惠
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
@Slf4j
@RestController("appDiscountController")
@RequestMapping("/ua/discount")
@Api(tags = "满减满折优惠")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    SpuFeignClient spuFeignClient;
    @Autowired
    MapperFacade mapperFacade;

    @GetMapping("/list")
    @ApiOperation(value = "获取促销活动列表", notes = "获取促销活动列表")
    public ServerResponseEntity<PageVO<DiscountVO>> getDiscountList(PageDTO page, AppDiscountListDTO discount) {
        Assert.isNull(discount.getShopId(),"商铺id不允许为空。");
        PageVO<DiscountVO> spuPage = discountService.getAppDiscountList(page,discount);
        return ServerResponseEntity.success(spuPage);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取满减活动信息", notes = "获取满减活动信息")
    @ApiImplicitParam(name = "discountId", value = "活动ID", required = true, dataType = "Long")
    public ServerResponseEntity<DiscountVO> info(@RequestParam("discountId") Long discountId,@RequestParam("shopId") Long shopId) {
        Assert.isNull(shopId,"商铺id不允许为空。");
        DiscountVO discount = discountService.discountInfo(discountId,shopId);
        if (Objects.isNull(discount)){
            // 该活动不存在或者已结束
            return ServerResponseEntity.showFailMsg("该活动不存在或者已结束");
        }
        discount.setSpuIds(null);
        return ServerResponseEntity.success(discount);
    }

//    @GetMapping("/spu_page_by_discount_id")
//    @ApiOperation(value = "获取促销活动商品分页", notes = "通过活动id获取促销活动详情")
//    @ApiImplicitParam(name = "discountId", value = "活动ID", required = true, dataType = "Long")
//    public ServerResponseEntity<EsPageVO<ProductSearchVO>> getSpuPageByDiscountId111(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO,
//                                                                    @RequestParam("discountId") Long discountId) {
//        Assert.isNull(productSearchDTO.getShopId(),"商铺id不允许为空。");
//        DiscountVO discount = discountService.getDiscountInfoById(discountId,productSearchDTO);
//        if(!Objects.equals(discount.getStatus(),1)){
//            return ServerResponseEntity.success(null);
//        }
//        if (Objects.equals(discount.getSuitableSpuType(), SuitableProdTypeEnum.ALL_SPU.value())) {
//            productSearchDTO.setShopId(discount.getShopId());
//        } else {
//            productSearchDTO.setSpuIds(discount.getSpuIds());
//        }
//        productSearchDTO.setPageNum(pageDTO.getPageNum());
//        productSearchDTO.setPageSize(pageDTO.getPageSize());
//        ServerResponseEntity<EsPageVO<ProductSearchVO>> response = searchSpuFeignClient.search(productSearchDTO);
//        if (Objects.nonNull(response) && !Objects.equals(response.getCode(), ResponseEnum.OK.value())) {
//            throw new LuckException(response.getMsg());
//        }
//        List<SpuSearchVO> spuList = response.getData().getList().get(0).getSpus();
//        if (CollectionUtil.isNotEmpty(spuList)) {
//
//            // 最优惠的那个满减项
//            DiscountItemVO discountItem = discount.getDiscountItemList().get(0);
//
//            // 折扣百分比
//            double discountProportion;
//
//            // 打折
//            if (Objects.equals(discount.getDiscountRule(), DiscountRule.P2D.value())) {
//                discountProportion = Arith.sub(1,Arith.div(discountItem.getDiscount(), 1000));
//            } else {
//                discountProportion = Arith.div(discountItem.getDiscount(), discountItem.getNeedAmount());
//            }
//            for (SpuSearchVO spuSearchVO : spuList) {
//                // 将要减去的价格
//                Double subPriceDouble = Arith.div(Arith.mul(spuSearchVO.getPriceFee(), discountProportion),1.0,0);
//                Long subPrice = subPriceDouble.longValue();
//                // 如果有活动价格上限
//                if (discount.getMaxReduceAmount() != null && subPrice > discount.getMaxReduceAmount()) {
//                    subPrice = discount.getMaxReduceAmount();
//                }
//                //价格最低只能为0.01
//                long activityPrice = spuSearchVO.getPriceFee() - subPrice;
//                if (activityPrice < 1L){
//                    activityPrice = 1L;
//                }
//                spuSearchVO.setActivityPrice(activityPrice);
//            }
//        }
//        return ServerResponseEntity.success(response.getData());
//    }

    @GetMapping("/spu_page_by_discount_id")
    @ApiOperation(value = "获取促销活动商品分页", notes = "通过活动id获取促销活动详情")
    @ApiImplicitParam(name = "discountId", value = "活动ID", required = true, dataType = "Long")
    public ServerResponseEntity<PageVO<SpuSearchVO>> getSpuPageByDiscountId(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO,
                                                                                  @RequestParam("discountId") Long discountId) {
        Assert.isNull(productSearchDTO.getShopId(),"商铺id不允许为空。");
        DiscountVO discount = discountService.getDiscountInfoById(discountId,productSearchDTO);
        if(!Objects.equals(discount.getStatus(),1)){
            return ServerResponseEntity.success(null);
        }
//        if (Objects.equals(discount.getSuitableSpuType(), SuitableProdTypeEnum.ALL_SPU.value())) {
//            productSearchDTO.setShopId(discount.getShopId());
//        } else {
//            productSearchDTO.setSpuIds(discount.getSpuIds());
//        }
//        productSearchDTO.setPageNum(pageDTO.getPageNum());
//        productSearchDTO.setPageSize(pageDTO.getPageSize());
//        ServerResponseEntity<EsPageVO<ProductSearchVO>> response = searchSpuFeignClient.search(productSearchDTO);
//        if (Objects.nonNull(response) && !Objects.equals(response.getCode(), ResponseEnum.OK.value())) {
//            throw new LuckException(response.getMsg());
//        }

        PageVO<SpuSearchVO> pagevo = new PageVO<>();
        List<SpuSearchVO> spuList = null;
        List<SpuVO> spus = null;

        SpuListDTO pageParam = new SpuListDTO();
        pageParam.setPageNum(pageDTO.getPageNum());
        pageParam.setPageSize(pageDTO.getPageSize());
        pageParam.setSort(productSearchDTO.getSort());
        pageParam.setStatus(1);

        if (Objects.equals(discount.getSuitableSpuType(), SuitableProdTypeEnum.ASSIGN_SPU.value())) {
            //如果指定店铺，分页查询全部商品数据
            pageParam.setSpuIds(discount.getSpuIds());
        }
        ServerResponseEntity<PageVO<SpuVO>> responseEntity = spuFeignClient.pageList(pageParam);
        if(responseEntity==null || responseEntity.getData()==null){
            log.error("查询商品服务，获取列表失败。请求参数：{}", JSONObject.toJSONString(pageParam));
            return ServerResponseEntity.success(null);
        }
        spus = responseEntity.getData().getList();
        spuList = mapperFacade.mapAsList(spus,SpuSearchVO.class);
        Map<Long, SpuVO> spuMap = spus.stream().collect(Collectors.toMap(SpuVO::getSpuId,o -> o));
        if (CollectionUtil.isNotEmpty(spuList)) {
            // 最优惠的那个满减项
            DiscountItemVO discountItem = discount.getDiscountItemList().get(0);
            // 折扣百分比
            double discountProportion;
            // 打折
            if (Objects.equals(discount.getDiscountRule(), DiscountRule.P2D.value())) {
                discountProportion = Arith.sub(1,Arith.div(discountItem.getDiscount(), 1000));
            } else {
                discountProportion = Arith.div(discountItem.getDiscount(), discountItem.getNeedAmount());
            }
            for (SpuSearchVO spuSearchVO : spuList) {
                // 将要减去的价格
                Double subPriceDouble = Arith.div(Arith.mul(spuSearchVO.getPriceFee(), discountProportion),1.0,0);
                Long subPrice = subPriceDouble.longValue();
                // 如果有活动价格上限
                if (discount.getMaxReduceAmount() != null && subPrice > discount.getMaxReduceAmount()) {
                    subPrice = discount.getMaxReduceAmount();
                }
                //价格最低只能为0.01
                long activityPrice = spuSearchVO.getPriceFee() - subPrice;
                if (activityPrice < 1L){
                    activityPrice = 1L;
                }
                spuSearchVO.setActivityPrice(activityPrice);
                spuSearchVO.setSpuName(spuMap.get(spuSearchVO.getSpuId()).getName());
                spuSearchVO.setSellingPoint(spuMap.get(spuSearchVO.getSpuId()).getSellingPoint());
            }
        }

        //商品组件导入排序，按照前端传入spuIds集合顺序排序
        if(CollectionUtil.isNotEmpty(discount.getSpuIds()) && CollectionUtil.isNotEmpty(spuList)){
            Map<Long,Integer> spuIdsMap=new LinkedHashMap<>();
            int seq=0;
            for(Long spuId:discount.getSpuIds()){
                seq++;
                spuIdsMap.put(spuId,seq);
            }
            spuList.forEach(spuPageVO -> {
                if(spuIdsMap.containsKey(spuPageVO.getSpuId())){
                    spuPageVO.setSeq(spuIdsMap.get(spuPageVO.getSpuId()));
                }
            });
            Collections.sort(spuList, Comparator.comparingInt(SpuSearchVO::getSeq));
        }

        pagevo.setTotal(responseEntity.getData().getTotal());
        pagevo.setPages(responseEntity.getData().getPages());
        pagevo.setList(spuList);
        return ServerResponseEntity.success(pagevo);
    }
}
