package com.mall4j.cloud.api.biz.dto.livestore.request.complaint;

import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.MateriaInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ComplaintUploadMaterialRequest {

    @ApiModelProperty("纠纷单号")
    private Long complaint_order_id;

    @ApiModelProperty("说明文本、图片")
    private MateriaInfo material_info;
}
