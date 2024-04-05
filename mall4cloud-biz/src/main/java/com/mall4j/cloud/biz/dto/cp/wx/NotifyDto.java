package com.mall4j.cloud.biz.dto.cp.wx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotifyDto implements Serializable {
    private String title;
    private String staffUserId;
    private String btnText;
    private String pushDate ;
    private String content;
    private String url;
    private  boolean emphasisFirstItem;
}
