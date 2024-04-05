package com.mall4j.cloud.transfer.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.common.constant.DistributedIdKey;
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
import java.util.stream.Collectors;

/**
 * @luzhengxiang
 * @create 2022-04-06 12:45 PM
 **/
@Slf4j
@Service
public class UserService {

    @Autowired
    DPSMember295Mapper dpsMember295Mapper;
    @Autowired
    StoreFeignClient storeFeignClient;
    @Autowired
    private SegmentFeignClient segmentFeignClient;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserExtensionMapper userExtensionMapper;
    @Autowired
    AuthAccountMapper authAccountMapper;
    @Autowired
    CrmVipInfo1Mapper crmVipInfo1Mapper;
    @Autowired
    AuthSocialMapper authSocialMapper;


    /**
     * 用户数据迁移
     * 数据来源表：crm_vip_info1  d_p_s_member_295
     * <p>
     * 目标表
     * user
     * auth_account
     * auth_social
     * user_extension
     */
    @Async
    public void userTransfer() {
        int currentPage = 1;
        int pageSize = 5000;


        log.info("用户同步执行开始。");
        long startTime = System.currentTimeMillis();
        //分页处理，每次处理2000条数据批量保存。
        PageHelper.startPage(currentPage, pageSize);
        List<DPSMember295> member295s = dpsMember295Mapper.list();
        PageInfo<DPSMember295> pageInfo = new PageInfo(member295s);
        createUsers(member295s, currentPage);

        int totalPage = pageInfo.getPages();
        int totalCount = (int) pageInfo.getTotal();
        for (int i = 2; i <= totalPage; i++) {
            PageHelper.startPage(i, pageSize);
            List<DPSMember295> newMember295s = dpsMember295Mapper.list();
            createUsers(newMember295s, i);
        }
        log.info("用户同步执行结束，累计耗时:{}ms", System.currentTimeMillis() - startTime);
    }

