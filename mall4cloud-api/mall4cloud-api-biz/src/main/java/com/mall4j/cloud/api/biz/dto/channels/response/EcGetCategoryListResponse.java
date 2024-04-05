package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcCatIdAndQuaId;
import lombok.Data;

import javax.print.event.PrintJobAttributeEvent;

@Data
public class EcGetCategoryListResponse extends EcBaseResponse {
    private EcCatIdAndQuaId list;
}
