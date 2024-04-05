package com.mall4j.cloud.biz.controller.wx.cp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.biz.dto.cp.CpPhoneTaskDTO;
import com.mall4j.cloud.biz.dto.cp.CpSelectPhoneTaskUserDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskRelDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskStaffService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskUserService;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserExportVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskStaffVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 引流手机号任务
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
@Slf4j
@RestController("CpPhoneTaskController")
@RequestMapping("/p/cp/phone/task")
@Api(tags = "引流手机号任务")
public class CpPhoneTaskController {

    @Autowired
    private CpPhoneTaskService cpPhoneTaskService;
    @Autowired
    private CpPhoneTaskStaffService cpPhoneTaskRelService;
    @Autowired
    private CpPhoneTaskUserService cpPhoneTaskUserService;

	@GetMapping("/page")
	@ApiOperation(value = "获取引流手机号任务列表", notes = "分页获取引流手机号任务列表")
	public ServerResponseEntity<PageVO<CpPhoneTask>> page(@Valid PageDTO pageDTO, SelectCpPhoneTaskDTO dto) {
		PageVO<CpPhoneTask> cpPhoneTaskPage = cpPhoneTaskService.page(pageDTO,dto);
		return ServerResponseEntity.success(cpPhoneTaskPage);
	}

    @GetMapping("/staff/page")
    @ApiOperation(value = "查看员工列表", notes = "查看员工列表")
    public ServerResponseEntity<PageVO<CpPhoneTaskStaffVO>> staffPage(@Valid PageDTO pageDTO, SelectCpPhoneTaskRelDTO dto) {
        PageVO<CpPhoneTaskStaffVO> taskRelVOPageVO = cpPhoneTaskRelService.page(pageDTO,dto);
        return ServerResponseEntity.success(taskRelVOPageVO);
    }

    @GetMapping("/user/page")
    @ApiOperation(value = "查看任务明细列表", notes = "查看任务明细列表")
    public ServerResponseEntity<PageVO<CpPhoneTaskUserVO>> userPage(@Valid PageDTO pageDTO, CpSelectPhoneTaskUserDTO dto) {
        PageVO<CpPhoneTaskUserVO> taskUserVOPageVO = cpPhoneTaskUserService.page(pageDTO,dto);
        return ServerResponseEntity.success(taskUserVOPageVO);
    }

    @GetMapping("/user/exportUser")
    @ApiOperation(value = "查看任务明细列表-导出", notes = "查看任务明细列表-导出")
    public void exportAnalyzeUSRF(CpSelectPhoneTaskUserDTO dto, HttpServletResponse response) {
        List<CpPhoneTaskUserExportVO> dataList=cpPhoneTaskUserService.exportList(dto);
//        if(CollUtil.isEmpty(dataList)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="user-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            ExcelUtil.exportExcel(CpPhoneTaskUserExportVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("导出好友统计失败，请重试！");
        }
    }

	@GetMapping
    @ApiOperation(value = "获取引流手机号任务", notes = "根据id获取引流手机号任务")
    public ServerResponseEntity<CpPhoneTask> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(cpPhoneTaskService.getById(id));
    }

    @GetMapping("/closeTask")
    @ApiOperation(value = "关闭任务", notes = "关闭任务")
    public ServerResponseEntity<CpPhoneTask> closeTask(@RequestParam Long id) {
        cpPhoneTaskService.closeTask(id);
        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "保存引流手机号任务", notes = "保存引流手机号任务")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CpPhoneTaskDTO cpPhoneTaskDTO) {
        cpPhoneTaskService.createAndUpdate(cpPhoneTaskDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新引流手机号任务", notes = "更新引流手机号任务")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CpPhoneTaskDTO cpPhoneTaskDTO) {
        cpPhoneTaskService.createAndUpdate(cpPhoneTaskDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除引流手机号任务", notes = "根据引流手机号任务id删除引流手机号任务")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        cpPhoneTaskService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("/distributeTaskUser")
    @ApiOperation(value = "测试分配手机号", notes = "测试分配手机号")
    public ServerResponseEntity<CpPhoneTask> distributeTaskUser(@RequestParam(required = false) Long id) {
        cpPhoneTaskUserService.distributeTaskUser(id);
        return ServerResponseEntity.success();
    }

}