    private void createUsers(List<DPSMember295> member295s, int currentPage) {
        //获取shopCodes
        List<String> shopCodes = member295s.stream().map(DPSMember295::getShopcode).distinct().collect(Collectors.toList());
        List<StoreCodeVO> storeCodeVO = storeFeignClient.findByStoreCodes(shopCodes);
        Map<String, StoreCodeVO> storeMap = storeCodeVO.stream().collect(Collectors.toMap(StoreCodeVO::getStoreCode, p -> p));

        List<User> users = new ArrayList<>();
        List<UserExtension> userExtensions = new ArrayList<>();
        List<AuthAccount> authAccounts = new ArrayList<>();
        List<AuthSocial> authSocials = new ArrayList<>();
        for (DPSMember295 member295 : member295s) {
            CrmVipInfo1 crmVipInfo1 = crmVipInfo1Mapper.getByOldCode(member295.getId());
            if (crmVipInfo1 == null) {
                crmVipInfo1 = crmVipInfo1Mapper.getByOldCode2(member295.getId());
                if (crmVipInfo1 == null) {
                    crmVipInfo1 = crmVipInfo1Mapper.getByOldCode3(member295.getId());
                    if (crmVipInfo1 == null) {
                        crmVipInfo1 = crmVipInfo1Mapper.getByOldCode4(member295.getId());
                    }
                }
            }
            if (crmVipInfo1 == null) {
                log.info("oldCode:{},在CrmVipInfo1表中不存在，不执行同步", member295.getId());
                continue;
            }


            User user = new User();
            user.setUserId(crmVipInfo1.getId());//id
//            user.setUserId(crmVipInfo1.getId());//id
            //创建时间在我们这里为注册时间。
            user.setCreateTime(member295.getRegistertime());
            user.setUpdateTime(member295.getUpdatetime());
            user.setNickName(crmVipInfo1.getNickname());//用户昵称

            user.setSex(member295.getGender());//1(男) or 0(女)
            user.setBirthDate(DateUtil.format(member295.getDateofbirth(), "yyyy-MM-dd"));//生日
            //todo 没有
            user.setPic(crmVipInfo1.getWxheadimg());//头像图片路径
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
            StoreCodeVO storeCodeVO1 = storeMap.get(member295.getShopcode());
            if (storeCodeVO1 != null) {
                user.setStoreId(storeCodeVO1.getStoreId());//门店ID
            } else {
                user.setStoreId(1L);//默认门店ID给1
            }
            user.setOrgId(0L);//组织ID
            //todo 获取字段
            user.setVipcode(member295.getId());//关联crm会员id
            //todo 望伟 获取字段
//            user.setStaffId();//所属导购ID
            user.setVeekerStatus(-1);//微客状态 -1-初始化 0-禁用 1-启用 2-拉黑
            user.setVeekerAuditStatus(-1);//微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝
//            user.setVeekerApplyTime();//微客申请时间

            user.setAddWechat(0);//是否添加微信 0-否 1-是(已废弃,是否添加通过关系表判断)

            user.setUnionId(crmVipInfo1.getWxunionid());//union_id
            user.setOpenId(crmVipInfo1.getWxno());//小程序openId
//            user.setRefereePhone();//推荐人手机号
            user.setCustomerName(member295.getMembername());//姓名
            user.setEmail(member295.getEmail());//邮箱
            user.setJob(member295.getJob());//职业
            user.setHaveBaby(member295.getFirstbabygender());//宝宝性别,M男宝宝，F女宝宝
            user.setBabyBirthday(DateUtil.format(member295.getFirstbabybirth(), "yyyy-MM-dd"));//baby_birthday
            user.setInterests(member295.getInterests());//兴趣
            user.setHobby(member295.getHobby());//爱好
            user.setSecondBabySex(member295.getSecondbabygender());//二宝性别,M男宝宝，F女宝宝
            user.setSecondBabyBirth(DateUtil.format(member295.getSecondbabybirth(), "yyyy-MM-dd"));//二宝生日,yyyy-MM-dd
            user.setThirdBabySex(member295.getThirdbabygender());//三宝性别,M男宝宝，F女宝宝
            user.setThirdBabyBirth(DateUtil.format(member295.getThirdbabybirth(), "yyyy-MM-dd"));//三宝生日,yyyy-MM-dd
//            user.setProvinceId();//省ID
            user.setProvince(member295.getProvincename());//省
//            user.setCityId();//城市ID
            user.setCity(member295.getCityname());//城市
//            user.setAreaId();//区ID
            user.setArea(member295.getDistrictname());//区
            user.setName(member295.getMembername());//会员姓名 看到数据都为空
            if (crmVipInfo1.getServicesaler() != null) {
                user.setStaffId(crmVipInfo1.getServicesaler().longValue());
            }
            users.add(user);


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
            if (StrUtil.isNotEmpty(member295.getMobile())) {
                authAccounts.add(authAccount);
            }


//            if (StrUtil.isNotEmpty(crmVipInfo1.getWxno())) {
                AuthSocial authSocial = new AuthSocial();
                authSocial.setUid(uid);
                authSocial.setCreateTime(member295.getCreatetime());
                authSocial.setUpdateTime(member295.getUpdatetime());
                authSocial.setSocialType(1);
                authSocial.setBizUserId(crmVipInfo1.getWxno());
                authSocial.setBizUnionid(crmVipInfo1.getWxunionid());
                authSocials.add(authSocial);
//            }

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
            userMapper.batchSave(users);
        }

        if (CollUtil.isNotEmpty(authAccounts)) {
            authAccountMapper.batchSave(authAccounts);
        }

        if (CollUtil.isNotEmpty(authSocials)) {
            authSocialMapper.batchSave(authSocials);
        }

        if (CollUtil.isNotEmpty(userExtensions)) {
            userExtensionMapper.batchSave(userExtensions);
        }
        log.info("第{}业数据执行完毕，users执行条数{}。", currentPage, users.size());
    }


