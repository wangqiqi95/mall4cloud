package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserWeixinAccountFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户关注取关公众号接口
 *
 * @luzhengxiang
 * @create 2022-04-01 4:20 PM
 **/
@RestController
@RequestMapping("/ua/user/follow")
@Api(tags="用户关注取关公众号接口")
public class UserFollowWechatController {
    @Autowired
    UserWeixinAccountFollowService userWeixinAccountFollowService;


    @ApiOperation(value="关注取关公众号")
    @PostMapping
    public ServerResponseEntity<Void> follow(@Valid @RequestBody UserWeixinccountFollowDTO param) {
        userWeixinAccountFollowService.follow(param);
        return ServerResponseEntity.success();
    }
}
