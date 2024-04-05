package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.task.CpTask;
import com.mall4j.cloud.biz.wx.cp.constant.AssignTypeEunm;
import com.mall4j.cloud.biz.wx.cp.constant.PushStatusEunm;
import com.mall4j.cloud.biz.dto.cp.StaffAssginDTO;
import com.mall4j.cloud.biz.dto.cp.StaffAssginGroupDTO;
import com.mall4j.cloud.biz.model.cp.ResignAssignLog;
import com.mall4j.cloud.biz.service.cp.ResignAssignLogService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *  在职和离职分配列表查询
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformStaffAssginController")
@RequestMapping("/p/cp/staff_assgin")
@Api(tags = "在职和离职分配列表查询")
public class StaffAssginController {
    private final ResignAssignLogService resignAssignLogService;
    private final StaffFeignClient staffClient;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final CpTask cpTask;


    @GetMapping("/custPage")
    @ApiOperation(value = "获取客户列表", notes = "获取客户列表")
    public  ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> custPage( UserStaffCpRelationSearchDTO searchDTO) {
        //查询绑定中的
        searchDTO.setStatus(1);
        return userStaffCpRelationFeignClient.pageWithStaff(searchDTO);
    }


    @PostMapping("/assgin")
    @ApiOperation(value = "分配好友关系", notes = "分配好友关系")
    public ServerResponseEntity<Void> assgin(@Valid @RequestBody StaffAssginDTO request) {
        //查询替换员工信息
        ServerResponseEntity<StaffVO> replaceByResp = staffClient.getStaffById(request.getReplaceBy());
        if (replaceByResp==null|| replaceByResp.getData() == null) {
            throw new LuckException("接替员工不存在!");
        }
        StaffVO replaceByStaff = replaceByResp.getData();
        log.info("分配好友关系=assgin=replaceByStaff:"+ Json.toJsonString(request));
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        staffQueryDTO.setStaffIdList(request.getAddBys());
        ServerResponseEntity<List<StaffVO>> resp = staffClient.findByStaffQueryDTO(staffQueryDTO);
        if(resp==null || resp.getData()==null){
            throw new LuckException("查询员工信息出错!");
        }
        Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(resp.getData(),StaffVO::getId);
        if(Objects.nonNull(staffVOMap.get(request.getReplaceBy()))){
            throw new LuckException("接替员工不可与当前分配员工一致,员工："+staffVOMap.get(request.getReplaceBy()).getStaffName());
        }
        List<StaffVO> staffList =  resp.getData();
        log.info("分配好友关系=assgin=staffList:"+ Json.toJsonString(resp));
        staffList.forEach(staffVO -> resignAssignLogService.assignCust(staffVO,replaceByStaff,request));

        //立即执行：客户继承定时任务
        cpTask.staffExtendsTaskAsync();

        return ServerResponseEntity.success();
    }


    @PostMapping("/assginGroup")
    @ApiOperation(value = "分配客群", notes = "分配客群")
    public ServerResponseEntity<Void> assginGroup(@Valid @RequestBody StaffAssginGroupDTO request) {
        //查询替换员工信息
        ServerResponseEntity<StaffVO> replaceByResp = staffClient.getStaffById(request.getReplaceBy());
        if (replaceByResp==null||replaceByResp.getData() == null) {
            throw new LuckException("替换员工不存在！");
        }
        StaffVO replaceByStaff = replaceByResp.getData();
        log.info("=assginGroup=replaceByStaff:"+ Json.toJsonString(request));
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        staffQueryDTO.setStaffIdList(request.getAddBys());
        ServerResponseEntity<List<StaffVO>> resp = staffClient.findByStaffQueryDTO(staffQueryDTO);
        if(resp==null || resp.getData()==null){
            throw new LuckException("查询员工信息出错!");
        }
        Map<Long,StaffVO> staffVOMap= LambdaUtils.toMap(resp.getData(),StaffVO::getId);
        if(Objects.nonNull(staffVOMap.get(request.getReplaceBy()))){
            throw new LuckException("接替员工不可与当前分配员工一致,员工："+staffVOMap.get(request.getReplaceBy()).getStaffName());
        }
        List<StaffVO> staffList =  resp.getData();
        log.info("=assginGroup=staffList:"+ Json.toJsonString(staffList));
        staffList.forEach(staffVO -> resignAssignLogService.assignGroup(staffVO,replaceByStaff,request));

        //立即执行：客户继承定时任务
        cpTask.staffExtendsTaskAsync();

        return ServerResponseEntity.success();
    }


    @GetMapping("/staffExtendsTask")
    @ApiOperation(value = "测试执行客户继承", notes = "测试执行客户继承")
    public  ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> staffExtendsTask() {
        cpTask.staffExtendsTask();
        return ServerResponseEntity.success();
    }

    @GetMapping("/custExtendStatusQueryTask")
    @ApiOperation(value = "测试客户继承状态更新", notes = "测试客户继承状态更新")
    public  ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> custExtendStatusQueryTask() {
        cpTask.custExtendStatusQueryTask();
        return ServerResponseEntity.success();
    }

}