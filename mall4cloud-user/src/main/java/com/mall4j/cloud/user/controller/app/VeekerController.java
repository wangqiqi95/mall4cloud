package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.VeekerApplyDTO;
import com.mall4j.cloud.user.service.VeekerService;
import com.mall4j.cloud.user.vo.VeekerApplyVO;
import com.mall4j.cloud.user.vo.VeekerStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("appVeekerController")
@RequestMapping("/veeker")
@Api(tags = "app-微客管理")
public class VeekerController {

    @Autowired
    private VeekerService veekerService;
    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;

    @PutMapping("/apply")
    @ApiOperation(value = "微客申请", notes = "微客申请")
    public ServerResponseEntity<VeekerApplyVO> apply(@RequestParam Long storeId,
                                                     @RequestParam(required = false) String tentacleNo) {
        VeekerApplyDTO veekerApplyDTO = new VeekerApplyDTO();
        veekerApplyDTO.setStoreId(storeId);
        if (StringUtils.isNotBlank(tentacleNo)) {
            ServerResponseEntity<TentacleContentVO> responseEntity = tentacleContentFeignClient.findByTentacleNo(tentacleNo);
            if (!responseEntity.isSuccess()) {
                throw new LuckException("触点获取失败");
            }
            veekerApplyDTO.setStaffId(responseEntity.getData().getTentacle().getBusinessId());
        }
        return ServerResponseEntity.success(veekerService.apply(veekerApplyDTO));
    }

    @GetMapping("/isVeeker")
    @ApiOperation(value = "是否是微客", notes = "是否是微客")
    public ServerResponseEntity<Boolean> isVeeker() {
        return ServerResponseEntity.success(veekerService.isVeeker(AuthUserContext.get().getUserId()));
    }

    @GetMapping("/getVeekerStatus")
    @ApiOperation(value = "微客状态", notes = "微客状态")
    public ServerResponseEntity<VeekerStatus> getVeekerStatus() {
        return ServerResponseEntity.success(veekerService.getVeekerStatus(AuthUserContext.get().getUserId()));
    }
}
