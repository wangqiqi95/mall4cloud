package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcCatAndQua;
import com.mall4j.cloud.api.biz.dto.channels.EcCats;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EcAllCategoryResponse extends EcBaseResponse {

    private List<EcCats> cats;

}