    @Async
    public void userSupplementTransfer() {

        //查询member
        int currentPage = 1;
        int pageSize = 2000;

        log.info("补充用户同步执行开始。");
        long startTime = System.currentTimeMillis();

        /**
         *
         * 这里用同样的方法，把四张表数据都转移到了crm_vip_info1_copy2
         * insert into crm_vip_info1_copy2
         * select * from crm_vip_info1
         * where Id not in (select user_id from `user`)
         * and WxUnionID is not null and OldCode is not null;
         *
         */
        String[] tables = {"crm_vip_info_all_add"};

        for (String table : tables) {
            //分页处理，每次处理1000条数据批量保存。
            PageHelper.startPage(currentPage, pageSize);
            List<CrmVipInfo1> vipInfo1s = crmVipInfo1Mapper.listByTable(table);
            PageInfo<CrmVipInfo1> pageInfo = new PageInfo(vipInfo1s);
            userSupplementcreateUsers(vipInfo1s, table, currentPage);

            int totalPage = pageInfo.getPages();
            int totalCount = (int) pageInfo.getTotal();
            for (int i = 2; i <= totalPage; i++) {
                PageHelper.startPage(i, pageSize);
                List<CrmVipInfo1> newvipInfo1s = crmVipInfo1Mapper.listByTable(table);
//                PageInfo<CrmVipInfo1> newpageInfo = new PageInfo(newvipInfo1s);
                userSupplementcreateUsers(newvipInfo1s, table, i);
            }
            currentPage = 1;
        }

        log.info("补充用户同步执行结束，累计耗时:{}ms", System.currentTimeMillis() - startTime);
    }


