package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.wx.AddJoinWay;
import com.mall4j.cloud.biz.dto.cp.wx.WxBizCpUserExternalGroupChatTransferResp;
import com.mall4j.cloud.biz.dto.cp.wx.WxCpGroupChatResult;
import com.mall4j.cloud.biz.dto.cp.wx.WxCpJoinWayResult;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * @Author: Administrator
 * @Description: 企业群操作
 * @Date: 2022-02-10 15:10
 * @Version: 1.0
 */
public interface WxCpGroupChatService {
    /**
     * 配置客户群进群方式
     * 企业可以在管理后台-客户联系中配置「加入群聊」的二维码或者小程序按钮，客户通过扫描二维码或点击小程序上的按钮，即可加入特定的客户群。
     * @param addJoinWay 群的名称
     */
    String addJoinWay(AddJoinWay addJoinWay);
    /**
     * 获取客户群进群方式配置
     * 获取企业配置的群二维码或小程序按钮。
     * @param configId  企业联系方式的配置id
     */
    WxCpGroupChatResult getJoinWay(String configId);

    /**
     * 更新客户群进群方式配置
     * 更新进群方式配置信息。注意：使用覆盖的方式更新。
     * @param addJoinWay 成员id
     */
    boolean updateJoinWay(AddJoinWay addJoinWay);

    /**
     * 删除一个进群方式配置。
     * @param configId 企业联系方式的配置id
     */
    boolean delJoinWay(String configId);

    /**
     *分配离职成员的客户群
     * @param chatIds
     * @param newOwner
     * @return
     * @throws WxErrorException
     */
    WxBizCpUserExternalGroupChatTransferResp transferGroupChat(String[] chatIds, String newOwner) throws WxErrorException;

    /**
     * 分配在职成员的客户群
     * @param chatIds
     * @param newOwner
     * @return
     * @throws WxErrorException
     */
    WxBizCpUserExternalGroupChatTransferResp onjobTransferGroupChat(String[] chatIds, String newOwner) throws WxErrorException;
}
