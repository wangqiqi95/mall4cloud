package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-01-24 17:09
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImageAttachMentVO extends AttachMentVO {
    private Image  image;

    public ImageAttachMentVO(String  mediaId, String  picUrl){
        this.image = new Image ();
        this.image.setMediaId(mediaId);
        this.image.setPicUrl(picUrl);
    }
    @Data
    public static class Image{
        private String  mediaId;
        private String  picUrl;
    }
}
