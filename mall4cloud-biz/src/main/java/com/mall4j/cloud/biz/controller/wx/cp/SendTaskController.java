package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.cp.constant.*;
import com.mall4j.cloud.biz.dto.cp.SendTaskDTO;
import com.mall4j.cloud.biz.dto.cp.SendTaskDetailDTO;
import com.mall4j.cloud.biz.dto.cp.SendTaskPageDTO;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import com.mall4j.cloud.biz.model.cp.SendTask;
import com.mall4j.cloud.biz.model.cp.TaskPush;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import com.mall4j.cloud.biz.service.cp.MaterialMsgService;
import com.mall4j.cloud.biz.service.cp.SendTaskService;
import com.mall4j.cloud.biz.service.cp.TaskPushService;
import com.mall4j.cloud.biz.service.cp.CpTaskStaffRefService;
import com.mall4j.cloud.biz.vo.cp.TaskPushVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 群发任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformSendTaskController")
@RequestMapping("/p/cp/send_task")
@Api(tags = "群发任务表")
public class SendTaskController {

    private final StaffFeignClient staffFeignClient;
    private final SendTaskService sendTaskService;
    private final MaterialMsgService msgService;
    private final CpTaskStaffRefService taskStaffRefService;
    private final TaskPushService taskPushService;
	private final MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取群发任务表列表", notes = "分页获取群发任务表列表")
	public ServerResponseEntity<PageVO<SendTask>> page(@Valid PageDTO pageDTO, SendTaskPageDTO request) {
		return ServerResponseEntity.success(sendTaskService.page(pageDTO, request));
	}

	@GetMapping
    @ApiOperation(value = "获取群发任务表", notes = "根据id获取群发任务表")
    public ServerResponseEntity<SendTaskDetailDTO> getById(@RequestParam Long id) {
        SendTask sendTask = sendTaskService.getById(id);
        List<TaskPush> pushList =  taskPushService.listPushTaskByTaskId(id);
        List<TaskPushVO> pushs = new ArrayList<>();
        pushList.forEach(item->{
            TaskPushVO taskPushVO = mapperFacade.map(item, TaskPushVO.class);
            List<MaterialMsg> msgs = msgService.listByMatId(taskPushVO.getId(),OriginEumn.GROUP_TASK);
            taskPushVO.setAttachment(msgs.get(0));
            pushs.add(taskPushVO);
        });
        List<CpTaskStaffRef> refs = null;
        if(StringUtils.isEmpty(sendTask.getTags())){
              refs = taskStaffRefService.listByTaskId(id, TaskType.TASK.getCode());
        }
        return ServerResponseEntity.success(new SendTaskDetailDTO(sendTask,pushs,refs));
    }

    @PostMapping
    @ApiOperation(value = "保存群发任务表", notes = "保存群发任务表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SendTaskDTO request) {
        SendTask sendTask = mapperFacade.map(request, SendTask.class);
        sendTask.setCreateBy(AuthUserContext.get().getUserId());
        sendTask.setCreateTime(new Date());
        sendTask.setCreateName(AuthUserContext.get().getUsername());
        sendTask.setUpdateTime(sendTask.getCreateTime());
        sendTask.setFlag(StatusType.WX.getCode());
        sendTask.setStatus(StatusType.YX.getCode());
        sendTask.setSendStatus(SendStatus.WAIT.getCode());
        log.info("==update==="+Json.toJsonString(request));
        //全部员工
        if(request.getTagList()!=null) {
            sendTask.setTags(Json.toJsonString(request.getTagList()));
        }
        sendTask.setCreateTime(new Date());
        List<StaffVO> staffList =  getStafflist(request.getStaffList());
        sendTask.setSendTotal(staffList!=null?staffList.size():0);
        sendTaskService.createOrUpdate(sendTask,staffList,request.getPushList(),true);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新群发任务表", notes = "更新群发任务表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SendTaskDTO request) {
        SendTask sendTask = sendTaskService.getById(request.getId()) ;
        sendTask.setTaskName(request.getTaskName());

        log.info("==update==="+Json.toJsonString(request));
        //全部员工
        if(CollectionUtils.isEmpty(request.getTagList())) {
            sendTask.setClearTag(1);
        }else{
            sendTask.setTags(Json.toJsonString(request.getTagList()));
        }
        boolean  isChange  = checkIsChange(request);
        sendTask.setUpdateTime(new Date());
        List<StaffVO> staffList =  getStafflist(request.getStaffList());
        log.info("==staffList=="+Json.toJsonString(staffList));
        sendTask.setSendTotal(staffList!=null?staffList.size():0);
        sendTaskService.createOrUpdate(sendTask,staffList,request.getPushList(),isChange);
        return ServerResponseEntity.success();
    }

    /**
     * 检查员工标签是否有改变
     * @param request
     * @return
     */
    private boolean checkIsChange(SendTaskDTO request) {
        SendTask sendTask  = sendTaskService.getById(request.getId());
        //原先选择的是标签，现在该选哪员工
        if(StringUtils.isNotEmpty(sendTask.getTags())){
            if(!CollectionUtils.isEmpty(request.getStaffList())){
                return true;
            }
        }
        //原先选择的是员工，现在改选为标签
        if(StringUtils.isEmpty(sendTask.getTags())){
              if(!CollectionUtils.isEmpty(request.getTagList())){
                  return true;
              }
        }
        //原先是员工，现在是员工，对比员工的列表
        if(StringUtils.isEmpty(sendTask.getTags()) && !CollectionUtils.isEmpty(request.getStaffList()) ){
            List<CpTaskStaffRef> oldStaffRefList = taskStaffRefService.listByTaskId(sendTask.getId(),TaskType.TASK.getCode());
            if(oldStaffRefList.size()!=request.getStaffList().size()){
                return true;
            }
            for(CpTaskStaffRef taskStaffRef : oldStaffRefList ){
                if(request.getStaffList().stream().filter(id->taskStaffRef.getId().longValue()==id).count()<=0){
                    return true;
                }
            }
        }
        return false;
    }

    @DeleteMapping
    @ApiOperation(value = "删除群发任务表", notes = "根据群发任务表id删除群发任务表")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        sendTaskService.deleteById(id);
        return ServerResponseEntity.success();
    }

    /**
     * 获取关联的员工列表。如果选择标签则发送全部员工
     * @param staffIds
     * @return
     */
    private List<StaffVO> getStafflist(List<Long> staffIds){
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        if(staffIds!=null) {
            staffQueryDTO.setStaffIdList(staffIds);
        }
        staffQueryDTO.setQiweiUserStatus(QiweiUserStatus.ALIVE.getCode());
        ServerResponseEntity<List<StaffVO>> response =  staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
        if(response!=null && !CollectionUtils.isEmpty(response.getData())){
            return  response.getData();
        }
        return null;
    }
}
