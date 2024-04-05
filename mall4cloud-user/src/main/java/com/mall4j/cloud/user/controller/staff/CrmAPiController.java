package com.mall4j.cloud.user.controller.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.user.constant.BuildTagFromEnum;
import com.mall4j.cloud.api.user.crm.request.*;
import com.mall4j.cloud.api.user.crm.response.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.crm.CrmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("CrmAPiController")
@RequestMapping("/s/crm")
@Api(tags = "导购端-数云接口")
public class CrmAPiController {

    @Autowired
    private CrmService crmService;

    @PostMapping("/queryPonint")
    @ApiOperation(value = "积分查询", notes = "积分查询")
    public ServerResponseEntity<CrmResult<QueryPointResponse>> queryPonint(@RequestBody QueryPointRequest request) {
        return crmService.queryPonint(request);
    }

    @PostMapping("/updateMemberPoint")
    @ApiOperation(value = "积分变更接口", notes = "积分变更接口")
    public ServerResponseEntity<CrmResult> updateMemberPoint(@RequestBody ChangePointRequest request) {
        return ServerResponseEntity.success(crmService.updateMemberPoint(request));
    }

    @PostMapping("/pointRecord")
    @ApiOperation(value = "积分变更接口", notes = "积分变更接口")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> pointRecord(@RequestBody QueryPointRecordRequest request) {
        return crmService.pointRecord(request);
    }

    @PostMapping("/queryCategory")
    @ApiOperation(value = "标签目录查询", notes = "标签目录查询")
    public ServerResponseEntity<CrmResult<List<QueryCategoryResponse>>> queryTagCategory() {
        return ServerResponseEntity.success(crmService.queryCategory());
    }

    @PostMapping("/queryTagCategory")
    @ApiOperation(value = "标签列表查询", notes = "标签列表查询")
    public ServerResponseEntity<CrmResult<QueryTagCategoryPageResponse>> queryTagCategory(@RequestBody QueryTagCategoryPageRequest request) {
        return ServerResponseEntity.success(crmService.queryTagCategory(request));
    }

    @PostMapping("/queryMemberTag")
    @ApiOperation(value = "查询用户标签", notes = "查询用户标签")
    public ServerResponseEntity<CrmResult<List<UserTag>>> queryMemberTag(@RequestBody QueryUserTagRequest request) {
        return ServerResponseEntity.success(crmService.queryMemberTag(request));
    }

    @PostMapping("/updateMemberTag")
    @ApiOperation(value = "更新用户标签", notes = "更新用户标签")
    public ServerResponseEntity<CrmResult<List<QueryTagCategoryPageResponse>>> updateMemberTag(@RequestBody UpdateUserTagRequest request) {
        return ServerResponseEntity.success(crmService.updateMemberTag(request));
    }

    @GetMapping("/editageUser")
    @ApiOperation(value = "用户数据查询", notes = "用户数据查询")
    public ServerResponseEntity<CrmResult<JSONArray>> editageUser(@RequestParam(value = "unionId",required = true) String unionId,
                                                                  @RequestParam(value = "active",required = false,defaultValue = "") String active,
                                                                  @RequestParam(value = "mobileNumber",required = false,defaultValue = "") String mobileNumber) {
        return crmService.editageUser(unionId,active,mobileNumber);
    }

    @PostMapping("/editageEnquiry")
    @ApiOperation(value = "询价单数据查询", notes = "询价单数据查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageEnquiry(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageEnquiry(request);
    }

    @PostMapping("/editageOrder")
    @ApiOperation(value = "订单数据查询", notes = "订单数据查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageOrder(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageOrder(request);
    }

    @PostMapping("/editageUserTag")
    @ApiOperation(value = "PLUS会员数据记录查询", notes = "PLUS会员数据记录查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageUserTag(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageUserTag(request);
    }

    @PostMapping("/editageCard")
    @ApiOperation(value = "预存款数据记录查询", notes = "预存款数据记录查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageCard(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageCard(request);
    }

