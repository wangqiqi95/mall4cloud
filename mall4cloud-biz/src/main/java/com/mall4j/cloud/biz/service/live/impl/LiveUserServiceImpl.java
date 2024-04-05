

package com.mall4j.cloud.biz.service.live.impl;


import cn.binarywang.wx.miniapp.json.WxMaGsonBuilder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.*;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.live.LiveUserRespInfo;
import com.mall4j.cloud.biz.mapper.live.LiveUserMapper;
import com.mall4j.cloud.biz.model.live.LiveInterfaceType;
import com.mall4j.cloud.biz.model.live.LiveUser;
import com.mall4j.cloud.biz.dto.live.ReturnLiveParam;
import com.mall4j.cloud.biz.service.live.LiveLogService;
import com.mall4j.cloud.biz.service.live.LiveUserService;
import com.mall4j.cloud.biz.util.WxInterfaceUtil;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.json.GsonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 *
 * @author lhd
 * @date 2020-11-20 17:49:56
 */
@Service
@AllArgsConstructor
public class LiveUserServiceImpl extends ServiceImpl<LiveUserMapper, LiveUser> implements LiveUserService {

    private final LiveUserMapper liveUserMapper;
    private final LiveLogService liveLogService;
    private final WxInterfaceUtil wxInterfaceUtil;
    private final WxConfig wxConfig;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnLiveParam saveInfo(LiveUser liveUser) throws WxErrorException {
        int count = count(new LambdaQueryWrapper<LiveUser>().eq(LiveUser::getAnchorWechat, liveUser.getAnchorWechat()));
        if(count > 0){
            throw new LuckException("该用户已存在");
        }
        for (Integer role : liveUser.getRoleList()) {
//            LiveUserReqInfo liveUserReqInfo = new LiveUserReqInfo();
//            liveUserReqInfo.setUsername(liveUser.getAnchorWechat());
//            liveUserReqInfo.setRole(role);
//            int count = count(new LambdaQueryWrapper<LiveUser>().eq(LiveUser::getAnchorWechat, liveUser.getAnchorWechat()).like(LiveUser::getRoles,role.toString()));
//            if(count > 0){
//                throw new LuckException("yami.sys.user.already");
//            }
            try {
                wxConfig.getWxMaService().getLiveMemberService().addRole(liveUser.getAnchorWechat(), role);
//                wxConfig.getWxMaService().post(CREATE_LIVE_USER, WxMaGsonBuilder.create().toJson(liveUserReqInfo));
            }catch (WxErrorException wxException) {
                wxException.getError();
                if (wxException.getError().getErrorCode() != 0) {
                    if (wxException.getError().getErrorCode() == 400001) {
                        // 微信号不合规
                        throw new LuckException("微信号不合规");
                    }
                    if (wxException.getError().getErrorCode() == 400002) {
//                        // 微信号需要实名认证，仅设置主播角色时可能出现
//                        throw new LuckException("yami.live.user.name.check");
                        JsonObject jsonObject = GsonParser.parse(wxException.getError().getJson());
                        ReturnLiveParam returnUser = new ReturnLiveParam();
                        returnUser.setIsRealName(0);
                        returnUser.setImg(jsonObject.get("codeurl").getAsString());
                        return returnUser;
                    }
                    if (wxException.getError().getErrorCode() == 400003) {
                        // 添加角色达到上限
                        throw new LuckException("添加角色达到上限");
                    }
                    if (wxException.getError().getErrorCode() == 400004) {
                        // 用户已具有管理员角色
                        throw new LuckException("用户已具有管理员角色");
                    }
                    if (wxException.getError().getErrorCode() == 400005) {
                        // 主播角色删除失败，该主播存在未开播的直播间
                        throw new LuckException("主播角色删除失败，该主播存在未开播的直播间");
                    }
                    throw new WxErrorException(WxError.fromJson(wxException.getError().getErrorMsg(), WxType.MiniApp));
                }
            }
        }
        // 1.校验今日可用次数并保存or修改商家次数记录
        liveLogService.checkNumsAndSaveLog(liveUser.getShopId(), LiveInterfaceType.ADD_LIVE_ROLE, "添加直播成员权限");
        // 获取一遍当前用户的openId再保存，用于后续更新操作
        syncLiveUser(liveUser);
        return null;
    }

    private void syncLiveUser(LiveUser liveUser) throws WxErrorException {
        List<LiveUserRespInfo> liveUserList = getLiveInfo(0, 1, liveUser.getAnchorWechat());
        if(CollectionUtil.isNotEmpty(liveUserList)){
            LiveUserRespInfo liveUserRespInfo = liveUserList.get(0);
            Date date = new Date();
            liveUser.setUpdateTime(date);
            liveUser.setCreateTime(date);
            liveUser.setUserName(StrUtil.hide(liveUser.getAnchorWechat(), 2, liveUser.getAnchorWechat().length() - 2));
            // 将数组转成字符串
            StringBuilder roleList = new StringBuilder(100);
            for (Integer role : liveUserRespInfo.getRoleList()) {
                roleList.append(role).append(StrUtil.COMMA);
            }
            if (roleList.lastIndexOf(StrUtil.COMMA) == roleList.length() - 1) {
                roleList.deleteCharAt(roleList.length() - 1);
            }
            liveUser.setRoles(roleList.toString());
            liveUser.setOpenId(liveUserRespInfo.getOpenid());
            liveUser.setNickName(liveUserRespInfo.getNickname());
            liveUser.setHeadingImg(liveUserRespInfo.getHeadingimg());
            save(liveUser);
        }
    }

