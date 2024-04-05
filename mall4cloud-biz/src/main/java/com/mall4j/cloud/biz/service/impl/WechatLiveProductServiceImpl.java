package com.mall4j.cloud.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.request.*;
import com.mall4j.cloud.api.biz.dto.livestore.request.DescInfo;
import com.mall4j.cloud.api.biz.dto.livestore.response.Skus;
import com.mall4j.cloud.api.biz.dto.livestore.response.*;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.biz.wx.wx.api.live.SpuApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.LiveProductDTO;
import com.mall4j.cloud.biz.mapper.WechatNotifyTaskMapper;
import com.mall4j.cloud.biz.model.WechatCatogoryQualificationDO;
import com.mall4j.cloud.biz.model.WechatNotifyTaskDO;
import com.mall4j.cloud.biz.service.WechatLiveCategoryService;
import com.mall4j.cloud.biz.service.WechatLiveMediaService;
import com.mall4j.cloud.biz.service.WechatLiveProductService;
import com.mall4j.cloud.biz.vo.LiveProductVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class WechatLiveProductServiceImpl implements WechatLiveProductService {

    @Autowired
    private SpuApi spuApi;

    @Autowired
    private WechatNotifyTaskMapper notifyTaskMapper;

    @Autowired
    private WechatLiveMediaService mediaService;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private SkuFeignClient skuFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private WechatLiveCategoryService wechatLiveCategoryService;

    @Value("${spu.path:/pages/detail/detail?spuId=%s&storeId=%s}")
    private String spuPath;

    /**
     * 分页查询商品列表
     *
     * @param page
     * @param onsale 5-上架 11-自主下架 13-违规下架/风控系统下架
     * @return
     */
    @Override
    public PageVO<LiveProductVO> list(PageDTO page, Integer onsale) {
        SpuListRequest spuListRequest = new SpuListRequest();
        spuListRequest.setPage(page.getPageNum());
        spuListRequest.setPageSize(page.getPageSize());
        spuListRequest.setNeedEditSpu(1);
        if (onsale != null && (onsale == 5 || onsale == 11 || onsale == 13)) {
            spuListRequest.setStatus(onsale);
        }
        SpuListResponse spuListResponse = spuApi.spuList(wxConfig.getWxMaToken(), spuListRequest);
        log.info("查询微信视频号商品列表接口成功，接口返回:{}", JSONObject.toJSONString(spuListResponse));

        PageVO<LiveProductVO> result = new PageVO<>();

        if (spuListResponse != null) {
            if (spuListResponse.getErrcode() != 0) {
                log.error("调用微信接口获取商品列表异常 wxOpen={}, req={}, res={}", JSON.toJSONString(spuListRequest),
                        JSON.toJSONString(spuListRequest), JSON.toJSONString(spuListResponse));
                throw new LuckException("获取商品列表异常, info:" + spuListResponse.getErrmsg());
            }
            result.setPages(PageUtil.getPages(spuListResponse.getTotalNum(), page.getPageSize()));
            result.setTotal(Long.valueOf(spuListResponse.getTotalNum()));

            List<Spu> spus = spuListResponse.getSpus();
            if (spus != null && !spus.isEmpty()) {
                List<LiveProductVO> list = new ArrayList<>();
                for (Spu spu : spus) {
                    LiveProductVO vo = new LiveProductVO();
                    vo.setProductId(Long.valueOf(spu.getOutProductId()));
                    vo.setImage(spu.getHeadImg().get(0));
                    vo.setName(spu.getTitle());
                    vo.setCount(spu.getSkus().stream().mapToInt(Skus::getStockNum).sum());
                    vo.setRejectReason(spu.getAuditInfo().getRejectReason());
                    vo.setStatus(spu.getEditStatus());
                    vo.setOnsale(spu.getStatus());
                    vo.setShopCode(spu.getPath().replaceAll(".+=(\\w+)", "$1"));

                    // 使用feign接口替换
                    StoreVO storeVO = storeFeignClient.findByStoreId(Long.valueOf(vo.getShopCode()));
                    if (storeVO == null) {
                        log.error("查询不到店铺, shopCode=" + vo.getShopCode());
                    } else {
                        vo.setShopName(storeVO.getName());
                    }

                    list.add(vo);
                }
                result.setList(list);
            } else {
                result.setList(new ArrayList<>());
            }
        }


        return result;
    }

    /**
     * 添加商品
     *
     * @param liveProductDTO
     */
    @Override
    public void add(LiveProductDTO liveProductDTO) {

        for (String productId : liveProductDTO.getValidCode()) {
            SpuRequest spuRequest = new SpuRequest();
            spuRequest.setBrandId(Long.valueOf(liveProductDTO.getBrandId()));
            spuRequest.setThirdCatId(Long.valueOf(liveProductDTO.getCategoryId()));
            // 校验是否需要资质
            ThirdCatList categoryVO = wechatLiveCategoryService.baseOne(liveProductDTO.getCategoryId());
            if (categoryVO.getQualificationType() == 1) {
                WechatCatogoryQualificationDO one = wechatLiveCategoryService.one(liveProductDTO.getCategoryId());
                if (one == null || one.getStatus() == null || one.getStatus() != 1) {
                    throw new LuckException("未取得该类目资质,请先申请!");
                }
            }

            spuRequest.setOutProductId(productId);

            spuRequest.setSceneGroupList(liveProductDTO.getSceneGroupList());

            // 查询商品信息
            ServerResponseEntity<SpuVO> responseEntity = spuFeignClient.getDetailById(Long.valueOf(productId));
            if (responseEntity == null || responseEntity.isFail()) {
                throw new LuckException("查询不到商品, spuId=" + productId);
            }
            SpuVO spuVo = responseEntity.getData();

            spuRequest.setTitle(spuVo.getName());
            spuRequest.setPath(String.format(spuPath, productId, liveProductDTO.getShopCode()));
            spuRequest.setHeadImg(Collections.singletonList(mediaService.uploadImage(spuVo.getMainImgUrl())));
            DescInfo descInfo = new DescInfo();
//            descInfo.setDesc(spuVo.getDetail());
            descInfo.setImgs(Collections.singletonList(mediaService.uploadImage(spuVo.getMainImgUrl())));
            spuRequest.setDescInfo(descInfo);



            ServerResponseEntity<List<SkuAppVO>> listServerResponseEntity = skuFeignClient.listBySpuId(Long.valueOf(productId));
            if (listServerResponseEntity == null || listServerResponseEntity.isFail()) {
                throw new LuckException("查询不到商品SKU, spuId=" + productId);
            }

            List<com.mall4j.cloud.api.biz.dto.livestore.request.Skus> skus = new ArrayList<>();
            for (SkuAppVO spuVoSku : listServerResponseEntity.getData()) {
                com.mall4j.cloud.api.biz.dto.livestore.request.Skus sku = new com.mall4j.cloud.api.biz.dto.livestore.request.Skus();
                sku.setMarketPrice(spuVoSku.getMarketPriceFee());
                sku.setOutProductId(productId);
                sku.setOutSkuId(spuVoSku.getSkuId().toString());
                sku.setSalePrice(spuVoSku.getPriceFee());
                List<SkuAttr> skuAttrs = new ArrayList<>();

                    SkuAttr skuAttr = new SkuAttr();
                    skuAttr.setAttrKey("通用尺码");
                    skuAttr.setAttrValue("大");
                    skuAttrs.add(skuAttr);

                // for (SpuSkuAttrValueVO spuSkuAttrValue : spuVoSku.getSpuSkuAttrValues()) {
                //     SkuAttr skuAttr = new SkuAttr();
                //     skuAttr.setAttrKey(spuSkuAttrValue.getAttrId().toString());
                //     skuAttr.setAttrValue(spuSkuAttrValue.getAttrName());
                //     skuAttrs.add(skuAttr);
                // }

                sku.setSkuAttrs(skuAttrs);
                sku.setStockNum((long) (spuVoSku.getStock() == null ? 0 : spuVoSku.getStock()));
                sku.setThumbImg(mediaService.uploadImage(spuVoSku.getImgUrl()));
                skus.add(sku);
            }
            spuRequest.setSkus(skus);

            SpuAuditResponse response = spuApi.addSpu(wxConfig.getWxMaToken(), spuRequest);
            log.info("调用视频号添加商品结束，执行参数:{},执行结果：{}",JSONObject.toJSONString(spuRequest),JSONObject.toJSONString(response));
            if(StringUtils.isNotEmpty(response.getErrmsg())){
                Assert.faild(response.getErrmsg());
            }
        }

    }

    /**
     * 更新商品
     *
     * @param liveProductDTO
     */
    @Override
    public void update(LiveProductDTO liveProductDTO) {

        for (String productId : liveProductDTO.getValidCode()) {
            boolean updateFlag = false;
            SpuRequest spuRequest = new SpuRequest();
            spuRequest.setBrandId(Long.valueOf(liveProductDTO.getBrandId()));
            spuRequest.setThirdCatId(Long.valueOf(liveProductDTO.getCategoryId()));
            spuRequest.setOutProductId(productId);

            // 查询商品信息
            ServerResponseEntity<SpuVO> responseEntity = spuFeignClient.getDetailById(Long.valueOf(productId));
            if (responseEntity == null || responseEntity.isFail()) {
                throw new LuckException("查询不到商品, spuId=" + productId);
            }

            // 查询微信的商品信息
            SpuQueryRequest spuQueryRequest = new SpuQueryRequest();
            spuQueryRequest.setOutProductId(productId);
            spuQueryRequest.setNeedEditSpu(0);

            SpuResponse spu = spuApi.spu(wxConfig.getWxMaToken(), spuQueryRequest);
            if (spu.getErrcode() != 0) {
                log.error("查询微信商品信息失败, req={}, res={}", JSON.toJSONString(spuQueryRequest), JSON.toJSONString(spu));
                throw new LuckException("查询微信商品信息失败, 原因:" + spu.getErrmsg());
            }
            SpuRequest wxSpu = spu.getSpu();

            SpuVO spuVo = responseEntity.getData();

            spuRequest.setTitle(spuVo.getName());
            updateFlag = !spuRequest.getTitle().equals(wxSpu.getTitle());
            spuRequest.setPath(String.format(spuPath, productId, liveProductDTO.getShopCode()));
            spuRequest.setHeadImg(Collections.singletonList(mediaService.uploadImage(spuVo.getMainImgUrl())));
            // updateFlag = updateFlag || !spuRequest.getHeadImg().equals(wxSpu.getHeadImg());
            DescInfo descInfo = new DescInfo();
            descInfo.setDesc(spuVo.getDetail());
            updateFlag = updateFlag || ObjectUtils.notEqual(descInfo.getDesc(), wxSpu.getDescInfo().getDesc());
            descInfo.setImgs(Collections.singletonList(mediaService.uploadImage(spuVo.getMainImgUrl())));
            // updateFlag = updateFlag || !descInfo.getImgs().equals(wxSpu.getDescInfo().getImgs());
            spuRequest.setDescInfo(descInfo);

            ServerResponseEntity<List<SkuAppVO>> listServerResponseEntity = skuFeignClient.listBySpuId(Long.valueOf(productId));
            if (listServerResponseEntity == null || listServerResponseEntity.isFail()) {
                throw new LuckException("查询不到商品SKU, spuId=" + productId);
            }

            List<com.mall4j.cloud.api.biz.dto.livestore.request.Skus> skus = new ArrayList<>();
            for (SkuAppVO spuVoSku : listServerResponseEntity.getData()) {
                com.mall4j.cloud.api.biz.dto.livestore.request.Skus sku = new com.mall4j.cloud.api.biz.dto.livestore.request.Skus();
                com.mall4j.cloud.api.biz.dto.livestore.request.Skus wxSku = findWxSku(wxSpu, spuVoSku.getSkuId().toString());
                updateFlag = updateFlag || wxSku == null;

                sku.setMarketPrice(spuVoSku.getMarketPriceFee());
                updateFlag = updateFlag || ObjectUtils.notEqual(sku.getMarketPrice(), wxSku.getMarketPrice());
                sku.setOutProductId(productId);
                sku.setOutSkuId(spuVoSku.getSkuId().toString());
                sku.setSalePrice(spuVoSku.getPriceFee());
                updateFlag = updateFlag || ObjectUtils.notEqual(sku.getSalePrice(), wxSku.getSalePrice());
                List<SkuAttr> skuAttrs = new ArrayList<>();

                SkuAttr skuAttr = new SkuAttr();
                skuAttr.setAttrKey("通用尺码");
                skuAttr.setAttrValue("大");
                skuAttrs.add(skuAttr);


                sku.setSkuAttrs(skuAttrs);
                sku.setStockNum(Long.valueOf(spuVoSku.getStock()));
                updateFlag = updateFlag || ObjectUtils.notEqual(sku.getStockNum(), wxSku.getStockNum());
                sku.setThumbImg(mediaService.uploadImage(spuVoSku.getImgUrl()));
                updateFlag = updateFlag || ObjectUtils.notEqual(sku.getThumbImg(), wxSku.getThumbImg());
                skus.add(sku);
            }
            spuRequest.setSkus(skus);

            if (updateFlag) {
                SpuAuditResponse spuAuditResponse =spuApi.updateSpu(wxConfig.getWxMaToken(), spuRequest);
                log.info("调用视频号更新商品结束，执行参数:{},执行结果：{}",JSONObject.toJSONString(spuRequest),JSONObject.toJSONString(spuAuditResponse));
            }
        }

    }

    private com.mall4j.cloud.api.biz.dto.livestore.request.Skus findWxSku(SpuRequest wxSpu, String skuId) {
        for(com.mall4j.cloud.api.biz.dto.livestore.request.Skus skus : wxSpu.getSkus()) {
            if (skus.getOutSkuId().equals(skuId)) {
                return skus;
            }
        }
        return null;
    }

    /**
     * 上架商品
     *
     * @param liveProductDTO
     */
    @Override
    public void onsale(LiveProductDTO liveProductDTO) {

        for (String productId : liveProductDTO.getValidCode()) {
            SpuRequest spuRequest = new SpuRequest();
            spuRequest.setOutProductId(productId);
            SpuAuditResponse spuAuditResponse = spuApi.onsale(wxConfig.getWxMaToken(), spuRequest);
            log.info("调用视频号上架商品结束，执行参数:{},执行结果：{}",JSONObject.toJSONString(spuRequest),JSONObject.toJSONString(spuAuditResponse));
        }
    }

    /**
     * 下架商品
     *
     * @param liveProductDTO
     */
    @Override
    public void offsale(LiveProductDTO liveProductDTO) {

        for (String productId : liveProductDTO.getValidCode()) {
            SpuRequest spuRequest = new SpuRequest();
            spuRequest.setOutProductId(productId);
            SpuAuditResponse spuAuditResponse = spuApi.offsale(wxConfig.getWxMaToken(), spuRequest);
            log.info("调用视频号下架商品结束，执行参数:{},执行结果：{}",JSONObject.toJSONString(spuRequest),JSONObject.toJSONString(spuAuditResponse));
        }
    }

    /**
     * 删除商品
     *
     * @param liveProductDTO
     */
    @Override
    public void delete(LiveProductDTO liveProductDTO) {

        for (String productId : liveProductDTO.getValidCode()) {
            SpuRequest spuRequest = new SpuRequest();
            spuRequest.setOutProductId(productId);
            SpuAuditResponse spuAuditResponse = spuApi.delSpu(wxConfig.getWxMaToken(), spuRequest);
            log.info("调用视频号删除商品结束，执行参数:{},执行结果：{}",JSONObject.toJSONString(spuRequest),JSONObject.toJSONString(spuAuditResponse));
        }
    }

    /**
     * 添加商品变更
     *
     * @param productId .
     */
    @Override
    public void recordChange(String productId) {
        WechatNotifyTaskDO entity = new WechatNotifyTaskDO();
        entity.setType("0");
        entity.setItemId(productId);
        entity.setItemStatus("0");

        notifyTaskMapper.insert(entity);
    }

    /**
     * 查询单个商品
     *
     * @param productId .
     */
    @Override
    public LiveProductDTO one(String productId) {
        if (StringUtils.isBlank(productId)) {
            throw new LuckException("商品参数为空");
        }

        SpuQueryRequest spuRequest = new SpuQueryRequest();
        spuRequest.setOutProductId(productId);
        spuRequest.setNeedEditSpu(1);
        SpuResponse spuResponse = spuApi.spu(wxConfig.getWxMaToken(), spuRequest);
        log.info("调用视频号查询商品详情结束，执行参数:{},执行结果：{}",JSONObject.toJSONString(spuRequest),JSONObject.toJSONString(spuResponse));

        if (spuResponse == null || spuResponse.getErrcode() != 0) {
            log.error("调用微信接口获取商品异常 req={}, res={}", productId, spuResponse);
            throw new LuckException("获取商品异常, info:" + (spuResponse==null?"null":spuResponse.getErrmsg()));
        }
        SpuRequest spu = spuResponse.getSpu();

        LiveProductDTO dto = new LiveProductDTO();
        dto.setCategoryId(String.valueOf(spu.getThirdCatId()));

        // 查询分类名称
        ThirdCatList thirdCat = wechatLiveCategoryService.baseOne(dto.getCategoryId());
        dto.setCategoryName(thirdCat.getFirstCatName() + "/" + thirdCat.getSecondCatName() + "/" + thirdCat.getThirdCatName());

        dto.setBrandId(String.valueOf(spu.getBrandId()));
        dto.setValidCode(Collections.singletonList(spu.getOutProductId()));
        dto.setShopCode(spu.getPath().replaceAll(".+=(\\w+)", "$1"));
        // 使用feign接口替换
        StoreVO storeVO = storeFeignClient.findByStoreId(Long.valueOf(dto.getShopCode()));
        if (storeVO == null) {
            log.error("查询不到店铺, shopCode=" + dto.getShopCode());
            dto.setShopName( dto.getShopCode());
        } else {
            dto.setShopName(storeVO.getName());
        }

        return dto;
    }
}
