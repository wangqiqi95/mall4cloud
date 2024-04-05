package com.mall4j.cloud.transfer.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.transfer.mapper.*;
import com.mall4j.cloud.transfer.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券数据迁移service
 *
 * @luzhengxiang
 * @create 2022-04-11 9:11 AM
 **/
@Slf4j
@Service
public class CouponService {

    @Autowired
    CrmCouponGrpMapper crmCouponGrpMapper;
    @Autowired
    CrmCouponGrpShopsMapper crmCouponGrpShopsMapper;
    @Autowired
    CrmCouponGrpSkuMapper crmCouponGrpSkuMapper;
    @Autowired
    CrmCouponListMapper crmCouponListMapper;
    @Autowired
    TCouponMapper couponMapper;
    @Autowired
    TCouponShopMapper tCouponShopMapper;
    @Autowired
    TCouponCommodityMapper tCouponCommodityMapper;
    @Autowired
    EdBaseShopMapper edBaseShopMapper;
    @Autowired
    MallSalesOrderDtlMapper mallSalesOrderDtlMapper;
    @Autowired
    TCouponUserMapper tCouponUserMapper;
    @Autowired
    MallSalesOrderCouponMapper mallSalesOrderCouponMapper;

    @Autowired
    StoreFeignClient storeFeignClient;
    @Autowired
    SkuFeignClient skuFeignClient;


    Map<String,StoreCodeVO> storeMap = new HashMap<>();


    /**
     * 优惠券迁移
     *
     * crm_coupon_grp -->  t_coupon
     * crm_coupon_grp_shops --> t_coupon_commodity
     * crm_coupon_grp_sku --> t_coupon_shop
     * crm_coupon_list --> t_coupon_user
     */
    @Async
    public void couponTransfer(){
//        coupon();
        couponUser();
    }


    private void coupon(){
        List<CrmCouponGrp> couponGrps = crmCouponGrpMapper.list();
        for (CrmCouponGrp couponGrp : couponGrps) {
            TCoupon tCoupon = new TCoupon();
            tCoupon.setCode(StrUtil.toString(couponGrp.getId()));//优惠券id
            tCoupon.setName(couponGrp.getCouponname());//优惠券名称
            //怎么处理
            tCoupon.setKind(0);//优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）
            //couponGrp.couponType DJ代金券 ZK折扣券 LP礼品券 YQ邀请券 CX 促销券 YY易业券
            if(couponGrp.getCoupontype().equals("DJ")){
                tCoupon.setType(0);//优惠券类型（0：抵用券/1：折扣券）
                tCoupon.setReduceAmount(couponGrp.getCouponvalue().longValue() * 100);//抵用金额
            }else if(couponGrp.getCoupontype().equals("ZK")){
                tCoupon.setType(1);//优惠券类型（0：抵用券/1：折扣券）
                tCoupon.setCouponDiscount(couponGrp.getCouponvalue() * 10);//折扣力度
            }else{
                log.info("优惠券id:{},类型:{}。系统不支持，不执行同步",couponGrp.getId(),couponGrp.getCoupontype());
                continue;
            }
            tCoupon.setPriceType(1);//价格类型（0：吊牌价/1：实付金额）
            tCoupon.setAmountLimitType(0);//金额限制类型（0：不限/1：满额）
//            tCoupon.setAmountLimitNum();//限制金额
            tCoupon.setCommodityLimitType(0);//商品限制类型（0：不限/1：不超过/2：不少于）
//            tCoupon.setCommodityLimitNum();//商品限制件数

            /**
             * couponGrp  CouponProIsExclude 是否有商品限制
             */
            if(StrUtil.equals(couponGrp.getCouponproisexclude(),"0")){
                tCoupon.setCommodityScopeType(0);//适用商品类型（0：不限/1：指定商品）
            }else{
                tCoupon.setCommodityScopeType(1);//适用商品类型（0：不限/1：指定商品）
            }

            tCoupon.setApplyScopeType(0);//适用范围（0：不限/1：线上/2：线下）
            /**
             * couponGrp.ApplyIsExclude  是否有组织限制（0=否，1=是)
             */
            if(StrUtil.equals(couponGrp.getApplyisexclude(),"0")){
                tCoupon.setIsAllShop(1);//是否全部门店
            }else{
                tCoupon.setIsAllShop(0);//是否全部门店
            }

            tCoupon.setCoverUrl("");//优惠券封面
            if(StrUtil.isNotEmpty(couponGrp.getCouponremark())){
                tCoupon.setDescription(couponGrp.getCouponremark());//优惠券说明
            }else{
                tCoupon.setDescription("");//优惠券说明
            }
            tCoupon.setNote(couponGrp.getCouponremark());//优惠券说明
            tCoupon.setTimeType(1);//生效时间类型（1：固定时间/2：领取后生效
            tCoupon.setStartTime(couponGrp.getBegdate());//生效开始时间
            tCoupon.setEndTime(couponGrp.getEnddate());//生效结束时间
            tCoupon.setAfterReceiveDays(0);//领券后X天起生效
            tCoupon.setStatus(0);//优惠券状态（0：有效/1：失效）
            tCoupon.setCreateTime(couponGrp.getCreatedate());//创建时间
            tCoupon.setUpdateTime(couponGrp.getLastmodifieddate());//更新时间
            tCoupon.setSourceType(1);//优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
//            tCoupon.setCrmCouponId();//crm优惠券id
            couponMapper.save(tCoupon);
            if(tCoupon.getIsAllShop()==0){
                //插入关联的门店
                saveCouponShop(tCoupon);
            }
            if(tCoupon.getCommodityScopeType()==1){
                //插入指定商品
                savecouponSpu(tCoupon);
            }
        }

    }

