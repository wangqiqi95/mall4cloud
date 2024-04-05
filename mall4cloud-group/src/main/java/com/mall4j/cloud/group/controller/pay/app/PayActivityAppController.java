package com.mall4j.cloud.group.controller.pay.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.PayActivityDrawAppDTO;
import com.mall4j.cloud.group.service.PayActivityBizService;
import com.mall4j.cloud.group.vo.app.PayActivityInfoAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ma/pay_activity")
@Api(tags = "app-支付有礼")
public class PayActivityAppController {
    @Resource
    private PayActivityBizService payActivityBizService;

    @GetMapping("/info/{shopId}/{orderId}")
    @ApiOperation(value = "支付有礼信息", notes = "支付有礼信息")
    public ServerResponseEntity<PayActivityInfoAppVO> info(@PathVariable("shopId") String shopId,@PathVariable("orderId") Long orderId){
        return payActivityBizService.info(shopId,orderId);
    }

    @PostMapping("/draw")
    @ApiOperation(value = "领取支付有礼奖励", notes = "领取支付有礼奖励")
    public ServerResponseEntity<Void> draw(@RequestBody PayActivityDrawAppDTO param){
        Long userId = AuthUserContext.get().getUserId();
        param.setUserId(userId);
        return payActivityBizService.draw(param);
    }
}
