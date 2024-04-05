package com.mall4j.cloud.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "merge.picture.param")
public class UploadUrlMediaProperties {

    private String remark;


}
