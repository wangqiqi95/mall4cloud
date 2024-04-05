package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserLevelVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AdInfoDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.OpenScreenAdBizService;
import com.mall4j.cloud.group.service.OpenScreenAdService;
import com.mall4j.cloud.group.service.OpenScreenAdShopService;
import com.mall4j.cloud.group.vo.OpenScreenAdListVO;
import com.mall4j.cloud.group.vo.OpenScreenAdVO;
import com.mall4j.cloud.group.vo.app.AppAdInfoVO;
import jodd.util.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OpenScreenAdBizServiceImpl implements OpenScreenAdBizService {
    @Resource
    private OpenScreenAdService openScreenAdService;
    @Resource
    private OpenScreenAdShopService openScreenAdShopService;
    @Resource
    private UserFeignClient userFeignClient;
    @Override
    public ServerResponseEntity<Void> saveOrUpdateOpenScreenAdActivity(OpenScreenAdDTO param) {
        OpenScreenAd openScreenAd = BeanUtil.copyProperties(param, OpenScreenAd.class);
        openScreenAdService.saveOrUpdate(openScreenAd);

        String applyShopIds = param.getApplyShopIds();
        Integer id = openScreenAd.getId();

        openScreenAdShopService.remove(new LambdaQueryWrapper<OpenScreenAdShop>().eq(OpenScreenAdShop::getActivityId,id));
        if (StringUtils.isNotEmpty(applyShopIds)){
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<OpenScreenAdShop> openScreenAdShops = new ArrayList<>();
            shopIds.forEach(temp->{
                OpenScreenAdShop openScreenAdShop = OpenScreenAdShop.builder()
                        .activityId(id)
                        .shopId(Long.valueOf(temp)).build();
                openScreenAdShops.add(openScreenAdShop);
            });
            openScreenAdShopService.saveBatch(openScreenAdShops);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<OpenScreenAdVO> detail(Integer id) {
        OpenScreenAd openScreenAd = openScreenAdService.getById(id);
        OpenScreenAdVO openScreenAdVO = BeanUtil.copyProperties(openScreenAd, OpenScreenAdVO.class);

        List<OpenScreenAdShop> list = openScreenAdShopService.list(new LambdaQueryWrapper<OpenScreenAdShop>().eq(OpenScreenAdShop::getActivityId, id));
        openScreenAdVO.setShops(list);
        return ServerResponseEntity.success(openScreenAdVO);
    }

    @Override
    public ServerResponseEntity<PageVO<OpenScreenAdListVO>> page(OpenScreenAdPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<OpenScreenAdListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> openScreenAdService.openScreenAdList(param));

        List<OpenScreenAdListVO> list = page.getResult();

        ServerResponseEntity<List<UserLevelVO>> listServerResponseEntity = userFeignClient.listLevelByLevelType(0);
        List<UserLevelVO> data = listServerResponseEntity.getData();
        Map<Long, String> levelMap = data.stream().collect(Collectors.toMap(UserLevelVO::getUserLevelId, UserLevelVO::getLevelName));


        List<OpenScreenAdListVO> resultList = list.stream().peek(temp -> {
                    Date activityEndTime = temp.getActivityEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    Date activityBeginTime = temp.getActivityBeginTime();

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

        PageVO<OpenScreenAdListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        openScreenAdService.update(new LambdaUpdateWrapper<OpenScreenAd>()
                .set(OpenScreenAd::getStatus,1)
                .eq(OpenScreenAd::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        openScreenAdService.update(new LambdaUpdateWrapper<OpenScreenAd>()
                .set(OpenScreenAd::getStatus,0)
                .eq(OpenScreenAd::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        openScreenAdService.update(new LambdaUpdateWrapper<OpenScreenAd>()
                .set(OpenScreenAd::getDeleted,1)
                .eq(OpenScreenAd::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<OpenScreenAdShop>> getActivityShop(Integer activityId) {
        List<OpenScreenAdShop> list = openScreenAdShopService.list(new LambdaQueryWrapper<OpenScreenAdShop>().eq(OpenScreenAdShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<OpenScreenAdShop> openScreenAdShops = new ArrayList<>();
        shopIds.forEach(temp->{
            OpenScreenAdShop openScreenAdShop = OpenScreenAdShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            openScreenAdShops.add(openScreenAdShop);
        });

        openScreenAdShopService.saveBatch(openScreenAdShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId) {
        openScreenAdShopService.remove(new LambdaQueryWrapper<OpenScreenAdShop>()
                .eq(OpenScreenAdShop::getActivityId,activityId)
                .eq(OpenScreenAdShop::getShopId,shopId));

        List<OpenScreenAdShop> list = openScreenAdShopService.list(new LambdaQueryWrapper<OpenScreenAdShop>().eq(OpenScreenAdShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)){
            openScreenAdService.update(null,new LambdaUpdateWrapper<OpenScreenAd>()
                    .set(OpenScreenAd::getIsAllShop,1)
                    .eq(OpenScreenAd::getId,activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        openScreenAdShopService.remove(new LambdaQueryWrapper<OpenScreenAdShop>()
                .eq(OpenScreenAdShop::getActivityId,activityId));
        openScreenAdService.update(null,new LambdaUpdateWrapper<OpenScreenAd>()
                .set(OpenScreenAd::getIsAllShop,1)
                .eq(OpenScreenAd::getId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<AppAdInfoVO> adInfo(AdInfoDTO param) {
        Long userId = param.getUserId();
        Date date = new Date();

        OpenScreenAd openScreenAd = openScreenAdService.selectFirstActivity(param);

        if (null == openScreenAd){
            return ServerResponseEntity.fail(ResponseEnum.NO_ACTIVITY);
        }

        Integer id = openScreenAd.getId();
        Date activityEndTime = openScreenAd.getActivityEndTime();
        String fansLevel = openScreenAd.getFansLevel();
        Integer adFrequency = openScreenAd.getAdFrequency();

        Integer redirectType = openScreenAd.getRedirectType();
        String activityPicUrl = openScreenAd.getActivityPicUrl();
        String redirectUrl = openScreenAd.getRedirectUrl();

        // 校验粉丝等级
        ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserData(userId);
        UserApiVO data = userData.getData();
        Integer userLevel = data.getLevel();

        if (StringUtils.isNotEmpty(fansLevel)){
            List<String> strings = Arrays.asList(fansLevel.split(StringPool.COMMA));
            List<Integer> levelLong = Convert.toList(Integer.class, strings);
            if (!levelLong.contains(userLevel)){
                return ServerResponseEntity.fail(ResponseEnum.USER_LEVEL_LIMIT);
            }
        }

        // 查询redis该用户是否打开过该广告
        String redisKey = "ad:" + id + ":" + userId;
        Object userAd = RedisUtil.get(redisKey);
        if (adFrequency != 1 && (null != userAd && (int)userAd>=1)){
            return ServerResponseEntity.fail(ResponseEnum.AD_LIMIT);
        }
        // 根据广告频率配置更新redis
        if (adFrequency == 0){
            DateTime dateTime = DateUtil.endOfDay(date);
            long endSecond = DateUtil.between(date, dateTime, DateUnit.SECOND);
            RedisUtil.incr(redisKey,1);
            RedisUtil.expire(redisKey,endSecond);
        }else if (adFrequency == 2){
            long endSecond = DateUtil.between(date, activityEndTime, DateUnit.SECOND);
            RedisUtil.incr(redisKey,1);
            RedisUtil.expire(redisKey,endSecond);
        }

        AppAdInfoVO result = AppAdInfoVO.builder()
                .redirectType(redirectType)
                .picUrl(activityPicUrl)
                .redirectUrl(redirectUrl).build();

        return ServerResponseEntity.success(result);
    }

}
