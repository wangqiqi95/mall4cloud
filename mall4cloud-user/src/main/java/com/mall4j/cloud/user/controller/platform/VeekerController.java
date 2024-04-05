package com.mall4j.cloud.user.controller.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageSendVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.user.dto.VeekerAuditDTO;
import com.mall4j.cloud.user.dto.VeekerQueryDTO;
import com.mall4j.cloud.user.dto.VeekerStatusUpdateDTO;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.VeekerService;
import com.mall4j.cloud.user.vo.VeekerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微客管理
 * @author gww
 */
@Slf4j
@RestController("veekerController")
@RequestMapping("/p/veeker")
@Api(tags = "微客管理")
public class VeekerController {

    @Autowired
    private VeekerService veekerService;
    @Autowired
    private UserService userService;
    @Autowired
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    private OnsMQTemplate sendMaSubcriptMessageTemplate;


    @GetMapping("/page")
    @ApiOperation(value = "列表", notes = "微客列表")
    public ServerResponseEntity<PageVO<VeekerVO>> page(@Valid PageDTO pageDTO, VeekerQueryDTO veekerQueryDTO) {
        return ServerResponseEntity.success(veekerService.page(pageDTO, veekerQueryDTO));
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "批量更新微客状态", notes = "批量更新微客状态")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody VeekerStatusUpdateDTO veekerStatusUpdateDTO) {
        veekerService.batchUpdateVeekerStatusByUserIds(veekerStatusUpdateDTO);
        // 清除用户缓存
        removeUserCache(veekerStatusUpdateDTO.getIdList());
        return ServerResponseEntity.success();
    }

    @PutMapping("/audit")
    @ApiOperation(value = "批量更新微客审核状态", notes = "批量更新微客审核状态")
    public ServerResponseEntity<Void> audit(@Valid @RequestBody VeekerAuditDTO veekerAuditDTO) {
        veekerService.batchUpdateVeekerAuditStatusByUserIds(veekerAuditDTO);
        // 清除用户缓存
        removeUserCache(veekerAuditDTO.getIdList());
        try {
            // 微客审核完发送订阅消息通知
            List<String> businessIds = veekerAuditDTO.getIdList().stream().map(String :: valueOf).collect(Collectors.toList());
            ServerResponseEntity<WeixinMaSubscriptTmessageVO> subscriptResp = wxMaApiFeignClient.getSubscriptTmessage(MaSendTypeEnum.WEI_KE_CHECK.getValue(),
                    businessIds);
            log.info("subscriptResp {}", JSONObject.toJSONString(subscriptResp.getData()));
            if (subscriptResp.isSuccess()) {
                WeixinMaSubscriptTmessageVO weixinMaSubscriptTmessageVO = subscriptResp.getData();
                List<WeixinMaSubscriptUserRecordVO> userRecords = weixinMaSubscriptTmessageVO.getUserRecords();

                if (!CollectionUtils.isEmpty(userRecords)) {

                    List<WeixinMaSubscriptTmessageSendVO> notifyList = userRecords.stream().map(u -> {
                        /**
                         *  1: 要跳转的 地址 subscriptResp 如果是详情页需要拼接参数
                         *  2: 消息模板内容替换
                         */
                        String page = veekerAuditDTO.getStatus() == 1 ? subscriptResp.getData().getPage() : "";
                        UserApiVO userApiVO =userService.getByUserId(u.getUserId());
                        /**
                         * 值替换
                         * 1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
                         * 可以替换的模版为固定的， 根据对应类型在参考 mall4cloud.weixin_ma_subscript_tmessage_optional_value
                         * 当前微客 场景下 提交人{submitter}、提交时间{submitTime}、  审核结果（通过/不通过）{auditResult}
                         * 构建参数map.
                         */
                        Map<String,String> paramMap = new HashMap();
                        paramMap.put("{submitter}",userApiVO.getNickName());
                        paramMap.put("{submitTime}", DateUtil.now());
                        paramMap.put("{auditResult}",veekerAuditDTO.getStatus()==1?"通过":"拒绝");

                        //构建msgdata参数
                        List<WeixinMaSubscriptTmessageSendVO.MsgData> msgDataList = weixinMaSubscriptTmessageVO.getTmessageValues().stream().map(t -> {
                            WeixinMaSubscriptTmessageSendVO.MsgData msgData = new WeixinMaSubscriptTmessageSendVO.MsgData();
                            msgData.setName(t.getTemplateValueName());
                            msgData.setValue(StrUtil.isEmpty(paramMap.get(t.getTemplateValue()))?t.getTemplateValue():paramMap.get(t.getTemplateValue()));
                            return msgData;
                        }).collect(Collectors.toList());

                        WeixinMaSubscriptTmessageSendVO sendVO = new WeixinMaSubscriptTmessageSendVO();
                        sendVO.setUserSubscriptRecordId(u.getId());
                        sendVO.setData(msgDataList);
                        sendVO.setPage(page);
                        return sendVO;
                    }).collect(Collectors.toList());
                    sendMaSubcriptMessageTemplate.syncSend(notifyList);
                }
            }
        } catch (Exception e) {
            log.error("微客审核订阅通知发送异常",e);
        }
        return ServerResponseEntity.success();
    }


    private String replaceMsgValue(){
        return "";
    }

    @PutMapping("/unbindStaff")
    @ApiOperation(value = "解绑", notes = "解绑")
    public ServerResponseEntity<Void> unbindStaff(@RequestParam Long userId) {
        veekerService.unbindStaff(userId);
        // 清除用户缓存
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        removeUserCache(userIdList);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "导出", notes = "导出")
    @GetMapping("/sold_excel")
    public ServerResponseEntity<Void> export(HttpServletResponse response, VeekerQueryDTO veekerQueryDTO) {
        veekerService.export(response, veekerQueryDTO);
        return ServerResponseEntity.success();
    }


    private void removeUserCache(List<Long> userIdList) {
        if (!CollectionUtils.isEmpty(userIdList)) {
            userIdList.forEach(userId -> {
                userService.removeUserCacheByUserId(userId);
            });
        }
    }
}