    private void userSupplementcreateUsers(List<CrmVipInfo1> vipInfo1s, String table, int currentPage) {


        List<User> users = new ArrayList<>();
        List<UserExtension> userExtensions = new ArrayList<>();
        List<AuthAccount> authAccounts = new ArrayList<>();
        List<AuthSocial> authSocials = new ArrayList<>();

        List<DPSMember295> member295s = new ArrayList<>();

        for (CrmVipInfo1 crmVipInfo1 : vipInfo1s) {

            DPSMember295 member295 = dpsMember295Mapper.getById(crmVipInfo1.getOldcode());
            if(member295 == null){
                log.info("当前用户在crm表中不存在，不插入当前用户数据.vipinfo.id:{}, vipOldCode:{}",crmVipInfo1.getId(),crmVipInfo1.getOldcode());
//                continue;
            }else{
                member295s.add(member295);
            }


            User user = new User();
            user.setUserId(crmVipInfo1.getId());//id
//            user.setUserId(crmVipInfo1.getId());//id
            //创建时间在我们这里为注册时间。
            user.setCreateTime(crmVipInfo1.getRegtime());
            user.setUpdateTime(crmVipInfo1.getLastmodifieddate());
            user.setNickName(crmVipInfo1.getNickname());//用户昵称


            String sex = "";
            if("1".equals(crmVipInfo1.getSex())){
                sex = "M";
            }
            if("2".equals(crmVipInfo1.getSex())){
                sex = "F";
            }
            user.setSex(sex);//1(男) or 0(女)
            user.setBirthDate(crmVipInfo1.getBirthday());//生日
            user.setPic(crmVipInfo1.getWxheadimg());//头像图片路径
            user.setStatus(1);//状态 1 正常 0 无效
            user.setLevel(1L);//会员等级（冗余字段）
//            user.setVipEndTime();//vip结束时间
            user.setLevelType(0);//等级条件 0 普通会员 1 付费会员
            user.setPhone(crmVipInfo1.getMobileno());//手机号 (冗余字段)
//            user.setVipLevel();//用户vip等级，为空则非付费会员

            user.setOrgId(0L);//组织ID
            //todo 获取字段
            user.setVipcode(crmVipInfo1.getOldcode());//关联crm会员id
            //todo 望伟 获取字段
//            user.setStaffId();//所属导购ID
            user.setVeekerStatus(-1);//微客状态 -1-初始化 0-禁用 1-启用 2-拉黑
            user.setVeekerAuditStatus(-1);//微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝
//            user.setVeekerApplyTime();//微客申请时间

            user.setAddWechat(0);//是否添加微信 0-否 1-是(已废弃,是否添加通过关系表判断)

            user.setUnionId(crmVipInfo1.getWxunionid());//union_id
            user.setOpenId(crmVipInfo1.getWxno());//小程序openId
//            user.setRefereePhone();//推荐人手机号
            user.setCustomerName(crmVipInfo1.getName());//姓名
            user.setEmail(crmVipInfo1.getEmail());//邮箱


            if(member295!=null){
                user.setJob(member295.getJob());//职业
                user.setHaveBaby(member295.getFirstbabygender());//宝宝性别,M男宝宝，F女宝宝
                user.setBabyBirthday(DateUtil.format(member295.getFirstbabybirth(), "yyyy-MM-dd"));//baby_birthday
                user.setInterests(member295.getInterests());//兴趣
                user.setHobby(member295.getHobby());//爱好
                user.setSecondBabySex(member295.getSecondbabygender());//二宝性别,M男宝宝，F女宝宝
                user.setSecondBabyBirth(DateUtil.format(member295.getSecondbabybirth(), "yyyy-MM-dd"));//二宝生日,yyyy-MM-dd
                user.setThirdBabySex(member295.getThirdbabygender());//三宝性别,M男宝宝，F女宝宝
                user.setThirdBabyBirth(DateUtil.format(member295.getThirdbabybirth(), "yyyy-MM-dd"));//三宝生日,yyyy-MM-dd


                user.setProvince(member295.getProvincename());//省
                user.setCity(member295.getCityname());//城市
                user.setArea(member295.getDistrictname());//区

//                user.setProvince(crmVipInfo1.getProvince());//省
//                user.setCity(crmVipInfo1.getCity());//城市
//                user.setArea(crmVipInfo1.getCounty());//区
            }

            user.setName(crmVipInfo1.getName());//会员姓名 看到数据都为空
            if (crmVipInfo1.getServicesaler() != null) {
                user.setStaffId(crmVipInfo1.getServicesaler().longValue());
            }
            users.add(user);


            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_AUTH_USER);
            Long uid = segmentIdResponse.getData();
            AuthAccount authAccount = new AuthAccount();
            authAccount.setUid(uid);
            authAccount.setCreateIp("");//创建ip
            authAccount.setEmail(crmVipInfo1.getEmail());
            authAccount.setPhone(crmVipInfo1.getMobileno());
            authAccount.setSysType(0);
            authAccount.setUserId(user.getUserId());
            authAccount.setIsAdmin(0);
            authAccount.setStatus(0);
            authAccount.setUsername(crmVipInfo1.getMobileno());
            authAccount.setCreateTime(crmVipInfo1.getRegtime());
            authAccount.setUpdateTime(crmVipInfo1.getLastmodifieddate());
            //如果手机号为空，不保存当前用户的authAccount记录
            if (StrUtil.isNotEmpty(crmVipInfo1.getMobileno())) {
                authAccounts.add(authAccount);
            }


//            if (StrUtil.isNotEmpty(crmVipInfo1.getWxno())) {
                AuthSocial authSocial = new AuthSocial();
                authSocial.setUid(uid);
                authSocial.setCreateTime(crmVipInfo1.getRegtime());
                authSocial.setUpdateTime(crmVipInfo1.getLastmodifieddate());
                authSocial.setSocialType(1);
                authSocial.setBizUserId(crmVipInfo1.getWxno());
                authSocial.setBizUnionid(crmVipInfo1.getWxunionid());
                authSocials.add(authSocial);
//            }

            UserExtension userExtension = new UserExtension();
            userExtension.setCreateTime(crmVipInfo1.getRegtime());
            userExtension.setUpdateTime(crmVipInfo1.getLastmodifieddate());
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


        //获取shopCodes
        List<String> shopCodes = new ArrayList<>();
        List<StoreCodeVO> storeCodeVO = new ArrayList<>();
        if(CollUtil.isNotEmpty(member295s)){
            shopCodes = member295s.stream().map(DPSMember295::getShopcode).distinct().collect(Collectors.toList());
            storeCodeVO = storeFeignClient.findByStoreCodes(shopCodes);
        }

        Map<String, StoreCodeVO> storeMap = new HashMap<>();
        if(CollUtil.isNotEmpty(storeCodeVO)){
            storeMap = storeCodeVO.stream().collect(Collectors.toMap(StoreCodeVO::getStoreCode, p -> p));
        }

        Map<String, DPSMember295> memberMap = member295s.stream().collect(Collectors.toMap(DPSMember295::getId, p -> p));


        for (User user : users) {
            /**
             * 查询门店获取
             */
            DPSMember295 member295 = memberMap.get(user.getVipcode());
            if(member295==null){
                user.setStoreId(1L);//默认门店ID给1
                continue;
            }

            StoreCodeVO storeCodeVO1 = storeMap.get(member295.getShopcode());
            if (storeCodeVO1 != null) {
                user.setStoreId(storeCodeVO1.getStoreId());//门店ID
            } else {
                user.setStoreId(1L);//默认门店ID给1
            }
        }


        if (CollUtil.isNotEmpty(users)) {
            userMapper.batchSave2(users);
        }

        if (CollUtil.isNotEmpty(authAccounts)) {
            authAccountMapper.batchSave2(authAccounts);
        }

        if (CollUtil.isNotEmpty(authSocials)) {
            authSocialMapper.batchSave2(authSocials);
        }

        if (CollUtil.isNotEmpty(userExtensions)) {
            userExtensionMapper.batchSave2(userExtensions);
        }
        log.info("table :{},第{}业数据执行完毕，users执行条数{}。",table, currentPage, users.size());
    }


