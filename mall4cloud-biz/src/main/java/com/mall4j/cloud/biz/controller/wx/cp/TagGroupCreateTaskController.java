package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeStaffDTO;
import com.mall4j.cloud.biz.service.cp.CpDetailGroupCodeAnalyzeService;
import com.mall4j.cloud.biz.vo.cp.GroupCreateTaskVO;
import com.mall4j.cloud.biz.vo.cp.analyze.AnalyzeGroupCodeVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeStaffVO;
import com.mall4j.cloud.biz.dto.GroupCreateTaskDetailDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskPageDTO;
import com.mall4j.cloud.biz.service.cp.GroupCreateTaskService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 标签建群任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformGroupCreateTaskController")
@RequestMapping("/p/cp/group_create_task")
@Api(tags = "标签建群任务表")
public class TagGroupCreateTaskController {

    private final  GroupCreateTaskService groupCreateTaskService;
    private final CpDetailGroupCodeAnalyzeService analyzeService;


	@GetMapping("/page")
	@ApiOperation(value = "获取标签建群任务表列表", notes = "分页获取标签建群任务表列表")
	public ServerResponseEntity<PageVO<GroupCreateTaskVO>> page(@Valid PageDTO pageDTO, GroupCreateTaskPageDTO request) {
		return ServerResponseEntity.success(groupCreateTaskService.page(pageDTO,request));
	}

    @GetMapping("/tagRelUsers")
    @ApiOperation(value = "发送好友列表", notes = "发送好友列表")
    public ServerResponseEntity<PageVO<CpTagGroupCodeSendUserVO>> pageTagCodeRelUser(@Valid PageDTO pageDTO, CpTagGroupCodeAnalyzeDTO request) {
        return ServerResponseEntity.success(analyzeService.pageTagCodeRelUser(pageDTO,request));
    }

    @GetMapping("/tagRelGroups")
    @ApiOperation(value = "关联群聊列表", notes = "关联群聊列表")
    public ServerResponseEntity<PageVO<AnalyzeGroupCodeVO>> pageTagGroupRelGroup(@Valid PageDTO pageDTO, CpTagGroupCodeAnalyzeDTO request) {
        return ServerResponseEntity.success(analyzeService.pageTagGroupRelGroup(pageDTO,request));
    }

    @GetMapping("/tagRelStaffs")
    @ApiOperation(value = "关联员工", notes = "关联员工")
    public ServerResponseEntity<PageVO<CpTagGroupCodeStaffVO>> pageTagGroupRelStaff(@Valid PageDTO pageDTO, CpTagGroupCodeAnalyzeStaffDTO request) {
        return ServerResponseEntity.success(analyzeService.pageTagGroupRelStaff(pageDTO,request));
    }

	@GetMapping
    @ApiOperation(value = "获取标签建群任务表", notes = "根据id获取标签建群任务表")
    public ServerResponseEntity<GroupCreateTaskDetailDTO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(groupCreateTaskService.getDetailById(id));
    }

    @GetMapping("/warnStaff")
    @ApiOperation(value = "提醒员工", notes = "提醒员工")
    public ServerResponseEntity<Void> warnStaff(@RequestParam Long id) {
        groupCreateTaskService.warnStaff(id);
        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "保存标签建群任务表", notes = "保存标签建群任务表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody GroupCreateTaskDTO groupCreateTaskDTO) {
        groupCreateTaskService.createOrUpdateTask(groupCreateTaskDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新标签建群任务表", notes = "更新标签建群任务表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody GroupCreateTaskDTO groupCreateTaskDTO){
        groupCreateTaskService.createOrUpdateTask(groupCreateTaskDTO);
        return ServerResponseEntity.success();
    }


    @DeleteMapping
    @ApiOperation(value = "删除标签建群任务表", notes = "根据标签建群任务表id删除标签建群任务表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        groupCreateTaskService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
