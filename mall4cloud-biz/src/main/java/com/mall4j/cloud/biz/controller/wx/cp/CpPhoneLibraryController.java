package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.biz.dto.cp.CpPhoneLibraryDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneLibrary;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import com.mall4j.cloud.biz.service.cp.CpPhoneLibraryService;
import com.mall4j.cloud.biz.task.CpPhoneLibraryTask;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 引流手机号库
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
@Slf4j
@RestController("CpPhoneLibraryController")
@RequestMapping("/p/cp/phone/library")
@Api(tags = "引流手机号库")
public class CpPhoneLibraryController {

    @Autowired
    private CpPhoneLibraryService cpPhoneLibraryService;
    @Autowired
    private CpPhoneLibraryTask cpPhoneLibraryTask;

	@GetMapping("/page")
	@ApiOperation(value = "获取引流手机号库列表", notes = "分页获取引流手机号库列表")
	public ServerResponseEntity<PageVO<CpPhoneLibrary>> page(@Valid PageDTO pageDTO, CpPhoneLibraryDTO cpPhoneLibraryDTO) {
		PageVO<CpPhoneLibrary> cpPhoneLibraryPage = cpPhoneLibraryService.page(pageDTO,cpPhoneLibraryDTO);
		return ServerResponseEntity.success(cpPhoneLibraryPage);
	}

    @ApiOperation(value = "导入手机号")
    @PostMapping("/importExcel")
    public ServerResponseEntity<String> importExcel(@RequestPart("excelFile") MultipartFile file) {
        if (file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        cpPhoneLibraryService.importExcel(file);
        return  ServerResponseEntity.success();
    }

	@GetMapping
    @ApiOperation(value = "获取引流手机号库", notes = "根据id获取引流手机号库")
    public ServerResponseEntity<CpPhoneLibrary> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpPhoneLibraryService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存引流手机号库", notes = "保存引流手机号库")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CpPhoneLibraryDTO cpPhoneLibraryDTO) {
        cpPhoneLibraryService.createAndUpdate(cpPhoneLibraryDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新引流手机号库", notes = "更新引流手机号库")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CpPhoneLibraryDTO cpPhoneLibraryDTO) {
        cpPhoneLibraryService.createAndUpdate(cpPhoneLibraryDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除引流手机号库", notes = "根据引流手机号库id删除引流手机号库")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        cpPhoneLibraryService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("/refeshStatus")
    @ApiOperation(value = "更新手机号状态", notes = "更新手机号状态")
    public ServerResponseEntity<CpPhoneTask> refeshStatus() {
        cpPhoneLibraryTask.refeshLibraryAndTask();
        return ServerResponseEntity.success();
    }
}
