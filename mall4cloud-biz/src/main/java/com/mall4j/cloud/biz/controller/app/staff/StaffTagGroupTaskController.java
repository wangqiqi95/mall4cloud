package com.mall4j.cloud.biz.controller.app.staff;


import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskPageDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeUserRefDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTask;
import com.mall4j.cloud.biz.model.cp.CpMediaRef;
import com.mall4j.cloud.biz.service.cp.CpMediaRefService;
import com.mall4j.cloud.biz.service.cp.CpTaskUserRefService;
import com.mall4j.cloud.biz.service.cp.GroupCreateTaskService;
import com.mall4j.cloud.biz.vo.cp.GroupCreateTaskVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 标签建群任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
@Slf4j
@RequiredArgsConstructor
@RestController("StaffTagGroupTaskController")
@RequestMapping("/s/tag/group/task")
@Api(tags = "导购-标签建群任务表")
public class StaffTagGroupTaskController {

    private final  GroupCreateTaskService groupCreateTaskService;
    private final CpTaskUserRefService cpTaskUserRefService;
    private final MapperFacade mapperFacade;
    private final CpMediaRefService mediaRefService;


	@GetMapping("/page")
	@ApiOperation(value = "获取标签建群任务表列表", notes = "分页获取标签建群任务表列表")
	public ServerResponseEntity<PageVO<GroupCreateTaskVO>> page(@Valid PageDTO pageDTO, GroupCreateTaskPageDTO request) {
		return ServerResponseEntity.success(groupCreateTaskService.mobilePage(pageDTO,request));
	}

    @PostMapping("/tagRelUsers")
    @ApiOperation(value = "发送好友列表", notes = "发送好友列表")
    public ServerResponseEntity<PageVO<CpTagGroupCodeSendUserVO>> pageTagCodeRelUser(@RequestBody CpTagGroupCodeUserRefDTO request) {
        return ServerResponseEntity.success(cpTaskUserRefService.pageTagCodeRelUser(request));
    }

	@GetMapping
    @ApiOperation(value = "获取标签建群任务详情", notes = "根据id获取标签建群任务详情")
    public ServerResponseEntity<GroupCreateTaskVO> getById(@RequestParam Long id) {
        GroupCreateTaskVO groupCreateTaskVO=mapperFacade.map(groupCreateTaskService.getById(id),GroupCreateTaskVO.class);
        if(Objects.isNull(groupCreateTaskVO)){
            throw new LuckException("未获取到任务详情");
        }
        CpMediaRef cpMediaRef=mediaRefService.getById(CodeChannelEnum.TAG_GROUP_CODE.getValue(),groupCreateTaskVO.getId().toString());
        if(Objects.nonNull(cpMediaRef)){
            groupCreateTaskVO.setDrainageUrlMediaId(cpMediaRef.getMediaId());
        }
        return ServerResponseEntity.success(groupCreateTaskVO);
    }

}
