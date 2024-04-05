package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * VO
 *
 * @author lhd
 * @date 2021-01-04 09:32:38
 */
public class NotifyTemplateVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息模板表")
    private Long templateId;

    @ApiModelProperty("1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒  6.发货提醒  7.拼团失败提醒 8.拼团成功提醒 " +
            "9.拼团开团提醒 10.会员升级提醒11.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒")
    private Integer sendType;

    @ApiModelProperty("通知内容")
    private String message;

    @ApiModelProperty("通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息")
    private String notifyTypes;

    @ApiModelProperty("公众号消息模板code")
    private String mpCode;

    @ApiModelProperty("消息发送类型 1.平台自行发送类型 2.商家")
    private Integer msgType;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;

    @ApiModelProperty("短信模板code")
    private String templateCode;

    /**
     * 可用通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息
     */
    private List<Integer> templateTypeList;
    /**
     * 可用通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息
     */
    private Boolean sms;

    private Boolean app;

    private Boolean sub;

    /** 模板关联的会员标签id */
    private List<Long> tagIds;

    public List<Integer> getTemplateTypeList() {
        return templateTypeList;
    }

    public void setTemplateTypeList(List<Integer> templateTypeList) {
        this.templateTypeList = templateTypeList;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getApp() {
        return app;
    }

    public void setApp(Boolean app) {
        this.app = app;
    }

    public Boolean getSub() {
        return sub;
    }

    public void setSub(Boolean sub) {
        this.sub = sub;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotifyTypes() {
        return notifyTypes;
    }

    public void setNotifyTypes(String notifyTypes) {
        this.notifyTypes = notifyTypes;
    }

    public String getMpCode() {
        return mpCode;
    }

    public void setMpCode(String mpCode) {
        this.mpCode = mpCode;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    @Override
    public String toString() {
        return "NotifyTemplateVO{" +
                "templateId=" + templateId +
                ", sendType=" + sendType +
                ", message='" + message + '\'' +
                ", notifyTypes='" + notifyTypes + '\'' +
                ", mpCode='" + mpCode + '\'' +
                ", msgType=" + msgType +
                ", status=" + status +
                ", templateCode='" + templateCode + '\'' +
                ", sms=" + sms +
                ", app=" + app +
                ", sub=" + sub +
                ", tagIds=" + tagIds +
                '}';
    }
}
