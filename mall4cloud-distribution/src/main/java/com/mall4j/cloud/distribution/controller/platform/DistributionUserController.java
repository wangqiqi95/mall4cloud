package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserBanDTO;
import com.mall4j.cloud.distribution.dto.DistributionUserDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionUserAchievementVO;
import com.mall4j.cloud.distribution.vo.DistributionUserBanVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销员信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@RestController("platformDistributionUserController")
@RequestMapping("/p/distribution_user")
@Api(tags = "platform-分销员信息")
public class DistributionUserController {

    @Autowired
    private DistributionUserService distributionUserService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员管理列表", notes = "分页获取分销员管理列表")
	public ServerResponseEntity<PageVO<DistributionUserVO>> page(@Valid PageDTO pageDTO ,DistributionUserDTO distributionUserDTO) {
        PageVO<DistributionUserVO> distributionUserPage = distributionUserService.distributionUserPage(pageDTO, distributionUserDTO);
        return ServerResponseEntity.success(distributionUserPage);
	}

    @GetMapping("/achievement_page")
    @ApiOperation(value = "获取分销业绩统计列表", notes = "分页获取分销业绩统计")
    public ServerResponseEntity<PageVO<DistributionUserAchievementVO>> achievementPage(@Valid PageDTO pageDTO, DistributionUserDTO distributionUserDTO, String userMobile) {
	    PageVO<DistributionUserAchievementVO> page = distributionUserService.achievementPage(pageDTO, distributionUserDTO, userMobile);
        return ServerResponseEntity.success(page);
    }

    @ApiOperation(value = "修改分销员的状态")
    @PutMapping("/update_state")
    public ServerResponseEntity<Void> updateState(@RequestBody DistributionUserBanDTO distributionUserBanDTO){
        DistributionUserBan distributionUserBan = mapperFacade.map(distributionUserBanDTO, DistributionUserBan.class);
        distributionUserService.updateDistributionStateAndBan(distributionUserBan);
        // 清除缓存
        DistributionUser dbDistributionUser = distributionUserService.getByDistributionUserId(distributionUserBanDTO.getDistributionUserId());
        distributionUserService.removeCacheByUserId(dbDistributionUser.getUserId());
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "获取分销员最新的封禁信息")
    @GetMapping("/ban_info")
    public ServerResponseEntity<DistributionUserBanVO> getLatestBanInfo(@RequestParam(value = "distributionUserId") Long distributionUserId) {
	    DistributionUserBanVO distributionUserBanVO = distributionUserService.getLatestBanInfoByDistributionUserId(distributionUserId);
	    return ServerResponseEntity.success(distributionUserBanVO);
    }
}
