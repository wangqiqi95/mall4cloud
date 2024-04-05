package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 微信触点门店二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
@Data
public class WeixinQrcodeTentacleStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 触点id
     */
    private String tentacleId;

    /**
     * 门店id
     */
    private String storeId;

    /**
     * 跳转路径
     */
    private String path;

	/**
	 * 参数
	 */
    private String scene;

    /**
     * 二维码尺寸
     */
    private Integer codeWidth;

    /**
     * 二维码路径
     */
    private String qrcodePath;

    /**
     * 状态： 0可用 1不可用
     */
    private Integer status;

    /**
     * 删除标识0-正常,1-已删除
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

}
