package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StoreAddDTO {
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String stationName;

    /**
     * 门店图片
     */
    @ApiModelProperty("门店图片")
    private String pic;

    /**
     * 电话区号
     */
    @ApiModelProperty("电话区号")
    private String phonePrefix;

    /**
     * 手机/电话号码
     */
    @ApiModelProperty("手机/电话号码")
    private String phone;

    /**
     * 0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败
     */
    @ApiModelProperty("0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

    /**
     * 省id
     */
    @ApiModelProperty("省id")
    private Long provinceId;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;

    /**
     * 市id
     */
    @ApiModelProperty("市id")
    private Long cityId;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区id
     */
    @ApiModelProperty("区id")
    private Long areaId;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String area;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String addr;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Double lng;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Double lat;

    /**
     * 第三方门店编码
     */
    @ApiModelProperty("第三方门店编码")
    private String storeCode;

    @ApiModelProperty("c端是否展示 0-不展示 ，1 -展示")
    private Integer isShow;

    @ApiModelProperty("联系人")
    private String  linkman;

    @ApiModelProperty("首次营业开始时间")
    private Date firstStartTime;

    @ApiModelProperty("首次营业结束时间")
    private Date firstEndTime;

    @ApiModelProperty("门店类型1-自营，2-经销，3-代销，4-电商，5-其他")
    private Integer type;

    @ApiModelProperty(value = "门店类型：自营/经销/代销/电商")
    private String storenature;

    @ApiModelProperty("状态(目前仅优惠券适用门店过滤使用)")
    private String slcName;

    @ApiModelProperty("是否虚拟门店：0否 1是")
    private Integer storeInviteType;

    private String email;

    private String erpCode;

    /**
     * 片区
     */
    private String secondOrgName;

    /**
     * 店群
     */
    private String thirdOrgName;

    private String shortName;
}
