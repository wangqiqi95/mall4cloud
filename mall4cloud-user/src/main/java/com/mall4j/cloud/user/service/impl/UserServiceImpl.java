package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.api.auth.dto.AuthAccountWithSocialDTO;
import com.mall4j.cloud.api.auth.dto.AuthSocialDTO;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.api.coupon.dto.QueryCrmIdsDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.api.crm.feign.CrmCouponClient;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.PointDetailGetDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerPointFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.PointDetailVo;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.constant.StaffRoleTypeEnum;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.feign.SysUserClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.platform.vo.TentacleVo;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.api.user.dto.UserChangeServiceStoreDTO;
import com.mall4j.cloud.api.user.dto.UserDynamicCodeDTO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.*;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.UserOrderStatisticVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.IpHelper;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.constant.CouponFlayTypeEnum;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.constant.scoreConvert.ScoreSourceEnum;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.manager.GroupPushTaskVipRelationManager;
import com.mall4j.cloud.user.manager.GroupSonTaskSendRecordManager;
import com.mall4j.cloud.user.manager.UserManager;
import com.mall4j.cloud.user.manager.UserTagRelationManager;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.UserChangeRecord;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.StaffGetUserDetailByMaterialVO;
import com.mall4j.cloud.user.vo.StaffUserVo;
import com.mall4j.cloud.user.vo.UserDynamicCodeVO;
import com.mall4j.cloud.user.vo.UserLevelVO;
import com.mall4j.cloud.user.vo.UserVO;
import io.seata.common.util.CollectionUtils;
import io.seata.common.util.StringUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@Slf4j
@Service
@RefreshScope
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Value("${mall4cloud.expose.permission:}")
    private Boolean permission;
    
    @Value("${encrypt.vipcode.switch:false}")
    private Boolean encryptSwitch;
    
    @Value("${encrypt.vipcode.expires:180}")
    private int expires;
    
    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private AccountFeignClient accountFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SegmentFeignClient segmentFeignClient;
