package com.mall4j.cloud.user.service.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.user.crm.request.*;
import com.mall4j.cloud.api.user.crm.response.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface CrmService {

    /**
     * 积分查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<QueryPointResponse>> queryPonint(QueryPointRequest request);

    /**
     * 群发任务同步
     *
     * @param request
     * @return
     */
    CrmResult<Void> taskSave(TaskSaveRequest request);


    /**
     * 积分变更接口
     * @param request
     * @return
     */
    CrmResult updateMemberPoint(ChangePointRequest request);


    /**
     * 积分明细查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> pointRecord(QueryPointRecordRequest request);

    /**
     * 获取标签分类
     * @return
     */
    CrmResult<List<QueryCategoryResponse>> queryCategory();

    /**
     * 标签列表查询
     * @param request
     * @return
     */
    CrmResult<QueryTagCategoryPageResponse> queryTagCategory(QueryTagCategoryPageRequest request);


    /**
     * 查询用户标签
     * @param request
     * @return
     */
    CrmResult<List<UserTag>> queryMemberTag(QueryUserTagRequest request);


    /**
     * 更新用户标签
     * @param request
     * @return
     */
    CrmResult updateMemberTag(UpdateUserTagRequest request);

    /**
     * 用户数据查询
     * @param unionId
     * @return
     */
    ServerResponseEntity<CrmResult<JSONArray>> editageUser(String unionId,String active,String mobileNumber);

    /**
     * 询价单数据查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageEnquiry(CrmQueryPageRequest request);

    /**
     * 订单数据查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageOrder(CrmQueryPageRequest request);

    /**
     * PLUS会员数据记录查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageUserTag(CrmQueryPageRequest request);

    /**
     * 预存款数据记录查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageCard(CrmQueryPageRequest request);

    /**
     * 团队信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageGroup(CrmQueryPageRequest request);

    /**
     * 支付信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editagePayment(CrmQueryPageRequest request);

    /**
     * 优惠券信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageCoupon(CrmQueryPageRequest request);

    ServerResponseEntity<CrmResult<List<JSONObject>>> editageCouponTracker(CrmQueryPageRequest request);

    /**
     * 订单评价信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageFeedBack(CrmQueryPageRequest request);

    /**
     * 发表信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editagePublish(CrmQueryPageRequest request);

    /**
     * 发票信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageInvoice(CrmQueryPageRequest request);

    /**
     * 编辑偏好数据查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageBooking(CrmQueryPageRequest request);

    /**
     * 老带新数据查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> editageReferFriend(CrmQueryPageRequest request);

    /**
     * 短信发送记录查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> logSms(CrmQueryPageRequest request);

    /**
     * 邮件发送记录查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> logEdms(CrmQueryPageRequest request);

    /**
     * 服务评价查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryServiceEvaluation(CrmQueryPageRequest request);

    /**
     * 积分兑换订单查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryPointExchangeOrder(CrmQueryPageRequest request);

    /**
     * 询价单跳出查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryProspectClientProfile(CrmQueryPageRequest request);

    /**
     * 询价单跳出详情信息查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryprospectInteraction(CrmQueryPageRequest request);

    /**
     * qryPaymentDetail
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryCreditDebit(CrmQueryPageRequest request);

    /**
     * qryappletUserproperties
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryappletUserproperties(CrmQueryPageRequest request);

    /**
     * qryFavoriteEditor
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryFavoriteEditor(CrmQueryPageRequest request);

    /**
     * qryEditorBlacklisting
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<List<JSONObject>>> qryEditorBlacklisting(CrmQueryPageRequest request);

}
