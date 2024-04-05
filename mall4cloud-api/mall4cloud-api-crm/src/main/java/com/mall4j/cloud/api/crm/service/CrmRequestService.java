package com.mall4j.cloud.api.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.crm.request.CrmMemberPoint;
import com.mall4j.cloud.api.crm.response.CrmResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;

public interface CrmRequestService {

    /**
     * 积分查询
     * @param request
     * @return
     */
    ServerResponseEntity<CrmResult<JSONObject>> queryPonint(CrmMemberPoint request);

}
