package com.mall4j.cloud.group.controller.ad.admin;

import com.mall4j.cloud.api.platform.vo.SelectedStoreVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AddPopUpAdDTO;
import com.mall4j.cloud.group.dto.QueryStorePageByAdPageDTO;
import com.mall4j.cloud.group.model.PopUpAdStoreRelation;
import com.mall4j.cloud.group.service.PopUpAdStoreRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/p/pop/up/ad/store/relation")
@Api(tags = "管理后台弹窗广告可用门店相关接口")
@Slf4j
public class PlatformPopUpAdStoreRelationController {

    @Autowired
    private PopUpAdStoreRelationService popUpAdStoreRelationService;

    @GetMapping("/get/by/ad")
    @ApiOperation("管理后台弹窗广告门店数据分页查询")
    public ServerResponseEntity<PageVO<SelectedStoreVO>> add(QueryStorePageByAdPageDTO pageDTO){
        return popUpAdStoreRelationService.getTheStorePageByAdId(pageDTO);
    }

}
