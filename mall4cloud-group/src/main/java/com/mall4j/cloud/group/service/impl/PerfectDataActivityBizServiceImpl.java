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
import com.mall4j.cloud.api.coupon.vo.CouponDetailVO;
import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityPageDTO;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.enums.ActivityTypeEnums;
import com.mall4j.cloud.group.mapper.PerfectDataUserRecordMapper;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.PerfectDataActivityBizService;
import com.mall4j.cloud.group.service.PerfectDataActivityService;
import com.mall4j.cloud.group.service.PerfectDataActivityShopService;
import com.mall4j.cloud.group.service.UserRewardRecordService;
import com.mall4j.cloud.group.vo.PerfectDataActivityListVO;
import com.mall4j.cloud.group.vo.PerfectDataActivityVO;
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
public class PerfectDataActivityBizServiceImpl implements PerfectDataActivityBizService {
    @Resource
    private PerfectDataActivityService perfectDataActivityService;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private UserRewardRecordService userRewardRecordService;
    @Resource
    private PerfectDataActivityShopService perfectDataActivityShopService;
    @Resource
    private CrmUserFeignClient crmUserFeignClient;
    @Resource
    private PerfectDataUserRecordMapper perfectDataUserRecordMapper;

    @Override
    public ServerResponseEntity<Void> saveOrUpdatePerfectDataActivity(PerfectDataActivityDTO param) {
        PerfectDataActivity perfectDataActivity = BeanUtil.copyProperties(param, PerfectDataActivity.class);
        perfectDataActivityService.saveOrUpdate(perfectDataActivity);

        String applyShopIds = param.getApplyShopIds();
        Integer id = perfectDataActivity.getId();

        perfectDataActivityShopService.remove(new LambdaQueryWrapper<PerfectDataActivityShop>().eq(PerfectDataActivityShop::getActivityId,id));
        if (StringUtils.isNotEmpty(applyShopIds)){
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<PerfectDataActivityShop> perfectDataActivityShops = new ArrayList<>();
            shopIds.forEach(temp->{
                PerfectDataActivityShop perfectDataActivityShop = PerfectDataActivityShop.builder()
                        .activityId(id)
                        .shopId(Long.valueOf(temp)).build();
                perfectDataActivityShops.add(perfectDataActivityShop);
            });
            perfectDataActivityShopService.saveBatch(perfectDataActivityShops);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PerfectDataActivityVO> detail(Integer id) {
        PerfectDataActivity registerActivity = perfectDataActivityService.getById(id);
        PerfectDataActivityVO result = BeanUtil.copyProperties(registerActivity, PerfectDataActivityVO.class);

        List<PerfectDataActivityShop> shops = perfectDataActivityShopService.list(new LambdaQueryWrapper<PerfectDataActivityShop>().eq(PerfectDataActivityShop::getActivityId, id));
        result.setShops(shops);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageVO<PerfectDataActivityListVO>> page(PerfectDataActivityPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();

        Page<PerfectDataActivityListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> perfectDataActivityService.perfectDataActivityList(param));

        List<PerfectDataActivityListVO> list = page.getResult();


        List<PerfectDataActivityListVO> resultList = list.stream().peek(temp -> {
                    Date activityBeginTime = temp.getActivityBeginTime();
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    Integer activityCouponSwitch = temp.getActivityCouponSwitch();
                    // 添加优惠券名称
                    if (1 == activityCouponSwitch) {
                        String activityCouponIds = temp.getActivityCouponIds();
                        ServerResponseEntity<CouponDetailVO> couponDetail = tCouponFeignClient.getCouponDetail(Long.valueOf(activityCouponIds));
                        CouponDetailVO data = couponDetail.getData();
                        temp.setCouponName(data.getName());
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

        PageVO<PerfectDataActivityListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        perfectDataActivityService.update(new LambdaUpdateWrapper<PerfectDataActivity>()
                .set(PerfectDataActivity::getStatus, 1)
                .eq(PerfectDataActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        perfectDataActivityService.update(new LambdaUpdateWrapper<PerfectDataActivity>()
                .set(PerfectDataActivity::getStatus, 0)
                .eq(PerfectDataActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        perfectDataActivityService.update(new LambdaUpdateWrapper<PerfectDataActivity>()
                .set(PerfectDataActivity::getDeleted, 1)
                .eq(PerfectDataActivity::getId, id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<PerfectDataActivityShop>> getActivityShop(Integer activityId) {
        List<PerfectDataActivityShop> list = perfectDataActivityShopService.list(new LambdaQueryWrapper<PerfectDataActivityShop>().eq(PerfectDataActivityShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<PerfectDataActivityShop> perfectDataActivityShops = new ArrayList<>();
        shopIds.forEach(temp->{
            PerfectDataActivityShop perfectDataActivityShop = PerfectDataActivityShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            perfectDataActivityShops.add(perfectDataActivityShop);
        });

        perfectDataActivityShopService.saveBatch(perfectDataActivityShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId) {
        perfectDataActivityShopService.remove(new LambdaQueryWrapper<PerfectDataActivityShop>()
                .eq(PerfectDataActivityShop::getActivityId,activityId)
                .eq(PerfectDataActivityShop::getShopId,shopId));

        List<PerfectDataActivityShop> list = perfectDataActivityShopService.list(new LambdaQueryWrapper<PerfectDataActivityShop>().eq(PerfectDataActivityShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)){
            perfectDataActivityService.update(null,new LambdaUpdateWrapper<PerfectDataActivity>()
                    .set(PerfectDataActivity::getIsAllShop,1)
                    .eq(PerfectDataActivity::getId,activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        perfectDataActivityShopService.remove(new LambdaQueryWrapper<PerfectDataActivityShop>()
                .eq(PerfectDataActivityShop::getActivityId,activityId));
        perfectDataActivityService.update(null,new LambdaUpdateWrapper<PerfectDataActivity>()
                .set(PerfectDataActivity::getIsAllShop,1)
                .eq(PerfectDataActivity::getId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public void userPerfectDataActivity(UserPerfectDataActivityDTO param) {
        Long userId = param.getUserId();
        Long shopId = param.getShopId();

        PerfectDataUserRecord perfectDataUserRecord = perfectDataUserRecordMapper.selectOne(new LambdaQueryWrapper<PerfectDataUserRecord>().eq(PerfectDataUserRecord::getUserId, userId));
        if (null != perfectDataUserRecord){
            return;
        }
        PerfectDataActivity perfectDataActivity = perfectDataActivityService.selectFirstActivity(param);

        if (null != perfectDataActivity) {
            Integer activityCouponSwitch = perfectDataActivity.getActivityCouponSwitch();
            Integer activityPointSwitch = perfectDataActivity.getActivityPointSwitch();

            Integer id = perfectDataActivity.getId();

            UserRewardRecord userRewardRecord = UserRewardRecord.builder()
                    .activityId(id)
                    .activityType(ActivityTypeEnums.PERFECT_DATA_ACTIVITY.getCode())
                    .userId(userId)
                    .shopId(shopId).build();
            if (1 == activityPointSwitch) {
                // 赠送积分
                Integer activityPointNumber = perfectDataActivity.getActivityPointNumber();
                userRewardRecord.setRewardPointNum(activityPointNumber);

                UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                updateScoreDTO.setUserId(userId);
                updateScoreDTO.setPoint_value(activityPointNumber);
                updateScoreDTO.setSource("完善资料赠送积分");
                updateScoreDTO.setPoint_channel("wechat");
                updateScoreDTO.setPoint_type("SKX_JLJF");
                updateScoreDTO.setRemark("完善资料赠送积分");
                updateScoreDTO.setIoType(1);
                //todo 待实现
//                crmUserFeignClient.updateScore(updateScoreDTO);
                log.info("perfectDataActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
            }
            if (1 == activityCouponSwitch) {
                //赠送券
                String activityCouponIds = perfectDataActivity.getActivityCouponIds();
                ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                receiveCouponDTO.setCouponId(Long.valueOf(activityCouponIds));
                receiveCouponDTO.setActivityId(Long.valueOf(id));
                receiveCouponDTO.setActivitySource(ActivitySourceEnum.PERFECT_DATA.value());
                receiveCouponDTO.setUserId(userId);
                tCouponFeignClient.receive(receiveCouponDTO);

                userRewardRecord.setRewardCouponIds(activityCouponIds);
            }
            userRewardRecordService.save(userRewardRecord);

            //保存领取记录
            PerfectDataUserRecord build = PerfectDataUserRecord.builder()
                    .activityId(id)
                    .userId(userId).build();
            perfectDataUserRecordMapper.insert(build);
        }
    }
}
