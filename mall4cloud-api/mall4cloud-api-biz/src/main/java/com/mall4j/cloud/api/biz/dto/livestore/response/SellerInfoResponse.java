
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.SellerInfo;
import lombok.Data;

@Data
public class SellerInfoResponse extends BaseResponse {

    private SellerInfo data;

}
