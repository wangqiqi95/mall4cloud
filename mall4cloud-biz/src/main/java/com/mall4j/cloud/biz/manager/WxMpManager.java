package com.mall4j.cloud.biz.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowsDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinActoinLogs;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinActoinLogsService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WxMpManager {

    private final WeixinWebAppService weixinWebAppService;
    private final WxConfig wxConfig;
    private final UserFeignClient userFeignClient;
    private final WeixinActoinLogsService weixinActoinLogsService;
    /**
     * 获取公众号关注用户列表
     */
    public void getWxMpFollowDatas(){
        /**
         * 1.获取已授权且配置密钥的公众号列表
         * 2.获取公众号token
         * 3.获取公众号用户关注列表
         * 4.处理关注数据
         */
        String appId=null;
        List<WeixinWebApp> apps=weixinWebAppService.getWxMpFollowDatas(appId).stream()
                .filter(item-> StrUtil.isNotEmpty(item.getWeixinAppSecret())).collect(Collectors.toList());
        if(CollUtil.isEmpty(apps)){
            return;
        }
        for (WeixinWebApp app : apps) {
            try {
                WxMp wxMp = new WxMp();
                wxMp.setAppId(app.getWeixinAppId());
                wxMp.setSecret(app.getWeixinAppSecret());
                WxMpUserList wxMpUserList=wxConfig.getWxMpService(wxMp).getUserService().userList();
                log.info("获取公众号关注用户列表appid:{}--userList--{}",wxMp.getAppId(), JSON.toJSONString(wxMpUserList));
                if(CollUtil.isNotEmpty(wxMpUserList.getOpenids())){
                    UserWeixinccountFollowsDTO followsDTO=new UserWeixinccountFollowsDTO();
                    followsDTO.setAppId(app.getWeixinAppId());
                    List<UserWeixinccountFollowDTO> followDTOs=new ArrayList<>();
                    List<WeixinActoinLogs> actoinLogs=new ArrayList<>();
                    List<WxMpUser> wxMpUsers=getWxMpUsers(wxMp,wxMpUserList.getOpenids());
                    Map<String,WxMpUser> wxMpUserMap= LambdaUtils.toMap(wxMpUsers,WxMpUser::getOpenId);
                    for (String openid : wxMpUserList.getOpenids()) {
                        WxMpUser wxMpUser=wxMpUserMap.get(openid);
                        log.info("WxMpUser----->"+wxMpUser.toString());
                        UserWeixinccountFollowDTO followDTO=new UserWeixinccountFollowDTO();
                        followDTO.setSubscribeScene(wxMpUser.getSubscribeScene());
                        followDTO.setAppId(app.getWeixinAppId());
                        followDTO.setOpenId(wxMpUser.getOpenId());
                        followDTO.setStatus(1);
                        followDTO.setUnionId(wxMpUser.getUnionId());
                        followDTO.setCreateTime(WechatUtils.formatDate(wxMpUser.getSubscribeTime().toString()));
                        followDTOs.add(followDTO);

                        //日志表【活跃数据】
                        WeixinActoinLogs weixinActoinLogs=new WeixinActoinLogs();
                        weixinActoinLogs.setAppId(app.getAppId());
                        weixinActoinLogs.setToUserName(app.getAppId());
                        weixinActoinLogs.setFromUserName(wxMpUser.getOpenId());
                        weixinActoinLogs.setPutTime(WechatUtils.formatDate(wxMpUser.getSubscribeTime().toString()));
                        weixinActoinLogs.setMsgType("event");
                        weixinActoinLogs.setEvent("subscribe");
                        weixinActoinLogs.setEventKey(wxMpUser.getSubscribeScene());
                        weixinActoinLogs.setCreateTime(new Date());
                        weixinActoinLogs.setDelFlag(0);
                        actoinLogs.add(weixinActoinLogs);
                    }
                    followsDTO.setFollowDTOList(followDTOs);
                    userFeignClient.followWeixinAccounts(followsDTO);
                    if(CollUtil.isNotEmpty(actoinLogs)){
                        weixinActoinLogsService.saveBatch(actoinLogs);
                    }
                }
            }catch (Exception e){
                log.error("获取公众号关注用户列表失败：appId:{} {} {}",app.getWeixinAppId(),e.getMessage(), e);
            }
        }


    }

    /**
     * 批量获取已关注公众号用户信息
     * 开发者可通过该接口来批量获取用户基本信息。最多支持一次拉取100条。
     * @param wxMp
     * @param openIds
     * @return
     */
    private List<WxMpUser> getWxMpUsers(WxMp wxMp,List<String> openIds){
        List<WxMpUser> wxMpUsers=new ArrayList<>();
        try {
            //开发者可通过该接口来批量获取用户基本信息。最多支持一次拉取100条。
            List<List<String>> newList = Lists.partition(openIds, 100);
            for (List<String> openIdList : newList) {
                List<WxMpUser> users=wxConfig.getWxMpService(wxMp).getUserService().userInfoList(openIdList);
                wxMpUsers.addAll(users);
            }
        }catch (Exception e){
            log.error("批量获取公众号关注用户信息失败： {} {}",e.getMessage(), e);
        }
        return wxMpUsers;
    }

}
