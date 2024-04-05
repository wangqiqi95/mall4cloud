package com.mall4j.cloud.biz.feign;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.feign.WxMpApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinWebAppInfoVo;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinActoinLogsService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Date 2022年02月14日, 0030 14:48
 * @Created by gmq
 */
@RestController
@RequiredArgsConstructor
public class WxMpApiController implements WxMpApiFeignClient {

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinActoinLogsService weixinActoinLogsService;

    @Override
    public ServerResponseEntity<WeixinWebAppInfoVo> getWxMpInfo(String appId) {
        if (StrUtil.isBlank(appId)) return null;
        WeixinWebApp weixinWebApp = weixinWebAppService.queryByAppid(appId);
        if (weixinWebApp != null) {
            WeixinWebAppInfoVo weixinWebAppInfoVo = new WeixinWebAppInfoVo();
            weixinWebAppInfoVo.setName(weixinWebApp.getName());
            weixinWebAppInfoVo.setCrmType(weixinWebApp.getCrmType());
            return ServerResponseEntity.success(weixinWebAppInfoVo);
        }
        return null;
    }

    @Override
    public ServerResponseEntity<String> getWxMpAccessToken(String appId) {
        String accessToken = weixinWebAppService.getAccessTokenByAppid(appId);
        if (StrUtil.isNotEmpty(accessToken)) {
            return ServerResponseEntity.success(accessToken);
        } else {
            return ServerResponseEntity.showFailMsg("获取公众号AccessToken失败 公众号未授权。");
        }
    }

    @Override
    public ServerResponseEntity<List<String>> listWechatAppIdById(List<Long> ids) {
        return ServerResponseEntity.success(weixinWebAppService.listWechatAppIdByIds(ids));
    }

    @Override
    public ServerResponseEntity<List<UserWeixinAccountFollowDataListVo>> fansDataByAppId(String startTime, String endTime, String appId) {
        UserWeixinccountFollowSelectDTO dto=new UserWeixinccountFollowSelectDTO();
        dto.setAppId(appId);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        return ServerResponseEntity.success(weixinActoinLogsService.fansDataByAppId(dto));
    }
}
