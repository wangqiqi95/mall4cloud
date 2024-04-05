package com.mall4j.cloud.common.security.config;

import com.mall4j.cloud.common.constant.Auth;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.security.adapter.AuthConfigAdapter;

import java.util.HashSet;

/**
 * @author Citrus
 * @date 2021/8/6 14:32
 */
public class SensitiveWordConfig {

    private static final String SENSITIVE_WORD_CONFIG_URL = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/config/getConfig";
    private static final String SENSITIVE_WORD_SEARCH_URL = "/p/sys_config/info/SENSITIVE_WORDS";
    private static final String GET_CAPTCHA = "/ua/captcha/get";
    private static final String CHECK_CAPTCHA = "/ua/captcha/check";
    private static final String SAVE_SENSITIVE_WORD = "/p/sys_config/save/";
    private static final String FEIGN_URL = "/feign/**";

    private static final String PAY_NOTICE = "/ua/notice/pay/**";

    private static final String REFUND_NOTICE = "/ua/notice/refund/**";
//    private static final String FEIGN_URL = "/feign/**";
    /**
     * excel
     */
    private static final String P_RECOMMEND_SPU_EXCEL = "/p/distribution_recommend_spu/export";
    private static final String P_SOLD_EXCEL = "/p/order/sold_excel";
    private static final String P_ORDER_REFUND_SOLD_EXCEL = "/p/order_refund/sold_excel";
    private static final String MP_SOLD_ORDER_EXCEL = "/mp/order/sold_excel";
    private static final String UN_DELIVERY_SOLD_EXCEL = "/mp/order/un_delivery_sold_excel";
    private static final String MP_SOLD_PROD_EXCEL = "/mp/spu/sold_excel";
    private static final String MP_DOWN_MODEL = "/mp/spu/down_model";
    private static final String P_SOLD_USER_EXCEL = "/p/user/sold_excel";
    private static final String P_USER_DOWN_MODEL = "/p/user/down_model";
    private static final String P_COMMISSION_DOWN_MODEL = "/p/distribution_commission_account/sold_excel";
    private static final String P_SOLD_STORE = "/p/store/sold_excel";
    private static final String SIGN_EXCEL = "/mp/sign_activity/normal_detail/export";
    private static final String LOTTERY_DRAW_EXCEL = "/mp/lottery_draw/award/record/export";
    private static final String DISTRIBUTION_ORDER_EXCEL = "/p/distribution_order/exportDistributionOrder";
    private static final String WITHDRAW_RECORD_EXCEL = "/p/distribution_withdraw_record/withdrawRecordExcel";
    //test
    private static final String TEST_SOLD_EXCEL_UPLOAD = "/p/excel/upload/sold_excel";
    /**
     * 小程序回调消息
     */
    private static final String WECAT_LIVESTORE_NOTIFY = "/ua/livestore/notify/*";
    /**
     * 视频号小店回调消息
     */
    private static final String WECAT_CHANNELS_NOTIFY = "/ua/channels/notify/*";
    /**
     * 权限
     */
    private static final String MENU_PERMISSION_USER_LIST = "/mp/menu_permission/list";
    private static final String MENU_PERMISSION_USER_PAGE = "/mp/menu_permission/page";

    /**
     * 二维码
     */
    private static final String MINI_QR_CODE = "/ua/qrcode_ticket/mini_qrcode";
    /**
     * 微信开发平台
     */
    private static final String WECHAT_OP = "/ua/wechat/op/*";
    private static final String P_SOLD_SCAN_RECS= "/p/weixin/autoscan/soldScanRecs";
    private static final String P_DOWN_WEIXIN_QRCODE= "/p/weixin/qrcode/downQrcode";
    private static final String P_DOWN_WEIXIN_QRCODE_ALL= "/p/weixin/qrcode/downQrcodeAll";

    /**
     * 员工信息
     */
    private static final String P_STAFF_SOLD_STAFFS= "/p/staff/soldStaffs";