    @Override
    public void synchronousWxLiveUsers() throws WxErrorException {
        List<LiveUserRespInfo> liveInfos = getLiveInfos();
        if(CollectionUtil.isEmpty(liveInfos)){
            return;
        }
        Date date = new Date();
        List<LiveUser> liveUsers = list(null);
        Map<String, LiveUserRespInfo> userRespInfoMap = liveInfos.stream().collect(Collectors.toMap(LiveUserRespInfo::getOpenid, liveUserRespInfo -> liveUserRespInfo));
        List<LiveUser> updateLiveUsers = new ArrayList<>();
        List<Long> deleteUserIds = new ArrayList<>();
        for (LiveUser liveUser : liveUsers) {
            if(userRespInfoMap.containsKey(liveUser.getOpenId())){
                LiveUserRespInfo liveUserRespInfo = userRespInfoMap.get(liveUser.getOpenId());
                liveUser.setUpdateTime(date);
                liveUser.setOpenId(liveUserRespInfo.getOpenid());
                liveUser.setNickName(liveUserRespInfo.getNickname());
                liveUser.setHeadingImg(liveUserRespInfo.getHeadingimg());
                // 将数组转成字符串
                StringBuilder roleList = new StringBuilder(100);
                for (Integer role : liveUserRespInfo.getRoleList()) {
                    roleList.append(role).append(StrUtil.COMMA);
                }
                if (roleList.lastIndexOf(",") == roleList.length() - 1) {
                    roleList.deleteCharAt(roleList.length() - 1);
                }
                liveUser.setRoles(roleList.toString());
                updateLiveUsers.add(liveUser);
            }else{
                deleteUserIds.add(liveUser.getLiveUserId());
            }
        }
        if(CollectionUtil.isNotEmpty(updateLiveUsers)){
            updateBatchById(updateLiveUsers);
        }
        if(CollectionUtil.isNotEmpty(deleteUserIds)){
            removeByIds(deleteUserIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAndWeChatById(Long liveUserId) throws WxErrorException {
        LiveUser liveUser = getById(liveUserId);
        int[] roles = StrUtil.splitToInt(liveUser.getRoles(), StrUtil.COMMA);
        Map<String, Object> map = new HashMap<>(5);
        map.put("username", liveUser.getAnchorWechat());
        for (int role : roles) {
            // 1.校验今日可用次数并保存or修改商家次数记录
            liveLogService.checkNumsAndSaveLog(liveUser.getShopId(), LiveInterfaceType.REMOVE_LIVE_ROLE, "删除直播成员权限");
            map.put("role", role);
            try {
                wxConfig.getWxMaService().post(DELETE_LIVE_USER, WxMaGsonBuilder.create().toJson(map));
            }catch (WxErrorException wxException) {
                log.error(wxException.getError().getErrorMsg());
                // TODO 先不写枚举，等以后换开源api在改
                if (wxException.getError().getErrorCode() != 0) {
                    if (wxException.getError().getErrorCode() == 2003) {
                        // jue
                        throw new LuckException("主播角色删除失败，该主播存在未开播的直播间");
                    }

                    if (wxException.getError().getErrorCode() == 400005) {
                        // 主播角色删除失败，该主播存在未开播的直播间
                        throw new LuckException("主播角色删除失败，该主播存在未开播的直播间");
                    }
                    throw new WxErrorException(WxError.fromJson(wxException.getError().getErrorMsg(), WxType.MiniApp));
                }
            }
        }
        removeById(liveUserId);
        return true;
    }


    private List<LiveUserRespInfo> getLiveInfos() throws WxErrorException {
        List<LiveUserRespInfo> results = new ArrayList<>();
        int start = 0;
        int limit = 30;
        int total = 0;
        boolean flag = true;
        while (true) {
            List<LiveUserRespInfo> respInfoList = getLiveInfo(start, limit,"");
            results.addAll(respInfoList);
            total = respInfoList.size();
            start = results.size();
            // 如果当前的总数小于一页的数据，则退出循环
            if (limit > total) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
        return results;
    }


    private List<LiveUserRespInfo> getLiveInfo(Integer start, Integer limit, String keyword) throws WxErrorException {
        List<LiveUserRespInfo> liveUsers = new ArrayList<>();
        try {
            JsonArray memberJsonArray = wxConfig.getWxMaService().getLiveMemberService().listByRole(-1,start,limit,keyword);
            Gson gson = new GsonBuilder().create();
            System.out.println(memberJsonArray);
            for (JsonElement jsonElement : memberJsonArray) {
                LiveUserRespInfo liveUser = gson.fromJson(jsonElement, LiveUserRespInfo.class);
                liveUsers.add(liveUser);
            }
        }catch (WxErrorException e){
            log.error("微信请求直播成员数据异常"+ e.getMessage());
        }
        return liveUsers;
    }
}
