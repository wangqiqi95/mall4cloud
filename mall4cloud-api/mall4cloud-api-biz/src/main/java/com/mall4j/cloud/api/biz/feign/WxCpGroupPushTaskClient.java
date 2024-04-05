package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.vo.WeixinWebAppInfoVo;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgSendResultVO;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgTaskVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @date 2023年03月20日
 * @author Peter_Tan
 */
@FeignClient(value = "mall4cloud-biz", contextId = "wxCpGroupPushTaskApi")
public interface WxCpGroupPushTaskClient {

    /**
     * 调用企业微信获取群发成员发送任务列表接口
     * @param msgId 群发消息的id
     * @param limit 返回的最大记录数
     * @param cursor 用于分页查询的游标
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ua/get/group/message/task")
    ServerResponseEntity<WxCpGroupMsgTaskVO> getGroupMessageTask(@RequestParam("msgId") String msgId, @RequestParam("limit") Integer limit, @RequestParam("cursor") String cursor);

    /**
     * 调用企业微信获取企业群发成员执行结果
     * @param msgId 群发消息的id
     * @param staffCpUserId 发送成员userid(发送者的企业微信ID)
     * @param limit 返回的最大记录数
     * @param cursor 用于分页查询的游标
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ua/get/group/message/sendResult")
    ServerResponseEntity<WxCpGroupMsgSendResultVO> getGroupMessageSendResult(@RequestParam("msgId") String msgId, @RequestParam("staffCpUserId") String staffCpUserId, @RequestParam("limit") Integer limit, @RequestParam("cursor") String cursor);

}
