package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MateriaInfo {

    @ApiModelProperty("纠纷凭证文本信息")
    private String content;

    @ApiModelProperty("纠纷凭证图片url列表")
    private List<String> media_url_list;

}
