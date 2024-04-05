package com.mall4j.cloud.transfer.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.transfer.mapper.*;
import com.mall4j.cloud.transfer.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-04-14 2:49 PM
 **/
@Slf4j
@Service
public class EtoTransferService {

    @Autowired
    EtoGiftsMapper etoGiftsMapper;
    @Autowired
    EtoGiftsRecordMapper etoGiftsRecordMapper;
    @Autowired
    ScoreConvertMapper scoreConvertMapper;
    @Autowired
    ScoreCouponLogMapper scoreCouponLogMapper;
    @Autowired
    DPSMember295Mapper dpsMember295Mapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CrmVipInfo1Mapper crmVipInfo1Mapper;
    @Autowired
    SegmentFeignClient segmentFeignClient;
    @Autowired
    AuthAccountMapper authAccountMapper;
    @Autowired
    UserExtensionMapper userExtensionMapper;
    @Autowired
    EtoMemberMapper etoMemberMapper;
    @Autowired
    AuthSocialMapper authSocialMapper;

    @Async
    public void edtoTransfer(){
        log.info("eto同步执行开始。");
        long startTime = System.currentTimeMillis();



        etoMemberTransfer();


        etoGiftsTransfer();

        log.info("eto同步执行结束，执行耗时：{}ms。",System.currentTimeMillis() - startTime);
    }

    private void etoMemberTransfer(){
        int currentPage = 1;
        int pageSize = 5000;

        log.info("eto用户同步开始。");
        long startTime = System.currentTimeMillis();
        //分页处理，每次处理2000条数据批量保存。
        PageHelper.startPage(currentPage, pageSize);
        List<EtoMember> EtoMembers = etoMemberMapper.list();
        PageInfo<EtoMember> pageInfo = new PageInfo(EtoMembers);
        createUser(EtoMembers, currentPage);

        int totalPage = pageInfo.getPages();
        int totalCount = (int)pageInfo.getTotal();
        for(int i = 2; i<=totalPage; i++){
            PageHelper.startPage(i, pageSize);
            List<EtoMember> newEtoMembers = etoMemberMapper.list();
            createUser(newEtoMembers, currentPage);
        }
        log.info("eto用户同步执行结束，累计耗时:{}ms",System.currentTimeMillis() - startTime);
    }


    private void etoGiftsTransfer(){
        List<EtoGifts> etoGifts = etoGiftsMapper.list();
        for (EtoGifts etoGift : etoGifts) {
            ScoreConvert scoreConvert = new ScoreConvert();
            scoreConvert.setConvertTitle(etoGift.getTitle());//标题
            scoreConvert.setConvertScore(Long.parseLong(etoGift.getCost()));//积分兑换数
            scoreConvert.setConvertUrl("");//积分活动封面
            scoreConvert.setMaxAmount(0L);//限制兑换总数
            scoreConvert.setStocks(0);//库存
            scoreConvert.setPersonMaxAmount(0L);//每人限制兑换数
            scoreConvert.setConvertType(1);//兑换活动种类（0：积分换物/1：积分换券）
            scoreConvert.setType(1);//积分换券活动类型（0：积分兑礼/1：积分换券）
            scoreConvert.setConvertStatus(1);//兑换状态（0：启用/1：停用）
            scoreConvert.setSort(100L);//排序
//            scoreConvert.setCouponId();//优惠券id
            scoreConvert.setIsAllShop(0);//是否全部门店
            scoreConvert.setIsAllConvertShop(0);//是否全部门店（兑换门店）
            scoreConvert.setIsAllCouponShop(0);//是否全部门店（优惠券）
            scoreConvert.setDescription("");//描述
            scoreConvert.setCommodityName("");//商品名称
            scoreConvert.setCommodityImgUrl("");//商品图片
            scoreConvert.setDeliveryType(0);//发货方式（0：邮寄/1：门店自取）
            scoreConvert.setStartTime(new Date());//开始时间
            scoreConvert.setEndTime(new Date());//结束时间
            scoreConvert.setDel(0);//是否删除（0：未删除/1：已删除）
            scoreConvert.setVersion(0);//版本号
            scoreConvert.setCreateId(-1L);
            scoreConvert.setCreateTime(new Date());
            scoreConvert.setUpdateId(-1L);
            scoreConvert.setUpdateTime(new Date());
            scoreConvertMapper.save(scoreConvert);
            etoGiftsRecordTransfer(etoGift,scoreConvert);
        }
    }



