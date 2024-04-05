package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.RegisterActivityDTO;
import com.mall4j.cloud.group.dto.RegisterActivityPageDTO;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.enums.ActivityTypeEnums;
import com.mall4j.cloud.group.model.RegisterActivity;
import com.mall4j.cloud.group.model.RegisterActivityShop;
import com.mall4j.cloud.group.model.UserRewardRecord;
import com.mall4j.cloud.group.service.RegisterActivityBizService;
import com.mall4j.cloud.group.service.RegisterActivityService;
import com.mall4j.cloud.group.service.RegisterActivityShopService;
import com.mall4j.cloud.group.service.UserRewardRecordService;
import com.mall4j.cloud.group.vo.RegisterActivityListVO;
import com.mall4j.cloud.group.vo.RegisterActivityVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterActivityBizServiceImpl implements RegisterActivityBizService {
    @Resource
    private RegisterActivityService registerActivityService;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private UserRewardRecordService userRewardRecordService;
    @Resource
    private RegisterActivityShopService registerActivityShopService;
    @Resource
    private CrmUserFeignClient crmUserFeignClient;
    @Override
    public ServerResponseEntity<Void> saveOrUpdateRegisterActivity(RegisterActivityDTO param) {
        RegisterActivity registerActivity = BeanUtil.copyProperties(param, RegisterActivity.class);
        registerActivityService.saveOrUpdate(registerActivity);

        String applyShopIds = param.getApplyShopIds();
        Integer id = registerActivity.getId();

        registerActivityShopService.remove(new LambdaQueryWrapper<RegisterActivityShop>().eq(RegisterActivityShop::getActivityId,id));
        if (StringUtils.isNotEmpty(applyShopIds)){
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<RegisterActivityShop> registerActivityShops = new ArrayList<>();
            shopIds.forEach(temp->{
                RegisterActivityShop registerActivityShop = RegisterActivityShop.builder()
                        .activityId(id)
                        .shopId(Long.valueOf(temp)).build();
                registerActivityShops.add(registerActivityShop);
            });
            registerActivityShopService.saveBatch(registerActivityShops);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<RegisterActivityVO> detail(Integer id) {
        RegisterActivity registerActivity = registerActivityService.getById(id);
        RegisterActivityVO result = BeanUtil.copyProperties(registerActivity, RegisterActivityVO.class);

        List<RegisterActivityShop> shops = registerActivityShopService.list(new LambdaQueryWrapper<RegisterActivityShop>().eq(RegisterActivityShop::getActivityId, id));
        result.setShops(shops);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageVO<RegisterActivityListVO>> page(RegisterActivityPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();

        Page<RegisterActivityListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> registerActivityService.registerActivityList(param));

        List<RegisterActivityListVO> list = page.getResult();

        List<RegisterActivityListVO> resultList = list.stream().peek(temp -> {
                    Date activityBeginTime = temp.getActivityBeginTime();
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
            String activityCouponIds = temp.getActivityCouponIds();
            if (StringUtils.isNotEmpty(activityCouponIds)){
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

        PageVO<RegisterActivityListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        registerActivityService.update(new LambdaUpdateWrapper<RegisterActivity>()
                .set(RegisterActivity::getStatus,1)
                .eq(RegisterActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        registerActivityService.update(new LambdaUpdateWrapper<RegisterActivity>()
                .set(RegisterActivity::getStatus,0)
                .eq(RegisterActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        registerActivityService.update(new LambdaUpdateWrapper<RegisterActivity>()
                .set(RegisterActivity::getDeleted,1)
                .eq(RegisterActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<RegisterActivityShop>> getActivityShop(Integer activityId) {
        List<RegisterActivityShop> list = registerActivityShopService.list(new LambdaQueryWrapper<RegisterActivityShop>().eq(RegisterActivityShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<RegisterActivityShop> registerActivityShops = new ArrayList<>();
        shopIds.forEach(temp->{
            RegisterActivityShop registerActivityShop = RegisterActivityShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            registerActivityShops.add(registerActivityShop);
        });

        registerActivityShopService.saveBatch(registerActivityShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId) {
        registerActivityShopService.remove(new LambdaQueryWrapper<RegisterActivityShop>()
                .eq(RegisterActivityShop::getActivityId,activityId)
                .eq(RegisterActivityShop::getShopId,shopId));

        List<RegisterActivityShop> list = registerActivityShopService.list(new LambdaQueryWrapper<RegisterActivityShop>().eq(RegisterActivityShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)){
            registerActivityService.update(null,new LambdaUpdateWrapper<RegisterActivity>()
                    .set(RegisterActivity::getIsAllShop,1)
                    .eq(RegisterActivity::getId,activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        registerActivityShopService.remove(new LambdaQueryWrapper<RegisterActivityShop>()
                .eq(RegisterActivityShop::getActivityId,activityId));
        registerActivityService.update(null,new LambdaUpdateWrapper<RegisterActivity>()
                .set(RegisterActivity::getIsAllShop,1)
                .eq(RegisterActivity::getId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public void userRegisterActivity(UserRegisterGiftDTO param) {
        Long userId = param.getUserId();
        Long shopId = param.getStoreId();
        log.info("userRegisterActivity param : {} ", JSONObject.toJSONString(param));
        RegisterActivity registerActivity = registerActivityService.selectFirstActivity(param);
        if(null != registerActivity){
            log.info("userRegisterActivity registerActivity param {}", JSONObject.toJSONString(registerActivity));
            Integer activityCouponSwitch = registerActivity.getActivityCouponSwitch();
            Integer activityPointSwitch = registerActivity.getActivityPointSwitch();
            Integer id = registerActivity.getId();

            UserRewardRecord userRewardRecord = UserRewardRecord.builder()
                    .activityId(id)
                    .activityType(ActivityTypeEnums.REGISTER_ACTIVITY.getCode())
                    .userId(userId)
                    .shopId(shopId).build();
            if (1 == activityPointSwitch){
                Integer activityPointNumber = registerActivity.getActivityPointNumber();
                UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                updateScoreDTO.setUserId(userId);
                updateScoreDTO.setPoint_value(activityPointNumber);
                updateScoreDTO.setSource("注册有礼");
                updateScoreDTO.setPoint_channel("wechat");
                updateScoreDTO.setPoint_type("SKX_JLJF");
                updateScoreDTO.setRemark("注册有礼赠送积分");
                updateScoreDTO.setIoType(1);
                //todo 待实现
//                crmUserFeignClient.updateScore(updateScoreDTO);
                log.info("userRegisterActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
                userRewardRecord.setRewardPointNum(activityPointNumber);
            }
            if (1 == activityCouponSwitch){
                // 赠送券
                String activityCouponIds = registerActivity.getActivityCouponIds();
                ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                receiveCouponDTO.setCouponId(Long.valueOf(activityCouponIds));
                receiveCouponDTO.setActivityId(Long.valueOf(id));
                receiveCouponDTO.setActivitySource(ActivitySourceEnum.REGISTER_ACTIVITY.value());
                receiveCouponDTO.setUserId(userId);
                log.info("userRegisterActivity giftCoupon param {}", JSONObject.toJSONString(receiveCouponDTO));
                tCouponFeignClient.receive(receiveCouponDTO);

                userRewardRecord.setRewardCouponIds(activityCouponIds);
            }

            userRewardRecordService.save(userRewardRecord);
        }
    }
}
