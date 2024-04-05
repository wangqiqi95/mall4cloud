package com.mall4j.cloud.biz.vo.cp;

import lombok.Data;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;

@Data
public class CpWxMediaUploadResult extends WxMediaUploadResult {

    private String drainageUrl;

    private String drainagePath;

}
