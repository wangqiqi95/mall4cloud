package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.api.coupon.constant.MyCouponStatus;
import com.mall4j.cloud.api.coupon.dto.ReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.TCouponUser;
import com.mall4j.cloud.api.docking.skq_crm.dto.CustomerGetDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.CustomerGetVo;
import com.mall4j.cloud.api.order.dto.CheckOrderDTO;
import com.mall4j.cloud.api.order.vo.OrderExcelVO;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.*;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.*;
import com.mall4j.cloud.group.vo.app.DrawAwardVO;
import com.mall4j.cloud.group.vo.app.DrawPrizeVO;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Slf4j
@Service
@Transactional
public class LotteryDrawActivityBizServiceImpl implements LotteryDrawActivityBizService {
    @Resource
    private LotteryDrawActivityService lotteryDrawActivityService;
    @Resource
    private LotteryDrawActivityPrizeService lotteryDrawActivityPrizeService;
    @Resource
    private LotteryDrawActivityGameService lotteryDrawActivityGameService;
    @Resource
    private LotteryDrawActivityShopService lotteryDrawActivityShopService;
    @Resource
    private LotteryDrawActivityStockLogService lotteryDrawActivityStockLogService;
    @Resource
    private LotteryDrawActivityAwardRecordService lotteryDrawActivityAwardRecordService;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Resource
    private LotteryDrawActivityShareService lotteryDrawActivityShareService;
    @Resource
    private CrmUserFeignClient crmUserFeignClient;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private StoreFeignClient storeFeignClient;
    @Autowired
    DownloadCenterFeignClient downloadCenterFeignClient;
    @Resource
    private CrmCustomerFeignClient crmCustomerFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public ServerResponseEntity<Integer> saveOrUpdateLotteryDrawActivity(LotteryDrawActivityDTO param) {
        LotteryDrawActivity lotteryDrawActivity = BeanUtil.copyProperties(param, LotteryDrawActivity.class);
        lotteryDrawActivityService.saveOrUpdate(lotteryDrawActivity);

        Integer id = param.getId();
        String applyShopIds = param.getApplyShopIds();

        lotteryDrawActivityShopService.remove(new LambdaQueryWrapper<LotteryDrawActivityShop>().eq(LotteryDrawActivityShop::getActivityId,id));
        if (StringUtils.isNotEmpty(applyShopIds)){
            List<String> shopIds = Arrays.asList(applyShopIds.split(StringPool.COMMA));
            List<LotteryDrawActivityShop> lotteryDrawActivityShops = new ArrayList<>();
            shopIds.forEach(temp->{
                LotteryDrawActivityShop lotteryDrawActivityShop = LotteryDrawActivityShop.builder()
                        .activityId(id)
                        .shopId(Long.valueOf(temp)).build();
                lotteryDrawActivityShops.add(lotteryDrawActivityShop);
            });
            lotteryDrawActivityShopService.saveBatch(lotteryDrawActivityShops);
        }
        return ServerResponseEntity.success(lotteryDrawActivity.getId());
    }

