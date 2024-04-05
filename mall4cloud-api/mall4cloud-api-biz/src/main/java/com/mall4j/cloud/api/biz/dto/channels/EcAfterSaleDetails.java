package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcAfterSaleDetails {
    //售后描述
    private String desc;
    //是否已经收到货
    private String receive_product;
    //取消售后时间
    private String cancel_time;
    //举证图片media_id列表,根据 mediaid 获取文件内容接口
    private List<String> media_id_list;
    //联系电话
    private String tel_number;

    private List<String> prove_imgs;
}
