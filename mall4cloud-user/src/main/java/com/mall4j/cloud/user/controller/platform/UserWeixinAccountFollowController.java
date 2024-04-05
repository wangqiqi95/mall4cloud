package com.mall4j.cloud.user.controller.platform;


import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.UserWeixinAccountFollowService;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataVo;
import com.mall4j.cloud.user.vo.UserWeixinAccountFollowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController("UserWeixinAccountFollowController")
@RequestMapping("/p/user/weixin/follow")
@Api(tags = "用户关注微信公众号管理")
public class UserWeixinAccountFollowController {

    @Autowired
    private UserWeixinAccountFollowService followService;

    @GetMapping("/pageUserFollowList")
    @ApiOperation(value = "粉丝管理列表", notes = "粉丝管理列表")
    public ServerResponseEntity<PageVO<UserWeixinAccountFollowVo>> pageUserFollowList(@Valid PageDTO pageDTO, UserWeixinccountFollowSelectDTO dto) {
        return ServerResponseEntity.success(followService.pageUserFollowList(pageDTO,dto));
    }

    @GetMapping("/userFollowData")
    @ApiOperation(value = "粉丝分析", notes = "粉丝分析")
    public ServerResponseEntity<UserWeixinAccountFollowDataVo> userFollowData(UserWeixinccountFollowSelectDTO dto) {
        return ServerResponseEntity.success(followService.userFollowData(dto));
    }

}
