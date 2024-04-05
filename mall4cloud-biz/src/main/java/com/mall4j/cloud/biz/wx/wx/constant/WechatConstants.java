package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * @Date 2021年12月30日, 0030 14:59
 */
public class WechatConstants {

    public static final String WX_OPEN_INFO = "WX_OPEN_INFO";

    public static final long WXOPEN_EXPIRE_TIME = 10080 * 60 * 1000;

    public static final String TICKET_SUCCESS="success";
    public static final String FAILED="success";
    public static final String GET_USER_PHONE_NUMBER="https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN";

    //授权公众号头像上传根目录
    public static final String uploadDir = "upload/img/commonweixin";
    public static final String WEIXIN_DOMAIN = "http://mp.weixin.qq.com";
    //拼接-扫码授权链接
    public static final String QRLink = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI&auth_type=AUTH_TYPE";
    //启动ticket推送服务
    public static final String API_START_PUSH_TICKET = "https://api.weixin.qq.com/cgi-bin/component/api_start_push_ticket";
    //获取令牌,第三方平台接口的调用凭据
    public static final String COMPONENT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    public static final String PRE_AUTH_CODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
    public static final String AUTHORIZER_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=COMPONENT_ACCESS_TOKEN";
    public static final String AUTHORIZER_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";
    public static final String AUTHORIZER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";
    public static final String API_GET_AUTHORIZER_INFO="https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
    //发送模板消息
    public static String SEND_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";


    public class WxConfigConstants {

        public static final String MP_SUBSCRIBE_OPEN="MP_SUBSCRIBE_OPEN";
        public static final String MP_SUBSCRIBE_STORE_OPEN="MP_SUBSCRIBE_STORE_OPEN";
        public static final String MP_AUTO_MSG_OPEN="MP_AUTO_MSG_OPEN";

    }
}
