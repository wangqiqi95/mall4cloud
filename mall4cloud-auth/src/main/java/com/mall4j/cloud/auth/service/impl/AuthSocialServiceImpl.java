package com.mall4j.cloud.auth.service.impl;

import com.mall4j.cloud.auth.mapper.AuthSocialMapper;
import com.mall4j.cloud.auth.model.AuthSocial;
import com.mall4j.cloud.auth.service.AuthSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
@Service
public class AuthSocialServiceImpl implements AuthSocialService {

    @Autowired
	private AuthSocialMapper authSocialMapper;

    @Override
    public AuthSocial getByBizUserId(String bizUserId, Integer socialType) {
        return authSocialMapper.getByBizUserIdAndType(bizUserId, socialType);
    }

    @Override
    public AuthSocial getByBizUnionIdAndType(String bizUserId, Integer socialType) {
        return authSocialMapper.getByBizUnionIdAndType(bizUserId, socialType);
    }

    @Override
    public void saveOrUpdate(AuthSocial authSocial) {
        if(authSocial.getId() == null) {
            authSocialMapper.save(authSocial);
        } else {
            authSocialMapper.update(authSocial);
        }
    }

    @Override
    public AuthSocial getByTempUid(String tempUid) {
        return authSocialMapper.getByTempUid(tempUid);
    }

    @Override
    public int countByUidAndSocialType(Long uid, Integer socialType) {
        return authSocialMapper.countByUidAndSocialType(uid, socialType);
    }


}
