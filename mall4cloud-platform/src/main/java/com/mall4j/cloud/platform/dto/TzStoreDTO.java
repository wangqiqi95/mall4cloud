package com.mall4j.cloud.platform.dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *·
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
public class TzStoreDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("服务门店id")
    private Long serviceStoreId;

    @ApiModelProperty("组织节点id")
    private Long orgId;

	@ApiModelProperty("父级节点")
	private Long parentOrgId;

    @ApiModelProperty("门店名称")
    private String stationName;

    @ApiModelProperty("门店图片")
    private String pic;

    @ApiModelProperty("电话区号")
    private String phonePrefix;

    @ApiModelProperty("手机/电话号码")
    private String phone;

    @ApiModelProperty("0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

    @ApiModelProperty("省id")
    private Long provinceId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市id")
    private Long cityId;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区id")
    private Long areaId;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("第三方门店编码")
    private String storeCode;

	@ApiModelProperty("简称")
	private String shortName;

	@ApiModelProperty("企微编码")
	private String qiWeiCode;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("联系人")
	private String linkman;

	@ApiModelProperty("手机号")
	private String mobile;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("1-品牌 ，2-片区，3-店群，4-门店")
	private Integer type;

    @ApiModelProperty("门店类型：18-正品仓,20-虚拟仓,23-次品仓,27-退货仓,21-BUFFER仓,29-寄卖仓,32-经销店,34-自营店,26-过季仓,43-其他店,68-报废仓,25质检仓,19-客户仓,48-电商店,9-店铺（作废）,30-下单地址,31-结算仓,68-报废店,28-样板仓,53-配比仓,58-物料仓,63-代销店,33-代销仓,22-中转仓")
    private Integer storeType;

    @ApiModelProperty(value = "门店性质 自营/经销/代销/电商/其他")
    private String storenature;

    @ApiModelProperty("开启电子价签同步：0否 1是")
    private Integer pushElStatus;

    @ApiModelProperty("邀请码状态 0-禁用 1-启用")
	private Integer inviteStatus;

    @ApiModelProperty("首次营业开始时间")
    private  String firstStartTime;

    @ApiModelProperty("首次营业结束时间")
    private String firstEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @ApiModelProperty("开业时间")
    private Date openDay;

    @ApiModelProperty("c端是否展示 0-不展示 ，1 -展示")
    private Integer isShow;
}
