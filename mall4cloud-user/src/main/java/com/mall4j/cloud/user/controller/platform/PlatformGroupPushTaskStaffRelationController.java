package com.mall4j.cloud.user.controller.platform;


import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.QueryGroupPushTaskPageDTO;
import com.mall4j.cloud.user.dto.QuerySonTaskStaffPageDTO;
import com.mall4j.cloud.user.service.GroupPushTaskStaffRelationService;
import com.mall4j.cloud.user.vo.GroupPushTaskPageVO;
import com.mall4j.cloud.user.vo.SonTaskStaffPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/p/group/push/task/staff/relation")
@Api(tags = "platform-管理后台群发任务与导购关系相关API")
public class PlatformGroupPushTaskStaffRelationController {

    @Autowired
    private GroupPushTaskStaffRelationService groupPushTaskStaffRelationService;

    @ParameterValid
    @GetMapping("/get/record/page/by/task")
    @ApiOperation("获取群发任务相关子任务执行统计数据")
    public ServerResponseEntity<PageVO<SonTaskStaffPageVO>> getPage(@Valid QuerySonTaskStaffPageDTO pageDTO){
        PageVO<SonTaskStaffPageVO> sonTaskStaffPage = groupPushTaskStaffRelationService.getSonTaskStaffList(pageDTO);
        return ServerResponseEntity.success(sonTaskStaffPage);

    }
}
