package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcCat;
import lombok.Data;

@Data
public class EcGetAvailablesoncategoriesResponse extends EcBaseResponse {

    private EcCat cat_list;

}