    @Override
    public ServerResponseEntity<Void> saveOrUpdateLotteryDrawActivityPrize(LotteryDrawActivityPrizeAddDTO param) {
        Integer lotteryDrawId = param.getLotteryDrawId();
        Integer prizeGrantTotal = param.getPrizeGrantTotal();
        Integer prizeTotal = param.getPrizeTotal();
        Integer winningRate = param.getWinningRate();
        Integer grantTarget = param.getGrantTarget();
        List<LotteryDrawActivityPrizeDTO> prizes = param.getPrizes();

        List<LotteryDrawActivityPrizeDTO> sunShinePrizes = prizes.stream().filter(p -> p.getSunshineAwardSwitch().equals(1)).collect(Collectors.toList());
        if (sunShinePrizes != null && sunShinePrizes.size() > 1) {
            throw new LuckException("该活动不可以添加两个阳光普照奖！");
        } else {
            List<LotteryDrawActivityPrizeDTO> onlyOnePrizes = prizes.stream().filter(p -> p.getOnlyOneSwitch().equals(1)).collect(Collectors.toList());
            if (onlyOnePrizes != null && onlyOnePrizes.size() > 1 && sunShinePrizes != null && sunShinePrizes.size() == 0) {
                throw new LuckException("该活动未设置阳光普照奖，只允许存在一个奖品设置仅可中一次！");
            }
        }
        List<LotteryDrawActivityPrizeDTO> bigAwardPrizes = prizes.stream().filter(p -> p.getBigAwardSwitch().equals(1)).collect(Collectors.toList());
        if (bigAwardPrizes != null && bigAwardPrizes.size() > 1) {
            throw new LuckException("该活动只可设置一个大奖！");
        }

        lotteryDrawActivityService.update(new LambdaUpdateWrapper<LotteryDrawActivity>()
                .set(LotteryDrawActivity::getWinningRate,winningRate)
                .set(LotteryDrawActivity::getGrantTarget,grantTarget)
                .set(LotteryDrawActivity::getPrizeTotal,prizeTotal)
                .set(LotteryDrawActivity::getPrizeGrantTotal,prizeGrantTotal).eq(LotteryDrawActivity::getId,lotteryDrawId));




        List<LotteryDrawActivityPrize> lotteryDrawActivityPrizes = Convert.toList(LotteryDrawActivityPrize.class, prizes);
        List<Integer> ids = lotteryDrawActivityPrizes.stream().filter(l -> l.getId() != null).map(LotteryDrawActivityPrize::getId).collect(Collectors.toList());
        LambdaQueryWrapper<LotteryDrawActivityPrize> lotteryDrawActivityPrizeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        lotteryDrawActivityPrizeLambdaQueryWrapper.eq(LotteryDrawActivityPrize::getLotteryDrawId, lotteryDrawId);
        if (ids != null && ids.size() > 0) {
            lotteryDrawActivityPrizeLambdaQueryWrapper.notIn(LotteryDrawActivityPrize::getId,ids);
        }
        List<LotteryDrawActivityPrize> lotteryDrawActivityPrizeList = lotteryDrawActivityPrizeService.list(lotteryDrawActivityPrizeLambdaQueryWrapper);
        lotteryDrawActivityPrizeService.removeByIds(lotteryDrawActivityPrizeList.stream().map(LotteryDrawActivityPrize::getId).collect(Collectors.toList()));
        lotteryDrawActivityPrizeService.saveOrUpdateBatch(lotteryDrawActivityPrizes);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> saveOrUpdateLotteryDrawActivityGame(LotteryDrawActivityGameDTO param) {
        LotteryDrawActivityGame lotteryDrawActivityGame = BeanUtil.copyProperties(param, LotteryDrawActivityGame.class);
        // 先删除后新增
        lotteryDrawActivityGameService.remove(Wrappers.lambdaQuery(LotteryDrawActivityGame.class)
                    .eq(LotteryDrawActivityGame::getLotteryDrawId, lotteryDrawActivityGame.getLotteryDrawId()));
        lotteryDrawActivityGameService.save(lotteryDrawActivityGame);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<LotteryDrawActivityVO> detail(Integer id) {
        List<LotteryDrawActivityPrize> lotteryDrawActivityPrizes = lotteryDrawActivityPrizeService.list(new LambdaQueryWrapper<LotteryDrawActivityPrize>()
                .eq(LotteryDrawActivityPrize::getLotteryDrawId, id));

        List<LotteryDrawActivityPrizeDTO> prizes = Convert.toList(LotteryDrawActivityPrizeDTO.class, lotteryDrawActivityPrizes);

        LotteryDrawActivityGame lotteryDrawActivityGameServiceOne = lotteryDrawActivityGameService.getOne(new LambdaQueryWrapper<LotteryDrawActivityGame>().eq(LotteryDrawActivityGame::getLotteryDrawId, id));

        LotteryDrawActivityGameDTO game = BeanUtil.copyProperties(lotteryDrawActivityGameServiceOne, LotteryDrawActivityGameDTO.class);


        LotteryDrawActivity lotteryDrawActivity = lotteryDrawActivityService.getById(id);

        LotteryDrawActivityVO lotteryDrawActivityVO = BeanUtil.copyProperties(lotteryDrawActivity, LotteryDrawActivityVO.class);

        lotteryDrawActivityVO.setPrizes(prizes);
        lotteryDrawActivityVO.setGame(game);

        List<LotteryDrawActivityShop> shops = lotteryDrawActivityShopService.list(new LambdaQueryWrapper<LotteryDrawActivityShop>().eq(LotteryDrawActivityShop::getActivityId, id));

        lotteryDrawActivityVO.setShops(shops);

        return ServerResponseEntity.success(lotteryDrawActivityVO);
    }

    @Override
    public ServerResponseEntity<PageVO<LotteryDrawListVO>> page(LotteryDrawPageDTO param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();


        Page<LotteryDrawListVO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> lotteryDrawActivityService.lotteryDrawList(param));

        List<LotteryDrawListVO> list = page.getResult();


        List<LotteryDrawListVO> resultList = list.stream().peek(temp -> {
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

        PageVO<LotteryDrawListVO> pageVO = new PageVO<>();
        pageVO.setPages(page.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        lotteryDrawActivityService.update(new LambdaUpdateWrapper<LotteryDrawActivity>()
                .set(LotteryDrawActivity::getStatus,1)
                .eq(LotteryDrawActivity::getId,id));

        List<LotteryDrawActivityPrize> prizes = lotteryDrawActivityPrizeService.list(new LambdaQueryWrapper<LotteryDrawActivityPrize>()
                .eq(LotteryDrawActivityPrize::getLotteryDrawId, id));

        prizes.forEach(temp->{
            Integer prizeId = temp.getId();
            String redisKey = "lottery::prize::stock::"+prizeId;
            Integer prizeInitStock = temp.getPrizeInitStock();
            RedisUtil.set(redisKey,prizeInitStock,-1L);
        });
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        lotteryDrawActivityService.update(new LambdaUpdateWrapper<LotteryDrawActivity>()
                .set(LotteryDrawActivity::getStatus,0)
                .eq(LotteryDrawActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        lotteryDrawActivityService.update(new LambdaUpdateWrapper<LotteryDrawActivity>()
                .set(LotteryDrawActivity::getDeleted,1)
                .eq(LotteryDrawActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<LotteryDrawActivityShop>> getActivityShop(Integer activityId) {
        List<LotteryDrawActivityShop> list = lotteryDrawActivityShopService.list(new LambdaQueryWrapper<LotteryDrawActivityShop>()
                .eq(LotteryDrawActivityShop::getActivityId, activityId));
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param) {
        Integer id = param.getActivityId();
        List<Long> shopIds = param.getShopIds();

        List<LotteryDrawActivityShop> lotteryDrawActivityShops = new ArrayList<>();
        shopIds.forEach(temp->{
            LotteryDrawActivityShop lotteryDrawActivityShop = LotteryDrawActivityShop.builder()
                    .activityId(id)
                    .shopId(temp).build();
            lotteryDrawActivityShops.add(lotteryDrawActivityShop);
        });

        lotteryDrawActivityShopService.saveBatch(lotteryDrawActivityShops);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId) {
        lotteryDrawActivityShopService.remove(new LambdaQueryWrapper<LotteryDrawActivityShop>()
                .eq(LotteryDrawActivityShop::getActivityId,activityId)
                .eq(LotteryDrawActivityShop::getShopId,shopId));

        List<LotteryDrawActivityShop> list = lotteryDrawActivityShopService.list(new LambdaQueryWrapper<LotteryDrawActivityShop>()
                .eq(LotteryDrawActivityShop::getActivityId, activityId));
        if (CollectionUtil.isEmpty(list)){
            lotteryDrawActivityService.update(null,new LambdaUpdateWrapper<LotteryDrawActivity>()
                    .set(LotteryDrawActivity::getIsAllShop,1)
                    .eq(LotteryDrawActivity::getId,activityId));
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteAllShop(Integer activityId) {
        lotteryDrawActivityShopService.remove(new LambdaQueryWrapper<LotteryDrawActivityShop>()
                .eq(LotteryDrawActivityShop::getActivityId,activityId));
        lotteryDrawActivityService.update(null,new LambdaUpdateWrapper<LotteryDrawActivity>()
                .set(LotteryDrawActivity::getIsAllShop,1)
                .eq(LotteryDrawActivity::getId,activityId));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<LotteryDrawActivityCensusVO> census(Integer id) {
        LotteryDrawActivityCensusVO result = lotteryDrawActivityService.census(id);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> stockChange(LotteryPrizeStockChangeDTO param) {
        Integer id = param.getId();
        Integer changeType = param.getChangeType();
        Integer changeNum = param.getChangeNum();
        String prizeName = param.getPrizeName();
        Integer lotteryDrawId = param.getLotteryDrawId();
        String username = AuthUserContext.get().getUsername();
        Long userId = AuthUserContext.get().getUserId();

        String redisKey = "lottery::prize::stock::"+id;

        if (1==changeType){
            RedisUtil.incr(redisKey,changeNum);
        }
        if (2==changeType){
            Long decr = RedisUtil.decr(redisKey, changeNum);
            if (decr < 0){
                RedisUtil.incr(redisKey, changeNum);
            }
            throw new LuckException("减少库存不能大于剩余库存!");
        }
        lotteryDrawActivityPrizeService.changeStockNum(param);

        LotteryDrawActivityStockLog stockLog = LotteryDrawActivityStockLog.builder()
                .lotteryDrawId(lotteryDrawId)
                .optNum(changeNum)
                .prizeName(prizeName)
                .optType(changeType)
                .optUserId(userId)
                .optUserName(username).build();
        lotteryDrawActivityStockLogService.save(stockLog);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<LotteryStockChangeLogVO>> stockChangeLog(Integer id) {
        List<LotteryDrawActivityStockLog> stockLogs = lotteryDrawActivityStockLogService.list(new LambdaQueryWrapper<LotteryDrawActivityStockLog>()
                .eq(LotteryDrawActivityStockLog::getLotteryDrawId, id)
                .orderByDesc(LotteryDrawActivityStockLog::getOptTime));
        List<LotteryStockChangeLogVO> lotteryStockChangeLogVOS = Convert.toList(LotteryStockChangeLogVO.class, stockLogs);
        return ServerResponseEntity.success(lotteryStockChangeLogVOS);
    }

    @Override
    public ServerResponseEntity<PageVO<LotteryAwardRecordVO>> awardRecord(LotteryAwardRecordListDTO param) {
        Date awardBeginTime = param.getAwardBeginTime();
        Date awardEndTime = param.getAwardEndTime();
        Integer awardFlag = param.getAwardFlag();
        Integer prizeId = param.getPrizeId();
        String userInfo = param.getUserInfo();
        Integer id = param.getId();

        LambdaQueryWrapper<LotteryDrawActivityAwardRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryDrawActivityAwardRecord::getLotteryDrawId,id);
        if (null != awardBeginTime){
            wrapper.between(LotteryDrawActivityAwardRecord::getAwardTime,awardBeginTime,awardEndTime);
        }
        if (1 == awardFlag){
            wrapper.ne(LotteryDrawActivityAwardRecord::getAwardType,0);
        }
        if (0 == awardFlag){
            wrapper.eq(LotteryDrawActivityAwardRecord::getAwardType,0);
        }

        if (null != prizeId){
            wrapper.eq(LotteryDrawActivityAwardRecord::getLotteryDrawPrizeId,prizeId);
        }

        if (StringUtils.isNotEmpty(userInfo)){
            wrapper.and(wrapper1->wrapper1.like(LotteryDrawActivityAwardRecord::getMobile,userInfo).or(wrapper2->wrapper2.like(LotteryDrawActivityAwardRecord::getNickname,userInfo)));
        }
        wrapper.orderByDesc(LotteryDrawActivityAwardRecord::getAwardTime);
        Page<LotteryDrawActivityAwardRecord> records = PageHelper.startPage(param.getPageNum(), param.getPageSize()).doSelectPage(() ->
                lotteryDrawActivityAwardRecordService.list(wrapper)
        );
        List<LotteryDrawActivityAwardRecord> result = records.getResult();

        List<LotteryAwardRecordVO> lotteryAwardRecordVOS = Convert.toList(LotteryAwardRecordVO.class, result);
        List<Long> useridList = result.stream().map(LotteryDrawActivityAwardRecord::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        Map<Long, UserApiVO> finalUserMaps = userMaps;
        lotteryAwardRecordVOS.forEach(temp->{
            if (temp.getCouponId() != null) {
                log.info("当前中奖信息:{}",JSONObject.toJSONString(temp));
                Long couponId = temp.getCouponId();
                Integer lotteryDrawId = temp.getLotteryDrawId();
                // 查询优惠券核销状态
                TCouponUser tCouponUser = new TCouponUser();
                tCouponUser.setCouponId(couponId);
                tCouponUser.setUserId(temp.getUserId());
                tCouponUser.setActivityId(Long.valueOf(lotteryDrawId));
                tCouponUser.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                log.info("当前券查询参数:{}",JSONObject.toJSONString(tCouponUser));
                ServerResponseEntity<List<TCouponUser>> listServerResponseEntity = tCouponFeignClient.userCouponList(tCouponUser);
                if (listServerResponseEntity != null && listServerResponseEntity.isSuccess() && listServerResponseEntity.getData() != null && listServerResponseEntity.getData().size() > 0) {
                    List<TCouponUser> data = listServerResponseEntity.getData();
                    TCouponUser couponDetail = data.get(0);
                    log.info("当前券信息:{}",JSONObject.toJSONString(couponDetail));
                    int status = couponDetail.getStatus();
                    String couponCode = couponDetail.getCouponCode();
                    temp.setCouponCode(couponCode);
                    temp.setCouponStatus(status);
                }
            }
            UserApiVO user = finalUserMaps.get(temp.getUserId());
            if (user != null) {
                temp.setNickname(user.getNickName());
            }
        });

        PageVO<LotteryAwardRecordVO> pageVO = new PageVO<>();
        pageVO.setPages(records.getPages());
        pageVO.setTotal(records.getTotal());
        pageVO.setList(lotteryAwardRecordVOS);
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public void awardRecordExport(HttpServletResponse response, LotteryAwardRecordListDTO param) {
        CalcingDownloadRecordDTO downloadRecordDTO = new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(LotteryAwardRecordExportVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo("" + AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId = null;
        if (serverResponseEntity.isSuccess()) {
            downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
        }

        Date awardBeginTime = param.getAwardBeginTime();
        Date awardEndTime = param.getAwardEndTime();
        Integer awardFlag = param.getAwardFlag();
        Integer prizeId = param.getPrizeId();
        String userInfo = param.getUserInfo();
        Integer id = param.getId();

        LambdaQueryWrapper<LotteryDrawActivityAwardRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryDrawActivityAwardRecord::getLotteryDrawId,id);
        if (null != awardBeginTime){
            wrapper.between(LotteryDrawActivityAwardRecord::getAwardTime,awardBeginTime,awardEndTime);
        }
        if (1 == awardFlag){
            wrapper.ne(LotteryDrawActivityAwardRecord::getAwardType,0);
        }
        if (0 == awardFlag){
            wrapper.eq(LotteryDrawActivityAwardRecord::getAwardType,0);
        }

        if (null != prizeId){
            wrapper.eq(LotteryDrawActivityAwardRecord::getLotteryDrawPrizeId,prizeId);
        }

        if (StringUtils.isNotEmpty(userInfo)){
            wrapper.and(wrapper1->wrapper1.like(LotteryDrawActivityAwardRecord::getMobile,userInfo).or(wrapper2->wrapper2.like(LotteryDrawActivityAwardRecord::getNickname,userInfo)));
        }
        wrapper.orderByDesc(LotteryDrawActivityAwardRecord::getAwardTime);

        List<LotteryDrawActivityAwardRecord> result = lotteryDrawActivityAwardRecordService.list(wrapper);

        List<LotteryAwardRecordVO> lotteryAwardRecordVOS = Convert.toList(LotteryAwardRecordVO.class, result);
        List<Long> useridList = result.stream().map(LotteryDrawActivityAwardRecord::getUserId).distinct().collect(Collectors.toList());
        log.info("会员id信息:{}",JSONObject.toJSONString(useridList));
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }
        log.info("会员信息:{}",JSONObject.toJSONString(userMaps));
        Map<Long, UserApiVO> finalUserMaps = userMaps;
        lotteryAwardRecordVOS.forEach(temp->{
            if (temp.getCouponId() != null && temp.getAwardType() ==1) {
                log.info("当前中奖信息:{}",JSONObject.toJSONString(temp));
                Long couponId = temp.getCouponId();
                Integer lotteryDrawId = temp.getLotteryDrawId();
                // 查询优惠券核销状态
                TCouponUser tCouponUser = new TCouponUser();
                tCouponUser.setCouponId(couponId);
                tCouponUser.setUserId(temp.getUserId());
                tCouponUser.setActivityId(Long.valueOf(lotteryDrawId));
                tCouponUser.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                log.info("当前券查询参数:{}",JSONObject.toJSONString(tCouponUser));
                ServerResponseEntity<List<TCouponUser>> listServerResponseEntity = tCouponFeignClient.userCouponList(tCouponUser);
                if (listServerResponseEntity != null && listServerResponseEntity.isSuccess() && listServerResponseEntity.getData() != null && listServerResponseEntity.getData().size() > 0) {
                    List<TCouponUser> data = listServerResponseEntity.getData();
                    TCouponUser couponDetail = data.get(0);
                    log.info("当前券信息:{}",JSONObject.toJSONString(couponDetail));
                    int status = couponDetail.getStatus();
                    String couponCode = couponDetail.getCouponCode();
                    temp.setCouponCode(couponCode);
                    temp.setCouponStatus(status);
                }
            }
            UserApiVO user = finalUserMaps.get(temp.getUserId());
            if (user != null) {
                temp.setNickname(user.getNickName());
            }
        });

        List<LotteryAwardRecordExportVO> excelVOS = lotteryAwardRecordVOS.stream().map(temp -> {
            LotteryAwardRecordExportVO lotteryAwardRecordExportVO = new LotteryAwardRecordExportVO();
            lotteryAwardRecordExportVO.setAwardTime(temp.getAwardTime());
            lotteryAwardRecordExportVO.setMobile(temp.getMobile());
            lotteryAwardRecordExportVO.setNickname(temp.getNickname());
            lotteryAwardRecordExportVO.setCouponCode(temp.getCouponCode());
            lotteryAwardRecordExportVO.setPrizeName(temp.getPrizeName());
            lotteryAwardRecordExportVO.setShopName(temp.getShopName());
            lotteryAwardRecordExportVO.setUsername(temp.getUserName());
            lotteryAwardRecordExportVO.setPhone(temp.getPhone());
            if (StrUtil.isNotBlank(temp.getAddr())) {
                lotteryAwardRecordExportVO.setAddress(temp.getAddr());
            }
            if (StrUtil.isNotBlank(temp.getUserAddr())) {
                lotteryAwardRecordExportVO.setAddress(lotteryAwardRecordExportVO.getAddress() + temp.getUserAddr());
            }
            if (temp.getCouponStatus() != null) {
                lotteryAwardRecordExportVO.setCouponStatus(MyCouponStatus.instance(temp.getCouponStatus()).desc());
            }
            return lotteryAwardRecordExportVO;
        }).collect(Collectors.toList());

//        ExcelUtil.soleExcel(response,exportVOS,LotteryAwardRecordExportVO.EXCEL_NAME,LotteryAwardRecordExportVO.MERGE_ROW_INDEX,LotteryAwardRecordExportVO.MERGE_COLUMN_INDEX, LotteryAwardRecordExportVO.class);
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        if(CollUtil.isEmpty(excelVOS)) {
            finishDownLoadDTO.setRemarks("没有可导出的数据");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("没有可导出的数据");
            return;
        }
        File file=null;
        try {
            int calCount=excelVOS.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行抽奖活动生成excel 总条数【{}】",calCount);
            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, LotteryAwardRecordExportVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(excelVOS);
            String contentType = "application/vnd.ms-excel";
            log.info("结束执行抽奖活动生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,excelVOS.size(),pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("上传文件地址："+responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+LotteryAwardRecordExportVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        }catch (Exception e){
            log.error("导出抽奖活动信息错误: "+e.getMessage(),e);
            finishDownLoadDTO.setRemarks("导出抽奖活动日志失败，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }

    @Transactional(noRollbackFor = Exception.class)
    @Override
    public ServerResponseEntity<DrawPrizeVO> draw(Integer id,Long storeId) {
        Long userId = AuthUserContext.get().getUserId();
        clearUserPermissionsCache(AuthUserContext.get().getUserId());
        ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserData(userId);
        UserApiVO userApiVO = userData.getData();
        // 校验当前用户是否注册登录
        if (ObjectUtil.isEmpty(userApiVO)) {
            throw new LuckException("请您先注册成为斯凯奇会员");
        }
        String userMobile = userApiVO.getUserMobile();
        String userPhone = userApiVO.getPhone();
        String nickName = userApiVO.getNickName();
        Integer userLevelId = userApiVO.getLevel();

//        Long storeId = AuthUserContext.get().getStoreId();
        StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
        String shopName = byStoreId.getName();
        Date date = new Date();

        LotteryDrawActivity lotteryDrawActivity = lotteryDrawActivityService.getById(id);
        Date activityBeginTime = lotteryDrawActivity.getActivityBeginTime();
        Date activityEndTime = lotteryDrawActivity.getActivityEndTime();
        Integer status = lotteryDrawActivity.getStatus();
        if (!(activityBeginTime.compareTo(date) <=0 && activityEndTime.compareTo(date) >0 && status ==1)){
            throw new LuckException("该活动不在活动时间内！");
        }
        List<LotteryDrawActivityShop> lotteryDrawActivityShops = lotteryDrawActivityShopService.list(new LambdaQueryWrapper<LotteryDrawActivityShop>()
                .eq(LotteryDrawActivityShop::getActivityId, id)
        );
        if (lotteryDrawActivityShops != null && lotteryDrawActivityShops.size() > 0) {
            Set<Long> collect = lotteryDrawActivityShops.stream().map(LotteryDrawActivityShop::getShopId).collect(Collectors.toSet());
            if (!collect.contains(storeId)) {
                throw new LuckException("该门店不在活动范围内！");
            }
        }

        Integer drawType = lotteryDrawActivity.getDrawType();
        Integer drawTimes = lotteryDrawActivity.getDrawTimes();
        Integer orderSwitch = lotteryDrawActivity.getOrderSwitch();


        Integer usePointType = lotteryDrawActivity.getUsePointType();
        Integer usePoint = lotteryDrawActivity.getUsePoint();

        Integer prizeGrantTotal = lotteryDrawActivity.getPrizeGrantTotal();
        Integer prizeTotal = lotteryDrawActivity.getPrizeTotal();

        //判断用户是否超过每次抽奖上限
        if (usePointType == 1){
            if (drawType == 1 && drawTimes>0){
                List<LotteryDrawActivityShare> shareList = lotteryDrawActivityShareService.list(new LambdaQueryWrapper<LotteryDrawActivityShare>()
                        .eq(LotteryDrawActivityShare::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityShare::getUserId, userId)
                        .between(LotteryDrawActivityShare::getShareTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));
                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId)
                        .between(LotteryDrawActivityAwardRecord::getAwardTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));

                int shareTimes = shareList.size();
                int userTimes = awardRecords.size();
//                if (userTimes == (drawTimes+shareTimes)){
                if (userTimes >= (drawTimes+shareTimes)){
                    throw new LuckException("您已达本次活动抽奖上限");
                }
            }
            if (drawType == 2 && drawTimes>0){
                List<LotteryDrawActivityShare> shareList = lotteryDrawActivityShareService.list(new LambdaQueryWrapper<LotteryDrawActivityShare>()
                        .eq(LotteryDrawActivityShare::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityShare::getUserId, userId));

                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId));

                int shareTimes = shareList.size();
                int userTimes = awardRecords.size();
//                if (userTimes == (drawTimes+shareTimes)){
                if (userTimes >= (drawTimes+shareTimes)){
                    throw new LuckException("您已达本次活动抽奖上限");
                }
            }
        }

        // 新增用户等级校验，判断用户等级是否达到参与抽奖游戏要求
        log.info("进入消费限制流程,当前用户为:{},用户详情信息为:{},抽奖游戏活动参数为:{}", userId, userApiVO, lotteryDrawActivity);
        CustomerGetDto customerGetDto = new CustomerGetDto();
        customerGetDto.setMobile(userApiVO.getPhone());
        // 调用crm接口获取用户信息
        ServerResponseEntity<CustomerGetVo> customerGetVoServerResponseEntity = crmCustomerFeignClient.customerGet(customerGetDto);
        if (customerGetVoServerResponseEntity != null && customerGetVoServerResponseEntity.isSuccess() && customerGetVoServerResponseEntity.getData() != null) {
            CustomerGetVo crmUserData = customerGetVoServerResponseEntity.getData();
            log.info("调用crm接口查询会员信息,{}", crmUserData);
            // 校验用户会员等级
            String fanLevels = lotteryDrawActivity.getFanLevels();
            if (StringUtils.isNotEmpty(fanLevels)) {
                List<String> userLevelList = Arrays.asList(fanLevels.split(StringPool.COMMA));
                //List<Integer> userLevelList = Convert.toList(Integer.class, strings);
                log.info("进入等级校验中,活动要求会员等级为:{},转成String类型List后:{}", fanLevels, userLevelList);
                if (!userLevelList.contains(crmUserData.getCurrent_grade_id())) {
                    if (StrUtil.isNotBlank(lotteryDrawActivity.getFanTips())) {
                        throw new LuckException(lotteryDrawActivity.getFanTips());
                    } else {
                        throw new LuckException("未达到参与活动的条件");
                    }
                }
            }
        }

        // 判断用户订单消费金额是否满足条件
        if (orderSwitch == 1) {
            CheckOrderDTO checkOrderDTO = new CheckOrderDTO();
            checkOrderDTO.setUserId(userId);
            checkOrderDTO.setPhone(userPhone);
            checkOrderDTO.setStartTime(DateUtil.formatDateTime(lotteryDrawActivity.getOrderStartTime()));
            checkOrderDTO.setEndTime(DateUtil.formatDateTime(lotteryDrawActivity.getOrderEndTime()));
            checkOrderDTO.setOrderType(lotteryDrawActivity.getOrderType());
            Long orderNum = lotteryDrawActivity.getOrderNum();
            if ((lotteryDrawActivity.getOrderExpendType() == null || lotteryDrawActivity.getOrderExpendType() == 0) && orderNum != null && orderNum >= 0) {
                String startTime = DateUtil.formatDateTime(lotteryDrawActivity.getOrderStartTime());
                String endTime = DateUtil.formatDateTime(lotteryDrawActivity.getOrderEndTime());
                String orderType = lotteryDrawActivity.getOrderType();
                log.info("判断用户消金额查询条件:{},{},{},{}",userId,startTime,endTime,orderType);
                ServerResponseEntity<Long> responseEntity = orderFeignClient.userConsumeAmount(userId,startTime,endTime,orderType);
                if (responseEntity != null && responseEntity.isSuccess() && responseEntity.getData() != null) {
                    Long orderPay = responseEntity.getData();
                    log.info("判断用户消金额是否满足条件:{},{}",orderNum,orderPay);
                    if (orderPay <= orderNum) {
                        if (StrUtil.isNotBlank(lotteryDrawActivity.getOrderTips())) {
                            throw new LuckException(lotteryDrawActivity.getOrderTips());
                        } else {
                            throw new LuckException("未达到参与活动的条件");
                        }
                    }
                }
                if (StrUtil.isNotBlank(lotteryDrawActivity.getAppointShop())) {
                    String[] types = lotteryDrawActivity.getAppointShop().split(",");
                    List<Long> storeIds = Arrays.stream(types).map(type -> {
                        return Long.valueOf(type);
                    }).collect(Collectors.toList());
                    ServerResponseEntity<List<StoreVO>> storeList = storeFeignClient.listByStoreIdList(storeIds);
                    List<String> storeCodes = storeList.getData().stream().map(StoreVO::getStoreCode).collect(Collectors.toList());
                    checkOrderDTO.setStoreIdList(storeIds);
                    checkOrderDTO.setStoreCodeList(storeCodes);
                    ServerResponseEntity<Integer> isResult = orderFeignClient.checkIsOrderIntoShops(checkOrderDTO);
                    log.info("消费校验返回数据,{}", isResult.getData());
                    if (isResult.getData() == 0) {
                        throw new LuckException("尚未在指定消费门店中进行消费，无法进行兑换！");
                    }
                }
            } else {
                // 获取指定消费门店,然后查询订单.看在活动时间范围内是否在指定门店中进行了消费
                if(lotteryDrawActivity.getOrderExpendType() != null && lotteryDrawActivity.getOrderExpendType() == 1){
                    checkOrderDTO.setOrderNum(lotteryDrawActivity.getOrderNum()/100);
                }
                if (StrUtil.isNotBlank(lotteryDrawActivity.getAppointShop())) {
                    String[] types = lotteryDrawActivity.getAppointShop().split(",");
                    List<Long> storeIds = Arrays.stream(types).map(type -> {
                        return Long.valueOf(type);
                    }).collect(Collectors.toList());
                    ServerResponseEntity<List<StoreVO>> storeList = storeFeignClient.listByStoreIdList(storeIds);
                    List<String> storeCodes = storeList.getData().stream().map(StoreVO::getStoreCode).collect(Collectors.toList());
                    checkOrderDTO.setStoreIdList(storeIds);
                    checkOrderDTO.setStoreCodeList(storeCodes);
                }
                ServerResponseEntity<Integer> isResult = orderFeignClient.checkIsOrderIntoShops(checkOrderDTO);
                log.info("消费校验返回数据,{}", isResult.getData());
                if (isResult.getData() == 0) {
                    if (StrUtil.isNotBlank(lotteryDrawActivity.getOrderTips())) {
                        throw new LuckException(lotteryDrawActivity.getOrderTips());
                    } else {
                        throw new LuckException("尚未在指定消费门店中进行消费，无法进行兑换！");
                    }
                }
            }
        }

        //判断用户是否需要使用积分
        if (usePointType == 2){
            if (drawType == 1){
                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId)
                        .between(LotteryDrawActivityAwardRecord::getAwardTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));
                int userTimes = awardRecords.size();
                if (userTimes < drawTimes){
                    usePoint = 0;
                }
            }
            if (drawType == 2){
                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId));
                int userTimes = awardRecords.size();
                if (userTimes < drawTimes){
                    usePoint = 0;
                }
            }
        }

        if (usePoint != 0){
            UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
            updateScoreDTO.setUserId(userId);
            updateScoreDTO.setPoint_value(-usePoint);
            updateScoreDTO.setSource("抽奖活动扣减积分");
            updateScoreDTO.setPoint_channel("wechat");
            updateScoreDTO.setPoint_type("SKX_HDHD");
            updateScoreDTO.setRemark("抽奖活动扣减积分");
            updateScoreDTO.setIoType(0);
            ServerResponseEntity serverResponseEntity;
            try {
                 serverResponseEntity = crmUserFeignClient.updateScore(updateScoreDTO);
            }catch (Exception e){
                throw new LuckException("您当前积分不足，请赚取更多积分获取抽奖资格。");
            }
            if (null == serverResponseEntity || serverResponseEntity.isFail()){
                throw new LuckException("您当前积分不足，请赚取更多积分获取抽奖资格。");
            }
            log.info("perfectDataActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
        }

        //判断奖品发放是否达到总数
        if (prizeGrantTotal == 1 && prizeTotal != 0) {
            List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                    .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                    .ne(LotteryDrawActivityAwardRecord::getAwardType, 0)
                    .between(LotteryDrawActivityAwardRecord::getAwardTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));
            int userTimes = awardRecords.size();
            if (userTimes >= prizeTotal){
                log.info("今日奖品发放总量达到上限未中奖");
                noDraw(userId,id,userMobile,nickName,shopName,storeId,null,null,null,null,drawTimes);
                throw new LuckException("很抱歉你未中奖！");
            }
        }
        if (prizeGrantTotal == 2 && prizeTotal != 0) {
            List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                    .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                    .ne(LotteryDrawActivityAwardRecord::getAwardType, 0));
            int userTimes = awardRecords.size();
            if (userTimes >= prizeTotal){
                log.info("奖品发放总量达到上限未中奖");
                noDraw(userId,id,userMobile,nickName,shopName,storeId,null,null,null,null,drawTimes);
                throw new LuckException("很抱歉你未中奖！");
            }
        }

        Integer winningRate = lotteryDrawActivity.getWinningRate();
        List<LotteryDrawActivityPrize> lotteryDrawActivityPrizes = lotteryDrawActivityPrizeService.list(new LambdaQueryWrapper<LotteryDrawActivityPrize>().eq(LotteryDrawActivityPrize::getLotteryDrawId, id));

        int randomInt = RandomUtil.randomInt(1, 1001);

        //若未中奖
        if (randomInt>winningRate){
            //查询是否拥有阳光普照奖
            List<LotteryDrawActivityPrize> sunPrize = lotteryDrawActivityPrizes.stream().filter(temp -> temp.getSunshineAwardSwitch() == 1).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sunPrize)){
                LotteryDrawActivityPrize lotteryDrawActivityPrize = sunPrize.get(0);

                Integer prizeId = lotteryDrawActivityPrize.getId();
                String prizeName = lotteryDrawActivityPrize.getPrizeName();
                String prizeImgs = null;
                if (StrUtil.isNotBlank(lotteryDrawActivityPrize.getPrizeImgs())) {
                    prizeImgs = lotteryDrawActivityPrize.getPrizeImgs();
                }
                Integer pointNum = lotteryDrawActivityPrize.getPointNum();
                Long couponId = lotteryDrawActivityPrize.getCouponId();
                Integer prizeType = lotteryDrawActivityPrize.getPrizeType();
                Integer prizeStatus = 2;
                if (1==prizeType){
                    //赠送券
                    ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                    receiveCouponDTO.setCouponId(couponId);
                    receiveCouponDTO.setActivityId(Long.valueOf(id));
                    receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                    receiveCouponDTO.setUserId(userId);
                    tCouponFeignClient.receive(receiveCouponDTO);
                }else if (2==prizeType){
                    UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                    updateScoreDTO.setUserId(userId);
                    updateScoreDTO.setPoint_value(pointNum);
                    updateScoreDTO.setSource("抽奖活动赠送积分");
                    updateScoreDTO.setPoint_channel("wechat");
                    updateScoreDTO.setPoint_type("SKX_JLJF");
                    updateScoreDTO.setRemark("抽奖活动赠送积分");
                    updateScoreDTO.setIoType(1);
                    crmUserFeignClient.updateScore(updateScoreDTO);
                    log.info("drawActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
                }else if (3 == prizeType) {
                    prizeStatus = 0;
                }
                LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                        .lotteryDrawId(id)
                        .prizeName(prizeName)
                        .awardType(prizeType)
                        .couponId(couponId)
                        .pointNum(pointNum)
                        .lotteryDrawPrizeId(prizeId)
                        .mobile(userMobile)
                        .nickname(nickName)
                        .shopId(storeId)
                        .shopName(shopName)
                        .status(prizeStatus)
                        .userId(userId).build();
                lotteryDrawActivityAwardRecordService.save(awardRecord);
                DrawPrizeVO result = DrawPrizeVO.builder()
                        .prizeImgs(prizeImgs)
                        .prizeName(prizeName).build();
                return ServerResponseEntity.success(result);
            }else {
                log.info("几率未中奖");
                noDraw(userId,id,userMobile,nickName,shopName,storeId,null,null,null,null,drawTimes);

                throw new LuckException("很抱歉你未中奖！");
            }
        }
        Integer grantTarget = lotteryDrawActivity.getGrantTarget();

        if (1==grantTarget){
            List<LotteryDrawActivityPrize> drawActivityPrizes = lotteryDrawActivityPrizes.stream().filter(temp -> {
                String userLevels = temp.getFanLevels();
                if (StringUtils.isNotEmpty(userLevels)) {
                    List<String> strings = Arrays.asList(userLevels.split(StringPool.COMMA));
                    List<Integer> userLevelList = Convert.toList(Integer.class, strings);
                    if (userLevelList.contains(userLevelId)) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());

            if (CollectionUtil.isEmpty(drawActivityPrizes)){

                //查询是否拥有阳光普照奖
                List<LotteryDrawActivityPrize> sunPrize = lotteryDrawActivityPrizes.stream().filter(temp -> temp.getSunshineAwardSwitch() == 1).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(sunPrize)){
                    LotteryDrawActivityPrize lotteryDrawActivityPrize = sunPrize.get(0);

                    Integer prizeId = lotteryDrawActivityPrize.getId();
                    String prizeImgs = null;
                    if (StrUtil.isNotBlank(lotteryDrawActivityPrize.getPrizeImgs())) {
                        prizeImgs = lotteryDrawActivityPrize.getPrizeImgs();
                    }
                    String prizeName = lotteryDrawActivityPrize.getPrizeName();
                    Integer pointNum = lotteryDrawActivityPrize.getPointNum();
                    Long couponId = lotteryDrawActivityPrize.getCouponId();
                    Integer prizeType = lotteryDrawActivityPrize.getPrizeType();
                    Integer prizeStatus = 2;
                    if (1==prizeType){
                        //赠送券
                        ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                        receiveCouponDTO.setCouponId(couponId);
                        receiveCouponDTO.setActivityId(Long.valueOf(id));
                        receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                        receiveCouponDTO.setUserId(userId);
                        tCouponFeignClient.receive(receiveCouponDTO);
                    }else if (2==prizeType){
                        UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                        updateScoreDTO.setUserId(userId);
                        updateScoreDTO.setPoint_value(pointNum);
                        updateScoreDTO.setSource("抽奖活动赠送积分");
                        updateScoreDTO.setPoint_channel("wechat");
                        updateScoreDTO.setPoint_type("SKX_JLJF");
                        updateScoreDTO.setRemark("抽奖活动赠送积分");
                        updateScoreDTO.setIoType(1);
                        crmUserFeignClient.updateScore(updateScoreDTO);
                        log.info("drawActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
                    }else if (3 == prizeType) {
                        prizeStatus = 0;
                    }
                    LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                            .lotteryDrawId(id)
                            .prizeName(prizeName)
                            .awardType(prizeType)
                            .couponId(couponId)
                            .pointNum(pointNum)
                            .lotteryDrawPrizeId(prizeId)
                            .mobile(userMobile)
                            .nickname(nickName)
                            .shopId(storeId)
                            .shopName(shopName)
                            .status(prizeStatus)
                            .userId(userId).build();
                    lotteryDrawActivityAwardRecordService.save(awardRecord);
                    DrawPrizeVO result = DrawPrizeVO.builder()
                            .prizeImgs(prizeImgs)
                            .prizeName(prizeName).build();
                    return ServerResponseEntity.success(result);
                }else {

                    noDraw(userId,id,userMobile,nickName,shopName,storeId,null,null,null,null,drawTimes);

                    throw new LuckException("很抱歉你未中奖！");
                }
            }
            DrawPrizeVO result = randomPrize(drawActivityPrizes, userId, id, userMobile, nickName, shopName, storeId,drawTimes,lotteryDrawActivityPrizes);

            return ServerResponseEntity.success(result);

        }else {
            DrawPrizeVO result = randomPrize(lotteryDrawActivityPrizes, userId, id, userMobile, nickName, shopName, storeId,drawTimes,lotteryDrawActivityPrizes);
            return ServerResponseEntity.success(result);
        }
    }

    @Override
    public ServerResponseEntity<Integer> drawScore(Integer id) {
        Long userId = AuthUserContext.get().getUserId();
        Date date = new Date();
        LotteryDrawActivity lotteryDrawActivity = lotteryDrawActivityService.getById(id);
        Date activityBeginTime = lotteryDrawActivity.getActivityBeginTime();
        Date activityEndTime = lotteryDrawActivity.getActivityEndTime();
        Integer status = lotteryDrawActivity.getStatus();
        if (!(activityBeginTime.compareTo(date) <=0 && activityEndTime.compareTo(date) >0 && status ==1)){
            throw new LuckException("该活动不在活动时间内！");
        }

        Integer drawType = lotteryDrawActivity.getDrawType();
        Integer drawTimes = lotteryDrawActivity.getDrawTimes();
        Integer usePointType = lotteryDrawActivity.getUsePointType();
        Integer usePoint = lotteryDrawActivity.getUsePoint();

        //判断用户是否超过每次抽奖上限
        if (usePointType == 1){
            if (drawType == 1 && drawTimes>0){
                List<LotteryDrawActivityShare> shareList = lotteryDrawActivityShareService.list(new LambdaQueryWrapper<LotteryDrawActivityShare>()
                        .eq(LotteryDrawActivityShare::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityShare::getUserId, userId)
                        .between(LotteryDrawActivityShare::getShareTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));
                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId)
                        .between(LotteryDrawActivityAwardRecord::getAwardTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));

                int shareTimes = shareList.size();
                int userTimes = awardRecords.size();
//                if (userTimes == (drawTimes+shareTimes)){
                if (userTimes >= (drawTimes+shareTimes)){
                    throw new LuckException("您已达本次活动抽奖上限");
                }
            }
            if (drawType == 2 && drawTimes>0){
                List<LotteryDrawActivityShare> shareList = lotteryDrawActivityShareService.list(new LambdaQueryWrapper<LotteryDrawActivityShare>()
                        .eq(LotteryDrawActivityShare::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityShare::getUserId, userId));

                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId));

                int shareTimes = shareList.size();
                int userTimes = awardRecords.size();
//                if (userTimes == (drawTimes+shareTimes)){
                if (userTimes >= (drawTimes+shareTimes)){
                    throw new LuckException("您已达本次活动抽奖上限");
                }
            }
        }

        //判断用户是否需要使用积分
        if (usePointType == 2){
            if (drawType == 1){
                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId)
                        .between(LotteryDrawActivityAwardRecord::getAwardTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));
                int userTimes = awardRecords.size();
                if (userTimes < drawTimes){
                    usePoint = 0;
                }
            }
            if (drawType == 2){
                List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId));
                int userTimes = awardRecords.size();
                if (userTimes < drawTimes){
                    usePoint = 0;
                }
            }
        }
      return ServerResponseEntity.success(usePoint);
    }

    private boolean reduceStock(Integer prizeId){
        String redisKey = "lottery::prize::stock::"+prizeId;

        Long decr = RedisUtil.decr(redisKey, 1);

        if (decr < 0){
            RedisUtil.incr(redisKey, 1);
            return false;
        }

        lotteryDrawActivityPrizeService.reduceStock(prizeId);
        return true;
    }


    private DrawPrizeVO randomPrize(List<LotteryDrawActivityPrize> prizes,Long userId,Integer activityId,String mobile,String nickName,String shopName,Long shopId,Integer drawTimes,List<LotteryDrawActivityPrize> lotteryDrawActivityPrizes){
        List<LotteryDrawActivityPrize> randomPrizes = new ArrayList<>();
        Collections.shuffle(prizes);
        prizes.forEach(temp->{
            Integer onlyOneSwitch = temp.getOnlyOneSwitch();
            Integer id = temp.getId();
            Integer prizeStock = temp.getPrizeStock();
            Integer bigAwardSwitch = temp.getBigAwardSwitch();
            Integer bigAwardType = temp.getBigAwardType();
            String bigAwardTime = temp.getBigAwardTime();


            if (1==onlyOneSwitch){
                List<LotteryDrawActivityAwardRecord> list = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                        .eq(LotteryDrawActivityAwardRecord::getLotteryDrawPrizeId, id)
                        .eq(LotteryDrawActivityAwardRecord::getUserId, userId));

                if (CollectionUtil.isEmpty(list) && prizeStock>0){
                    if (1==bigAwardSwitch){
                        if (2==bigAwardType){
                            List<String> dateStrList = Arrays.asList(bigAwardTime.split(StringPool.COMMA));
                            Date date = new Date();
                            for (String s : dateStrList) {
                                DateTime parse = DateUtil.parse(s);
                                boolean sameDay = DateUtil.isSameDay(parse, date);
                                if (sameDay){
                                    randomPrizes.add(temp);
                                }
                            }
                        }
                    }else {
                        randomPrizes.add(temp);
                    }

                }
            }else {
                if (prizeStock>0){
                    if (1==bigAwardSwitch){
                        if (2==bigAwardType){
                            List<String> dateStrList = Arrays.asList(bigAwardTime.split(StringPool.COMMA));
                            Date date = new Date();
                            for (String s : dateStrList) {
                                DateTime parse = DateUtil.parse(s);
                                boolean sameDay = DateUtil.isSameDay(parse, date);
                                if (sameDay){
                                    randomPrizes.add(temp);
                                }
                            }
                        }
                    }else {
                        randomPrizes.add(temp);
                    }
                }
            }
        });

        if (CollectionUtil.isEmpty(randomPrizes)){

            //查询是否拥有阳光普照奖
            List<LotteryDrawActivityPrize> sunPrize = lotteryDrawActivityPrizes.stream().filter(temp -> temp.getSunshineAwardSwitch() == 1).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sunPrize)){
                LotteryDrawActivityPrize lotteryDrawActivityPrize = sunPrize.get(0);

                Integer prizeId = lotteryDrawActivityPrize.getId();
                String prizeImgs = null;
                if (StrUtil.isNotBlank(lotteryDrawActivityPrize.getPrizeImgs())) {
                    prizeImgs = lotteryDrawActivityPrize.getPrizeImgs();
                }
                String prizeName = lotteryDrawActivityPrize.getPrizeName();
                Integer pointNum = lotteryDrawActivityPrize.getPointNum();
                Long couponId = lotteryDrawActivityPrize.getCouponId();
                Integer prizeType = lotteryDrawActivityPrize.getPrizeType();
                Integer prizeStatus = 2;
                if (1==prizeType){
                    //赠送券
                    ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                    receiveCouponDTO.setCouponId(couponId);
                    receiveCouponDTO.setActivityId(Long.valueOf(activityId));
                    receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                    receiveCouponDTO.setUserId(userId);
                    tCouponFeignClient.receive(receiveCouponDTO);
                }else if (2==prizeType){
                    UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                    updateScoreDTO.setUserId(userId);
                    updateScoreDTO.setPoint_value(pointNum);
                    updateScoreDTO.setSource("抽奖活动赠送积分");
                    updateScoreDTO.setPoint_channel("wechat");
                    updateScoreDTO.setPoint_type("SKX_JLJF");
                    updateScoreDTO.setRemark("抽奖活动赠送积分");
                    updateScoreDTO.setIoType(1);
                    crmUserFeignClient.updateScore(updateScoreDTO);
                    log.info("drawActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
                }else if (3 == prizeType) {
                    prizeStatus = 0;
                }
                LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                        .lotteryDrawId(activityId)
                        .prizeName(prizeName)
                        .awardType(prizeType)
                        .couponId(couponId)
                        .pointNum(pointNum)
                        .lotteryDrawPrizeId(prizeId)
                        .mobile(mobile)
                        .nickname(nickName)
                        .shopId(shopId)
                        .shopName(shopName)
                        .status(prizeStatus)
                        .userId(userId).build();
                lotteryDrawActivityAwardRecordService.save(awardRecord);
                DrawPrizeVO result = DrawPrizeVO.builder()
                        .prizeImgs(prizeImgs)
                        .prizeName(prizeName).build();
                return result;
            }else {
                noDraw(userId, activityId, mobile, nickName, shopName, shopId, null, null, null, null, drawTimes);

                throw new LuckException("很抱歉你未中奖！");
            }

        }
        int sum = randomPrizes.stream().mapToInt(LotteryDrawActivityPrize::getPrizeStock).sum();
        log.info("随机数:{},奖品池数量:{}",sum,randomPrizes.size());

        int randomInt = RandomUtil.randomInt(1, sum + 1);

        LotteryDrawActivityPrize awardPrize = null;
        int tempCount = 0;
        for (LotteryDrawActivityPrize prize:
        randomPrizes) {
            log.info("当前奖品数据:{}",JSONObject.toJSONString(prize));
            Integer prizeStock = prize.getPrizeStock();
            randomInt -= tempCount;
            tempCount += prizeStock;
            if (randomInt <= prizeStock){
                awardPrize = prize;
                break;
            }
        }
        if (null == awardPrize){
            //查询是否拥有阳光普照奖
            List<LotteryDrawActivityPrize> sunPrize = lotteryDrawActivityPrizes.stream().filter(temp -> temp.getSunshineAwardSwitch() == 1).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sunPrize)){
                LotteryDrawActivityPrize lotteryDrawActivityPrize = sunPrize.get(0);

                Integer prizeId = lotteryDrawActivityPrize.getId();
                String prizeName = lotteryDrawActivityPrize.getPrizeName();
                String prizeImgs = null;
                if (StrUtil.isNotBlank(lotteryDrawActivityPrize.getPrizeImgs())) {
                    prizeImgs = lotteryDrawActivityPrize.getPrizeImgs();
                }
                Integer pointNum = lotteryDrawActivityPrize.getPointNum();
                Long couponId = lotteryDrawActivityPrize.getCouponId();
                Integer prizeType = lotteryDrawActivityPrize.getPrizeType();
                Integer prizeStatus = 2;
                if (1==prizeType){
                    //赠送券
                    ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                    receiveCouponDTO.setCouponId(couponId);
                    receiveCouponDTO.setActivityId(Long.valueOf(activityId));
                    receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                    receiveCouponDTO.setUserId(userId);
                    tCouponFeignClient.receive(receiveCouponDTO);
                }else if (2==prizeType){
                    UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                    updateScoreDTO.setUserId(userId);
                    updateScoreDTO.setPoint_value(pointNum);
                    updateScoreDTO.setSource("抽奖活动赠送积分");
                    updateScoreDTO.setPoint_channel("wechat");
                    updateScoreDTO.setPoint_type("SKX_JLJF");
                    updateScoreDTO.setRemark("抽奖活动赠送积分");
                    updateScoreDTO.setIoType(1);
                    crmUserFeignClient.updateScore(updateScoreDTO);
                    log.info("drawActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
                }else if (3 == prizeType) {
                    prizeStatus = 0;
                }
                LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                        .lotteryDrawId(activityId)
                        .prizeName(prizeName)
                        .awardType(prizeType)
                        .couponId(couponId)
                        .pointNum(pointNum)
                        .lotteryDrawPrizeId(prizeId)
                        .mobile(mobile)
                        .nickname(nickName)
                        .shopId(shopId)
                        .shopName(shopName)
                        .status(prizeStatus)
                        .userId(userId).build();
                lotteryDrawActivityAwardRecordService.save(awardRecord);
                DrawPrizeVO result = DrawPrizeVO.builder()
                        .prizeImgs(prizeImgs)
                        .prizeName(prizeName).build();
                return result;
            }else {
                noDraw(userId,activityId,mobile,nickName,shopName,shopId,null,null,null,null,drawTimes);

                throw new LuckException("很抱歉你未中奖！");
            }

        }


        Integer prizeType = awardPrize.getPrizeType();
        String awardPrizeImgs = null;
        if (StrUtil.isNotBlank(awardPrize.getPrizeImgs())) {
            awardPrizeImgs = awardPrize.getPrizeImgs();
        }
        Long couponId = awardPrize.getCouponId();
        Integer pointNum = awardPrize.getPointNum();
        String prizeName = awardPrize.getPrizeName();
        Integer id = awardPrize.getId();

        //若库存不足，则直接未中奖
        boolean b = reduceStock(id);
        if (!b){
            //查询是否拥有阳光普照奖
            List<LotteryDrawActivityPrize> sunPrize = lotteryDrawActivityPrizes.stream().filter(temp -> temp.getSunshineAwardSwitch() == 1).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sunPrize)){
                LotteryDrawActivityPrize lotteryDrawActivityPrize = sunPrize.get(0);
                String prizeImgs = null;
                if (StrUtil.isNotBlank(lotteryDrawActivityPrize.getPrizeImgs())) {
                    prizeImgs = lotteryDrawActivityPrize.getPrizeImgs();
                }
                Integer prizeId = lotteryDrawActivityPrize.getId();
                Integer prizeStatus = 2;
                if (1==lotteryDrawActivityPrize.getPrizeType()){
                    //赠送券
                    ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
                    receiveCouponDTO.setCouponId(lotteryDrawActivityPrize.getCouponId());
                    receiveCouponDTO.setActivityId(Long.valueOf(activityId));
                    receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
                    receiveCouponDTO.setUserId(userId);
                    tCouponFeignClient.receive(receiveCouponDTO);
                }else if (2==lotteryDrawActivityPrize.getPrizeType()){
                    UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
                    updateScoreDTO.setUserId(userId);
                    updateScoreDTO.setPoint_value(lotteryDrawActivityPrize.getPointNum());
                    updateScoreDTO.setSource("抽奖活动赠送积分");
                    updateScoreDTO.setPoint_channel("wechat");
                    updateScoreDTO.setPoint_type("SKX_JLJF");
                    updateScoreDTO.setRemark("抽奖活动赠送积分");
                    updateScoreDTO.setIoType(1);
                    crmUserFeignClient.updateScore(updateScoreDTO);
                    log.info("drawActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
                }else if (3 == lotteryDrawActivityPrize.getPrizeType()) {
                    prizeStatus = 0;
                }
                LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                        .lotteryDrawId(activityId)
                        .prizeName(lotteryDrawActivityPrize.getPrizeName())
                        .awardType(lotteryDrawActivityPrize.getPrizeType())
                        .couponId(lotteryDrawActivityPrize.getCouponId())
                        .pointNum(lotteryDrawActivityPrize.getPointNum())
                        .lotteryDrawPrizeId(prizeId)
                        .mobile(mobile)
                        .nickname(nickName)
                        .shopId(shopId)
                        .shopName(shopName)
                        .status(prizeStatus)
                        .userId(userId).build();
                lotteryDrawActivityAwardRecordService.save(awardRecord);
                DrawPrizeVO result = DrawPrizeVO.builder()
                        .prizeImgs(prizeImgs)
                        .prizeName(lotteryDrawActivityPrize.getPrizeName()).build();
                return result;
            }else {
                noDraw(userId,activityId,mobile,nickName,shopName,shopId,prizeName,couponId,pointNum,id,drawTimes);

                throw new LuckException("很抱歉你未中奖！");
            }
        }
        Integer status = 2;
        if (1==prizeType){
            //赠送券
            ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
            receiveCouponDTO.setCouponId(couponId);
            receiveCouponDTO.setActivityId(Long.valueOf(activityId));
            receiveCouponDTO.setActivitySource(ActivitySourceEnum.DRAW_ACTIVITY.value());
            receiveCouponDTO.setUserId(userId);
            tCouponFeignClient.receive(receiveCouponDTO);

        }else if (2==prizeType){
            UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
            updateScoreDTO.setUserId(userId);
            updateScoreDTO.setPoint_value(pointNum);
            updateScoreDTO.setSource("抽奖活动赠送积分");
            updateScoreDTO.setPoint_channel("wechat");
            updateScoreDTO.setPoint_type("SKX_JLJF");
            updateScoreDTO.setRemark("抽奖活动赠送积分");
            updateScoreDTO.setIoType(1);
            crmUserFeignClient.updateScore(updateScoreDTO);
            log.info("drawActivity giftPoint param {}", JSONObject.toJSONString(updateScoreDTO));
        }else if (3==prizeType) {
            status = 0;
        }



        LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                .lotteryDrawId(activityId)
                .prizeName(prizeName)
                .awardType(prizeType)
                .couponId(couponId)
                .pointNum(pointNum)
                .lotteryDrawPrizeId(id)
                .mobile(mobile)
                .nickname(nickName)
                .shopId(shopId)
                .shopName(shopName)
                .status(status)
                .userId(userId).build();
        lotteryDrawActivityAwardRecordService.save(awardRecord);
        DrawPrizeVO result = DrawPrizeVO.builder()
                .prizeImgs(awardPrizeImgs)
                .prizeName(prizeName).build();
        return result;
    }

    private void noDraw(Long userId,Integer activityId,
                        String userMobile,String nickName,
                        String shopName,Long shopId,
                        String prizeName,Long couponId,Integer pointNum,Integer prizeId,Integer drawTimes){

//        if(Objects.nonNull(drawTimes) && drawTimes>0){
//            log.info("抽奖次数限制(为0不限制，大于0限制) {}",drawTimes);
//            return;
//        }

        LotteryDrawActivityAwardRecord awardRecord = LotteryDrawActivityAwardRecord.builder()
                .lotteryDrawId(activityId)
                .prizeName(prizeName)
                .awardType(0)
                .couponId(null)
                .pointNum(null)
                .lotteryDrawPrizeId(null)
                .mobile(userMobile)
                .nickname(nickName)
                .shopId(shopId)
                .shopName(shopName)
                .userId(userId).build();
        lotteryDrawActivityAwardRecordService.save(awardRecord);
    }

    @Override
    public ServerResponseEntity<List<DrawAwardVO>> drawAward(Integer id) {
//        Long userId = AuthUserContext.get().getUserId();
        List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
                .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
//                .eq(LotteryDrawActivityAwardRecord::getUserId, userId)
                .ne(LotteryDrawActivityAwardRecord::getAwardType, 0)
                .orderByDesc(LotteryDrawActivityAwardRecord::getAwardTime)
                .last("limit 0,20")
        );

        List<DrawAwardVO> drawAwardVOS = Convert.toList(DrawAwardVO.class, awardRecords);
        drawAwardVOS.forEach(vo -> {
                if(vo.getAwardType().equals(3)){
                    LotteryDrawActivityPrize byId = lotteryDrawActivityPrizeService.getById(vo.getLotteryDrawPrizeId());
                    vo.setPrizeRule(byId.getPrizeRule());
                }
        });
