package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountAuthDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountBindBankCardDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccountAuth;
import com.mall4j.cloud.distribution.model.DistributionWithdrawConfig;
import com.mall4j.cloud.distribution.service.DistributionCommissionAccountAuthService;
import com.mall4j.cloud.distribution.service.DistributionCommissionAccountService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawConfigService;
import com.mall4j.cloud.distribution.vo.DistributionCommissionAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 佣金管理-佣金账户
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
@RestController("staffDistributionCommissionAccountController")
@RequestMapping("/s/distribution_commission_account")
@Api(tags = "导购小程序-佣金管理-佣金账户")
public class DistributionCommissionAccountController {

    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;
    @Autowired
    private DistributionWithdrawConfigService distributionWithdrawConfigService;
    @Autowired
    private DistributionCommissionAccountAuthService distributionCommissionAccountAuthService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StaffFeignClient staffFeignClient;

    @GetMapping("/info")
    @ApiOperation(value = "账户信息", notes = "账户信息")
    public ServerResponseEntity<DistributionCommissionAccountVO> info() {
        Long userId = AuthUserContext.get().getUserId();
        DistributionCommissionAccountVO distributionCommissionAccountVO = distributionCommissionAccountService.info(userId, 1);
        distributionCommissionAccountVO.setCommissionSwitch(0);
        DistributionWithdrawConfig distributionWithdrawConfig = distributionWithdrawConfigService.getByIdentityType(1);
        if (Objects.nonNull(distributionWithdrawConfig) && distributionWithdrawConfig.getCommissionSwitch() != null) {
            distributionCommissionAccountVO.setCommissionSwitch(distributionWithdrawConfig.getCommissionSwitch());
        }
        return ServerResponseEntity.success(distributionCommissionAccountVO);
    }

    @GetMapping("/authInfo")
    @ApiOperation(value = "账户认证信息", notes = "账户认证信息")
    public ServerResponseEntity<DistributionCommissionAccountAuth> authInfo() {
        return ServerResponseEntity.success(distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(1,
                AuthUserContext.get().getUserId()));
    }

    @PostMapping("/auth")
    @ApiOperation(value = "身份认证提交", notes = "身份认证提交")
    public ServerResponseEntity<Void> auth(@Valid @RequestBody DistributionCommissionAccountAuthDTO commissionAccountAuthDTO) {
        Long userId = AuthUserContext.get().getUserId();
        ServerResponseEntity<StaffVO> responseEntity =  staffFeignClient.getStaffById(userId);
        if (responseEntity.isSuccess()) {
            commissionAccountAuthDTO.setTelephone(responseEntity.getData().getMobile());
        }
        commissionAccountAuthDTO.setIdentityType(1);
        commissionAccountAuthDTO.setUserId(userId);
        distributionCommissionAccountAuthService.auth(commissionAccountAuthDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/bindBankCard")
    @ApiOperation(value = "绑定银行卡", notes = "绑定银行卡")
    public ServerResponseEntity<Void> bindBankCard(@Valid @RequestBody DistributionCommissionAccountBindBankCardDTO commissionAccountBindBankCardDTO) {
        Long userId = AuthUserContext.get().getUserId();
        Integer identityType = 1;
        DistributionCommissionAccountAuth distributionCommissionAccountAuth = distributionCommissionAccountAuthService.getByIdentityTypeAndUserId(identityType,
                userId);
        if (Objects.nonNull(distributionCommissionAccountAuth)) {
            DistributionCommissionAccountAuth update = mapperFacade.map(commissionAccountBindBankCardDTO, DistributionCommissionAccountAuth.class);
            update.setId(distributionCommissionAccountAuth.getId());
            update.setCardName(distributionCommissionAccountAuth.getName());
            distributionCommissionAccountAuthService.update(update);
        } else {
            DistributionCommissionAccountAuth save = mapperFacade.map(commissionAccountBindBankCardDTO, DistributionCommissionAccountAuth.class);
            save.setIdentityType(identityType);
            save.setUserId(userId);
            distributionCommissionAccountAuthService.save(save);
        }
        return ServerResponseEntity.success();
    }

}
