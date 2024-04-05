package com.mall4j.cloud.api.biz.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;

import java.io.Serializable;

@Data
public class MsgAttachment extends Attachment implements Serializable {

    private static final long serialVersionUID = -8078748379570640176L;

    @ApiModelProperty("资源路径")
    private String localUrl;

    @ApiModelProperty("是否开启互动雷达 0否1是")
    private Integer interactiveRadar;

    @ApiModelProperty("原始数据-仅限开启互动雷达返回")
    private JSONObject attachmentData;

    private Long msgId;

    public void init(){
        this.setLink(null);
        this.setImage(null);
        this.setVideo(null);
        this.setFile(null);
        this.setMiniProgram(null);
    }
}
