package com.mall4j.cloud.platform.controller.platform;

import com.mall4j.cloud.platform.model.TentacleMarketingPlan;
import com.mall4j.cloud.platform.service.TentacleMarketingPlanService;
import com.mall4j.cloud.platform.vo.TentacleMarketingPlanVO;
import com.mall4j.cloud.platform.dto.TentacleMarketingPlanDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 触点作业批次
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@RestController("platformTentacleMarketingPlanController")
@RequestMapping("/p/tentacle_marketing_plan")
@Api(tags = "触点作业批次")
public class TentacleMarketingPlanController {

    @Autowired
    private TentacleMarketingPlanService tentacleMarketingPlanService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取触点作业批次列表", notes = "分页获取触点作业批次列表")
	public ServerResponseEntity<PageVO<TentacleMarketingPlan>> page(@Valid PageDTO pageDTO) {
		PageVO<TentacleMarketingPlan> tentacleMarketingPlanPage = tentacleMarketingPlanService.page(pageDTO);
		return ServerResponseEntity.success(tentacleMarketingPlanPage);
	}

	@GetMapping
    @ApiOperation(value = "获取触点作业批次", notes = "根据id获取触点作业批次")
    public ServerResponseEntity<TentacleMarketingPlan> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(tentacleMarketingPlanService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存触点作业批次", notes = "保存触点作业批次")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TentacleMarketingPlanDTO tentacleMarketingPlanDTO) {
        TentacleMarketingPlan tentacleMarketingPlan = mapperFacade.map(tentacleMarketingPlanDTO, TentacleMarketingPlan.class);
        tentacleMarketingPlan.setId(null);
        tentacleMarketingPlanService.save(tentacleMarketingPlan);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新触点作业批次", notes = "更新触点作业批次")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TentacleMarketingPlanDTO tentacleMarketingPlanDTO) {
        TentacleMarketingPlan tentacleMarketingPlan = mapperFacade.map(tentacleMarketingPlanDTO, TentacleMarketingPlan.class);
        tentacleMarketingPlanService.update(tentacleMarketingPlan);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除触点作业批次", notes = "根据触点作业批次id删除触点作业批次")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        tentacleMarketingPlanService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
