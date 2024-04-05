package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

import java.util.List;

@Data
public class EcAddcomplaintmaterialReqeust {
    //纠纷单号
    private Long complaint_id;
    //举证文字内容，最多500字
    private String content;
    //举证图片media_id列表
    private List<String> media_id_list;
}
