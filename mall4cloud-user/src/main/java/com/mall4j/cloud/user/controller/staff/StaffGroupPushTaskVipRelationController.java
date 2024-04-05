package com.mall4j.cloud.user.controller.staff;


import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.UpdateVipRelationFriendStateDTO;
import com.mall4j.cloud.user.service.GroupPushTaskVipRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/s/group/push/task/vip/relation")
@Api(tags = "导购端群发任务用户关系API")
public class StaffGroupPushTaskVipRelationController {

    @Autowired
    private GroupPushTaskVipRelationService groupPushTaskVipRelationService;


    @ParameterValid
    @ApiOperation("修改群发用户关联表好友状态记录")
    @PostMapping("update/friend/state")
    public ServerResponseEntity updateFriendState(@Valid @RequestBody UpdateVipRelationFriendStateDTO updateVipRelationFriendStateDTO){
        return groupPushTaskVipRelationService.updateFriendState(updateVipRelationFriendStateDTO);
    }

}
