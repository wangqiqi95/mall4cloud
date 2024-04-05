package com.mall4j.cloud.group.controller.sign.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.UserNoticeDTO;
import com.mall4j.cloud.group.dto.UserSignDTO;
import com.mall4j.cloud.group.service.SignActivityBizService;
import com.mall4j.cloud.group.vo.app.SignActivityAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sign_activity")
@Api(tags = "app-签到活动")
public class SignController {
    @Resource
    private SignActivityBizService signActivityBizService;

    @GetMapping("/info")
    @ApiOperation(value = "用户签到信息", notes = "用户签到信息")
    public ServerResponseEntity<SignActivityAppVO> signInfo(){
        Long userId = AuthUserContext.get().getUserId();
        return signActivityBizService.signInfo(userId);
    }

    @PostMapping("/sign")
    @ApiOperation(value = "用户签到", notes = "用户签到")
    public ServerResponseEntity<Void> sign(@RequestBody UserSignDTO param){
        Long userId = AuthUserContext.get().getUserId();
        Long storeId = AuthUserContext.get().getStoreId();
        param.setUserId(userId);
        param.setShopId(storeId);
        return signActivityBizService.sign(param);
    }

    @PostMapping("/series")
    @ApiOperation(value = "用户领取连签奖励", notes = "用户领取连签奖励")
    public ServerResponseEntity<Void> series(@RequestBody UserSignDTO param){
        Long userId = AuthUserContext.get().getUserId();
        Long storeId = AuthUserContext.get().getStoreId();
        param.setUserId(userId);
        param.setShopId(storeId);
        return signActivityBizService.series(param);
    }

    @PostMapping("/disable")
    @ApiOperation(value = "用户禁用签到通知", notes = "用户禁用签到通知")
    public ServerResponseEntity<Void> disable (@RequestBody UserNoticeDTO param){
        Long userId = AuthUserContext.get().getUserId();
        param.setUserId(userId);
        return signActivityBizService.disableUserNotice(param);
    }

    @PostMapping("/enable")
    @ApiOperation(value = "用户开启签到通知", notes = "用户开启签到通知")
    public ServerResponseEntity<Void> enable (@RequestBody UserNoticeDTO param){
        Long userId = AuthUserContext.get().getUserId();
        param.setUserId(userId);
        return signActivityBizService.enableUserNotice(param);
    }
}
