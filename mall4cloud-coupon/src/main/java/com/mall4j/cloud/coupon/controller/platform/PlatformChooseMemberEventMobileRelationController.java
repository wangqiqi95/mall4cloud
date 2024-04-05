package com.mall4j.cloud.coupon.controller.platform;


import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventMobileRelationPageDTO;
import com.mall4j.cloud.coupon.dto.DeleteChooseMemberEventMobileRelationDTO;
import com.mall4j.cloud.coupon.service.ChooseMemberEventMobileRelationService;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventMobileRelationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 指定会员活动关系表 前端控制器
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@RestController
@RequestMapping("/p/choose/member/event/user/relation")
@Api(tags = {"活动会员手机号关联API"})
public class PlatformChooseMemberEventMobileRelationController {

    @Autowired
    private ChooseMemberEventMobileRelationService chooseMemberEventMobileRelationService;


    @ParameterValid
    @GetMapping("page/by/event")
    @ApiOperation("查询活动指定用户手机列表")
    public ServerResponseEntity<PageVO<ChooseMemberEventMobileRelationVO>> getPage(@Valid ChooseMemberEventMobileRelationPageDTO pageDTO,
                                                                                   BindingResult bindingResult){
        return chooseMemberEventMobileRelationService.getTheMobilePage(pageDTO);
    }



    @ParameterValid
    @PostMapping("remove/list")
    @ApiOperation("批量删除活动关联用户手机号")
    public ServerResponseEntity removeRelationByIdList(@Valid @RequestBody DeleteChooseMemberEventMobileRelationDTO deleteMobileRelationDTO,
                                                       BindingResult bindingResult) {
        return chooseMemberEventMobileRelationService.removeRelationByIdList(deleteMobileRelationDTO);
    }

    @PostMapping("export/mobile/to/download/center")
    @ApiOperation("导出会员名单到下载中心")
    public ServerResponseEntity<String> export(@RequestBody ChooseMemberEventMobileRelationPageDTO param, HttpServletResponse response){
        return chooseMemberEventMobileRelationService.export(param, response);
    }
}