    private void createUser(List<EtoMember> etoMembers, int currentPage) {

        List<User> users = new ArrayList<>();
        List<UserExtension> userExtensions = new ArrayList<>();
        List<AuthAccount> authAccounts = new ArrayList<>();
        List<AuthSocial> authSocials = new ArrayList<>();

        for (EtoMember etoMember : etoMembers) {

            DPSMember295 member295 = dpsMember295Mapper.getById(etoMember.getMemberid());
            if(member295==null){
                log.error("vipcode:{},在crm不存在，不执行同步。",etoMember.getMemberid());
                continue;
            }

//            User exitsUser = userMapper.getByVipCode2(etoMember.getMemberid());
            User exitsUser = userMapper.getByunionId2(etoMember.getUnionid());
            if(exitsUser!=null){
                if(StrUtil.equals(member295.getMobile(),exitsUser.getPhone())){
                    userMapper.updateVipCodeById(member295.getId(),exitsUser.getUserId());
                    log.error("vipcode:{},用户已存在且手机号码与数据库中存在的不相同。修改成crm的vipcode：{}",etoMember.getMemberid(),member295.getId());
                }
                log.error("vipcode:{},用户已存在。",etoMember.getMemberid());
                continue;
            }



            ServerResponseEntity<Long> userIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_USER);
            Long userId = userIdResponse.getData();
            if(userId==null){
                log.error("number:{},获取用户id失败。",etoMember.getMemberid());
                continue;
            }

            User user = new User();
            user.setUserId(userId);//id
//            user.setUserId(crmVipInfo1.getId());//id
            user.setCreateTime(member295.getCreatetime());
            user.setUpdateTime(member295.getUpdatetime());
            user.setNickName(member295.getNick());//用户昵称

            user.setSex(member295.getGender());//1(男) or 0(女)
            user.setBirthDate(DateUtil.format(member295.getDateofbirth(),"yyyy-MM-dd"));//生日
            //todo 没有
//            user.setPic();//头像图片路径
            //todo 通过什么字段判断用户状态
            user.setStatus(1);//状态 1 正常 0 无效
            user.setLevel(1L);//会员等级（冗余字段）
//            user.setVipEndTime();//vip结束时间
            user.setLevelType(0);//等级条件 0 普通会员 1 付费会员
            user.setPhone(member295.getMobile());//手机号 (冗余字段)
//            user.setVipLevel();//用户vip等级，为空则非付费会员
            /**
             * 查询门店获取
             */
            user.setStoreId(0L);//门店ID
            user.setOrgId(0L);//组织ID
            //todo 获取字段
            user.setVipcode(member295.getId());//关联crm会员id
            //todo 望伟 获取字段
//            user.setStaffId();//所属导购ID
            user.setVeekerStatus(-1);//微客状态 -1-初始化 0-禁用 1-启用 2-拉黑
            user.setVeekerAuditStatus(-1);//微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝
//            user.setVeekerApplyTime();//微客申请时间

            user.setAddWechat(0);//是否添加微信 0-否 1-是(已废弃,是否添加通过关系表判断)

            user.setUnionId(etoMember.getUnionid());//union_id
//        user.setOpenId(crmVipInfo1.getWxno());//小程序openId
//            user.setRefereePhone();//推荐人手机号
            user.setCustomerName(member295.getMembername());//姓名
            user.setEmail(member295.getEmail());//邮箱
            user.setJob(member295.getJob());//职业
            user.setHaveBaby(member295.getFirstbabygender());//宝宝性别,M男宝宝，F女宝宝
            user.setBabyBirthday(DateUtil.format(member295.getFirstbabybirth(),"yyyy-MM-dd"));//baby_birthday
            user.setInterests(member295.getInterests());//兴趣
            user.setHobby(member295.getHobby());//爱好
            user.setSecondBabySex(member295.getSecondbabygender());//二宝性别,M男宝宝，F女宝宝
            user.setSecondBabyBirth(DateUtil.format(member295.getSecondbabybirth(),"yyyy-MM-dd"));//二宝生日,yyyy-MM-dd
            user.setThirdBabySex(member295.getThirdbabygender());//三宝性别,M男宝宝，F女宝宝
            user.setThirdBabyBirth(DateUtil.format(member295.getThirdbabybirth(),"yyyy-MM-dd"));//三宝生日,yyyy-MM-dd
//            user.setProvinceId();//省ID
            user.setProvince(member295.getProvincename());//省
//            user.setCityId();//城市ID
            user.setCity(member295.getCityname());//城市
//            user.setAreaId();//区ID
            user.setArea(member295.getDistrictname());//区
            user.setName(member295.getMembername());//会员姓名 看到数据都为空
//        if(crmVipInfo1.getServicesaler()!=null){
//            user.setStaffId(crmVipInfo1.getServicesaler().longValue());
//        }
            users.add(user);
//        users.add(user);


            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_AUTH_USER);
            Long uid = segmentIdResponse.getData();
            AuthAccount authAccount = new AuthAccount();
            authAccount.setUid(uid);
            authAccount.setCreateIp("");//创建ip
            authAccount.setEmail(member295.getEmail());
            authAccount.setPhone(member295.getMobile());
            authAccount.setSysType(0);
            authAccount.setUserId(user.getUserId());
            authAccount.setIsAdmin(0);
            authAccount.setStatus(0);
            authAccount.setUsername(member295.getMobile());
            authAccount.setCreateTime(member295.getCreatetime());
            authAccount.setUpdateTime(member295.getUpdatetime());
            //如果手机号为空，不保存当前用户的authAccount记录
            if(StrUtil.isNotEmpty(member295.getMobile())){
                authAccounts.add(authAccount);
            }

