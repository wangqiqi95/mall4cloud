package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.biz.feign.WxMpApiFeignClient;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.group.feign.dto.UserFollowWechatActivityDTO;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.FollowWechatDTO;
import com.mall4j.cloud.group.dto.FollowWechatPageDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.enums.ActivityTypeEnums;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.FollowWechatActivityShopService;
import com.mall4j.cloud.group.service.FollowWechatBizService;
import com.mall4j.cloud.group.service.FollowWechatService;
import com.mall4j.cloud.group.service.UserRewardRecordService;
import com.mall4j.cloud.group.vo.FollowWechatListVO;
import com.mall4j.cloud.group.vo.FollowWechatVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class FollowWechatBizServiceImpl implements FollowWechatBizService {
    @Resource
    private FollowWechatService followWechatService;
    @Resource
    private FollowWechatActivityShopService followWechatActivityShopService;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private UserRewardRecordService userRewardRecordService;
    @Resource
    private CrmUserFeignClient crmUserFeignClient;

    @Override
    public ServerResponseEntity<Void> saveOrUpdateRegisterActivity(FollowWechatDTO param) {
        FollowWechatActivity followWechatActivity = BeanUtil.copyProperties(param, FollowWechatActivity.class);
        followWechatService.saveOrUpdate(followWechatActivity);

        String applyShopIds = param.getApplyShopIds();
        Integer followWechatActivityId = followWechatActivity.getId();

        followWechatActivityShopService.remove(new LambdaQueryWrapper<FollowWechatActivityShop>().eq(FollowWechatActivityShop::getActivityId, followWechatActivityId));
        if (StringUtils.isNotEmpty(applyShopIds)) {
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<FollowWechatActivityShop> followWechatActivityShops = new ArrayList<>();
            shopIds.forEach(temp -> {
                FollowWechatActivityShop followWechatActivityShop = FollowWechatActivityShop.builder()
                        .activityId(followWechatActivityId)
                        .shopId(Long.valueOf(temp)).build();
                followWechatActivityShops.add(followWechatActivityShop);
            });
            followWechatActivityShopService.saveBatch(followWechatActivityShops);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<FollowWechatVO> detail(Integer id) {
        FollowWechatActivity followWechatActivity = followWechatService.getById(id);
        FollowWechatVO result = BeanUtil.copyProperties(followWechatActivity, FollowWechatVO.class);

        List<FollowWechatActivityShop> list = followWechatActivityShopService.list(new LambdaQueryWrapper<FollowWechatActivityShop>().eq(FollowWechatActivityShop::getActivityId, id));
        result.setShops(list);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageVO<FollowWechatListVO>> page(FollowWechatPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<FollowWechatListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> followWechatService.followWechatList(param));

        List<FollowWechatListVO> list = page.getResult();


        List<FollowWechatListVO> resultList = list.stream().peek(temp -> {
                    Date activityBeginTime = temp.getActivityBeginTime();
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    String activityCouponIds = temp.getActivityCouponIds();
                    if (StringUtils.isNotEmpty(activityCouponIds)) {
                        String[] ids = activityCouponIds.split(StringPool.COMMA);
                        temp.setActivityCouponNum(ids.length);
                    }

                    if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                        if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                            temp.setStatus(ActivityStatusEnums.IN_PROGRESS.getCode());
                        } else if (date.compareTo(activityBeginTime) < 0) {
                            temp.setStatus(ActivityStatusEnums.NOT_START.getCode());
                        } else if (date.compareTo(activityEndTime) > 0) {
                            temp.setStatus(ActivityStatusEnums.END.getCode());
                        }
                    }
                }
        ).collect(Collectors.toList());

        PageVO<FollowWechatListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        followWechatService.update(new LambdaUpdateWrapper<FollowWechatActivity>()
                .set(FollowWechatActivity::getStatus, 1)
                .eq(FollowWechatActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        followWechatService.update(new LambdaUpdateWrapper<FollowWechatActivity>()
                .set(FollowWechatActivity::getStatus, 0)
                .eq(FollowWechatActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        followWechatService.update(new LambdaUpdateWrapper<FollowWechatActivity>()
                .set(FollowWechatActivity::getDeleted, 1)
                .eq(FollowWechatActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<FollowWechatActivityShop>> getActivityShop(Integer activityId) {
        List<FollowWechatActivityShop> list = followWechatActivityShopService.list(new LambdaQueryWrapper<FollowWechatActivityShop>().eq(FollowWechatActivityShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<FollowWechatActivityShop> followWechatActivityShops = new ArrayList<>();
        shopIds.forEach(temp -> {
            FollowWechatActivityShop followWechatActivityShop = FollowWechatActivityShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            followWechatActivityShops.add(followWechatActivityShop);
        });

        followWechatActivityShopService.saveBatch(followWechatActivityShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId) {
        followWechatActivityShopService.remove(new LambdaQueryWrapper<FollowWechatActivityShop>()
                .eq(FollowWechatActivityShop::getActivityId, activityId)
                .eq(FollowWechatActivityShop::getShopId, shopId));

        List<FollowWechatActivityShop> list = followWechatActivityShopService.list(new LambdaQueryWrapper<FollowWechatActivityShop>().eq(FollowWechatActivityShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)) {
            followWechatService.update(null, new LambdaUpdateWrapper<FollowWechatActivity>()
                    .set(FollowWechatActivity::getIsAllShop, 1)
                    .eq(FollowWechatActivity::getId, activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        followWechatActivityShopService.remove(new LambdaQueryWrapper<FollowWechatActivityShop>()
                .eq(FollowWechatActivityShop::getActivityId, activityId));
        followWechatService.update(null, new LambdaUpdateWrapper<FollowWechatActivity>()
                .set(FollowWechatActivity::getIsAllShop, 1)
                .eq(FollowWechatActivity::getId, activityId));
        return ServerResponseEntity.success();
    }

    private UserFeignClient userFeignClient;

    @Autowired
    public void setUserFeignClient(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    private WxMpApiFeignClient wxMpApiFeignClient;

    @Autowired
    public void setWxMpApiFeignClient(WxMpApiFeignClient wxMpApiFeignClient) {
        this.wxMpApiFeignClient = wxMpApiFeignClient;
    }

    @Override
    public void userFollowWechatActivity(UserFollowWechatActivityDTO params) {
        Long userId = params.getUserId();
        Long shopId = params.getShopId();
        Long wechatId = params.getWechatId();
        String unionId = params.getUnionId();
        String appId = params.getAppId();


        log.info("Starting processing follow account activities..... {}",JSONObject.toJSON(params));

        FollowWechatActivity followWechatActivity = followWechatService.selectFirstActivity(params);

        if (null != followWechatActivity) {

            log.info("followWechatActivity---》 {}",JSONObject.toJSON(followWechatActivity));

            // 获取参与活动的本地数据库AppId
            String wechatIds = followWechatActivity.getWechatIds();
            List<Long> wechatIdsList = Arrays.asList(wechatIds.split(StringPool.COMMA)).stream().map(Long::parseLong).collect(Collectors.toList());
            // 判断当前用户之前是否参与过活动
            QueryWrapper<UserRewardRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("activity_id", followWechatActivity.getId()).eq("activity_type", 3).eq("user_id", params.getUserId());
            int count = userRewardRecordService.count(queryWrapper);
            if (count > 0) {
                log.error("用户(:{})之前参与活动(:{})，不能参与此处活动。", unionId, appId);
                return;
            }


            // 将WechatIds转化为AppIds
            // Feign拉取微信官方AppId
            ServerResponseEntity<List<String>> wechatVersionAppIdResponse = wxMpApiFeignClient.listWechatAppIdById(wechatIdsList);
            boolean success = wechatVersionAppIdResponse.isSuccess();
            if (!success) {
                log.error("在远程拉取微信AppId数据时出现异常!");
                return;
            }
            List<String> wechatVersionAppIdList = wechatVersionAppIdResponse.getData();

            Integer receiveCondition = followWechatActivity.getReceiveCondition();
            if (1 == receiveCondition) {
                log.debug("必须关注所有的公众号,当前选择的活动标识:{}", followWechatActivity.getId());
                // 根据用户id查询用户关注的所有公众号 @Author EricJeppesen
                // 拉取用户关注的AppId
                ServerResponseEntity<List<String>> listServerResponseEntity = userFeignClient.listFollowUpAppIdByUnionId(unionId);
                success = listServerResponseEntity.isSuccess();
                if (success) {
                    // 用户关注的AppId
                    List<String> customerFollowUpWechatAppId = listServerResponseEntity.getData();

                    // 查看用户是否有关注所有公众号
                    for (int i = 0; i < wechatVersionAppIdList.size(); i++) {
                        // 需要匹配的AppId
                        String requirementAppId = wechatVersionAppIdList.get(i);
                        boolean isCurrentWechatAppIdFollowUp = false;
                        for (int j = 0; j < customerFollowUpWechatAppId.size(); j++) {
                            // 用户关注的AppId
                            String customerFollowUpWechatAppIdItem = customerFollowUpWechatAppId.get(j);
                            if (Objects.equals(requirementAppId, customerFollowUpWechatAppIdItem)) {
                                isCurrentWechatAppIdFollowUp = true;
                                break;
                            }
                        }
                        // 只要有一个微信公众号没有关注，就不给予奖励活动，直接返回
                        if (!isCurrentWechatAppIdFollowUp) {
                            log.debug("当前用户UnionId:{},当前选择的活动标识:{},没关注的AppId:{}", unionId, followWechatActivity.getId(), requirementAppId);
                            return;
                        }
                    }
                }

            }
            if (2 == receiveCondition) {
                log.debug("只需要关注其中的一个公众号,当前选择的活动标识:{}", followWechatActivity.getId());
                // 只要关注其中一个公众号就 发起当前活动
                if (!wechatVersionAppIdList.contains(appId)) {
                    //如果关注的公众号不在活动内则直接返回
                    return;
                }
            }

            Integer activityCouponSwitch = followWechatActivity.getActivityCouponSwitch();
            Integer activityPointSwitch = followWechatActivity.getActivityPointSwitch();

            Integer id = followWechatActivity.getId();

            UserRewardRecord userRewardRecord = UserRewardRecord.builder()
                    .activityId(id)
                    .activityType(ActivityTypeEnums.FOLLOW_WECHAT_ACTIVITY.getCode())
                    .userId(userId)
                    .shopId(shopId).build();
            if (1 == activityPointSwitch) {
                // 赠送积分
                Integer activityPointNumber = followWechatActivity.getActivityPointNumber();
                userRewardRecord.setRewardPointNum(activityPointNumber);

                UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                updateScoreDTO.setUserId(userId);
                updateScoreDTO.setPoint_value(activityPointNumber);
                updateScoreDTO.setSource("关注公众号赠送积分");
                updateScoreDTO.setPoint_channel("wechat");
                updateScoreDTO.setPoint_type("SKX_JLJF");
                updateScoreDTO.setRemark("关注公众号赠送积分");
                updateScoreDTO.setIoType(1);
                //todo 待实现
//                crmUserFeignClient.updateScore(updateScoreDTO);
                log.info("followWechatActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
            }
            if (1 == activityCouponSwitch) {
                //赠送券
                // Fix by EricJeppesen
                // 可能存在多张优惠券的发放，当触及到多张优惠券的发放时，可能会导致之前TCouponFeignClient@recive发送不出去的问题
                if(StrUtil.isNotBlank(followWechatActivity.getActivityCouponIds())){
                    String activityCouponIds = followWechatActivity.getActivityCouponIds();
                    List<Long> couponIds = Arrays.asList(activityCouponIds.split(StringPool.COMMA)).stream().map(Long::parseLong).collect(Collectors.toList());
                    BatchReceiveCouponDTO batchReceiveCouponDTO = new BatchReceiveCouponDTO();
                    batchReceiveCouponDTO.setCouponIds(couponIds);
                    batchReceiveCouponDTO.setActivityId(Long.valueOf(id));
                    batchReceiveCouponDTO.setActivitySource(ActivitySourceEnum.PERFECT_DATA.value());
                    batchReceiveCouponDTO.setUserId(userId);
                    tCouponFeignClient.batchReceive(batchReceiveCouponDTO);

                    userRewardRecord.setRewardCouponIds(activityCouponIds);
                }

            }
            userRewardRecordService.save(userRewardRecord);
        }

    }
}
