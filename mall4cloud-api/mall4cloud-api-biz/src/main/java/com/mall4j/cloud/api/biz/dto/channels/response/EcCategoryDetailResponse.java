package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcCat;
import com.mall4j.cloud.api.biz.dto.channels.EcCatAttr;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EcCategoryDetailResponse extends EcBaseResponse{
    private EcCatAttr attr;

    private EcCat info;
}