            AuthSocial authSocial = new AuthSocial();
            authSocial.setUid(uid);
            authSocial.setCreateTime(member295.getCreatetime());
            authSocial.setUpdateTime(member295.getUpdatetime());
            authSocial.setSocialType(1);
//                authSocial.setBizUserId(crmVipInfo1.getWxno());
            authSocial.setBizUnionid(etoMember.getUnionid());
            authSocials.add(authSocial);

            UserExtension userExtension = new UserExtension();
            userExtension.setCreateTime(member295.getCreatetime());
            userExtension.setUpdateTime(member295.getUpdatetime());
            userExtension.setUserId(user.getUserId());//用户id
            userExtension.setLevel(1);//用户等级
            userExtension.setLevelType(0);//等级条件 0 普通会员 1 付费会员
            userExtension.setGrowth(0);//用户当前成长值
            userExtension.setScore(0L);//用户积分
            userExtension.setBalance(0L);//用户总余额
            userExtension.setActualBalance(0L);//用户实际余额
            userExtension.setVersion(0);//乐观锁
            userExtension.setSignDay(0);//连续签到天数
            userExtensions.add(userExtension);
        }


        if (CollUtil.isNotEmpty(users)) {
            userMapper.batchSave3(users);
        }

        if (CollUtil.isNotEmpty(authAccounts)) {
            authAccountMapper.batchSave3(authAccounts);
        }

        if (CollUtil.isNotEmpty(authSocials)) {
            authSocialMapper.batchSave3(authSocials);
        }

        if (CollUtil.isNotEmpty(userExtensions)) {
            userExtensionMapper.batchSave3(userExtensions);
        }
        log.info("第{}业数据执行完毕，users执行条数{}。", currentPage, users.size());
    }

    private void etoGiftsRecordTransfer(EtoGifts etoGift,ScoreConvert scoreConvert){
        List<EtoGiftsRecord> giftsRecords = etoGiftsRecordMapper.listByGiftsId(etoGift.getId());
        log.info("giftid:{},领券记录数:{}",etoGift.getId(),giftsRecords==null?0:giftsRecords.size());
        for (EtoGiftsRecord record : giftsRecords) {

            User user = getUserByEtoMemberId(record.getUserid());
            if(user == null){
                log.info("用户number：{}，查找不到对应的记录，不保存当前领取记录。",record.getNumber());
                continue;
            }
            ScoreCouponLog scoreCouponLog = new ScoreCouponLog();
            scoreCouponLog.setConvertId(scoreConvert.getConvertId());
            scoreCouponLog.setType(1);
            scoreCouponLog.setUserId(user.getUserId());
//                scoreCouponLog.setUserCardNumber();
            scoreCouponLog.setUserPhone(user.getPhone());
            scoreCouponLog.setConvertScore(Long.parseLong(record.getCostIntegral()));
//                scoreCouponLog.setNote();
            scoreCouponLog.setCreateTime(DateUtil.parseDateTime(record.getCreatedAt()));
            scoreCouponLog.setCreateUserId(-1L);
            scoreCouponLogMapper.save(scoreCouponLog);
        }
    }

    private User getUser(String number){
        User exitsUser = userMapper.getByVipCode2(number);
        if(exitsUser!=null){
            return exitsUser;
        }
//        User exitsUser3 = userMapper.getByVipCode3(number);
//        if(exitsUser3!=null){
//            return exitsUser3;
//        }
        return null;
    }

    private User getUserByEtoMemberId(Long etomemberId){
        EtoMember etoMember = etoMemberMapper.getById(etomemberId);
        if(etoMember!=null){
            return userMapper.getByunionIdAll(etoMember.getUnionid());
        }
        return null;
    }



}
