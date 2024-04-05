package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Data
@ApiModel("门店表")
public class TzStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private Long storeId;

    /**
     * 门店id
     */
	@ApiModelProperty("组织节点id")
    private Long orgId;

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
	private  String firstStartTime;

	@ApiModelProperty("首次营业结束时间")
	private String firstEndTime;

	@ApiModelProperty("门店类型：18-正品仓,20-虚拟仓,23-次品仓,27-退货仓,21-BUFFER仓,29-寄卖仓,32-经销店,34-自营店,26-过季仓,43-其他店,68-报废仓,25质检仓,19-客户仓,48-电商店,9-店铺（作废）,30-下单地址,31-结算仓,68-报废店,28-样板仓,53-配比仓,58-物料仓,63-代销店,33-代销仓,22-中转仓")
	private Integer type;

	@ApiModelProperty(value = "门店性质 自营/经销/代销/电商/其他")
	private String storenature;

	@ApiModelProperty("开启电子价签同步：0否 1是")
	private Integer pushElStatus;

	@ApiModelProperty("slc状态：全套-待客户反馈审批/转店-待SVP审批/关店-待Kids总经理审批/转店-待财务审批/无效的/全套-待经销商/商场反馈审批/平面-待客户反馈审批/已删除/已关店/待转店/转店-待登记ERP代码/待开店登记/待SVP审批/待关店/待提交全套图/平面-待客户审批/待分配设计师/待采购下单/转店-待总经理审批/全套-待区域设计经理审批/暂停营业/全套-待经销商审批/未通过/道具增补-待店筹审批/待登记ERP代码/待编制预算/关店-待SVP审批/平面-待区域设计经理审批/平面-待经销商/商场反馈审批/已转店/已暂停/待完善装修资料/待提交平面图/待验收完成/平面-待区域总监审批/平面-待经销商审批/关店-待省总审批/关店-待区总审批/待财务审批/草稿状态/关店-待AVP审批/运营中/平面-待区总审批/全套-待客户审批/开店申请-草稿(经销商)/关店-待财务审批/平面-陈列套餐变更调整-待审批/升级-待客户编辑")
	private String slcName;

	private String qwCode;

//    @ApiModelProperty("备注")
//    private String remark;

	private String email;

    @ApiModelProperty("开业时间")
    private Date openDay;

	@ApiModelProperty("邀请码状态 0-禁用 1-启用")
	private Integer inviteStatus;

	@ApiModelProperty("是否虚拟门店：0否 1是")
	private Integer storeInviteType;

	@TableField(exist = false)
	@ApiModelProperty("所属店群")
	private String  shopGroups;

	@TableField(exist = false)
	@ApiModelProperty("所属片区")
	private String  shopAres;

	@TableField(exist = false)
	private boolean  updateFlag;

	@ApiModelProperty("门店关联标签")
	@TableField(exist = false)
	private String  storeTagStr;

	@ApiModelProperty("门店关联标签总数")
	@TableField(exist = false)
	private Integer storeTagCount=0;

	@ApiModelProperty("组织节点简称")
	@TableField(exist = false)
	private String shortName;

	public String getSlcName() {
		return slcName;
	}

	public void setSlcName(String slcName) {
		this.slcName = slcName;
	}

	public Integer getPushElStatus() {
		return pushElStatus;
	}

	public void setPushElStatus(Integer pushElStatus) {
		this.pushElStatus = pushElStatus;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

    public String getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(String firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public String getFirstEndTime() {
        return firstEndTime;
    }

    public void setFirstEndTime(String firstEndTime) {
        this.firstEndTime = firstEndTime;
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getQwCode() {
		return qwCode;
	}

	public void setQwCode(String qwCode) {
		this.qwCode = qwCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPhonePrefix() {
		return phonePrefix;
	}

	public void setPhonePrefix(String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public Integer getInviteStatus() {
		return inviteStatus;
	}

	public void setInviteStatus(Integer inviteStatus) {
		this.inviteStatus = inviteStatus;
	}

	public String getStorenature() {
		return storenature;
	}

	public void setStorenature(String storenature) {
		this.storenature = storenature;
	}

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return "TzStore{" +
				"storeId=" + storeId +
				",orgId=" + orgId +
				",stationName=" + stationName +
				",pic=" + pic +
				",phonePrefix=" + phonePrefix +
				",phone=" + phone +
				",status=" + status +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",provinceId=" + provinceId +
				",province=" + province +
				",cityId=" + cityId +
				",city=" + city +
				",areaId=" + areaId +
				",area=" + area +
				",addr=" + addr +
				",lng=" + lng +
				",lat=" + lat +
				",storeCode=" + storeCode +
				'}';
	}
}
