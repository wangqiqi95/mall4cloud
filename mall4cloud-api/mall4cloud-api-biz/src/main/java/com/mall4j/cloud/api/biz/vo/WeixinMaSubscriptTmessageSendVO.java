package com.mall4j.cloud.api.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-03-05 4:50 PM
 **/
@Data
public class WeixinMaSubscriptTmessageSendVO {

    // 用户订阅记录id  通过这个id修改用户的下单状态。
    private Long userSubscriptRecordId;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面.
     * <pre>
     * 参数：page
     * 是否必填： 否
     * 描述： 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * </pre>
     */
    private String page;

    /**
     * 模板内容，不填则下发空模板.
     * <pre>
     * 参数：data
     * 是否必填： 是
     * 描述： 模板内容，不填则下发空模板
     * </pre>
     */
    private List<MsgData> data;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private String value;
    }
}
