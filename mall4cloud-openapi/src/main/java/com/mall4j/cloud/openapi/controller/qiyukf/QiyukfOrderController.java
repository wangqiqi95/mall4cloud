package com.mall4j.cloud.openapi.controller.qiyukf;


import com.mall4j.cloud.api.openapi.dto.qiyukf.QiyukfQueryDTO;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfOrderServerResponseEntity;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfTokenVO;
import com.mall4j.cloud.openapi.service.qiyukf.QiyukfOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ua/support/qiyukf/order")
@Api(tags = "提供七鱼客服查询用户相关订单信息")
public class QiyukfOrderController {

    @Autowired
    private QiyukfOrderService qiyukfOrderService;

    @GetMapping("/get/token")
    public QiyukfTokenVO getToken(@RequestParam("appid") String appid, @RequestParam("appsecret") String appsecret){
        return qiyukfOrderService.getToken(appid, appsecret);
    }



    @PostMapping("/page")
    public QiyukfOrderServerResponseEntity getTheOrderPageByUser(@RequestBody QiyukfQueryDTO qiyukfQueryDTO, HttpServletRequest request){
        return qiyukfOrderService.getTheOrderPageByUser(qiyukfQueryDTO, request);
    }
}
