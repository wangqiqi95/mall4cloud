package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.auth.vo.AuthAccountVO;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.coupon.dto.SyncPointConvertCouponDto;
import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.api.coupon.vo.PaperCouponOrderVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderVo;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.api.docking.skq_crm.dto.*;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCouponFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponFreezeThawResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponReleaseResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponSingleGetResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CouponGetVo;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.MyCouponStatus;
import com.mall4j.cloud.coupon.constant.TCouponStatus;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.mapper.*;
import com.mall4j.cloud.coupon.model.*;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.TCouponActivityCentreService;
import com.mall4j.cloud.coupon.service.TCouponService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


@Slf4j
@Service
public class TCouponUserServiceImpl implements TCouponUserService {
    @Resource
    private TCouponMapper tCouponMapper;
    @Resource
    private TCouponUserMapper tCouponUserMapper;
    @Resource
    private TCouponShopMapper tCouponShopMapper;
    @Resource
    private CrmCouponFeignClient crmCouponFeignClient;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private StaffFeignClient staffFeignClient;
    @Resource
    private TCouponCodeMapper tCouponCodeMapper;
    @Resource
    private CouponService couponService;
    @Resource
    private CouponMapper couponMapper;
    @Resource
    private TCouponCategoryMapper tCouponCategoryMapper;
    @Resource
    private CrmCouponTokenMapper crmCouponTokenMapper;
    @Resource
    private TCouponCommodityMapper tCouponCommodityMapper;
    @Resource
    private StoreFeignClient storeFeignClient;
    @Resource
    private AccountFeignClient accountFeignClient;
    @Resource
    private OnsMQTemplate sendMaSubcriptMessageTemplate;
    @Resource
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    OrderFeignClient orderFeignClient;
    @Resource
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    TCouponActivityCentreService couponActivityCentreService;
    @Autowired
    TCouponService tCouponService;

