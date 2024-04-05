package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 公众号事件推送日志VO
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:16
 */
@Data
public class WeixinActoinLogsVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("开发者微信号")
    private String toUserName;

    @ApiModelProperty("发送方账号(openId)")
    private String fromUserName;

    @ApiModelProperty("消息创建时间")
    private Date putTime;

    @ApiModelProperty("消息类型(文本:text/图片:image/语音:voice/视频:video/小视频:shortvideo/地理位置:location/链接:link/事件:event)")
    private String msgType;

    @ApiModelProperty("文本消息内容")
    private String content;

    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("图片链接")
    private String picUrl;

    @ApiModelProperty("消息媒体id")
    private String mediaId;

    @ApiModelProperty("消息缩略图媒体id")
    private String thumbmediaid;

    @ApiModelProperty("语音格式")
    private String format;

    @ApiModelProperty("地理位置纬度")
    private String locatioinX;

    @ApiModelProperty("地理位置经度")
    private String locatioinY;

    @ApiModelProperty("地理位置经度")
    private String precision;

    @ApiModelProperty("地图缩放大小")
    private String scale;

    @ApiModelProperty("地理位置信息")
    private String label;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("语音识别结果")
    private String recognition;

    @ApiModelProperty("消息描述")
    private String description;

    @ApiModelProperty("消息链接")
    private String url;

    @ApiModelProperty("事件类型：subscribe:订阅/unsubscribe:取消订阅/SCAN:扫码/LOCATION:上报地理位置/CLICK/VIEW")
    private String event;

    @ApiModelProperty("事件key值")
    private String eventKey;

    @ApiModelProperty("二维码ticket")
    private String ticket;
}
