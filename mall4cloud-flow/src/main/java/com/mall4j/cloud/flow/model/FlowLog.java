package com.mall4j.cloud.flow.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户流量记录
 *
 * @author YXF
 * @date 2020-07-13 13:18:33
 */
public class FlowLog implements Serializable{

    /**
     * 会话uuid
     */
    private String uuid;
    /**
     * uuid
     */
    private String uuidSession;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;
    /**
     * 用户登陆ip
     */
    private String ip;
    /**
     * 页面id
     */
    private Integer pageId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 1:页面访问  2:分享访问  3:页面点击 4:加购
     */
    private Integer visitType;
    /**
     * 业务数据（商品页：商品id;支付界面：订单编号数组；支付成功界面：订单编号数组）
     */
    private String bizData;

    /**
     * 业务数据（商品页：商品类型）
     */
    private String bizType;
    /**
     * 用户操作步骤数
     */
    private Integer step;
    /**
     * 用户操作数量
     */
    private Integer nums;

    /**
     * 页面结束时间（跳转页面时间）
     */
    private Date endTime;

    /**
     * 用户下一操作页面编号
     */
    private Integer nextPageId;
    /**
     * 用户下一操作页面编号
     */
    private Long stopTime;

    /**
     * 小时（该数据为第几小时的）
     */
    private Integer hour;

}
