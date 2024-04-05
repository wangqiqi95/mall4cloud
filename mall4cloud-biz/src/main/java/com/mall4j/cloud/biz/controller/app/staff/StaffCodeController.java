package com.mall4j.cloud.biz.controller.app.staff;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.model.cp.CpStaffCodePlus;
import com.mall4j.cloud.biz.service.cp.StaffCodePlusService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController("staffCodeController")
@RequestMapping("/s/staff_code")
@Api(tags = "导购小程序-企业微信二维码")
public class StaffCodeController {

    private  final StaffCodePlusService staffCodeService;
    private final MapperFacade mapperFacade;

    private final StaffFeignClient staffFeignClient;

    @GetMapping("/getStaffQrCode")
    @ApiOperation(value = "获取员工企业微信的二维码", notes = "获取员工企业微信的二维码")
    public ServerResponseEntity<String> getStaffQrCode() throws WxErrorException {
        Long staffId = AuthUserContext.get().getUserId();
        List<CpStaffCodePlus> staffCodes = staffCodeService.selectByStaffId(staffId);
        if(CollectionUtils.isEmpty(staffCodes)){
            ServerResponseEntity<StaffVO> response =  staffFeignClient.getStaffById(staffId);
            if(response==null||response.getData()==null|| StringUtils.isEmpty(response.getData().getQiWeiUserId())){
                log.info("该员工尚未同步企业微信 staffId:【{}】",staffId);
//                return ServerResponseEntity.showFailMsg("该员工尚未同步企业微信！");
                return ServerResponseEntity.showFailMsg("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
            }
            try {
                WxCpUser wxCpUser =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.CP_CONNECT_AGENT_ID).getUserService().getById(response.getData().getQiWeiUserId());
                log.info("该员工获取到企微个人信息(企业微信) staffId:【{}】 活码信息【{}】",staffId, JSON.toJSON(wxCpUser));
                return ServerResponseEntity.success(wxCpUser.getQrCode());
            }catch (WxErrorException e){
                log.error("该员工未获取到企微个人信息 {}",e,e.getMessage());
                if(Objects.nonNull(e.getError())){
                    if(e.getError().getErrorCode()==40098){
                        return ServerResponseEntity.showFailMsg("你的企业微信账号未实名认证，请先完成实名认证（打开企业微信【我】界面，点击公司名称区域--点击[姓名]--查看[实名认证]栏，点击[立即认证]，按提示完成操作），之后重新登陆导购助手即可正常打开会员招募码");
                    }
                    if(e.getError().getErrorCode()==60111){
                        return ServerResponseEntity.showFailMsg("后台暂无你的个人信息，请联系你所在区域的HR或者上级营运同事，反馈需查核/新增个人信息");
                    }
                    if(e.getError().getErrorCode()==81012){
                        return ServerResponseEntity.showFailMsg("二维码获取失败，请先登录企业微信");
                    }
                }
                return ServerResponseEntity.showFailMsg("二维码获取失败，请联系你所在区域的HR或者上级营运同事，反馈需查核");
            }

        }
        //取单人活码 如果没有 则取多人活码
        log.info("该员工获取到企微活码(后台配置) staffId:【{}】 活码信息【{}】",staffId, JSON.toJSON(staffCodes.get(0)));
        return ServerResponseEntity.success(staffCodes.get(0).getQrCode());
    }

    @GetMapping("/ua/getStaffQrCode")
    @ApiOperation(value = "通过员工id获取员工企业微信的二维码", notes = "通过员工id获取员工企业微信的二维码")
    public ServerResponseEntity<String> getStaffQrCodeByStaffId(@RequestParam Long staffId) {
        List<CpStaffCodePlus> staffCodes = staffCodeService.selectByStaffId(staffId);
        if(CollectionUtils.isEmpty(staffCodes)){
            return ServerResponseEntity.showFailMsg("尚未配置企业微信二维码");
        }
        //取单人活码 如果没有 则取多人活码
        return ServerResponseEntity.success(staffCodes.get(0).getQrCode());
    }

}
