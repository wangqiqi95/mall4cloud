
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class LogisticCompanyResponse extends BaseResponse {

    @JsonProperty("company_list")
    private List<LogisticCompany> data;

}
