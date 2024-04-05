package com.mall4j.cloud.user.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.mall4j.cloud.common.model.ExcelModel;

import java.util.Date;

/**
 * 用户导入信息
 * @author cl
 * @date 2021-05-12 10:13:29
 */
public class UserExcelDTO extends ExcelModel {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "导入用户信息模板";
    public static final String SHEET_NAME = "sheet";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 1;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"序号"}, index = 0)
    private String seq;

    @ExcelProperty(value = {"手机号(必填)"}, index = 1)
    @ColumnWidth(20)
    private String phone;

    @ExcelProperty(value = {"密码(必填)"}, index = 2)
    @ColumnWidth(16)
    private String password;

    @ExcelProperty(value = {"会员类型(0普通会员/1付费会员,必填)"}, index = 3)
    @ColumnWidth(15)
    private Integer levelType;

    @ExcelProperty(value = {"付费会员等级(付费会员必填，普通会员以成长值为准)"}, index = 4)
    @ColumnWidth(15)
    private Integer level;

    /**
     *  注解@ContentStyle(dataFormat = 49) 设置单元格格式为文本格式
     *  只会将对象属性所在单元格设置为文本格式，所在列的其他单元格还是为自定义格式
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"vip结束时间(格式:2021-08-08 14:32:01，会员类型是1则必填)"}, index = 5)
    @ColumnWidth(25)
    private Date vipEndTime;

    @ExcelProperty(value = {"用户昵称(最多32个字，非纯数字)，必填"}, index = 6)
    @ColumnWidth(30)
    private String nickName;

    @ExcelProperty(value = {"用户名(由数字字母下划线4-16位组成)，必填"}, index = 7)
    @ColumnWidth(20)
    private String userName;

    @ExcelProperty(value = {"邮箱"}, index = 8)
    @ColumnWidth(20)
    private String email;

    @ExcelProperty(value = {"性别(0男/1女)"}, index = 9)
    private String sex;

    @ExcelProperty(value = {"生日(格式:2009-11-27)"}, index = 10)
    @ColumnWidth(20)
    private String birthDate;

    @ExcelProperty(value = {"积分(限整数)"}, index = 11)
    private String score;

    @ExcelProperty(value = {"余额(限整数)"}, index = 12)
    private String balance;

    @ExcelProperty(value = {"成长值(限整数)"}, index = 13)
    private String growth;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(Date vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserExcelDTO{" +
                "seq='" + seq + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", levelType=" + levelType +
                ", level=" + level +
                ", vipEndTime=" + vipEndTime +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", score='" + score + '\'' +
                ", balance='" + balance + '\'' +
                ", growth='" + growth + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
