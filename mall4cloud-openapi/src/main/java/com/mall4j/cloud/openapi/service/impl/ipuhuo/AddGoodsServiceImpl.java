package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.*;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuAttrValueDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：添加商品业务操作层
 */
@Service("addGoodsService")
public class AddGoodsServiceImpl implements IPuHuoProductHandleService, InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);
    private static final ReqMethodType reqMethodType = ReqMethodType.AddGoods;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    OnsMQTemplate productSyncTemplate;

    @Override
    public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
        long start = System.currentTimeMillis();
        AddGoodsReqDto addGoodsReqDto = JSONUtil.toBean(commonReqDto.getBizcontent(), new TypeReference<AddGoodsReqDto>() {
        }, true);
        IPuHuoRespDto<BaseResultDto> respDto = IPuHuoRespDto.success(new AddGoodsRespDto());
        try {
            // TODO 具体业务逻辑
            syncProduct(addGoodsReqDto);
        } finally {
            logger.info("处理爱铺货添加商品推送信息结束，请求参数为：{}，处理结果为：{}，共耗时：{}", addGoodsReqDto, respDto, System.currentTimeMillis() - start);
        }

        return respDto;
    }

    private void syncProduct(AddGoodsReqDto addGoodsReqDto) {
        //封装商品同步参数
        SpuDTO spuDTO = buildSpuDTO(addGoodsReqDto);
        //调用商品接口进行同步
//        productFeignClient.iphSync(spuDTO);

        productSyncTemplate.syncSend(spuDTO, RocketMqConstant.IPH_PRODUCT_TAG);
    }

    private SpuDTO buildSpuDTO(AddGoodsReqDto addGoodsReqDto) {
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setShopCategoryId(Long.valueOf(addGoodsReqDto.getCid()));
        spuDTO.setName(addGoodsReqDto.getTitle());
        spuDTO.setSpuCode(addGoodsReqDto.getOutercode());
        spuDTO.setStatus(getStatus(addGoodsReqDto.getStatus()));
        spuDTO.setPriceFee(addGoodsReqDto.getPrice().longValue() * 100);
        spuDTO.setSellingPoint(addGoodsReqDto.getSellpoint());
        spuDTO.setDetail(addGoodsReqDto.getDesc());
        AddGoodsReqDto.SkuPropImg[] propimgs = addGoodsReqDto.getPropimgs();
        List<AddGoodsReqDto.SkuPropImg> skuPropImgs = null;
        if (propimgs.length > 0) {
            skuPropImgs = Arrays.asList(propimgs);
        }
        //商品图片
        buildImg(spuDTO, addGoodsReqDto.getItemimgs());
        //商品基础属性
        buildSpuAttrValue(spuDTO, addGoodsReqDto.getProps());
        //sku属性
        buildSku(spuDTO, addGoodsReqDto.getSkus(), skuPropImgs);
        return spuDTO;

    }

    /**
     * 封装规格属性
     *
     * @param spuDTO
     * @param skus
     * @param skuPropImgs
     */
    private void buildSku(SpuDTO spuDTO, AddGoodsReqDto.Sku[] skus, List<AddGoodsReqDto.SkuPropImg> skuPropImgs) {
        if (skus.length > 0) {
            List<AddGoodsReqDto.Sku> skuList = Arrays.asList(skus);
            List<SkuDTO> skuDTOList = skuList.stream().map(sku -> {
                SkuDTO skuDTO = new SkuDTO();
                skuDTO.setSkuCode(sku.getOutercode());
                skuDTO.setPriceFee(sku.getPrice().longValue() * 100);
                skuDTO.setMarketPriceFee(sku.getOriginalprice().longValue() * 100);
                //构建属性参数
                buildSkuAttr(sku, skuDTO, skuPropImgs,spuDTO);
                return skuDTO;
            }).collect(Collectors.toList());
            spuDTO.setSkuList(skuDTOList);
        }
    }

    private void buildSkuAttr(AddGoodsReqDto.Sku sku, SkuDTO skuDTO, List<AddGoodsReqDto.SkuPropImg> skuPropImgs,SpuDTO spuDTO) {
        HashMap<String, String> attrImgMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(skuPropImgs)) {
            skuPropImgs.stream().forEach(skuPropImg -> {
                attrImgMap.put(skuPropImg.getPname() + skuPropImg.getPvalue(), skuPropImg.getUrl());
            });
        }
        AddGoodsReqDto.Prop[] props = sku.getProps();
        if (props.length > 0) {
            List<AddGoodsReqDto.Prop> propList = Arrays.asList(props);
            List<SpuSkuAttrValueDTO> spuSkuAttrValueDTOList = propList.stream().map(prop -> {
                SpuSkuAttrValueDTO spuSkuAttrValueDTO = new SpuSkuAttrValueDTO();
                spuSkuAttrValueDTO.setAttrName(prop.getPname());
                spuSkuAttrValueDTO.setAttrValueName(prop.getPvalue());
                String skuImg = attrImgMap.get(prop.getPname() + prop.getPvalue());
                if (StrUtil.isNotBlank(skuImg)) {
                    spuSkuAttrValueDTO.setImgUrl(skuImg);
                    skuDTO.setImgUrl(skuImg);
                    spuDTO.setHasSkuImg(1);
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
    private void buildSpuAttrValue(SpuDTO spuDTO, AddGoodsReqDto.Prop[] props) {
        if (props.length > 0) {
            List<AddGoodsReqDto.Prop> propList = Arrays.asList(props);
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
    private void buildImg(SpuDTO spuDTO, AddGoodsReqDto.ItemImgs[] itemimgs) {
        if (itemimgs.length > 0) {
            List<AddGoodsReqDto.ItemImgs> itemImgs = Arrays.asList(itemimgs);
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


    @Override
    public void afterPropertiesSet() throws Exception {
        IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
    }
}
