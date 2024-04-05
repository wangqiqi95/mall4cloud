package com.mall4j.cloud.group.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.group.model.GroupActivity;
import com.mall4j.cloud.group.model.LotteryDrawActivity;
import com.mall4j.cloud.group.model.LotteryDrawActivityPrize;
import com.mall4j.cloud.group.model.SignActivity;
import com.mall4j.cloud.group.service.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 见 https://github.com/xuxueli/xxl-job
 * 开发步骤：
 *      1、任务开发：在Spring Bean实例中，开发Job方法；
 *      2、注解配置：为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 *      3、执行日志：需要通过 "XxlJobHelper.log" 打印执行日志；
 *      4、任务结果：默认任务结果为 "成功" 状态，不需要主动设置；如有诉求，比如设置任务结果为失败，可以通过 "XxlJobHelper.handleFail/handleSuccess" 自主设置任务结果；
 *
 * @author FrozenWatermelon
 * @date 2021/04/23
 */
@Component
public class GroupTask {

    @Autowired
    private GroupActivityService groupActivityService;

    @Resource
    private SignActivityService signActivityService;
    @Resource
    private UserSignNoticeService userSignNoticeService;
    @Resource
    private LotteryDrawActivityService lotteryDrawActivityService;
    @Resource
    private LotteryDrawActivityPrizeService lotteryDrawActivityPrizeService;
    @Resource
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    private OnsMQTemplate sendMaTemplateMessage;

    /**
     * 结束活动，改变商品类型
     */
    @XxlJob("activityFinishAndProdChange")
    public void activityFinishAndProdChange() {
        XxlJobHelper.log("拼团活动结束，团购商品恢复为普通商品。。。");
        // 0:未启用、1:启用、2:违规下架、3:等待审核
        // 获取活动结束或团单结束未成团的拼团团队
        List<GroupActivity> groupActivityList = groupActivityService.listUnEndButNeedEndActivity();

        if (CollUtil.isEmpty(groupActivityList)){
            return;
        }
        groupActivityService.changeProdTypeByGroupActivityIdList(groupActivityList);
    }

    /**
     * 用户签到活动提醒
     */
    @XxlJob("userSignNotice")
    public void userSignNotice(){
        XxlJobHelper.log("开始执行签到提醒定时任务");
        Date date = new Date();
        SignActivity signActivity = signActivityService.getOne(new LambdaQueryWrapper<SignActivity>()
                .and(wrapper1->wrapper1.le(SignActivity::getActivityBeginTime, date)
                        .gt(SignActivity::getActivityEndTime, date).or(wrapper2->wrapper2.le(SignActivity::getActivityBeginTime, date).eq(SignActivity::getActivityType,0))).eq(SignActivity::getDeleted,0)
                .eq(SignActivity::getActivityStatus, 1));

        if (null != signActivity){
            Integer id = signActivity.getId();
            String noticeTime = signActivity.getNoticeTime();

            String curDateStr = DateUtil.format(date, "HH:mm");
            if (curDateStr.equals(noticeTime)){
                XxlJobHelper.log("命中签到提醒，正在执行中.......");
                ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptTmessage = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.SIGIN_IN_DAY.getValue(), Collections.singletonList(String.valueOf(id)));
                // 调用发送模板消息接口
                WeixinMaSubscriptTmessageVO data = subscriptTmessage.getData();
                XxlJobHelper.log("查询到的模板消息{}",data);
                if (null != data){
                    String page = data.getPage();
                    List<WeixinMaSubscriptUserRecordVO> userRecords = data.getUserRecords();

                    WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptTmessage.getData();

                    /**
                     * 值替换
                     * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                     * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                     * 当前签到 场景下 没有值
                     * 构建参数map.
                     */
                    Map<String,String> paramMap = new HashMap();

                    //构建msgdata参数
                    XxlJobHelper.log("需要替换的模版的参数信息:{}",JSONObject.toJSONString(weixinMaSubscriptTmessageVO.getTmessageValues()));
                    List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                        WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                        msgData.setName(t.getTemplateValueName());
                        msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                        return msgData;
                    }).collect(Collectors.toList());
                    XxlJobHelper.log("替换完成的参数对象:{}",JSONObject.toJSONString(msgDataList));

                    List<WeixinMaSubscriptTmessageSendVO> sendParams = userRecords.stream().map(temp->{
                        WeixinMaSubscriptTmessageSendVO weixinMaSubscriptTmessageSendVO = new WeixinMaSubscriptTmessageSendVO();
                        weixinMaSubscriptTmessageSendVO.setPage(page);
                        weixinMaSubscriptTmessageSendVO.setData(msgDataList);
                        weixinMaSubscriptTmessageSendVO.setUserSubscriptRecordId(temp.getId());
                        return weixinMaSubscriptTmessageSendVO;
                    }).collect(Collectors.toList());
                    XxlJobHelper.log("发送模板参数为{}", JSONObject.toJSONString(sendParams));
                    sendMaTemplateMessage.syncSend(sendParams);
                }

            }
        }
        XxlJobHelper.log("签到提醒定时任务执行完毕");
    }

    @XxlJob("drawPrizeStockInit")
    public void drawPrizeStockInit(){
        XxlJobHelper.log("开始执行初始化抽奖库存定时任务");
        Date date = new Date();
        DateTime endOfDay = DateUtil.endOfDay(date);
        long redisTime = DateUtil.between(date, endOfDay, DateUnit.SECOND);
        List<LotteryDrawActivity> lotteryDrawActivities = lotteryDrawActivityService.list(new LambdaQueryWrapper<LotteryDrawActivity>()
                .eq(LotteryDrawActivity::getPrizeGrantTotal, 1)
                .eq(LotteryDrawActivity::getStatus, 1)
                .le(LotteryDrawActivity::getActivityBeginTime, date)
                .gt(LotteryDrawActivity::getActivityEndTime, date)
                .eq(LotteryDrawActivity::getDeleted, 0));
        if (CollectionUtil.isNotEmpty(lotteryDrawActivities)){
            lotteryDrawActivities.forEach(temp->{
                Integer id = temp.getId();
                List<LotteryDrawActivityPrize> activityPrizes = lotteryDrawActivityPrizeService.list(new LambdaQueryWrapper<LotteryDrawActivityPrize>().eq(LotteryDrawActivityPrize::getLotteryDrawId, id));
                activityPrizes.forEach(prize->{
                    Integer prizeId = prize.getId();
                    Integer prizeInitStock = prize.getPrizeInitStock();
                    String redisKey = "lottery::prize::stock::"+prizeId;
                    RedisUtil.set(redisKey,prizeInitStock,redisTime);
                    lotteryDrawActivityPrizeService.update(new LambdaUpdateWrapper<LotteryDrawActivityPrize>()
                            .set(LotteryDrawActivityPrize::getPrizeStock,prizeInitStock)
                            .eq(LotteryDrawActivityPrize::getId,prizeId));
                });
            });
        }
        XxlJobHelper.log("初始化抽奖库存定时任务执行完毕");
    }
}
