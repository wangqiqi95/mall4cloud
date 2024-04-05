package com.mall4j.cloud.biz.service.live;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.SellerInfo;


public interface LiveStoreService {
    SellerInfo getSellerInfo();

    BaseResponse updateSellerInfo(SellerInfo sellerInfo);
}
