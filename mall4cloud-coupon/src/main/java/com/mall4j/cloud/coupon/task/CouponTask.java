package com.mall4j.cloud.coupon.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.crm.dto.CrmCouponOverdueQuery;
import com.mall4j.cloud.api.crm.dto.DOInstance50001388DTO;
import com.mall4j.cloud.api.crm.feign.CrmCouponClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.coupon.mapper.CouponMapper;
import com.mall4j.cloud.coupon.mapper.TCouponUserMapper;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.vo.MyCouponListVO;
import com.mall4j.cloud.coupon.vo.UserCouponVO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 */
@Component
public class CouponTask {


    public static final Logger log = LoggerFactory.getLogger(CouponTask.class);

    @Autowired
    private CouponUserService couponUserService;
    @Autowired
    private CouponService couponService;
    @Resource
    private CouponMapper couponMapper;
    @Autowired
    private CacheManagerUtil cacheManagerUtil;
    @Resource
    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Resource
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Resource
    private TCouponUserMapper tCouponUserMapper;
    @Resource
    private CrmCouponClient crmCouponClient;
    @Resource
    private UserFeignClient userFeignClient;


    /**
     * 删除用户失效180天以上的优惠券
     */
    @XxlJob("deleteCouponUser")
    public void deleteCouponUser() {
        log.info("删除用户失效180天以上的优惠券");
        // 删除用户失效半年以上的优惠券 参考京东
        couponUserService.deleteUnValidTimeCoupons(DateUtil.offsetDay(new Date(), -180));
    }

    /**
     * 改变用户优惠券的状态(设为失效状态)
     */
    @XxlJob("changeCouponUser")
    public void changeCouponUser() {
        log.info("设置用户的过期优惠券为失效状态");
        // 设置用户的过期优惠券为失效状态
        couponUserService.updateStatusByTime();
    }

    /**
     * 改变店铺优惠券的状态(设为过期状态)
     */
    @XxlJob("changeCoupon")
    public void changeCoupon() {
        log.info("设置店铺的过期优惠券为过期状态");
        log.info("改变店铺库存为0优惠券的状态(设为取消投放状态)");
        // 查询需要更新的店铺id列表
        List<Long> shopIds = couponMapper.listOverdueStatusShopIds();
        // 取消投放
        couponService.cancelPut();
        //清除缓存
        if (CollectionUtils.isNotEmpty(shopIds)) {
            for (Long shopId : shopIds) {
                cacheManagerUtil.evictCache(CacheNames.COUPON_LIST_BY_SHOP_KEY, String.valueOf(shopId));
            }
        }
    }

    /**
     * 投放优惠券
     */
    @XxlJob("putOnCoupon")
    public void putOnCoupon() {
        log.info("投放优惠券");
        // 投放优惠券
        couponService.putonCoupon();
    }

    /**
     * 小程序优惠券过期定时任务
     */
    @XxlJob("couponOverdue")
    public void couponOverdue() {
        XxlJobHelper.log("============= 优惠券过期提醒定时任务 =============");

        Date date = new Date();
        Date endTime = DateUtil.offset(date, DateField.HOUR, 24);

        //查询优惠券过期订阅模版
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.COUPON_EXPIRE.getValue(), null);
        log.info("优惠券过期订阅模版: {}", JSONObject.toJSONString(subscriptResp));
        if (subscriptResp.isSuccess()) {
            //获取订阅用户
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!org.springframework.util.CollectionUtils.isEmpty(userRecords)) {
                List<WeixinMaSubscriptTmessageSendVO> sendList = new ArrayList<>();

                userRecords.forEach(user -> {
                    //查询用户24小时内要过期的优惠券
                    List<UserCouponVO> userCouponVOS = tCouponUserMapper.couponOverdue(user.getUserId(), date, endTime);
                    if (CollectionUtils.isNotEmpty(userCouponVOS)) {
                        userCouponVOS.forEach(coupon -> {
                            /**
                             * 值替换
                             * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                             * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                             * 当前优惠券业务场景下 {couponName}、{getDate}、{expiresDate}、使用说明{useRemark}、{shopName}、{remark}
                             */
                            Map<String, String> paramMap = new HashMap();
                            paramMap.put("{couponName}", coupon.getName());
                            paramMap.put("{getDate}", DateUtils.dateToString(coupon.getReceiveTime()));
                            paramMap.put("{expiresDate}", DateUtils.dateToString(coupon.getEndTime()));
                            paramMap.put("{useRemark}", coupon.getDescription());
                            paramMap.put("{shopName}", "");
                            paramMap.put("{remark}", "您领取的优惠券即将到期，请尽快使用！");

                            List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                                WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                                msgData.setName(t.getTemplateValueName());
                                msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                                return msgData;
                            }).collect(Collectors.toList());

                            WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                            sendVO.setUserSubscriptRecordId(user.getId());
                            sendVO.setData(msgDataList);
                            sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                            sendList.add(sendVO);
                        });
                    }
                });

                log.info("优惠券过期提醒订阅消息发送值：{}",JSONObject.toJSONString(sendList));
                sendMaSubcriptMessageTemplate.syncSend(sendList);
            }
        } else {
            log.error("查询优惠券过期模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
        }
    }

    //    public static void main(String[] args) {
