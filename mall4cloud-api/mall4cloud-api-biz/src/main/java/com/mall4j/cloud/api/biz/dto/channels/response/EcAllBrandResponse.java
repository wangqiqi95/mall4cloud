package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcBrand;
import lombok.Data;

import java.util.List;

@Data
public class EcAllBrandResponse extends EcBaseResponse {

    private List<EcBrand> brands;
    private String next_key;
    private Boolean continue_flag;

}
