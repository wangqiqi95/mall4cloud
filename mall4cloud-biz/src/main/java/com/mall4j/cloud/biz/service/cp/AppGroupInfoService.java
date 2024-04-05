package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.vo.cp.AppGroupDetail;

public interface AppGroupInfoService {

    /**
     *
     * @param state 渠道唯一标识
     * @return
     */
    AppGroupDetail getAppGroupDetail(String state);


}
