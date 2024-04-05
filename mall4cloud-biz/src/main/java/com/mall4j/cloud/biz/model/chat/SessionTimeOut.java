package com.mall4j.cloud.biz.model.chat;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话超时规则表
 */
public class SessionTimeOut extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键id
    private Long id;
    //名称
    private String name;
    //适用人员
    private String suitPeople;
    //场景类型
    private String type;
    //上班日期 1：周一，2：周二，3：周三，4：周四，5：周五，6：周六，7：周日,多选用,分开
    private String workDate;
    //上班开始时间
    private String workStartDate;
    //上班结束时间
    private String workEndDate;
    //超时时效
    private String timeOut;
    //正常结束语是否开启0：关闭，1：开启
    private Integer normalEnd;
    //正常结束语
    private String conclusion;
    //通知提醒开启0：关闭，1：开启
    private Integer remind;
    //提醒当事人或员工
    private String remindPeople;
    //提醒时间0：立即上报，1：指定时间
    private Integer remindOpen;
    //延迟上报时间，以小时为单位
    private String remindTime;
    private String staff;
    private String suitPeopleName;

    private String createName;

    private String staffName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuitPeople() {
        return suitPeople;
    }

    public void setSuitPeople(String suitPeople) {
        this.suitPeople = suitPeople;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getWorkStartDate() {
        return workStartDate;
    }

    public void setWorkStartDate(String workStartDate) {
        this.workStartDate = workStartDate;
    }

    public String getWorkEndDate() {
        return workEndDate;
    }

    public void setWorkEndDate(String workEndDate) {
        this.workEndDate = workEndDate;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getNormalEnd() {
        return normalEnd;
    }

    public void setNormalEnd(Integer normalEnd) {
        this.normalEnd = normalEnd;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Integer getRemind() {
        return remind;
    }

    public void setRemind(Integer remind) {
        this.remind = remind;
    }

    public String getRemindPeople() {
        return remindPeople;
    }

    public void setRemindPeople(String remindPeople) {
        this.remindPeople = remindPeople;
    }

    public Integer getRemindOpen() {
        return remindOpen;
    }

    public void setRemindOpen(Integer remindOpen) {
        this.remindOpen = remindOpen;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getSuitPeopleName() {
        return suitPeopleName;
    }

    public void setSuitPeopleName(String suitPeopleName) {
        this.suitPeopleName = suitPeopleName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Override
    public String toString() {
        return "SessionTimeOut{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", suitPeople='" + suitPeople + '\'' +
                ", type=" + type +
                ", workDate='" + workDate + '\'' +
                ", workStartDate=" + workStartDate +
                ", workEndDate=" + workEndDate +
                ", timeOut='" + timeOut + '\'' +
                ", normalEnd=" + normalEnd +
                ", conclusion='" + conclusion + '\'' +
                ", remind=" + remind +
                ", remindPeople='" + remindPeople + '\'' +
                ", remindOpen=" + remindOpen +
                ", remindTime='" + remindTime + '\'' +
                '}';
    }
}
