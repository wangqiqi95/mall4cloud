package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
@RestController("appUserStaffCpRelationController")
@RequestMapping("/user_staff_cp_relation")
@Api(tags = "")
public class UserStaffCpRelationController {

    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;

    @Autowired
	private MapperFacade mapperFacade;

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserStaffCpRelationDTO userStaffCpRelationDTO) {
        UserStaffCpRelation userStaffCpRelation = mapperFacade.map(userStaffCpRelationDTO, UserStaffCpRelation.class);
        userStaffCpRelationService.save(userStaffCpRelation);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserStaffCpRelationDTO userStaffCpRelationDTO) {
        UserStaffCpRelation userStaffCpRelation = mapperFacade.map(userStaffCpRelationDTO, UserStaffCpRelation.class);
        userStaffCpRelationService.updateById(userStaffCpRelation);
        return ServerResponseEntity.success();
    }
}
