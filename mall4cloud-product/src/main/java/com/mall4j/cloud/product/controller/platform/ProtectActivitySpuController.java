package com.mall4j.cloud.product.controller.platform;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.product.dto.ProtectActivitySpuDTO;
import com.mall4j.cloud.product.dto.ProtectActivitySpuPageDTO;
import com.mall4j.cloud.product.dto.UpdateProtectActivitySpuStatusDTO;
import com.mall4j.cloud.product.event.SoldSpuEvent;
import com.mall4j.cloud.product.model.ProtectActivitySpu;
import com.mall4j.cloud.product.service.ProtectActivitySpuService;
import com.mall4j.cloud.product.vo.ProtectActivitySpuVO;
import com.mall4j.cloud.product.vo.SpuExcelImportDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
@Slf4j
@RestController("platformProtectActivitySpuController")
@RequestMapping("/p/protect/spu")
@Api(tags = "platform-电商保护价")
public class ProtectActivitySpuController {

    @Autowired
    private ProtectActivitySpuService protectActivitySpuService;

    @Autowired
	private MapperFacade mapperFacade;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

	@PostMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<PageVO<ProtectActivitySpuVO>> page(@Valid PageDTO pageDTO,@RequestBody ProtectActivitySpuPageDTO spuPageDTO) {
		PageVO<ProtectActivitySpuVO> protectActivitySpuPage = protectActivitySpuService.page(pageDTO,spuPageDTO);
		return ServerResponseEntity.success(protectActivitySpuPage);
	}

	@GetMapping
    @ApiOperation(value = "获取", notes = "根据id获取")
    public ServerResponseEntity<ProtectActivitySpuVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(protectActivitySpuService.getDetailById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ProtectActivitySpuDTO protectActivitySpuDTO) {
        if(CollectionUtil.isEmpty(protectActivitySpuDTO.getSpuIds())){
            throw new LuckException("商品不能为空");
        }
        protectActivitySpuService.saveTo(protectActivitySpuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ProtectActivitySpuDTO protectActivitySpuDTO) {
        if(protectActivitySpuService.getById(protectActivitySpuDTO.getId())==null){
            throw new LuckException("数据不存在");
        }
        if(CollectionUtil.isEmpty(protectActivitySpuDTO.getSpuIds())){
            throw new LuckException("商品不能为空");
        }
        protectActivitySpuService.updateTo(protectActivitySpuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    public ServerResponseEntity<Void> updateStatus(@Valid @RequestBody UpdateProtectActivitySpuStatusDTO protectActivitySpuDTO) {
        protectActivitySpuService.updateStatus(protectActivitySpuDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateExStatus")
    @ApiOperation(value = "更新执行状态(审核)", notes = "更新执行状态(审核)")
    public ServerResponseEntity<Void> updateExStatus(@Valid @RequestBody UpdateProtectActivitySpuStatusDTO protectActivitySpuDTO) {
        protectActivitySpuService.updateExStatus(protectActivitySpuDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "删除", notes = "根据id删除")
    public ServerResponseEntity<Void> deleteByIds(@RequestBody List<Long> ids) {
        protectActivitySpuService.deleteByIds(ids);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "批量导入")
    @PostMapping("/importExcel")
    public ServerResponseEntity<String> importExcel(@RequestParam("multipartFile") MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        File file = FileUtil.transferFile(multipartFile);
        return protectActivitySpuService.importSpus(file);
    }

    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出数据", notes = "导出数据")
    public ServerResponseEntity<String> exportExcel(@RequestBody ProtectActivitySpuPageDTO spuPageDTO) {

        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(SoldSpuExcelVO.EXCEL_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }
            protectActivitySpuService.exportSpus(spuPageDTO,downLoadHisId);
            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.info("导出商品信息错误: {} {}",e.getMessage(),e);
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

}
