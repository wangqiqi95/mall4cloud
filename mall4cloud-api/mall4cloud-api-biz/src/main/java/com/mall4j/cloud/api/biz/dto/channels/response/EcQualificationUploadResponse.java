package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcFile;
import lombok.Data;

@Data
public class EcQualificationUploadResponse extends EcBaseResponse{
    private EcFile data;
}
