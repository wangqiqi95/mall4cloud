package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 埋点数据统计对象
 */
@Data
public class MicroPageBurialPointStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long recordId;

    /**
     * 微信unionID
     */
    private String unionId;

    /**
     * 浏览时间
     */
    private Date browseTime;

    /*
    * 日期
    * */
    private String date;

}
