package com.mall4j.cloud.biz.controller.wx.cp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.*;
import com.mall4j.cloud.biz.wx.cp.constant.OriginEumn;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: hwy
 * @Description:
 * @Date: 2022-01-24 20:52
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformMaterialController")
@RequestMapping("/p/cp/meterial")
@Api(tags = "素材管理接口")
public class MaterialController {
    private final MaterialService materialService;
    private final MaterialStoreService storeService;
    private final MaterialMsgService msgService;
    private final MapperFacade mapperFacade;
    private final CpMaterialBrowseRecordService materialBrowseRecordService;
    private final CpMaterialUseRecordService cpMaterialUseRecordService;
    private final CpMaterialLableService cpMaterialLableService;
    private final CpMaterialMsgImgService cpMaterialMsgImgService;

    @PostMapping("/uploadImage")
    @ApiOperation(value = "上传图片到企业微信素材库", notes = "上传图片到企业微信素材库")
    public ServerResponseEntity<String> uploadImage(@Valid @RequestBody UploadImageDTO request) throws IOException, WxErrorException {
        File tempFile = ZipUtils.creatTempImageFile("img_",request.getImagePath());
        String path = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().uploadImg(tempFile);
        tempFile.delete();
        return ServerResponseEntity.success(path);
    }

    @PostMapping("/uploadTempImage")
    @ApiOperation(value = "上传图片到企业微信临时素材库", notes = "上传图片到企业微信临时素材库")
    public ServerResponseEntity<WxMediaUploadResult> uploadTempImage( @Valid @RequestBody UploadImageDTO request) throws IOException, WxErrorException {
        String type  = request.getType();
        if(StringUtils.isEmpty(type)){
            type = "image";
        }
        try {
            File tempFile = null;
            WxMediaUploadResult tempPath=null;
            if(type.equals("file")){
                if (StringUtils.isEmpty(request.getFileName())){
                    throw new LuckException("文件名称不能为空");
                }
                tempFile = ZipUtils.creatTempFile(request.getFileName(),request.getImagePath(),false);
                tempPath = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().upload(type,tempFile);
            }else{
                tempFile = ZipUtils.creatTempImageFile("tmpImg_",request.getImagePath());
                tempPath = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getMediaService().upload(type,tempFile);
            }
            tempFile.delete();
            return ServerResponseEntity.success(tempPath);
        }catch (Exception e){
            log.info("上传图片到企业微信临时素材库 失败 {} {}",e,e.getMessage());
            throw new LuckException(e.getMessage());
        }
    }


    @GetMapping("/page")
    @ApiOperation(value = "获取素材表列表", notes = "分页获取素材表列表")
    public ServerResponseEntity<PageVO<MaterialVO>> page(@Valid PageDTO pageDTO, MaterialPageDTO request) {
        PageVO<MaterialVO> materialPage = materialService.page(pageDTO,request);
        return ServerResponseEntity.success(materialPage);
    }

    @GetMapping("/getByIds")
    @ApiOperation(value = "根据id集合获取素材表列表", notes = "根据id集合获取素材表列表")
    public ServerResponseEntity<List<Material>> getByIds(MaterialPageDTO request) {
        return ServerResponseEntity.success(materialService.getByIds(request));
    }

    @GetMapping
    @ApiOperation(value = "获取素材表", notes = "根据id获取素材表")
    public ServerResponseEntity<MaterialDetailVO> getById(@RequestParam Long id) {
        Material material = materialService.getById(id);
        if(material==null){
            Assert.faild("当前id不存在或记录已删除。");
        }
        List<MaterialStore> stores =  storeService.listByMatId(material.getId());
        List<MaterialMsg>msgs = msgService.listByMatId(material.getId(),OriginEumn.MAT);
        List<CpMaterialLable> lables = cpMaterialLableService.listByMatId(material.getId());
        return ServerResponseEntity.success(new MaterialDetailVO(material,msgs,stores,lables));
    }

