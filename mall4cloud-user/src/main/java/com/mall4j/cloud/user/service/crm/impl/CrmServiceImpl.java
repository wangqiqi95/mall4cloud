package com.mall4j.cloud.user.service.crm.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.user.constant.BuildTagFromEnum;
import com.mall4j.cloud.api.user.crm.CrmMethod;
import com.mall4j.cloud.api.user.crm.request.*;
import com.mall4j.cloud.api.user.crm.response.*;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.service.crm.CrmService;
import com.mall4j.cloud.user.utils.CrmClients;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CrmServiceImpl implements CrmService {

    @Override
    public CrmResult<Void> taskSave(TaskSaveRequest request) {
        if (null == request) {
            Assert.faild("参数不允许为空");
        }
        log.info("同步CDP群发任务参数 {}", request);
        String result = CrmClients.clients().postCrm(CrmMethod.TASK_SAVE.uri(), JSONObject.toJSONString(request));
        if (StringUtils.isBlank(result)) {
            Assert.faild("调用-积分查询接口无响应");
        }
        CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);
        if (!crmResult.isSuccess()) {
            throw new LuckException("同步CDP群发任务失败");
        }
        return crmResult;
    }

    @Override
    public ServerResponseEntity<CrmResult<QueryPointResponse>> queryPonint(QueryPointRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            String result = CrmClients.clients().get(CrmMethod.MEMBER_POINT_QUERY.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-积分查询接口无响应");
            }
            CrmResult<QueryPointResponse> crmResult = JSONUtil.toBean(result,
                    new TypeReference<CrmResult<QueryPointResponse>>() {}, true);
//        Assert.isTrue(!crmResult.isSuccess(),"调用-积分查询接口异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-积分查询接口异常 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-积分查询接口异常 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    @Override
    public CrmResult updateMemberPoint(ChangePointRequest request) {
        if (null == request) {
            Assert.faild("参数不允许为空");
        }
        String result = CrmClients.clients().postCrm(CrmMethod.MEMBER_POINT_POST.uri(), JSONObject.toJSONString(request));

        if (StringUtils.isBlank(result)) {
            Assert.faild("调用-积分变更接口无响应");
        }
        CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);
        Assert.isTrue(!crmResult.isSuccess(),"调用-积分变更接口异常，msg:"+crmResult.getMsg());
        return crmResult;
    }

    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> pointRecord(QueryPointRecordRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            if(StrUtil.isNotEmpty(request.getStartTime())){
                stringObjectMap.put("startTime",request.getStartTime());
            }
            if(StrUtil.isNotEmpty(request.getEndTime())){
                stringObjectMap.put("endTime",request.getEndTime());
            }
//        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(request);
            String result = CrmClients.clients().get(CrmMethod.MEMBER_POINTRECORD_GET.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-积分明细查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<QueryPointRecordResponse>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<QueryPointRecordResponse>>>() {}, true);
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-积分明细查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-积分明细查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    @Override
    public CrmResult<List<QueryCategoryResponse>> queryCategory() {

        Map<String, Object> stringObjectMap = null;
        String result = CrmClients.clients().get(CrmMethod.MEMBER_CATEGORY_QUERY.uri(), stringObjectMap);

        if (StringUtils.isBlank(result)) {
            Assert.faild("调用-标签分类查询无响应");
        }
        CrmResult<List<QueryCategoryResponse>> crmResult = JSONUtil.toBean(result,
                new TypeReference<CrmResult<List<QueryCategoryResponse>>>() {}, true);
        return crmResult;
    }

    @Override
    public CrmResult<QueryTagCategoryPageResponse> queryTagCategory(QueryTagCategoryPageRequest request) {
        if (null == request) {
            Assert.faild("参数不允许为空");
        }
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(request);
        String result = CrmClients.clients().get(CrmMethod.MEMBER_TAGCATEGORY_QUERY.uri(), stringObjectMap);

        if (StringUtils.isBlank(result)) {
            Assert.faild("调用-标签列表查询无响应");
        }
        CrmResult<QueryTagCategoryPageResponse> crmResult = JSONUtil.toBean(result,
                new TypeReference<CrmResult<QueryTagCategoryPageResponse>>() {}, true);
//        CrmResult<QueryTagCategoryPageResponse> crmResult = JSONObject.parseObject(result, CrmResult.class);
        if(!crmResult.isSuccess() && Objects.isNull(crmResult.getResult())){
            return crmResult;
        }
//        Assert.isTrue(!crmResult.isSuccess(),"调用-标签列表查询异常，msg:"+crmResult.getMsg());
        return crmResult;
    }

    @Override
    public CrmResult<List<UserTag>> queryMemberTag(QueryUserTagRequest request) {
        Assert.faild("接口已废弃");
//        if (null == request) {
//            Assert.faild("参数不允许为空");
//        }
//        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(request);
//        String result = CrmClients.clients().get(CrmMethod.MEMBER_TAG_QUERY.uri(), stringObjectMap);
//
//        if (StringUtils.isBlank(result)) {
//            Assert.faild("调用-查询用户标签查询无响应");
//        }
//        CrmResult<List<UserTag>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<UserTag>>>() {}, true);
        return null;
    }

    @Override
    public CrmResult updateMemberTag(UpdateUserTagRequest request) {
        if(Objects.isNull(request.getBuildTagFrom())){
            request.setBuildTagFrom(BuildTagFromEnum.REQUEST_H5_API.getCode());
        }
        if (null == request) {
            Assert.faild("参数不允许为空");
        }
        log.info("调用-修改用户标签接口，动作来源:{} 数据信息:{}", BuildTagFromEnum.getDesc(request.getBuildTagFrom()), JSON.toJSONString(request));
        String result = CrmClients.clients().postCrm(CrmMethod.MEMBER_TAG_UPDATE.uri(), JSONObject.toJSONString(request));

        if (StringUtils.isBlank(result)) {
            Assert.faild("调用-修改用户标签接口无响应");
        }
        CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);
        Assert.isTrue(!crmResult.isSuccess(),"调用-修改用户标签接口异常，msg:"+crmResult.getMsg());
        return crmResult;
    }

    /**
     * 用户数据查询
     * @param unionId
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<JSONArray>> editageUser(String unionId,String active,String mobileNumber) {
        try {
            if (StrUtil.isEmpty(unionId)) {
                Assert.faild("参数不允许为空");
            }
            log.info("调用-用户数据查询，数据信息:{}", unionId);
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("unionId",unionId);
            if(StrUtil.isNotEmpty(active)){
                stringObjectMap.put("active",active);
            }
            if(StrUtil.isNotEmpty(mobileNumber)){
                stringObjectMap.put("mobile_number",mobileNumber);
            }
            String result = CrmClients.clients().get(CrmMethod.EDITAGEUSER.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-用户数据查询无响应");
            }
            CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);
//        Assert.isTrue(!crmResult.isSuccess(),"调用-用户数据查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-用户数据查询异常 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-用户数据 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 询价单数据查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageEnquiry(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            if(StrUtil.isNotEmpty(request.getActive())){
                stringObjectMap.put("active",request.getActive());
            }
            if(StrUtil.isNotEmpty(request.getEnquiry_code())){
                stringObjectMap.put("enquiry_code",request.getEnquiry_code());
            }
            String result = CrmClients.clients().get(CrmMethod.EDITAGEENQUIRY.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-询价单数据查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//        Assert.isTrue(!crmResult.isSuccess(),"调用-询价单数据查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-询价单数据查询异常 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-询价单数据查询异常 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 订单数据查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageOrder(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            if(StrUtil.isNotEmpty(request.getActive())){
                stringObjectMap.put("active",request.getActive());
            }
            if(StrUtil.isNotEmpty(request.getJob_code())){
                stringObjectMap.put("job_code",request.getJob_code());
            }
            String result = CrmClients.clients().get(CrmMethod.EDITAGEORDER.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-订单数据查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//        Assert.isTrue(!crmResult.isSuccess(),"调用-订单数据查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-订单数据查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-订单数据查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * PLUS会员数据记录查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageUserTag(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEUSERTAG.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-PLUS会员数据记录 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-PLUS会员数据记录 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-PLUS会员数据记录 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-PLUS会员数据记录 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 预存款数据记录查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageCard(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGECARD.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-预存款数据记录查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//        Assert.isTrue(!crmResult.isSuccess(),"调用-预存款数据记录查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-预存款数据记录查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-预存款数据记录查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 团队信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageGroup(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEGROUP.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-团队信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//        Assert.isTrue(!crmResult.isSuccess(),"调用-团队信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-团队信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-团队信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 支付信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editagePayment(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEPAYMENT.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-支付信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-支付信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-支付信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-支付信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 优惠券信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageCoupon(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("userId",request.getUserId());
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGECOUPON.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-优惠券信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-优惠券信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-优惠券信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-优惠券信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageCouponTracker(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("userId",request.getUserId());
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGECOUPONTRACKER.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-优惠券信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-优惠券信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-优惠券信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-优惠券信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }
    }

    /**
     * 订单评价信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageFeedBack(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            if(StrUtil.isNotEmpty(request.getJob_code())){
                stringObjectMap.put("job_code",request.getJob_code());
            }
            String result = CrmClients.clients().get(CrmMethod.EDITAGEFEEDBACK.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-订单评价信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-订单评价信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-订单评价信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-订单评价信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 发表信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editagePublish(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEPUBLISH.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-发表信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-发表信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-发表信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-发表信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 发票信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageInvoice(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEINVOICE.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-发票信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-发票信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-编辑偏好数据查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-编辑偏好数据查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 编辑偏好数据查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageBooking(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEBOOKING.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-编辑偏好数据查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-编辑偏好数据查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-编辑偏好数据查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-编辑偏好数据查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 老带新数据查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> editageReferFriend(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.EDITAGEREFERFRIEND.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-老带新数据查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-老带新数据查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-老带新数据查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-老带新数据查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 短信发送记录查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> logSms(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.MEMBER_LOG_SMS.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-短信发送记录查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-短信发送记录查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-短信发送记录查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-短信发送记录查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 邮件发送记录查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> logEdms(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.MEMBER_LOG_EDM.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-邮件发送记录查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-邮件发送记录查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-服务评价查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-服务评价查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 服务评价查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryServiceEvaluation(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYSERVICEEVALUATION.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-服务评价查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-服务评价查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-服务评价查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-服务评价查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 积分兑换订单查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryPointExchangeOrder(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYPOINTEXCHANGEORDER.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-积分兑换订单查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
            Assert.isTrue(!crmResult.isSuccess(),"调用-积分兑换订单查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-积分兑换订单查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-积分兑换订单查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 询价单跳出查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryProspectClientProfile(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYPROSPECTCLIENTPROFILE.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-询价单跳出查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-询价单跳出查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-询价单跳出查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-询价单跳出查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    /**
     * 询价单跳出详情信息查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryprospectInteraction(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYPROSPECTINTERACTION.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-询价单跳出详情信息查询 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//        CrmResult<List<JSONObject>> crmResult = JSONUtil.toBean(result,
//                new TypeReference<CrmResult<List<JSONObject>>>() {}, true);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-询价单跳出详情信息查询 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-询价单跳出详情信息查询 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-询价单跳出详情信息查询 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryCreditDebit(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYCREDITDEBIT.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-qryPaymentDetail 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-qryPaymentDetail 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-qryPaymentDetail 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-qryPaymentDetail 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryappletUserproperties(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYAPPLETUSERPROPERTIES.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-qryappletUserproperties 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
//            Assert.isTrue(!crmResult.isSuccess(),"调用-qryappletUserproperties 查询异常，msg:"+crmResult.getMsg());
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-qryappletUserproperties 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-qryappletUserproperties 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }

    }

    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryFavoriteEditor(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYFAVORITEEDITOR.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-qryFavoriteEditor 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-qryFavoriteEditor 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-qryFavoriteEditor 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }
    }

    @Override
    public ServerResponseEntity<CrmResult<List<JSONObject>>> qryEditorBlacklisting(CrmQueryPageRequest request) {
        try {
            if (null == request) {
                Assert.faild("参数不允许为空");
            }
            Map<String, Object> stringObjectMap = new HashMap<>();
            if(StrUtil.isNotEmpty(request.getUnionId())){
                stringObjectMap.put("unionId",request.getUnionId());
            }
            stringObjectMap.put("userId",request.getUserId());
            stringObjectMap.put("page",request.getPage());
            stringObjectMap.put("pageSize",request.getPageSize());
            String result = CrmClients.clients().get(CrmMethod.QRYEDITORBLACKLISTING.uri(), stringObjectMap);

            if (StringUtils.isBlank(result)) {
                Assert.faild("调用-qryEditorBlacklisting 查询无响应");
            }
            CrmResult<List<JSONObject>> crmResult = Json.parseObject(result,CrmResult.class);
            return ServerResponseEntity.success(crmResult);
        }catch (Exception e){
            log.error("调用-qryEditorBlacklisting 查询异常，msg:{} {}",e,e.getMessage());
            CrmResult crmResult=new CrmResult();
            crmResult.setSuccess(false);
            crmResult.setMsg("调用-qryEditorBlacklisting 查询异常:"+e.getMessage());
            return ServerResponseEntity.success(crmResult);
        }
    }
}