//    @Autowired
//    private OnsMQTemplate userNotifyDistributionUserTemplate;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;
    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;
    @Resource
    private SysUserClient sysUserClient;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    UserWeixinAccountFollowService userWeixinAccountFollowService;
    @Autowired
    private UserChangeRecordService userChangeRecordService;
    @Autowired
    private UserTagRelationManager userTagRelationManager;
    @Autowired
    private GroupSonTaskSendRecordManager groupSonTaskSendRecordManager;
    @Autowired
    private GroupPushTaskVipRelationManager groupPushTaskVipRelationManager;
    @Resource
    private TCouponFeignClient tCouponFeignClient;
    @Autowired
    private CrmCouponClient crmCouponClient;
    @Resource
    private CrmCustomerPointFeignClient crmCustomerPointFeignClient;

    @Override
    public PageVO<UserApiVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userMapper.listUser());
    }

    /**
     * 企微添加好友同时注册用户表信息，根据unionId
     * @param relation
     */
    @Override
    public void createUserByUnionId(UserStaffCpRelation relation) {
        String unionId=relation.getUserUnionId();
        UserApiVO userApiVO = userMapper.getByUnionId(unionId);
        Long userId=Objects.nonNull(userApiVO)?userApiVO.getUserId():null;
        log.info("企微添加好友同时注册用户表信息 userApiVO:{}",JSON.toJSONString(userApiVO));
        if(Objects.isNull(userApiVO)){
            User user=new User();
            user.setNickName(relation.getQiWeiNickName());
            user.setStaffId(relation.getStaffId());
            user.setUnionId(unionId);
            user.setCreateTime(new Date());
            user.setStatus(0);
            user.setCreateBy("cp_add_friend");
            this.save(user);
            userId=user.getUserId();
        }
        //更新好友关系表
        relation.setUserId(userId);
        userStaffCpRelationService.updateById(relation);
    }

    /**
     * 根据企微客户unionId唯一创建会员信息
     * @param relations
     */
    @Override
    public void batchCreateUserByUnionId(List<UserStaffCpRelation> relations) {
        List<String> unionIds=relations.stream().filter(item->StrUtil.isNotEmpty(item.getUserUnionId())).map(item->item.getUserUnionId()).distinct().collect(Collectors.toList());
        if(CollUtil.isEmpty(unionIds)){
            return;
        }
        //根据unionId批量获取会员信息
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.in(User::getUnionId,unionIds);
        List<User> users=this.list(lambdaQueryWrapper);
        List<User> addUpdateUsers=new ArrayList<>();
        //多个企微客户可能存在重复数据，因为单个客户可以添加多个员工，但是unionId是唯一的
        Map<String,UserStaffCpRelation> relationMap=relations.stream().collect(Collectors.toMap(UserStaffCpRelation::getUserUnionId, s->s, (v1, v2)->v2));
        if(CollUtil.isEmpty(users)){
            for (Map.Entry<String,UserStaffCpRelation> entry : relationMap.entrySet()) {
                User user=new User();
                user.setNickName(entry.getValue().getQiWeiNickName());
                user.setStaffId(entry.getValue().getStaffId());
                user.setUnionId(entry.getValue().getUserUnionId());
                user.setCreateTime(new Date());
                user.setStatus(0);
                user.setCreateBy("cp_add_friend");
                addUpdateUsers.add(user);
            }
        }else{
            Map<String,User> userMap=users.stream().collect(Collectors.toMap(User::getUnionId, s->s, (v1, v2)->v2));
            for (Map.Entry<String,UserStaffCpRelation> entry : relationMap.entrySet()) {
                if(!userMap.containsKey(entry.getValue().getUserUnionId())){
                    User user=new User();
                    user.setNickName(entry.getValue().getQiWeiNickName());
                    user.setStaffId(entry.getValue().getStaffId());
                    user.setUnionId(entry.getValue().getUserUnionId());
                    user.setCreateTime(new Date());
                    user.setStatus(0);
                    user.setCreateBy("cp_add_friend");
                    addUpdateUsers.add(user);
                }
            }
        }

        if(CollUtil.isNotEmpty(addUpdateUsers)){
            //保存会员信息
            this.saveBatch(addUpdateUsers);

            //更新客户关系表数据与会员信息关联
            List<UserStaffCpRelation> updateRelations=new ArrayList<>();
            Map<String,User> userMap=addUpdateUsers.stream().collect(Collectors.toMap(User::getUnionId, s->s, (v1, v2)->v2));
            for (UserStaffCpRelation relation : relations) {
                if(userMap.containsKey(relation.getUserUnionId())){
                    UserStaffCpRelation relationUpdate=new UserStaffCpRelation();
                    relationUpdate.setId(relation.getId());
                    relationUpdate.setUserId(userMap.get(relation.getUserUnionId()).getUserId());
                    updateRelations.add(relationUpdate);
                }
            }
            if(CollUtil.isNotEmpty(updateRelations)){
                userStaffCpRelationService.updateBatchById(updateRelations);
            }
        }
    }


    @Override
    @Cacheable(cacheNames = UserCacheNames.USER_INFO, key = "#userId")
    public UserApiVO getByUserId(Long userId) {
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        userApiVO.setLevel(1);
        UserLevelVO ordinaryLevel = userLevelService.getOneByTypeAndLevel(LevelTypeEnum.ORDINARY_USER.value(), userApiVO.getLevel());
        if (Objects.nonNull(userApiVO.getVipLevel())) {
            UserLevelVO payLevel = userLevelService.getOneByTypeAndLevel(LevelTypeEnum.PAY_USER.value(), userApiVO.getVipLevel());
            userApiVO.setVipUserLevelId(payLevel.getUserLevelId());
            userApiVO.setVipLevelName(payLevel.getLevelName());
        }
        userApiVO.setUserLevelId(ordinaryLevel.getUserLevelId());
        userApiVO.setLevelName(ordinaryLevel.getLevelName());
        userApiVO.setUserMobile(userApiVO.getPhone());
        if (Objects.nonNull(userApiVO.getStoreId()) && userApiVO.getStoreId() > 0) {
            StoreVO storeVO = storeFeignClient.findByStoreId(userApiVO.getStoreId());
            userApiVO.setStoreName(storeVO.getName());
        }
        userApiVO.setIdentity(0);
        if (Optional.ofNullable(userApiVO.getVeekerStatus()).orElse(0).equals(1)){
            userApiVO.setIdentity(1);
        }
        if (StringUtils.isNotEmpty(userApiVO.getPhone())){
            ServerResponseEntity<StaffVO> currentStaff = staffFeignClient.getStaffByMobile(userApiVO.getPhone());
            if (currentStaff.isSuccess() && null != currentStaff.getData() && currentStaff.getData().getStatus() == 0){
                userApiVO.setIdentity(2);
                userApiVO.setCurrentStaffId(currentStaff.getData().getId());
            }
        }
        if (Objects.nonNull(userApiVO.getStaffId()) && userApiVO.getStaffId() > 0) {
            ServerResponseEntity<StaffVO> staffRep = staffFeignClient.getStaffById(userApiVO.getStaffId());
            if (staffRep.isSuccess() && Objects.nonNull(staffRep.getData())) {
                StaffVO staffVO = staffRep.getData();
                userApiVO.setStaffName(staffVO.getStaffName());
                userApiVO.setStaffStoreId(staffVO.getStoreId());
                userApiVO.setStaffStoreName(staffVO.getStoreName());
                userApiVO.setStaffNo(staffVO.getStaffNo());
                userApiVO.setStaffStatus(staffVO.getStatus());
            }
        }
        return userApiVO;
    }

    @Override
    public UserApiVO getByUnionId(String unionId) {
        return userMapper.getByUnionId(unionId);
    }

    @Override
    public UserApiVO getByOpenId(String openid) {
        return userMapper.getByUnionId(openid);
    }

    @Override
    public UserApiVO getByUserIdMp(Long userId) {
        User user = userMapper.selectById(userId);
        UserApiVO userApiVO = mapperFacade.map(user,UserApiVO.class);
        return userApiVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = UserCacheNames.USER_INFO, key = "#user.userId")
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public void updateUserToCrm(User user) {
    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_INFO, key = "#userId")
    public void removeUserCacheByUserId(Long userId) {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLevel(Integer level, int minGrowth, Integer maxGrowth) {
        userMapper.updateUserLevel(level, minGrowth, maxGrowth);
        userExtensionService.updateUserLevel(level, minGrowth, maxGrowth);
    }

    @Override
    public List<UserApiVO> getUserByUserIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userMapper.getUserByUserIds(userIds);
    }

    @Override
    public UserApiVO checkUserExist(String phone, String unionId) {
        return userMapper.checkUserExist(phone,unionId);
    }

    @Override
    public UserApiVO getUserAndOpenIdsByUserId(Long userId) {
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        List<String> ids = accountFeignClient.getBizUserIdListByUserId(userId).getData();
        userApiVO.setBizUserIdList(ids);
        return userApiVO;
    }

    @Override
    public UserApiVO getUserAndOpenIdsByUserId(String unionId) {
        UserApiVO userApiVO = userMapper.getByUnionId(unionId);
        return userApiVO;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Long> save(UserRegisterDTO param, AuthSocialVO authSocial,User user) {
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_USER);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long userId = segmentIdResponse.getData();

        param.setUserId(userId);

        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setEmail(param.getEmail());
        authAccountDTO.setPassword(param.getPassword());
        authAccountDTO.setPhone(param.getMobile());
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        authAccountDTO.setUsername(param.getUserName());
        authAccountDTO.setStatus(1);
        authAccountDTO.setUserId(userId);

        AuthAccountWithSocialDTO authAccountWithSocialDTO = new AuthAccountWithSocialDTO();
        if (authSocial != null) {
            AuthSocialDTO authSocialDTO = mapperFacade.map(authSocial, AuthSocialDTO.class);
            authAccountWithSocialDTO.setAuthSocial(authSocialDTO);
        }
        authAccountWithSocialDTO.setAuthAccount(authAccountDTO);


        // 保存统一账户信息
        ServerResponseEntity<Long> serverResponse = accountFeignClient.saveAccountWithSocial(authAccountWithSocialDTO);
        // 抛异常回滚
        if (!serverResponse.isSuccess()) {
            throw new LuckException(serverResponse.getMsg());
        }
        //设置默认门店信息

//        User user = new User();

        if (StrUtil.isNotBlank(authSocial.getBizUnionid())) {
            user.setUnionId(authSocial.getBizUnionid());
        }
        user.setUserId(userId);
        user.setPhone(authAccountDTO.getPhone());
        // 这里保存之后才有用户id
        UserApiVO userByMobile = getUserByMobile(authAccountDTO.getPhone());
        if (userByMobile != null) {
            user = BeanUtil.copyProperties(userByMobile,User.class);
            if (StrUtil.isNotBlank(authSocial.getBizUnionid())) {
                user.setUnionId(authSocial.getBizUnionid());
            }
            updateUser(user);
        } else {
            this.save(user);
            //同步更新crm
            this.updateUserToCrm(user);
        }
        if(StrUtil.isNotEmpty(user.getUnionId())){
            try {
                userWeixinAccountFollowService.syncFollw(user.getUnionId());
            }catch (Exception e){
                log.error("同步公众号数据到crm失败",e);
            }
        }


        UserExtension userExtension = new UserExtension();
        userExtension.setBalance(0L);
        userExtension.setActualBalance(0L);
        userExtension.setGrowth(0);
        userExtension.setLevel(1);
        userExtension.setLevelType(0);
        userExtension.setScore(0L);
        userExtension.setVersion(0);
        userExtension.setSignDay(0);
        // 上面保存这边才能拿到userId
        userExtension.setUserId(user.getUserId());

        userExtensionService.save(userExtension);

        //用户注册成功后初始化用户数据
        userLevelService.initUserInfoAndLevelInfo(userExtension, Constant.USER_LEVEL_INIT, LevelTypeEnum.ORDINARY_USER.value(), authAccountDTO.getPhone());

        // 好友关系表绑定userId
        userStaffCpRelationService.bindUserId(user.getUnionId(), userId,user.getStaffId());
        Map<String,Long> data = new HashMap<>();
        data.put("uid",serverResponse.getData());
        data.put("userId",user.getUserId());
        return data;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void update(UserRegisterDTO param, User user) {

        userMapper.updateById(user);
        UserExtension userExtension = new UserExtension();
        userExtension.setBalance(0L);
        userExtension.setActualBalance(0L);
        userExtension.setGrowth(0);
        userExtension.setLevel(1);
        userExtension.setLevelType(0);
        userExtension.setScore(0L);
        userExtension.setVersion(0);
        userExtension.setSignDay(0);
        // 上面保存这边才能拿到userId
        userExtension.setUserId(user.getUserId());

        userExtensionService.save(userExtension);

    }

    private void buildUser(User user, UserRegisterDTO param) {
        user.setStoreId(param.getStoreId());
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffByMobile(user.getPhone());
        if (staffResp.isSuccess() && Objects.nonNull(staffResp.getData())
                && staffResp.getData().getStatus() == 0) {
            // 当前会员如果是导购 所属导购为自己
            user.setStaffId(staffResp.getData().getId());
        } else {
            if (StrUtil.isNotBlank(param.getTentacleNo())) {
                //查询触点携带的门店id及staffId
                ServerResponseEntity<TentacleContentVO> byTentacleNo = tentacleContentFeignClient.findByTentacleNo(param.getTentacleNo());
                if (byTentacleNo.isSuccess()) {
                    TentacleVo tentacle = byTentacleNo.getData().getTentacle();
                    if (tentacle.getTentacleType() == 4) {
                        user.setStaffId(tentacle.getBusinessId());
                    }
                }
            }
        }
        //其余拓展字段
        user.setVipcode(param.getVipCode());
    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_INFO, key = "#userId")
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void updateUserMobile(Long userId, String mobile) {
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setUserId(userId);
        authAccountDTO.setPhone(mobile);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        ServerResponseEntity<Void> voidServerResponseEntity = accountFeignClient.updateUserMobile(authAccountDTO);
        if (!voidServerResponseEntity.isSuccess()) {
            throw new LuckException(voidServerResponseEntity.getMsg());
        }
        // 更新user表手机号
        User user = new User();
        user.setUserId(userId);
        user.setPhone(mobile);
        userMapper.updateUser(user);
    }

    @Override
    public PageVO<UserManagerVO> getUserInfoPage(PageDTO pageDTO, UserManagerDTO userManagerDTO) {
        PageVO<UserManagerVO> pageVO = new PageVO<>();
        List<Long> staffIdList = getQueryStaffIdList(userManagerDTO);
        // 用户信息
        userManagerDTO.setStaffList(staffIdList);
        Long userTotal = !CollectionUtils.isEmpty(staffIdList) && staffIdList.size() == 0 ? 0l :
                userMapper.countUserPageByParam(userManagerDTO);
        pageVO.setTotal(userTotal);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        if (Objects.equals(0L, userTotal)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }
        List<UserManagerVO> userList = getUserInfoList(new PageAdapter(pageDTO), userManagerDTO);
        pageVO.setList(userList);
        return pageVO;
    }

    @Override
//    public List<UserManagerVO> getUserInfoList(PageDTO pageDTO, UserManagerDTO userManagerDTO) {
    public List<UserManagerVO> getUserInfoList(PageAdapter pageAdapter, UserManagerDTO userManagerDTO) {
        // 从用户表中查询数据，并且初始化一些相关订单变量的数据，就不用在后面再次设置相关相关初始值了
//        List<UserManagerVO> userList = userMapper.listUserByParam(userManagerDTO, new PageAdapter(pageDTO));
        List<UserManagerVO> userList = userMapper.listUserByParam(userManagerDTO, pageAdapter);
        // 组装门店 & 导购信息
        Map<Long, StoreVO> storeVOMap = new HashMap<>();
        Map<Long, StaffVO> staffVOMap = new HashMap<>();
        List<Long> staffIdList = userList.stream().filter(u -> u.getStaffId() != null && u.getStaffId() > 0).map(UserManagerVO :: getStaffId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(staffIdList)) {
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffIdList(staffIdList);
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (!staffData.isSuccess()){
                return new ArrayList<>();
            }
            staffVOMap = staffData.getData().stream().filter(s -> s.getStatus() == 0).collect(Collectors.toMap(StaffVO :: getId, Function.identity()));
        }
        List<Long> storeIdList = userList.stream().filter(s -> s.getStoreId() != null).map(UserManagerVO:: getStoreId)
                .collect(Collectors.toList());
        List<Long> serviceStoreIdList = userList.stream().filter(s -> s.getStaffStoreId() != null).map(UserManagerVO:: getStaffStoreId)
            .collect(Collectors.toList());
        storeIdList.addAll(serviceStoreIdList);
        log.info("服务门店 {}", storeIdList);
        if (CollectionUtils.isNotEmpty(storeIdList)) {
            //大量查询需要post
            ServerResponseEntity<List<StoreVO>> storeResp = storeFeignClient.listBypByStoreIdList(storeIdList);
            if (storeResp.isSuccess()) {
                storeVOMap = storeResp.getData().stream().collect(Collectors.toMap(StoreVO :: getStoreId, Function.identity()));
            }
        }
        for (UserManagerVO userManagerVO : userList) {
            // 将余额由分转为元
//            BigDecimal currentBalance = userManagerVO.getCurrentBalance();
//            BigDecimal sumBalance = userManagerVO.getSumBalance();
//            userManagerVO.setCurrentBalance(PriceUtil.toDecimalPrice(currentBalance.longValue()));
//            userManagerVO.setSumBalance(PriceUtil.toDecimalPrice(sumBalance.longValue()));
            userManagerVO.setRechargeAmount(PriceUtil.toDecimalPrice(userManagerVO.getRechargeAmount().longValue()));
            //读取到环境变量后隐藏手机号码
            if (BooleanUtil.isFalse(permission)) {
                userManagerVO.setPhone(PhoneUtil.hideBetween(userManagerVO.getPhone()).toString());
            }
            // 导购
            if (userManagerVO.getStaffId() != null) {
                StaffVO staffVO = staffVOMap.get(userManagerVO.getStaffId());
                if (Objects.nonNull(staffVO)) {
                    userManagerVO.setStaffName(staffVO.getStaffName());
                    userManagerVO.setStaffNameNo(staffVO.getStaffNo());
                }
            }
            // 服务门店
            if (userManagerVO.getStaffStoreId() != null) {
                StoreVO storeVO = storeVOMap.get(Long.valueOf(userManagerVO.getStaffStoreId()));
                if (Objects.nonNull(storeVO)) {
                    userManagerVO.setStaffStoreName(storeVO.getName());
                    userManagerVO.setStaffStoreCode(storeVO.getStoreCode());
                }
            }
            // 注册门店
            if (userManagerVO.getStoreId() != null) {
                StoreVO storeVO = storeVOMap.get(userManagerVO.getStoreId());

                if (Objects.nonNull(storeVO)) {
                    userManagerVO.setStoreName(storeVO.getName());
                    userManagerVO.setStoreCode(storeVO.getStoreCode());
                }
            }
            //注册来源
            if(Objects.nonNull(userManagerVO.getRegSource())){

            }
        }
        return userList;
    }

    @Override
    public UserManagerVO getUserInfo(Long userId) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(0);
        pageDTO.setPageSize(1);
        UserManagerDTO userManagerDTO = new UserManagerDTO();
        userManagerDTO.setUserIds(Collections.singletonList(userId));
        PageVO<UserManagerVO> infoPage = getUserInfoPage(pageDTO, userManagerDTO);
        if (infoPage.getTotal() < 1) {
            throw new LuckException("该用户不存在");
        }
        UserManagerVO userManagerVO = infoPage.getList().get(0);
        return userManagerVO;
    }

    @Override
    public void updateUserLevelByUserExtensionAndUserIds(List<Long> userIds) {
        userMapper.updateUserLevelByUserExtensionAndUserIds(userIds);
    }


    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_INFO, key = "#userDTO.userId")
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        Integer status = userDTO.getStatus();
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        boolean isUpdateStatus = Objects.equals(userApiVO.getStatus(), status);
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setNickName(userDTO.getNickName());
        if (!isUpdateStatus) {
            user.setStatus(status);
        }
        user.setUpdateTime(DateUtil.date());
        // 更新用户表信息
        this.updateUser(user);
        // 如果用户状态未发生改变，就不需要修改账户表中用户的状态
        if (isUpdateStatus) {
            return;
        }
        // 修改用户状态时需要修改auth_account表的状态,同时需要将用户下线
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setUserId(user.getUserId());
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        authAccountDTO.setStatus(user.getStatus());
        ServerResponseEntity<Void> updateResponse = accountFeignClient.updateAuthAccountStatusAndDeleteUserToken(authAccountDTO);
        if (!Objects.equals(updateResponse.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(updateResponse.getMsg());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserStaff(ChangeStaffDTO changeStaffDTO) {
        if (CollectionUtil.isEmpty(changeStaffDTO.getUserId())) {
            throw new LuckException("用户id集合不能为空");
        }

        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffById(changeStaffDTO.getStaffId());
        if (!staffResp.isSuccess() || Objects.isNull(staffResp.getData())) {
            throw new LuckException("员工不存在");
        }
        StaffVO staffVO = staffResp.getData();
        if (staffVO.getStatus() == 1) {
            throw new LuckException("不能分配给已离职的员工");
        }

        List<User> users = this.listByIds(changeStaffDTO.getUserId());
        List<UserChangeRecord> recordList = new ArrayList<>();
        Long userId = AuthUserContext.get().getUserId();
        ServerResponseEntity<SysUserVO> byUserId = sysUserClient.getSysUserByUserId(userId);
        String adminNickName = "";
        if (byUserId.isSuccess() && byUserId.getData() != null) {
            adminNickName = byUserId.getData().getNickName();
        }
        StoreVO afterStore = storeFeignClient.findByStoreId(staffVO.getStoreId());
        for (User user : users) {

            Long afterServiceStoreId = staffVO.getStoreId();
            if (afterServiceStoreId.equals(user.getServiceStoreId())) {
                log.info("修改前后服务门店相同 无需变更：{}",JSONObject.toJSONString(user));
            } else {
                UserChangeRecord userChangeRecord = new UserChangeRecord();
                userChangeRecord.setUserId(user.getUserId());
                StoreVO beforeStore = storeFeignClient.findByStoreId(user.getServiceStoreId());
                if (beforeStore == null) {
                    userChangeRecord.setBeforeId(user.getServiceStoreId());
                    userChangeRecord.setBeforeName("无");
                } else {
                    userChangeRecord.setBeforeId(beforeStore.getStoreId());
                    userChangeRecord.setBeforeName(beforeStore.getName());
                }
                if (afterStore == null) {
                    userChangeRecord.setAfterId(staffVO.getStoreId());
                    userChangeRecord.setAfterName("无");
                } else {
                    userChangeRecord.setAfterId(afterStore.getStoreId());
                    userChangeRecord.setAfterName(afterStore.getName());
                }
                userChangeRecord.setType(0);
                userChangeRecord.setSource(2);
                userChangeRecord.setCreator(adminNickName);
                recordList.add(userChangeRecord);
            }

            if (staffVO.getId().equals(user.getStaffId())) {
                log.info("修改前后导购相同 无需变更：{}", JSONObject.toJSONString(user));
            } else {
                UserChangeRecord userChangeRecord = new UserChangeRecord();
                userChangeRecord.setUserId(user.getUserId());
                userChangeRecord.setBeforeId(user.getStaffId());
                ServerResponseEntity<StaffVO> staffById = staffFeignClient.getStaffById(user.getStaffId());
                if (staffById.isSuccess() && staffById.getData() != null) {
                    userChangeRecord.setBeforeName(staffById.getData().getStaffName());
                } else {
                    userChangeRecord.setBeforeName("无");
                }
                userChangeRecord.setAfterId(staffVO.getId());
                userChangeRecord.setAfterName(staffVO.getStaffName());
                userChangeRecord.setType(1);
                userChangeRecord.setSource(2);
                userChangeRecord.setCreator(adminNickName);
                recordList.add(userChangeRecord);
            }
        }
        // 修改会员的服务门店
        update(new LambdaUpdateWrapper<User>().in(User::getUserId, changeStaffDTO.getUserId())
            .set(User::getServiceStoreId, staffVO.getStoreId()));

        // 记录变更记录
        userChangeRecordService.saveBatch(recordList);

        userMapper.batchBindStaff(changeStaffDTO.getUserId(), changeStaffDTO.getStaffId());
    }

    @Override
    public List<UserVO> getUserListByPhones(List<String> phones) {
        if (CollUtil.isEmpty(phones)) {
            return Collections.emptyList();
        }
        return userMapper.getUserListByPhones(phones);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUser(List<User> userList, List<UserExtension> userExtensionList) {
        if (CollUtil.isEmpty(userList)) {
            return;
        }
        // 新增用户
        int row = userMapper.saveBatch(userList);
        if (row == 0) {
            return;
        }
        // userList 的大小 不等于 插入row的行数，所以我们要查询插入的用户是那些
        // 如果是已经存在的userId在 userExtensionList中也不要紧，在保存sql的时候会排除掉数据库中已经存在的userId
        List<Long> userIds = userList.stream().map(User::getUserId).collect(Collectors.toList());
        List<UserApiVO> userApiVOList = userMapper.getUserByUserIds(userIds);
        List<Long> dbUserIds = userApiVOList.stream().map(UserApiVO::getUserId).collect(Collectors.toList());
        userExtensionList = userExtensionList.stream().filter(item -> dbUserIds.contains(item.getUserId())).collect(Collectors.toList());
        userExtensionService.saveBatch(userExtensionList);
        // 积分、成长值、余额日志
        userExtensionService.registerUserScoreGrowthBalanceLog(userExtensionList);
        // 赠送注册积分
        userLevelService.registerUserScore(userExtensionList);
        // 导入后赠送等级相关的积分和权益
        userLevelService.registerUserSendRights(userExtensionList);
    }

    @Override
    public List<UserVO> getUserByCreateTimeRange(Integer status, Date startDate, Date endDate) {
        return userMapper.getUserByCreateTimeRange(status, startDate, endDate);
    }

    @Override
    public MemberOverviewVO getUserCountInfo(Date endTime) {
        Date startTime = DateUtil.beginOfDay(endTime);
        MemberOverviewVO memberOverviewListVO = userMapper.getUserCountInfo(startTime, endTime);
        return memberOverviewListVO;
    }

    @Override
    public Integer countUserByMobile(String mobile) {
        return userMapper.countUserByMobile(mobile);
    }

    @Override
    public Integer countUserByLevel(Integer level, Integer levelType) {
        return userMapper.countUserByLevel(level, levelType);
    }

    @Override
    public List<UserVO> selectMemberByEndTime(DateTime endOfDay) {
        return userMapper.selectMemberByEndTime(endOfDay);
    }

    @Override
    public void updateUserTypeLevelByUserExtensionAndUserIds(List<Long> userIds) {
        userMapper.updateUserTypeLevelByUserExtensionAndUserIds(userIds);
    }

    @Override
    public PageVO<UserApiVO> pageUserByStaff(PageDTO pageDTO, UserQueryDTO queryDTO) {
        // todo 回访 & 消费
        PageVO<UserApiVO> objectPageVO = PageUtil.doPage(pageDTO, () -> userMapper.listByStaff(queryDTO));
        if (CollectionUtils.isNotEmpty(objectPageVO.getList())){
            objectPageVO.getList().forEach(userApiVO -> {
                if(StringUtils.isNotBlank(userApiVO.getName())){
                    userApiVO.setNickName(userApiVO.getName());
                }
                if(StringUtils.isBlank(userApiVO.getName()) && StringUtils.isNotBlank(userApiVO.getCustomerName())){
                    userApiVO.setNickName(userApiVO.getCustomerName());
                }
                if(Objects.nonNull(queryDTO.getStaffId())){
                    userApiVO.setAddWechat(userStaffCpRelationService.getByStaffAndUser(queryDTO.getStaffId(), userApiVO.getUserId()) == null ? 0 : 1);
                }
            });
        }
        return objectPageVO;
    }



    @Override
    public List<UserApiVO> listUserByStaff(UserQueryDTO queryDTO) {
        return userMapper.listByStaff(queryDTO);
    }

    @Override
    public int countStaffUser(UserQueryDTO queryDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", queryDTO.getStaffId());
        if (queryDTO.getLevelType() != null) {
            queryWrapper.eq("level_type", queryDTO.getLevelType());
        }
        if (null != queryDTO.getStartTime() && null != queryDTO.getEndTime()) {
            queryWrapper.between("create_time", queryDTO.getStartTime(), queryDTO.getEndTime());
        }
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public List<StaffUserVo> listStaffUser(PageDTO pageDTO, UserQueryDTO queryDTO) {
        List<StaffUserVo> staffUserVoList = new ArrayList<>();
        for (int i = 0; i < pageDTO.getPageSize(); i++) {
            LocalDateTime now = LocalDateTime.now();
            int number = ((pageDTO.getPageNum() - 1) * pageDTO.getPageSize()) + i;
            if (queryDTO.getCountType() == 1) {
                LocalDateTime localDateTime = now.minusDays(number);
                staffUserVoList.add(buildStaffUser(DateUtil.beginOfDay(new Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())),
                        DateUtil.endOfDay(new Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())), queryDTO.getCountType(), queryDTO.getStaffId()));
            } else if (queryDTO.getCountType() == 2) {
                LocalDateTime localDateTime = now.minusWeeks(number);
                staffUserVoList.add(buildStaffUser(DateUtil.beginOfWeek(new Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())),
                        DateUtil.endOfWeek(new Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())), queryDTO.getCountType(), queryDTO.getStaffId()));
            } else {
                LocalDateTime localDateTime = now.minusMonths(number);
                staffUserVoList.add(buildStaffUser(DateUtil.beginOfMonth(new Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())),
                        DateUtil.endOfMonth(new Date(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli())), queryDTO.getCountType(), queryDTO.getStaffId()));
            }
        }
        return staffUserVoList;
    }

    @Override
    public UserApiVO getUserByMobile(String mobile) {
        return userMapper.getUserByMobile(mobile);
    }

    @Override
    public List<UserApiVO> getUserByStoreId(Long storeId) {
        return userMapper.getUserByStoreId(storeId);
    }

    @Override
    public List<UserApiVO> getUserByStaffId(Long staffId) {
        return userMapper.getUserByStaffId(staffId);
    }

    @Override
    public UserApiVO getUserByVipCode(String vipCode) {
        return userMapper.getUserByVipCode(vipCode);
    }

    @Override
    public DistributionUserVO countUserNum(DistributionUserQueryDTO distributionUserQueryDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        if (null != distributionUserQueryDTO.getStoreId()) {
            queryWrapper.eq("store_id", distributionUserQueryDTO.getStoreId());
        }
        if (distributionUserQueryDTO.isWitkey()) {
            queryWrapper.eq("veeker_audit_status", 2);
            if (null != distributionUserQueryDTO.getStartTime() && null != distributionUserQueryDTO.getEndTime()) {
                queryWrapper.between("veeker_apply_time", distributionUserQueryDTO.getStartTime(), distributionUserQueryDTO.getEndTime());
            }
        } else {
            if (distributionUserQueryDTO.isRecruit()) {
                queryWrapper.ne("staff_id", 0);
            }
            if (null != distributionUserQueryDTO.getStartTime() && null != distributionUserQueryDTO.getEndTime()) {
                queryWrapper.between("create_time", distributionUserQueryDTO.getStartTime(), distributionUserQueryDTO.getEndTime());
            }
        }
        queryWrapper.select("user_id");
        Integer integer = userMapper.selectCount(queryWrapper);
        DistributionUserVO vo = new DistributionUserVO();
        vo.setUserNum(integer);
        return vo;
    }

    @Override
    public List<UserBindStaffDTO> bindStaff(UserBindStaffDTO userBindStaffDTO) {
        List<UserBindStaffDTO> userBindStaffDTOList = new ArrayList<>();
        if (userBindStaffDTO.getBindType() == 1) {
            // 指定分配
            List<Long> userIds = userBindStaffDTO.getUserDataList().stream().map(UserBindStaffUserDataDTO::getUserId)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtil.isEmpty(userIds)) {
                throw new LuckException("用户id集合不能为空");
            }
            // 组装参数，调用服务关系变更方法
            ChangeStaffDTO changeStaffDTO = new ChangeStaffDTO();
            changeStaffDTO.setUserId(userIds);
            changeStaffDTO.setStaffId(userBindStaffDTO.getStaffId());
            log.info("由导购端分配会员导购入口进入——导购与会员服务关系变更方法。入参:{}", JSONObject.toJSONString(changeStaffDTO));
            this.changeUserStaff(changeStaffDTO);

            //userMapper.batchBindStaff(userIds, userBindStaffDTO.getStaffId());
            userBindStaffDTOList.add(userBindStaffDTO);
        }else if (userBindStaffDTO.getBindType() == 2) {
            // 随机分配
            List<Long> userIds = userBindStaffDTO.getUserDataList().stream().map(UserBindStaffUserDataDTO::getUserId)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtil.isEmpty(userIds)) {
                throw new LuckException("用户id集合不能为空");
            }

            Long storeId = AuthUserContext.get().getStoreId();
            if(Objects.isNull(storeId)){
                log.error("会员随机分配导购接口——当前门店信息为空");
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            List<Long> storeIds = new ArrayList<>();
            storeIds.add(storeId);

            randomBindStaff(userIds, storeIds);

            userBindStaffDTOList.add(userBindStaffDTO);
        }
        return userBindStaffDTOList;
    }

    /**
     * 随机分配用户给导购
     * @param userIds 用户列表
     * @param storeIds 当前导购门店，用于查询当前所有导购
     */
    @Async
    public void randomBindStaff(List<Long> userIds, List<Long> storeIds) {

        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        staffQueryDTO.setStoreIdList(storeIds);
        staffQueryDTO.setRoleType(StaffRoleTypeEnum.GUIDE.getValue());
        staffQueryDTO.setStatus(0);
        List<StaffVO> staffVOS = staffFeignClient.findByStaffQueryDTO(staffQueryDTO).getData();
        List<Long> staffIds = staffVOS.stream().map(StaffVO::getId).collect(Collectors.toList());

        log.info("随机分配用户给导购,门店为: {},当前门店用户为: {},当前门店导购为: {}",storeIds.get(0), userIds, staffIds);

        userIds.forEach(userId -> {
            int index = Integer.valueOf(RandomUtil.randomNumbers(String.valueOf(staffIds.size()).length())) % staffIds.size();

            // 调用方法对该用户及导购进行绑定关系
            List<Long> bindStaffUserIds = new ArrayList<>();
            bindStaffUserIds.add(userId);
            ChangeStaffDTO changeStaffDTO = new ChangeStaffDTO();
            changeStaffDTO.setUserId(bindStaffUserIds);
            changeStaffDTO.setStaffId(staffIds.get(index));

            log.info("由导购端分配会员导购入口进入——导购与会员服务关系变更方法。入参:{}", JSONObject.toJSONString(changeStaffDTO));

            this.changeUserStaff(changeStaffDTO);

        });

    }

    @Override
    public List<Long> unBindStaffByStaffId(Long staffId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", staffId);
        List<User> users = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("staff_id", staffId);
        User user = new User();
        user.setStaffId(0l);
        userMapper.update(user, updateWrapper);
        return users.stream().map(User :: getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> findUserIdListByStaffId(Long staffId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", staffId);
        List<User> users = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.stream().map(User :: getUserId).collect(Collectors.toList());
    }


    @Override
    public ServerResponseEntity<Void> updateUserScore(UpdateScoreDTO param) {
        int ioType;
        if (param.getScore() > 0){
            ioType = 1;
        }else {
            ioType = 0;
        }

        return ServerResponseEntity.success();
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> logoutUser(Long userId) {
        String maxPhone = userMapper.findMaxPhone();
        Long maxPhoneL = Long.parseLong(maxPhone);
        maxPhoneL = maxPhoneL + 1L;
        maxPhone = maxPhoneL.toString();
        userMapper.insertRemoveLog(maxPhone, userId);
        userMapper.updLogoutUser(maxPhone, userId);
        userMapper.updLogoutAccount(maxPhone, userId);
        userMapper.updLogoutSocial(maxPhone, userId);
        return ServerResponseEntity.success();
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> deleteUserAddr(Long userId) {
        userMapper.deleteUserAddr(userId);
        return ServerResponseEntity.success();
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Long saveTest(UserRegisterDTO param) {
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_USER);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long userId = segmentIdResponse.getData();

        param.setUserId(userId);

        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setEmail(param.getEmail());
        authAccountDTO.setPassword(param.getPassword());
        authAccountDTO.setPhone(param.getMobile());
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        authAccountDTO.setUsername(param.getUserName());
        authAccountDTO.setStatus(1);
        authAccountDTO.setUserId(userId);

        AuthAccountWithSocialDTO authAccountWithSocialDTO = new AuthAccountWithSocialDTO();
//        if (authSocial != null) {
//            AuthSocialDTO authSocialDTO = mapperFacade.map(authSocial, AuthSocialDTO.class);
//            authAccountWithSocialDTO.setAuthSocial(authSocialDTO);
//        }
        authAccountWithSocialDTO.setAuthAccount(authAccountDTO);


        // 保存统一账户信息
        ServerResponseEntity<Long> serverResponse = accountFeignClient.saveAccountWithSocial(authAccountWithSocialDTO);
        // 抛异常回滚
        if (!serverResponse.isSuccess()) {
            throw new LuckException(serverResponse.getMsg());
        }
        //设置默认门店信息

        User user = new User();
        user.setUserId(userId);
        user.setPic(param.getImg());
        user.setNickName(param.getNickName());
        user.setLevel(Constant.USER_LEVEL_INIT);
        user.setLevelType(LevelTypeEnum.ORDINARY_USER.value());
        user.setStatus(1);
        user.setPhone(authAccountDTO.getPhone());
//        user.setUnionId(authSocial.getBizUnionid());
        buildUser(user, param);
        // 这里保存之后才有用户id
        this.save(user);

        UserExtension userExtension = new UserExtension();
        userExtension.setBalance(0L);
        userExtension.setActualBalance(0L);
        userExtension.setGrowth(0);
        userExtension.setLevel(1);
        userExtension.setLevelType(0);
        userExtension.setScore(0L);
        userExtension.setVersion(0);
        userExtension.setSignDay(0);
        // 上面保存这边才能拿到userId
        userExtension.setUserId(user.getUserId());

        userExtensionService.save(userExtension);

        //用户注册成功后初始化用户数据
        userLevelService.initUserInfoAndLevelInfo(userExtension, Constant.USER_LEVEL_INIT, LevelTypeEnum.ORDINARY_USER.value(), authAccountDTO.getPhone());

        // 好友关系表绑定userId
        userStaffCpRelationService.bindUserId(user.getUnionId(), userId,user.getStaffId());

        return serverResponse.getData();
    }


//    @CachePut(cacheNames = UserCacheNames.BORROW_LIVING_ROOMG, key = "#userId")
    @Override
    public String setBorrowLivingRoomId(String openId, String livingRoomId) {
//        cacheManagerUtil.putCache(UserCacheNames.BORROW_LIVING_ROOMG,StrUtil.toString(userId),livingRoomId);
        String keys= UserCacheNames.BORROW_LIVING_ROOMG+":"+openId;
        redisTemplate.boundValueOps(keys).set(livingRoomId,60*60*24,TimeUnit.SECONDS);
        return livingRoomId;
    }

    @Override
    public String getBorrowLivingRoomId(Long userId) {
        String keys= UserCacheNames.BORROW_LIVING_ROOMG+":"+userId;
        return redisTemplate.boundValueOps(keys).get();
    }

    @Override
    public List<User> getAllVipCodeIsNullUser() {
        return userMapper.selectList(new LambdaQueryWrapper<User>()
                .isNull(User::getVipcode)
        );
    }

    @Override
    public void updateUnionId(Long userId, String unionId) {
        userMapper.updateUnionIdByUserId(userId,unionId);
    }

    @Override
    public void changeServiceStore(UserChangeServiceStoreDTO userChangeServiceStoreDTO) {
        update(new LambdaUpdateWrapper<User>().in(User::getUserId, userChangeServiceStoreDTO.getUserIds())
            .set(User::getServiceStoreId, userChangeServiceStoreDTO.getAfterId()));
        // 记录数据
        List<UserChangeRecord> list = new ArrayList<>();
        for (Long userId : userChangeServiceStoreDTO.getUserIds()) {
            UserChangeRecord userChangeRecord = new UserChangeRecord();
            userChangeRecord.setUserId(userId);
            userChangeRecord.setBeforeId(userChangeServiceStoreDTO.getBeforeId());
            userChangeRecord.setBeforeName(userChangeServiceStoreDTO.getBeforeName());
            userChangeRecord.setAfterId(userChangeServiceStoreDTO.getAfterId());
            userChangeRecord.setAfterName(userChangeServiceStoreDTO.getAfterName());
            userChangeRecord.setType(userChangeServiceStoreDTO.getType());
            userChangeRecord.setSource(userChangeServiceStoreDTO.getSource());
            userChangeRecord.setCreator(userChangeServiceStoreDTO.getCreator());
            userChangeRecord.setUserId(userId);
            list.add(userChangeRecord);
        }
        userChangeRecordService.saveBatch(list);
    }

    @Override
    public List<Long> getBirthdayUser() {
        return userMapper.getBirthdayUser();
    }

    @Override
    public List<UserApiVO> findWeekerByKeyword(String keyword) {
        return userMapper.findWeekerByKeyword(keyword);
    }

    @Override
    public void fixUserServiceStore(List<User> users) {
        for (User user : users) {
            log.info("会员id = {} 导购id = {} 注册门店id = {}",user.getUserId(), user.getStaffId(), user.getStoreId());
            Long serviceStoreId = 1L;
            if (user.getStaffId() == null || user.getStaffId() == 0L) {
                if (user.getStoreId() != null && user.getStoreId() != 1L) {
                    // 无导购 取注册门店

                }
            } else {
                // 有导购 取导购的门店
                ServerResponseEntity<StaffVO> staffById = staffFeignClient.getStaffById(user.getStaffId());
                if (staffById.isSuccess() && staffById.getData() != null) {
                    StaffVO staff = staffById.getData();
                    if (staff.getStoreId() != null && staff.getStoreId() != 1L) {
                        serviceStoreId = staff.getStoreId();

                    }
                }
            }
            update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId()).set(User::getServiceStoreId, serviceStoreId));
        }

    }

    private StaffUserVo buildStaffUser(Date startTime, Date endTime, Integer countType, Long staffId) {
        StaffUserVo staffUserVo = new StaffUserVo();
        staffUserVo.setStartTime(startTime);
        staffUserVo.setEndTime(endTime);
        staffUserVo.setCountType(countType);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", staffId);
        queryWrapper.between("create_time", startTime, endTime);
        List<User> users = userMapper.selectList(queryWrapper);
        staffUserVo.setUserList(users);
        staffUserVo.setTotalNum(users.size());
        return staffUserVo;
    }


    private List<UserManagerVO> assemblyUserData(List<UserManagerVO> userList, Map<Long, UserOrderStatisticVO> userOrderMap) {
        for (UserManagerVO userManagerVO : userList) {
            UserOrderStatisticVO orderStatisticVO = userOrderMap.get(userManagerVO.getUserId());
            if (Objects.isNull(orderStatisticVO)) {
                continue;
            }
            // 实付金额 累计支付的金额: 除了余额支付/积分支付的订单累计金额 + 余额充值金额
            userManagerVO.setActualAmount(PriceUtil.toDecimalPrice(orderStatisticVO.getActualAmount().longValue()).add(userManagerVO.getRechargeAmount()));
            // 最近支付下单时间
            Date reConsTime1 = orderStatisticVO.getReConsTime();
            // 最近余额充值时间
            Date reConsTime2 = userManagerVO.getReConsTime();
            // 最近消费时间 reConsTime1 reConsTime2 比较
            if (Objects.isNull(reConsTime2)) {
                userManagerVO.setReConsTime(reConsTime1);
            } else if (Objects.nonNull(reConsTime1)) {
                userManagerVO.setReConsTime(DateUtil.compare(reConsTime1, reConsTime2) >= 0 ? reConsTime1 : reConsTime2);
            }
            userManagerVO.setConsAmount(PriceUtil.toDecimalPrice(orderStatisticVO.getConsAmount().longValue()));
            userManagerVO.setConsTimes(orderStatisticVO.getConsTimes());
            userManagerVO.setAverDiscount(PriceUtil.toDecimalPrice(orderStatisticVO.getAverDiscount().longValue()));
            userManagerVO.setAfterSaleAmount(PriceUtil.toDecimalPrice(orderStatisticVO.getAfterSaleAmount().longValue()));
            userManagerVO.setAfterSaleTimes(orderStatisticVO.getAfterSaleTimes());
        }
        return userList;
    }


    private List<Long> getQueryStaffIdList(UserManagerDTO userManagerDTO) {
        boolean flag = false;
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        if (!CollectionUtils.isEmpty(userManagerDTO.getStaffList())) {
            staffQueryDTO.setStaffIdList(userManagerDTO.getStaffList());
            flag = true;
        }
        if (flag) {
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (!staffData.isSuccess() || org.springframework.util.CollectionUtils.isEmpty(staffData.getData())){
                return null;
            }
            return staffData.getData().stream().map(StaffVO :: getId).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 导购筛选查询我的会员入参
     * @param staffSelectUserDTO 导购筛选查询我的会员入参
     * @return
     */
    @Override
    public PageVO<UserApiVO> staffSelectUser(StaffSelectUserDTO staffSelectUserDTO) {

        log.info("导购筛选查询我的会员查询参数:{}",JSONObject.toJSONString(staffSelectUserDTO));
        // 声明用户ID列表
        List<Long> res = new ArrayList<>();
        // 声明满足导购选中标签的会员ID
        List<Long> byTagIdUserList = new ArrayList<>();
        // 声明满足是否加好友状态的会员ID
        List<Long> byCouponUserList = new ArrayList<>();
        // 从缓存中获取导购的用户ID
        Long staffId = AuthUserContext.get().getUserId();
        // 拼装参数，之后会根据导购ID和导购其下的用户ID列表查询出用户信息
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setStaffId(staffId);
        staffSelectUserDTO.setStaffId(staffId);
        Long firstTagId = null;
        if(CollectionUtil.isNotEmpty(staffSelectUserDTO.getTagId())){
            staffSelectUserDTO.setTagIdListSize(staffSelectUserDTO.getTagId().size());
            firstTagId = staffSelectUserDTO.getTagId().get(0);
            staffSelectUserDTO.getTagId().remove(0);
        }
        staffSelectUserDTO.setFirstTagId(firstTagId);
        // 查询用户服务，根据导购ID获取该导购下的会员信息
        List<UserApiVO> userApiVOS = userMapper.staffScreenUserForTag(staffSelectUserDTO);

        if(CollectionUtil.isEmpty(userApiVOS)){
            return null;
        }

        // 筛选过滤导购下的会员信息
        List<Long> userIds = userApiVOS.stream().map(UserApiVO::getUserId).collect(Collectors.toList());
        log.info("下级会员数量:{}, 用户ID为:{}",userIds.size(), userIds);
        List<String> vipCodes = userApiVOS.stream().map(UserApiVO::getVipcode).collect(Collectors.toList());
        log.info("用户编号为:{}", vipCodes);
        res.addAll(userIds);

        // 判断导购有没有选择优惠券筛选条件
        if(ObjectUtil.isNotEmpty(staffSelectUserDTO.getHasCouponFlag())){
            // 跳转至优惠券筛选校验方法中
            extracted(staffSelectUserDTO, byCouponUserList, userIds, vipCodes);
            log.info("根据优惠券条件查询结果为{}.", byCouponUserList);
            // 校验根据优惠券条件筛选的用户
            if(CollectionUtil.isNotEmpty(res) && CollectionUtil.isNotEmpty(byCouponUserList)){
                res.retainAll(byCouponUserList);
            }
            if(CollectionUtil.isEmpty(res) && CollectionUtil.isNotEmpty(byCouponUserList)){
                res.addAll(byCouponUserList);
            }
            if(CollectionUtil.isEmpty(byCouponUserList)){
                log.info("导购下会员有值:{},根据优惠券条件查询为空{}.", res, byCouponUserList);
                return null;
            }
        }

        // 判断用户ID列表是否为空，如果不为空就调用用户信息查询接口获取用户信息进行返回
        log.info("导购筛选查询会员最终结果为{}.", res);
        if(CollectionUtil.isNotEmpty(res)){
            // 对查询出的用户ID列表进行去重
            res.stream().distinct().collect(Collectors.toList());
            queryDTO.setUserIds(res);
            log.info("会员查询参数:{}",JSONObject.toJSONString(queryDTO));
            PageVO<UserApiVO> objectPageVO = PageUtil.doPage(staffSelectUserDTO, () -> userMapper.listByStaff(queryDTO));;
            if (CollectionUtils.isNotEmpty(objectPageVO.getList()) && null != queryDTO.getStaffId()){
                objectPageVO.getList().forEach(userApiVO -> {
                    if(Objects.nonNull(userApiVO.getName())){
                        userApiVO.setNickName(userApiVO.getName());
                    }
                    if(Objects.isNull(userApiVO.getName()) && Objects.nonNull(userApiVO.getCustomerName())){
                        userApiVO.setNickName(userApiVO.getCustomerName());
                    }
                    userApiVO.setAddWechat(userStaffCpRelationService.getByStaffAndUser(queryDTO.getStaffId(), userApiVO.getUserId()) == null ? 0 : 1);
                });
            }
            return objectPageVO;
        }
        return null;
    }

    @Override
    public PageVO<UserApiVO> staffScreenUserForTask(StaffSelectUserForTaskDTO staffSelectUserForTaskDTO) {
        log.info("群发任务导购筛选查询我的会员查询参数:{}",JSONObject.toJSONString(staffSelectUserForTaskDTO));
        // 声明用户ID列表
        List<Long> res = new ArrayList<>();
        // 声明满足导购选中标签的会员ID
        //List<Long> byTagIdUserList = new ArrayList<>();
        // 从缓存中获取导购的用户ID
        Long staffId = AuthUserContext.get().getUserId();
        // 拼装参数，之后会根据导购ID和导购其下的用户ID列表查询出用户信息
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setStaffId(staffId);
        staffSelectUserForTaskDTO.setStaffId(staffId);
        // 查询用户服务，根据导购ID和群发任务ID查询出在该导购下并同时在该群发任务下的会员信息
        IPage<UserApiVO> userPage = userManager.staffScreenUserForTask(staffSelectUserForTaskDTO);

        // PageVO<UserApiVO> userApiVOS = PageUtil.doPage(staffSelectUserForTaskDTO, () -> userMapper.staffScreenUserForTask(staffSelectUserForTaskDTO));
        //List<UserApiVO> userApiVOS = userMapper.staffScreenUserForTask(staffSelectUserForTaskDTO);
        /*
        // 筛选过滤导购下的会员信息
        List<Long> userIds = userApiVOS.getList().stream().map(UserApiVO::getUserId).collect(Collectors.toList());
        log.info("下级会员数量:{}, 用户ID为:{}",userIds.size(), userIds);
        List<String> vipCodes = userApiVOS.getList().stream().map(UserApiVO::getVipcode).collect(Collectors.toList());
        log.info("用户编号为:{}", vipCodes);
        res.addAll(userIds);
        */

        // 筛选满足导购选中标签的会员ID
        /*if(CollectionUtil.isNotEmpty(staffSelectUserForTaskDTO.getTagId()) && CollectionUtil.isNotEmpty(vipCodes)){
            // 如果用户选择了会员标签筛选条件，那么就查询出满足导购选中标签的会员ID
            staffSelectUserForTaskDTO.getTagId().forEach(tagId -> {
                byTagIdUserList.addAll(userTagRelationManager.staffGetUserIdByTagId(tagId, vipCodes));
            });
            log.info("查询出满足导购选中标签的会员ID为:{}", byTagIdUserList);
            if(CollectionUtil.isNotEmpty(res) && CollectionUtil.isNotEmpty(byTagIdUserList)){
                res.retainAll(byTagIdUserList);
            }
            if(CollectionUtil.isEmpty(res) && CollectionUtil.isNotEmpty(byTagIdUserList)){
                res.addAll(byTagIdUserList);
            }
            if(CollectionUtil.isNotEmpty(res) && CollectionUtil.isEmpty(byTagIdUserList)){
                log.info("导购下会员有值:{},根据标签条件查询为空{}.", res, byTagIdUserList);
                return null;
            }
        }*/

        // 判断用户ID列表是否为空，如果不为空就调用用户信息查询接口获取用户信息进行返回
        if(CollectionUtil.isNotEmpty(userPage.getRecords())){
            List<Long> userIds = userPage.getRecords().stream().map(UserApiVO::getUserId).collect(Collectors.toList());
            log.info("下级会员数量:{}, 用户ID为:{}",userIds.size(), userIds);
            res.addAll(userIds);
            // 对查询出的用户ID列表进行去重
            res.stream().distinct().collect(Collectors.toList());
            queryDTO.setUserIds(res);
            log.info("会员查询参数:{}",JSONObject.toJSONString(queryDTO));
            List<UserApiVO> objectPageVO = userMapper.listByStaff(queryDTO);

            Map<Long, UserApiVO> userApiVOMap = objectPageVO.stream()
                    .collect(Collectors.toMap(UserApiVO::getUserId, user -> user));

            userPage.getRecords().forEach(userApiVO -> {
                UserApiVO userVO;
                if (MapUtil.isNotEmpty(userApiVOMap) && Objects.nonNull(userVO = userApiVOMap.get(userApiVO.getUserId()))){
                    if(StringUtils.isNotBlank(userVO.getName())){
                        userApiVO.setNickName(userVO.getName());
                    }
                    if(StringUtils.isBlank(userVO.getName()) && StringUtils.isNotBlank(userVO.getCustomerName())){
                        userApiVO.setNickName(userVO.getCustomerName());
                    }
                    userApiVO.setSex(userVO.getSex());
                    userApiVO.setBirthDate(userVO.getBirthDate());
                    userApiVO.setPhone(userVO.getPhone());
                }
            });

            PageVO<UserApiVO> pageVO = new PageVO<>();

            pageVO.setPages((int) userPage.getPages());
            pageVO.setList(userPage.getRecords());
            pageVO.setTotal(userPage.getTotal());

            return pageVO;
        }
        return null;
    }

    @Override
    public List<UserApiVO> getUserByUnionIdList(List<String> unionIdList) {
        if (CollectionUtil.isEmpty(unionIdList)){
            return new ArrayList<>();
        }
        List<User> userList = lambdaQuery()
                .in(User::getUnionId, unionIdList)
                .list();
        List<UserApiVO> userApiVOS = mapperFacade.mapAsList(userList, UserApiVO.class);
        return userApiVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanUserBindStaff(Long userId) {
        // 清除小程序数据库这个用户的导购数据
        this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getUserId, userId)
                .set(User::getStaffId, 0)
                .set(User::getUpdateTime, new Date()));
    }

    /**
     * 素材中心场景-导购获取用户信息
     * @param userId 用户ID
     * @return
     */
    @Override
    public StaffGetUserDetailByMaterialVO staffGetUserDetailByMaterialVO(Long userId) {
        StaffGetUserDetailByMaterialVO staffGetUserDetailByMaterialVO = new StaffGetUserDetailByMaterialVO();
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        BeanUtils.copyProperties(userApiVO, staffGetUserDetailByMaterialVO);
        log.info("素材中心场景-导购获取用户信息:{}", staffGetUserDetailByMaterialVO);
        // 组装门店 & 导购信息
        Map<Long, StoreVO> storeVOMap = new HashMap<>();
        List<Long> storeIdList = new ArrayList<>();
        storeIdList.add(userApiVO.getStoreId());
        storeIdList.add(userApiVO.getServiceStoreId());
        if (CollectionUtils.isNotEmpty(storeIdList)) {
            //大量查询需要post
            ServerResponseEntity<List<StoreVO>> storeResp = storeFeignClient.listBypByStoreIdList(storeIdList);
            if (storeResp.isSuccess()) {
                storeVOMap = storeResp.getData().stream().collect(Collectors.toMap(StoreVO :: getStoreId, Function.identity()));
            }
        }
        // 注册门店
        if (staffGetUserDetailByMaterialVO.getStoreId() != null) {
            StoreVO storeVO = storeVOMap.get(staffGetUserDetailByMaterialVO.getStoreId());
            if (Objects.nonNull(storeVO)) {
                staffGetUserDetailByMaterialVO.setStoreName(storeVO.getName());
                staffGetUserDetailByMaterialVO.setStoreCode(storeVO.getStoreCode());
            }
        }
        // 服务门店
        if (staffGetUserDetailByMaterialVO.getServiceStoreId() != null) {
            StoreVO storeVO = storeVOMap.get(Long.valueOf(staffGetUserDetailByMaterialVO.getServiceStoreId()));
            if (Objects.nonNull(storeVO)) {
                staffGetUserDetailByMaterialVO.setServiceStoreName(storeVO.getName());
                staffGetUserDetailByMaterialVO.setServiceStoreCode(storeVO.getStoreCode());
            }
        }
        return staffGetUserDetailByMaterialVO;
    }

    /**
     * 根据优惠券筛选会员信息
     * @param staffSelectUserDTO
     * @param res
     * @param userIds
     * @param vipCodes
     */
    private void extracted(StaffSelectUserDTO staffSelectUserDTO, List<Long> res, List<Long> userIds, List<String> vipCodes) {
        log.info("进入导购端根据优惠券条件进行查询校验方法");
        staffSelectUserDTO.setUserIds(userIds);
        staffSelectUserDTO.setVipCodes(vipCodes);
        if (staffSelectUserDTO.getEndDay() != null) {
            staffSelectUserDTO.setBeginTime(DateUtil.formatDateTime(new Date()));
            staffSelectUserDTO.setEndTime(DateUtil.formatDateTime(DateUtil.offsetDay(new Date(), staffSelectUserDTO.getEndDay())));
        }

    }

    
    @Override
    public ServerResponseEntity<UserDynamicCodeVO> getDynamicCode(Long userId) {
    
        UserDynamicCodeVO userDynamicCodeVO = new UserDynamicCodeVO();
        
        //根据userId获取用户数据
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        if (Objects.isNull(userApiVO)) {
            return ServerResponseEntity.showFailMsg("请您先注册成为会员");
        }
        
        try {
            //根据phone调用crm系统查询用户信息
            log.info("用户phone={}", userApiVO.getPhone());

            String dynamicCode = userApiVO.getPhone();
            //校验开关
            if(encryptSwitch){
                //先判断该会员在缓存中是否存在动态会员码
                String userDynamicCodeKey = UserCacheNames.USER_DYNAMIC_CODE_KEY + userId;
                if( RedisUtil.hasKey(userDynamicCodeKey) ){
                    //存在,生成新的并且删除旧的,重新设置过期时间
                    String dynamicCodeCache = RedisUtil.get(userDynamicCodeKey);
                    if(StrUtil.isNotBlank(dynamicCodeCache)) {
                        //删除旧的会员关联动态会员码缓存
                        RedisUtil.del(UserCacheNames.DYNAMIC_CODE_KEY + dynamicCodeCache);
                    }
                }
                //生成随机key作为动态会员码,加入"10000"表示自研小程序的码
                StringBuffer sb = new StringBuffer();
                sb.append("10000");
                sb.append(RandomUtil.randomNumbers(6));
                sb.append(dynamicCode.substring(dynamicCode.length()-4));
                dynamicCode = sb.toString();
                //将动态会员码和手机号缓存在redis中
                RedisUtil.set(userDynamicCodeKey, dynamicCode,0);
                RedisUtil.set(UserCacheNames.DYNAMIC_CODE_KEY + dynamicCode ,userApiVO.getPhone() ,expires);
            }
            
            log.info("生成动态会员码,encryptSwitch={}, dynamicCode={}, phone={}" ,encryptSwitch ,dynamicCode ,userApiVO.getPhone());
    
            userDynamicCodeVO.setDynamicCode(dynamicCode);
            userDynamicCodeVO.setExpires(expires);
        }catch (Exception e){
            log.error("动态会员码解析失败 ",e);
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        return ServerResponseEntity.success(userDynamicCodeVO);
    }
    
    @Override
    public ServerResponseEntity<String> decryptDynamicCode(UserDynamicCodeDTO dynamicCodeDTO) {
        String dynamicCode = dynamicCodeDTO.getDynamicCode();
        if(StrUtil.isEmpty(dynamicCode)){
            return ServerResponseEntity.fail(ResponseEnum.USER_DYNAMIC_CODE_EXPIRE);
        }
        log.info("动态会员码解析入参,encryptSwitch={}, dynamicCode={}",encryptSwitch,dynamicCode);
        
        //判断动态码来源,不是"10000"开头表示微盟的动态码

        try {
            if(encryptSwitch){
                //从缓存中获取手机号,过期报错
                String dynamicCodeKey = UserCacheNames.DYNAMIC_CODE_KEY + dynamicCode;
                if( !RedisUtil.hasKey( dynamicCodeKey )){
                    log.info("动态会员码已经过期, dynamicCode={}",dynamicCode);
                    return ServerResponseEntity.fail(ResponseEnum.USER_DYNAMIC_CODE_EXPIRE);
                }
    
                String phone = RedisUtil.get(dynamicCodeKey);
                
                log.info("动态会员码解析后的数据, phone={}",phone);
                dynamicCode = phone;
            }
        }catch (Exception e){
            log.error("动态会员码解析失败 ",e);
            return ServerResponseEntity.fail(ResponseEnum.USER_DYNAMIC_CODE_EXPIRE);
        }
        
        return ServerResponseEntity.success(dynamicCode);
    }

    @Override
    public boolean isRecEnabled(Long userId) {
        Boolean enabled = RedisUtil.get(UserCacheNames.REC_ENABLED+userId);
        return Objects.isNull(enabled) || enabled;
    }

    @Override
    public void recSwitch(Long userId) {
        if(Objects.nonNull(userId)) {
            UserApiVO userApiVO = userMapper.getByUserId(userId);
            User user = new User();
            user.setUserId(userId);
            if(Objects.isNull(userApiVO.getRecSwitch())){
                user.setRecSwitch(true);
            }else{
                user.setRecSwitch(!userApiVO.getRecSwitch());
            }
            userMapper.updateById(user);

            if(user.getRecSwitch()){
                RedisUtil.del(UserCacheNames.REC_ENABLED+userId);
            }else{
                RedisUtil.set(UserCacheNames.REC_ENABLED+userId,user.getRecSwitch(),0);
            }
        }
    }

    @Override
    public PageVO<UserApiVO> pageCouponUserByStaff(PageDTO pageDTO, QueryHasCouponUsersRequest queryHasCouponUsersRequest) {
        Long userId = AuthUserContext.get().getUserId();
        log.info("查詢參數:{}",JSONObject.toJSONString(queryHasCouponUsersRequest));
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setStaffId(userId);
        List<UserApiVO> userApiVOS = userMapper.listByStaff(queryDTO);
        List<Long> userIds = userApiVOS.stream().map(UserApiVO::getUserId).collect(Collectors.toList());
        log.info("下级会员数量:{}",userIds.size());
        List<String> vipCodes = userApiVOS.stream().map(UserApiVO::getVipcode).collect(Collectors.toList());
        queryHasCouponUsersRequest.setUserIds(userIds);
        queryHasCouponUsersRequest.setVipCodes(vipCodes);
        if (queryHasCouponUsersRequest.getEndDay() != null) {
            queryHasCouponUsersRequest.setBeginTime(DateUtil.formatDateTime(new Date()));
            queryHasCouponUsersRequest.setEndTime(DateUtil.formatDateTime(DateUtil.offsetDay(new Date(),queryHasCouponUsersRequest.getEndDay())));
        }

        List<Long> res = new ArrayList<>();
        // 获取小程序实例表符合数据的用户集合
        ServerResponseEntity<List<Long>> couponUserIds = tCouponFeignClient.getCouponUserIds(queryHasCouponUsersRequest);
        if (couponUserIds != null && couponUserIds.isSuccess() && couponUserIds.getData() != null && couponUserIds.getData().size() > 0) {
            if (queryHasCouponUsersRequest.getHasCouponFlag() == 1) {
                res.addAll(couponUserIds.getData());
                log.info("小程序有优惠券会员数量:{}",res.size());
            }
        }

        // 获取所有符合优惠券条件的优惠券id
        if (queryHasCouponUsersRequest.getHasCouponFlag() != null && queryHasCouponUsersRequest.getHasCouponFlag() == 1) {
            QueryCrmIdsDTO queryCrmIdsDTO = new QueryCrmIdsDTO();
            queryCrmIdsDTO.setCouponPictures(queryHasCouponUsersRequest.getCouponPictures());
            queryCrmIdsDTO.setKinds(queryHasCouponUsersRequest.getKinds());
            queryCrmIdsDTO.setCouponName(queryHasCouponUsersRequest.getCouponName());
            ServerResponseEntity<List<String>> responseEntity = tCouponFeignClient.queryCrmIds(queryCrmIdsDTO);
            if (responseEntity != null && responseEntity.isSuccess() && responseEntity.getData() != null && responseEntity.getData().size() > 0) {
                queryHasCouponUsersRequest.setCrmCouponIds(responseEntity.getData());
                log.info("crm符合条件优惠券数量:{}",responseEntity.getData().size());
            }
        }
        // 获取crm实例表符合数据的用户集合
        List<String> haveCodes = null;
        ServerResponseEntity<List<String>> listServerResponseEntity = crmCouponClient.queryHasCrmCouponUsers(queryHasCouponUsersRequest);
        if (listServerResponseEntity != null && listServerResponseEntity.isSuccess() && listServerResponseEntity.getData() != null && listServerResponseEntity.getData().size() > 0) {
            log.info("crm用户数量:{}",listServerResponseEntity.getData().size());
            if (queryHasCouponUsersRequest.getHasCouponFlag() == 1) {
                List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getVipcode, listServerResponseEntity.getData()));
                if (users != null && users.size() > 0) {
                    res.addAll(users.stream().map(u -> u.getUserId()).collect(Collectors.toList()));
                    log.info("crm有优惠券会员数量:{}",users.size());
                }
            } else {
                List<String> codes = vipCodes.stream().filter(v -> !listServerResponseEntity.getData().contains(v)).collect(Collectors.toList());
                haveCodes = vipCodes.stream().filter(v -> listServerResponseEntity.getData().contains(v)).collect(Collectors.toList());
                if (codes != null && codes.size() > 0) {
                    List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getVipcode,codes));
                    if (users != null && users.size() > 0) {
                        List<Long> ids = users.stream().map(u -> u.getUserId()).collect(Collectors.toList());
                        if (couponUserIds != null && couponUserIds.isSuccess() && couponUserIds.getData() != null && couponUserIds.getData().size() > 0) {
                            ids = ids.stream().filter(u -> !couponUserIds.getData().contains(u)).collect(Collectors.toList());
                        }
                        res.addAll(ids);
                        log.info("crm无优惠券会员数量:{}",users.size());
                    }
                }
            }
        }
        if (queryHasCouponUsersRequest.getHasCouponFlag() == 0) {
            if (haveCodes != null && haveCodes.size() > 0) {
                List<User> haveUsers = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getVipcode, haveCodes));
                if (haveUsers != null && haveUsers.size() > 0) {
                    List<Long> haveIds = haveUsers.stream().map(u -> u.getUserId()).collect(Collectors.toList());
                    if (couponUserIds != null && couponUserIds.isSuccess() && couponUserIds.getData() != null && couponUserIds.getData().size() > 0) {
                        List<Long> unCouponUserIds = userIds.stream().filter(u -> !couponUserIds.getData().contains(u)).collect(Collectors.toList());
                        log.info("小程序无优惠券数量:{},crm有优惠券数量:{}",unCouponUserIds.size(),haveIds.size());
                        unCouponUserIds = unCouponUserIds.stream().filter(u -> !haveIds.contains(u)).collect(Collectors.toList());
                        log.info("小程序无优惠券数量:{}",unCouponUserIds);
                        res.addAll(unCouponUserIds);
                    }
                }
            } else {
                if (couponUserIds != null && couponUserIds.isSuccess() && couponUserIds.getData() != null && couponUserIds.getData().size() > 0) {
                    List<Long> unCouponUserIds = userIds.stream().filter(u -> !couponUserIds.getData().contains(u)).collect(Collectors.toList());
                    log.info("小程序无优惠券数量:{}",unCouponUserIds);
                    res.addAll(unCouponUserIds);
                }
            }
        }
        if (res == null || res.size() == 0) {
            return null;
        }
        queryDTO.setUserIds(res);
        log.info("会员查询参数:{}",JSONObject.toJSONString(queryDTO));
        PageVO<UserApiVO> objectPageVO = PageUtil.doPage(pageDTO, () -> userMapper.listByStaff(queryDTO));;
        if (CollectionUtils.isNotEmpty(objectPageVO.getList()) && null != queryDTO.getStaffId()){
            objectPageVO.getList().forEach(userApiVO -> {
                if(Objects.nonNull(userApiVO.getName())){
                    userApiVO.setNickName(userApiVO.getName());
                }
                if(Objects.isNull(userApiVO.getName()) && Objects.nonNull(userApiVO.getCustomerName())){
                    userApiVO.setNickName(userApiVO.getCustomerName());
                }
                userApiVO.setAddWechat(userStaffCpRelationService.getByStaffAndUser(queryDTO.getStaffId(), userApiVO.getUserId()) == null ? 0 : 1);
            });
        }
        return objectPageVO;
    }

    @Override
    public ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> getScoreDetail(Integer pageNo, Integer pageSize, Long userId) {
        UserApiVO user = userMapper.getByUserId(userId);
        if (ObjectUtil.isEmpty(user)) {
            throw new LuckException("用户信息查询为空");
        }
        if (ObjectUtil.isEmpty(user.getVipcode())) {
            throw new LuckException("CRM会员卡查询为空");
        }

        //过去180天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -180);
        Date d = c.getTime();
        String startDay = format.format(d);
        String endDay = format.format(new Date());

        PointDetailGetDto pointDetailGetDto = new PointDetailGetDto();
        pointDetailGetDto.setVipcode(user.getVipcode());
        pointDetailGetDto.setPage_index(pageNo);
        pointDetailGetDto.setPage_size(pageSize);
        pointDetailGetDto.setTime_start(startDay);
        pointDetailGetDto.setTime_end(endDay);
        log.info("调用crm积分明细查询参数：{}", JSONObject.toJSONString(pointDetailGetDto));
//        ServerResponseEntity<CrmPageResult<List<PointDetailVo>>> result = crmCustomerPointFeignClient.pointDetailGet(pointDetailGetDto);
//        log.info("调用crm积分明细查询结果为：{}", JSONObject.toJSONString(result));

        return ServerResponseEntity.success();
    }
}
