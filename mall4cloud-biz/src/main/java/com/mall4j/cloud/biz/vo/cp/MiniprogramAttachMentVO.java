package com.mall4j.cloud.biz.vo.cp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-01-24 17:09
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MiniprogramAttachMentVO extends AttachMentVO {
    private Miniprogram  miniprogram;

    @Data
    @Accessors(chain = true)
    public static class Miniprogram{
        private String  title;
        private String  picUrl;
        private String  page;
        private String  appid;


    }
}
