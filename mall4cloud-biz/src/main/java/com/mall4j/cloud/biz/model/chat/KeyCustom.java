package com.mall4j.cloud.biz.model.chat;

import lombok.Data;

/**
 * 关键词敏感词性质自定义表
 *
 */
@Data
public class KeyCustom {

    private Long id;
    private String name;
    private String keyValue;
}
