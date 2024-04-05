package com.mall4j.cloud.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.model.ExcelModel;
import java.util.Date;

/**
 * 用户信息导出VO
 * @author cl
 * @date 2021-05-11 09:39:02
 */
public class UserExcelVO extends ExcelModel {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "用户信息";
    public static final String SHEET_NAME = "sheet";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"用户信息", "序号"}, index = 0)
    private String seq;

    @ExcelProperty(value = {"用户信息", "用户昵称"}, index = 1)
    private String nickName;

    @ExcelProperty(value = {"用户信息", "手机号码"}, index = 2)
    private String phone;

    @ExcelProperty(value = {"用户信息", "会员卡号"}, index = 3)
    private String vipcode;

    /** 0 普通会员 1 付费会员 SeckillSkuMapper.xml */
    @ExcelProperty(value = {"用户信息", "会员类型"}, index = 4)
    private Integer levelType;

    @ExcelProperty(value = {"用户信息", "会员等级"}, index = 5)
    private String levelName;

    @ExcelProperty(value = {"用户信息", "积分"}, index = 6)
    private Long score;

    @ExcelProperty(value = {"用户信息", "状态"}, index = 7)
    private Integer status;

    @ExcelProperty(value = {"用户信息", "开卡场景"}, index = 8)
    private String regSource;

    @ExcelProperty(value = {"用户信息", "服务导购"})
    private String staffName;

    @ExcelProperty(value = {"用户信息", "服务导购工号"})
    private String staffNameNo;

    @ExcelProperty(value = {"用户信息", "服务门店"})
    private String staffStoreName;

    @ExcelProperty(value = {"用户信息", "服务门店编码"})
    private String staffStoreCode;

    @ExcelProperty(value = {"用户信息", "注册门店"})
    private String storeName;

    @ExcelProperty(value = {"用户信息", "注册门店编码"})
    private String storeCode;

    @ExcelProperty(value = {"用户信息", "会员成长值"})
    private Long growth;

    // -----------------------------------------------------以上是user 的信息

    @ExcelProperty(value = {"用户信息", "消费金额"})
    private Double consAmount;

    @ExcelProperty(value = {"用户信息", "实付金额"})
    private Double actualAmount;

    @ExcelProperty(value = {"用户信息", "消费次数"})
    private Integer consTimes;

    @ExcelProperty(value = {"用户信息", "平均折扣"})
    private Double averDiscount;

    @ExcelProperty(value = {"用户信息", "充值金额"})
    private Double rechargeAmount;

    @ExcelProperty(value = {"用户信息", "充值次数"})
    private Integer rechargeTimes;

    @ExcelProperty(value = {"用户信息", "售后金额"})
    private Double afterSaleAmount;

    @ExcelProperty(value = {"用户信息", "售后次数"})
    private Integer afterSaleTimes;

    @ExcelProperty(value = {"用户信息", "当前积分"})
    private Long currentScore;

    @ExcelProperty(value = {"用户信息", "累积积分"})
    private Long sumScore;

    @ExcelProperty(value = {"用户信息", "当前余额"})
    private Double currentBalance;

    @ExcelProperty(value = {"用户信息", "累计余额"})
    private Double sumBalance;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"用户信息", "注册时间"})
    private Date createTime;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"用户信息", "最近消费时间"})
    private Date reConsTime;

    public String getStaffStoreCode() {
        return staffStoreCode;
    }

    public void setStaffStoreCode(String staffStoreCode) {
        this.staffStoreCode = staffStoreCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getGrowth() {
        return growth;
    }

    public void setGrowth(Long growth) {
        this.growth = growth;
    }

    public Double getConsAmount() {
        return consAmount;
    }

    public void setConsAmount(Double consAmount) {
        this.consAmount = consAmount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getConsTimes() {
        return consTimes;
    }

    public void setConsTimes(Integer consTimes) {
        this.consTimes = consTimes;
    }

    public Double getAverDiscount() {
        return averDiscount;
    }

    public void setAverDiscount(Double averDiscount) {
        this.averDiscount = averDiscount;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeTimes() {
        return rechargeTimes;
    }

    public void setRechargeTimes(Integer rechargeTimes) {
        this.rechargeTimes = rechargeTimes;
    }

    public Double getAfterSaleAmount() {
        return afterSaleAmount;
    }

    public void setAfterSaleAmount(Double afterSaleAmount) {
        this.afterSaleAmount = afterSaleAmount;
    }

    public Integer getAfterSaleTimes() {
        return afterSaleTimes;
    }

    public void setAfterSaleTimes(Integer afterSaleTimes) {
        this.afterSaleTimes = afterSaleTimes;
    }

    public Long getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Long currentScore) {
        this.currentScore = currentScore;
    }

    public Long getSumScore() {
        return sumScore;
    }

    public void setSumScore(Long sumScore) {
        this.sumScore = sumScore;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getSumBalance() {
        return sumBalance;
    }

    public void setSumBalance(Double sumBalance) {
        this.sumBalance = sumBalance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReConsTime() {
        return reConsTime;
    }

    public void setReConsTime(Date reConsTime) {
        this.reConsTime = reConsTime;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffStoreName() {
        return staffStoreName;
    }

    public void setStaffStoreName(String staffStoreName) {
        this.staffStoreName = staffStoreName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStaffNameNo() {
        return staffNameNo;
    }

    public void setStaffNameNo(String staffNameNo) {
        this.staffNameNo = staffNameNo;
    }

    public String getVipcode() {
        return vipcode;
    }

    public void setVipcode(String vipcode) {
        this.vipcode = vipcode;
    }

    @Override
    public String toString() {
        return "UserExcelVO{" +
                "seq='" + seq + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", levelType=" + levelType +
                ", levelName='" + levelName + '\'' +
                ", score=" + score +
                ", status=" + status +
                ", growth=" + growth +
                ", consAmount=" + consAmount +
                ", actualAmount=" + actualAmount +
                ", consTimes=" + consTimes +
                ", averDiscount=" + averDiscount +
                ", rechargeAmount=" + rechargeAmount +
                ", rechargeTimes=" + rechargeTimes +
                ", afterSaleAmount=" + afterSaleAmount +
                ", afterSaleTimes=" + afterSaleTimes +
                ", currentScore=" + currentScore +
                ", sumScore=" + sumScore +
                ", currentBalance=" + currentBalance +
                ", sumBalance=" + sumBalance +
                ", createTime=" + createTime +
                ", reConsTime=" + reConsTime +
                '}';
    }
}
