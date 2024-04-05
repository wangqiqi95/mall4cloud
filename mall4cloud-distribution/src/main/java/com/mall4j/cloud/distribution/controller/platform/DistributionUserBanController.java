package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserBanDTO;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.service.DistributionUserBanService;
import com.mall4j.cloud.distribution.vo.DistributionUserBanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销封禁记录
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
@RestController("platformDistributionUserBanController")
@RequestMapping("/p/distribution_user_ban")
@Api(tags = "platform-分销封禁记录")
public class DistributionUserBanController {

    @Autowired
    private DistributionUserBanService distributionUserBanService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销封禁记录列表", notes = "分页获取分销封禁记录列表")
	public ServerResponseEntity<PageVO<DistributionUserBanVO>> page(@Valid PageDTO pageDTO,DistributionUserBanDTO distributionUserBanDTO) {
		return ServerResponseEntity.success(distributionUserBanService.page(pageDTO,distributionUserBanDTO));
	}
}
