package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 公众号事件推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:16
 */
@Data
public class WeixinActoinLogs extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 开发者微信号
     */
    private String toUserName;

    /**
     * 发送方账号(openId)
     */
    private String fromUserName;

    /**
     * 消息创建时间
     */
    private Date putTime;

    /**
     * 消息类型(文本:text/图片:image/语音:voice/视频:video/小视频:shortvideo/地理位置:location/链接:link/事件:event)
     */
    private String msgType;

    /**
     * 文本消息内容
     */
    private String content;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 图片链接
     */
    private String picUrl;

    /**
     * 消息媒体id
     */
    private String mediaId;

    /**
     * 消息缩略图媒体id
     */
    private String thumbmediaid;

    /**
     * 语音格式
     */
    private String format;

    /**
     * 地理位置纬度
     */
    private String locatioinX;

    /**
     * 地理位置经度
     */
    private String locatioinY;

    /**
     * 地理位置经度
     */
    private String precision;

    /**
     * 地图缩放大小
     */
    private String scale;

    /**
     * 地理位置信息
     */
    private String label;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 语音识别结果
     */
    private String recognition;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 消息链接
     */
    private String url;

    /**
     * 事件类型：subscribe:订阅/unsubscribe:取消订阅/SCAN:扫码/LOCATION:上报地理位置/CLICK/VIEW
     */
    private String event;

    /**
     * 事件key值
     */
    private String eventKey;

    /**
     * 二维码ticket
     */
    private String ticket;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;

	private String appId;


}
