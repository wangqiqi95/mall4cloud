package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.api.coupon.dto.QueryCrmIdsDTO;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCouponFeignClient;
import com.mall4j.cloud.api.platform.dto.InsiderStorePageDTO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.user.constant.ConvertTypeEnum;
import com.mall4j.cloud.api.user.feign.ScoreActivityClient;
import com.mall4j.cloud.common.cache.constant.CouponCacheNames;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.SpuCodeVo;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.TCommodityScopeType;
import com.mall4j.cloud.coupon.constant.TCouponStatus;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.manager.TCouponManager;
import com.mall4j.cloud.coupon.mapper.*;
import com.mall4j.cloud.coupon.model.*;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.vo.CouponListVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class TCouponServiceImpl implements TCouponService {
    @Resource
    private TCouponMapper tCouponMapper;
    @Resource
    private TCouponCodeMapper codeMapper;
    @Resource
    private TCouponCommodityMapper commodityMapper;
    @Resource
    private TCouponShopMapper tCouponShopMapper;
    @Resource
    private TCouponCategoryMapper tCouponCategoryMapper;
    @Resource
    private ScoreActivityClient scoreActivityClient;
    @Resource
    private TCouponCommodityMapper tCouponCommodityMapper;
    @Resource
    private ProductFeignClient productFeignClient;
    @Autowired
    private TCouponUserMapper tCouponUserMapper;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Resource
    private CrmCouponFeignClient crmCouponFeignClient;

    @Autowired
    private TCouponManager tCouponManager;
    @Autowired
    TCouponSpuMapper tCouponSpuMapper;
    @Autowired
    TCouponSkuMapper tCouponSkuMapper;

    @Override
    public ServerResponseEntity<PageInfo<CouponListVO>> list(CouponListDTO param) {
        log.info("优惠券列表的搜索条件为 :{}", param);
        PageInfo<CouponListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                tCouponMapper.list(param)
        );
        if(!CollectionUtils.isEmpty(result.getList())){
            result.getList().forEach(temp ->{
                // 官方小程序：小程序后台创建的券或 crm 创建有官方小程序门店code；
                // 官网：由crm创建，且适用门店 code 有 官方商城门店 code；
                // 线下指定门店：由crm创建，包含了其他任何一个门店code，是否适用线上字段为【否】；
                // 指定门店小程序：由crm创建，包含了其他任何一个门店code，是否适用线上字段为【是】；
                List<String> scene = new ArrayList<>();
                if (temp.getSourceType() == 1){
                    scene.add("官方小程序");
                }else if (temp.getSourceType() == 2){
                    if (temp.getIsAllShop()){
                        scene.add("官网");
                        scene.add("官方小程序");
                    }else {
                        List<TCouponShop> shops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, temp.getId()));
                        List<Long> shopIds = shops.stream().map(TCouponShop::getShopId).collect(Collectors.toList());
                        if (shopIds.contains(Constant.MAIN_SHOP)){
                            scene.add("官方小程序");
                        }else if (shopIds.contains(Constant.WEBSITE_SHOP)){
                            scene.add("官网");
                        }
                    }
                    if (temp.getApplyScopeType() == 0){
                        scene.add("指定线下门店");
                        scene.add("指定门店小程序");
                    }else if (temp.getApplyScopeType() == 1){
                        scene.add("指定门店小程序");
                    }else {
                        scene.add("指定线下门店");
                    }
                }
                temp.setScene(scene);
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> save(CouponDetailDTO param) {
        TCoupon tCoupon = BeanUtil.copyProperties(param, TCoupon.class);
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO != null) {
            tCoupon.setCreateId(userInfoInTokenBO.getUserId());
            tCoupon.setUpdateId(userInfoInTokenBO.getUserId());
        }
        tCouponMapper.insert(tCoupon);

        //优惠券码关系
        if(!CollectionUtils.isEmpty(param.getCodes())){
            addCodes(param.getCodes(),tCoupon.getId());
        }

        //优惠券商品关系SPU级别
        if(!CollectionUtils.isEmpty(param.getCommodities())){
            addCommodities(param.getCommodities(),tCoupon.getId());
        }

        //优惠券门店关系
        if(!CollectionUtils.isEmpty(param.getShops())){
            addShops(param.getShops(),tCoupon.getId());
        }

        //优惠券适用商品分类
        if(!CollectionUtils.isEmpty(param.getCategorys())){
            addCategorys(tCoupon.getId(),param.getCategorys());
        }

        //优惠券商品关系SKU级别
        if(!CollectionUtils.isEmpty(param.getSpus())){
            addSpus(tCoupon.getId(),param.getSpus());
        }



        com.mall4j.cloud.api.coupon.dto.CouponDetailDTO couponDetailDTO = new com.mall4j.cloud.api.coupon.dto.CouponDetailDTO();
        couponDetailDTO.setId(tCoupon.getId()+"");
        couponDetailDTO.setKind(param.getKind());
        couponDetailDTO.setName(param.getName());
        couponDetailDTO.setNote(param.getNote());
        if (param.getReduceAmount() != null) {
            couponDetailDTO.setReduceAmount(param.getReduceAmount().divide(new BigDecimal(100)).toString());
        }
        couponDetailDTO.setStartTime(param.getStartTime());
        couponDetailDTO.setTimeType(param.getTimeType());
        couponDetailDTO.setType(param.getType());
        couponDetailDTO.setAfterReceiveDays(param.getAfterReceiveDays());
        if (param.getAmountLimitNum() != null) {
            couponDetailDTO.setAmountLimitNum(param.getAmountLimitNum().divide(new BigDecimal(100)).toString());
        }
        couponDetailDTO.setAmountLimitType(param.getAmountLimitType());
        couponDetailDTO.setApplyScopeType(param.getApplyScopeType());
        couponDetailDTO.setCodes(param.getCodes());
        couponDetailDTO.setCodeNum(param.getCodeSum());
        if (param.getCommodities() != null) {
            couponDetailDTO.setCommodities(param.getCommodities().stream().map(x -> x + "").collect(Collectors.toList()));
        }
        couponDetailDTO.setCommodityLimitNum(param.getCommodityLimitNum());
        couponDetailDTO.setCommodityLimitType(param.getCommodityLimitType());
        couponDetailDTO.setCommodityNum(param.getCommodityNum());
        couponDetailDTO.setCommodityScopeType(param.getCommodityScopeType());
        if (param.getCouponDiscount() != null) {
            couponDetailDTO.setCouponDiscount(param.getCouponDiscount().toString());
        }
        couponDetailDTO.setCoverUrl(param.getCoverUrl());
        couponDetailDTO.setDescription(param.getDescription());
        couponDetailDTO.setEndTime(param.getEndTime());
        if(couponDetailDTO.getCommodityScopeType()==3 || couponDetailDTO.getCommodityScopeType()==4){
            short scopeType = couponDetailDTO.getCommodityScopeType();
            scopeType-=2;
            couponDetailDTO.setCommodityScopeType(scopeType);

            List<TCouponSpu> spus = param.getSpus();
            List<TCouponSku> allSkus = new ArrayList<>();
            for (TCouponSpu spu : spus) {
                allSkus.addAll(spu.getSkus());
            }
            couponDetailDTO.setCommodities(allSkus.stream().map(x -> x.getPriceCode()).collect(Collectors.toList()));
        }

        log.info("调用crm添加小程序券的参数为：{}",JSONObject.toJSONString(couponDetailDTO));
        ServerResponseEntity serverResponseEntity = crmCouponFeignClient.couponUpdatePush(couponDetailDTO);
        log.info("调用crm添加小程序券的返回值为：{}",JSONObject.toJSONString(serverResponseEntity));
        if (serverResponseEntity.isFail()) {
            throw new LuckException(serverResponseEntity.getMsg());
        }
        return ServerResponseEntity.success();
    }

    private void addSpus(Long id, List<TCouponSpu> spus) {
        for (TCouponSpu spu : spus) {
            spu.setCouponId(id);
            tCouponSpuMapper.insert(spu);

            if(CollUtil.isEmpty(spu.getSkus())){
                Assert.faild("spus.sku商品明细不能为空。");
            }

            List<TCouponSku> skus = new ArrayList<>();
            for (TCouponSku dto : spu.getSkus()) {
                TCouponSku sku = new TCouponSku();
                sku.setCouponId(id);
                sku.setSpuId(spu.getSpuId());
                sku.setPriceCode(dto.getPriceCode());
                skus.add(sku);
            }
            tCouponSkuMapper.insertBatch(skus);
        }
    }

    /**
     * 同步优惠券
     *
     * @param param 优惠券详情
     */
    @Override
    public ServerResponseEntity<CouponSyncDto> syncAddCoupon(com.mall4j.cloud.api.coupon.dto.CouponDetailDTO param) {
        TCoupon tCoupon = BeanUtil.copyProperties(param, TCoupon.class, "commodities", "storeCodes", "id");
        if (param.getStoreScopeType() == 0) {
            tCoupon.setIsAllShop(true);
        } else {
            tCoupon.setIsAllShop(false);
        }
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO != null) {
            tCoupon.setCreateId(userInfoInTokenBO.getUserId());
            tCoupon.setUpdateId(userInfoInTokenBO.getUserId());
        }
        String crmCouponId = param.getId();
        tCoupon.setCrmCouponId(crmCouponId);
        if (StringUtils.isNotBlank(param.getCouponDiscount())) {
            BigDecimal mul = NumberUtil.mul(param.getCouponDiscount(), "10");
            tCoupon.setCouponDiscount(mul);
        }
        if (StringUtils.isNotBlank(param.getReduceAmount())) {
            tCoupon.setReduceAmount(new BigDecimal(param.getReduceAmount()).multiply(new BigDecimal(100)));
        }
        if (StringUtils.isNotBlank(param.getAmountLimitNum())) {
            tCoupon.setAmountLimitNum(new BigDecimal(param.getAmountLimitNum()).multiply(new BigDecimal(100)));
        }
        if (StringUtils.isNotBlank(param.getMaxDeductionAmount())) {
            tCoupon.setMaxDeductionAmount(new BigDecimal(param.getMaxDeductionAmount()).multiply(new BigDecimal(100)));
        }
        List<TCoupon> exits = null;
        if (StringUtils.isNotBlank(crmCouponId) && (exits = tCouponMapper.selectList(new LambdaQueryWrapper<TCoupon>().eq(TCoupon::getCrmCouponId, crmCouponId))) != null) {
            int size = exits.size();
            if (size > 1) {
                throw new LuckException("重复推送优惠券,id：" + crmCouponId);
            }
            if (size == 1) {
                param.setCouponid(exits.get(0).getId());
                return this.syncUpdateCoupon(param);
            }
        }
        //优惠券商品关系 如果为 1||2  现新逻辑要转换为3||4来保存
        if(param.getCommodityScopeType() == 1 || param.getCommodityScopeType() == 2){
            short type = param.getCommodityScopeType();
            type +=2;
            tCoupon.setCommodityScopeType(type);
            param.setCommodityScopeType(type);
        }
        tCouponMapper.insert(tCoupon);

        //优惠券码关系
        if(!CollectionUtils.isEmpty(param.getCodes())){
            addCodes(param.getCodes(),tCoupon.getId());
        }

        //原逻辑 20230304日修改注释
        //优惠券商品关系
//        if(param.getCommodityScopeType() != null && (param.getCommodityScopeType() == 1 || param.getCommodityScopeType() == 2) && !CollectionUtils.isEmpty(param.getCommodities())){
//            addCommodities(tCoupon.getId(), param.getCommodities());
//        }

        //优惠券商品关系
        if(param.getCommodityScopeType() != null && (param.getCommodityScopeType() == 3 || param.getCommodityScopeType() == 4) && !CollectionUtils.isEmpty(param.getCommodities())){
            //保存crm商品数据
            addconverteCrmCommodities(tCoupon.getId(), param.getCommodities());
        }

        //优惠券门店关系
        if(param.getStoreScopeType() != null && param.getStoreScopeType() == 1 && !CollectionUtils.isEmpty(param.getStoreCodes())){
            addShops(tCoupon.getId(),param.getStoreCodes());
        }
        //优惠券分类关系
        if(param.getCategoryScopeType() != null && param.getCategoryScopeType() == 2 && !CollectionUtils.isEmpty(param.getCategorys())){
            addCategorys(tCoupon.getId(),param.getCategorys());
        }

        return ServerResponseEntity.success(new CouponSyncDto(tCoupon.getId(), tCoupon.getCrmCouponId()));
    }

    /**
     *
     * @param id
     * @param spuCode
     */
    private void addconverteCrmCommodities(Long id, List<String> spuCode) {
        List<String> spuCodes = spuCode.stream().map(x -> x.substring(0, x.indexOf("-"))).collect(Collectors.toList());

        ServerResponseEntity<List<SpuCodeVo>> listServerResponseEntity = spuFeignClient.listSpuBySpuCodes(spuCodes);
        log.info("根据商品code查询商品，结果为：" + JSONObject.toJSONString(listServerResponseEntity));
        Map<String, Long> spuCodesMap = new HashMap<>(spuCode.size());
        if(listServerResponseEntity.isFail() || CollUtil.isEmpty(listServerResponseEntity.getData())){
            Assert.faild("根据商品code查询商品数据失败或者商品不存在。");
        }

        //按照spu级别保存
        for (SpuCodeVo spuCodeVo : listServerResponseEntity.getData()) {
            spuCodesMap.put(spuCodeVo.getSpuCode(), spuCodeVo.getSpuId());
            TCouponSpu spu = new TCouponSpu();
            spu.setCouponId(id);
            spu.setSpuId(spuCodeVo.getSpuId());
            tCouponSpuMapper.insert(spu);
        }

        //按照sku的priceCode 保存
        List<TCouponSku> skus = new ArrayList<>();
        for (String s : spuCode) {
            if(!spuCodesMap.containsKey(s.substring(0, s.indexOf("-")))){
                log.warn(StrUtil.format("商品在小程序中不存在，spucode:{}。",s));
                continue;
            }
            TCouponSku sku = new TCouponSku();
            sku.setCouponId(id);
            long spuId = spuCodesMap.get(s.substring(0, s.indexOf("-")));
            sku.setSpuId(spuId);
            sku.setPriceCode(s);
            skus.add(sku);
        }
        tCouponSkuMapper.insertBatch(skus);

    }

    @Override
    public ServerResponseEntity<CouponDetailDTO> detail(Long id) {
        TCoupon tCoupon = tCouponMapper.selectById(id);
        CouponDetailDTO result = BeanUtil.copyProperties(tCoupon, CouponDetailDTO.class);

        if (result.getKind() == 2){
            List<TCouponCode> tCouponCodes = codeMapper.selectList(new LambdaQueryWrapper<TCouponCode>().eq(TCouponCode::getCouponId, result.getId()));
            result.setCodes(tCouponCodes.stream().map(c -> c.getCouponCode()).collect(Collectors.toList()));
            result.setCodeSum(tCouponCodes.size());
            Integer stock = tCouponUserMapper.selectCount(new LambdaQueryWrapper<TCouponUser>()
                    .eq(TCouponUser::getCouponId, result.getId()));
            if (tCouponCodes != null && tCouponCodes.size() > 0) {
                result.setCodeStock(tCouponCodes.size() - stock);
            } else {
                result.setCodeStock(0);
            }

        }
        // 如果优惠券适用商品类型为 1 指定商品(spu)可用 2指定商品(spu)不可用时
        if (result.getCommodityScopeType() == 1 ||result.getCommodityScopeType() == 2 ){
            List<TCouponCommodity> tCouponCommodities = commodityMapper.selectList(new LambdaQueryWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId, result.getId()));
            result.setCommodityNum(tCouponCommodities.size());
            result.setCommodities(tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList()));
        }
        // 如果优惠券适用商品类型为 3 指定商品(sku)可用 4指定商品(sku)不可用时
        if (result.getCommodityScopeType() == 3 ||result.getCommodityScopeType() == 4 ){
            List<TCouponSpu> spus =tCouponSpuMapper.selectList(new LambdaQueryWrapper<TCouponSpu>().eq(TCouponSpu::getCouponId, result.getId()));
            for (TCouponSpu spu : spus) {
                List<TCouponSku> skus =tCouponSkuMapper.selectList(new LambdaQueryWrapper<TCouponSku>().eq(TCouponSku::getCouponId, result.getId()).eq(TCouponSku::getSpuId, spu.getSpuId()));
                spu.setSkus(skus);
            }
            result.setSpus(spus);
        }

        if (!result.getIsAllShop()){
            List<TCouponShop> couponShops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, result.getId()));
            result.setShops(couponShops.stream().map(TCouponShop::getShopId).collect(Collectors.toList()));
        }
        if(result.getCategoryScopeType()==2){
            List<TCouponCategory> categories = tCouponCategoryMapper.selectList(new LambdaQueryWrapper<TCouponCategory>().eq(TCouponCategory::getCouponId, result.getId()));
            result.setCategorys(categories.stream().map(TCouponCategory::getCategory).collect(Collectors.toList()));
        }

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> update(CouponDetailDTO param) {
        TCoupon tCoupon = BeanUtil.copyProperties(param, TCoupon.class);
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        tCoupon.setUpdateId(AuthUserContext.get().getUserId());
        tCouponMapper.updateById(tCoupon);
        if (tCoupon.getStartTime() == null && tCoupon.getEndTime() == null) {
            LambdaUpdateWrapper<TCoupon> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(TCoupon::getEndTime,null);
            wrapper.set(TCoupon::getStartTime,null);
            wrapper.eq(TCoupon::getId,tCoupon.getId());
            tCouponMapper.update(tCoupon,wrapper);
        }
//        codeMapper.delete(new LambdaUpdateWrapper<TCouponCode>().eq(TCouponCode::getCouponId,param.getId()));
//        //优惠券码关系
//        if(!CollectionUtils.isEmpty(param.getCodes())){
//            addCodes(param.getCodes(),tCoupon.getId());
//        }

        commodityMapper.delete(new LambdaUpdateWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId,param.getId()));
        //优惠券商品关系 spu级别
        if(!CollectionUtils.isEmpty(param.getCommodities())){
            addCommodities(param.getCommodities(),tCoupon.getId());
        }

        tCouponSpuMapper.delete(new LambdaUpdateWrapper<TCouponSpu>().eq(TCouponSpu::getCouponId,param.getId()));
        tCouponSkuMapper.delete(new LambdaUpdateWrapper<TCouponSku>().eq(TCouponSku::getCouponId,param.getId()));
        //优惠券商品关系SKU级别
        if(!CollectionUtils.isEmpty(param.getSpus())){
            addSpus(tCoupon.getId(),param.getSpus());
        }

        tCouponShopMapper.delete(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId,param.getId()));
        //优惠券门店关系
        if(!CollectionUtils.isEmpty(param.getShops())){
            addShops(param.getShops(),tCoupon.getId());
        }

        tCouponCategoryMapper.delete(new LambdaQueryWrapper<TCouponCategory>().eq(TCouponCategory::getCouponId,param.getId()));
        //优惠券适用商品分类
        if(!CollectionUtils.isEmpty(param.getCategorys())){
            addCategorys(tCoupon.getId(),param.getCategorys());
        }

        com.mall4j.cloud.api.coupon.dto.CouponDetailDTO couponDetailDTO = new com.mall4j.cloud.api.coupon.dto.CouponDetailDTO();
        couponDetailDTO.setId(tCoupon.getId()+"");
        couponDetailDTO.setKind(param.getKind());
        couponDetailDTO.setName(param.getName());
        couponDetailDTO.setNote(param.getNote());
        if (param.getReduceAmount() != null) {
            couponDetailDTO.setReduceAmount(param.getReduceAmount().divide(new BigDecimal(100)).toString());
        }
        couponDetailDTO.setStartTime(param.getStartTime());
        couponDetailDTO.setTimeType(param.getTimeType());
        couponDetailDTO.setType(param.getType());
        couponDetailDTO.setAfterReceiveDays(param.getAfterReceiveDays());
        if (param.getAmountLimitNum() != null) {
            couponDetailDTO.setAmountLimitNum(param.getAmountLimitNum().divide(new BigDecimal(100)).toString());
        }
        couponDetailDTO.setAmountLimitType(param.getAmountLimitType());
        couponDetailDTO.setApplyScopeType(param.getApplyScopeType());
        couponDetailDTO.setCodes(param.getCodes());
        couponDetailDTO.setCodeNum(param.getCodeSum());
        if (param.getCommodities() != null) {
            couponDetailDTO.setCommodities(param.getCommodities().stream().map(x -> x + "").collect(Collectors.toList()));
        }
        couponDetailDTO.setCommodityLimitNum(param.getCommodityLimitNum());
        couponDetailDTO.setCommodityLimitType(param.getCommodityLimitType());
        couponDetailDTO.setCommodityNum(param.getCommodityNum());
        couponDetailDTO.setCommodityScopeType(param.getCommodityScopeType());
        if (param.getCouponDiscount() != null){
            couponDetailDTO.setCouponDiscount(param.getCouponDiscount().toString());
        };
        couponDetailDTO.setCoverUrl(param.getCoverUrl());
        couponDetailDTO.setDescription(param.getDescription());
        couponDetailDTO.setEndTime(param.getEndTime());

        if(couponDetailDTO.getCommodityScopeType()==3 || couponDetailDTO.getCommodityScopeType()==4){
            short scopeType = couponDetailDTO.getCommodityScopeType();
            scopeType-=2;
            couponDetailDTO.setCommodityScopeType(scopeType);

            List<TCouponSpu> spus = param.getSpus();
            List<TCouponSku> allSkus = new ArrayList<>();
            for (TCouponSpu spu : spus) {
                allSkus.addAll(spu.getSkus());
            }
            couponDetailDTO.setCommodities(allSkus.stream().map(x -> x.getPriceCode()).collect(Collectors.toList()));
        }

        log.info("调用crm同步小程序券的参数为：{}",JSONObject.toJSONString(couponDetailDTO));
        ServerResponseEntity serverResponseEntity = crmCouponFeignClient.couponUpdatePush(couponDetailDTO);
        log.info("调用crm更新小程序券的返回值为：{}",JSONObject.toJSONString(serverResponseEntity));
        if (serverResponseEntity.isFail()) {
            throw new LuckException(serverResponseEntity.getMsg());
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> addCode(CouponDetailDTO param) {
//        codeMapper.delete(new LambdaUpdateWrapper<TCouponCode>().eq(TCouponCode::getCouponId,param.getId()));
        //优惠券码关系
        if(!CollectionUtils.isEmpty(param.getCodes())){
            addCodes(param.getCodes(),param.getId());
        }
        return ServerResponseEntity.success();
    }

    /**
     * 同步优惠券
     *
     * @param param 优惠券详情
     */
    @Override
    public ServerResponseEntity<CouponSyncDto> syncUpdateCoupon(com.mall4j.cloud.api.coupon.dto.CouponDetailDTO param) {
        TCoupon tCoupon = BeanUtil.copyProperties(param, TCoupon.class, "commodities", "storeCodes", "id");
        if (param.getStoreScopeType() == 0) {
            tCoupon.setIsAllShop(true);
        } else {
            tCoupon.setIsAllShop(false);
        }
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO != null) {
            tCoupon.setUpdateId(userInfoInTokenBO.getUserId());
        }
        tCoupon.setCrmCouponId(param.getId());
        if (StringUtils.isNotBlank(param.getCouponDiscount())) {
            BigDecimal mul = NumberUtil.mul(param.getCouponDiscount(), "10");
            tCoupon.setCouponDiscount(mul);
        }
        if (StringUtils.isNotBlank(param.getReduceAmount())) {
            tCoupon.setReduceAmount(new BigDecimal(param.getReduceAmount()).multiply(new BigDecimal(100)));
        }
        if (StringUtils.isNotBlank(param.getAmountLimitNum())) {
            tCoupon.setAmountLimitNum(new BigDecimal(param.getAmountLimitNum()).multiply(new BigDecimal(100)));
        }
        if (StringUtils.isNotBlank(param.getMaxDeductionAmount())) {
            tCoupon.setMaxDeductionAmount(new BigDecimal(param.getMaxDeductionAmount()).multiply(new BigDecimal(100)));
        }

        tCoupon.setId(param.getCouponid());
        if (tCoupon.getId() == null && StringUtils.isNotBlank(param.getId())) {

            List<TCoupon> exists = tCouponMapper.selectList(new LambdaQueryWrapper<TCoupon>().eq(TCoupon::getCrmCouponId, param.getId()));
            int size = exists.size();
            if (size > 1) {
                throw new LuckException("重复推送优惠券,id：" + param.getId());
            }
            if (size == 1) {
                tCoupon.setId(exists.get(0).getId());
            }

        }
        if (tCoupon.getId() == null) {
            throw new LuckException("要更新的优惠券不存在，请求同步添加");
        }
        //优惠券商品关系 如果为 1||2  现新逻辑要转换为3||4来保存
        if(param.getCommodityScopeType() == 1 || param.getCommodityScopeType() == 2){
            short type = param.getCommodityScopeType();
            type +=2;
            tCoupon.setCommodityScopeType(type);
            param.setCommodityScopeType(type);
        }
        tCouponMapper.updateById(tCoupon);

        codeMapper.delete(new LambdaUpdateWrapper<TCouponCode>().eq(TCouponCode::getCouponId,tCoupon.getId()));
        //优惠券码关系
        if(!CollectionUtils.isEmpty(param.getCodes())){
            addCodes(param.getCodes(),tCoupon.getId());
        }

        //原逻辑 20230304日修改注释
//        commodityMapper.delete(new LambdaUpdateWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId,tCoupon.getId()));
//        //优惠券商品关系
//        if(param.getCommodityScopeType() != null && (param.getCommodityScopeType() == 1 || param.getCommodityScopeType() == 2) && !CollectionUtils.isEmpty(param.getCommodities())){
//            addCommodities(tCoupon.getId(), param.getCommodities());
//        }
        //优惠券商品关系
        tCouponSpuMapper.delete(new LambdaUpdateWrapper<TCouponSpu>().eq(TCouponSpu::getCouponId,tCoupon.getId()));
        tCouponSkuMapper.delete(new LambdaUpdateWrapper<TCouponSku>().eq(TCouponSku::getCouponId,tCoupon.getId()));
        if(param.getCommodityScopeType() != null && (param.getCommodityScopeType() == 3 || param.getCommodityScopeType() == 4) && !CollectionUtils.isEmpty(param.getCommodities())){
            //保存crm商品数据
//            addCommodities(tCoupon.getId(), param.getCommodities());
            addconverteCrmCommodities(tCoupon.getId(), param.getCommodities());
        }

        tCouponShopMapper.delete(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId,tCoupon.getId()));
        //优惠券门店关系
        if(param.getStoreScopeType() != null && param.getStoreScopeType() == 1 && !CollectionUtils.isEmpty(param.getStoreCodes())){
            addShops(tCoupon.getId(), param.getStoreCodes());
        }
        tCouponCategoryMapper.delete(new LambdaQueryWrapper<TCouponCategory>().eq(TCouponCategory::getCouponId,tCoupon.getId()));

        //优惠券分类关系
        if(param.getCategoryScopeType() != null && param.getCategoryScopeType() == 2 && !CollectionUtils.isEmpty(param.getCategorys())){
            addCategorys(tCoupon.getId(),param.getCategorys());
        }

        return ServerResponseEntity.success(new CouponSyncDto(tCoupon.getId(), tCoupon.getCrmCouponId()));
    }

    @Override
    public ServerResponseEntity<Void> failure(Long id) {
        TCoupon tCoupon = new TCoupon();
        tCoupon.setId(id);
        tCoupon.setStatus(TCouponStatus.DEL.value());
        tCoupon.setUpdateId(AuthUserContext.get().getUserId());
        tCouponMapper.updateById(tCoupon);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<String>> importCode(MultipartFile file) {
        log.info("导入文件为：{}",file);

        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("获取输入流异常");
            e.printStackTrace();
        }

        //调用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //校验模板的正确性
        List<Object> objects = excelReader.readRow(0);
        log.info("模板表头信息：{}", JSONObject.toJSONString(objects));

        //列名和对象属性名一致
        excelReader.addHeaderAlias("序号","id");
        excelReader.addHeaderAlias("优惠券券码","couponCode");

        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
        List<TCouponCode> importObjects = excelReader.readAll(TCouponCode.class);
        List<String> result = importObjects.stream().map(TCouponCode::getCouponCode).collect(Collectors.toList());

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<List<TCoupon>> selectCouponByIds(List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            ServerResponseEntity.success();
        }
        List<TCoupon> tCoupons = tCouponMapper.selectCouponByIds(ids);
        return ServerResponseEntity.success(tCoupons);
    }

    @Override
    public ServerResponseEntity<PageInfo<TCouponCode>> couponCodeList(CodeListDTO param) {
        PageInfo<TCouponCode> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                codeMapper.list(param)
        );
        return ServerResponseEntity.success(result);
    }

    //todo  couponId+分页信息做key 缓存数据
    @Override
    public ServerResponseEntity<PageVO<SpuCommonVO>> couponSpuList(SpuListDTO param) {
        TCoupon tCoupon = tCouponMapper.selectById(param.getCouponId());
        //适用商品
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        productSearchDTO.setKeyword(param.getKeyword());
        productSearchDTO.setStatus(1);
        productSearchDTO.setSort(param.getSort());
        if (tCoupon.getCommodityScopeType() != 0){
            if(tCoupon.getCommodityScopeType() == 1 || tCoupon.getCommodityScopeType() ==2){
                //部分商品适用
                List<TCouponCommodity> tCouponCommodities = tCouponCommodityMapper.selectList(new LambdaQueryWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId, param.getCouponId()));
                List<Long> commodities = tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList());

                if (tCoupon.getCommodityScopeType() == 1){
                    productSearchDTO.setSpuIds(commodities);
                }
                if (tCoupon.getCommodityScopeType() == 2) {
                    productSearchDTO.setSpuIdsExclude(commodities);
                }
            }
            if(tCoupon.getCommodityScopeType() == 3 || tCoupon.getCommodityScopeType() ==4){
                //部分商品适用
                List<TCouponSku> tCouponCommodities = tCouponSkuMapper.selectList(new LambdaQueryWrapper<TCouponSku>().eq(TCouponSku::getCouponId, param.getCouponId()));
                List<Long> commodities = tCouponCommodities.stream().map(TCouponSku::getSpuId).collect(Collectors.toList());
                if (tCoupon.getCommodityScopeType() == 3){
                    productSearchDTO.setSpuIds(commodities);
                }
                if (tCoupon.getCommodityScopeType() == 4) {
                    productSearchDTO.setSpuIdsExclude(commodities);
                }
            }
        }

        if(tCoupon.getCategoryScopeType()==2){
            List<TCouponCategory> categorys = tCouponCategoryMapper.selectList(new LambdaQueryWrapper<TCouponCategory>().eq(TCouponCategory::getCouponId, param.getCouponId()));
            Set<String> strCategorys = categorys.stream().map(TCouponCategory::getCategory).collect(Collectors.toSet());
            List strCategorylist = new ArrayList(strCategorys);
            productSearchDTO.setErpCategorys(strCategorylist);
        }


        productSearchDTO.setPageNum(param.getPageNum());
        productSearchDTO.setPageSize(param.getPageSize());
        productSearchDTO.setSpuSearchAttr(param.getSpuSearchAttr());
        productSearchDTO.setSearchActivityType(param.getSearchActivityType());
        log.info("调用商品信息查询参数为：{}", JSONObject.toJSONString(productSearchDTO));

        return productFeignClient.couponSearch(productSearchDTO);
    }

    @Override
    public ServerResponseEntity<PageVO<SelectedStoreVO>> couponShopList(SpuListDTO param) {
        InsiderStorePageDTO insiderStorePageDTO = new InsiderStorePageDTO();
        insiderStorePageDTO.setPageNum(param.getPageNum());
        insiderStorePageDTO.setPageSize(param.getPageSize());
        insiderStorePageDTO.setProvinceId(param.getProvinceId());
        insiderStorePageDTO.setAreaId(param.getAreaId());
        insiderStorePageDTO.setCityId(param.getCityId());
        insiderStorePageDTO.setCityName(param.getCityName());
        insiderStorePageDTO.setLng(param.getLng());
        insiderStorePageDTO.setLat(param.getLat());
        insiderStorePageDTO.setKeyword(param.getKeyword());
        insiderStorePageDTO.setSlcNames(param.getSlcNames());

        //判断是否为兑礼到店类型活动
        Boolean toShop = false;
        if (Objects.nonNull(param.getToShop()) &&
                param.getToShop().equals(ConvertTypeEnum.TO_SHOP.getType())){
            toShop = true;
        }
        if (param.getType() == 0){
            TCoupon tCoupon = tCouponMapper.selectById(param.getCouponId());
            if (!tCoupon.getIsAllShop()){
                List<TCouponShop> couponShops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, param.getCouponId()));
                List<Long> shopIds = couponShops.stream().map(TCouponShop::getShopId).collect(Collectors.toList());
                insiderStorePageDTO.setStoreIdList(shopIds);
            }
        }

        if (param.getType() == 1){
            ServerResponseEntity<List<Long>> shops = scoreActivityClient.getShops(param.getActivityId());
            if (shops.isSuccess()){
                if(!CollectionUtils.isEmpty(shops.getData())){
                    insiderStorePageDTO.setStoreIdList(shops.getData());
                }
            }else {
                throw new LuckException("获取积分活动使用门店失败");
            }
        }

        log.info("调用门店信息查询参数为：{}", JSONObject.toJSONString(insiderStorePageDTO));
        ServerResponseEntity<PageVO<SelectedStoreVO>> pageVOServerResponseEntity = storeFeignClient.storePage(insiderStorePageDTO);
        if (pageVOServerResponseEntity.isFail()){
            log.info("调用门店信息查询失败，返回值为：{}", JSONObject.toJSONString(pageVOServerResponseEntity));
        }
        //兑礼到店需查询中台相关库存
        if (toShop && CollectionUtil.isNotEmpty(pageVOServerResponseEntity.getData().getList())){
            //调用中台相关接口查询商品之于门店的库存量1
            List<SelectedStoreVO> selectedStoreVOList = tCouponManager
                    .wrapperInventory(pageVOServerResponseEntity.getData().getList(), param.getCouponId(),
                            param.getActivityId());
    
            pageVOServerResponseEntity.getData().setList(selectedStoreVOList);
            if (CollectionUtil.isEmpty(selectedStoreVOList)){
                pageVOServerResponseEntity.getData().setPages(0);
                pageVOServerResponseEntity.getData().setTotal(0L);
            }
        }
        return pageVOServerResponseEntity;
    }

    private void addCodes(List<String> codes, Long id){
        List<TCouponCode> couponCodes = new ArrayList<>();
        codes.forEach(temp ->{
            TCouponCode tCouponCode = new TCouponCode();
            tCouponCode.setCouponId(id);
            tCouponCode.setCouponCode(temp);
            couponCodes.add(tCouponCode);
        });
        codeMapper.insertBatch(couponCodes);
    }

    private void addCommodities(List<Long> commodities,Long id){
        List<TCouponCommodity> couponCommodities = new ArrayList<>();
        commodities.forEach(temp ->{
            TCouponCommodity tCouponCommodity = new TCouponCommodity();
            tCouponCommodity.setCouponId(id);
            tCouponCommodity.setCommodityId(temp);
            couponCommodities.add(tCouponCommodity);
        });
        commodityMapper.insertBatch(couponCommodities);
    }

    private void addCommodities(Long id, List<String> spuCode){
        List<TCouponCommodity> couponCommodities = new ArrayList<>();
        List<String> spuCodes = spuCode.stream().map(x -> x.substring(0, x.indexOf("-"))).collect(Collectors.toList());
        ServerResponseEntity<List<SpuCodeVo>> listServerResponseEntity = spuFeignClient.listSpuBySpuCodes(spuCodes);
        log.info("根据商品code查询商品，结果为：" + JSONObject.toJSONString(listServerResponseEntity));
        Map<String, Long> spuCodesMap = new HashMap<>(spuCode.size());
        if (listServerResponseEntity.isSuccess()) {
            for (SpuCodeVo spuCodeVo : listServerResponseEntity.getData()) {
                spuCodesMap.put(spuCodeVo.getSpuCode(), spuCodeVo.getSpuId());
            }
        }
        spuCodes.forEach(temp ->{
            TCouponCommodity tCouponCommodity = new TCouponCommodity();
            tCouponCommodity.setCouponId(id);
            Long orDefault = spuCodesMap.getOrDefault(temp, null);
            if (orDefault == null) {
                throw new LuckException("不存在商品code为:" + temp + "的商品");
            }
            tCouponCommodity.setCommodityId(orDefault);
            tCouponCommodity.setSpuCode(temp);
            couponCommodities.add(tCouponCommodity);
        });
        commodityMapper.insertBatch(couponCommodities);
    }

    private void addShops(List<Long> shops,Long id){
        List<TCouponShop> couponShops = new ArrayList<>();
        shops.forEach(temp ->{
            TCouponShop couponShop = new TCouponShop();
            couponShop.setCouponId(id);
            couponShop.setShopId(temp);
            couponShops.add(couponShop);
        });
        tCouponShopMapper.insertBatch(couponShops);
    }

    private void addShops(Long id, List<String> shopCodes){
        List<StoreCodeVO> byStoreCodes = storeFeignClient.findByStoreCodes(shopCodes);
        Map<String, Long> storeMap = new HashMap<>(shopCodes.size());
        for (StoreCodeVO byStoreCode : byStoreCodes) {
            storeMap.put(byStoreCode.getStoreCode(), byStoreCode.getStoreId());
        }
        List<TCouponShop> couponShops = new ArrayList<>();
        shopCodes.forEach(temp ->{
            TCouponShop couponShop = new TCouponShop();
            couponShop.setCouponId(id);
            Long orDefault = storeMap.getOrDefault(temp, null);
            if (orDefault == null) {
                throw new LuckException("不存在门店code为:" + temp + "的门店");
            }
            couponShop.setShopId(orDefault);
            couponShop.setShopCode(temp);
            couponShops.add(couponShop);
        });
        tCouponShopMapper.insertBatch(couponShops);
    }

    private void addCategorys(Long id, List<String> categorys) {
        List<TCouponCategory> couponCategories = new ArrayList<>();
        categorys.forEach(temp ->{
            TCouponCategory couponCategory = new TCouponCategory();
            couponCategory.setCouponId(id);
            couponCategory.setCategory(temp);
            couponCategories.add(couponCategory);
        });
        tCouponCategoryMapper.insertBatch(couponCategories);
    }

    @Override
    public void syncCoupons() {
        List<TCoupon> tCoupons = tCouponMapper.selectList(new LambdaQueryWrapper<TCoupon>().eq(TCoupon::getSourceType, (short) 1));
        if(!CollectionUtils.isEmpty(tCoupons)){
            tCoupons.forEach(temp -> {
                if (temp.getTimeType() != 0) {
                    com.mall4j.cloud.api.coupon.dto.CouponDetailDTO couponDetailDTO = new com.mall4j.cloud.api.coupon.dto.CouponDetailDTO();
                    couponDetailDTO.setId(temp.getId() + "");
                    couponDetailDTO.setKind(temp.getKind());
                    couponDetailDTO.setName(temp.getName());
                    couponDetailDTO.setNote(temp.getNote());
                    if (temp.getReduceAmount() != null) {
                        couponDetailDTO.setReduceAmount(temp.getReduceAmount().divide(new BigDecimal(100)).toString());
                    }
                    couponDetailDTO.setStartTime(temp.getStartTime());
                    couponDetailDTO.setTimeType((short) temp.getTimeType());
                    couponDetailDTO.setType(temp.getType());
                    couponDetailDTO.setAfterReceiveDays(temp.getAfterReceiveDays());
                    if (temp.getAmountLimitNum() != null) {
                        couponDetailDTO.setAmountLimitNum(temp.getAmountLimitNum().divide(new BigDecimal(100)).toString());
                    }
                    couponDetailDTO.setAmountLimitType(temp.getAmountLimitType());
                    couponDetailDTO.setApplyScopeType(temp.getApplyScopeType());
                    List<TCouponCode> tCouponCodes = codeMapper.selectList(new LambdaQueryWrapper<TCouponCode>().eq(TCouponCode::getCouponId, temp.getId()));
                    if (tCouponCodes != null && tCouponCodes.size() > 0) {
                        couponDetailDTO.setCodes(tCouponCodes.stream().map(TCouponCode::getCouponCode).collect(Collectors.toList()));
                        couponDetailDTO.setCodeNum(tCouponCodes.size());
                    }
                    List<TCouponCommodity> tCouponCommodities = tCouponCommodityMapper.selectList(new LambdaQueryWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId, temp.getId()));
                    if (tCouponCommodities != null && tCouponCommodities.size() > 0) {
                        List<Long> collect = tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList());
                        couponDetailDTO.setCommodities(collect.stream().map(x -> x + "").collect(Collectors.toList()));
                        couponDetailDTO.setCommodityNum(collect.size());
                    }

                    couponDetailDTO.setCommodityLimitNum(temp.getCommodityLimitNum());
                    couponDetailDTO.setCommodityLimitType(temp.getCommodityLimitType());
                    couponDetailDTO.setCommodityScopeType(temp.getCommodityScopeType());
                    if (temp.getCouponDiscount() != null) {
                        couponDetailDTO.setCouponDiscount(temp.getCouponDiscount().toString());
                    }
                    couponDetailDTO.setCoverUrl(temp.getCoverUrl());
                    couponDetailDTO.setDescription(temp.getDescription());
                    couponDetailDTO.setEndTime(temp.getEndTime());

                    log.info("调用crm同步小程序券的参数为：{}", JSONObject.toJSONString(couponDetailDTO));
                    ServerResponseEntity serverResponseEntity = crmCouponFeignClient.couponUpdatePush(couponDetailDTO);
                    log.info("调用crm同步小程序券的返回值为：{}", JSONObject.toJSONString(serverResponseEntity));
                    if (serverResponseEntity.isFail()) {
                        throw new LuckException(serverResponseEntity.getMsg());
                    }
                }
            });
        }
        log.info("crm同步小程序券完成");
    }

    @Override
    public List<String> queryCrmIds(QueryCrmIdsDTO queryCrmIdsDTO) {
        List<String> crmIds = tCouponMapper.queryCrmIds(queryCrmIdsDTO);
        if(CollUtil.isEmpty(crmIds)){
            return new ArrayList<>();
        }
        return crmIds;
    }

    @Override
    @Cacheable(cacheNames = CouponCacheNames.CRM_COUPON_INFO, key = "#crmCouponId")
    public TCoupon queryByCrmCouponId(String crmCouponId) {
        return tCouponMapper.selectOne(new LambdaQueryWrapper<TCoupon>().eq(TCoupon::getCrmCouponId, crmCouponId));
    }

    @Override
    @CacheEvict(cacheNames = CouponCacheNames.CRM_COUPON_INFO, key = "#crmCouponId")
    public void removeByCrmCouponId(String crmCouponId) {
    }

    @Override
    @Cacheable(cacheNames = CouponCacheNames.COUPON_INFO, key = "#id")
    public CouponDetailDTO queryCacheByCouponId(Long id) {
        TCoupon tCoupon = tCouponMapper.selectById(id);
        CouponDetailDTO result = BeanUtil.copyProperties(tCoupon, CouponDetailDTO.class);

        // 如果优惠券适用商品类型为 1 指定商品(spu)可用 2指定商品(spu)不可用时
        if (result.getCommodityScopeType() == 1 ||result.getCommodityScopeType() == 2 ){
            List<TCouponCommodity> tCouponCommodities = commodityMapper.selectList(new LambdaQueryWrapper<TCouponCommodity>().eq(TCouponCommodity::getCouponId, result.getId()));
            result.setCommodityNum(tCouponCommodities.size());
            result.setCommodities(tCouponCommodities.stream().map(TCouponCommodity::getCommodityId).collect(Collectors.toList()));
        }
        // 如果优惠券适用商品类型为 3 指定商品(sku)可用 4指定商品(sku)不可用时
        if (result.getCommodityScopeType() == 3 ||result.getCommodityScopeType() == 4 ){
            List<TCouponSku> skus = tCouponSkuMapper.selectList(new LambdaQueryWrapper<TCouponSku>().eq(TCouponSku::getCouponId, result.getId()));
            List<String> priceCodes = skus.stream().map(t -> t.getPriceCode()).distinct().collect(Collectors.toList());
            result.setPriceCodes(priceCodes);
        }

        if (!result.getIsAllShop()){
            List<TCouponShop> couponShops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, result.getId()));
            result.setShops(couponShops.stream().map(TCouponShop::getShopId).collect(Collectors.toList()));
        }
        if(result.getCategoryScopeType()==2){
            List<TCouponCategory> categories = tCouponCategoryMapper.selectList(new LambdaQueryWrapper<TCouponCategory>().eq(TCouponCategory::getCouponId, result.getId()));
            result.setCategorys(categories.stream().map(TCouponCategory::getCategory).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    @CacheEvict(cacheNames = CouponCacheNames.COUPON_INFO, key = "#id")
    public void removeCacheByCouponId(Long id) {

    }

    //    @Override
//    public ServerResponseEntity<Void> addCode(CouponDetailDTO param) {
//        //优惠券码关系
//        if(!CollectionUtils.isEmpty(param.getCodes())){
//            addCodes(param.getCodes(),param.getId());
//        }
//        return ServerResponseEntity.success();
//    }
}
