package com.mall4j.cloud.biz.model.chat;

import lombok.Data;

/**
 * 结束语表
 *
 */
@Data
public class EndStatement {

    private String id;
    private String name;
    private String mate;
    private Long sessionId;
}
