package com.mall4j.cloud.biz.controller.app.staff;

import com.mall4j.cloud.biz.dto.cp.wx.MeterialBrowseDTO;
import com.mall4j.cloud.biz.manager.MaterialManager;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.CpMaterialMsgImgVO;
import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import com.mall4j.cloud.biz.vo.cp.MaterialDetailVO;
import com.mall4j.cloud.biz.vo.cp.MaterialTypeVO;
import com.mall4j.cloud.biz.vo.cp.MiniMaterialVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 导购小程序查询素材接口
 * @Author: hwy
 * @Description:
 * @Date: 2022-01-24 20:52
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController("staffMaterialController")
@RequestMapping("/s/meterial")
@Api(tags = "导购小程序查询素材接口")
public class MaterialController {
    private final MaterialService materialService;
    private final MaterialTypeService materialTypeService;
    private final MaterialMsgService msgService;
    private final MaterialManager materialManager;
    private final CpMaterialMsgImgService materialMsgImgService;

    @Autowired
    CpMaterialUseRecordService materialUseRecordService;

    @GetMapping("/page")
    @ApiOperation(value = "素材列表", notes = "素材列表")
    public ServerResponseEntity<PageVO<MiniMaterialVO>> page( @Valid PageDTO pageDTO, MaterialPageDTO request)  {
//        request.setOrgIds(AuthUserContext.get().getOrgIds());
            PageVO<MiniMaterialVO> materialPage = materialService.miniPage(pageDTO,request);
        return ServerResponseEntity.success(materialPage);
    }

    @GetMapping("/ua/detail")
    @ApiOperation(value = "素材详情", notes = "素材详情")
    public ServerResponseEntity<MaterialDetailVO> detail(@RequestParam Long id) {
        Material material = materialService.getById(id);
        List<MaterialMsg> msgs = msgService.listByMatId(material.getId(),OriginEumn.MAT);
        return ServerResponseEntity.success(new MaterialDetailVO(material,msgs,null));
    }

    @GetMapping("/type")
    @ApiOperation(value = "素材类型接口", notes = "素材类型接口")
    public ServerResponseEntity<List<MaterialTypeVO>> type() {
        return ServerResponseEntity.success(materialTypeService.listParentContainChildren());
    }

    @GetMapping("/use")
    @ApiOperation(value = "导购复制使用素材时调用此接口增加使用次数", notes = "导购复制使用素材时调用此接口增加使用次数")
    public ServerResponseEntity<Void> type(@RequestParam Long id) {
        Material material = materialService.getById(id);
        Assert.isNull(material,"素材不存在，请检查数据后再试。");
        Long staffId =  AuthUserContext.get().getUserId();
        materialUseRecordService.use(id,staffId);
        return ServerResponseEntity.success();
    }


    @PostMapping("/ua/browse")
    @ApiOperation(value = "浏览素材", notes = "浏览素材")
    public ServerResponseEntity<Void> browse(@RequestBody MeterialBrowseDTO request) {
        materialManager.browse(request);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/file/preview")
    @ApiOperation(value = "素材文件图片预览", notes = "素材文件图片预览")
    public ServerResponseEntity<List<CpMaterialMsgImgVO>> filePreview(@RequestBody CpMaterialMsgImgSelectDTO dto) {
        return ServerResponseEntity.success(materialMsgImgService.filePreview(dto));
    }

}
