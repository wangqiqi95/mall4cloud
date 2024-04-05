package com.mall4j.cloud.api.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.crm.request.CrmBaseRequest;
import com.mall4j.cloud.api.crm.request.CrmMemberPoint;
import com.mall4j.cloud.api.crm.response.CrmResult;
import com.mall4j.cloud.api.crm.service.CrmRequestService;
import com.mall4j.cloud.api.crm.util.CrmClients;
import com.mall4j.cloud.api.crm.util.CrmMethod;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrmRequestServiceImpl implements CrmRequestService {

    /**
     * 积分查询
     * @param request
     * @return
     */
    @Override
    public ServerResponseEntity<CrmResult<JSONObject>> queryPonint(CrmMemberPoint request) {
        if (null == request) {
            Assert.faild("参数不允许为空");
        }

        CrmBaseRequest crmBaseRequest=new CrmBaseRequest();
        crmBaseRequest.init(MDC.get("X-B3-TraceId"));
        crmBaseRequest.setIdentify(JSONObject.toJSONString(request));

        String result = CrmClients.clients().postCrm(CrmMethod.MEMBER_POINT_QUERY.uri(), JSONObject.toJSONString(crmBaseRequest));
        if (StringUtils.isBlank(result)) {
            Assert.faild("调用-积分查询接口无响应");
        }
        CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);
        if (!crmResult.isSuccess()) {
            throw new LuckException("调用-积分查询接口失败");
        }

        return null;
    }
}
