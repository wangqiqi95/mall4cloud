package com.mall4j.cloud.product.controller.platform;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.multishop.bo.EsShopDetailBO;
import com.mall4j.cloud.api.multishop.constant.ShopStatus;
import com.mall4j.cloud.api.multishop.feign.ShopDetailFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.*;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuAttrValueDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.product.dto.SkuPriceDTO;
import com.mall4j.cloud.product.dto.SpuAppPageVO;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuChannelPriceService;
import com.mall4j.cloud.product.service.SpuPriceService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.task.ProductPirceErrorTask;
import com.mall4j.cloud.product.task.SkuPriceCodeStatusTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lth
 * @Date 2021/7/19 16:42
 */
@Slf4j
@RestController("platformSpuController")
@RequestMapping("/p/spu")
@Api(tags = "platform-spu信息")
public class SpuController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuPriceService spuPriceService;

    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuChannelPriceService spuChannelPriceService;

    @Autowired
    private ShopDetailFeignClient shopDetailFeignClient;
    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;

    @Autowired
    OnsMQTemplate productSyncTemplate;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private ProductPirceErrorTask productPirceErrorTask;
    @Autowired
    private SkuPriceCodeStatusTask skuPriceCodeStatusTask;

    @PostMapping("/page")
    @ApiOperation(value = "C端分页接口查询", notes = "根据门店Id查询")
