package com.mall4j.cloud.biz.model.channels;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 视频号4.0 类目
 *
 * @author FrozenWatermelon
 * @date 2023-02-15 16:01:16
 */
@Data
public class ChannelsCategory extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审核单id
     */
    private Long auditId;

    /**
     * 等级1
     */
    private Long level1;

    /**
     * 等级1
     */
    private String name1;

    /**
     * 等级2
     */
    private Long level2;

    /**
     * 等级2
     */
    private String name2;

    /**
     * 等级3
     */
    private Long level3;

    /**
     * 等级3
     */
    private String name3;

    /**
     * 审核状态, 1：审核中，3：审核成功，2：审核拒绝，12：主动取消申请单
     */
    private Integer status;

    /**
     * 如果审核拒绝，返回拒绝原因
     */
    private String rejectReason;

    /**
     * 资质材料，图片fileid，图片类型，最多不超过10张
     */
    private String certificate;

    /**
     * 报备函，图片fileid，图片类型，最多不超过10张
     */
    private String baobeihan;

    /**
     * 经营证明，图片fileid，图片类型，最多不超过10张
     */
    private String jingyingzhengming;

    /**
     * 带货口碑，图片fileid，图片类型，最多不超过10张
     */
    private String daihuokoubei;

    /**
     * 入住资质，图片fileid，图片类型，最多不超过10张
     */
    private String ruzhuzhizhi;

    /**
     * 经营流水，图片fileid，图片类型，最多不超过10张
     */
    private String jingyingliushui;

    /**
     * 补充材料，图片fileid，图片类型，最多不超过10张
     */
    private String buchongcailiao;

    /**
     * 经营平台，仅支持taobao，jd，douyin，kuaishou，pdd，other这些取值
     */
    private String jingyingpingtai;

    /**
     * 账号名称
     */
    private String zhanghaomingcheng;

    private String certificateUrl;
    private String baobeihanUrl;
    private String jingyingzhengmingUrl;
    private String daihuokoubeiUrl;
    private String ruzhuzhizhiUrl;
    private String jingyingliushuiUrl;
    private String buchongcailiaoUrl;
}
