package com.mall4j.cloud.auth.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.bo.UserRegisterNotifyBO;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.dto.*;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthAccountVO;
import com.mall4j.cloud.api.auth.vo.TokenInfoVO;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.auth.manager.TokenStore;
import com.mall4j.cloud.auth.mapper.AuthAccountMapper;
import com.mall4j.cloud.auth.mapper.AuthSocialMapper;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.auth.model.AuthSocial;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.UserAdminType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.security.bo.AuthAccountInVerifyBO;
import com.mall4j.cloud.common.security.constant.InputUserNameEnum;
import com.mall4j.cloud.common.util.PrincipalUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
@RestController
@Slf4j
public class AccountFeignController implements AccountFeignClient {

    @Autowired
    private AuthAccountMapper authAccountMapper;

    @Autowired
    private AuthSocialMapper authSocialMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private OnsMQTransactionTemplate userNotifyRegisterTemplate;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Long> save(AuthAccountDTO authAccountDTO) {

        ServerResponseEntity<AuthAccount> verify = verify(authAccountDTO);
        if (!verify.isSuccess()) {
            return ServerResponseEntity.transform(verify);
        }
        AuthAccount data = verify.getData();
        AuthAccountInVerifyBO userNameBo = authAccountMapper.getAuthAccountInVerifyByInputUserName(InputUserNameEnum.USERNAME.value(), authAccountDTO.getUsername(), authAccountDTO.getSysType());
        log.info("用户名参数：{}", JSONObject.toJSONString(userNameBo));
        if (userNameBo != null && !Objects.equals(userNameBo.getUserId(), authAccountDTO.getUserId())) {
            AuthSocial oldSocial = authSocialMapper.getByUid(userNameBo.getUid());
            if (oldSocial != null && StrUtil.isNotEmpty(authAccountDTO.getBizUnionid())) {
                log.info("原来的Social参数：{}", JSONObject.toJSONString(oldSocial));
                AuthSocial bizUnionIdAndType = authSocialMapper.getByBizUnionIdAndType(authAccountDTO.getBizUnionid(), 1);
                log.info("bizUnionIdAndType参数：{}", JSONObject.toJSONString(bizUnionIdAndType));
                if (bizUnionIdAndType != null) {
                    authSocialMapper.deleteById(bizUnionIdAndType.getId());
                }
                oldSocial.setBizUnionid(authAccountDTO.getBizUnionid());
                authSocialMapper.update(oldSocial);
            }
            return ServerResponseEntity.success(data.getUid());
        }
        if (StrUtil.isNotBlank(authAccountDTO.getPhone())) {
            AuthAccountInVerifyBO phoneBo = authAccountMapper.getAuthAccountInVerifyByInputUserName(InputUserNameEnum.PHONE.value(), authAccountDTO.getPhone(), authAccountDTO.getSysType());
            if (phoneBo != null && !Objects.equals(phoneBo.getUserId(), authAccountDTO.getUserId())) {
                log.info("手机号参数：{}", JSONObject.toJSONString(phoneBo));
                AuthSocial oldSocial = authSocialMapper.getByUid(userNameBo.getUid());
                if (oldSocial != null && StrUtil.isNotEmpty(authAccountDTO.getBizUnionid())) {
                    log.info("原来的Social参数：{}", JSONObject.toJSONString(oldSocial));
                    AuthSocial bizUnionIdAndType = authSocialMapper.getByBizUnionIdAndType(authAccountDTO.getBizUnionid(), 1);
                    if (bizUnionIdAndType != null) {
                        authSocialMapper.deleteById(bizUnionIdAndType.getId());

                    }
                    oldSocial.setBizUnionid(authAccountDTO.getBizUnionid());
                    authSocialMapper.update(oldSocial);
                }
                return ServerResponseEntity.success(data.getUid());
            }
        }
        try {
            authAccountMapper.save(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("用户名已存在，请更换用户名再次尝试");
        }

        return ServerResponseEntity.success(data.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> update(AuthAccountDTO authAccountDTO) {
        ServerResponseEntity<AuthAccount> verify = verify(authAccountDTO);
        if (!verify.isSuccess()) {
            return ServerResponseEntity.transform(verify);
        }
        if (!Objects.equals(authAccountDTO.getStatus(), 1)) {
            AuthAccount authAccount = authAccountMapper.getByUserIdAndType(authAccountDTO.getUserId(), authAccountDTO.getSysType());
            if (Objects.equals(authAccount.getIsAdmin(), UserAdminType.ADMIN.value())) {
                return ServerResponseEntity.showFailMsg("该账号为管理员不能禁用");
            }

        }
        authAccountMapper.updateAccountInfo(verify.getData());
        // 修改了用户的状态 or 密码，应该要将用户下线
        if (!Objects.equals(authAccountDTO.getStatus(), 1) || StrUtil.isNotBlank(authAccountDTO.getPassword())) {
            AuthAccount dbAccount = authAccountMapper.getByUserIdAndType(authAccountDTO.getUserId(), authAccountDTO.getSysType());
            tokenStore.deleteAllToken(authAccountDTO.getSysType().toString(), dbAccount.getUid());
        }
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateAuthAccountStatus(AuthAccountDTO authAccountDTO) {
        if (Objects.isNull(authAccountDTO.getStatus())) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        AuthAccount authAccount = mapperFacade.map(authAccountDTO, AuthAccount.class);
        authAccountMapper.updateAccountInfo(authAccount);
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> deleteByUserIdAndSysType(Long userId) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        AuthAccount authAccount = authAccountMapper.getByUserIdAndType(userId, userInfoInTokenBO.getSysType());
        if (Objects.isNull(authAccount)) {
            return ServerResponseEntity.success();
        }
        if (Objects.equals(authAccount.getIsAdmin(), UserAdminType.ADMIN.value())) {
            throw new LuckException("管理员账号不能删除");
        }
        authAccountMapper.deleteByUserIdAndSysType(userId, userInfoInTokenBO.getSysType());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Integer> countByMobile(String mobile) {
        return ServerResponseEntity.success(authAccountMapper.countByMobile(mobile,SysTypeEnum.ORDINARY.value()));
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(Long userId,Integer sysType) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        AuthAccount authAccount = authAccountMapper.getByUserIdAndType(userId, userInfoInTokenBO.getSysType());
        if (Objects.nonNull(authAccount)) {
            authAccount.setPassword(null);
        }
        return ServerResponseEntity.success(mapperFacade.map(authAccount, AuthAccountVO.class));
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getBySysTypeAndIsAdminAndTenantId(Integer sysType, Integer isAdmin, Long tenantId) {
        AuthAccount authAccount = authAccountMapper.getBySysTypeAndIsAdminAndTenantId(sysType, isAdmin, tenantId);
        if (Objects.nonNull(authAccount)) {
            authAccount.setPassword(null);
        }
        return ServerResponseEntity.success(mapperFacade.map(authAccount, AuthAccountVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Long> saveAccountWithSocial(AuthAccountWithSocialDTO authAccountWithSocialDTO) {
        AuthAccountDTO authAccount = authAccountWithSocialDTO.getAuthAccount();
        AuthSocialDTO authSocial = authAccountWithSocialDTO.getAuthSocial();
        if (null != authSocial) {
            authAccount.setBizUnionid(authSocial.getBizUnionid());
        }


        // 保存账户
        ServerResponseEntity<Long> serverResponse = save(authAccount);
        if (!serverResponse.isSuccess()) {
            return ServerResponseEntity.transform(serverResponse);
        }
        Long uid = serverResponse.getData();

        // 如果有社交帐户信息
        if (authSocial != null) {
            authSocial.setUid(uid);
            authSocialMapper.bindUidByTempUid(authSocial.getUid(), authSocial.getTempUid());
        }

        return ServerResponseEntity.success(uid);
    }

    @Override
    public ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(userInfoInTokenBO));
    }

    @Override
    public ServerResponseEntity<List<AuthAccountVO>> listByUserAccount(String userName, String phone, String email, SysTypeEnum sysType) {
        return ServerResponseEntity.success(authAccountMapper.listByUserAccount(userName, phone, email, sysType.value()));
    }

    @Override
    public ServerResponseEntity<Void> updateUserMobile(AuthAccountDTO authAccountDTO) {
        if (authAccountMapper.countByMobile(authAccountDTO.getSysType(), authAccountDTO.getPhone()) > 0) {
            throw new LuckException("该手机号码已存在");
        }
        authAccountMapper.updateUserMobile(authAccountDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<AuthAccountVO>> listUserByUserIdsAndType(List<Long> userIds, Integer systemType) {
        if (CollUtil.isEmpty(userIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<AuthAccountVO> authAccountList = authAccountMapper.listUserByUserIdsAndType(userIds, systemType);
        return ServerResponseEntity.success(authAccountList);
    }

    @Override
    public ServerResponseEntity<List<AuthAccountVO>> listByUserIdsAndPhoneAndType(List<Long> userIds, String phone, Integer sysType) {
        List<AuthAccountVO> authAccountList = authAccountMapper.listByUserIdsAndPhoneAndType(userIds, phone, sysType);
        return ServerResponseEntity.success(authAccountList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateUserInfoByUserIdAndSysType(UserInfoInTokenBO userInfoInTokenBO, Long userId, Integer sysType) {
        AuthAccount byUserIdAndType = authAccountMapper.getByUserIdAndType(userId, sysType);
        userInfoInTokenBO.setUid(byUserIdAndType.getUid());
        tokenStore.updateUserInfoByUidAndAppId(byUserIdAndType.getUid(), sysType.toString(), userInfoInTokenBO);
        AuthAccount authAccount = mapperFacade.map(userInfoInTokenBO, AuthAccount.class);
        int res = authAccountMapper.updateUserInfoByUserId(authAccount, userId, sysType);
        if (res != 1) {
            throw new LuckException("用户信息错误，更新失败");
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Integer> countByMobileAndSysType(String mobile, Integer sysType) {
        return ServerResponseEntity.success(authAccountMapper.countByMobile(mobile, sysType));
    }

    @Override
    public ServerResponseEntity<Integer> countByUserNameAndSysType(String userName, Integer sysType) {
        return ServerResponseEntity.success(authAccountMapper.countByUserNameAndSysType(userName, sysType));
    }

    @Override
    public ServerResponseEntity<List<String>> listUserNameByNames(List<String> names) {
        if (CollUtil.isEmpty(names)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        return ServerResponseEntity.success(authAccountMapper.listUserNameByNames(names));
    }

    @Override
    public ServerResponseEntity<Void> updateAuthAccountStatusAndDeleteUserToken(AuthAccountDTO authAccountDTO) {
        // 修改了用户的状态，应该要将用户下线
        if (Objects.isNull(authAccountDTO.getUserId())) {
            throw new LuckException("用户id不能为空");
        }
        if (Objects.isNull(authAccountDTO.getSysType())) {
            throw new LuckException("系统类型不能为空");
        }
        if (Objects.isNull(authAccountDTO.getStatus())) {
            throw new LuckException("用户状态不能为空");
        }
        AuthAccount authAccount = mapperFacade.map(authAccountDTO, AuthAccount.class);
        authAccountMapper.updateAccountInfo(authAccount);
        // 删除对应的token信息，使用户下线
        AuthAccount dbAccount = authAccountMapper.getByUserIdAndType(authAccountDTO.getUserId(), authAccountDTO.getSysType());
        tokenStore.deleteAllToken(authAccountDTO.getSysType().toString(), dbAccount.getUid());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<String>> getBizUserIdListByUserId(Long userId) {
        return ServerResponseEntity.success(authAccountMapper.getBizUserIdListByUserId(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<String> batchRegisterAccount(AuthAccountUserDTO authAccountUserDTO) {
        List<AuthAccountDTO> accountDTOList = authAccountUserDTO.getAccountDTOList();
        if (CollUtil.isEmpty(accountDTOList)) {
            ServerResponseEntity.showFailMsg("用户信息列表为空");
        }
        accountDTOList = filterAuthAccountsByPhonesOrEmailsOrUserNames(accountDTOList);
        int size = accountDTOList.size();
        if (CollUtil.isEmpty(accountDTOList)) {
            ServerResponseEntity.showFailMsg("符合导入规则的数据" + size + "条");
        }
        List<AuthAccount> authAccounts = new ArrayList<>();
        for (AuthAccountDTO authAccountDTO : accountDTOList) {
            // 用户名
            if (!PrincipalUtil.isUserName(authAccountDTO.getUsername())) {
                continue;
            }
            // 邮箱
            if (StrUtil.isNotBlank(authAccountDTO.getEmail()) && !PrincipalUtil.isEmail(authAccountDTO.getEmail())) {
                continue;
            }
            // 手机号
            if (StrUtil.isNotBlank(authAccountDTO.getPhone()) && !PrincipalUtil.isMobile(authAccountDTO.getPhone())) {
                continue;
            }
            AuthAccount authAccount = mapperFacade.map(authAccountDTO, AuthAccount.class);
            // 设置分布式id
            authAccount.setUid(segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_AUTH_USER).getData());
            authAccounts.add(authAccount);
        }


        // 批量注册用户
        if (CollUtil.isNotEmpty(authAccounts)) {
            List<Long> userIds = authAccounts.stream().map(AuthAccount::getUserId).collect(Collectors.toList());
            // 需要插入的用户信息
            List<UserRegisterDTO> userRegisterList = authAccountUserDTO.getUserRegisterList();
            List<UserRegisterExtensionDTO> userRegisterExtensionDTOList = authAccountUserDTO.getUserRegisterExtensionDTOList();
            List<UserRegisterDTO> userList = userRegisterList.stream().filter(item -> userIds.contains(item.getUserId())).collect(Collectors.toList());
            List<UserRegisterExtensionDTO> userExtensionList = userRegisterExtensionDTOList.stream().filter(item -> userIds.contains(item.getUserId())).collect(Collectors.toList());
            UserRegisterNotifyBO userRegisterNotifyBO = new UserRegisterNotifyBO(mapperFacade.mapAsList(authAccounts, AuthAccountDTO.class), userList, userExtensionList);
            // 发送用户注册事件
            SendResult sendResult = userNotifyRegisterTemplate.sendMessageInTransaction(userRegisterNotifyBO, authAccounts);
            if (sendResult == null || sendResult.getMessageId() == null) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            return ServerResponseEntity.success(userIds.size() + "");
        }
        return ServerResponseEntity.showFailMsg("无数据导入");
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getMerchantInfoByTenantId(Long tenantId) {
        AuthAccountVO authAccountVO = authAccountMapper.getMerchantInfoByTenantId(tenantId);
        return ServerResponseEntity.success(authAccountVO);
    }

    @Override
    public ServerResponseEntity<Boolean> verifyAccount(AuthAccountDTO authAccountDTO) {
        ServerResponseEntity<AuthAccount> verify = verify(authAccountDTO);
        if (!verify.isSuccess()) {
            throw new LuckException(verify.getMsg());
        }
        return ServerResponseEntity.success(verify.isSuccess());
    }

    /**
     * 过滤掉已经存在的 手机号号 用户名 邮箱 的账户
     *
     * @param accountDTOList 未过滤前的账户集合
     * @return 过滤后的账户集合
     */
    private List<AuthAccountDTO> filterAuthAccountsByPhonesOrEmailsOrUserNames(List<AuthAccountDTO> accountDTOList) {
        List<String> phones = accountDTOList.stream().map(AuthAccountDTO::getPhone).distinct().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<String> emails = accountDTOList.stream().map(AuthAccountDTO::getEmail).distinct().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<String> userNames = accountDTOList.stream().map(AuthAccountDTO::getUsername).distinct().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        // 获取已存在的username用户
        List<String> dbUserNames;
        List<AuthAccountVO> authAccountNameList = authAccountMapper.getAuthAccountByInputUserName(InputUserNameEnum.USERNAME.value(), userNames, SysTypeEnum.ORDINARY.value());
        if (CollUtil.isNotEmpty(authAccountNameList)) {
            dbUserNames = authAccountNameList.stream().map(AuthAccountVO::getUsername).collect(Collectors.toList());
        } else {
            dbUserNames = new ArrayList<>();
        }
        // 获取已存在的email用户
        List<String> dbEmails;
        if (CollUtil.isNotEmpty(emails)) {
            List<AuthAccountVO> authAccountEmailList = authAccountMapper.getAuthAccountByInputUserName(InputUserNameEnum.EMAIL.value(), emails, SysTypeEnum.ORDINARY.value());
            if (CollUtil.isNotEmpty(authAccountEmailList)) {
                dbEmails = authAccountEmailList.stream().map(AuthAccountVO::getEmail).collect(Collectors.toList());
            } else {
                dbEmails = new ArrayList<>();
            }
        } else {
            dbEmails = new ArrayList<>();
        }
        // 获取已存在的phone用户
        List<String> dbPhones;
        if (CollUtil.isNotEmpty(phones)) {
            List<AuthAccountVO> authAccountPhoneList = authAccountMapper.getAuthAccountByInputUserName(InputUserNameEnum.PHONE.value(), phones, SysTypeEnum.ORDINARY.value());
            if (CollUtil.isNotEmpty(authAccountPhoneList)) {
                dbPhones = authAccountPhoneList.stream().map(AuthAccountVO::getPhone).collect(Collectors.toList());
            } else {
                dbPhones = new ArrayList<>();
            }
        } else {
            dbPhones = new ArrayList<>();
        }
        // 过滤已存在的 username email phone用户
        accountDTOList = accountDTOList.stream().filter(item -> {
            boolean isExist = dbUserNames.contains(item.getUsername()) || dbEmails.contains(item.getEmail()) || dbPhones.contains(item.getPhone());
            return !isExist;
        }).collect(Collectors.toList());
        // 过滤掉邮箱用户重复的数据。导入的用户手机号不为空，phone已经过滤了，响应的username也过滤了,这里只给邮箱去重就可以了
        List<AuthAccountDTO> accountList = accountDTOList.stream().filter(item -> Objects.isNull(item.getEmail())).collect(Collectors.toList());
        accountDTOList = accountDTOList.stream()
                .filter(item -> Objects.nonNull(item.getEmail()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(AuthAccountDTO::getEmail, Comparator.nullsLast(String::compareTo)))), ArrayList::new));
        accountList.addAll(accountDTOList);
        return accountList;
    }

    private ServerResponseEntity<AuthAccount> verify(AuthAccountDTO authAccountDTO) {

        // 用户名
        if (!PrincipalUtil.isUserName(authAccountDTO.getUsername())) {
            return ServerResponseEntity.showFailMsg("用户名格式不正确");
        }

        // 邮箱
        if (StrUtil.isNotBlank(authAccountDTO.getEmail()) && !PrincipalUtil.isEmail(authAccountDTO.getEmail())) {
            return ServerResponseEntity.showFailMsg("邮箱格式不正确");
        }

        // 手机号
        if (StrUtil.isNotBlank(authAccountDTO.getPhone()) && !PrincipalUtil.isMobile(authAccountDTO.getPhone())) {
            return ServerResponseEntity.showFailMsg("手机号格式不正确");
        }

//        AuthAccountInVerifyBO userNameBo = authAccountMapper.getAuthAccountInVerifyByInputUserName(InputUserNameEnum.USERNAME.value(), authAccountDTO.getUsername(), authAccountDTO.getSysType());
//        if (userNameBo != null && !Objects.equals(userNameBo.getUserId(), authAccountDTO.getUserId())) {
//            return ServerResponseEntity.showFailMsg("用户名已存在，请更换用户名再次尝试");
//        }
        if (StrUtil.isNotBlank(authAccountDTO.getEmail())) {
            AuthAccountInVerifyBO emailBO = authAccountMapper.getAuthAccountInVerifyByInputUserName(InputUserNameEnum.EMAIL.value(), authAccountDTO.getEmail(), authAccountDTO.getSysType());
            if (emailBO != null && !Objects.equals(emailBO.getUserId(), authAccountDTO.getUserId())) {
                return ServerResponseEntity.showFailMsg("邮箱已存在，请更换邮箱再次尝试");
            }
        }

//        if (StrUtil.isNotBlank(authAccountDTO.getPhone())) {
//            AuthAccountInVerifyBO phoneBo = authAccountMapper.getAuthAccountInVerifyByInputUserName(InputUserNameEnum.PHONE.value(), authAccountDTO.getPhone(), authAccountDTO.getSysType());
//            if (phoneBo != null && !Objects.equals(phoneBo.getUserId(), authAccountDTO.getUserId())) {
//                return ServerResponseEntity.showFailMsg("手机号已存在，请更换手机号再次尝试");
//            }
//        }

        AuthAccount authAccount = mapperFacade.map(authAccountDTO, AuthAccount.class);

        if (StrUtil.isNotBlank(authAccount.getPassword())) {
            authAccount.setPassword(passwordEncoder.encode(authAccount.getPassword()));
        }

        return ServerResponseEntity.success(authAccount);
    }

}
