package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.api.platform.vo.DistributionRecruitConfigApiVO;
import com.mall4j.cloud.distribution.constant.DistributionUserStateEnum;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.service.DistributionConfigService;
import com.mall4j.cloud.distribution.service.DistributionUserBindService;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionUserBindInfoVO;


import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 分销员绑定关系
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
@RestController("appDistributionUserBindController")
@RequestMapping("/distribution_user_bind")
@Api(tags = "分销员绑定关系")
public class DistributionUserBindController {

    private static final Logger logger = LoggerFactory.getLogger(DistributionUserBindController.class);

    @Autowired
    private DistributionUserBindService distributionUserBindService;
    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserService distributionUserService;

    @PostMapping("/bind_user")
    @ApiOperation(value = "绑定用户", notes = "根据分销员卡号绑定用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "distributionUserId", value = "卡号", required = true, dataType = "Long")
    })
    public ServerResponseEntity<Void> bindUser(@RequestParam Long distributionUserId) {

        Long userId = AuthUserContext.get().getUserId();
        logger.info("绑定用户distributionUserId:{}",distributionUserId);

        if (Objects.isNull(distributionUserId)) {
            logger.error("获取推广员信息失败");
            return ServerResponseEntity.success();
        }

        DistributionUser shareUser = distributionUserService.getByDistributionUserId(distributionUserId);

        if (shareUser == null) {
            logger.error("获取推广员信息失败");
            return ServerResponseEntity.success();
        }
        if (Objects.equals(DistributionUserStateEnum.NORMAL.value(),shareUser.getState())) {
            logger.error("推广员状态异常");
            return ServerResponseEntity.success();
        }
        DistributionRecruitConfigApiVO recruitConfigApiVO = distributionConfigService.getDistributionRecruitConfig();
        if (recruitConfigApiVO.getState() == 0) {
            logger.error("推广计划已关闭");
            return ServerResponseEntity.success();
        }
        // 绑定分销员
        int type = 0;
        ServerResponseEntity<DistributionUser> serverResponse = distributionUserBindService.bindDistribution(shareUser, userId, type);
        if (!serverResponse.isSuccess()) {
            logger.error(serverResponse.getMsg());
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/bind_user_list")
    @ApiOperation(value = "绑定用户列表", notes = "获取分销员所绑定的用户列表")
    public ServerResponseEntity<PageVO<DistributionUserBindInfoVO>> bindUserList(@Valid PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(distributionUserBindService.bindUserList(pageDTO, userId));
    }
}