    @PostMapping
    @ApiOperation(value = "保存素材表", notes = "保存素材表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody MaterialDTO materialDTO) {
        Material material = mapperFacade.map(materialDTO, Material.class);
        material.setCreateBy(AuthUserContext.get().getUserId());
        material.setCreateName(AuthUserContext.get().getUsername());
        material.setCreateTime(new Date());
        material.setStatus(StatusType.YX.getCode());
        material.setFlag(StatusType.WX.getCode());
        material.setUpdateTime(material.getCreateTime());
        material.setContent(materialDTO.getMsgList());
        material.setValidityCreateTime(materialDTO.getValidityCreateTime());
        material.setValidityEndTime(materialDTO.getValidityEndTime());
        material.setScriptContent(materialDTO.getScriptContent());
        log.info("保存素材表参数为：{}", material);
        materialService.save(material,materialDTO.getMsgList(),materialDTO.getStoreIds(),materialDTO.getLableList());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新素材表", notes = "更新素材表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody MaterialDTO materialDTO) {
        Material material = mapperFacade.map(materialDTO, Material.class);
        // material.setUpdateTime(material.getCreateTime());
        material.setUpdateTime(new Date());
        material.setContent(materialDTO.getMsgList());
        materialService.save(material,materialDTO.getMsgList(),materialDTO.getStoreIds(),materialDTO.getLableList());
        return ServerResponseEntity.success();
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启动/禁用素材", notes = "根据素材表id禁用素材表")
    public ServerResponseEntity disableMat( @Valid @RequestBody EnableMatDTO request) {
        Date now = new Date();
        Material material = materialService.getById(request.getId());
//        if(now.compareTo(material.getValidityCreateTime()) > 0 && now.compareTo(material.getValidityEndTime()) < 0){
            material.setStatus(request.getStatus());
            materialService.update(material);
            return ServerResponseEntity.success();
//        }
    }

    @PostMapping("/remove")
    @ApiOperation(value = "删除素材(逻辑删除)", notes = "删除素材(逻辑删除)")
    public ServerResponseEntity remove( @Valid @RequestBody EnableMatDTO request) {
        Material material = materialService.getById(request.getId());
        material.setFlag(1);
        materialService.update(material);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteByIds")
    @ApiOperation(value = "批量删除素材(逻辑删除)", notes = "批量删除素材(逻辑删除)")
    public ServerResponseEntity deleteByIds(@RequestBody List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            Assert.faild("需要删除数据不能为空");
        }
        materialService.deleteByIds(ids);
        return ServerResponseEntity.success();
    }

    @PostMapping("/changeMenu")
    @ApiOperation(value = "修改素材分类", notes = "修改素材分类")
    public ServerResponseEntity changeMenu( @Valid @RequestBody MaterialChangeMenuDTO request) {
        if(CollectionUtil.isEmpty(request.getMatId())){
            Assert.faild("需要修改的素材id不允许为空");
        }
        materialService.changeMenu(request);
        return ServerResponseEntity.success();
    }

    @GetMapping("browseStatistics")
    @ApiOperation(value = "素材浏览统计记录", notes = "素材浏览统计记录")
    public ServerResponseEntity<MaterialBrowseStatisticsVO> materialBrowseStatistics(@Valid MaterialBrowsePageDTO request) {
        if(StrUtil.isEmpty(request.getCreateTimeStart())){
            Assert.faild("开始时间不能为空。");
        }
        if(StrUtil.isEmpty(request.getCreateTimeEnd())){
            Assert.faild("结束时间不能为空。");
        }
        MaterialBrowseStatisticsVO statisticsVO = materialBrowseRecordService.materialBrowseStatistics(request);
        return ServerResponseEntity.success(statisticsVO);
    }

    @GetMapping("/browsePage")
    @ApiOperation(value = "分页获取素材浏览列表", notes = "分页获取素材浏览列表")
    public ServerResponseEntity<PageVO<CpMaterialBrowseRecordVO>> browsePage(@Valid PageDTO pageDTO, MaterialBrowsePageDTO request) {
        PageVO<CpMaterialBrowseRecordVO> materialPage = materialService.browsePage(pageDTO,request);
        return ServerResponseEntity.success(materialPage);
    }

    @GetMapping("/browse/soldBrowse")
    @ApiOperation(value = "分页获取素材浏览列表-导出", notes = "分页获取素材浏览列表-导出")
    public void soldBrowse(MaterialBrowsePageDTO request, HttpServletResponse response) {
        List<CpMaterialBrowseRecordVO> list=materialService.soldBrowsePage(request);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="素材浏览数据-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<CpMaterialBrowseSoldVO> dataList= mapperFacade.mapAsList(list,CpMaterialBrowseSoldVO.class);
            ExcelUtil.exportExcel(CpMaterialBrowseSoldVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/usePage")
    @ApiOperation(value = "分页获取素材使用数据列表", notes = "分页获取素材使用数据列表")
    public ServerResponseEntity<PageVO<CpMaterialUseRecord>> usePage(@Valid PageDTO pageDTO, MaterialUseRecordPageDTO request) {
        PageVO<CpMaterialUseRecord> materialPage = cpMaterialUseRecordService.page(pageDTO,request);
        return ServerResponseEntity.success(materialPage);
    }

    @GetMapping("/record/soldUser")
    @ApiOperation(value = "分页获取素材使用数据-导出", notes = "分页获取素材使用数据-导出")
    public void soldUser(MaterialUseRecordPageDTO request, HttpServletResponse response) {
        List<CpMaterialUseRecord> list=cpMaterialUseRecordService.exportPage(request);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="素材浏览使用数据-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<CpMaterialUseRecordExportVO> dataList= mapperFacade.mapAsList(list,CpMaterialUseRecordExportVO.class);
            ExcelUtil.exportExcel(CpMaterialUseRecordExportVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/useStatistics")
    @ApiOperation(value = "素材使用报表记录", notes = "素材使用报表记录")
    public ServerResponseEntity<List<MaterialBrowseRecordByDayVO>> useStatistics(@Valid MaterialUseRecordPageDTO request) {
        if(StrUtil.isEmpty(request.getCreateTimeStart())){
            Assert.faild("开始时间不能为空。");
        }
        if(StrUtil.isEmpty(request.getCreateTimeEnd())){
            Assert.faild("结束时间不能为空。");
        }
        List<MaterialBrowseRecordByDayVO> lists = cpMaterialUseRecordService.useStatistics(request);
        return ServerResponseEntity.success(lists);
    }

    @GetMapping("/formtFileAndSaveTo")
    @ApiOperation(value = "formtFileAndSaveTo", notes = "formtFileAndSaveTo")
    public ServerResponseEntity<Void> usePage(Long matId,Long matMsgId,String path,String fileName) {
        cpMaterialMsgImgService.formtFileAndSaveTo(matId,matMsgId,path,fileName);
        return ServerResponseEntity.success();
    }

}
