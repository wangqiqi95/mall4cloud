package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcCatAttrInfo {

    //类目必填项名称
    private String name;
    //类目必填项类型，string为自定义，select_one为多选一
    private String type;
    //类目必填项值
    private String value;
    //是否类目必填项
    private Boolean is_required;

}
