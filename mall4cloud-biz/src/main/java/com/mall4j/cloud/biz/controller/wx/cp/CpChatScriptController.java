package com.mall4j.cloud.biz.controller.wx.cp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.model.cp.CpChatScript;
import com.mall4j.cloud.biz.model.cp.CpChatScriptUseRecord;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.service.cp.CpChatScriptService;

import com.mall4j.cloud.biz.service.cp.CpChatScriptUseRecordService;
import com.mall4j.cloud.biz.vo.cp.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 话术表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@Slf4j
@RestController("appCpChatScriptController")
@RequestMapping("/p/cp_chat_script")
@Api(tags = "话术表")
public class CpChatScriptController {

    @Autowired
    private CpChatScriptService cpChatScriptService;

    @Autowired
	private MapperFacade mapperFacade;
    @Autowired
    CpChatScriptUseRecordService cpChatScriptUseRecordService;

	@GetMapping("/page")
	@ApiOperation(value = "获取话术表列表", notes = "分页获取话术表列表")
	public ServerResponseEntity<PageVO<CpChatScriptpageVO>> page(@Valid PageDTO pageDTO, CpChatScriptPageDTO request) {
        if(request.getType()==null){
            Assert.faild("话术类型不允许为空");
        }
		PageVO<CpChatScriptpageVO> cpChatScriptPage = cpChatScriptService.page(pageDTO,request);
		return ServerResponseEntity.success(cpChatScriptPage);
	}

    @GetMapping("/listByIds")
    @ApiOperation(value = "根据ids获取话术表列表", notes = "根据ids获取话术表列表")
    public ServerResponseEntity<List<CpChatScript>> listByIds( CpChatScriptPageDTO request) {
        List<CpChatScript> cpChatScriptPage = cpChatScriptService.getByIds(request);
        return ServerResponseEntity.success(cpChatScriptPage);
    }

	@GetMapping
    @ApiOperation(value = "获取话术表", notes = "根据id获取话术表")
    public ServerResponseEntity<CpChatScriptpageVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpChatScriptService.getDetailById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存话术表", notes = "保存话术表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CpChatScriptDTO cpChatScriptDTO) {
        CpChatScript cpChatScript = mapperFacade.map(cpChatScriptDTO, CpChatScript.class);
        cpChatScript.setCreateBy(AuthUserContext.get().getUserId());
        cpChatScript.setCreateName(AuthUserContext.get().getUsername());
        cpChatScript.setCreateTime(new Date());
        cpChatScriptService.save(cpChatScript);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新话术表", notes = "更新话术表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CpChatScriptDTO cpChatScriptDTO) {
        CpChatScript cpChatScript = mapperFacade.map(cpChatScriptDTO, CpChatScript.class);
        cpChatScript.setUpdateTime(new Date());
        cpChatScriptService.update(cpChatScript);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "逻辑删除话术表", notes = "逻辑删除话术表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        cpChatScriptService.logicDeleteById(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启动/禁用话术", notes = "根据话术表id禁用话术表")
    public ServerResponseEntity disableMat( @Valid @RequestBody EnableMatDTO request) {
        CpChatScript cpChatScript = cpChatScriptService.getById(request.getId());
        if(cpChatScript==null){
            Assert.faild("id有误或数据已经删除，请检查后再操作");
        }
        cpChatScript.setStatus(request.getStatus());
        cpChatScriptService.disableMat(cpChatScript);
        return ServerResponseEntity.success();
    }


    @GetMapping("/usePage")
    @ApiOperation(value = "分页获取素材使用数据列表", notes = "分页获取素材使用数据列表")
    public ServerResponseEntity<PageVO<CpChatScriptUseRecord>> usePage(@Valid PageDTO pageDTO, MaterialUseRecordPageDTO request) {
        return ServerResponseEntity.success(cpChatScriptUseRecordService.usePage(pageDTO,request));
    }

    @GetMapping("/soldUsePage")
    @ApiOperation(value = "分页获取素材使用数据列表-导出", notes = "分页获取素材使用数据列表-导出")
    public void soldBrowse(MaterialUseRecordPageDTO request, HttpServletResponse response) {
        List<CpChatScriptUseRecord> list=cpChatScriptUseRecordService.soldUsePage(request);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="UsePage-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<CpChatScriptUserSoldVO> dataList= mapperFacade.mapAsList(list,CpChatScriptUserSoldVO.class);
            ExcelUtil.exportExcel(CpChatScriptUserSoldVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("useStatistics")
    @ApiOperation(value = "素材使用报表记录", notes = "素材使用报表记录")
    public ServerResponseEntity<List<MaterialBrowseRecordByDayVO>> useStatistics(@Valid MaterialUseRecordPageDTO request) {
        if(StrUtil.isEmpty(request.getCreateTimeStart())){
            Assert.faild("开始时间不能为空。");
        }
        if(StrUtil.isEmpty(request.getCreateTimeEnd())){
            Assert.faild("结束时间不能为空。");
        }
        List<MaterialBrowseRecordByDayVO> lists = cpChatScriptUseRecordService.useStatistics(request);
        return ServerResponseEntity.success(lists);
    }


}
