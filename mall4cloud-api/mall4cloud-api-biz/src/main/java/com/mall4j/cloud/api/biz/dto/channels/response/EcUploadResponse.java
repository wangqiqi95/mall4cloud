package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcPicFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EcUploadResponse extends EcBaseResponse {
    private EcPicFile pic_file;
    private String img_url;

    @Override
    public String toString() {
        return "EcUploadResponse{" +
                "pic_file=" + pic_file +
                ", img_url='" + img_url + '\'' +
                '}';
    }
}
