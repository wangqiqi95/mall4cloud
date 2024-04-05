package com.mall4j.cloud.platform.controller.app;

import com.mall4j.cloud.platform.model.Tentacle;
import com.mall4j.cloud.platform.service.TentacleService;
import com.mall4j.cloud.platform.vo.TentacleVO;
import com.mall4j.cloud.platform.dto.TentacleDTO;

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
 * 触点信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@RestController("appTentacleController")
@RequestMapping("/tentacle")
@Api(tags = "触点信息")
public class TentacleController {

    @Autowired
    private TentacleService tentacleService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取触点信息列表", notes = "分页获取触点信息列表")
	public ServerResponseEntity<PageVO<Tentacle>> page(@Valid PageDTO pageDTO) {
		PageVO<Tentacle> tentaclePage = tentacleService.page(pageDTO);
		return ServerResponseEntity.success(tentaclePage);
	}

	@GetMapping
    @ApiOperation(value = "获取触点信息", notes = "根据id获取触点信息")
    public ServerResponseEntity<Tentacle> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(tentacleService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存触点信息", notes = "保存触点信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody TentacleDTO tentacleDTO) {
        Tentacle tentacle = mapperFacade.map(tentacleDTO, Tentacle.class);
        tentacleService.save(tentacle);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新触点信息", notes = "更新触点信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody TentacleDTO tentacleDTO) {
        Tentacle tentacle = mapperFacade.map(tentacleDTO, Tentacle.class);
        tentacleService.update(tentacle);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除触点信息", notes = "根据触点信息id删除触点信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        tentacleService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
