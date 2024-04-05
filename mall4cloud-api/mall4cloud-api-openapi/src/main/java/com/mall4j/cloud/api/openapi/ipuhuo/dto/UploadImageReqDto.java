package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 类描述：上传图片请求参数
 */
public class UploadImageReqDto extends CommonReqDto implements Serializable {
    private static final long serialVersionUID = 3943148736827595522L;
    private String name;

    private MultipartFile file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "UploadImageReqDto{" + "name='" + name + '\'' + ", file=" + file + '}';
    }
}
