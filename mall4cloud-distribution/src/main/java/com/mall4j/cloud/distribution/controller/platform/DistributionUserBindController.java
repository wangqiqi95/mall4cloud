package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserBindDTO;
import com.mall4j.cloud.distribution.model.DistributionUserBind;
import com.mall4j.cloud.distribution.service.DistributionUserBindService;
import com.mall4j.cloud.distribution.vo.DistributionUserBindVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销员绑定关系
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
@RestController("platformDistributionUserBindController")
@RequestMapping("/p/distribution_user_bind")
@Api(tags = "platform-分销员绑定关系")
public class DistributionUserBindController {

    @Autowired
    private DistributionUserBindService distributionUserBindService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员绑定关系列表", notes = "分页获取分销员绑定关系列表")
	public ServerResponseEntity<PageVO<DistributionUserBindVO>> page(@Valid PageDTO pageDTO,DistributionUserBindDTO distributionUserBindDTO) {
		return ServerResponseEntity.success(distributionUserBindService.page(pageDTO,distributionUserBindDTO));
	}
}
