package com.mall4j.cloud.group.vo.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppAdInfoVO implements Serializable {
    private String picUrl;
    private Integer redirectType;
    private String redirectUrl;
}
