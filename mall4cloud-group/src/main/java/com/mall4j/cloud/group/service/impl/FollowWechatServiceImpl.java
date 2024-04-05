package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.group.feign.dto.UserFollowWechatActivityDTO;
import com.mall4j.cloud.group.dto.FollowWechatPageDTO;
import com.mall4j.cloud.group.mapper.FollowWechatActivityMapper;
import com.mall4j.cloud.group.model.FollowWechatActivity;
import com.mall4j.cloud.group.service.FollowWechatService;
import com.mall4j.cloud.group.vo.FollowWechatListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FollowWechatServiceImpl extends ServiceImpl<FollowWechatActivityMapper,FollowWechatActivity> implements FollowWechatService {
    @Resource
    private FollowWechatActivityMapper followWechatActivityMapper;
    @Override
    public List<FollowWechatListVO> followWechatList(FollowWechatPageDTO param) {
        return followWechatActivityMapper.followWechatList(param);
    }

    @Override
    public FollowWechatActivity selectFirstActivity(UserFollowWechatActivityDTO params) {
        return followWechatActivityMapper.selectFirstActivity(params);
    }
}
