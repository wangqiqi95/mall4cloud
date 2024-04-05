package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.user.model.CrmUserTagRelation;
import com.mall4j.cloud.user.service.CrmUserTagRelationService;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 数云维护 用户标签关系表
 *
 * @author FrozenWatermelon
 * @date 2023-11-25 10:41:27
 */
//@RestController("appCrmUserTagRelationController")
//@RequestMapping("/crm_user_tag_relation")
//@Api(tags = "数云维护 用户标签关系表")
//public class CrmUserTagRelationController {
//
//    @Autowired
//    private CrmUserTagRelationService crmUserTagRelationService;
//
//    @Autowired
//	private MapperFacade mapperFacade;
//
//	@GetMapping("/page")
//	@ApiOperation(value = "获取数云维护 用户标签关系表列表", notes = "分页获取数云维护 用户标签关系表列表")
//	public ServerResponseEntity<PageVO<CrmUserTagRelation>> page(@Valid PageDTO pageDTO) {
//		PageVO<CrmUserTagRelation> crmUserTagRelationPage = crmUserTagRelationService.page(pageDTO);
//		return ServerResponseEntity.success(crmUserTagRelationPage);
//	}
//
//	@GetMapping
//    @ApiOperation(value = "获取数云维护 用户标签关系表", notes = "根据id获取数云维护 用户标签关系表")
//    public ServerResponseEntity<CrmUserTagRelation> getById(@RequestParam Long id) {
//        return ServerResponseEntity.success(crmUserTagRelationService.getById(id));
//    }
//
//}
