package com.mall4j.cloud.biz.service.channels;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.*;
import com.mall4j.cloud.api.biz.dto.channels.request.*;
import com.mall4j.cloud.api.biz.dto.channels.response.*;
import com.mall4j.cloud.biz.wx.wx.channels.EcProductApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.ChannelsSkuDTO;
import com.mall4j.cloud.biz.dto.channels.request.AddChannlesSpuRequest;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuAttrValueVO;
import com.mall4j.cloud.common.product.vo.SpuSkuAttrValueVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EcProductService {

    @Autowired
    EcProductApi ecProductApi;
    @Autowired
    WxConfig wxConfig;
    @Autowired
    EcBasicsService ecBasicsService;

    /**
     * 添加或更新商品数据
     * @param addChannlesSpuRequest
     * @param spuVO
     * @return
     */
    public EcAddProductResponse addOrUpdateEcProduct(AddChannlesSpuRequest addChannlesSpuRequest, SpuVO spuVO, boolean isAdd){
        log.info("EcProductService addOrUpdate product, spuRequest参数：[{}], SpuVO参数：[{}]",
                JSONObject.toJSONString(addChannlesSpuRequest), JSONObject.toJSONString(spuVO));


        EcAddProductRequest ecAddProductRequest = new EcAddProductRequest();
        // 商品基本信息
        // TODO 商品标题有强制字符要求，测试环境加上后缀
        ecAddProductRequest.setTitle(spuVO.getName());
        ecAddProductRequest.setOut_product_id(StrUtil.toString(addChannlesSpuRequest.getSpuId()));
        //ecAddProductRequest.setSub_title();
        ecAddProductRequest.setDeliver_method(addChannlesSpuRequest.getDeliverMethod());
        ecAddProductRequest.setBrand_id(StrUtil.toString(addChannlesSpuRequest.getBrandId()));
        //ecAddProductRequest.setQualifications();
        //ecAddProductRequest.setAftersale_desc();


        // 商品类目
        List<EcCat> cats = getEcCats(addChannlesSpuRequest);
        ecAddProductRequest.setCats(cats);

        // 商品参数
        // TODO 商品参数也有强制需要，具体参照类目详情
        List<EcAttrInfo> attrInfoList = new ArrayList<>();

        List<SpuAttrValueVO> spuAttrValues = spuVO.getSpuAttrValues();
        if (spuAttrValues != null && !spuAttrValues.isEmpty()){
            for (SpuAttrValueVO spuAttrValue : spuAttrValues) {
                EcAttrInfo ecAttrInfo = new EcAttrInfo();
                ecAttrInfo.setAttr_key(spuAttrValue.getAttrName());
                ecAttrInfo.setAttr_value(spuAttrValue.getAttrValueName());
                attrInfoList.add(ecAttrInfo);
            }
            ecAddProductRequest.setAttrs(attrInfoList);
        }
       /*
        EcAttrInfo ecAttrInfo = new EcAttrInfo();
        ecAttrInfo.setAttr_key("品牌");
        ecAttrInfo.setAttr_value("scrm");
        attrInfoList.add(ecAttrInfo);

        EcAttrInfo ecAttrInfo1 = new EcAttrInfo();
        ecAttrInfo1.setAttr_key("颜色");
        ecAttrInfo1.setAttr_value("红色、白色、灰色");
        attrInfoList.add(ecAttrInfo1);

        EcAttrInfo ecAttrInfo2 = new EcAttrInfo();
        ecAttrInfo2.setAttr_key("款式");
        ecAttrInfo2.setAttr_value("新款");
        attrInfoList.add(ecAttrInfo2);

        *//*EcAttrInfo ecAttrInfo3 = new EcAttrInfo();
        ecAttrInfo3.setAttr_key("材质");
        ecAttrInfo3.setAttr_value("桑蚕丝");
        attrInfoList.add(ecAttrInfo3);*//*

        EcAttrInfo ecAttrInfo4 = new EcAttrInfo();
        ecAttrInfo4.setAttr_key("尺码");
        ecAttrInfo4.setAttr_value("X");
        attrInfoList.add(ecAttrInfo4);

        EcAttrInfo ecAttrInfo5 = new EcAttrInfo();
        ecAttrInfo5.setAttr_key("鞋面材质");
        ecAttrInfo5.setAttr_value("其他");
        attrInfoList.add(ecAttrInfo5);

        EcAttrInfo ecAttrInfo6 = new EcAttrInfo();
        ecAttrInfo6.setAttr_key("鞋底材质");
        ecAttrInfo6.setAttr_value("其他");
        attrInfoList.add(ecAttrInfo6);*/

        ecAddProductRequest.setAttrs(attrInfoList);
        // 运费模版
        EcExpressInfo expressInfo = new EcExpressInfo();
        expressInfo.setTemplate_id(addChannlesSpuRequest.getFreightTemplate());
        ecAddProductRequest.setExpress_info(expressInfo);

        // 限购
        EcLimitedInfo ecLimitedInfo = new EcLimitedInfo();

        //sku信息
        List<EcSkus> skusList = getEcSkusList(addChannlesSpuRequest, spuVO);
        ecAddProductRequest.setSkus(skusList);

        // 是否更新完成立即上架
        if (addChannlesSpuRequest.getIsQuestListing()){
            ecAddProductRequest.setListing(1);
        }

        // 商品主图上传
        String[] strings = spuVO.getImgUrls().split(",");
        List<String> headImgs = new ArrayList<>();
        int headImgMax = 9;
        int headImgCount = 0;
        for (String url : strings) {
            if (headImgCount >= headImgMax){
                break;
            }
            String imgUrl = ecBasicsService.convertImgUrlToChannelsImgUrl(url);
            // 为空不处理
            if (StrUtil.isNotBlank(imgUrl)) {
                headImgs.add(imgUrl);
                headImgCount++;
            }
        }
        ecAddProductRequest.setHead_imgs(headImgs);

        // 商品图片详情信息上传
        EcDescInfo descInfo = getEcDescInfo(spuVO);
        ecAddProductRequest.setDesc_info(descInfo);

        EcAddProductResponse response = null;
        if (isAdd) {
            log.info("添加视频号商品");
            response = ecProductApi.add(wxConfig.getWxEcToken(), ecAddProductRequest);
        } else {
            log.info("更新视频号商品");
            ecAddProductRequest.setProduct_id(StrUtil.toString(addChannlesSpuRequest.getOutSpuId()));
            response = ecProductApi.update(wxConfig.getWxEcToken(), ecAddProductRequest);
        }
        log.info("调用视频号商品API，参数:[{}],执行结果:[{}]", JSONObject.toJSONString(ecAddProductRequest),
                JSONObject.toJSONString(response));

        if (response == null || response.getErrcode() != 0) {

            if (response != null && StrUtil.isNotEmpty(response.getErrmsg())) {
                Assert.faild(response.getErrmsg());
            } else {
                Assert.faild("请求微信视频号API失败");
            }
        }

        return response;
    }

    private List<EcSkus> getEcSkusList(AddChannlesSpuRequest addChannlesSpuRequest, SpuVO spuVO) {
        List<EcSkus> skusList = new ArrayList<>();
        Map<Long,SkuVO> skuVOMap = spuVO.getSkus().stream().collect(Collectors.toMap(SkuVO::getSkuId, Function.identity()));
        for (ChannelsSkuDTO skus : addChannlesSpuRequest.getSkus()) {
            EcSkus ecSkus = new EcSkus();
            if (skus.getOutSkuId() != null) {
                ecSkus.setSku_id(StrUtil.toString(skus.getOutSkuId()));
            }

            ecSkus.setOut_sku_id(StrUtil.toString(skus.getSkuId()));
            ecSkus.setStock_num(skus.getStockNum());
            ecSkus.setSale_price(skus.getPrice());

            SkuVO skuVO = skuVOMap.get(skus.getSkuId());
            // 价格检查
            //checkPrice(skus, skuVO);

            String imgUrl = ecBasicsService.convertImgUrlToChannelsImgUrl(skuVO.getImgUrl());
            if (StrUtil.isBlank(imgUrl)) {
                log.info("SKU获取原图异常,请校验,错误图片地址：[{}]", skuVO.getImgUrl());
                Assert.faild("SKU获取原图异常,请校验");
            }
            ecSkus.setThumb_img(imgUrl);

            List<EcAttrInfo> ecSkuAttrInfos = new ArrayList<>();
            for (SpuSkuAttrValueVO spuSkuAttrValue : skuVO.getSpuSkuAttrValues()) {
                EcAttrInfo ecAttrInfo = new EcAttrInfo();
                ecAttrInfo.setAttr_key(spuSkuAttrValue.getAttrName());
                ecAttrInfo.setAttr_value(spuSkuAttrValue.getAttrValueName());
                ecSkuAttrInfos.add(ecAttrInfo);
            }

            ecSkus.setSku_attrs(ecSkuAttrInfos);
            skusList.add(ecSkus);
        }
        return skusList;
    }

    /**
     * 商品销售价格检查
     * @param skus RequestSku
     * @param skuVO skuVo
     */
    private void checkPrice(ChannelsSkuDTO skus, SkuVO skuVO) {
        Long skuProtectPrice = skuVO.getSkuProtectPrice();
        Long marketPriceFee = skuVO.getMarketPriceFee();
        Long price = skus.getPrice();
        String skuCode = skuVO.getSkuCode();

        if (price < skuProtectPrice){
            Assert.faild("商品条码[" + skuCode + "]，不能低于保护价[" + skuProtectPrice + "]");
        }
        if (price > marketPriceFee){
            Assert.faild("商品条码[" + skuCode + "]，不能大于吊牌价[" +  marketPriceFee +"]");
        }
        if (price < (marketPriceFee * 0.3)){
            Assert.faild("商品条码[" + skuCode + "]，不能低于吊牌价3折");
        }
    }

    private static List<EcCat> getEcCats(AddChannlesSpuRequest addChannlesSpuRequest) {
        List<EcCat> cats = new ArrayList<>();
        for (String s : addChannlesSpuRequest.getCats().split(",")) {
            EcCat cat = new EcCat();
            cat.setCat_id(s);
            cats.add(cat);
        }
        return cats;
    }

    private EcDescInfo getEcDescInfo(SpuVO spuVO) {
        EcDescInfo descInfo = new EcDescInfo();
        List<String> descImgs = new ArrayList<>();
        if(StrUtil.isNotEmpty(spuVO.getDetail())){
            descImgs  = getImgSrc(spuVO.getDetail());
        }
        List<String> wxdescImgs = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(descImgs)){
            int headImgMax = 20;
            int headImgCount = 0;
            for (String url : descImgs) {
                if (headImgCount >= headImgMax){
                    break;
                }
                if (StrUtil.isBlank(url)){
                    break;
                }
                String wxdescImg = ecBasicsService.convertImgUrlToChannelsImgUrl(url);
                // 为空不处理
                if (StrUtil.isNotBlank(wxdescImg)) {
                    wxdescImgs.add(wxdescImg);
                    headImgCount++;
                }
            }
        }
        descInfo.setImgs(wxdescImgs);
        descInfo.setDesc("详情图");
        return descInfo;
    }

    /**
     * 将html标签中img标签的src解析出来
     * @param htmlStr
     * @return
     */
    private List<String> getImgSrc(String htmlStr) {
        if( htmlStr == null ){
            return null;
        }
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            //img = img + "," + m_image.group();
            // Matcher m =
            // Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(m_image.group());

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    /**
     * 修改商品数据
     * @param addChannlesSpuRequest
     * @param spuVO
     * @return
     */
    public EcAddProductResponse updateEcProduct(AddChannlesSpuRequest addChannlesSpuRequest, SpuVO spuVO){
        log.info("EcProductService编辑商品,updateEcProduct:参数：{},SpuVO:参数{}", JSONObject.toJSONString(addChannlesSpuRequest),JSONObject.toJSONString(spuVO));

        String imgUrl = ecBasicsService.convertImgUrlToChannelsImgUrl(spuVO.getMainImgUrl());
        if (StrUtil.isBlank(imgUrl)) {
            log.info("商品主图原图获取失败,请校验,错误图片地址：[{}]", spuVO.getMainImgUrl());
            Assert.faild("商品主图原图获取失败,请校验");
        }

        EcAddProductRequest ecAddProductRequest = new EcAddProductRequest();
        ecAddProductRequest.setProduct_id(StrUtil.toString(addChannlesSpuRequest.getOutSpuId()));
        ecAddProductRequest.setTitle(spuVO.getName());
        ecAddProductRequest.setHead_imgs(CollectionUtil.toList(imgUrl));

        List<EcCat> cats = getEcCats(addChannlesSpuRequest);
        ecAddProductRequest.setCats(cats);
        ecAddProductRequest.setBrand_id(StrUtil.toString(addChannlesSpuRequest.getBrandId()));

        //运费模版
        EcExpressInfo expressInfo = new EcExpressInfo();
        expressInfo.setTemplate_id(addChannlesSpuRequest.getFreightTemplate());
        ecAddProductRequest.setExpress_info(expressInfo);

        //商品详情图片
        getEcDescInfo(spuVO);

        //sku信息
        Map<Long,SkuVO> skuVOMap = spuVO.getSkus().stream().collect(Collectors.toMap(SkuVO::getSkuId, Function.identity()));
        List<EcSkus> ecSkuses = new ArrayList<>();
        for (ChannelsSkuDTO skus : addChannlesSpuRequest.getSkus()) {
            EcSkus ecSkus = new EcSkus();
            // ecSkus 设置 外部的skuid
            ecSkus.setOut_sku_id(StrUtil.toString(skus.getSkuId()));
            ecSkus.setStock_num(skus.getStockNum());
            ecSkus.setSale_price(skus.getPrice());

            SkuVO skuVO = skuVOMap.get(skus.getSkuId());
            List<EcAttrInfo> ecSkuAttrInfos = new ArrayList<>();
            for (SpuSkuAttrValueVO spuSkuAttrValue : skuVO.getSpuSkuAttrValues()) {
                EcAttrInfo ecAttrInfo = new EcAttrInfo();
                ecAttrInfo.setAttr_key(spuSkuAttrValue.getAttrName());
                ecAttrInfo.setAttr_value(spuSkuAttrValue.getAttrValueName());
                ecSkuAttrInfos.add(ecAttrInfo);
            }
            ecSkus.setSku_attrs(ecSkuAttrInfos);
        }
        ecAddProductRequest.setSkus(ecSkuses);

        log.info("编辑视频号商品，参数:{}",JSONObject.toJSONString(ecAddProductRequest));
        EcAddProductResponse response = ecProductApi.update(wxConfig.getWxEcToken(),ecAddProductRequest);
        log.info("编辑视频号商品，参数:{},执行结果:{}",JSONObject.toJSONString(ecAddProductRequest),JSONObject.toJSONString(response));
        return response;
    }

    /**
     * 撤回商品审核
     * @param productId
     */
    public EcBaseResponse auditCancel(String productId){
        EcProductAuditCancelRequest ecProductAuditCancelRequest = new EcProductAuditCancelRequest();
        ecProductAuditCancelRequest.setProduct_id(productId);
        log.info("视频号撤回商品审核，参数:{}",productId);
        EcBaseResponse ecBaseResponse = ecProductApi.auditCancel(wxConfig.getWxEcToken(),ecProductAuditCancelRequest);
        log.info("视频号撤回商品审核，参数:{}，执行结果:{}",productId,JSONObject.toJSONString(ecBaseResponse));
        return ecBaseResponse;
    }

    /**
     * 上架商品
     * @param productId
     */
    public void listing(String productId){
        EcProductListingRequest ecProductListingRequest = new EcProductListingRequest();
        ecProductListingRequest.setProduct_id(productId);
        log.info("视频号商品上架，参数:{}",productId);
        EcBaseResponse response =ecProductApi.listing(wxConfig.getWxEcToken(),ecProductListingRequest);
        log.info("视频号商品上架，参数:{}，执行结果:{}",productId,JSONObject.toJSONString(response));
        if (response == null || response.getErrcode() != 0) {
            if (response != null && StrUtil.isNotEmpty(response.getErrmsg())) {
                Assert.faild(response.getErrmsg());
            } else {
                Assert.faild("请求微信视频号API失败");
            }
        }
    }

    /**
     * 下架商品
     * @param productId
     */
    public void delisting(String productId){
        EcProductDelistingRequest ecProductDelistingRequest = new EcProductDelistingRequest();
        ecProductDelistingRequest.setProduct_id(productId);
        log.info("视频号商品下架，参数:{}",productId);
        EcBaseResponse response = ecProductApi.delisting(wxConfig.getWxEcToken(),ecProductDelistingRequest);
        log.info("视频号商品下架，参数:{}，执行结果:{}",productId,JSONObject.toJSONString(response));
        if (response == null || response.getErrcode() != 0) {
            if (response != null && StrUtil.isNotEmpty(response.getErrmsg())) {
                Assert.faild(response.getErrmsg());
            } else {
                Assert.faild("请求微信视频号API失败");
            }
        }
    }

    /**
     * 查看实时库存
     * @param productId
     * @param skuId
     */
    public EcGetStockResponse getStock(Long productId, Long skuId){
        EcGetStockRequest ecGetStockRequest = new EcGetStockRequest();
        ecGetStockRequest.setProduct_id(StrUtil.toString(productId));
        ecGetStockRequest.setSku_id(StrUtil.toString(skuId));
        EcGetStockResponse ecGetStockResponse = ecProductApi.getStock(wxConfig.getWxEcToken(),ecGetStockRequest);
        log.info("获取实时商品库存，productId: [{}], skuId: [{}], response: [{}]", productId, skuId, ecGetStockResponse);
        return ecGetStockResponse;
    }

    public EcProductListResponse List(){
        EcProductListRequest request = new EcProductListRequest();
        request.setPage_size(10);
        return ecProductApi.list(wxConfig.getWxEcToken(), request);
    }

    public EcProductResponse get(Long id, Integer type){
        EcGetProductRequest request = new EcGetProductRequest();
        request.setProduct_id(StrUtil.toString(id));
        request.setData_type(type);
        EcProductResponse response = ecProductApi.get(wxConfig.getWxEcToken(), request);
        Object json = JSONObject.toJSON(response);
        log.info("获取视频号商品数据,outSpuId:[{}], response:[{}]", id, json);
        if (response == null || response.getErrcode() != 0) {
            if (response != null && StrUtil.isNotEmpty(response.getErrmsg())) {
                Assert.faild(response.getErrmsg());
            } else {
                Assert.faild("请求微信视频号API失败");
            }
        }

        return response;
    }

    public EcUpdateStockResponse updateStock(Long outSpuId, Long outSkuId, Integer stock, Integer type) {
        EcUpdateStockRequest request = new EcUpdateStockRequest();
        request.setProduct_id(StrUtil.toString(outSpuId));
        request.setSku_id(StrUtil.toString(outSkuId));
        request.setNum(stock);
        request.setDiff_type(type);
        /*if (stock > 0) {
            request.setDiff_type(1);
        } else if (stock < 0) {
            request.setDiff_type(2);
            request.setNum(Math.abs(stock));
        } else {
            request.setDiff_type(3);
        }*/
        EcUpdateStockResponse response = ecProductApi.updateStock(wxConfig.getWxEcToken(), request);
        Object jsonObject = JSONObject.toJSON(response);
        log.info("调用视频号4.0更新商品库存，requestParams: [{}], response: [{}]", request, jsonObject);
        if (response == null || response.getErrcode() != 0) {
            if (response != null && StrUtil.isNotEmpty(response.getErrmsg())) {
                Assert.faild(response.getErrmsg());
            } else {
                Assert.faild("请求微信视频号API失败");
            }
        }
        return response;
    }

    public void delete(String productId){
        EcDeleteProductRequest request = new EcDeleteProductRequest();
        request.setProduct_id(productId);
        EcBaseResponse response = ecProductApi.delete(wxConfig.getWxEcToken(), request);
        log.info("调用视频号4.0 删除商品，requestParams: [{}], response: [{}]", response, response);
        if (response == null || response.getErrcode() != 0) {
            if (response != null && StrUtil.isNotEmpty(response.getErrmsg())) {
                Assert.faild(response.getErrmsg());
            } else {
                Assert.faild("请求微信视频号API失败");
            }
        }

    }
}