    private void saveCouponShop(TCoupon tCoupon){
        List<CrmCouponGrpShops> crmCouponGrpShops = crmCouponGrpShopsMapper.listByCouponId(tCoupon.getCode());
        for (CrmCouponGrpShops crmCouponGrpShop : crmCouponGrpShops) {
            TCouponShop tCouponShop = new TCouponShop();
            tCouponShop.setCouponId(Long.parseLong(tCoupon.getCode()));

            StoreCodeVO storeCodeVO = getStoreByCode(crmCouponGrpShop.getShopcode());
            if(storeCodeVO==null){
                log.info("优惠券id：{},关联的店铺code：{},查找不到对应店铺，跳过当前记录。",tCoupon.getId(),crmCouponGrpShop.getShopcode());
                continue;
            }
            tCouponShop.setShopCode(storeCodeVO.getStoreCode());
            tCouponShop.setShopId(storeCodeVO.getStoreId());
            tCouponShopMapper.save(tCouponShop);
        }

    }


    private void savecouponSpu(TCoupon tCoupon){
        List<CrmCouponGrpSku> skus = crmCouponGrpSkuMapper.listByCouponId(tCoupon.getCode());
        for (CrmCouponGrpSku sku : skus) {
            TCouponCommodity couponCommodity = new TCouponCommodity();
            couponCommodity.setCouponId(tCoupon.getId());
            ServerResponseEntity<SkuVO> skuResponse = skuFeignClient.getBySkuCode(sku.getSku());
            if(!skuResponse.isSuccess() || skuResponse.getData()==null){
                log.info("当前优惠券:{},关联商品:{}不存在，不执行关联商品新增。",tCoupon.getId(),sku.getSku());
                continue;
            }
            SkuVO skuVO = skuResponse.getData();
            couponCommodity.setCommodityId(skuVO.getSpuId());
//            couponCommodity.setSpuCode();
            tCouponCommodityMapper.save(couponCommodity);
        }

    }

