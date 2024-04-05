package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 拼团队伍详情
 *
 * @author YXF
 * @date 2021/3/30
 */
public class AppGroupTeamInfoVO {

    @ApiModelProperty(value = "拼团单信息")
    private AppGroupTeamVO groupTeam;

    @ApiModelProperty(value = "拼团活动商品信息")
    private AppGroupActivityVO groupActivity;

    @ApiModelProperty(value = "参团用户列表")
    private List<AppGroupUserVO> groupUserList;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty("spu名称")
    private String spuName;

    @ApiModelProperty("spu卖点")
    private String sellingPoint;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty("售价，整数方式保存")
    private Long priceFee;

    public AppGroupActivityVO getGroupActivity() {
        return groupActivity;
    }

    public void setGroupActivity(AppGroupActivityVO groupActivity) {
        this.groupActivity = groupActivity;
    }

    public List<AppGroupUserVO> getGroupUserList() {
        return groupUserList;
    }

    public void setGroupUserList(List<AppGroupUserVO> groupUserList) {
        this.groupUserList = groupUserList;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getPriceFee() {
        return priceFee;
    }

    public void setPriceFee(Long priceFee) {
        this.priceFee = priceFee;
    }

    public AppGroupTeamVO getGroupTeam() {
        return groupTeam;
    }

    public void setGroupTeam(AppGroupTeamVO groupTeam) {
        this.groupTeam = groupTeam;
    }

    public String getSellingPoint() {
        return sellingPoint;
    }

    public void setSellingPoint(String sellingPoint) {
        this.sellingPoint = sellingPoint;
    }

    @Override
    public String toString() {
        return "AppGroupTeamInfoVO{" +
                "groupTeam=" + groupTeam +
                ", groupActivity=" + groupActivity +
                ", groupUserList=" + groupUserList +
                ", orderId=" + orderId +
                ", spuName='" + spuName + '\'' +
                ", sellingPoint='" + sellingPoint + '\'' +
                ", mainImgUrl='" + mainImgUrl + '\'' +
                ", priceFee=" + priceFee +
                '}';
    }
}
