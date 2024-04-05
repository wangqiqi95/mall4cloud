package com.mall4j.cloud.group.controller.ad.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.AdInfoDTO;
import com.mall4j.cloud.group.service.OpenScreenAdBizService;
import com.mall4j.cloud.group.vo.app.AppAdInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/OpenScreen")
@Api(tags = "app-开屏广告")
public class OpenScreenAdAppController {
    @Resource
    private OpenScreenAdBizService openScreenAdBizService;


    @PostMapping("/ad")
    @ApiOperation(value = "开屏广告信息", notes = "开屏广告信息")
    public ServerResponseEntity<AppAdInfoVO> adInfo(@RequestBody AdInfoDTO param){
        Long userId = AuthUserContext.get().getUserId();
        param.setUserId(userId);
        return openScreenAdBizService.adInfo(param);
    }

}