    private void buildVipInfoUser(List<User> users,List<UserExtension> userExtensions,List<AuthAccount> authAccounts,
                                  List<AuthSocial> authSocials,CrmVipInfo1 crmVipInfo1){


//        User user = new User();
//        user.setUserId(crmVipInfo1.getId());//id
////            user.setUserId(crmVipInfo1.getId());//id
//        //创建时间在我们这里为注册时间。
//        user.setCreateTime(crmVipInfo1.getRegtime());
//        user.setUpdateTime(crmVipInfo1.getLastmodifieddate());
//        user.setNickName(crmVipInfo1.getNickname());//用户昵称
//
//
//        user.setSex("0".equals(crmVipInfo1.getSex()));//m(男) or f(女)
//        user.setBirthDate(DateUtil.format(member295.getDateofbirth(), "yyyy-MM-dd"));//生日
//        user.setPic(crmVipInfo1.getWxheadimg());//头像图片路径
//        user.setStatus(1);//状态 1 正常 0 无效
//        user.setLevel(1L);//会员等级（冗余字段）
////            user.setVipEndTime();//vip结束时间
//        user.setLevelType(0);//等级条件 0 普通会员 1 付费会员
//        user.setPhone(member295.getMobile());//手机号 (冗余字段)
////            user.setVipLevel();//用户vip等级，为空则非付费会员
//
//        user.setOrgId(0L);//组织ID
//        //todo 获取字段
//        user.setVipcode(member295.getId());//关联crm会员id
//        //todo 望伟 获取字段
////            user.setStaffId();//所属导购ID
//        user.setVeekerStatus(-1);//微客状态 -1-初始化 0-禁用 1-启用 2-拉黑
//        user.setVeekerAuditStatus(-1);//微客审核状态 -1-初始化 0-待审核 1-已同意 2-已拒绝
////            user.setVeekerApplyTime();//微客申请时间
//
//        user.setAddWechat(0);//是否添加微信 0-否 1-是(已废弃,是否添加通过关系表判断)
//
//        user.setUnionId(crmVipInfo1.getWxunionid());//union_id
//        user.setOpenId(crmVipInfo1.getWxno());//小程序openId
////            user.setRefereePhone();//推荐人手机号
//        user.setCustomerName(member295.getMembername());//姓名
//        user.setEmail(member295.getEmail());//邮箱
//        user.setJob(member295.getJob());//职业
//        user.setHaveBaby(member295.getFirstbabygender());//宝宝性别,M男宝宝，F女宝宝
//        user.setBabyBirthday(DateUtil.format(member295.getFirstbabybirth(), "yyyy-MM-dd"));//baby_birthday
//        user.setInterests(member295.getInterests());//兴趣
//        user.setHobby(member295.getHobby());//爱好
//        user.setSecondBabySex(member295.getSecondbabygender());//二宝性别,M男宝宝，F女宝宝
//        user.setSecondBabyBirth(DateUtil.format(member295.getSecondbabybirth(), "yyyy-MM-dd"));//二宝生日,yyyy-MM-dd
//        user.setThirdBabySex(member295.getThirdbabygender());//三宝性别,M男宝宝，F女宝宝
//        user.setThirdBabyBirth(DateUtil.format(member295.getThirdbabybirth(), "yyyy-MM-dd"));//三宝生日,yyyy-MM-dd
////            user.setProvinceId();//省ID
//        user.setProvince(member295.getProvincename());//省
////            user.setCityId();//城市ID
//        user.setCity(member295.getCityname());//城市
////            user.setAreaId();//区ID
//        user.setArea(member295.getDistrictname());//区
//        user.setName(member295.getMembername());//会员姓名 看到数据都为空
//        if (crmVipInfo1.getServicesaler() != null) {
//            user.setStaffId(crmVipInfo1.getServicesaler().longValue());
//        }
//        users.add(user);
//
//
//        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_AUTH_USER);
//        Long uid = segmentIdResponse.getData();
//        AuthAccount authAccount = new AuthAccount();
//        authAccount.setUid(uid);
//        authAccount.setCreateIp("");//创建ip
//        authAccount.setEmail(member295.getEmail());
//        authAccount.setPhone(member295.getMobile());
//        authAccount.setSysType(0);
//        authAccount.setUserId(user.getUserId());
//        authAccount.setIsAdmin(0);
//        authAccount.setStatus(0);
//        authAccount.setUsername(member295.getMobile());
//        authAccount.setCreateTime(member295.getCreatetime());
//        authAccount.setUpdateTime(member295.getUpdatetime());
//        //如果手机号为空，不保存当前用户的authAccount记录
//        if (StrUtil.isNotEmpty(member295.getMobile())) {
//            authAccounts.add(authAccount);
//        }
//
//
//        if (StrUtil.isNotEmpty(crmVipInfo1.getWxno())) {
//            AuthSocial authSocial = new AuthSocial();
//            authSocial.setUid(uid);
//            authSocial.setCreateTime(member295.getCreatetime());
//            authSocial.setUpdateTime(member295.getUpdatetime());
//            authSocial.setSocialType(1);
//            authSocial.setBizUserId(crmVipInfo1.getWxno());
//            authSocial.setBizUnionid(crmVipInfo1.getWxunionid());
//            authSocials.add(authSocial);
//        }
//
//        UserExtension userExtension = new UserExtension();
//        userExtension.setCreateTime(member295.getCreatetime());
//        userExtension.setUpdateTime(member295.getUpdatetime());
//        userExtension.setUserId(user.getUserId());//用户id
//        userExtension.setLevel(1);//用户等级
//        userExtension.setLevelType(0);//等级条件 0 普通会员 1 付费会员
//        userExtension.setGrowth(0);//用户当前成长值
//        userExtension.setScore(0L);//用户积分
//        userExtension.setBalance(0L);//用户总余额
//        userExtension.setActualBalance(0L);//用户实际余额
//        userExtension.setVersion(0);//乐观锁
//        userExtension.setSignDay(0);//连续签到天数
//        userExtensions.add(userExtension);
    }




}
