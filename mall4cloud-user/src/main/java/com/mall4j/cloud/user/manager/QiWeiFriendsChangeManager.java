package com.mall4j.cloud.user.manager;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.constant.ContactChangeTypeEnum;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.user.dto.UpdateVipRelationFriendStateDTO;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.model.UserStaffCpRelationPhone;
import com.mall4j.cloud.user.service.GroupPushTaskVipRelationService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserStaffCpRelationPhoneService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class QiWeiFriendsChangeManager {

    private final StaffFeignClient staffFeignClient;
    private final UserService userService;
    private final UserStaffCpRelationService userStaffCpRelationService;
    private final UserStaffCpRelationPhoneService relationPhoneService;

    @Transactional(rollbackFor = Exception.class)
    public void doMessage(UserStaffCpRelationDTO userStaffCpRelationDTO) {
        log.info("qiweiFriendSync param : {} ", JSONObject.toJSONString(userStaffCpRelationDTO));
        //员工企微id
        String qiWeiStaffId = userStaffCpRelationDTO.getQiWeiStaffId();
        //客户外部联系人id
        String qiWeiUserId = userStaffCpRelationDTO.getQiWeiUserId();
        if (userStaffCpRelationDTO.getStatus() == 1) { //绑定关系: 1-绑定 2-解绑中 3-删除客户
            UserStaffCpRelation userStaffCpRelation = userStaffCpRelationService.getByQiWeiStaffIdAndQiWeiUserId(qiWeiStaffId, qiWeiUserId);
            // 添加好友，需要根据外部联系人id和员工企业微信Id查询
            if (Objects.nonNull(userStaffCpRelation)) {
                //为了补全客户的userId 和 unionId
                if(userStaffCpRelation.getUserId()==null || StringUtils.isEmpty(userStaffCpRelation.getUserUnionId())){
                     if(StringUtils.isNotEmpty(userStaffCpRelationDTO.getUserUnionId())){
                         UserApiVO user = userService.getByUnionId(userStaffCpRelationDTO.getUserUnionId());
                         if(user!=null) {
                             userStaffCpRelation.setUserId(user.getUserId());
                             userStaffCpRelation.setUserUnionId(userStaffCpRelationDTO.getUserUnionId());
                             userStaffCpRelation.setQiWeiNickName(user.getNickName());
                         }
                     }
                 }
                userStaffCpRelation.setType(userStaffCpRelationDTO.getType());
                userStaffCpRelation.setGender(userStaffCpRelationDTO.getGender());
                userStaffCpRelation.setAvatar(userStaffCpRelationDTO.getAvatar());
                userStaffCpRelation.setCpRemark(userStaffCpRelationDTO.getCpRemark());
                userStaffCpRelation.setCpDescription(userStaffCpRelationDTO.getCpDescription());
                userStaffCpRelation.setCpRemarkMobiles(getCpRemarkMobile(userStaffCpRelationDTO.getCpRemarkMobiles()));
                if(userStaffCpRelation.getStatus()!=1){
                    //重新添加
                    userStaffCpRelation.setCpCreateTime(new Date());
                }else{
                    userStaffCpRelation.setCpCreateTime(userStaffCpRelationDTO.getCpCreateTime());
                }
                userStaffCpRelation.setUpdateTime(new Date());
                userStaffCpRelation.setCpAddWay(userStaffCpRelationDTO.getCpAddWay());
                userStaffCpRelation.setCpState(userStaffCpRelationDTO.getCpState());//渠道源自动义唯一参数
                userStaffCpRelation.setCodeGroupId(userStaffCpRelationDTO.getCodeGroupId());//渠道源分组id
                userStaffCpRelation.setCodeChannelId(userStaffCpRelationDTO.getCodeChannelId());//渠道标识id【biz.cp_code_channel】
                userStaffCpRelation.setContactChange(userStaffCpRelationDTO.getContactChangeType());
                userStaffCpRelation.setContactChangeType(ContactChangeTypeEnum.getCode(userStaffCpRelationDTO.getContactChangeType()));
                userStaffCpRelation.setCorpName(StrUtil.isNotEmpty(userStaffCpRelationDTO.getCorpName())?userStaffCpRelationDTO.getCorpName():null);
                userStaffCpRelation.setCorpFullName(StrUtil.isNotEmpty(userStaffCpRelationDTO.getCorpFullName())?userStaffCpRelationDTO.getCorpFullName():null);
                userStaffCpRelation.setCpRemarkCorpName(StrUtil.isNotEmpty(userStaffCpRelationDTO.getCpRemarkCorpName())?userStaffCpRelationDTO.getCpRemarkCorpName():null);
                userStaffCpRelation.setStatus(1);
                userStaffCpRelationService.updateById(userStaffCpRelation);
                log.info("已添加好友，无需重复添加");

                //保存手机号关联
                relationPhoneService.saveTo(userStaffCpRelation.getId(),
                        userStaffCpRelation.getQiWeiUserId(),
                        userStaffCpRelation.getStaffId(),
                        userStaffCpRelation.getQiWeiStaffId(),
                        userStaffCpRelation.getCpRemarkMobiles(),
                        userStaffCpRelation.getStatus());
                return;
            }
            Long staffId = null;
            ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffByQiWeiUserId(qiWeiStaffId);
            if (staffResp.isSuccess() && Objects.nonNull(staffResp.getData())) {
                staffId = staffResp.getData().getId();
            }
            //保存
            UserStaffCpRelation saveUserStaffCpRelation = new UserStaffCpRelation();
            saveUserStaffCpRelation.setStatus(1);
            saveUserStaffCpRelation.setStaffId(staffId);
            saveUserStaffCpRelation.setQiWeiStaffId(qiWeiStaffId);
            saveUserStaffCpRelation.setQiWeiUserId(userStaffCpRelationDTO.getQiWeiUserId());
            saveUserStaffCpRelation.setUserUnionId(userStaffCpRelationDTO.getUserUnionId());
            saveUserStaffCpRelation.setCreateTime(Objects.nonNull(userStaffCpRelationDTO.getCpCreateTime())?userStaffCpRelationDTO.getCpCreateTime():new Date());
            saveUserStaffCpRelation.setUpdateTime(saveUserStaffCpRelation.getCreateTime());
            saveUserStaffCpRelation.setType(userStaffCpRelationDTO.getType());
            saveUserStaffCpRelation.setQiWeiNickName(userStaffCpRelationDTO.getQiWeiNickName());
            saveUserStaffCpRelation.setContactChange(userStaffCpRelationDTO.getContactChangeType());
            saveUserStaffCpRelation.setContactChangeType(ContactChangeTypeEnum.getCode(userStaffCpRelationDTO.getContactChangeType()));
            saveUserStaffCpRelation.setAutoType(userStaffCpRelationDTO.getAutoType());
            saveUserStaffCpRelation.setAvatar(userStaffCpRelationDTO.getAvatar());
            saveUserStaffCpRelation.setCpRemark(userStaffCpRelationDTO.getCpRemark());
            saveUserStaffCpRelation.setCpDescription(userStaffCpRelationDTO.getCpDescription());
            saveUserStaffCpRelation.setCpRemarkMobiles(getCpRemarkMobile(userStaffCpRelationDTO.getCpRemarkMobiles()));
//            saveUserStaffCpRelation.setCpCreateTime(userStaffCpRelationDTO.getCpCreateTime());
            saveUserStaffCpRelation.setCpCreateTime(new Date());
            saveUserStaffCpRelation.setGender(userStaffCpRelationDTO.getGender());
            saveUserStaffCpRelation.setCpAddWay(userStaffCpRelationDTO.getCpAddWay());
            saveUserStaffCpRelation.setCpState(userStaffCpRelationDTO.getCpState());//渠道源自动义唯一参数
            saveUserStaffCpRelation.setCodeGroupId(userStaffCpRelationDTO.getCodeGroupId());//渠道源分组id
            saveUserStaffCpRelation.setCodeChannelId(userStaffCpRelationDTO.getCodeChannelId());//渠道标识id【biz.cp_code_channel】
            saveUserStaffCpRelation.setCpOperUserId(userStaffCpRelationDTO.getCpOperUserId());
            saveUserStaffCpRelation.setCorpName(StrUtil.isNotEmpty(userStaffCpRelationDTO.getCorpName())?userStaffCpRelationDTO.getCorpName():null);
            saveUserStaffCpRelation.setCorpFullName(StrUtil.isNotEmpty(userStaffCpRelationDTO.getCorpFullName())?userStaffCpRelationDTO.getCorpFullName():null);
            saveUserStaffCpRelation.setCpRemarkCorpName(StrUtil.isNotEmpty(userStaffCpRelationDTO.getCpRemarkCorpName())?userStaffCpRelationDTO.getCpRemarkCorpName():null);
            userStaffCpRelationService.save(saveUserStaffCpRelation);

            //保存手机号关联
            relationPhoneService.saveTo(saveUserStaffCpRelation.getId(),
                    saveUserStaffCpRelation.getQiWeiUserId(),
                    saveUserStaffCpRelation.getStaffId(),
                    saveUserStaffCpRelation.getQiWeiStaffId(),
                    saveUserStaffCpRelation.getCpRemarkMobiles(),
                    saveUserStaffCpRelation.getStatus());

            //同时注册用户表: user
            userService.createUserByUnionId(saveUserStaffCpRelation);

            UpdateVipRelationFriendStateDTO stateDTO = new UpdateVipRelationFriendStateDTO();
            stateDTO.setStaffId(staffId);
            stateDTO.setVipUserId(saveUserStaffCpRelation.getUserId());
            stateDTO.setVipCpUserId(saveUserStaffCpRelation.getQiWeiUserId());
            return;
            //2 正在继承中
        }

        UserStaffCpRelation userStaffCpRelation = userStaffCpRelationService.getByQiWeiStaffIdAndQiWeiUserId(qiWeiStaffId, qiWeiUserId);
        if(userStaffCpRelation==null){
            log.info("好友关系不存在，解绑好友失败");
            return;
        }
        //2-解绑
        if (userStaffCpRelationDTO.getStatus() == 2) {
            userStaffCpRelation.setStatus(2);
            userStaffCpRelation.setUpdateTime(new Date());
            userStaffCpRelation.setContactChange(userStaffCpRelationDTO.getContactChangeType());
            userStaffCpRelation.setContactChangeType(ContactChangeTypeEnum.getCode(userStaffCpRelationDTO.getContactChangeType()));
            userStaffCpRelationService.updateById(userStaffCpRelation);
            //删除手机号关联
            relationPhoneService.deleteById(userStaffCpRelation.getQiWeiUserId(),userStaffCpRelation.getStatus());
        }else if (userStaffCpRelationDTO.getStatus() == 3) {
            // 3-删除客户
            String contactChangeType=userStaffCpRelationDTO.getContactChangeType();
            //判断是否双方互相删除
            if(userStaffCpRelationDTO.getContactChangeType().equals(WxCpConsts.ExternalContactChangeType.DEL_FOLLOW_USER)
            || userStaffCpRelationDTO.getContactChangeType().equals(WxCpConsts.ExternalContactChangeType.DEL_EXTERNAL_CONTACT)){
                if((userStaffCpRelation.getContactChange().equals(WxCpConsts.ExternalContactChangeType.DEL_FOLLOW_USER)//删除跟进成员事件
                        && userStaffCpRelationDTO.getContactChangeType().equals(WxCpConsts.ExternalContactChangeType.DEL_EXTERNAL_CONTACT))//删除企业客户事件
                        ||
                        (userStaffCpRelation.getContactChange().equals(WxCpConsts.ExternalContactChangeType.DEL_EXTERNAL_CONTACT)//删除企业客户事件
                                && userStaffCpRelationDTO.getContactChangeType().equals(WxCpConsts.ExternalContactChangeType.DEL_FOLLOW_USER))//删除跟进成员事件
                ){
                    contactChangeType= ContactChangeTypeEnum.DEL_ALL.getDesc();//互相删除
                }
            }
            userStaffCpRelation.setUpdateTime(new Date());
            userStaffCpRelation.setStatus(3);
            userStaffCpRelationService.deleteById(userStaffCpRelation.getId(),contactChangeType);
            //删除手机号关联
            relationPhoneService.deleteById(userStaffCpRelation.getQiWeiUserId(),userStaffCpRelation.getStatus());
        }
    }

    private String getCpRemarkMobile(String cpRemarkMobiles){
        return StrUtil.isNotBlank(cpRemarkMobiles)&&(!cpRemarkMobiles.equals("{}") || !cpRemarkMobiles.equals("[]"))?cpRemarkMobiles:null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserStaffCpRelationStatus(UserStaffCpRelationDTO userStaffCpRelationDTO) {
        log.info("updateUserStaffCpRelationStatus param : {} ", JSONObject.toJSONString(userStaffCpRelationDTO));
        //员工企微id
        String qiWeiStaffId = userStaffCpRelationDTO.getQiWeiStaffId();
        //客户外部联系人id
        String qiWeiUserId = userStaffCpRelationDTO.getQiWeiUserId();
        UserStaffCpRelation userStaffCpRelation = userStaffCpRelationService.getByQiWeiStaffIdAndQiWeiUserId(qiWeiStaffId, qiWeiUserId);
        if(userStaffCpRelation==null){
            log.info("好友关系不存在，解绑好友失败");
            return;
        }
        //2-解绑
        if (userStaffCpRelationDTO.getStatus() == 2) {
            userStaffCpRelation.setStatus(2);
            userStaffCpRelation.setUpdateTime(new Date());
            userStaffCpRelation.setUpdateBy("在离职分配");
            if(StrUtil.isNotEmpty(userStaffCpRelationDTO.getContactChangeType())){
                userStaffCpRelation.setContactChange(userStaffCpRelationDTO.getContactChangeType());
                userStaffCpRelation.setContactChangeType(ContactChangeTypeEnum.getCode(userStaffCpRelationDTO.getContactChangeType()));
            }
            userStaffCpRelationService.updateById(userStaffCpRelation);
            //删除手机号关联
            relationPhoneService.deleteById(userStaffCpRelation.getQiWeiUserId(),userStaffCpRelation.getStatus());
        }
        //2-分配失败继续绑定
        if (userStaffCpRelationDTO.getStatus() == 1) {
            userStaffCpRelation.setStatus(1);
            userStaffCpRelation.setUpdateTime(new Date());
            userStaffCpRelation.setUpdateBy("在离职分配失败");
            userStaffCpRelationService.updateById(userStaffCpRelation);
            //保存手机号关联
            relationPhoneService.saveTo(userStaffCpRelation.getId(),
                    userStaffCpRelation.getQiWeiUserId(),
                    userStaffCpRelation.getStaffId(),
                    userStaffCpRelation.getQiWeiStaffId(),
                    userStaffCpRelation.getCpRemarkMobiles(),
                    userStaffCpRelation.getStatus());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserStaffCpRelationRemark(UserStaffCpRelationDTO userStaffCpRelationDTO) {
        log.info("修改客户备注 入参 : {} ", JSONObject.toJSONString(userStaffCpRelationDTO));
        //员工企微id
        String qiWeiStaffId = userStaffCpRelationDTO.getQiWeiStaffId();
        //客户外部联系人id
        String qiWeiUserId = userStaffCpRelationDTO.getQiWeiUserId();
        UserStaffCpRelation userStaffCpRelation = userStaffCpRelationService.getByQiWeiStaffIdAndQiWeiUserId(qiWeiStaffId, qiWeiUserId);
        if(userStaffCpRelation==null){
            log.info("操作失败，好友关系不存在");
            return;
        }
        userStaffCpRelation.setCpRemark(userStaffCpRelationDTO.getCpRemark());
        userStaffCpRelation.setCpDescription(userStaffCpRelationDTO.getCpDescription());
        userStaffCpRelation.setUpdateTime(new Date());
        userStaffCpRelationService.updateById(userStaffCpRelation);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserStaffCpRelationAutoType(UserStaffCpRelationDTO userStaffCpRelationDTO) {
        log.info("修改客户自动通过好友关系 入参 : {} ", JSONObject.toJSONString(userStaffCpRelationDTO));
        //员工企微id
        String qiWeiStaffId = userStaffCpRelationDTO.getQiWeiStaffId();
        //客户外部联系人id
        String qiWeiUserId = userStaffCpRelationDTO.getQiWeiUserId();
        UserStaffCpRelation userStaffCpRelation = userStaffCpRelationService.getByQiWeiStaffIdAndQiWeiUserId(qiWeiStaffId, qiWeiUserId);
        if(userStaffCpRelation==null){
            log.info("操作失败，好友关系不存在");
            return;
        }
        userStaffCpRelation.setAutoType(userStaffCpRelationDTO.getAutoType());
        userStaffCpRelation.setUpdateTime(new Date());
        userStaffCpRelationService.updateById(userStaffCpRelation);
    }
}
