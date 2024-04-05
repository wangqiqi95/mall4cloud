package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcAddCategoryInfo {
    private Long level1;
    private Long level2;
    private Long level3;

    //资质材料，图片fileid，图片类型，最多不超过10张
    private List<String> certificate;
    //报备函，图片fileid，图片类型，最多不超过10张
    private List<String> baobeihan;
    //经营证明，图片fileid，图片类型，最多不超过10张
    private List<String> jingyingzhengming;
    //带货口碑，图片fileid，图片类型，最多不超过10张
    private List<String> daihuokoubei;
    //入住资质，图片fileid，图片类型，最多不超过10张
    private List<String> ruzhuzhizhi;
    //经营流水，图片fileid，图片类型，最多不超过10张
    private List<String> jingyingliushui;
    //补充材料，图片fileid，图片类型，最多不超过10张
    private List<String> buchongcailiao;
    //经营平台，仅支持taobao，jd，douyin，kuaishou，pdd，other这些取值
    private String jingyingpingtai;
    //账号名称
    private String zhanghaomingcheng;
}
