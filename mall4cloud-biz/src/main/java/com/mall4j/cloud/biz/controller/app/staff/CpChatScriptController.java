package com.mall4j.cloud.biz.controller.app.staff;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.dto.cp.CpChatScriptDTO;
import com.mall4j.cloud.biz.dto.cp.CpChatScriptPageDTO;
import com.mall4j.cloud.biz.dto.cp.EnableMatDTO;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.model.cp.CpChatScript;
import com.mall4j.cloud.biz.model.cp.CpMaterialUseRecord;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.service.cp.CpChatScriptService;
import com.mall4j.cloud.biz.service.cp.CpChatScriptUseRecordService;
import com.mall4j.cloud.biz.vo.cp.CpChatScriptpageVO;
import com.mall4j.cloud.biz.vo.cp.MaterialBrowseRecordByDayVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 导购 话术表
 *
 * @author FrozenWatermelon
 * @date 2023-10-26 15:58:03
 */
@RestController("staffCpChatScriptController")
@RequestMapping("/s/cp_chat_script")
@Api(tags = "导购 话术表")
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
		PageVO<CpChatScriptpageVO> cpChatScriptPage = cpChatScriptService.appPage(pageDTO,request);
		return ServerResponseEntity.success(cpChatScriptPage);
	}

	@GetMapping
    @ApiOperation(value = "获取话术表", notes = "根据id获取话术表")
    public ServerResponseEntity<CpChatScriptpageVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpChatScriptService.getDetailById(id));
    }

    @GetMapping("/use")
    @ApiOperation(value = "导购复制使用话术时调用此接口增加使用次数", notes = "导购复制使用话术时调用此接口增加使用次数")
    public ServerResponseEntity<Void> type(@RequestParam Long id) {
        CpChatScript script = cpChatScriptService.getById(id);
        Assert.isNull(script,"话术不存在，请检查数据后再试。");
        script.setUpdateTime(new Date());
        Long staffId =  AuthUserContext.get().getUserId();
        cpChatScriptUseRecordService.use(id,staffId);
        cpChatScriptService.updateUseNum(script);
        return ServerResponseEntity.success();
    }


//    @PostMapping
//    @ApiOperation(value = "保存话术表", notes = "保存话术表")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody CpChatScriptDTO cpChatScriptDTO) {
//        CpChatScript cpChatScript = mapperFacade.map(cpChatScriptDTO, CpChatScript.class);
//        cpChatScript.setCreateBy(AuthUserContext.get().getUserId());
//        cpChatScript.setCreateName(AuthUserContext.get().getUsername());
//        cpChatScript.setCreateTime(new Date());
//        cpChatScriptService.save(cpChatScript);
//        return ServerResponseEntity.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新话术表", notes = "更新话术表")
//    public ServerResponseEntity<Void> update(@Valid @RequestBody CpChatScriptDTO cpChatScriptDTO) {
//        CpChatScript cpChatScript = mapperFacade.map(cpChatScriptDTO, CpChatScript.class);
//        cpChatScript.setUpdateTime(new Date());
//        cpChatScriptService.update(cpChatScript);
//        return ServerResponseEntity.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "逻辑删除话术表", notes = "逻辑删除话术表")
//    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
//        cpChatScriptService.logicDeleteById(id);
//        return ServerResponseEntity.success();
//    }
//
//    @PostMapping("/enable")
//    @ApiOperation(value = "启动/禁用话术", notes = "根据话术表id禁用话术表")
//    public ServerResponseEntity disableMat( @Valid @RequestBody EnableMatDTO request) {
//        CpChatScript cpChatScript = cpChatScriptService.getById(request.getId());
//        if(cpChatScript==null){
//            Assert.faild("id有误或数据已经删除，请检查后再操作");
//        }
//        cpChatScript.setStatus(request.getStatus());
//        cpChatScriptService.update(cpChatScript);
//        return ServerResponseEntity.success();
//    }
//
//
//    @GetMapping("/usePage")
//    @ApiOperation(value = "分页获取素材使用数据列表", notes = "分页获取素材使用数据列表")
//    public ServerResponseEntity<PageVO<CpMaterialUseRecord>> usePage(@Valid PageDTO pageDTO, MaterialUseRecordPageDTO request) {
//        PageVO<CpMaterialUseRecord> materialPage = cpChatScriptUseRecordService.usePage(pageDTO,request);
//        return ServerResponseEntity.success(materialPage);
//    }
//
//    @GetMapping("useStatistics")
//    @ApiOperation(value = "素材使用报表记录", notes = "素材使用报表记录")
//    public ServerResponseEntity<List<MaterialBrowseRecordByDayVO>> useStatistics(@Valid MaterialUseRecordPageDTO request) {
//        if(StrUtil.isEmpty(request.getCreateTimeStart())){
//            Assert.faild("开始时间不能为空。");
//        }
//        if(StrUtil.isEmpty(request.getCreateTimeEnd())){
//            Assert.faild("结束时间不能为空。");
//        }
//        List<MaterialBrowseRecordByDayVO> lists = cpChatScriptUseRecordService.useStatistics(request);
//        return ServerResponseEntity.success(lists);
//    }


}
