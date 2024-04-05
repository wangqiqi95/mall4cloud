package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * 应用消息配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Data
public class NotifyConfig extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 消息名称
     */
    private String msgName;

    /**
     * 消息状态
     */
    private Integer msgStatus;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 消息推送时间
     */
    private String pushTime;

    /**
     * 消息推送对象
     */
    private String toUser;

    /**
     * 变量名
     */
    private String paramName;

    /**
     * 消息url
     */
    private String msgUrl;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 状态：0禁用/1启用
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer flag;


	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				",msgName=" + msgName +
				",msgStatus=" + msgStatus +
				",msgContent=" + msgContent +
				",pushTime=" + pushTime +
				",toUser=" + toUser +
				",paramName=" + paramName +
				",msgUrl=" + msgUrl +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
