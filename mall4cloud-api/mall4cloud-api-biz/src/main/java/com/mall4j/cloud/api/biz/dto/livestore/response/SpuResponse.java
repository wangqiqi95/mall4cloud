package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.SpuRequest;
import lombok.Data;

@Data
public class SpuResponse extends BaseResponse {

    private SpuRequest spu;
}