//    public ServerResponseEntity<PageVO<SpuAppPageVO>> page(@RequestParam(value = "storeId", required = false) Long storeId, SpuPageSearchDTO searchDTO) {
    public ServerResponseEntity<PageVO<SpuAppPageVO>> page(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId,
                                                           @RequestBody SpuPageSearchDTO searchDTO) {
        if(searchDTO.getStoreId()==null){
            searchDTO.setStoreId(storeId);
        }
        log.info("C端分页接口查询 storeId->【{}】",searchDTO.getStoreId());
        PageVO<SpuAppPageVO> page = spuService.appPage(searchDTO);
        return ServerResponseEntity.success(page);
    }

    @PostMapping("/testSpuSkuChannel")
    @ApiOperation(value = "限时调价活动过滤R渠道商品", notes = "限时调价活动过滤R渠道商品")
    public ServerResponseEntity<List<TimeDiscountActivityVO>> testSpuSkuChannel(@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId,
                                                                              @RequestParam(value = "spuId", required = false) List<Long> spuId) {

        List<Spu> spus=spuService.list(new LambdaQueryWrapper<Spu>()
                .in(Spu::getSpuId,spuId));
        List<Long> spuIdList = spus.stream().map(Spu::getSpuId).collect(Collectors.toList());
        //查询所有sku满足限时调价的集合
        List<SkuPriceDTO> appSkuPriceBySkuIdList = skuService.getAppSkuPriceBySkuIdList(spuIdList, storeId);

        //增加限时调价逻辑
        TimeDiscountActivityDTO timeDiscountActivityDTO = new TimeDiscountActivityDTO();
        timeDiscountActivityDTO.setShopId(storeId);
        timeDiscountActivityDTO.setSkuIds(appSkuPriceBySkuIdList.stream().map(SkuPriceDTO::getSkuId).collect(Collectors.toList()));
        ServerResponseEntity<List<TimeDiscountActivityVO>> listServerResponseEntity = timeDiscountFeignClient.convertActivityPrice(timeDiscountActivityDTO);

        return listServerResponseEntity;
    }

    @PutMapping("/to_top")
    @ApiOperation(value = "通过商品id设置是否置顶", notes = "通过商品id设置是否置顶")
    public ServerResponseEntity<Void> toTopBySpuId(@RequestParam(value = "spuId") Long spuId) {
        SpuVO spuVO = spuService.getBySpuId(spuId);
        if (!Objects.equals(spuVO.getStatus(), StatusEnum.ENABLE.value()) && Objects.equals(spuVO.getIsTop(), StatusEnum.DISABLE.value())) {
            throw new LuckException("只能置顶已上架的商品");
        }
        spuService.toTopBySpuId(spuId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/score_save")
    @ApiOperation(value = "保存积分spu信息", notes = "保存积分spu信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SpuDTO spuDTO) {
        // 发布商品时，需要根据店铺状态判断是否可以发布商品
        ServerResponseEntity<EsShopDetailBO> shopRequest = shopDetailFeignClient.getShopByShopId(AuthUserContext.get().getTenantId());
        EsShopDetailBO shopDetail = shopRequest.getData();
        if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE.value())) {
            throw new LuckException("店铺处于违规下线状态，不能发布新商品");
        }
        spuDTO.setShopCategoryId(Constant.DEFAULT_ID);
        spuDTO.setSpuType(SpuType.SCORE.value());
        spuDTO.setStatus(StatusEnum.ENABLE.value());
        spuService.save(spuDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/test_stockSysnc")
    @ApiOperation(value = "模拟调试erp库存同步", notes = "模拟调试erp库存同步")
    public ServerResponseEntity<Void> test_stockSysnc(@Valid @RequestBody ErpStockDTO erpStockDTO) {
        erpStockDTO.getStockDTOList().forEach(stdStockDto ->{
            if(stdStockDto.getAvailableStockNum()<0){
                stdStockDto.setAvailableStockNum(0);
            }else{
                stdStockDto.setAvailableStockNum(stdStockDto.getAvailableStockNum());
            }
        });
        spuService.stockSync(erpStockDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/test_priceSysnc")
    @ApiOperation(value = "模拟调试erp价格同步", notes = "模拟调试erp价格同步")
    public ServerResponseEntity<Void> test_priceSysnc(@Valid @RequestBody ErpPriceDTO erpPriceDTO) {
        erpPriceDTO.getSkuPriceDTOList().forEach(item->{
            item.setPriceCode(item.getProductCode());
            item.setPrice(item.getPrice() * 100);
        });
        spuService.priceSyncNew(erpPriceDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/test_prodSysnc")
    @ApiOperation(value = "模拟调试erp商品基础数据同步", notes = "模拟调试erp商品基础数据同步")
    public ServerResponseEntity<Void> test_prodSysnc(@Valid @RequestBody List<TestStdProductDto> stdProductDtos) {
        spuService.sync(convert(stdProductDtos));

//        productSyncTemplate.syncSend(convert(stdProductDtos),RocketMqConstant.ERP_PRODUCT_TAG);

        return ServerResponseEntity.success();
    }

    @PostMapping("/skuPriceCodeStatusTask")
    @ApiOperation(value = "开始执行批量启用sku_store条码状态", notes = "开始执行批量启用sku_store条码状态")
    public ServerResponseEntity<Void> skuPriceCodeStatusTask() {
        skuPriceCodeStatusTask.SkuPriceCodeStatusTask();
        return ServerResponseEntity.success();
    }

    @PostMapping("/productPirceError")
    @ApiOperation(value = "商品价格异常警告", notes = "商品价格异常警告")
    public ServerResponseEntity<Void> productPirceError() {
        productPirceErrorTask.productPirceError();
        return ServerResponseEntity.success();
    }

    @PostMapping("/channelPriceTask")
    @ApiOperation(value = "模拟调试商品设置渠道价", notes = "模拟调试商品设置渠道价")
    public ServerResponseEntity<Void> channelPriceTask(@RequestParam(value = "spuCodes",defaultValue = "") String spuCodes) {
        spuChannelPriceService.channelPriceTask(spuCodes,Optional.ofNullable(AuthUserContext.get()).orElse(new UserInfoInTokenBO()).getUsername());
        return ServerResponseEntity.success();
    }

    @PostMapping("/cancelChannelPriceTask")
    @ApiOperation(value = "模拟调试商品取消渠道价", notes = "模拟调试商品取消渠道价")
    public ServerResponseEntity<Void> cancelChannelPriceTask(@RequestParam(value = "spuCodes",defaultValue = "") String spuCodes) {
        spuChannelPriceService.cancelChannelPriceTask(spuCodes,Optional.ofNullable(AuthUserContext.get()).orElse(new UserInfoInTokenBO()).getUsername());
        return ServerResponseEntity.success();
    }


    @PostMapping("/test_iphprodSysnc")
    @ApiOperation(value = "模拟调试iph商品基础数据同步", notes = "模拟调试iph商品基础数据同步")
    public ServerResponseEntity<Void> test_iphprodSysnc(@Valid @RequestBody TestAddGoodsReqDto addGoodsReqDto) {
        //封装商品同步参数
        SpuDTO spuDTO = buildSpuDTO(addGoodsReqDto);
        //调用商品接口进行同步
        spuService.iphSync(spuDTO);

//        productSyncTemplate.syncSend(spuDTO, RocketMqConstant.IPH_PRODUCT_TAG);

        return ServerResponseEntity.success();
    }

    @PostMapping("/test_findByStoreCodes")
    @ApiOperation(value = "模拟调试通过fegin带参获取门店400", notes = "模拟调试通过fegin带参获取门店400")
    public ServerResponseEntity<Void> test_findByStoreCodes(@Valid @RequestBody List<String> storeCodeList) {
        if(CollUtil.isEmpty(storeCodeList)){
            List<String> storeCodes=new ArrayList<>();
            ServerResponseEntity<List<StoreVO>> response=storeFeignClient.listByStoreIdList(new ArrayList<>());
            if(response.isSuccess()){
                storeCodeList=new ArrayList<>();
                response.getData().forEach(item->{
                    storeCodes.add(item.getStoreCode());
                });
            }
            storeCodeList.addAll(storeCodes);
            storeCodeList.addAll(storeCodes);
        }
        log.info("入参门店集合数量："+storeCodeList.size());

        List<StoreCodeVO> byStoreCodes = storeFeignClient.findByStoreCodes(storeCodeList);
//        List<StoreCodeVO> byStoreCodes = storeFeignClient.postFindByStoreCodes(storeCodeList);

        log.info("找到门店集合数量："+byStoreCodes.size());

        return ServerResponseEntity.success();
    }

    private ErpSyncDTO convert(List<TestStdProductDto> data) {
        //赋值商品编码
        data.forEach(stdProductDto -> {
            String name = stdProductDto.getName();
//            String spuCode = name.substring(0, name.indexOf("-"));
            String spuCode=name.split("-").length>1?name.substring(0, name.indexOf("-")):name;
            stdProductDto.setSpuCode(spuCode);
        });
        //进行商品同步
        Map<String, List<TestStdProductDto>> spuMap = data.stream().collect(Collectors.groupingBy(TestStdProductDto::getSpuCode));
        //只进行商品主数据同步
        ArrayList<SpuErpSyncDTO> spuDTOList = new ArrayList<>();
        spuMap.forEach((spuCode, skuList) -> {
            SpuErpSyncDTO spuDTO = new SpuErpSyncDTO();
            spuDTO.setName(spuCode);
            spuDTO.setSpuCode(spuCode);
            spuDTO.setStatus(0);
            TestStdProductDto stdProductDto = skuList.stream().min(Comparator.comparingInt(TestStdProductDto::getPricelist)).get();
            Long minPrice = Long.valueOf(stdProductDto.getPricelist() * 100);
            spuDTO.setMarketPriceFee(minPrice);
            spuDTO.setPriceFee(minPrice);
            spuDTO.setStyleCode(stdProductDto.getStyleType());
            //商品渠道(R线下渠道 T电商渠道 L清货渠道)
            spuDTO.setChannelName(stdProductDto.getChannelName());
            //校验折扣等级数据规范
            spuDTO.setDiscount(getDiscount(spuCode,stdProductDto.getDiscount()));
            List<SkuErpSyncDTO> skuErpSyncDTOList = skuList.stream().map(stdProductDto1 -> {
                SkuErpSyncDTO skuErpSyncDTO = new SkuErpSyncDTO();
                BeanUtils.copyProperties(stdProductDto1, skuErpSyncDTO);
                skuErpSyncDTO.setColorName(stdProductDto1.getColorName());
                skuErpSyncDTO.setSizeName(stdProductDto1.getSizeName());
                //价格为0 默认999
                if(stdProductDto1.getPricelist()==0){
                    stdProductDto1.setPricelist(999);
                }
                skuErpSyncDTO.setPriceFee(stdProductDto1.getPricelist().longValue() * 100);
                skuErpSyncDTO.setStatus(Objects.equals("Y", stdProductDto1.getIsactive()) ? 1 : 0);
                skuErpSyncDTO.setColorCode(stdProductDto1.getColorCode());
                skuErpSyncDTO.setMarketPriceFee(stdProductDto1.getPricelist().longValue() * 100);
                skuErpSyncDTO.setSkuCode(stdProductDto1.getNo());
                skuErpSyncDTO.setIntsCode(stdProductDto1.getIntscode());
                skuErpSyncDTO.setPriceCode(stdProductDto1.getName());
                skuErpSyncDTO.setStyleCode(stdProductDto1.getStyleType());
                //商品渠道(R线下渠道 T电商渠道 L清货渠道)
                skuErpSyncDTO.setChannelName(stdProductDto1.getChannelName());
                //校验折扣等级数据规范
                skuErpSyncDTO.setDiscount(getDiscount(stdProductDto1.getNo(),stdProductDto1.getDiscount()));
                return skuErpSyncDTO;
            }).collect(Collectors.toList());
            spuDTO.setSkuList(skuErpSyncDTOList);
            spuDTOList.add(spuDTO);
        });
        ErpSyncDTO erpSyncDTO = new ErpSyncDTO();
        erpSyncDTO.setDtoList(spuDTOList);
        return erpSyncDTO;
    }

    private static String getDiscount(String spuCode,String discount){
        log.info("中台商品基础信息同步1 商品货号/skuCode【{}】 渠道折扣等级【{}】",spuCode,discount);
        if(StrUtil.isBlank(discount)){
            return "0";
        }
        if (!PrincipalUtil.isMaximumOfTwoDecimal(discount) && !discount.equals("0")) {
            discount="0";
            log.error("最多是保留2位小数的数值");
        }
        if(NumberUtil.parseDouble(discount)<0 || NumberUtil.parseDouble(discount)<0.99){
            discount="0";
            log.error("小于0，默认为0");
        }
        log.info("中台商品基础信息同步2 商品货号/skuCode【{}】 渠道折扣等级【{}】",spuCode,discount);
        return discount;
    }

    private SpuDTO buildSpuDTO(TestAddGoodsReqDto TestAddGoodsReqDto) {
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setShopCategoryId(Long.valueOf(TestAddGoodsReqDto.getCid()));
        spuDTO.setName(TestAddGoodsReqDto.getTitle());
        spuDTO.setSpuCode(TestAddGoodsReqDto.getOutercode());
        spuDTO.setStatus(getStatus(TestAddGoodsReqDto.getStatus()));
        spuDTO.setPriceFee(TestAddGoodsReqDto.getPrice().longValue() * 100);
        spuDTO.setSellingPoint(TestAddGoodsReqDto.getSellpoint());
        spuDTO.setDetail(TestAddGoodsReqDto.getDesc());
        TestAddGoodsReqDto.SkuPropImg[] propimgs = TestAddGoodsReqDto.getPropimgs();
        List<TestAddGoodsReqDto.SkuPropImg> skuPropImgs = null;
        if (propimgs.length > 0) {
            skuPropImgs = Arrays.asList(propimgs);
        }
        //商品图片
        buildImg(spuDTO, TestAddGoodsReqDto.getItemimgs());
        //商品基础属性
        buildSpuAttrValue(spuDTO, TestAddGoodsReqDto.getProps());
        //sku属性
        buildSku(spuDTO, TestAddGoodsReqDto.getSkus(), skuPropImgs);
        return spuDTO;

    }

    /**
     * 封装规格属性
     *
     * @param spuDTO
     * @param skus
     * @param skuPropImgs
     */
    private void buildSku(SpuDTO spuDTO, TestAddGoodsReqDto.Sku[] skus, List<TestAddGoodsReqDto.SkuPropImg> skuPropImgs) {
        if (skus.length > 0) {
            List<TestAddGoodsReqDto.Sku> skuList = Arrays.asList(skus);
            List<SkuDTO> skuDTOList = skuList.stream().map(sku -> {
                SkuDTO skuDTO = new SkuDTO();
                skuDTO.setSkuCode(sku.getOutercode());
                skuDTO.setPriceFee(sku.getPrice().longValue() * 100);
                skuDTO.setMarketPriceFee(sku.getOriginalprice().longValue() * 100);
                //构建属性参数
                buildSkuAttr(sku, skuDTO, skuPropImgs);
                return skuDTO;
            }).collect(Collectors.toList());
            spuDTO.setSkuList(skuDTOList);
        }
    }

    private void buildSkuAttr(TestAddGoodsReqDto.Sku sku, SkuDTO skuDTO, List<TestAddGoodsReqDto.SkuPropImg> skuPropImgs) {
        HashMap<String, String> attrImgMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(skuPropImgs)) {
            skuPropImgs.stream().forEach(skuPropImg -> {
                attrImgMap.put(skuPropImg.getPname() + skuPropImg.getPvalue(), skuPropImg.getUrl());
            });
        }
        TestAddGoodsReqDto.Prop[] props = sku.getProps();
        if (props.length > 0) {
            List<TestAddGoodsReqDto.Prop> propList = Arrays.asList(props);
            List<SpuSkuAttrValueDTO> spuSkuAttrValueDTOList = propList.stream().map(prop -> {
                SpuSkuAttrValueDTO spuSkuAttrValueDTO = new SpuSkuAttrValueDTO();
                spuSkuAttrValueDTO.setAttrName(prop.getPname());
                spuSkuAttrValueDTO.setAttrValueName(prop.getPvalue());
                String skuImg = attrImgMap.get(prop.getPname() + prop.getPvalue());
                if (StrUtil.isNotBlank(skuImg)) {
                    spuSkuAttrValueDTO.setImgUrl(skuImg);
                    skuDTO.setImgUrl(skuImg);
                }
                return spuSkuAttrValueDTO;
            }).collect(Collectors.toList());
            //封装属性
            skuDTO.setSpuSkuAttrValues(spuSkuAttrValueDTOList);
            //设置sku名称
            String skuName = spuSkuAttrValueDTOList.stream().map(SpuSkuAttrValueDTO::getAttrValueName).collect(Collectors.joining(","));
            skuDTO.setSkuName(skuName);
        }
    }

    /**
     * 封装商品规格属性信息
     *
     * @param spuDTO
     * @param props
     */
    private void buildSpuAttrValue(SpuDTO spuDTO, TestAddGoodsReqDto.Prop[] props) {
        if (props.length > 0) {
            List<TestAddGoodsReqDto.Prop> propList = Arrays.asList(props);
            List<SpuAttrValueDTO> attrValueDTOList = propList.stream().map(prop -> {
                SpuAttrValueDTO spuAttrValueDTO = new SpuAttrValueDTO();
                spuAttrValueDTO.setAttrName(prop.getPname());
                spuAttrValueDTO.setAttrValueName(prop.getPvalue());
                return spuAttrValueDTO;
            }).collect(Collectors.toList());
            spuDTO.setSpuAttrValues(attrValueDTOList);
        }
    }

    /**
     * 处理商品主图信息
     *
     * @param spuDTO
     * @param itemimgs
     */
    private void buildImg(SpuDTO spuDTO, TestAddGoodsReqDto.ItemImgs[] itemimgs) {
        if (itemimgs.length > 0) {
            List<TestAddGoodsReqDto.ItemImgs> itemImgs = Arrays.asList(itemimgs);
            spuDTO.setMainImgUrl(itemImgs.get(0).getUrl());
            String imgs = itemImgs.stream().map(itemImgs1 -> itemImgs1.getUrl()).collect(Collectors.joining(","));
            spuDTO.setImgUrls(imgs);
        }
    }

    private Integer getStatus(String status) {
//       上架=onsale，下架=instock，草稿=draft
        /**
         DELETE(-1)
         * 下架
         OFF_SHELF(0),
         /**
         * 上架
         PUT_SHELF(1),
         /**
         * 平台下架
         PLATFORM_OFF_SHELF(2),
         /**
         * 未审核
         WAITAUDIT(3)
         */
        Integer spuStatus = 0;
        switch (status) {
            case "onsale":
                spuStatus = 1;
                break;
            case "instock":
                spuStatus = 0;
        }
        return spuStatus;
    }

    public static void main(String[] strings){
        Long channelPrice;
        Long marketPrice=699L;
        Long activityPrice=499L;
        Long priceFee = marketPrice * 10 / 10;
        priceFee = NumberUtil.max(activityPrice, priceFee);
        System.out.println(priceFee);

        String channelDiscount="";
        System.out.println(getDiscount("124L",channelDiscount));
        //正常折扣等级
        Double discount= NumberUtil.parseDouble(channelDiscount);
        String actualDiscount=discount.toString();
        System.out.println(discount);

        if(discount<=0 || discount<0.99){//折扣等级小于0不打折，渠道价=吊牌价
            channelPrice=marketPrice;
        }else if(discount<3.0 && discount>0.0){//折扣等级小于3折，设置渠道价=商品吊牌价*默认折扣等级3折
            channelPrice = marketPrice * 3 / 10;
            actualDiscount="3";
        }else{//正常折扣
            Double discountPrice = marketPrice.doubleValue() * discount / 10;
            channelPrice=discountPrice.longValue();
        }
        System.out.println(channelPrice);
    }
}
