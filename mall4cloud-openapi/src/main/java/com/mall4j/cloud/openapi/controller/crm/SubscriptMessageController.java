package com.mall4j.cloud.openapi.controller.crm;

import com.mall4j.cloud.api.openapi.skq_crm.dto.CrmUpdateUserDto;
import com.mall4j.cloud.api.openapi.skq_crm.dto.SubscriptMessageRequest;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import com.mall4j.cloud.openapi.service.crm.ICrmMemberService;
import com.mall4j.cloud.openapi.service.crm.SubscriptMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "CRM订阅消息相关接口")
public class SubscriptMessageController extends BaseController {

    @Autowired
    SubscriptMessageService subscriptMessageService;

    @ResponseBody
    @PostMapping("/subscript/message/record")
    @ApiOperation(value = "发送订阅消息", notes = "crm端发送订阅消息推送给小程序")
    public CrmResponse record(@RequestBody List<SubscriptMessageRequest> requests) {
        CrmResponse<Void> verify = verify();
        return verify.isFail() ? verify : subscriptMessageService.record(requests);
    }
}
