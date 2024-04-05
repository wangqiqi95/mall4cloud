package com.mall4j.cloud.api.biz.feign;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.vo.CpConfigVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalUnassignList;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactBatchInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.user.WxCpDeptUserResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author hwy
 * @Date 2022/01/18 10:28
 */
@FeignClient(value = "mall4cloud-biz",contextId = "wxCpApi")
public interface WxCpApiFeignClient {

    /**
     * 获取用户详细信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/externalcontact/externalcontactDetail")
    ServerResponseEntity<JSONObject> externalcontactDetail(@RequestParam("qiWeiUserId") String qiWeiUserId, @RequestParam("staffQiWeiUserId") String staffQiWeiUserId) ;

    /**
     * 企微部门列表
     * @param id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/depart/list")
    ServerResponseEntity<List<WxCpDepart>> wxCpDepartList(@RequestParam(value = "id",required = false) Long id) ;

    /**
     * 获取成员ID列表
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/user/list_id")
    ServerResponseEntity<WxCpDeptUserResult> wxCpUserListId(@RequestParam("cursor") String cursor,@RequestParam("limit") Integer limit) ;

    /**
     * 读取成员
     * @param userId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/user/get")
    ServerResponseEntity<WxCpUser> wxCpByUserId(@RequestParam("userId") String userId) ;

    /**
     * 手机号换取userId
     * @param mobile
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/user/getByMobile")
    ServerResponseEntity<String> wxCpByMobile(@RequestParam("mobile") String mobile) ;

    /**
     * 调用通讯录接口API：access_token，
     * 写通讯录接口，只能由通讯录同步助手的access_token来调用。同时需要保证通讯录同步功能是开启的
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/connect/accessToken")
    ServerResponseEntity<String> wxCpConnectAccessToken();

    /**
     * 自建应用token
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/external/accessToken")
    ServerResponseEntity<String> wxCpExternalAccessToken();

    /**
     * wxCpAgentJsapiTicket
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/external/wxCpAgentJsapiTicket")
    ServerResponseEntity<String> wxCpAgentJsapiTicket();

    /**
     * 获取待分配的离职成员列表
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/getUnassignedList")
    ServerResponseEntity<WxCpUserExternalUnassignList> getUnassignedList(@RequestParam("cursor") String cursor, @RequestParam("pageSize") Integer pageSize) ;

    /**
     * 删除企业微信员工
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/deleteByUserId")
    ServerResponseEntity<Void> deleteByUserId(@RequestParam("userId") String userId) ;

    /**
     * 获取企微配置
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/oauth2Url")
    ServerResponseEntity<String> oauth2Url(@RequestParam("redirectUri") String redirectUri,@RequestParam(value = "scope",required = false)  String scope) ;

    /**
     * 获取公众号配置
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/mp/oauth2Url")
    ServerResponseEntity<String> Mpoauth2Url(@RequestParam("redirectUri") String redirectUri,@RequestParam(value = "scope",required = false)  String scope) ;

    /**
     * 根绝员工获取外部联系人
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/mp/externalcontactList")
    ServerResponseEntity<List<WxCpExternalContactInfo>> externalcontactList(@RequestParam("userId") String userId) ;

    /**
     * 根绝员工批量获取外部联系人
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpApi/mp/batchExternalcontactList")
    ServerResponseEntity<WxCpExternalContactBatchInfo> batchExternalcontactList(@RequestParam("userId") String userId,
                                                                                @RequestParam("cursor") String cursor,
                                                                                @RequestParam("limit") int limit) ;
}
