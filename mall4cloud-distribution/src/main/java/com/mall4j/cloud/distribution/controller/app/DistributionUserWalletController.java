package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletInfoVO;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletVO;
import com.mall4j.cloud.distribution.dto.DistributionUserWalletDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 分销员钱包信息
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@RestController("appDistributionUserWalletController")
@RequestMapping("/distribution_user_wallet")
@Api(tags = "app-分销员钱包信息")
public class DistributionUserWalletController {

    @Autowired
    private DistributionUserWalletService distributionUserWalletService;

    @Autowired
    private DistributionUserService distributionUserService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping
    @ApiOperation(value = "获取分销员钱包信息")
    public ServerResponseEntity<DistributionUserWalletInfoVO> getByWalletId() {
        //查看当前用户是否为分销员
        DistributionUserVO distributionUser = distributionUserService.getByUserId(AuthUserContext.get().getUserId());
        if (Objects.isNull(distributionUser)) {
            throw new LuckException("您还不是分销员");
        }
        DistributionUserWallet distributionUserWallet = distributionUserWalletService.getByDistributionUserId(distributionUser.getDistributionUserId());
        DistributionUserWalletInfoVO userWalletInfoVO = mapperFacade.map(distributionUserWallet, DistributionUserWalletInfoVO.class);
        return ServerResponseEntity.success(userWalletInfoVO);
    }
}
