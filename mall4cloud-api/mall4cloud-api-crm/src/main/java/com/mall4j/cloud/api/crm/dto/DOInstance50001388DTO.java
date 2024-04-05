package com.mall4j.cloud.api.crm.dto;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据类型 data.offer.Instance50001 的表
 *
 * @author FrozenWatermelon
 * @date 2022-08-19 10:09:58
 */
@Data
public class DOInstance50001388DTO extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 模板id
     */
    private String templateid;

    /**
     * 项目逻辑id
     */
    private String projectentityid;

    /**
	 * 状态
     * GRANTED--已发放（未生效）
	 * ACTIVATED--已激活
	 * INVALIDATED--已失效
	 * VERIFICATED--已核销
	 * FROZEN--冻结中
     */
    private String status;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date edited;

    /**
     * 客户id
     */
    private String customerid;

    /**
     * 项目名称
     */
    private String projectname;

    /**
     * 反核销次数
     */
    private Long revocationtimes;

    /**
     * 发放时间
     */
    private Date granttime;

    /**
     * 券码
     */
    private String couponcode;

    /**
     * 预计生效时间
     */
    private Date expectedeffecttime;

    /**
     * 生效时间
     */
    private Date effecttime;

    /**
     * 预计失效时间
     */
    private Date expectedexpiredtime;

    /**
     * 失效时间
     */
    private Date expiredtime;

    /**
     * 核销时间
     */
    private Date verificationtime;

    /**
     * 反核销时间
     */
    private Date revocationtime;

    /**
     * 本次发放数量
     */
    private Long grantcountthistime;

    /**
     * 优惠金额
     */
    private Double discountamount;

    /**
     * 使用效果类型
     */
    private String applyeffecttype;

    /**
     * 兑换商品
     */
    private String exchangecommodity;

    /**
     * 方案entityid
     */
    private String programentityid;

    /**
     * 批量发放请求id
     */
    private String requestid;

    /**
     * 批次id
     */
    private String batchid;

    /**
     * 批量发放请求生成序号
     */
    private Long offset;

    /**
     * 方案名称
     */
    private String programname;

    /**
     * 模板名称
     */
    private String templatename;

    /**
     * 对应主体id
     */
    private String subjectentityid;

    /**
     * 主体映射字段值
     */
    private String mappingvalue;

    /**
     * 客体映射字段值
     */
    private String objectmappingvalue;

    /**
     * 当前项目
     */
    private String currentproject;

    /**
     * 绑定时间
     */
    private Date bindtime;

    /**
     * 解绑时间
     */
    private Date unbindtime;

    /**
     * 核准时间
     */
    private Date checkedtime;

    /**
     * 修改时间
     */
    private Date modifiedtime;

    /**
     * 全局事务id
     */
    private String txid;

    /**
     * 发放原因
     */
    private String grantreason;

    /**
     *
     */
    private String subject;

    /**
     * 类型
     */
    private String grantchannelname;

    /**
     * 类型ID
     */
    private String grantchannelid;

    /**
     * 主订单id
     */
    private String orderid;

    /**
     * 使用渠道id
     */
    private String usechannelid;

    /**
     * 使用渠道名称
     */
    private String usechannelname;

    /**
     * 使用时间类型
     */
    private Date usetime;

    /**
     * 数据类型 data.offer.Instance50001 字段 cashier 的列
     */
    private String cashier;

    /**
     * 优惠券使用类型
     */
    private String costcentertype;

    /**
     * 数据类型 data.offer.Instance50001 字段 couponDenomination 的列
     */
    private String coupondenomination;

    /**
     * 数据类型 data.offer.Instance50001 字段 projectId 的列
     */
    private String projectid;

    /**
     * 数据类型 data.offer.Instance50001 字段 object 的列
     */
    private String object;

    /**
     * 使用时间类型
     */
    private String applytimetype;

    /**
     * 订单最低消费金额
     */
    private Double minimumorderamount;


}
