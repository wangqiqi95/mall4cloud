package com.mall4j.cloud.biz.controller.wx.cp;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.manager.CpStaffCodeTimeRefeshManager;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.biz.dto.cp.StaffCodePlusDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeIdDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodePagePlusDTO;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 渠道活码配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformPlusStaffCodeController")
@RequestMapping("/p/cp/plus/staff_code")
@Api(tags = "渠道活码配置-plus版")
@RefreshScope
public class StaffCodePlusController {
    private final StaffCodePlusService staffCodeService;
    private final CpStaffCodeTimeRefeshManager staffCodeTimeRefeshManager;
    private final DomainConfig domainConfig;

	@GetMapping("/page")
	@ApiOperation(value = "获取渠道活码表列表", notes = "分页获取渠道活码表列表")
	public ServerResponseEntity<PageVO<StaffCodePlusVO>> page(@Valid PageDTO pageDTO, StaffCodePagePlusDTO request) {
		PageVO<StaffCodePlusVO> staffCodeVOPageVO = staffCodeService.page(pageDTO,request);
		return ServerResponseEntity.success(staffCodeVOPageVO);
	}

	@GetMapping
    @ApiOperation(value = "获取渠道活码表", notes = "根据id获取渠道活码表")
    public ServerResponseEntity<StaffCodeDetailPlusVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(staffCodeService.getDetailById(id));
    }


    @PostMapping
    @ApiOperation(value = "保存渠道活码表", notes = "保存渠道活码表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody StaffCodePlusDTO request) {
        staffCodeService.save(request);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新渠道活码表", notes = "更新渠道活码表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody StaffCodePlusDTO request) {
        staffCodeService.update(request);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除渠道活码表", notes = "根据渠道活码表id删除渠道活码表")
    public ServerResponseEntity<Void> delete(@RequestParam Long[] ids) {
	    for(Long id :ids) {
            staffCodeService.deleteById(id);
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/download/batchQrcode")
    @ApiOperation(value = "批量下载渠道活码", notes = "批量下载渠道活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestBody StaffCodeIdDTO staffCodeIdDTO, HttpServletResponse response) throws IOException {
        if (staffCodeIdDTO == null || staffCodeIdDTO .getIds() == null || staffCodeIdDTO.getIds().length == 0){
            return ServerResponseEntity.success();
        }
        List<CpStaffCodePlus> staffCodes=staffCodeService.list(new LambdaQueryWrapper<CpStaffCodePlus>().in(CpStaffCodePlus::getId,staffCodeIdDTO.getIds()));
        if(CollUtil.isEmpty(staffCodes)){
            throw new LuckException("未获取到活码信息");
        }
        List<File> fileList = new ArrayList<>();
        for (CpStaffCodePlus staffCode : staffCodes) {
            String logo=domainConfig.getDomain()+staffCode.getCodeStyle();
//            File file = ZipUtils.creatTempImageLogoFile(staffCode.getCodeName(),staffCode.getQrCode(),logo);
            File file = ZipUtils.creatTempImageFile(staffCode.getCodeName(),staffCode.getQrCode());
            fileList.add(file);
        }
        ZipUtils.downloadZip("channelCode",fileList,response);
        return ServerResponseEntity.success();
    }

    @GetMapping("/download/qrcode")
    @ApiOperation(value = "下载活码", notes = "下载活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestParam Long id,  HttpServletResponse response) throws IOException {
        CpStaffCodePlus staffCode =  staffCodeService.getById(id);
        if(Objects.isNull(staffCode)){
            throw new LuckException("未获取到活码信息");
        }
        String logo=domainConfig.getDomain()+staffCode.getCodeStyle();
//        ZipUtils.downLoadImageLogo(staffCode.getCodeName(),staffCode.getQrCode(),logo,response);
        ZipUtils.downLoadImage(staffCode.getCodeName(),staffCode.getQrCode(),response);
        return ServerResponseEntity.success();
    }

    @GetMapping("/refeshTimeStaff")
    @ApiOperation(value = "渠道活码分时段接待人员刷新", notes = "渠道活码分时段接待人员刷新")
    public ServerResponseEntity<Void> refeshTimeStaff(@RequestParam Long id) {
        staffCodeTimeRefeshManager.refeshTimeStaff();
        return ServerResponseEntity.success();
    }

}
