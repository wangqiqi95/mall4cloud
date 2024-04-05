package com.mall4j.cloud.payment.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.dto.AccountSearchDTO;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.mall4j.cloud.payment.service.RefundInfoService;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.PayInfoVO;
import com.mall4j.cloud.payment.vo.RefundInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pineapple
 * @date 2021/6/9 15:57
 */
@RestController("platformAccountDetailController")
@RequestMapping("/p/account_detail")
@Api(tags = "platform-账户详情")
public class AccountDetailController {
    @Autowired
    private PayInfoService payInfoService;

    @Autowired
    private RefundInfoService refundInfoService;

    @GetMapping("/get_income_account_detail")
    @ApiOperation(value = "获取收入账户详情", notes = "根据时间获取")
    public ServerResponseEntity<AccountDetailVO> getIncomeAccountDetail(AccountSearchDTO accountSearchDTO){
        AccountDetailVO accountDetailVO = payInfoService.getIncomeAccountDetail(accountSearchDTO.getStartTime(), accountSearchDTO.getEndTime());
        return ServerResponseEntity.success(accountDetailVO);
    }

    @GetMapping("/get_refund_account_detail")
    @ApiOperation(value = "获取退款账户详情", notes = "根据时间获取")
    public ServerResponseEntity<AccountDetailVO> getRefundAccountDetail(AccountSearchDTO accountSearchDTO){
        AccountDetailVO accountDetailVO = refundInfoService.getRefundAccountDetail(accountSearchDTO.getStartTime(), accountSearchDTO.getEndTime());
        return ServerResponseEntity.success(accountDetailVO);
    }

    @GetMapping("/get_pay_info")
    @ApiOperation(value = "获取支付详情",notes = "根据分页参数和时间获取")
    public ServerResponseEntity<PageVO<PayInfoVO>> getPayInfo(PageDTO pageDTO,AccountSearchDTO accountSearchDTO){
        PageVO<PayInfoVO> pageVO = payInfoService.getPayInfoPage(pageDTO, accountSearchDTO.getStartTime(), accountSearchDTO.getEndTime());
        return ServerResponseEntity.success(pageVO);
    }

    @GetMapping("/get_refund_info")
    @ApiOperation(value = "获取退款详情",notes = "根据分页参数和时间获取")
    public ServerResponseEntity<PageVO<RefundInfoVO>> getRefundInfo(PageDTO pageDTO,AccountSearchDTO accountSearchDTO){
        PageVO<RefundInfoVO> pageVO = refundInfoService.getRefundInfoPage(pageDTO, accountSearchDTO.getStartTime(), accountSearchDTO.getEndTime());
        return ServerResponseEntity.success(pageVO);
    }
}