//        drawAwardVOS.forEach(vo -> {
//            LotteryDrawActivityPrize byId = lotteryDrawActivityPrizeService.getById(vo.getLotteryDrawPrizeId());
//            if (byId != null && StrUtil.isNotBlank(byId.getPrizeImgs())) {
//                vo.setPrizeImgs(byId.getPrizeImgs());
//            }
//        });
        return ServerResponseEntity.success(drawAwardVOS);
    }

    @Override
    public ServerResponseEntity<List<DrawAwardVO>> myAward(Integer id) {
        Long userId = AuthUserContext.get().getUserId();
        List<LotteryDrawActivityAwardRecord> awardRecords = lotteryDrawActivityAwardRecordService.list(new LambdaQueryWrapper<LotteryDrawActivityAwardRecord>()
//                .eq(LotteryDrawActivityAwardRecord::getLotteryDrawId, id)
                .eq(LotteryDrawActivityAwardRecord::getUserId, userId)
                .ne(LotteryDrawActivityAwardRecord::getAwardType, 0)
                .orderByDesc(LotteryDrawActivityAwardRecord::getAwardTime));

        List<DrawAwardVO> drawAwardVOS = Convert.toList(DrawAwardVO.class, awardRecords);
        drawAwardVOS.forEach(vo -> {
            LotteryDrawActivityPrize byId = lotteryDrawActivityPrizeService.getById(vo.getLotteryDrawPrizeId());
            if (byId != null && StrUtil.isNotBlank(byId.getPrizeImgs())) {
                vo.setPrizeImgs(byId.getPrizeImgs());
            }
            LotteryDrawActivity lotteryDrawActivity = lotteryDrawActivityService.getById(vo.getLotteryDrawId());
            if (lotteryDrawActivity != null && StrUtil.isNotBlank(lotteryDrawActivity.getActivityName())) {
                vo.setActivityName(lotteryDrawActivity.getActivityName());
            }

            if(vo.getAwardType().equals(3)){
                vo.setPrizeRule(byId.getPrizeRule());
            }

        });
        return ServerResponseEntity.success(drawAwardVOS);
    }

    @Override
    public ServerResponseEntity<Void> updateRecord(LotteryDrawActivityAwardRecord lotteryDrawActivityAwardRecord) {
        if (lotteryDrawActivityAwardRecord != null) {
            if (lotteryDrawActivityAwardRecord.getId() == null || StrUtil.isBlank(lotteryDrawActivityAwardRecord.getPhone()) || StrUtil.isBlank(lotteryDrawActivityAwardRecord.getUserAddr())) {
                throw new LuckException("接收数据不正确！");
            }
        } else {
            throw new LuckException("接收数据不正确！");
        }
        LotteryDrawActivityAwardRecord byId = lotteryDrawActivityAwardRecordService.getById(lotteryDrawActivityAwardRecord.getId());
        if (byId != null) {
            if (byId.getAwardType() != 3) {
                throw new LuckException("该奖品不是实物，不用填写收货地址！");
            }
            if (byId.getStatus() == 2) {
                throw new LuckException("该奖品已发货，不能修改地址！");
            }
            lotteryDrawActivityAwardRecord.setStatus(1);
            lotteryDrawActivityAwardRecordService.updateById(lotteryDrawActivityAwardRecord);
        } else {
            throw new LuckException("未查询到此中奖记录！");
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateLogistics(LotteryDrawActivityAwardRecord lotteryDrawActivityAwardRecord) {
        if (lotteryDrawActivityAwardRecord != null) {
            if (lotteryDrawActivityAwardRecord.getId() == null || StrUtil.isBlank(lotteryDrawActivityAwardRecord.getLogisticsNo())) {
                throw new LuckException("接收数据不正确！");
            }
        } else {
            throw new LuckException("接收数据不正确！");
        }
        LotteryDrawActivityAwardRecord byId = lotteryDrawActivityAwardRecordService.getById(lotteryDrawActivityAwardRecord.getId());
        if (byId != null) {
            if (byId.getAwardType() != 3) {
                throw new LuckException("该奖品不是实物，不用填写收货地址！");
            }
            if (byId.getStatus() == 2) {
                throw new LuckException("该奖品已发货！");
            }
            lotteryDrawActivityAwardRecord.setStatus(2);
            lotteryDrawActivityAwardRecordService.updateById(lotteryDrawActivityAwardRecord);
        } else {
            throw new LuckException("未查询到此中奖记录！");
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> share(Integer id) {
        Long userId = AuthUserContext.get().getUserId();
        LotteryDrawActivity lotteryDrawActivity = lotteryDrawActivityService.getById(id);
        if (lotteryDrawActivity == null) {
            return ServerResponseEntity.success();
        }
        Date date = new Date();
        if (lotteryDrawActivity.getShareSwitch().equals(1)){
            List<LotteryDrawActivityShare> shareList = lotteryDrawActivityShareService.list(new LambdaQueryWrapper<LotteryDrawActivityShare>()
                    .eq(LotteryDrawActivityShare::getLotteryDrawId, id)
                    .eq(LotteryDrawActivityShare::getUserId, userId)
                    .between(LotteryDrawActivityShare::getShareTime, DateUtil.beginOfDay(date), DateUtil.endOfDay(date)));
            if ((shareList != null && lotteryDrawActivity.getShareTimes() != null && shareList.size() < lotteryDrawActivity.getShareTimes()) || lotteryDrawActivity.getShareTimes() == null || lotteryDrawActivity.getShareTimes().equals(0)) {
                LotteryDrawActivityShare lotteryDrawActivityShare = new LotteryDrawActivityShare();
                lotteryDrawActivityShare.setLotteryDrawId(id);
                lotteryDrawActivityShare.setUserId(userId);
                lotteryDrawActivityShare.setShareTime(date);
                lotteryDrawActivityShareService.save(lotteryDrawActivityShare);
            }
        }
        return ServerResponseEntity.success();
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userId", condition = "#userId!=null")
    })
    public void clearUserPermissionsCache(Long userId) {
    }
}
