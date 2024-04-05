package com.mall4j.cloud.biz.model.chat;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 命中关键词表
 *
 */
public class KeyWordHit extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 敏感词
     */
    private String sensitives;
    /**
     * 匹配词
     */
    private String mate;
    /**
     *触发人
     */
    private String trigger;
    /**
     * 类型 1:1对1 0:社群
     */
    private Integer type;
    /**
     * 标签
     */
    private String label;
    /**
     * 提醒员工
     */
    private String staff;
    /**
     * 员工id
     */
    private String staffId;
    /**
     * 触发人id
     */
    private String triggerId;
    private Long keyId;
    private String hitName;

    private Long hitTime;
    /**
     * 触发时间
     */
    private Date triggerTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensitives() {
        return sensitives;
    }

    public void setSensitives(String sensitives) {
        this.sensitives = sensitives;
    }

    public String getMate() {
        return mate;
    }

    public void setMate(String mate) {
        this.mate = mate;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getHitName() {
        return hitName;
    }

    public void setHitName(String hitName) {
        this.hitName = hitName;
    }

    public Long getHitTime() {
        return hitTime;
    }

    public void setHitTime(Long hitTime) {
        this.hitTime = hitTime;
    }

    @Override
    public String toString() {
        return "KeyWordHit{" +
                "id='" + id + '\'' +
                ", sensitives='" + sensitives + '\'' +
                ", mate='" + mate + '\'' +
                ", trigger='" + trigger + '\'' +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                ", staff='" + staff + '\'' +
                '}';
    }
}
