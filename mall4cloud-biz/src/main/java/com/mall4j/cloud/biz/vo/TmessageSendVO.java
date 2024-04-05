package com.mall4j.cloud.biz.vo;

import lombok.Data;

/**
 * @Date 2022年2月8日, 0008 17:15
 * @Created by eury
 *
 *以下是发送模板内容：
 * {
 *   "appId": "wx50f6e5cf89ab8f87",
 *   "data": "{\"first\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"39.8元\",\"color\":\"#173177\"},\"keyword3\":{\"value\":\"2014年9月22日\",\"color\":\"#173177\"},\"remark\":{\"value\":\"欢迎再次购买！\",\"color\":\"#173177\"}}",
 *   "miniProgram": "{\"appid\":\"wx672369b73d337ba7\",\"pagepath\":\"pages/userCenter/userCenter\"}",
 *   "mpAppId": "wx672369b73d337ba7",
 *   "pagePath": "pages/userCenter/userCenter",
 *   "templateId": "7hrxAMDpX2ftZM6Y1XQWPGiexfpAV5rkKTNOz1qmhKY",
 *   "touser": "o-7fRvxTJnQFCWgO-9zdZiuaeNIA",
 *   "url": "",
 *   "tmessageId": "1644390991783011"
 * }
 *
 *
 */
@Data
public class TmessageSendVO {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private String miniProgram;

    /**
     * 所需跳转到的小程序appid
     */
    private String appId;

    /**
     * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
     */
    private String pagePath;

    /**
     * 模板数据
     */
    private String data;

    /**
     * 公众号mpAppId
     */
    private String mpAppId;

    /**
     * 本系统微信消息模板id
     */
    private String tmessageId;

}
