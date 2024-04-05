package com.mall4j.cloud.openapi.controller.crm;

import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponseEnum;
import com.mall4j.cloud.api.user.dto.CrmUserSyncDTO;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mall4j.cloud.api.openapi.skq_crm.dto.CrmUpdateUserDto;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import com.mall4j.cloud.openapi.service.crm.ICrmMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 会员信息controller
 * @date 2021/12/28 14:20：16
 */
@RestController
@Api(tags = "crm会员信息相关接口")
public class MemberController extends BaseController {

    @Autowired
    ICrmMemberService crmMemberService;

//    @ResponseBody
//    @PostMapping("/member/updateSync")
//    @ApiOperation(value = "会员信息更新（crm端）", notes = "crm端会员基础信息（包含等级）更新时推送给小程序")
//    public CrmResponse updateSync(@RequestBody CrmUpdateUserDto crmUpdateUserDto) {
//        CrmResponse<Void> verify = verify();
//        return verify.isFail() ? verify : crmMemberService.updateSync(crmUpdateUserDto);
//    }


//    @ResponseBody
//    @PostMapping("/member/userSync")
//    @ApiOperation(value = "crm同步会员信息", notes = "crm端会员基础信息同步给小程序")
////    public CrmResponse userSync(@Valid @RequestBody CrmUserSyncDTO crmUserSyncDTO, BindingResult result) {
//    public CrmResponse userSync(@RequestBody CrmUserSyncDTO crmUserSyncDTO) {
//        /*if (result.getErrorCount()>0){
//            List<String> paramErrorMassage = result.getFieldErrors().stream().map(e -> {
//                return e.getDefaultMessage();
//            }).collect(Collectors.toList());
//            return CrmResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), paramErrorMassage);
//        }*/
//        CrmResponse<Void> verify = verify();
//        return verify.isFail() ? verify : crmMemberService.userSync(crmUserSyncDTO);
//    }

}
