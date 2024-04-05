package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.api.platform.vo.DistributionRecruitConfigApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import com.mall4j.cloud.distribution.service.DistributionUserIncomeService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.*;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 分销员信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@RestController("appDistributionUserController")
@RequestMapping("/distribution_user")
@Api(tags = "app-分销员信息")
public class DistributionUserController {

    @Autowired
    private DistributionUserService distributionUserService;
    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserIncomeService distributionUserIncomeService;

    @Autowired
	private MapperFacade mapperFacade;



	@GetMapping("/info")
    @ApiOperation(value = "查看分销中心信息", notes = "查看分销中心信息")
    public ServerResponseEntity<AchievementDataVO> getByDistributionUserId() {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUser = distributionUserService.getByUserId(userId);
        if (Objects.isNull(distributionUser)) {
            // 您还不是销售员
            throw new LuckException("您还不是销售员");
        }
        AchievementDataVO achievementDataVO = distributionUserService.getAchievementDataById(distributionUser.getDistributionUserId());
        return ServerResponseEntity.success(achievementDataVO);
    }

    @GetMapping("/distribution_user_info")
    @ApiOperation(value = "分销员信息", notes = "分销员信息")
    public ServerResponseEntity<DistributionUserInfoVO> getDistributionUserInfo() {
        Long userId = AuthUserContext.get().getUserId();
        // 推广开关
        DistributionRecruitConfigApiVO recruitConfig = distributionConfigService.getDistributionRecruitConfig();
        DistributionUserVO distributionUser = distributionUserService.getByUserId(userId);
        DistributionUserInfoVO distributionUserInfoVO = mapperFacade.map(distributionUser, DistributionUserInfoVO.class);
        if (distributionUserInfoVO == null) {
            distributionUserInfoVO = new DistributionUserInfoVO();
        }
        distributionUserInfoVO.setRecruitState(recruitConfig.getState());
        return ServerResponseEntity.success(distributionUserInfoVO);
    }


    @GetMapping("/subordinate")
    @ApiOperation(value = "查看下级", notes = "查看下级")
    public ServerResponseEntity<PageVO<DistributionUserSimpleInfoVO>> subordinatePage(@Valid PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUser = distributionUserService.getByUserId(userId);
        PageVO<DistributionUserSimpleInfoVO> pageVO = distributionUserService.getPageDistributionUserSimpleInfoByParentUserId(pageDTO, distributionUser.getDistributionUserId());
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/distribution_orders")
    @ApiOperation(value = "分页查看分销员佣金明细", notes = "分页查看分销员佣金明细")
    public ServerResponseEntity<PageVO<DistributionOrderVO>> getDistributionOrder(@Valid PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        DistributionUserVO distributionUser = distributionUserService.getByUserId(userId);
        PageVO<DistributionOrderVO> pageVO = distributionUserIncomeService.getDistributionOrderByDistributionUserId(pageDTO, distributionUser.getDistributionUserId());
        return ServerResponseEntity.success(pageVO);
    }


}
