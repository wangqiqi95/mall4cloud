package com.mall4j.cloud.api.biz.dto.cp;

import com.mall4j.cloud.api.biz.constant.cp.NotityTypeEunm;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class NotifyMsgTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private NotityTypeEunm typeEunm;

    private String qiWeiStaffId;

    /**
     * 好友关系表user_staff_cp_relation：id
     */
    private String userId;

    private TimeOut timeOut;
    private Hitkeyword hitkeyword;
    private Material material;
    private FollowUp followUp;
    private FollowLoss followLoss;
    private PhoneTask phoneTask;

    /**
     * 超时提醒
     */
    @Data
    @Builder
    public static class TimeOut{
        //超时时长
        String timeOutTime;
        //触发时长
        String offendOutTime;
        //触发人
        String offendUser;
        //会话对象：会话的客户昵称或群名称
        String userName;
    }

    /**
     * 命中关键词
     */
    @Data
    @Builder
    public static class Hitkeyword{
        //触发人
        String offendUser;
        //触发时长
        String offendOutTime;
        //标签
        String tag;
        //敏感词
        String mate;
    }

    /**
     * 素材浏览
     */
    @Data
    @Builder
    public static class Material{
        //客户昵称
        String nickName;
        //客户名称
        String userName;
        //素材名称
        String materialName;
        //浏览时长
        String browseTime;
        //标签
        String tag;
        //浏览时间
        String browseDate;
    }

    /**
     * 跟进提醒
     */
    @Data
    @Builder
    public static class FollowUp{
        //员工姓名
        String staffName;
        //客户昵称
        String nickName;
        //客户名称
        String userName;
        //创建时间
        String createDate;
    }

    /**
     * 好友流失
     */
    @Data
    @Builder
    public static class FollowLoss{
        //客户昵称
        String nickName;
        //客户名称
        String userName;
        //删除时间
        String deleteDate;
    }

    /**
     * 手机号任务
     */
    @Data
    @Builder
    public static class PhoneTask{
        //开始日期
        String startTime;
        //结束日期
        String endTime;
        //每日数量
        String number;
        Long taskId;
    }

}