//        Date date = new Date();
//
//        System.out.println(DateUtil.beginOfDay(DateUtil.offsetDay(date, 7)));
//        System.out.println(DateUtil.endOfDay(DateUtil.offsetDay(date, 7)));
//
//
//        System.out.println(DateUtil.offset(DateUtil.beginOfDay(DateUtil.offsetDay(date, 7)), DateField.HOUR, -8));
//        System.out.println(DateUtil.offset(DateUtil.endOfDay(DateUtil.offsetDay(date, 7)), DateField.HOUR, -8));
//    }
    /**
     * 小程序优惠券过期定时任务
     */
    @XxlJob("crmCouponOverdue")
    public void crmCouponOverdue() {
        XxlJobHelper.log("============= 优惠券过期提醒定时任务 =============");

        /**
         * 提前七天优惠券到期提醒。
         * 比如今天为2022-09-15。提前七天
         * 开始时间为：2022-09-22 00:00:00
         * 结束时间为：2022-09-22 23:59:59
         * 时间内的优惠券都需要查询出来发送即将到期提醒。
         *
         * crm库数据时间相对于我们数据库晚8个小时减去8个小时。
         * 开始时间为：2022-09-21 16:00:00
         * 结束时间为：2022-09-22 15:59:59
         *
         */
        Date date = new Date();
        DateTime beginTime = DateUtil.offset(DateUtil.beginOfDay(DateUtil.offsetDay(date, 7)), DateField.HOUR, -8);
        DateTime endTime = DateUtil.offset(DateUtil.endOfDay(DateUtil.offsetDay(date, 7)), DateField.HOUR, -8);


        //查询优惠券过期订阅模版
        ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.COUPON_EXPIRE.getValue(), null);
        log.info("优惠券过期订阅模版: {}", JSONObject.toJSONString(subscriptResp));
        if (subscriptResp.isSuccess()) {
            //获取订阅用户
            WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
            List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
            if (!org.springframework.util.CollectionUtils.isEmpty(userRecords)) {
                List<WeixinMaSubscriptTmessageSendVO> sendList = new ArrayList<>();

                userRecords.forEach(user -> {
                    ServerResponseEntity<UserApiVO> userById = userFeignClient.getUserById(user.getUserId());
                    if (userById != null && userById.isSuccess() && userById.getData() != null) {
                        //查询用户24小时内要过期的优惠券
                        CrmCouponOverdueQuery crmCouponOverdueQuery = new CrmCouponOverdueQuery();
                        crmCouponOverdueQuery.setBeginTime(DateUtil.formatDateTime(beginTime));
                        crmCouponOverdueQuery.setEndTime(DateUtil.formatDateTime(endTime));
                        crmCouponOverdueQuery.setVipCode(userById.getData().getVipcode());
                        log.info("查询参数:{}",JSONObject.toJSONString(crmCouponOverdueQuery));
                        ServerResponseEntity<List<DOInstance50001388DTO>> listServerResponseEntity = crmCouponClient.crmCouponOverdue(crmCouponOverdueQuery);
                        if (listServerResponseEntity != null && listServerResponseEntity.isSuccess()) {
                            if (CollectionUtils.isNotEmpty(listServerResponseEntity.getData())) {
                                log.info("过期优惠券参数量:{}",listServerResponseEntity.getData().size());
                                listServerResponseEntity.getData().forEach(coupon -> {
                                    /**
                                     * 值替换
                                     * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                                     * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                                     * 当前优惠券业务场景下 {couponName}、{getDate}、{expiresDate}、使用说明{useRemark}、{shopName}、{remark}
                                     */
                                    Map<String, String> paramMap = new HashMap();
                                    paramMap.put("{couponName}", coupon.getProjectname());
                                    paramMap.put("{getDate}", DateUtils.dateToString(coupon.getEffecttime()));
                                    paramMap.put("{expiresDate}", DateUtils.dateToString(coupon.getExpectedexpiredtime()));
                                    paramMap.put("{useRemark}", coupon.getGrantreason());
                                    paramMap.put("{shopName}", "");
                                    paramMap.put("{remark}", "您领取的优惠券即将到期，请尽快使用！");

                                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                                        msgData.setName(t.getTemplateValueName());
                                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                                        return msgData;
                                    }).collect(Collectors.toList());

                                    WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                                    sendVO.setUserSubscriptRecordId(user.getId());
                                    sendVO.setData(msgDataList);
                                    sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                                    sendList.add(sendVO);
                                });
                            }
                        }
                    }
                });

                log.info("优惠券过期提醒订阅消息发送值：{}",JSONObject.toJSONString(sendList));
                sendMaSubcriptMessageTemplate.syncSend(sendList);
            }
        } else {
            log.error("查询优惠券过期模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
        }
    }

    /**
     * 小程序优惠券过期定时任务
     */
    @XxlJob("newCrmCouponOverdue")
    public void newCrmCouponOverdue() {
        XxlJobHelper.log("============= 优惠券过期提醒定时任务 =============");

        /**
         * 提前七天优惠券到期提醒。
         * 比如今天为2022-09-15。提前七天
         * 开始时间为：2022-09-22 00:00:00
         * 结束时间为：2022-09-22 23:59:59
         * 时间内的优惠券都需要查询出来发送即将到期提醒。
         *
         * crm库数据时间相对于我们数据库晚8个小时减去8个小时。
         * 开始时间为：2022-09-21 16:00:00
         * 结束时间为：2022-09-22 15:59:59
         *
         */
        Date date = new Date();
        DateTime beginTime = DateUtil.offset(DateUtil.beginOfDay(DateUtil.offsetDay(date, 7)), DateField.HOUR, -8);
        DateTime endTime = DateUtil.offset(DateUtil.endOfDay(DateUtil.offsetDay(date, 7)), DateField.HOUR, -8);

        //查询用户24小时内要过期的优惠券
        CrmCouponOverdueQuery crmCouponOverdueQuery = new CrmCouponOverdueQuery();
        crmCouponOverdueQuery.setBeginTime(DateUtil.formatDateTime(beginTime));
        crmCouponOverdueQuery.setEndTime(DateUtil.formatDateTime(endTime));
        log.info("查询参数:{}",JSONObject.toJSONString(crmCouponOverdueQuery));
        ServerResponseEntity<List<DOInstance50001388DTO>> listServerResponseEntity = crmCouponClient.crmCouponOverdue(crmCouponOverdueQuery);
        if (listServerResponseEntity == null || listServerResponseEntity.isFail() || CollectionUtil.isEmpty(listServerResponseEntity.getData())) {
            return;
        }
        log.info("过期优惠券参数量:{}",listServerResponseEntity.getData().size());
        listServerResponseEntity.getData().forEach(coupon -> {
            log.info("优惠券数据:{}",JSONObject.toJSONString(coupon));
            ServerResponseEntity<UserApiVO> userById = userFeignClient.getUserByVipCode(coupon.getCustomerid());
            if (userById != null && userById.isSuccess() && userById.getData() != null) {
                //查询优惠券过期订阅模版
                List<String> bussinessIds = new ArrayList<>();
                bussinessIds.add(userById.getData().getUserId().toString());
                ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.COUPON_EXPIRE.getValue(), bussinessIds);
                log.info("优惠券过期订阅模版: {}", JSONObject.toJSONString(subscriptResp));
                if (subscriptResp.isSuccess()) {
                    //获取订阅用户
                    WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                    List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
                    if (!org.springframework.util.CollectionUtils.isEmpty(userRecords)) {
                        List<WeixinMaSubscriptTmessageSendVO> sendList = new ArrayList<>();
                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前优惠券业务场景下 {couponName}、{getDate}、{expiresDate}、使用说明{useRemark}、{shopName}、{remark}
                         */
                        Map<String, String> paramMap = new HashMap();
                        paramMap.put("{couponName}", coupon.getProjectname());

                        /**
                         * crm存的时间早8小时，这里取出来+8小时
                         */
                        String getDate = DateUtils.dateToString(DateUtil.offset(coupon.getEffecttime(), DateField.HOUR, 8));
                        String expiresDate = DateUtils.dateToString(DateUtil.offset(coupon.getExpectedexpiredtime(), DateField.HOUR, 8));

                        paramMap.put("{getDate}", getDate);
                        paramMap.put("{expiresDate}", expiresDate);
                        paramMap.put("{useRemark}", coupon.getGrantreason());
                        paramMap.put("{shopName}", "");
                        paramMap.put("{remark}", "您领取的优惠券即将到期，请尽快使用！");

                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());

                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(userRecords.get(0).getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                        sendList.add(sendVO);
                        log.info("优惠券过期提醒订阅消息发送值：{}",JSONObject.toJSONString(sendList));
                        wxMaApiFeignClient.updateUserRecordId(userRecords.get(0).getId());
                        sendMaSubcriptMessageTemplate.syncSend(sendList);
                    }
                }else {
                    log.info("未获取到订阅消息模板信息");
                }
            } else {
                log.info("未获取到用户信息");
            }
        });
    }

    /**
     * 小程序优惠券过期定时任务
     */
    @XxlJob("newCouponOverdue")
    public void newCouponOverdue() {
        XxlJobHelper.log("============= 优惠券过期提醒定时任务 =============");

        Date date = new Date();
        Date endTime = DateUtil.offset(date, DateField.HOUR, 24);


        int pageNum = 1;
        int pageSize = 5000;
        //查询用户24小时内要过期的优惠券
        //小程序用户券记录
        PageInfo<UserCouponVO> result = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->
                tCouponUserMapper.couponOverdue(null, date, endTime)
        );
        
        List<UserCouponVO> userCouponVOS = result.getList();
        if (CollectionUtils.isNotEmpty(userCouponVOS)) {
            doCouponOverdueRemind(userCouponVOS);
        }

        while (result.isHasNextPage()){
            pageNum++;
            result = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->
                    tCouponUserMapper.couponOverdue(null, date, endTime)
            );
            userCouponVOS = result.getList();
            if (CollectionUtils.isNotEmpty(userCouponVOS)) {
                doCouponOverdueRemind(userCouponVOS);
            }
        }
    }

    private void doCouponOverdueRemind(List<UserCouponVO> userCouponVOS){
        if (CollectionUtils.isNotEmpty(userCouponVOS)) {
            userCouponVOS.forEach(coupon -> {
                //查询优惠券过期订阅模版
                List<String> bussinessIds = new ArrayList<>();
                bussinessIds.add(coupon.getUserId().toString());
                ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.COUPON_EXPIRE.getValue(), bussinessIds);
                log.info("优惠券过期订阅模版: {}", JSONObject.toJSONString(subscriptResp));
                if (subscriptResp.isSuccess()) {
                    //获取订阅用户
                    WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                    List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
                    if (!org.springframework.util.CollectionUtils.isEmpty(userRecords)) {
                        List<WeixinMaSubscriptTmessageSendVO> sendList = new ArrayList<>();
                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前优惠券业务场景下 {couponName}、{getDate}、{expiresDate}、使用说明{useRemark}、{shopName}、{remark}
                         */
                        Map<String, String> paramMap = new HashMap();
                        paramMap.put("{couponName}", coupon.getName());
                        paramMap.put("{getDate}", DateUtils.dateToString(coupon.getReceiveTime()));
                        paramMap.put("{expiresDate}", DateUtils.dateToString(coupon.getEndTime()));
                        paramMap.put("{useRemark}", coupon.getDescription());
                        paramMap.put("{shopName}", "");
                        paramMap.put("{remark}", "您领取的优惠券即将到期，请尽快使用！");

                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue())) ? t.getTemplateValue() : paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());

                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(userRecords.get(0).getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(weixinMaSubscriptTmessageVO.getPage());
                        sendList.add(sendVO);
                        log.info("优惠券过期提醒订阅消息发送值：{}",JSONObject.toJSONString(sendList));
                        wxMaApiFeignClient.updateUserRecordId(userRecords.get(0).getId());
                        sendMaSubcriptMessageTemplate.syncSend(sendList);
                    }
                } else {
                    log.error("查询优惠券过期模版失败，返回值为：{}", JSONObject.toJSONString(subscriptResp));
                }
            });
        }

    }

}