    private void couponUser(){
        long startTime = System.currentTimeMillis();
//        String[] tables ={"crm_coupon_list1"};
        String[] tables ={"crm_coupon_list1_add","crm_coupon_list2_add","crm_coupon_list3_add","crm_coupon_list4_add","crm_coupon_list5_add",
                "crm_coupon_list6_add","crm_coupon_list7_add","crm_coupon_list8_add"};
        for (String table : tables) {
            int currentPage = 1;
            int pageSize = 2000;
            PageHelper.startPage(currentPage, pageSize);
            List<CrmCouponList> coupons =  crmCouponListMapper.listByTableName(table);
            PageInfo<CrmCouponList> pageInfo = new PageInfo(coupons);
            saveCoupons(coupons,currentPage,table);


            int totalPage = pageInfo.getPages();
            int totalCount = (int)pageInfo.getTotal();
            for(int i = 2; i<=totalPage; i++){
                PageHelper.startPage(i,pageSize);
                List<CrmCouponList> newCoupons =  crmCouponListMapper.listByTableName(table);
                saveCoupons(newCoupons,i,table);
            }
        }

        log.info("优惠券领取明细同步执行结束，累计耗时:{}ms",System.currentTimeMillis() - startTime);

    }

    private void saveCoupons(List<CrmCouponList> coupons,int currentPage,String tableName){
        List<TCouponUser> couponUsers = new ArrayList<>();
        for (CrmCouponList coupon : coupons) {
            TCouponUser couponUser = new TCouponUser();
            couponUser.setCouponId(coupon.getCoupongrpid().longValue());//优惠券ID
            couponUser.setCouponCode(coupon.getCouponno());//券码
            couponUser.setCouponSourceType(1);//优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
            couponUser.setActivityId(0L);//活动ID
            couponUser.setActivitySource(5);//活动来源（1：推券/2：领券/3：买券/4：积分活动） *
//            couponUser.setBatchId();//某次领取批次号
                couponUser.setUserId(coupon.getVipid());//用户ID
//            couponUser.setUserName();//用户名称
//            couponUser.setUserPhone();//用户手机号
//            couponUser.setVipCode();//关联crm会员id
            couponUser.setReceiveTime(coupon.getVipbinddate());//领券时间
            couponUser.setUserStartTime(coupon.getBegdate());//开始时间
            couponUser.setUserEndTime(coupon.getEnddate());//结束时间

            // crm_coupon_list status 0可使用 2已锁定 4 已禁用 8已核销 10退券
            if(coupon.getStatus() == 0 || coupon.getStatus() == 10){
                couponUser.setStatus(1);//优惠券状态 0:冻结 1:有效 2:核销 *
            }else if (coupon.getStatus() == 8){
                couponUser.setStatus(2);//优惠券状态 0:冻结 1:有效 2:核销 *
                //根据优惠券 查询订单明细表  查看当前优惠券是在哪个订单上使用的。
                MallSalesOrderCoupon orderCoupon = mallSalesOrderCouponMapper.getByCouponId(coupon.getCouponno(),coupon.getVipid());
                if(orderCoupon!=null){
                    couponUser.setOrderNo(orderCoupon.getOrderid());
                }
//                mallSalesOrderDtlMapper.getByCouponId(coupon.getId());
//                couponUser.setOrderNo();//订单号
//                couponUser.setOrderAmount();//订单金额
//                couponUser.setCouponAmount();//优惠金额
            }else{
                continue;
            }
//            couponUser.setStaffId();//导购id


//            couponUser.setShopId();//领取门店id
//            couponUser.setShopName();//领取门店名称
            couponUsers.add(couponUser);
        }
        if(CollUtil.isNotEmpty(couponUsers)){
            tCouponUserMapper.batchSave(couponUsers);
        }
        log.info("表:{}.第{}页数据插入执行结束。插入优惠券领取明细条数:{}",tableName, currentPage, coupons.size());
    }


    private StoreCodeVO getStoreByCode(String code){
        if(storeMap.containsKey(code)){
            return storeMap.get(code);
        }
        EdBaseShop edBaseShop = edBaseShopMapper.getById(Long.parseLong(code));

        List<String> list = new ArrayList<>();
        list.add(edBaseShop.getCode());
        List<StoreCodeVO> stores = storeFeignClient.findByStoreCodes(list);
        if(stores!=null &&stores.size()>0){
            storeMap.put(code,stores.get(0));
            return stores.get(0);
        }else{
            storeMap.put(code,null);
            return null;
        }
    }


}
