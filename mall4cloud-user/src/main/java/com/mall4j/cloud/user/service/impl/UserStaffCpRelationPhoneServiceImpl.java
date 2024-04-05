package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationPhoneMapper;
import com.mall4j.cloud.user.model.UserStaffCpRelationPhone;
import com.mall4j.cloud.user.service.UserStaffCpRelationPhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-28 14:35:39
 */
@Slf4j
@Service
public class UserStaffCpRelationPhoneServiceImpl extends ServiceImpl<UserStaffCpRelationPhoneMapper, UserStaffCpRelationPhone> implements UserStaffCpRelationPhoneService {

    @Autowired
    private UserStaffCpRelationPhoneMapper userStaffCpRelationPhoneMapper;

    @Override
    public void saveTo(Long relationId, String userId, Long staffId,String staffUserId,String mobiles,Integer status) {
        log.info("UserStaffCpRelationPhone save: relationId:{} userId:{} mobiles:{}",relationId,userId,mobiles);
        deleteById(userId,3);
        mobiles=getCpRemarkMobile(mobiles);
        if(StrUtil.isEmpty(mobiles)){
            return;
        }
        List<String> mobile=JSONArray.parseArray(mobiles,String.class);
        List<UserStaffCpRelationPhone> relationPhones=new ArrayList<>();
        for (String s : mobile) {
            UserStaffCpRelationPhone phone=new UserStaffCpRelationPhone();
            phone.setDelFlag(0);
            phone.setPhone(s);
            phone.setRelationId(relationId);
            phone.setUserId(userId);
            phone.setStatus(status);
            phone.setStaffId(staffId);
            phone.setStaffUserId(staffUserId);
            relationPhones.add(phone);
        }

        this.saveBatch(relationPhones);
    }

    @Override
    public List<UserStaffCpRelationPhone> getListByPhone(List<String> phones) {
        LambdaQueryWrapper<UserStaffCpRelationPhone> lambdaQueryWrapper=new LambdaQueryWrapper<UserStaffCpRelationPhone>();
        lambdaQueryWrapper.eq(UserStaffCpRelationPhone::getDelFlag,0);
        lambdaQueryWrapper.eq(UserStaffCpRelationPhone::getStatus,1);
        lambdaQueryWrapper.in(UserStaffCpRelationPhone::getPhone,phones);
        List<UserStaffCpRelationPhone> list=this.list(lambdaQueryWrapper);
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        //qiWeiUserId 去重复【一个客户会对应多个员工】
        return new ArrayList<>(list.stream().collect(Collectors.toMap(UserStaffCpRelationPhone::getPhone, s->s, (v1,v2)->v2)).values());
    }

    private String getCpRemarkMobile(String cpRemarkMobiles){
        return StrUtil.isNotBlank(cpRemarkMobiles)&&(!cpRemarkMobiles.equals("{}") || !cpRemarkMobiles.equals("[]"))?cpRemarkMobiles:null;
    }

    @Override
    public void deleteById(String userId,Integer status) {
        this.update(new LambdaUpdateWrapper<UserStaffCpRelationPhone>()
                .eq(UserStaffCpRelationPhone::getUserId,userId)
                .eq(UserStaffCpRelationPhone::getStatus,status)
                .set(UserStaffCpRelationPhone::getDelFlag,1));
    }

}
