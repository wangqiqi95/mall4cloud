package com.mall4j.cloud.biz.controller.wx.cp;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeSelectDTO;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeStaffSelectDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpAutoGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCode;
import com.mall4j.cloud.biz.service.cp.CpDetailGroupCodeAnalyzeService;
import com.mall4j.cloud.biz.service.cp.CpAutoGroupCodeService;
import com.mall4j.cloud.biz.service.cp.CpAutoGroupCodeStaffService;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeStaffVO;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeVO;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpAutoGroupCodeSendUserVO;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自动拉群活码表
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Slf4j
@RestController("CpAutoGroupCodeController")
@RequestMapping("/p/cp/auto/group/code")
@Api(tags = "自动拉群活码表")
public class CpAutoGroupCodeController {

    @Autowired
    private CpAutoGroupCodeService cpAutoGroupCodeService;
    @Autowired
    private CpAutoGroupCodeStaffService codeStaffService;
    @Autowired
    private CpDetailGroupCodeAnalyzeService groupCodeAnalyzeService;
    @Autowired
    private DomainConfig domainConfig;


	@GetMapping("/page")
	@ApiOperation(value = "获取自动拉群活码表列表", notes = "分页获取自动拉群活码表列表")
	public ServerResponseEntity<PageVO<CpAutoGroupCodeVO>> page(@Valid PageDTO pageDTO,CpAutoGroupCodeSelectDTO dto) {
		PageVO<CpAutoGroupCodeVO> cpAutoGroupCodePage = cpAutoGroupCodeService.page(pageDTO,dto);
		return ServerResponseEntity.success(cpAutoGroupCodePage);
	}

    @GetMapping("/staffs")
    @ApiOperation(value = "查看执行人员列表", notes = "查看执行人员列表")
    public ServerResponseEntity<PageVO<CpAutoGroupCodeStaffVO>> staffPage(@Valid PageDTO pageDTO,CpAutoGroupCodeStaffSelectDTO dto) {
        PageVO<CpAutoGroupCodeStaffVO> cpAutoGroupCodePage = codeStaffService.page(pageDTO,dto);
        return ServerResponseEntity.success(cpAutoGroupCodePage);
    }

    @GetMapping("/relUsers")
    @ApiOperation(value = "发送好友列表", notes = "发送好友列表")
    public ServerResponseEntity<PageVO<CpAutoGroupCodeSendUserVO>> pageRelUser(@Valid PageDTO pageDTO, CpAutoGroupCodeAnalyzeDTO dto) {
        PageVO<CpAutoGroupCodeSendUserVO> cpAutoGroupCodePage = groupCodeAnalyzeService.pageAutoCodeRelUser(pageDTO,dto);
        return ServerResponseEntity.success(cpAutoGroupCodePage);
    }

    @GetMapping("/relGroups")
    @ApiOperation(value = "关联群聊列表", notes = "关联群聊列表")
    public ServerResponseEntity<PageVO<AnalyzeGroupCodeVO>> pageRelGroup(@Valid PageDTO pageDTO, CpAutoGroupCodeAnalyzeDTO dto) {
        PageVO<AnalyzeGroupCodeVO> cpAutoGroupCodePage = groupCodeAnalyzeService.pageAutoCodeRelGroup(pageDTO,dto);
        return ServerResponseEntity.success(cpAutoGroupCodePage);
    }

	@GetMapping
    @ApiOperation(value = "获取自动拉群活码表", notes = "根据id获取自动拉群活码表")
    public ServerResponseEntity<CpAutoGroupCodeVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpAutoGroupCodeService.getDetialById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存自动拉群活码表", notes = "保存自动拉群活码表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CpAutoGroupCodeDTO cpAutoGroupCodeDTO) {
        cpAutoGroupCodeService.createOrUpate(cpAutoGroupCodeDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新自动拉群活码表", notes = "更新自动拉群活码表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CpAutoGroupCodeDTO cpAutoGroupCodeDTO) {
        cpAutoGroupCodeService.createOrUpate(cpAutoGroupCodeDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除自动拉群活码表", notes = "根据自动拉群活码表id删除自动拉群活码表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        cpAutoGroupCodeService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/batchDelete")
    @ApiOperation(value = "批量删除自动拉群活码表", notes = "批量删除自动拉群活码表")
    public ServerResponseEntity<Void> batchDelete(@RequestParam Long[] ids) {
        for(Long id :ids) {
            cpAutoGroupCodeService.deleteById(id);
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/download/batchQrcode")
    @ApiOperation(value = "批量下载渠道活码", notes = "批量下载渠道活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestBody CpAutoGroupCodeSelectDTO staffCodeIdDTO, HttpServletResponse response) throws IOException {
        if (staffCodeIdDTO == null || staffCodeIdDTO .getIds() == null || staffCodeIdDTO.getIds().length == 0){
            return ServerResponseEntity.success();
        }
        List<CpAutoGroupCode> staffCodes=cpAutoGroupCodeService.list(new LambdaQueryWrapper<CpAutoGroupCode>().in(CpAutoGroupCode::getId,staffCodeIdDTO.getIds()));
        if(CollUtil.isEmpty(staffCodes)){
            throw new LuckException("未获取到活码信息");
        }
        List<File> fileList = new ArrayList<>();
        for (CpAutoGroupCode autoGroupCode : staffCodes) {
            String logo= domainConfig.getDomain()+autoGroupCode.getCodeStyle();
            File file = ZipUtils.creatTempImageLogoFile(autoGroupCode.getCodeName(),autoGroupCode.getQrCode(),logo);
            fileList.add(file);
        }
        ZipUtils.downloadZip("autoCode",fileList,response);
        return ServerResponseEntity.success();
    }

    @GetMapping("/download/qrcode")
    @ApiOperation(value = "下载活码", notes = "下载活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestParam Long id,  HttpServletResponse response) throws IOException {
        CpAutoGroupCode autoGroupCode =  cpAutoGroupCodeService.getById(id);
        if(Objects.isNull(autoGroupCode)){
            throw new LuckException("未获取到活码信息");
        }
        String logo= domainConfig.getDomain()+autoGroupCode.getCodeStyle();
        ZipUtils.downLoadImageLogo(autoGroupCode.getCodeName(),autoGroupCode.getQrCode(),logo,response);
        return ServerResponseEntity.success();
    }

}
