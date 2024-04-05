package com.mall4j.cloud.biz.controller.wx.cp;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.manager.DrainageUrlManager;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.biz.dto.cp.GroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCodePageDTO;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.GroupCodeRefService;
import com.mall4j.cloud.biz.service.cp.GroupCodeService;
import com.mall4j.cloud.biz.vo.cp.GroupCodeRefVO;
import com.mall4j.cloud.biz.vo.cp.GroupCodeVO;
import com.mall4j.cloud.biz.wx.wx.util.QrCodeUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 群活码表
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformGroupCodeController")
@RequestMapping("/p/cp/group/code")
@Api(tags = "群活码表")
public class GroupCodeController {
    private final GroupCodeService groupCodeService;
    private final GroupCodeRefService refService;
    private final DomainConfig domainConfig;

    @GetMapping("/page")
	@ApiOperation(value = "获取群活码表列表", notes = "分页获取群活码表列表")
	public ServerResponseEntity<PageVO<GroupCodeVO>> page(@Valid PageDTO pageDTO, GroupCodePageDTO request) {
	    PageVO<GroupCodeVO> groupCodePage = groupCodeService.page(pageDTO,request);
		return ServerResponseEntity.success(groupCodePage);
	}

    @GetMapping("/pageAnalyzes")
    @ApiOperation(value = "群活码详情-关联群列表", notes = "群活码详情-关联群列表")
    public ServerResponseEntity<PageVO<AnalyzeGroupCodeVO>> pageAnalyzes(@Valid PageDTO pageDTO, Long codeId, String groupName) {
        PageVO<AnalyzeGroupCodeVO> analyzeGroupCodeVOPageVO = refService.pageAnalyzes(pageDTO,codeId,groupName);
        return ServerResponseEntity.success(analyzeGroupCodeVOPageVO);
    }

	@GetMapping
    @ApiOperation(value = "获取群活码表", notes = "根据id获取群活码表")
    public ServerResponseEntity<GroupCodeVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(groupCodeService.getDetailById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存群活码表", notes = "保存群活码表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody GroupCodeDTO request) {
        groupCodeService.createOrUpdateGroupCode(request);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新群活码表", notes = "更新群活码表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody GroupCodeDTO request) {
        groupCodeService.createOrUpdateGroupCode(request);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除群活码表", notes = "根据群活码表id删除群活码表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        groupCodeService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/batchDelete")
    @ApiOperation(value = "批量删除群活码表", notes = "批量删除群活码表")
    public ServerResponseEntity<Void> batchDelete(@RequestParam Long[] ids) {
        for(Long id :ids) {
            groupCodeService.deleteById(id);
        }
        return ServerResponseEntity.success();
    }


    @PostMapping("/download/batchQrcode")
    @ApiOperation(value = "批量下载活码", notes = "批量下载活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestBody GroupCodePageDTO dto,  HttpServletResponse response) throws IOException {
        List<File> fileList = new ArrayList<>();
        List<CpGroupCode> groupCodes=groupCodeService.list(new LambdaQueryWrapper<CpGroupCode>().in(CpGroupCode::getId,dto.getIds()));
        if(CollUtil.isEmpty(groupCodes)){
            throw new LuckException("未获取到活码信息");
        }
        for (CpGroupCode groupCode : groupCodes) {
            String logo= domainConfig.getDomain()+groupCode.getStyle();
            String downloadurl=groupCode.getDrainageUrl();
            if(groupCode.getGroupType()==2){
                List<CpGroupCodeRef>  cpGroupCodeRefs=refService.getListByCodeId(Arrays.asList(groupCode.getId()), CodeChannelEnum.GROUP_CODE.getValue());
                if(CollUtil.isEmpty(cpGroupCodeRefs)){
                    log.info("未获取到下载信息:{}", JSON.toJSONString(groupCode));
                    continue;
                }
                downloadurl=cpGroupCodeRefs.get(0).getQrCode();
            }
            File file = ZipUtils.creatTempImageLogoFile(groupCode.getName(),downloadurl,logo);
            fileList.add(file);
        }
        ZipUtils.downloadZip("groupCode",fileList,response);
        return ServerResponseEntity.success();
    }


    @GetMapping("/download/qrcode")
    @ApiOperation(value = "下载活码", notes = "下载活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestParam Long id,  HttpServletResponse response) throws IOException {
        CpGroupCode groupCode =  groupCodeService.getById(id);
        if(Objects.isNull(groupCode)){
            throw new LuckException("未获取到活码信息");
        }
        String logo= domainConfig.getDomain()+groupCode.getStyle();
        String downloadurl=groupCode.getDrainageUrl();
        if(groupCode.getGroupType()==2){
            List<CpGroupCodeRef>  cpGroupCodeRefs=refService.getListByCodeId(Arrays.asList(id), CodeChannelEnum.GROUP_CODE.getValue());
            if(CollUtil.isEmpty(cpGroupCodeRefs)){
                throw new LuckException("未获取到下载信息");
            }
             downloadurl=cpGroupCodeRefs.get(0).getQrCode();
        }
        ZipUtils.downLoadImageLogo(groupCode.getName(),downloadurl,logo,response);
        return ServerResponseEntity.success();
    }

}
