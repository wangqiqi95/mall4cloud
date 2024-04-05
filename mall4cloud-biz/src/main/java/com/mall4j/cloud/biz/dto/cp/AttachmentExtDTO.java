package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-17 10:05
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class AttachmentExtDTO {

    private Attachment attachment;

    private String localUrl;

    @ApiModelProperty("文件原始名称")
    private String fileName;

    @ApiModelProperty("素材库id")
    private Long materialId;

    public AttachmentExtDTO(Attachment attachment,String localUrl){
        this.attachment =attachment;
        this.localUrl = localUrl;
    }

}
