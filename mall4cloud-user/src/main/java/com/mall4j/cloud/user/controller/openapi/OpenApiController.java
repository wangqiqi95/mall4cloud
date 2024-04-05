package com.mall4j.cloud.user.controller.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.user.dto.openapi.SendTaskDTO;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.vo.openapi.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;




@Slf4j
@RestController
@RequestMapping("/openapi")
@Api(tags="对数云外部接口")
public class OpenApiController extends BaseController{

    @Autowired
    private GroupPushTaskService groupPushTaskService;

    /**
     * 鉴权方式 请求头添加三个参数
     * timestamp       时间戳
     * secretKey
     * sign 生成方式如下：
     *          String str = timestamp + "&" + secretKey +"&"+"{\"taskId\":1,\"userIds\":[\"123\"]}";
     * 			String urlEncode = java.net.URLEncoder.encode(str.toLowerCase(), "utf-8").toLowerCase();
     * 			String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(urlEncode);
     *
     * 	DefaultAuthConfigAdapter 已配置该接口鉴权过滤无需token
     */
    @ApiOperation(value = "发送任务", notes = "发送任务")
    @PostMapping("/std/service/pushTask")
    public ApiResponse<Void> sendTask(@Valid @RequestBody SendTaskDTO request) {
        /**
         * scrm接口文档尽快于2023-11-22给到
         * 1.CDP仅接入任务主表，任务子表作为SCRM分阶段推送的记录CDP不做接入；
         * 2.任务主表中，CDP仅接入task_type=1（即CDP任务类型）的任务，需SCRM在推送接口中进行过滤；
         * 3.CDP活动推送时间即为任务开始执行时间，请SCRM内部确认CDP任务类型是否支持设置子任务；
         * 4.请SCRM提供活动人群与任务ID关联关系接口给到CDP用以推送筛选后推送任务的人群；
         */
        log.info("对数云外部接口-发送任务，接收数据：{}", JSON.toJSONString(request));
        //校验数据
        ApiResponse verify = verify(JSONObject.toJSONString(request));
        if (!verify.successful()) {
            return ApiResponse.fail("非法请求");
        }
        ApiResponse apiResponse=groupPushTaskService.crmSendTaskNew(request);
        log.info("对数云外部接口-发送任务，结果：{}", JSON.toJSONString(apiResponse));
        return apiResponse;
//        return groupPushTaskService.crmSendTask(request);
    }




}