    /**
     * 企业微信二维码
     */
    private static final String STAFF_QR_CODE_SIMPLE = "/p/cp/simple/staff_code/download/qrcode";
    private static final String STAFF_QR_CODE_PLUS = "/p/cp/plus/staff_code/download/qrcode";
    private static final String STAFF_QR_CODE_ZIP_SIMPLE = "/p/cp/simple/staff_code/download/batchQrcode";
    private static final String STAFF_QR_CODE_ZIP_PLUS = "/p/cp/plus/staff_code/download/batchQrcode";

    private static final String GROUP_QR_CODE = "/p/cp/group/code/download/qrcode";
    private static final String GROUP_QR_QRCODETEST = "/p/cp/group/code/download/qrcodetest";
    private static final String GROUP_QR_CODE_ZIP = "/p/cp/group/code/download/batchQrcode";

    private static final String AUTO_GROUP_QR_CODE_ZIP = "/p/cp/auto/group/code/download/batchQrcode";
    private static final String AUTO_GROUP_QR_CODE = "/p/cp/auto/group/code/download/qrcode";
    /**
     * 商品评论下载
     */
    private static final String P_PRODUCT_DOWNLOAD_SPUCOMM_EXCEL = "/p/spucomm/spu_comm_excel";

    /**
     * 微信小程序
     */
    private static final String UA_DOWNLOAD_WXA_CODE = "/ua/qrcode_ticket/downloadWxaCode";

    /**
     * 门店邀请码
     */
    private static final String P_STORE_DOWNLOAD_INVITE_QR_CODE = "/p/store/downloadInviteQrCode";

    /**
     * 微客导出
     */
    private static final String P_VEEKER_EXPORT = "/p/veeker/sold_excel";

    /**
     * 好友统计导出
     */
    private static final String USER_COUNT_EXPORTTOPDATA = "/p/user_count/exportTopData";
    private static final String USER_COUNT_EXPORTLIST = "/p/user_count/exportList";

    private static final String EXPORTANALYZEUSRF = "/p/cp/analayze/staff/code/exportAnalyzeUSRF";
    private static final String PHONE_TASK_USER_EXPORTUSER = "/p/cp/phone/task/user/exportUser";

    /**
     * 门店活动-报名用户导出
     */
    private static final String P_DISTRIBUTION_STORE_ACTIVITY_USER = "/p/distribution_store_activity_user/export";

    private static final String P_CHOOSE_MEMBER_EVENT_STATISTICS = "/p/choose/member/event/export/statistics";

    private static final String CHANNELS_FILE_MEDIA_GET = "/ua/channels/file/media/get";

    private static final String CHANNELS_SPU_EXPORT = "/ua/channels/spu/export";

    private static final String ACTUATOR_URI = "/actuator/**";

    private static final String QUESTIONNAIRE_USER_EXPORT = "/mp/questionnaire/export_user";

    private static final String MOMENTS_SENDRECORD = "/p/distribution_moments/sendRecord/sold_excel";

    private static final String EXPORT_MATERIAL_RECORD_USER = "/p/cp/meterial/record/soldUser";
    private static final String EXPORT_MATERIAL_BROWSE_USER = "/p/cp/meterial/browse/soldBrowse";
    private static final String EXPORT_WELCOME_BROWSE_USER = "/p/cp/welcome/record/soldUser";
    private static final String CP_CHAT_SCRIPT_SOLDUSEPAGE = "/p/cp_chat_script/soldUsePage";
    private static final String KEYWORDHIT_SOLDEXCEL = "/p/keywordHit/soldExcel";
    private static final String TIMEOUTLOG_SOLDEXCEL = "/p/timeOutLog/soldExcel";
    private static final String USER_GROUP_TASK_DETAIL_SOLDEXCEL = "/p/group/push/task/get/son/task/detail/soldExcel";
    private static final String CUST_GROUP_CUSTINFO_SOLDEXCEL = "/p/cp/cust_group/custInfo/soldExcel";
    private static final String CUST_SOLDGROUPEXCEL = "/p/cp/cust_group/soldGroupExcel";
    private static final String SEARCH_SOLDEXCEL_USERSESSION = "/p/search/soldExcelUserSession";
    private static final String SOLDSESSIONFILEEXCEL = "/p/search/soldSessionFileExcel";
    private static final String AGREE_SOLDEXCEL = "/p/agree/soldExcel";
    //客户管理导出
    private static final String SOLDEXCELUSERSTAFFREL = "/p/crm/user_/SoldExcelUserStaffRel";

