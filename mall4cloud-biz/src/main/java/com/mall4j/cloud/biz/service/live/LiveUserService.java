

package com.mall4j.cloud.biz.service.live;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.live.LiveUser;
import com.mall4j.cloud.biz.dto.live.ReturnLiveParam;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 *
 *
 * @author lhd
 * @date 2020-11-20 17:49:56
 */
public interface LiveUserService extends IService<LiveUser> {

    String CREATE_LIVE_USER = "https://api.weixin.qq.com/wxaapi/broadcast/role/addrole";
    String GET_LIVE_USER_INFO = "https://api.weixin.qq.com/wxaapi/broadcast/role/getrolelist";
    String DELETE_LIVE_USER = "https://api.weixin.qq.com/wxaapi/broadcast/role/deleterole";

    /**
     * 保存直播成员信息
     * @param liveUser 直播成员
     * @throws WxErrorException 微信异常
     * @return 结果
     */
    ReturnLiveParam saveInfo(LiveUser liveUser)throws WxErrorException;

    /**
     * 同步微信的直播间成员列表
     * @throws WxErrorException 微信返回异常
     */
    void synchronousWxLiveUsers() throws WxErrorException ;

    /**
     * 删除直播成员信息
     * @param liveUserId 直播成员信息
     * @throws WxErrorException 微信返回异常
     * @return 结果
     */
    boolean removeAndWeChatById(Long liveUserId) throws WxErrorException;
}