    @PostMapping("/editageGroup")
    @ApiOperation(value = "团队信息查询", notes = "团队信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageGroup(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageGroup(request);
    }

    @PostMapping("/editagePayment")
    @ApiOperation(value = "支付信息查询", notes = "支付信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editagePayment(@RequestBody CrmQueryPageRequest request) {
        return crmService.editagePayment(request);
    }

    @PostMapping("/editageCoupon")
    @ApiOperation(value = "优惠券信息查询", notes = "优惠券信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageCoupon(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageCoupon(request);
    }

    @PostMapping("/editageCouponTracker")
    @ApiOperation(value = "优惠券信息查询-editageCouponTracker", notes = "优惠券信息查询-editageCouponTracker")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageCouponTracker(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageCouponTracker(request);
    }

    @PostMapping("/editageFeedBack")
    @ApiOperation(value = "订单评价信息查询", notes = "订单评价信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageFeedBack(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageFeedBack(request);
    }

    @PostMapping("/editagePublish")
    @ApiOperation(value = "发表信息查询", notes = "发表信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editagePublish(@RequestBody CrmQueryPageRequest request) {
        return crmService.editagePublish(request);
    }

    @PostMapping("/editageInvoice")
    @ApiOperation(value = "发票信息查询", notes = "发票信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageInvoice(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageInvoice(request);
    }

    @PostMapping("/editageBooking")
    @ApiOperation(value = "编辑偏好数据查询", notes = "编辑偏好数据查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageBooking(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageBooking(request);
    }

    @PostMapping("/editageReferFriend")
    @ApiOperation(value = "老带新数据查询", notes = "老带新数据查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageReferFriend(@RequestBody CrmQueryPageRequest request) {
        return crmService.editageReferFriend(request);
    }

    @PostMapping("/logSms")
    @ApiOperation(value = "短信发送记录查询", notes = "短信发送记录查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> logSms(@RequestBody CrmQueryPageRequest request) {
        return crmService.logSms(request);
    }

    @PostMapping("/logEdms")
    @ApiOperation(value = "邮件发送记录查询", notes = "邮件发送记录查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> logEdms(@RequestBody CrmQueryPageRequest request) {
        return crmService.logEdms(request);
    }

    @PostMapping("/qryServiceEvaluation")
    @ApiOperation(value = "服务评价查询", notes = "服务评价查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryServiceEvaluation(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryServiceEvaluation(request);
    }

    @PostMapping("/qryPointExchangeOrder")
    @ApiOperation(value = "积分兑换订单查询", notes = "积分兑换订单查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryPointExchangeOrder(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryPointExchangeOrder(request);
    }

    @PostMapping("/qryProspectClientProfile")
    @ApiOperation(value = "询价单跳出查询", notes = "询价单跳出查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryProspectClientProfile(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryProspectClientProfile(request);
    }

    @PostMapping("/qryprospectInteraction")
    @ApiOperation(value = "询价单跳出详情信息查询", notes = "询价单跳出详情信息查询")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryprospectInteraction(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryprospectInteraction(request);
    }

    @PostMapping("/qryCreditDebit")
    @ApiOperation(value = "qryCreditDebit", notes = "qryCreditDebit")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryCreditDebit(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryCreditDebit(request);
    }

    @PostMapping("/qryappletUserproperties")
    @ApiOperation(value = "qryappletUserproperties", notes = "qryappletUserproperties")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryappletUserproperties(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryappletUserproperties(request);
    }

    @PostMapping("/qryFavoriteEditor")
    @ApiOperation(value = "qryFavoriteEditor", notes = "qryFavoriteEditor")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryFavoriteEditor(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryFavoriteEditor(request);
    }

    @PostMapping("/qryEditorBlacklisting")
    @ApiOperation(value = "qryEditorBlacklisting", notes = "qryEditorBlacklisting")
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryEditorBlacklisting(@RequestBody CrmQueryPageRequest request) {
        return crmService.qryEditorBlacklisting(request);
    }

}