    @Override
    public ServerResponseEntity<Void> receive(ReceiveCouponDTO param) {
        log.info("调用发券借口：{}",JSONObject.toJSONString(param));
        TCouponUser tCouponUser = tCouponUser(param);
        if (!ObjectUtil.isEmpty(tCouponUser)){
            tCouponUserMapper.insert(tCouponUser);
        }

        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> batchReceive(BatchReceiveCouponDTO param) {
        ArrayList<TCouponUser> tCouponUsers = new ArrayList<>();
        param.getCouponIds().forEach(temp ->{
            ReceiveCouponDTO receiveCouponDTO = BeanUtil.copyProperties(param, ReceiveCouponDTO.class);
            receiveCouponDTO.setCouponId(temp);
            TCouponUser tCouponUser = tCouponUser(receiveCouponDTO);
            if (ObjectUtil.isNotEmpty(tCouponUser)){
                tCouponUsers.add(tCouponUser);
            }
        });

        if (tCouponUsers.size() > 0){
            tCouponUserMapper.insertBatch(tCouponUsers);
        }
        return ServerResponseEntity.success();
    }

//    @HystrixCommand(ignoreExceptions = LuckException.class)
//    @HystrixCommand(ignoreExceptions = LuckException.class,commandProperties = {
//            //2秒钟以内就是正常的业务逻辑
//            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="100")})
    @Override
    public ServerResponseEntity<PageInfo<MyCouponListVO>> myCouponList(AppCouponListDTO param) {
        //过去365天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 365);
        Date d = c.getTime();
        String startDay = format.format(d);

        //小程序用户券记录
        PageInfo<MyCouponListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                tCouponUserMapper.list(param.getType(),param.getUserId(),d)
        );
        List<MyCouponListVO> list = result.getList();
        log.info("===========分页查询的数据：{}",JSONObject.toJSONString(list));

        //crm用户券记录
        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(param.getUserId());
        if (user.isFail()){
            log.info("用户信息查询异常:{}", user.getMsg());
        }else {
            log.info("用户手机号为:{}", user.getData().getPhone());
        }
        CouponGetDto couponGetDto = new CouponGetDto();
        couponGetDto.setPage_index(param.getPageNo());
        couponGetDto.setPage_size(param.getPageSize());
        couponGetDto.setMobile(user.getData().getPhone());
        couponGetDto.setStatus(param.getType());
        couponGetDto.setBegin_time(startDay);
        couponGetDto.setEnd_time(format.format(new Date()));
        log.info("调用crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
        ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList = crmCouponFeignClient.couponGet(couponGetDto);
        log.info("调用crm查询用户优惠券记录接口返回值为：{}", JSONObject.toJSONString(couponGetList));
        if (couponGetList.isSuccess()){
            CrmPageResult<List<CouponGetVo>> data = couponGetList.getData();
            //合并集合
            result.setTotal(result.getTotal() + data.getTotal_count());
            List<CouponGetVo> crmCouponList = data.getJsondata();
            if (!CollectionUtils.isEmpty(crmCouponList)){
                crmCouponList.forEach(temp ->{
                    //CRM优惠券id
                    String coupon_rule_id = temp.getCoupon_rule_id();
                    //缓存中获取优惠券。
                    TCoupon tCoupon = tCouponService.queryByCrmCouponId(coupon_rule_id);
//                    TCoupon tCoupon = tCouponMapper.selectOne(new LambdaQueryWrapper<TCoupon>().eq(TCoupon::getCrmCouponId, coupon_rule_id));

                    if (ObjectUtil.isNotEmpty(tCoupon)){
                        MyCouponListVO myCouponListVO = new MyCouponListVO();
                        myCouponListVO.setCouponId(tCoupon.getId());
                        myCouponListVO.setCoverUrl(tCoupon.getCoverUrl());
                        myCouponListVO.setDescription(tCoupon.getDescription());
                        myCouponListVO.setApplyScopeType(tCoupon.getApplyScopeType());
                        myCouponListVO.setSourceType(tCoupon.getSourceType());
                        myCouponListVO.setIsAllShop(tCoupon.getIsAllShop());
                        if (tCoupon.getCouponPicture() != null) {
                            myCouponListVO.setCouponPicture(tCoupon.getCouponPicture());
                        }
                        if (!myCouponListVO.getIsAllShop()){
                            List<TCouponShop> couponShops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, myCouponListVO.getCouponId()));
                            List<Long> shops = couponShops.stream().map(TCouponShop::getShopId).collect(toList());
                            myCouponListVO.setShops(shops);
                        }
                        myCouponListVO.setCouponCode(temp.getCoupon_code());
                        myCouponListVO.setName(temp.getCoupon_name());
                        //使用效果，DISCOUNT:折扣,CASH_COUPON:代金
                        if (temp.getUse_effect().equals("DISCOUNT")){
                            myCouponListVO.setType((short) 1);
                            myCouponListVO.setCouponDiscount(temp.getDiscount().multiply(new BigDecimal(100)));
                        }
                        if (temp.getUse_effect().equals("CASH_COUPON")){
                            myCouponListVO.setType((short) 0);
                            myCouponListVO.setReduceAmount(temp.getMoney().multiply(new BigDecimal(100)));
                        }
                        try {
                            if (StringUtils.isNotEmpty(temp.getBegin_time())){
                                myCouponListVO.setStartTime(format.parse(temp.getBegin_time()));
                            }
                            if (StringUtils.isNotEmpty(temp.getEnd_time())){
                                myCouponListVO.setEndTime(format.parse(temp.getEnd_time()));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        list.add(myCouponListVO);
                    }
                });
            }
        }else {
            log.info("CRM优惠券列表查询异常:{}", couponGetList.getMsg());
        }

        //优惠券使用场景
        if (!CollectionUtils.isEmpty(result.getList())){
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
                        scene.add("官方商城");
                        scene.add("官方小程序");

                        if (temp.getApplyScopeType() == 0){
                            scene.add("指定线下门店");
                            scene.add("指定门店小程序");
                        }else if (temp.getApplyScopeType() == 1){
                            scene.add("指定门店小程序");
                        }else {
                            scene.add("指定线下门店");
                        }

                    }else {
                        if(CollectionUtil.isNotEmpty(temp.getShops())){
                            if(temp.getShops().contains(Constant.MAIN_SHOP)){
                                scene.add("官方小程序");
                            }
                            if (temp.getShops().contains(Constant.WEBSITE_SHOP)){
                                scene.add("官方商城");
                            }

                            List<Long> otherShops = new ArrayList<>(temp.getShops());
                            otherShops.remove(Constant.MAIN_SHOP);
                            otherShops.remove(Constant.WEBSITE_SHOP);
                            //是否包含 除自营店id或者官方商城id 之外的门店
                            if(CollectionUtil.isNotEmpty(otherShops)){
                                if (temp.getApplyScopeType() == 0){
                                    scene.add("指定线下门店");
                                    scene.add("指定门店小程序");
                                }else if (temp.getApplyScopeType() == 1){
                                    scene.add("指定门店小程序");
                                }else {
                                    scene.add("指定线下门店");
                                }
                            }
                        }
                    }
                }
                temp.setScene(scene);
            });
        }

        log.info("优惠券查询返回值：{}",JSONObject.toJSONString(result.getList()));

        if (CollectionUtil.isNotEmpty(result.getList())){
            List<MyCouponListVO> listVOS = result.getList().stream()
                    .sorted(Comparator.comparing(MyCouponListVO::getEndTime,Comparator.nullsLast(Date::compareTo)))
                    .collect(toList());
            result.setList(listVOS);
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageInfo<UserCouponListVO>> userCouponList(AppCouponListDTO param) {
        //过去365天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 365);
        Date d = c.getTime();
        String startDay = format.format(d);

        //小程序用户券记录
        PageInfo<UserCouponListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                tCouponUserMapper.platformList(param.getType(),param.getUserId(),d)
        );
        List<UserCouponListVO> list = result.getList();
        log.info("===========分页查询的数据：{}",JSONObject.toJSONString(list));

        //crm用户券记录
        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(param.getUserId());
        if (user.isFail()){
            log.info("用户信息查询异常:{}", user.getMsg());
        }else {
            log.info("用户手机号为:{}", user.getData().getPhone());
        }

        CouponGetDto couponGetDto = new CouponGetDto();
        couponGetDto.setPage_index(param.getPageNo());
        couponGetDto.setPage_size(param.getPageSize());
        couponGetDto.setMobile(user.getData().getPhone());
        couponGetDto.setStatus(param.getType());
        couponGetDto.setBegin_time(startDay);
        couponGetDto.setEnd_time(format.format(new Date()));

        log.info("调用crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
        ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList = crmCouponFeignClient.couponGet(couponGetDto);
        log.info("调用crm查询用户优惠券记录接口返回值为：{}", JSONObject.toJSONString(couponGetList));

        if (couponGetList.isSuccess()){
            CrmPageResult<List<CouponGetVo>> data = couponGetList.getData();
            //合并集合
            result.setTotal(result.getTotal() + data.getTotal_count());
            List<CouponGetVo> crmCouponList = data.getJsondata();
            if (!CollectionUtils.isEmpty(crmCouponList)){
                crmCouponList.forEach(temp ->{
                    //CRM优惠券id
                    String coupon_rule_id = temp.getCoupon_rule_id();
                    TCoupon tCoupon = tCouponMapper.selectOne(new LambdaQueryWrapper<TCoupon>().eq(TCoupon::getCrmCouponId, coupon_rule_id));
                    if (ObjectUtil.isNotEmpty(tCoupon)){
                        UserCouponListVO userCouponListVO = new UserCouponListVO();
                        userCouponListVO.setCouponId(tCoupon.getId());
                        userCouponListVO.setAmountLimitType(tCoupon.getAmountLimitType());
                        userCouponListVO.setAmountLimitNum(tCoupon.getAmountLimitNum());
                        userCouponListVO.setName(temp.getCoupon_name());
                        userCouponListVO.setKind((short)0);
                        //使用效果，DISCOUNT:折扣,CASH_COUPON:代金
                        if (temp.getUse_effect().equals("DISCOUNT")){
                            userCouponListVO.setType((short) 1);
                        }
                        if (temp.getUse_effect().equals("CASH_COUPON")){
                            userCouponListVO.setType((short) 0);
                        }
                        // 券状态，VALID:可使用，EXPIRED：已过期，USED：已使用
                        if (StrUtil.isNotBlank(temp.getStatus())){
                            if (temp.getStatus().equals("VALID")) {
                                userCouponListVO.setStatus(1);
                            } else if (temp.getStatus().equals("EXPIRED")) {
                                userCouponListVO.setStatus(0);
                            } else if (temp.getStatus().equals("USED")) {
                                userCouponListVO.setStatus(2);
                            }
                        }

                        try {
                            userCouponListVO.setStartTime(format.parse(temp.getBegin_time()));
                            if (ObjectUtil.isNotEmpty(temp.getEnd_time())){
                                userCouponListVO.setEndTime(format.parse(temp.getEnd_time()));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        list.add(userCouponListVO);
                    }
                });
            }
        }else {
            log.info("CRM优惠券列表查询异常:{}", couponGetList.getMsg());
        }

        log.info("优惠券查询返回值：{}",JSONObject.toJSONString(result.getList()));

        return ServerResponseEntity.success(result);
    }

//    @HystrixCommand(ignoreExceptions = LuckException.class)
    @Override
    public ServerResponseEntity<MyCouponDetailVO> myCouponDetail(Long id) {
        MyCouponDetailVO result = tCouponUserMapper.detail(id);

        // ============  优惠券使用场景
        // 官方小程序：小程序后台创建的券或 crm 创建有官方小程序门店code；
        // 官网：由crm创建，且适用门店 code 有 官方商城门店 code；
        // 线下指定门店：由crm创建，包含了其他任何一个门店code，是否适用线上字段为【否】；
        // 指定门店小程序：由crm创建，包含了其他任何一个门店code，是否适用线上字段为【是】；
        List<String> scene = new ArrayList<>();
        if (result.getSourceType() == 1){
            scene.add("官方小程序");
        }else if (result.getSourceType() == 2){
            if (result.getIsAllShop()){
                scene.add("官方商城");
                scene.add("官方小程序");

                if (result.getApplyScopeType() == 0){
                    scene.add("指定线下门店");
                    scene.add("指定门店小程序");
                }else if (result.getApplyScopeType() == 1){
                    scene.add("指定门店小程序");
                }else {
                    scene.add("指定线下门店");
                }
            }else {
                List<TCouponShop> shops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, result.getCouponId()));
                List<Long> shopIds = shops.stream().map(TCouponShop::getShopId).collect(toList());
                result.setShops(shopIds);
                result.setShopNum(shops.size());
                if (shopIds.contains(Constant.MAIN_SHOP)){
                    scene.add("官方小程序");
                }
                if (shopIds.contains(Constant.WEBSITE_SHOP)){
                    scene.add("官方商城");
                }

                List<Long> otherShops = new ArrayList<>(shopIds);
                otherShops.remove(Constant.MAIN_SHOP);
                otherShops.remove(Constant.WEBSITE_SHOP);
                //是否包含 除自营店id或者官方商城id 之外的门店
                if(CollectionUtil.isNotEmpty(otherShops)){
                    if (result.getApplyScopeType() == 0){
                        scene.add("指定线下门店");
                        scene.add("指定门店小程序");
                    }else if (result.getApplyScopeType() == 1){
                        scene.add("指定门店小程序");
                    }else {
                        scene.add("指定线下门店");
                    }
                }
            }
        }
        result.setScene(scene);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageInfo<MyCouponListVO>> getCouponListByBatchId(BatchCouponListDTO param) {
        log.info("登陆态信息为：{}", JSONObject.toJSONString(AuthUserContext.get()));
        PageInfo<MyCouponListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                tCouponUserMapper.getCouponListByBatchId(param.getBatchId(),param.getUserId())
        );

        List<MyCouponListVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                List<String> scene = new ArrayList<>();
                if (temp.getSourceType() == 1){
                    scene.add("官方小程序");
                }else if (temp.getSourceType() == 2){
                    if (temp.getIsAllShop()){
                        scene.add("官网");
                        scene.add("官方小程序");
                    }else {
                        List<TCouponShop> shops = tCouponShopMapper.selectList(new LambdaQueryWrapper<TCouponShop>().eq(TCouponShop::getCouponId, temp.getCouponId()));
                        List<Long> shopIds = shops.stream().map(TCouponShop::getShopId).collect(toList());
                        temp.setShops(shopIds);
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
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> writeOff(String code,Short type,Long userId) {
        if (type == 1){
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserData(userId);
            if (userData != null && userData.isSuccess()) {
                WriteOffDetailVO writeOffDetailVO = tCouponUserMapper.writeOffCouponDetail(code);
                CouponStateUpdateDto couponStateUpdateDto = new CouponStateUpdateDto();
                couponStateUpdateDto.setCoupon_id(writeOffDetailVO.getCouponId()+"");
                couponStateUpdateDto.setCoupon_code(writeOffDetailVO.getCouponCode());
                couponStateUpdateDto.setMobile(userData.getData().getPhone());
                couponStateUpdateDto.setOper("writeOff");
                log.info("调用crm核销(小程序)券接口的参数为：{}",JSONObject.toJSONString(couponStateUpdateDto));
                ServerResponseEntity responseEntity = crmCouponFeignClient.couponStateUpdate(couponStateUpdateDto);
                log.info("调用crm核销(小程序)券接口的返回值为：{}",JSONObject.toJSONString(responseEntity));
                if (responseEntity.isFail()){
                    throw new LuckException(responseEntity.getMsg());
                }
            }
            TCouponUser tCouponUser = new TCouponUser();
            tCouponUser.setStatus(2);
            tCouponUser.setWriteOffTime(new Timestamp(System.currentTimeMillis()));
            tCouponUserMapper.update(tCouponUser,new LambdaUpdateWrapper<TCouponUser>().eq(TCouponUser::getCouponCode,code));
        }else {
            CouponWriteOffDto couponWriteOffDto = new CouponWriteOffDto();
            couponWriteOffDto.setUnion_id(userId.toString());
            couponWriteOffDto.setCoupon_code(code);
            log.info("调用crm券码的参数为：{}",JSONObject.toJSONString(couponWriteOffDto));
            ServerResponseEntity serverResponseEntity = crmCouponFeignClient.couponWriteOff(couponWriteOffDto);
            log.info("调用crm券码的返回值为：{}",JSONObject.toJSONString(serverResponseEntity ));
            if (serverResponseEntity.isFail()){
                throw new LuckException(serverResponseEntity.getMsg());
            }
        }

        return ServerResponseEntity.success();
    }


//    @HystrixCommand
    @Override
    public List<CouponOrderVO> couponList(Long userId, Long shopId, Long actualTotal, List<ShopCartItemVO> shopCartItems, String couponCode) {
        log.info("下单可用优惠券请求参数 ---- 用户id为：{}，门店id为：{}，订单全额为：{}，购物车信息为：{}",userId,shopId,actualTotal,JSONObject.toJSONString(shopCartItems));


        //会员日活动优先级最高
        //如果下单商品全部被会员日活动设置了为不可以使用优惠券，则直接返回空
        if(shopCartItems.stream().filter(s->s.getFriendlyCouponUseFlag()==0).collect(toList()).size() ==shopCartItems.size() ){
            return new ArrayList<>();
        }

        List<Long> coouponIds = new ArrayList<>();
        //如果下单商品中全部为参加了虚拟门店活动，并且设置限定优惠券。 过滤掉当前会员其他优惠券。只显示当前活动设置的可用优惠券。
        if(shopCartItems.stream().filter(s->s.getFriendlyCouponUseFlag()==3).collect(toList()).size() ==shopCartItems.size()){
            for (ShopCartItemVO shopCartItem : shopCartItems) {
                coouponIds.addAll(shopCartItem.getInvateStoreCouponids());
            }
        }

        //过滤掉下单商品中会员日活动设置为不可用优惠券的商品 (friendlyCouponUseFlag = 0为不可以使用优惠券)
        shopCartItems = shopCartItems.stream().filter(s->s.getFriendlyCouponUseFlag()>=1).collect(toList());
        //过滤完不参与会员日活动价格商品后重新计算剩余商品订单金额。
        actualTotal = shopCartItems.stream().collect(Collectors.summingLong(ShopCartItemVO::getActualTotal));
        log.info("过滤会员日后 下单可用优惠券请求参数 ---- 用户id为：{}，门店id为：{}，订单全额为：{}，购物车信息为：{}",userId,shopId,actualTotal,JSONObject.toJSONString(shopCartItems));


        //购物车里的spu
        List<Long> spuIds = shopCartItems.stream().map(ShopCartItemVO::getSpuId).collect(toList());

        //小程序用户券记录
        // todo 暂时还原了原来的处理逻辑，因为这里场景测试发现问题，例如 指定商品不可用维护几千款商品。逻辑同现在修复前还是在内存中存了大的list
        //  修复时间来不及放在后续迭代中修复。
        List<CouponOrderVO> coupons = couponService.getCouponListByUserAndShop(userId, shopId,spuIds);

        //获取当前登录用户的会员信息
        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(userId);
        if (user.isFail()){
            log.info("用户信息查询异常:{}", user.getMsg());
        }
        CouponGetDto couponGetDto = new CouponGetDto();
        ServerResponseEntity<String> storeCodeByStoreId = storeFeignClient.getStoreCodeByStoreId(shopId);
        if (storeCodeByStoreId.isSuccess()){
            couponGetDto.setStore(storeCodeByStoreId.getData());
        }
        //查询crm用户券记
        couponGetDto.setPage_index(1);
        couponGetDto.setPage_size(100);
        couponGetDto.setMobile(user.getData().getPhone());
        couponGetDto.setStatus("VALID");
        log.info("crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
        ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList = crmCouponFeignClient.couponGet(couponGetDto);
        log.info("crm查询用户优惠券记录接口返回值为：{}", JSONObject.toJSONString(couponGetList));

        //获取到crm券进行合并处理
        if (couponGetList.isSuccess()){
            List<CouponGetVo> crmCouponList = couponGetList.getData().getJsondata();
            
            //纸质券码查询到的优惠券加到crmCouponList
            if(StrUtil.isNotEmpty(couponCode)){
                ServerResponseEntity<CouponSingleGetResp> couponSingleGetEntity = crmCouponFeignClient.couponSingleGet(couponCode);
                log.info("券码:{} 查询到单个优惠券信息为: {}",couponCode, JSONObject.toJSONString(couponSingleGetEntity));
                if(couponSingleGetEntity.isSuccess() && Objects.nonNull(couponSingleGetEntity.getData())){
                    CouponSingleGetResp couponSingleGetResp = couponSingleGetEntity.getData();
                    //校验券项目是否存在
                    TCoupon tCoupon = tCouponService.queryByCrmCouponId(couponSingleGetResp.getCoupon_rule_id());
                    if(Objects.isNull(tCoupon)){
                        Assert.faild("兑换失败，请联系客服");
                    }
                    
                    //门店校验
                    if (!tCoupon.getIsAllShop()) {
                        TCouponShop tCouponShop = tCouponShopMapper.selectOne(new LambdaQueryWrapper<TCouponShop>()
                                .eq(TCouponShop::getCouponId, tCoupon.getId())
                                .eq(TCouponShop::getShopId, shopId));
                        if (Objects.isNull(tCouponShop)) {
                            Assert.faild("当前门店不可使用该券，详询客服");
                        }
                    }
                    
                    CouponGetVo couponGetVo = BeanUtil.copyProperties(couponSingleGetResp, CouponGetVo.class);
                    crmCouponList.add(couponGetVo);
                }
            }
    
            if (!CollectionUtils.isEmpty(crmCouponList)){
                //根据crm_coupon_id查询出优惠券信息列表
                List<String> crmCouponIds = crmCouponList.stream().map(CouponGetVo::getCoupon_rule_id).collect(toList());
                // 20221211 增加spuids的参数， 减少关联商品数量
//                List<CouponOrderVO> crmCouponOrderList = couponMapper.getCrmCouponListAndSpuIds(shopId, crmCouponIds,spuIds);

                List<CouponOrderVO> crmCouponOrderList = couponMapper.getCrmCouponList(shopId, crmCouponIds);
                for (CouponOrderVO couponOrderVO : crmCouponOrderList) {
                    CouponDetailDTO detailDTO = tCouponService.queryCacheByCouponId(couponOrderVO.getCouponId());
                    couponOrderVO.setSpuIds(detailDTO.getCommodities());
                    couponOrderVO.setPriceCodes(detailDTO.getPriceCodes());

//                    if(StrUtil.isNotEmpty(couponOrderVO.getCommodityIds())){
//                        List<String> commodityIds = Arrays.asList(couponOrderVO.getCommodityIds().split(","));
//                        couponOrderVO.setSpuIds(commodityIds.stream().map(s ->Long.parseLong(s)).collect(Collectors.toList()));
//                    }
//                    if(StrUtil.isNotEmpty(couponOrderVO.getStrPriceCodes())){
//                        List<String> priceCodes = Arrays.asList(couponOrderVO.getStrPriceCodes().split(","));
//                        couponOrderVO.setPriceCodes(priceCodes);
//                    }
                }


                crmCouponOrderList.forEach(temp ->{
                    List<CouponGetVo> crmCoupon = crmCouponList.stream().filter(one -> one.getCoupon_rule_id().equals(temp.getCrmCouponId())).collect(toList());
                    if (!CollectionUtils.isEmpty(crmCoupon)){
                        //20220921 日修改，返回crm会员的全部优惠券。
                        for (CouponGetVo couponGetVo : crmCoupon) {

                            CouponOrderVO newcrmCoupon = new CouponOrderVO();
                            BeanUtil.copyProperties(temp,newcrmCoupon);

                            newcrmCoupon.setCouponCode(couponGetVo.getCoupon_code());
                            newcrmCoupon.setShopId(shopId);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                if (ObjectUtil.isNotEmpty(couponGetVo.getBegin_time())){
                                    newcrmCoupon.setStartTime(format.parse(couponGetVo.getBegin_time()));
                                }
                                if (ObjectUtil.isNotEmpty(couponGetVo.getEnd_time())){
                                    newcrmCoupon.setEndTime(format.parse(couponGetVo.getEnd_time()));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
    
                            //返回纸质券券码
                            if(StrUtil.isNotEmpty(couponCode) && couponCode.equals(couponGetVo.getCoupon_code())){
                                newcrmCoupon.setPaperCouponCode(couponCode);
                            }

                            coupons.add(newcrmCoupon);

                        }
//                        temp.setCouponCode(crmCoupon.get(0).getCoupon_code());
//                        temp.setShopId(shopId);
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        try {
//                            if (ObjectUtil.isNotEmpty(crmCoupon.get(0).getBegin_time())){
//                                temp.setStartTime(format.parse(crmCoupon.get(0).getBegin_time()));
//                            }
//                            if (ObjectUtil.isNotEmpty(crmCoupon.get(0).getEnd_time())){
//                                temp.setEndTime(format.parse(crmCoupon.get(0).getEnd_time()));
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        coupons.add(temp);
                    }
                });
            }
        }else {
            log.info("CRM优惠券列表查询异常:{}", couponGetList.getMsg());
        }

        List<CouponOrderVO> result = new ArrayList<>();



        //购物车里所有商品的priceCode
        List<String> allPriceCodes = shopCartItems.stream().map(ShopCartItemVO::getPriceCode).collect(toList());

        int shopCartItemsCount = shopCartItems.stream().mapToInt(ShopCartItemVO::getCount).sum();
        log.info("购物车里的spu：{}",JSONObject.toJSONString(spuIds));
        log.info("商品过滤前，当前会员的可用优惠券：{}",JSONObject.toJSONString(coupons));
        for (CouponOrderVO temp : coupons) {
            log.info("优惠券商品过滤，当前处理的优惠券信息：{}",JSONObject.toJSONString(temp));

            //================================== 1.查询优惠券关联的中台分类  1为不限制 2为指定分类可用 ==================================
            //================================== 1.查询优惠券关联的中台分类  1为不限制 时 ==================================
            if(temp.getCategoryScopeType()==1){
                //================================== 2.优惠券关联商品sku校验（适用商品类型（0：不限/1：指定商品spu/2:指定商品不可用spu /3指定商品sku /4指定商品不可用sku ））==================================
                if (temp.getSuitableProdType() == 0){
                    //适用商品类型为不限时只校验商品数量
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (shopCartItemsCount > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (shopCartItemsCount < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (shopCartItemsCount < temp.getCommodityLimitNum() || shopCartItemsCount > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        log.info("购物车总价为：{}",JSONObject.toJSONString(temp.getCashCondition()));
                        if (temp.getCashCondition() > actualTotal){
                            continue;
                        }
                    }
                }

                //1：指定商品spu
                if (temp.getSuitableProdType() == 1) {
                    //优惠券适用的spu
                    List<Long> couponSpuIds = temp.getSpuIds();
                    //如果当前适用商品的可用优惠券为空，说明商品不在适用范围内
                    if(CollectionUtils.isEmpty(couponSpuIds)){
                        continue;
                    }
                    //取交集
                    List<Long> spuCollect = couponSpuIds.stream().filter(spuIds::contains).collect(toList());
                    log.info("适用商品类型为指定商品时，交集的spu：{}",JSONObject.toJSONString(spuCollect));
                    //交集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(spuCollect)) {
                        continue;
                    }
                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    int count = shopCartItems.stream().filter(car -> spuCollect.contains(car.getSpuId())).mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品交集取价格
                        //优惠券适用的spu
//                        List<Long> couponSpuIds = temp.getSpuIds();
                        //取交集
//                        List<Long> spuCollect = couponSpuIds.stream().filter(spuIds::contains).collect(toList());
                        long totalAmount = 0;
                        for (ShopCartItemVO car : shopCartItems) {
                            log.info("适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (spuCollect.contains(car.getSpuId())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }


                }

                //2: 指定商品不可用spu
                if (temp.getSuitableProdType() == 2) {
                    //优惠券不可用的spu
                    List<Long> disableSpuIds = temp.getSpuIds();

                    //取差集
                    List<Long> spus = shopCartItems.stream().map(ShopCartItemVO::getSpuId).collect(toList());
                    spus.removeAll(disableSpuIds);
                    log.info("适用商品类型为指定商品不可用时，差集的spu：{}",JSONObject.toJSONString(spus));
                    //差集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(spus)) {
                        continue;
                    }
                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    //过滤掉购物车中不可用的的商品后，汇总数量判断商品数量条件是否满足
                    int count = shopCartItems.stream().filter(car -> spus.contains(car.getSpuId())).mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    }else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品交集取价格
                        //优惠券不适用的spu
//                        List<Long> disableSpuIds = temp.getSpuIds();
                        //取差集
//                        List<Long> spus = shopCartItems.stream().map(ShopCartItemVO::getSpuId).collect(toList());
//                        spus.removeAll(disableSpuIds);
                        long totalAmount = 0;
                        for (ShopCartItemVO car : shopCartItems) {
                            log.info("不适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (spus.contains(car.getSpuId())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }

                }

                //3：指定商品sku
                if (temp.getSuitableProdType() == 3) {
                    //优惠券适用的spu
                    List<String> couponPriceCodes = temp.getPriceCodes();
                    //如果当前优惠券的可用priceCode为空，说明商品不在适用范围内
                    if(CollectionUtils.isEmpty(couponPriceCodes)){
                        continue;
                    }
                    //取交集
                    List<String> priceCodeCollect = couponPriceCodes.stream().filter(allPriceCodes::contains).collect(toList());
                    //交集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(priceCodeCollect)) {
                        continue;
                    }
                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    int count = shopCartItems.stream().filter(car -> priceCodeCollect.contains(car.getPriceCode())).mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品交集取价格
                        //优惠券适用的spu
//                        List<Long> couponSpuIds = temp.getSpuIds();
                        //取交集
//                        List<Long> spuCollect = couponSpuIds.stream().filter(spuIds::contains).collect(toList());
                        long totalAmount = 0;
                        for (ShopCartItemVO car : shopCartItems) {
                            log.info("适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (priceCodeCollect.contains(car.getPriceCode())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }


                }

                //4: 指定商品不可用sku
                if (temp.getSuitableProdType() == 4) {
                    //优惠券不可用的spu
                    List<String> disablePriceCodes = temp.getPriceCodes();

                    //取差集
                    List<String> priceCodes = shopCartItems.stream().map(ShopCartItemVO::getPriceCode).collect(toList());
                    priceCodes.removeAll(disablePriceCodes);
                    log.info("适用商品类型为指定商品不可用(sku)时，差集的sku：{}",JSONObject.toJSONString(priceCodes));
                    //差集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(priceCodes)) {
                        continue;
                    }
                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    //过滤掉购物车中不可用的的商品后，汇总数量判断商品数量条件是否满足
                    int count = shopCartItems.stream().filter(car -> priceCodes.contains(car.getPriceCode())).mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    }else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品交集取价格
                        //优惠券不适用的spu
//                        List<Long> disableSpuIds = temp.getSpuIds();
                        //取差集
//                        List<Long> spus = shopCartItems.stream().map(ShopCartItemVO::getSpuId).collect(toList());
//                        spus.removeAll(disableSpuIds);
                        long totalAmount = 0;
                        for (ShopCartItemVO car : shopCartItems) {
                            log.info("不适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (priceCodes.contains(car.getPriceCode())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }

                }

            }


            //================================== 1.查询优惠券关联的中台分类  1为不限制 2为指定分类可用 ==================================
            //================================== 1.查询优惠券关联的中台分类  2为指定分类可用时 ==================================
            if(temp.getCategoryScopeType()==2){
                List<TCouponCategory> tCouponCategories = tCouponCategoryMapper.selectList(new LambdaQueryWrapper<TCouponCategory>().eq(TCouponCategory::getCouponId, temp.getCouponId()));
                //分类为空则跳过
                if(CollectionUtils.isEmpty(tCouponCategories)){
                    continue;
                }
                //当前优惠券的中台分类集合
                List<String> currentCouponCategories = tCouponCategories.stream().map(TCouponCategory::getCategory).collect(toList());
                temp.setCategorys(currentCouponCategories);

                //购物车里的 所有商品的对应中台分类集合
                List<String> categoriesIds = shopCartItems.stream().map(ShopCartItemVO::getCategory).collect(toList());

                //存在分类限制，校验分类
                //取交集
                List<String> categorieCollect = currentCouponCategories.stream().filter(categoriesIds::contains).collect(toList());
                //如果当前购物车内商品在分类中不存在，则直接跳过
                if (CollectionUtils.isEmpty(categorieCollect)) {
                    continue;
                }

                // 通过优惠券的适用分类过滤购物车商品
                List<ShopCartItemVO> categorieFiltershopCartItems = shopCartItems.stream().filter(car -> categorieCollect.contains(car.getCategory())).collect(toList());
                //获取过滤过分类后的购物车内spu商品集合
                List<Long> categorieSpuIds = categorieFiltershopCartItems.stream().map(ShopCartItemVO::getSpuId).collect(toList());
                //获取过滤过分类后的购物车内商品 priceCode集合
                List<String> categoriePriceCodes = categorieFiltershopCartItems.stream().map(ShopCartItemVO::getPriceCode).collect(toList());

                //================================== 2.优惠券关联商品sku校验（适用商品类型（0：不限/1：指定商品spu/2:指定商品不可用spu /3指定商品sku /4指定商品不可用sku ））==================================
                //0：不限
                if(temp.getSuitableProdType() == 0){
                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间）==================================
                    int count = categorieFiltershopCartItems.stream().mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }

                    //================================== 金额限制类型（0：不限/1：满额）==================================
                    if(temp.getAmountLimitType() == 1){
                        Long amountLimitActualTotal = categorieFiltershopCartItems.stream().mapToLong(ShopCartItemVO::getActualTotal).sum();
                        //当前优惠券不限商品则取购物车总价
                        if (temp.getSuitableProdType() == 0) {
                            log.info("限制金额:{},购物车总价为：{}",JSONObject.toJSONString(temp.getCashCondition()),amountLimitActualTotal);
                            if (temp.getCashCondition() > amountLimitActualTotal){
                                continue;
                            }
                        }
                    }
                }

                //1：指定商品SPU
                if (temp.getSuitableProdType() == 1) {
                    //优惠券适用的spu
                    List<Long> couponSpuIds = temp.getSpuIds();
                    //如果当前适用商品的可用优惠券为空，说明商品不在适用范围内
                    if(CollectionUtils.isEmpty(couponSpuIds)){
                        continue;
                    }
                    //取交集
                    List<Long> spuCollect = couponSpuIds.stream().filter(categorieSpuIds::contains).collect(toList());
                    log.info("适用商品类型为指定商品时，交集的spu：{}",JSONObject.toJSONString(spuCollect));
                    //交集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(spuCollect)) {
                        continue;
                    }

                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    int count = categorieFiltershopCartItems.stream().filter(car -> spuCollect.contains(car.getSpuId())).mapToInt(ShopCartItemVO::getCount).sum();

                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }

                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品交集取价格
                        //优惠券适用的spu
//                        List<Long> couponSpuIds = temp.getSpuIds();
                        //取交集
//                        List<Long> spuCollect = couponSpuIds.stream().filter(spuIds::contains).collect(toList());
                        long totalAmount = 0;
                        for (ShopCartItemVO car : categorieFiltershopCartItems) {
                            log.info("适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (spuCollect.contains(car.getSpuId())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }

                }

                //2: 指定商品不可用 SPU
                if (temp.getSuitableProdType() == 2) {
                    //优惠券不可用的spu
                    List<Long> disableSpuIds = temp.getSpuIds();
                    //取差集
                    List<Long> spus = categorieFiltershopCartItems.stream().map(ShopCartItemVO::getSpuId).collect(toList());
                    spus.removeAll(disableSpuIds);
                    log.info("适用商品类型为指定商品不可用时，差集的spu：{}",JSONObject.toJSONString(spus));
                    //差集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(spus)) {
                        continue;
                    }

                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    //过滤掉购物车中不可用的的商品后，汇总数量判断商品数量条件是否满足
                    int count = categorieFiltershopCartItems.stream().filter(car -> spus.contains(car.getSpuId())).mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品差集取价格
                        long totalAmount = 0;
                        for (ShopCartItemVO car : categorieFiltershopCartItems) {
                            log.info("不适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (spus.contains(car.getSpuId())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }
                }

                //3：指定商品 SKU
                if (temp.getSuitableProdType() == 3) {
                    //优惠券适用的 priceCode
                    List<String> coupontPriceCodes = temp.getPriceCodes();
                    //如果当前优惠券的 适用商品的priceCodes为空，说明商品不在适用范围内
                    if(CollectionUtils.isEmpty(coupontPriceCodes)){
                        continue;
                    }
                    //取交集
                    List<String> priceCodeCollect = coupontPriceCodes.stream().filter(categoriePriceCodes::contains).collect(toList());
                    //log.info("适用商品类型为指定商品时，交集的spu：{}",JSONObject.toJSONString(spuCollect));
                    //交集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(priceCodeCollect)) {
                        continue;
                    }

                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    int count = categorieFiltershopCartItems.stream().filter(car -> priceCodeCollect.contains(car.getPriceCode())).mapToInt(ShopCartItemVO::getCount).sum();

                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }

                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品交集取价格
                        //优惠券适用的spu
//                        List<Long> couponSpuIds = temp.getSpuIds();
                        //取交集
//                        List<Long> spuCollect = couponSpuIds.stream().filter(spuIds::contains).collect(toList());
                        long totalAmount = 0;
                        for (ShopCartItemVO car : categorieFiltershopCartItems) {
                            log.info("适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (priceCodeCollect.contains(car.getPriceCode())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }

                }

                //4: 指定商品不可用 SKU
                if (temp.getSuitableProdType() == 4) {
                    //优惠券不可用的 priceCode
                    List<String> disablePriceCodes = temp.getPriceCodes();
                    //取差集
                    List<String> priceCodes = categorieFiltershopCartItems.stream().map(ShopCartItemVO::getPriceCode).collect(toList());
                    priceCodes.removeAll(disablePriceCodes);
                    //差集为空说明商品不在适用范围内
                    if (CollectionUtils.isEmpty(priceCodes)) {
                        continue;
                    }

                    //================================== 3.商品限制类型（0：不限/1：不超过/2：不少于 /3:区间） ==================================
                    //过滤掉购物车中不可用的的商品后，汇总数量判断商品数量条件是否满足
                    int count = categorieFiltershopCartItems.stream().filter(car -> priceCodes.contains(car.getPriceCode())).mapToInt(ShopCartItemVO::getCount).sum();
                    if (temp.getCommodityLimitType() == 1) {
                        //不超过
                        if (count > temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 2) {
                        //不少于
                        if (count < temp.getCommodityLimitNum()) {
                            continue;
                        }
                    } else if (temp.getCommodityLimitType() == 3) {
                        //区间
                        if (count < temp.getCommodityLimitNum() || count > temp.getCommodityLimitMaxNum()) {
                            continue;
                        }
                    }
                    //================================== 4.金额限制类型（0：不限/1：满额）==================================
                    if (temp.getAmountLimitType() == 1) {
                        //当前优惠券限制商品，拿出商品差集取价格
                        long totalAmount = 0;
                        for (ShopCartItemVO car : categorieFiltershopCartItems) {
                            log.info("不适用商品-商品价格为：{}",JSONObject.toJSONString(car.getTotalAmount()));
                            if (priceCodes.contains(car.getPriceCode())) {
                                totalAmount = car.getTotalAmount() + totalAmount;
                            }
                        }
                        if (temp.getCashCondition() > totalAmount){
                            continue;
                        }
                    }
                }

            }



            //========================== 判断购物车商品是否满足原折扣限制
            if(temp.getDisNoles()==1 && temp.getDisNolesValue()!=null && temp.getDisNolesValue().doubleValue()>0){
                //过滤不满足折扣条件的商品 实际折扣 = 售价 / 吊牌价
                // 售价 / 吊牌价 (实际折扣) < 折扣限制/10 （折扣限制页面维护是维护的个位数 例如9折维护9 ，这里要除以10处理）
                List<ShopCartItemVO> disNolesFiltershopCartItems = shopCartItems.stream().filter(s ->
                                BigDecimal.valueOf(s.getPriceFee()).divide(BigDecimal.valueOf(s.getMarketPriceFee()),2,BigDecimal.ROUND_DOWN).doubleValue()
                        > temp.getDisNolesValue().divide(BigDecimal.TEN,2,BigDecimal.ROUND_DOWN).doubleValue()).collect(toList());
                //如果所有商品都不满足条件，则过滤当前优惠券
                log.info("原折扣限制判断，符合条件的购物车商品数量：{},购物车商品总数量:{}",disNolesFiltershopCartItems.size(),shopCartItems.size());
                if(CollectionUtil.isEmpty(disNolesFiltershopCartItems)){
                    continue;
                }
            }
            result.add(temp);
        }

        log.info("优惠券过滤前，下单可用优惠券信息为：{}",JSONObject.toJSONString(result));
        //如果下单商品中全部为参加了虚拟门店活动，并且设置限定优惠券。 过滤掉当前会员其他优惠券。只显示当前活动设置的可用优惠券。
        if(shopCartItems.stream().filter(s->s.getFriendlyCouponUseFlag()==3).collect(toList()).size() ==shopCartItems.size()){
            log.info("过滤优惠券：{}",coouponIds);
            //过滤虚拟门店没有设置的优惠券。
            for (int i=0; i<result.size(); i++) {
                if(!coouponIds.contains(result.get(i).getCouponId())){
                    result.remove(i);
                    i--;
                }
            }
        }
        log.info("优惠券过滤后，下单可用优惠券信息为：{}",JSONObject.toJSONString(result));
        //下单可使用优惠券过滤 [券码导入] 类型的优惠券
        result = result.stream().filter(s->s.getKind()!=2).collect(toList());
        
        //将纸质券排序放到第一位
        List<String> availableCouponCodes = result.stream().map(CouponOrderVO::getCouponCode).collect(Collectors.toList());
        if(StrUtil.isNotEmpty(couponCode) && availableCouponCodes.contains(couponCode)){
            int index = 0;
            for (int i = 0; i < result.size(); i++) {
                if (couponCode.equals(result.get(i).getCouponCode())) {
                    index = i;
                    break;
                }
            }
            Collections.swap(result, index, 0);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateCouponStatus(UpdateCouponStatusDTO param) {
        log.info("变更券状态  = {}",JSONObject.toJSONString(param));
        Short sourceType = param.getSourceType();
        //该优惠券来源于crm
        if (sourceType == 2){
            //================================== 核销优惠券 ==================================
            if (param.getStatus().equals(MyCouponStatus.FAILURE.value())){
                ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserAndOpenIdsByUserId(param.getUserId());
                if (userData.isFail()){
                    throw new LuckException("用户信息查询失败");
                }
                if (ObjectUtil.isEmpty(userData.getData())){
                    throw new LuckException("用户信息查询为空");
                }
                if (ObjectUtil.isEmpty(userData.getData().getVipcode())){
                    throw new LuckException("CRM卡号查询为空");
                }

                ServerResponseEntity<EsOrderBO> orderResponse = orderFeignClient.getEsOrder(param.getOrderNo());
                if(orderResponse==null || !orderResponse.isSuccess() || orderResponse.getData()==null){
                    Assert.faild(StrUtil.format("订单编号:{}有误，核销优惠券失败",param.getOrderNo()));
                }
                CouponWriteOffDto couponWriteOffDto = new CouponWriteOffDto();
                couponWriteOffDto.setVipcode(userData.getData().getVipcode());
                couponWriteOffDto.setCoupon_code(param.getCouponCode());
                couponWriteOffDto.setStore_code(param.getStoreCode());
                String orderNumber = orderResponse.getData().getOrderNumber();
                couponWriteOffDto.setOrder_no(orderNumber);
                log.info("调用crm核销券码的参数为：{}",JSONObject.toJSONString(couponWriteOffDto));
                ServerResponseEntity serverResponseEntity = crmCouponFeignClient.couponWriteOff(couponWriteOffDto);
                log.info("调用crm核销券码的返回值为：{}",JSONObject.toJSONString(serverResponseEntity ));
                if (serverResponseEntity.isFail()){
                    throw new LuckException(serverResponseEntity.getMsg());
                }
                //同时修改优惠券使用状态为已使用。
                try {
                    tCouponUserMapper.crmUseCoupon(param.getCouponCode(),param.getOrderNo());
                }catch (Exception e){
                    log.error("核销crm优惠券，修改用户领券记录状态失败。couponCode:{},orderId:{}",param.getCouponCode(),param.getOrderNo());
                }

            }

            //================================== 冻结优惠券 ==================================
            if (param.getStatus().equals(MyCouponStatus.FREEZE.value())){
                CouponFreezeThawDto couponFreezeThawDto = new CouponFreezeThawDto();
                couponFreezeThawDto.setCoupon_code(param.getCouponCode());
                couponFreezeThawDto.setType(param.getStatus().toString());
                log.info("调用crm冻结券码的参数为：{}",JSONObject.toJSONString(couponFreezeThawDto));
                ServerResponseEntity<CouponFreezeThawResp> crmResult = crmCouponFeignClient.freezeThaw(couponFreezeThawDto);
                log.info("调用crm冻结券码的返回值为：{}",JSONObject.toJSONString(crmResult));
                if (crmResult.isFail()){
                    throw new LuckException(crmResult.getMsg());
                }else {
                    //冻结存储token
                    CouponFreezeThawResp data = crmResult.getData();
                    String token = data.getToken();
                    CrmCouponToken crmCouponToken = new CrmCouponToken();
                    crmCouponToken.setCrmCouponCode(param.getCouponCode());
                    crmCouponToken.setToken(token);
                    crmCouponToken.setOrderNo(param.getOrderNo());
                    crmCouponToken.setStoreCode(param.getStoreCode());
                    crmCouponToken.setUserId(param.getUserId());
                    crmCouponTokenMapper.insert(crmCouponToken);

                    tCouponUserMapper.crmFreezeCoupon(param.getCouponCode(),param.getOrderNo(),param.getOrderAmount(),param.getCouponAmount());
                }
            }

            //================================== 解冻结优惠券 ==================================
            if (param.getStatus().equals(MyCouponStatus.EFFECTIVE.value())){
                //获取解冻token
                CrmCouponToken crmCouponToken = crmCouponTokenMapper.selectOne(new LambdaQueryWrapper<CrmCouponToken>().eq(CrmCouponToken::getCrmCouponCode, param.getCouponCode()));
                if (ObjectUtil.isEmpty(crmCouponToken)){
                    throw new LuckException("优惠券解冻失败，未获取到冻结token");
                }
                CouponFreezeThawDto couponFreezeThawDto = new CouponFreezeThawDto();
                couponFreezeThawDto.setCoupon_code(param.getCouponCode());
                couponFreezeThawDto.setType(param.getStatus().toString());
                couponFreezeThawDto.setToken(crmCouponToken.getToken());
                log.info("调用crm解冻券码的参数为：{}",JSONObject.toJSONString(couponFreezeThawDto));
                ServerResponseEntity serverResponseEntity = crmCouponFeignClient.freezeThaw(couponFreezeThawDto);
                log.info("调用crm解冻券码的返回值为：{}",JSONObject.toJSONString(serverResponseEntity ));
                if (serverResponseEntity.isFail()){
                    throw new LuckException(serverResponseEntity.getMsg());
                }else{
                    crmCouponTokenMapper.delete(new LambdaUpdateWrapper<CrmCouponToken>().eq(CrmCouponToken::getCrmCouponCode,param.getCouponCode()));
                    
                    //A或者B开头的券码就是纸质券,如果是纸质券码类型,删除领券记录
                    if(param.getCouponCode().startsWith("A") || param.getCouponCode().startsWith("B") ){
                        tCouponUserMapper.delete(new LambdaQueryWrapper<TCouponUser>().eq(TCouponUser::getCouponCode,param.getCouponCode()));
                    }else{
                        tCouponUserMapper.crmEffective(param.getCouponCode());
                    }
                }
            }
        }

        //该优惠券来源于小程序
        if (sourceType == 1){
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserData(param.getUserId());
            if (userData != null && userData.isSuccess()) {
                WriteOffDetailVO writeOffDetailVO = tCouponUserMapper.writeOffCouponDetail(param.getCouponCode());
                CouponStateUpdateDto couponStateUpdateDto = new CouponStateUpdateDto();
                couponStateUpdateDto.setCoupon_id(writeOffDetailVO.getCouponId()+"");
                couponStateUpdateDto.setCoupon_code(writeOffDetailVO.getCouponCode());
                couponStateUpdateDto.setMobile(userData.getData().getPhone());
                couponStateUpdateDto.setOper("writeOff");
                log.info("调用crm核销(小程序)券接口的参数为：{}",JSONObject.toJSONString(couponStateUpdateDto));
                ServerResponseEntity responseEntity = crmCouponFeignClient.couponStateUpdate(couponStateUpdateDto);
                log.info("调用crm核销(小程序)券接口的返回值为：{}",JSONObject.toJSONString(responseEntity));
                if (responseEntity.isFail()){
                    throw new LuckException(responseEntity.getMsg());
                }
            }
            TCouponUser updateParam = new TCouponUser();
            updateParam.setCouponCode(param.getCouponCode());
            updateParam.setStatus(param.getStatus());
            if (param.getStatus() == 2){
                updateParam.setWriteOffTime(new Timestamp(System.currentTimeMillis()));
            }
            updateParam.setOrderNo(param.getOrderNo());
            updateParam.setOrderAmount(param.getOrderAmount());
            updateParam.setCouponAmount(param.getCouponAmount());
            tCouponUserMapper.updateByCouponCode(updateParam);
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<StaffReceiveCouponStatsVO> staffReceiveCouponStats(Long staffId) {
        StaffReceiveCouponStatsVO couponStatsVO = new StaffReceiveCouponStatsVO();
        QueryWrapper<TCouponUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", staffId);
        queryWrapper.ne("status", 0);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        queryWrapper.between("receive_time", Date.from(startTime.atZone(zone).toInstant()), Date.from(endTime.atZone(zone).toInstant()));
        List<TCouponUser> tCouponUserList = tCouponUserMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(tCouponUserList)) {
            Long notWriteOffCoupons = tCouponUserList.stream().filter(tCouponUser -> tCouponUser.getStatus() == 1).count();
            Long writeOffCoupons = tCouponUserList.stream().filter(tCouponUser -> tCouponUser.getStatus() == 2).count();
            Long total = notWriteOffCoupons + writeOffCoupons;
            couponStatsVO.setNotWriteOffCoupons(notWriteOffCoupons);
            couponStatsVO.setWriteOffCoupons(writeOffCoupons);
            couponStatsVO.setWriteOffRate(new BigDecimal(writeOffCoupons).divide(new BigDecimal(total),
                    2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        }
        return ServerResponseEntity.success(couponStatsVO);
    }

    @Override
    public ServerResponseEntity<StaffWriteOffCouponStatsVO> staffWriteOffCouponStats(Long staffId) {
        StaffWriteOffCouponStatsVO couponStatsVO = new StaffWriteOffCouponStatsVO();
        QueryWrapper<TCouponUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", staffId);
        queryWrapper.eq("status", 2);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        queryWrapper.between("write_off_time", Date.from(startTime.atZone(zone).toInstant()), Date.from(endTime.atZone(zone).toInstant()));
        List<TCouponUser> tCouponUserList = tCouponUserMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(tCouponUserList)) {
            Map<Long, List<TCouponUser>> couponUserMap = tCouponUserList.stream().collect(Collectors.groupingBy(c -> c.getUserId()));
            couponStatsVO.setWriteOffUsers(Long.valueOf(couponUserMap.size()));
            couponStatsVO.setWriteOffCoupons(Long.valueOf(tCouponUserList.size()));
            couponStatsVO.setWriteOffAmount(tCouponUserList.stream().filter(t -> t.getCouponAmount() != null)
                    .mapToLong(TCouponUser :: getCouponAmount).sum());
        }
        return ServerResponseEntity.success(couponStatsVO);
    }

    @Override
    public ServerResponseEntity<List<TCouponUser>> couponUserList(TCouponUser param) {
        List<TCouponUser> tCouponUsers = tCouponUserMapper.couponUserList(param);
        return ServerResponseEntity.success(tCouponUsers);
    }

    @Override
    public ServerResponseEntity<Integer> countCanUseCoupon(Long userId) {
        //过去365天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 365);
        Date d = c.getTime();
        String startDay = format.format(d);

        List<MyCouponListVO> valid = tCouponUserMapper.list("VALID", userId, d);
        Integer count = valid.size();

        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(userId);
        if (user.isSuccess() && !StrUtil.isEmpty(user.getData().getVipcode())) {
            CouponGetDto couponGetDto = new CouponGetDto();
            couponGetDto.setPage_index(1);
            couponGetDto.setPage_size(100);
            couponGetDto.setVipcode(user.getData().getVipcode());
            couponGetDto.setStatus("VALID");
            couponGetDto.setBegin_time(startDay);
            couponGetDto.setEnd_time(format.format(new Date()));
            log.info("调用crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
            ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList = crmCouponFeignClient.couponGet(couponGetDto);
            if (couponGetList.isSuccess() && couponGetList.getData() != null){
                CrmPageResult<List<CouponGetVo>> data = couponGetList.getData();
                if (count != null) {
                    count += Integer.parseInt(String.valueOf(data.getTotal_count()));
                } else {
                    count = Integer.parseInt(String.valueOf(data.getTotal_count()));
                }
            }
        }
        return ServerResponseEntity.success(count);
    }


    @Override
    public ServerResponseEntity<List<TCouponUser>> selectOrderNo(Date startTime, Date endTime) {
        log.info("查询使用企业券订单参数为：{}，{}",JSONObject.toJSONString(startTime),JSONObject.toJSONString(endTime));
        List<TCouponUser> tCouponUsers = tCouponUserMapper.selectOrderNo(startTime, endTime);
        log.info("查询使用企业券订单返回值为：{}",JSONObject.toJSONString(tCouponUsers));
        return ServerResponseEntity.success(tCouponUsers);
    }


    @Override
    public UpdateCouponStatusDTO selectByOrderNo(Long orderNo) {
        log.info("selectByOrderNo tCouponUserMapper.selectOne,查询参数：{}",orderNo);
        UpdateCouponStatusDTO updateCouponStatusDTO = new UpdateCouponStatusDTO();

        if(Objects.isNull(orderNo)){
            log.error("selectByOrderNo 查询参数为空，参数信息:{}",orderNo);
        }
        TCouponUser tCouponUser = tCouponUserMapper.selectOne(new LambdaQueryWrapper<TCouponUser>().eq(TCouponUser::getOrderNo, orderNo));
        log.info("selectByOrderNo tCouponUserMapper.selectOne,查询参数：{},查询结果:{}",orderNo,JSONObject.toJSONString(tCouponUser));
        if(tCouponUser==null){
            log.error("订单号查询优惠券失败，参数：{}",orderNo);
            return null;
        }

        if (ObjectUtil.isNotEmpty(tCouponUser) && tCouponUser.getCouponSourceType()==2){
            //查询crm冻结信息
            CrmCouponToken crmCouponToken = crmCouponTokenMapper.selectOne(new LambdaQueryWrapper<CrmCouponToken>().eq(CrmCouponToken::getOrderNo, orderNo));
            if(crmCouponToken==null){
                log.error("crm优惠券token查询为空，参数：{}",orderNo);
                return null;
            }
            updateCouponStatusDTO.setSourceType((short) 2);
            updateCouponStatusDTO.setCouponCode(crmCouponToken.getCrmCouponCode());
            updateCouponStatusDTO.setStatus(2);
            updateCouponStatusDTO.setOrderNo(crmCouponToken.getOrderNo());
            updateCouponStatusDTO.setStoreCode(crmCouponToken.getStoreCode());
            updateCouponStatusDTO.setUserId(crmCouponToken.getUserId());
        }
        if (ObjectUtil.isNotEmpty(tCouponUser) && tCouponUser.getCouponSourceType()==1){
            updateCouponStatusDTO.setId(tCouponUser.getId());
            updateCouponStatusDTO.setSourceType((short) 1);
            updateCouponStatusDTO.setCouponCode(tCouponUser.getCouponCode());
            updateCouponStatusDTO.setStatus(2);
            updateCouponStatusDTO.setOrderNo(tCouponUser.getOrderNo());
            updateCouponStatusDTO.setOrderAmount(tCouponUser.getOrderAmount());
            updateCouponStatusDTO.setCouponAmount(tCouponUser.getCouponAmount());
        }
        return updateCouponStatusDTO;
    }

    @Override
    public TCouponUser selectByCouponCode(String couponCode) {
        return tCouponUserMapper.selectByCouponCode(couponCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> batchReceiveCoupon(SystemCouponDTO param) {
        List<TCouponUser> tCouponUsers = new ArrayList<>();

        Long createId = AuthUserContext.get().getUserId();
        String createName = AuthUserContext.get().getUsername();
        String createPhone = "";
        ServerResponseEntity<AuthAccountVO> account = accountFeignClient.getByUserIdAndSysType(createId, 2);
        if (account.isSuccess()){
            createPhone = account.getData().getPhone();
        }else {
            log.info("系统发券查询发放用户信息为：{}",JSONObject.toJSONString(account));
        }

        for (Long userId : param.getUserIds()) {
            ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
            receiveCouponDTO.setCouponId(param.getCouponId());
            receiveCouponDTO.setUserId(userId);
            receiveCouponDTO.setActivitySource(ActivitySourceEnum.SYSTEM_COUPON.value());
            receiveCouponDTO.setActivityId(-1L);
            receiveCouponDTO.setCreateId(createId);
            receiveCouponDTO.setCreateName(createName);
            receiveCouponDTO.setCreatePhone(createPhone);
            TCouponUser tCouponUser = tCouponUser(receiveCouponDTO);
            tCouponUsers.add(tCouponUser);
        }

        log.info("************************ 组装参数为：{}" ,JSONObject.toJSONString(tCouponUsers));

        TCoupon tCoupon = tCouponMapper.selectById(param.getCouponId());

//        if (tCoupon.getSourceType() == 1){
            tCouponUserMapper.insertBatch(tCouponUsers);
//        }

        return ServerResponseEntity.success();
    }

    @Override
    public Boolean isUseEnterpriseCoupon(Long orderNo) {
        boolean result = false;
        TCouponUser tCouponUser = tCouponUserMapper.selectByOrderNo2(orderNo);
        if (!ObjectUtil.isEmpty(tCouponUser)){
            result = true;
        }
        return result;
    }

    @Override
    public ServerResponseEntity<WriteOffDetailVO> writeOffCouponDetail(String couponCode) {
        WriteOffDetailVO writeOffDetailVO = tCouponUserMapper.writeOffCouponDetail(couponCode);
        if (Objects.isNull(writeOffDetailVO)) {
            throw new LuckException("券码不存在");
        }
        return ServerResponseEntity.success(writeOffDetailVO);
    }

    @Override
    public ServerResponseEntity<Void> writeOffCoupon(WriteOffCouponDTO param) {
        TCouponUser tCouponUser = BeanUtil.copyProperties(param,TCouponUser.class);
        tCouponUser.setStatus(MyCouponStatus.FAILURE.value());
        tCouponUser.setWriteOffTime(new Timestamp(System.currentTimeMillis()));
        tCouponUserMapper.updateById(tCouponUser);
        return ServerResponseEntity.success();
    }

    @Override
    public PageInfo<CouponActivityDetailVO> couponActivityDetail(CouponActivityDTO param) {
        PageInfo<CouponActivityDetailVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                tCouponUserMapper.couponActivityDetail(param)
        );
        /**
         * 查询用户列表
         */
        List<Long> useridList = result.getList().stream().map(CouponActivityDetailVO::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }
        /**
         * 导购
         */
        List<Long> daogouList = result.getList().stream().map(CouponActivityDetailVO::getCreateId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StaffVO>> daogouResponse = staffFeignClient.getStaffBypByIds(daogouList);
        Map<Long, StaffVO> daogouMaps = new HashMap<>();
        if (daogouResponse != null && daogouResponse.isSuccess() && daogouResponse.getData().size() > 0) {
            daogouMaps = daogouResponse.getData().stream().collect(Collectors.toMap(StaffVO::getId, p -> p));
        }
        Map<Long, StaffVO> finalDaogouMaps = daogouMaps;
        Map<Long, UserApiVO> finalUserMaps = userMaps;
        result.getList().forEach(r -> {
            UserApiVO user = finalUserMaps.get(r.getUserId());
            if (user != null) {
                r.setUserName(user.getNickName());
                r.setUserPhone(user.getPhone());
            }
            StaffVO staffVO = finalDaogouMaps.get(r.getCreateId());
            if (staffVO != null) {
                r.setCreateName(staffVO.getStaffName());
                r.setCreateNo(staffVO.getStaffNo());
            }
        });
        /**
         * 门店编码
         */
        if(!CollectionUtils.isEmpty(result.getList())){
            List<Long> shopId = result.getList().stream().map(CouponActivityDetailVO::getShopId).distinct().collect(toList());
            ServerResponseEntity<List<StoreVO>> listServerResponseEntity = storeFeignClient.listByStoreIdList(shopId);
            if(listServerResponseEntity.isSuccess() && !CollectionUtils.isEmpty(listServerResponseEntity.getData())){
                List<StoreVO> storeVOList = listServerResponseEntity.getData();
                Map<Long, StoreVO> storeVOMap = storeVOList.stream().collect(toMap(StoreVO::getStoreId, k -> k));
                result.getList().forEach(vo->{
                    StoreVO storeVO = storeVOMap.get(vo.getShopId());
                    if(storeVO != null){
                        vo.setShopCode(storeVO.getStoreCode());
                    }
                });
            }
        }

        return result;
    }

    @Override
    public CouponUserCountDataVO countCouponUserByUserId(Long userId) {
        //过去180天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 180);
        Date d = c.getTime();
        String startDay = format.format(d);

        //====================== 小程序用户券记录 ======================
        //未使用
        int valid = tCouponUserMapper.list("VALID", userId, d).size();
        //已使用
        int used = tCouponUserMapper.list("USED", userId, d).size();
        //已失效
        int expired = tCouponUserMapper.list("EXPIRED", userId, d).size();


        //====================== crm用户券记录 ======================
        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(userId);
        if (user.isFail()){
            log.info("用户信息查询异常:{}", user.getMsg());
        }else {
            log.info("用户手机号为:{}", user.getData().getPhone());
        }

        CouponGetDto couponGetDto = new CouponGetDto();
        couponGetDto.setPage_index(1);
        couponGetDto.setPage_size(200);
        couponGetDto.setMobile(user.getData().getPhone());
        couponGetDto.setBegin_time(startDay);
        couponGetDto.setEnd_time(format.format(new Date()));

        //未使用
        couponGetDto.setStatus("VALID");
        log.info("调用crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
        ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList1 = crmCouponFeignClient.couponGet(couponGetDto);
        log.info("调用crm查询用户优惠券记录接口返回值为：{}", JSONObject.toJSONString(couponGetList1));
        int crmValid = 0;
        if (couponGetList1.isSuccess()){
            crmValid = (int) couponGetList1.getData().getTotal_count();
        }else {
            log.info("CRM优惠券列表查询异常:{}", couponGetList1.getMsg());
        }
        //已使用
        couponGetDto.setStatus("USED");
        log.info("调用crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
        ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList2 = crmCouponFeignClient.couponGet(couponGetDto);
        log.info("调用crm查询用户优惠券记录接口返回值为：{}", JSONObject.toJSONString(couponGetList2));
        int crmUsed = 0;
        if (couponGetList2.isSuccess()){
            crmUsed = (int) couponGetList2.getData().getTotal_count();
        }else {
            log.info("CRM优惠券列表查询异常:{}", couponGetList2.getMsg());
        }
        //已失效
        couponGetDto.setStatus("EXPIRED");
        log.info("调用crm查询用户优惠券记录接口参数为：{}", JSONObject.toJSONString(couponGetDto));
        ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGetList3 = crmCouponFeignClient.couponGet(couponGetDto);
        log.info("调用crm查询用户优惠券记录接口返回值为：{}", JSONObject.toJSONString(couponGetList3));
        int crmExpired = 0;
        if (couponGetList3.isSuccess()){
            crmExpired = (int) couponGetList3.getData().getTotal_count();
        }else {
            log.info("CRM优惠券列表查询异常:{}", couponGetList3.getMsg());
        }

        CouponUserCountDataVO result = new CouponUserCountDataVO();
        result.setCouponUsedNums(used + crmUsed);
        result.setCouponUsableNums(valid + crmValid);
        result.setCouponExpiredNums(expired + crmExpired);

        return result;
    }


    @Override
    public List<TCouponUserOrderDetailVO> getCouponsByOrderIds(List<Long> orderIds) {
        return tCouponUserMapper.getCouponsByOrderIds(orderIds);
    }

    @Override
    public List<TCouponUserOrderVo> selectByOrderIds(List<Long> orderIds) {
        return tCouponUserMapper.selectByOrderIds(orderIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCRMCoupon(CouponOrderVO couponOrderVO) {
        log.info("同步crm优惠券保存到数据表中,参数信息：{}",JSONObject.toJSONString(couponOrderVO));
        TCouponUser couponUser = tCouponUserMapper.selectByCouponCode(couponOrderVO.getCouponCode());
        if(couponUser != null ){
            log.info("crm优惠券已经存在数据表中，不执行操作。");
            return;
        }

        //查询优惠券状态
        TCoupon tCoupon = tCouponMapper.selectById(couponOrderVO.getCouponId());
        if (ObjectUtil.isEmpty(tCoupon)) {
            log.info("优惠券不存在，无法兑换！，参数信息:{}",JSONObject.toJSONString(couponOrderVO));
            return;
        }
        //获取用户的会员信息
        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(couponOrderVO.getCouponUserId());
        if (user.isFail()){
            log.info("用户信息查询异常:{}", user.getMsg());
            throw new LuckException("用户信息查询异常！");
        }else {
            log.info("用户手机号为:{}", user.getData().getPhone());
        }

        TCouponUser tCouponUser = BeanUtil.copyProperties(couponOrderVO, TCouponUser.class);
        tCouponUser.setUserId(couponOrderVO.getCouponUserId());
        tCouponUser.setStatus(1);
        tCouponUser.setCouponCode(couponOrderVO.getCouponCode());
        tCouponUser.setReceiveTime(new Timestamp(System.currentTimeMillis()));
        tCouponUser.setVipCode(user.getData().getVipcode());
        tCouponUser.setUserName(user.getData().getNickName());
        tCouponUser.setUserPhone(user.getData().getPhone());
        tCouponUser.setUserStartTime(couponOrderVO.getStartTime());
        tCouponUser.setUserEndTime(couponOrderVO.getEndTime());
        tCouponUser.setStaffId(0l);

        tCouponUser.setActivityId(-1l);
        tCouponUser.setActivitySource(-1);

        tCouponUser.setReceiveTime(new Timestamp(System.currentTimeMillis()));//
        tCouponUser.setCouponSourceType((short) 2);
        tCouponUser.setCreateId(-1l);
        tCouponUserMapper.insert(tCouponUser);
    }

    private TCouponUser tCouponUser(ReceiveCouponDTO param){
        //查询优惠券状态
        TCoupon tCoupon = tCouponMapper.selectById(param.getCouponId());
        if (ObjectUtil.isEmpty(tCoupon)) {
            throw new LuckException("优惠券不存在，无法兑换！");
        }
        if (tCoupon.getStatus() == TCouponStatus.DEL.value()) {
            throw new LuckException("优惠券已失效，无法兑换！");
        }

        //获取用户的会员信息
        ServerResponseEntity<UserApiVO> user = userFeignClient.getUserAndOpenIdsByUserId(param.getUserId());
        if (user.isFail()){
            log.info("用户信息查询异常:{}", user.getMsg());
            throw new LuckException("用户信息查询异常！");
        }else {
            log.info("用户手机号为:{}", user.getData().getPhone());
        }

        //新增用户领取记录
        TCouponUser tCouponUser = BeanUtil.copyProperties(param, TCouponUser.class);
        tCouponUser.setStatus(1);
        tCouponUser.setReceiveTime(new Timestamp(System.currentTimeMillis()));
        tCouponUser.setVipCode(user.getData().getVipcode());
        tCouponUser.setUserName(user.getData().getNickName());
        tCouponUser.setUserPhone(user.getData().getPhone());
        //优惠券生效时间为固定时间
        if (Objects.equals(tCoupon.getTimeType(), 1)) {
            if (tCoupon.getEndTime().getTime() < System.currentTimeMillis()) {
                throw new LuckException("优惠券已过期，无法兑换！");
            }else {
                tCouponUser.setUserStartTime(tCoupon.getStartTime());
                tCouponUser.setUserEndTime(tCoupon.getEndTime());
            }
        } else {
            // 生效时间类型为领取后生效
            tCouponUser.setUserStartTime(new Date());
            tCouponUser.setUserEndTime(DateUtil.offsetDay(new Date(), tCoupon.getAfterReceiveDays()));
        }


        //判断优惠券类型
        if (tCoupon.getSourceType() == 2){
            //crm优惠券，调用crm接口，获取优惠券码
            CouponReleaseDto couponReleaseDto = new CouponReleaseDto();
            couponReleaseDto.setMobile(user.getData().getPhone());
            couponReleaseDto.setWechat_coupon_rule_id(tCoupon.getId().toString());
            couponReleaseDto.setPoints(0);
            couponReleaseDto.setRequest_id(RandomUtil.getUniqueKey(true));

            log.info("调用crm发券接口的参数为：{}",JSONObject.toJSONString(couponReleaseDto));
            ServerResponseEntity<CouponReleaseResp> couponReleaseRespServerResponse = crmCouponFeignClient.couponRelease(couponReleaseDto);
            log.info("调用crm发券接口的返回值为：{}",JSONObject.toJSONString(couponReleaseRespServerResponse));
            if (couponReleaseRespServerResponse.isFail()){
                throw new LuckException(couponReleaseRespServerResponse.getMsg());
            }

            insertPushUserInfo(tCouponUser,param,user.getData());

            tCouponUser.setCouponSourceType((short) 2);
            tCouponUser.setCouponCode(couponReleaseRespServerResponse.getData().getCoupon_code());
            couponWxMsg(tCoupon,tCouponUser);
            return tCouponUser;
        }else {

            //券码导入的券
            if (tCoupon.getKind() == 2){
//                TCouponCode tCouponCode = tCouponCodeMapper.getLimitOne(new LambdaQueryWrapper<TCouponCode>().eq(TCouponCode::getCouponId, tCoupon.getId()).eq(TCouponCode::getStatus, 0));
                TCouponCode tCouponCode = tCouponCodeMapper.getLimitOne(tCoupon.getId(),0);
                if (tCouponCode == null ) {
                    throw new LuckException("该优惠券库存不足!");
                }
//                tCouponUser.setCouponCode("DS" + tCouponCode.getCouponCode());
                tCouponUser.setCouponCode(tCouponCode.getCouponCode());
                int result = tCouponCodeMapper.receive(tCouponCode.getId());
                if(result == 0){
                    Assert.faild("活动火爆，请稍后再试！");
                }
            }else {
                tCouponUser.setCouponCode("DS" + RandomUtil.getUniqueKey(false));
            }

            CouponStateUpdateDto couponStateUpdateDto = new CouponStateUpdateDto();
            couponStateUpdateDto.setCoupon_id(tCoupon.getId()+"");
            couponStateUpdateDto.setCoupon_code(tCouponUser.getCouponCode());
            couponStateUpdateDto.setMobile(user.getData().getPhone());
            couponStateUpdateDto.setOper("release");
            log.info("调用crm发(小程序)券接口的参数为：{}",JSONObject.toJSONString(couponStateUpdateDto));
            ServerResponseEntity responseEntity = crmCouponFeignClient.couponStateUpdate(couponStateUpdateDto);
            log.info("调用crm发(小程序)券接口的返回值为：{}",JSONObject.toJSONString(responseEntity));
            if (responseEntity.isFail()){
                throw new LuckException(responseEntity.getMsg());
            }

            //券码导入的券
//            if (tCoupon.getKind() == 2){
//                List<TCouponCode> tCouponCodes = tCouponCodeMapper.selectList(new LambdaQueryWrapper<TCouponCode>().eq(TCouponCode::getCouponId, tCoupon.getId()).eq(TCouponCode::getStatus, 0));
//                TCouponCode tCouponCode = tCouponCodes.get(0);
//                tCouponCode.setStatus((short) 1);
//                int result = tCouponCodeMapper.updateById(tCouponCode);
//            }

            insertPushUserInfo(tCouponUser,param,user.getData());

            //系统发放的券
//            if (param.getActivitySource().equals(ActivitySourceEnum.SYSTEM_COUPON.value())){
//                tCouponUser.setCreateId(param.getCreateId());
//                tCouponUser.setCreateName(param.getCreateName());
//                tCouponUser.setCreatePhone(param.getCreatePhone());
//            }else {
//                tCouponUser.setCreateId(param.getUserId());
//                tCouponUser.setCreateName(user.getData().getNickName());
//                tCouponUser.setCreatePhone(user.getData().getPhone());
//            }
//
//            //门店信息
//            if (ObjectUtil.isNotEmpty(param.getShopId())){
//                StoreVO store = storeFeignClient.findByStoreId(param.getShopId());
//                log.info("优惠券查询门店信息为：{}",JSONObject.toJSONString(store));
//                if (ObjectUtil.isNotEmpty(store)){
//                    tCouponUser.setShopName(store.getName());
//                }
//            }

            tCouponUser.setCouponSourceType((short) 1);

            couponWxMsg(tCoupon,tCouponUser);
            return tCouponUser;
        }
    }

    private void insertPushUserInfo(TCouponUser tCouponUser,ReceiveCouponDTO param,UserApiVO userApiVO){
        //系统发放的券
        if (param.getActivitySource().equals(ActivitySourceEnum.SYSTEM_COUPON.value())) {
            tCouponUser.setCreateId(param.getCreateId());
            tCouponUser.setCreateName(param.getCreateName());
            tCouponUser.setCreatePhone(param.getCreatePhone());
        }else if(param.getActivitySource().equals(ActivitySourceEnum.PUSH.value())){//推券中心
            tCouponUser.setCreateId(param.getStaffId());
            tCouponUser.setCreateName(param.getStaffName());
            tCouponUser.setCreatePhone(param.getStaffPhone());
        }else {
            tCouponUser.setCreateId(param.getUserId());
            tCouponUser.setCreateName(userApiVO.getNickName());
            tCouponUser.setCreatePhone(userApiVO.getPhone());
        }
        //门店信息
        if (ObjectUtil.isNotEmpty(param.getShopId())){
            StoreVO store = storeFeignClient.findByStoreId(param.getShopId());
            log.info("优惠券查询门店信息为：{}",JSONObject.toJSONString(store));
            if (ObjectUtil.isNotEmpty(store)){
                tCouponUser.setShopName(store.getName());
            }
        }
    }

    private void couponWxMsg(TCoupon tCoupon,TCouponUser tCouponUser){
        //不影响正常业务，捕获这里可能的异常
        try {
            //系统发券发送订阅消息
            //查询订阅模版
            List<String> busIds = new ArrayList<>();
            busIds.add(tCouponUser.getUserId().toString());
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getinsIderSubscriptTmessage(MaSendTypeEnum.COUPON_ARRIVAL.getValue(), busIds);
            log.info("优惠券到账提醒订阅模版: {}", JSONObject.toJSONString(subscriptResp));
            if (subscriptResp.isSuccess()) {
                //获取订阅用户
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();
                if (!CollectionUtils.isEmpty(userRecords)){
                    List<WeixinMaSubscriptTmessageSendVO> sendList = userRecords.stream().map(user -> {
                        //计算优惠券过期时间
                        String expiresDate = "";
                        if(tCoupon.getTimeType()==1){
                            expiresDate = com.mall4j.cloud.common.util.DateUtils.dateToString(new Date());
                        }else{
                            expiresDate = DateUtil.offsetDay(new Date(),tCoupon.getAfterReceiveDays()).toString();
                        }
                        /**
                         * 当前优惠券到账提醒场景下:优惠券名称{couponName}、到期时间{expiresDate}、获取时间{getDate}、商家名称{shopName}、使用说明{useRemark}、温馨提示{remark}
                         * 构建参数map.
                         */
                        Map<String, String> paramMap = new HashMap();
                        paramMap.put("{couponName}", tCoupon.getName());
                        paramMap.put("{expiresDate}", expiresDate);
                        paramMap.put("{getDate}", com.mall4j.cloud.common.util.DateUtils.dateToString(new Date()));
                        paramMap.put("{useRemark}", tCoupon.getDescription());
                        paramMap.put("{remark}", tCoupon.getDescription());

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
                        return sendVO;
                    }).collect(Collectors.toList());
                    log.info(JSONObject.toJSONString(sendList));
                    sendMaSubcriptMessageTemplate.syncSend(sendList);
                }
            }else {
                log.error("优惠券到账提醒订阅模版，返回值为：{}", JSONObject.toJSONString(subscriptResp));
            }
        }catch (Exception e){
            log.error("发放优惠券发送订阅消息执行失败",e);
        }
    }

    @Override
    public List<Long> getCouponUserIds(QueryHasCouponUsersRequest queryHasCouponUsersRequest) {
        return tCouponUserMapper.getCouponUserIds(queryHasCouponUsersRequest);
    }

    @Override
    public String couponActivityDetailExport(CouponActivityDTO param) {
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(CouponActivityDetailExport.PUll_COUPON_ACTIVITY_DETAIL_FILE_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downloadCenterId=null;
            if(serverResponseEntity.isSuccess()){
                downloadCenterId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            CouponActivityDetailExport couponActivityDetailExport=new CouponActivityDetailExport();
            couponActivityDetailExport.setCouponActivityDTO(param);
            couponActivityDetailExport.setDownloadCenterId(downloadCenterId);
            applicationContext.publishEvent(couponActivityDetailExport);

            return "操作成功，请转至下载中心下载";
        }catch (Exception e){
            log.error("导出券明细错误: "+e.getMessage(),e);
            return "操作失败";
        }
    }

    @Override
    public List<SyncPointConvertCouponDto> getSyncPointConvertCouponByBatchIds(List<Long> batchIds) {
        return tCouponUserMapper.getSyncPointConvertCouponByBatchIds(batchIds);
    }
    
    @Override
    public List<PaperCouponOrderVO> listPaperCouponOrder(Date startTime, Date endTime) {
        return tCouponUserMapper.listPaperCouponOrder(startTime, endTime);
    }
    
}
