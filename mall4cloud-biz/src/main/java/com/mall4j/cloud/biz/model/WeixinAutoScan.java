package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信扫码回复表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
@Data
public class WeixinAutoScan extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 二维码id
     */
    private String qrcodeId;

    /**
     * 原始公众号id
     */
    private String appId;

    /**
     * 用户交互类型: 0全部 1:已关注用户 2：未关注用户
     */
    private Integer type;

	private String msgType;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除标识0-正常,1-已删除
     */
    private Integer delFlag;

	/**
	 * 标签集合
	 */
	private String tags;
}
