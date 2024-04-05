package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionSpuBindDTO;
import com.mall4j.cloud.distribution.model.DistributionSpuBind;
import com.mall4j.cloud.distribution.service.DistributionSpuBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户商品绑定信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@RestController("platformDistributionSpuBindController")
@RequestMapping("/p/distribution_spu_bind")
@Api(tags = "用户商品绑定信息")
public class DistributionSpuBindController {

    @Autowired
    private DistributionSpuBindService distributionSpuBindService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取用户商品绑定信息列表", notes = "分页获取用户商品绑定信息列表")
	public ServerResponseEntity<PageVO<DistributionSpuBind>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionSpuBind> distributionSpuBindPage = distributionSpuBindService.page(pageDTO);
		return ServerResponseEntity.success(distributionSpuBindPage);
	}

	@GetMapping
    @ApiOperation(value = "获取用户商品绑定信息", notes = "根据id获取用户商品绑定信息")
    public ServerResponseEntity<DistributionSpuBind> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionSpuBindService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存用户商品绑定信息", notes = "保存用户商品绑定信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionSpuBindDTO distributionSpuBindDTO) {
        DistributionSpuBind distributionSpuBind = mapperFacade.map(distributionSpuBindDTO, DistributionSpuBind.class);
        distributionSpuBind.setId(null);
        distributionSpuBindService.save(distributionSpuBind);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新用户商品绑定信息", notes = "更新用户商品绑定信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionSpuBindDTO distributionSpuBindDTO) {
        DistributionSpuBind distributionSpuBind = mapperFacade.map(distributionSpuBindDTO, DistributionSpuBind.class);
        distributionSpuBindService.update(distributionSpuBind);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户商品绑定信息", notes = "根据用户商品绑定信息id删除用户商品绑定信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionSpuBindService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
