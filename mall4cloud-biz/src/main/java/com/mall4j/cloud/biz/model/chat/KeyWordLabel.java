package com.mall4j.cloud.biz.model.chat;

import lombok.Data;

/**
 * 关键词标签表
 *
 */
@Data
public class KeyWordLabel {

    private Long id;
    private String tagId;
    private String tagName;
    private String tagValue;
}
