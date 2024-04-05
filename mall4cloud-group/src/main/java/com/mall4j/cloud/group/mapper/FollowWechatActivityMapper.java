package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.group.feign.dto.UserFollowWechatActivityDTO;
import com.mall4j.cloud.group.dto.FollowWechatPageDTO;
import com.mall4j.cloud.group.model.FollowWechatActivity;
import com.mall4j.cloud.group.vo.FollowWechatListVO;

import java.util.List;

public interface FollowWechatActivityMapper extends BaseMapper<FollowWechatActivity> {
    List<FollowWechatListVO> followWechatList(FollowWechatPageDTO param);

    FollowWechatActivity selectFirstActivity(UserFollowWechatActivityDTO params);
}