    static final HashSet<String> SENSITIVE_WHITE_SET;
    static {
        SENSITIVE_WHITE_SET = new HashSet<>();
        SENSITIVE_WHITE_SET.add(Auth.CHECK_TOKEN_URI);
        SENSITIVE_WHITE_SET.add(Auth.CHECK_RBAC_URI);
        SENSITIVE_WHITE_SET.add(AuthConfigAdapter.DOC_URI);
        SENSITIVE_WHITE_SET.add(SENSITIVE_WORD_CONFIG_URL);
        SENSITIVE_WHITE_SET.add(P_RECOMMEND_SPU_EXCEL);
        SENSITIVE_WHITE_SET.add(SENSITIVE_WORD_SEARCH_URL);
        SENSITIVE_WHITE_SET.add(GET_CAPTCHA);
        SENSITIVE_WHITE_SET.add(CHECK_CAPTCHA);
        SENSITIVE_WHITE_SET.add(SAVE_SENSITIVE_WORD);
        SENSITIVE_WHITE_SET.add(P_SOLD_EXCEL);
        SENSITIVE_WHITE_SET.add(P_ORDER_REFUND_SOLD_EXCEL);
        SENSITIVE_WHITE_SET.add(MP_SOLD_ORDER_EXCEL);
        SENSITIVE_WHITE_SET.add(UN_DELIVERY_SOLD_EXCEL);
        SENSITIVE_WHITE_SET.add(MP_SOLD_PROD_EXCEL);
        SENSITIVE_WHITE_SET.add(MP_DOWN_MODEL);
        SENSITIVE_WHITE_SET.add(P_SOLD_USER_EXCEL);
        SENSITIVE_WHITE_SET.add(P_USER_DOWN_MODEL);
        SENSITIVE_WHITE_SET.add(MENU_PERMISSION_USER_LIST);
        SENSITIVE_WHITE_SET.add(MENU_PERMISSION_USER_PAGE);
        SENSITIVE_WHITE_SET.add(FEIGN_URL);
        SENSITIVE_WHITE_SET.add(MINI_QR_CODE);
        SENSITIVE_WHITE_SET.add(WECHAT_OP);
        SENSITIVE_WHITE_SET.add(P_SOLD_SCAN_RECS);
        SENSITIVE_WHITE_SET.add(P_DOWN_WEIXIN_QRCODE);
        SENSITIVE_WHITE_SET.add(P_DOWN_WEIXIN_QRCODE_ALL);

        SENSITIVE_WHITE_SET.add(STAFF_QR_CODE_PLUS);
        SENSITIVE_WHITE_SET.add(STAFF_QR_CODE_SIMPLE);
        SENSITIVE_WHITE_SET.add(STAFF_QR_CODE_ZIP_PLUS);
        SENSITIVE_WHITE_SET.add(STAFF_QR_CODE_ZIP_SIMPLE);
        SENSITIVE_WHITE_SET.add(GROUP_QR_CODE);
        SENSITIVE_WHITE_SET.add(GROUP_QR_QRCODETEST);
        SENSITIVE_WHITE_SET.add(GROUP_QR_CODE_ZIP);
        SENSITIVE_WHITE_SET.add(AUTO_GROUP_QR_CODE_ZIP);
        SENSITIVE_WHITE_SET.add(AUTO_GROUP_QR_CODE);
        SENSITIVE_WHITE_SET.add(UA_DOWNLOAD_WXA_CODE);
        SENSITIVE_WHITE_SET.add(P_STORE_DOWNLOAD_INVITE_QR_CODE);
        SENSITIVE_WHITE_SET.add(P_VEEKER_EXPORT);
        SENSITIVE_WHITE_SET.add(P_DISTRIBUTION_STORE_ACTIVITY_USER);
        SENSITIVE_WHITE_SET.add(P_COMMISSION_DOWN_MODEL);
        SENSITIVE_WHITE_SET.add(P_STAFF_SOLD_STAFFS);
        SENSITIVE_WHITE_SET.add(P_SOLD_STORE);
        SENSITIVE_WHITE_SET.add(SIGN_EXCEL);
        SENSITIVE_WHITE_SET.add(LOTTERY_DRAW_EXCEL);
        SENSITIVE_WHITE_SET.add(DISTRIBUTION_ORDER_EXCEL);
        SENSITIVE_WHITE_SET.add(P_PRODUCT_DOWNLOAD_SPUCOMM_EXCEL);
        SENSITIVE_WHITE_SET.add(WITHDRAW_RECORD_EXCEL);


        SENSITIVE_WHITE_SET.add(REFUND_NOTICE);
        SENSITIVE_WHITE_SET.add(PAY_NOTICE);

        SENSITIVE_WHITE_SET.add(WECAT_LIVESTORE_NOTIFY);
//        SENSITIVE_WHITE_SET.add(TEST_SOLD_EXCEL_UPLOAD);
        SENSITIVE_WHITE_SET.add(P_CHOOSE_MEMBER_EVENT_STATISTICS);
        SENSITIVE_WHITE_SET.add(ACTUATOR_URI);
        SENSITIVE_WHITE_SET.add(WECAT_CHANNELS_NOTIFY);
        SENSITIVE_WHITE_SET.add(CHANNELS_FILE_MEDIA_GET);
        SENSITIVE_WHITE_SET.add(CHANNELS_SPU_EXPORT);
        SENSITIVE_WHITE_SET.add(QUESTIONNAIRE_USER_EXPORT);
        SENSITIVE_WHITE_SET.add(MOMENTS_SENDRECORD);
        SENSITIVE_WHITE_SET.add(USER_COUNT_EXPORTTOPDATA);
        SENSITIVE_WHITE_SET.add(USER_COUNT_EXPORTLIST);
        SENSITIVE_WHITE_SET.add(EXPORTANALYZEUSRF);
        SENSITIVE_WHITE_SET.add(PHONE_TASK_USER_EXPORTUSER);
        SENSITIVE_WHITE_SET.add(EXPORT_MATERIAL_RECORD_USER);
        SENSITIVE_WHITE_SET.add(EXPORT_MATERIAL_BROWSE_USER);
        SENSITIVE_WHITE_SET.add(EXPORT_WELCOME_BROWSE_USER);
        SENSITIVE_WHITE_SET.add(CP_CHAT_SCRIPT_SOLDUSEPAGE);
        SENSITIVE_WHITE_SET.add(KEYWORDHIT_SOLDEXCEL);
        SENSITIVE_WHITE_SET.add(TIMEOUTLOG_SOLDEXCEL);
        SENSITIVE_WHITE_SET.add(USER_GROUP_TASK_DETAIL_SOLDEXCEL);
        SENSITIVE_WHITE_SET.add(CUST_GROUP_CUSTINFO_SOLDEXCEL);
        SENSITIVE_WHITE_SET.add(CUST_SOLDGROUPEXCEL);
        SENSITIVE_WHITE_SET.add(SEARCH_SOLDEXCEL_USERSESSION);
        SENSITIVE_WHITE_SET.add(SOLDSESSIONFILEEXCEL);
        SENSITIVE_WHITE_SET.add(AGREE_SOLDEXCEL);
        SENSITIVE_WHITE_SET.add(SOLDEXCELUSERSTAFFREL);
        SENSITIVE_WHITE_SET.add("/MP_verify_RvClmP7qvIkiYS78.txt");
//        SENSITIVE_WHITE_SET.add("/3844769369.txt");
//        SENSITIVE_WHITE_SET.add("/b4GJ3DxwCt.txt");

    }

    private SensitiveWordConfig() {

    }

    public static HashSet<String> getSensitiveWhiteSet() {
        return SENSITIVE_WHITE_SET;
    }
}
