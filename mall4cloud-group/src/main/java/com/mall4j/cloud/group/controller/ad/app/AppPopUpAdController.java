package com.mall4j.cloud.group.controller.ad.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ApplyAdToUserDTO;
import com.mall4j.cloud.group.service.PopUpAdService;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ma/pop/up/ad")
@Api(tags = "小程序端弹窗广告相关接口")
@Slf4j
public class AppPopUpAdController {


    @Autowired
    private PopUpAdService popUpAdService;


    @GetMapping("/get/ad/apply/to/user")
    @ApiOperation("获取适用于用户的广告iD与相关页面")
    public ServerResponseEntity<Map<String, List<Long>>> getThePopUpAdTreeByUser(@RequestParam Long storeId){
         return popUpAdService.getThePopUpAdTreeByUser(storeId);
    }


    @PostMapping("/apply/ad/browse")
    @ApiOperation("获取适用于用户的广告内容")
    public ServerResponseEntity<PopUpAdContainerVO> popUpByAdIdList(@Valid @RequestBody ApplyAdToUserDTO applyAdToUserDTO){
        return popUpAdService.popUpByAdIdList(applyAdToUserDTO.getAdIdList(),
                applyAdToUserDTO.getTempUid(),
                applyAdToUserDTO.getStoreId(),
                applyAdToUserDTO.getEntrance());
    }
}
