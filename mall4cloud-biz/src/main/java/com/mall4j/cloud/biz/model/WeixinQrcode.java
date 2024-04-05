package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import com.mall4j.cloud.common.util.RandomUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 微信二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
@Data
public class WeixinQrcode extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 二维码标题（详情）
     */
    private String actionInfo;

    /**
     * 二维码类型：QR_SCENE：临时整型，QR_STR_SCENE：临时字符串，QR_LIMIT_SCENE：永久整型，
QR_LIMIT_STR_SCENE：永久字符串
     */
    private String actionName;

    /**
     * 整型场景值ID
     */
    private Integer sceneId;

    /**
     * 字符串场景值ID
     */
    private String sceneStr;

    /**
     * 场景值来源
     */
    private Integer sceneSrc;

    /**
     * 二维码ticket
     */
    private String ticket;

    private String url;

    /**
     * 有效时间(秒)
     */
    private Integer expireSeconds;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 二维码地址
     */
    private String qrcodeUrl;

    /**
     * 公众帐号ID
     */
    private String appId;

    /**
     * 触发类型：text文本/news图文
     */
    private String msgType;

    /**
     * 文本内容
     */
    private String textContent;

    /**
     * 图文选择类型（1：自定义，2：选择模板）
     */
    private String actionNewsType;

    /**
     * 图文标题
     */
    private String newsTitle;

    /**
     * 图文摘要
     */
    private String newsDesc;

    /**
     * 图文封面图
     */
    private String newsImg;

    /**
     * 图文跳转链接
     */
    private String newsUrl;

    /**
     * 图文选择模板ID
     */
    private String newsTemplateid;

    /**
     * 标签，逗号隔开
     */
    private String tags;

    /**
     * 触点门店
     */
    private String storeId;

    /**
     * 二维码中间logo
     */
    private String logoUrl;

    /**
     * 有效期类型： 1永久 2临时
     */
    private Integer isExpire;

    /**
     * 状态： 0待审核 1审核通过 2审核不通过
     */
    private Integer status;

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
	 * 触点类型：0员工/1自定义
	 */
	private Integer contactType;

	/**
	 * 自定义触点内容
	 */
	private String contactStr;

	public void inint(){
		this.setId(RandomUtil.getUniqueNumStr());
		this.setCreateTime(new Date());
		this.setStatus(0);
		this.setDelFlag(0);
	}
}
