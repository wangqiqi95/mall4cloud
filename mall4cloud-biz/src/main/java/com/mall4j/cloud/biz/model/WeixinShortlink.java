package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
public class WeixinShortlink extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 小程序appis
     */
    private String appId;

    /**
     * 小程序页面路径
     */
    private String pagePath;

    /**
     * 短链key
     */
    private String shortKey;

    /**
     * 跳转长链接
     */
    private String longUrl;

    /**
     * 域名
     */
    private String doMain;

    /**
     * 携带参数
     */
    private String scen;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;

	/**
	 *	打开次数
	 */
	private Integer openNumber;

	/**
	 *	打开人数
	 */
	private Integer openPeople;

    /**
     * 跳转短链接
     */
    private String shortUrl;

}
