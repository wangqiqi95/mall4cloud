package com.mall4j.cloud.biz.task;

import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.biz.wx.cp.constant.AssignTypeEunm;
import com.mall4j.cloud.api.biz.constant.cp.NotityTypeEunm;
import com.mall4j.cloud.biz.wx.cp.constant.PushStatusEunm;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 企业微信相关定时任务
 * @author hwy
 */
@Component("cpTask")
@RequiredArgsConstructor
@Slf4j
public class CpTask {
    private final  String REDIS_MSG_TYPE = "config_notify_type:";
    private final ResignAssignLogService logService;
    private final TaskSendDetailService taskSendDetailService;
    private final NotifyConfigService notifyConfigService;
    private final SendTaskService sendTaskService;
    private  final WxCpPushService wxCpPushService;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;



    //---------------------------基础配置消息的任务-----------------------------------------------------
    /**
     * 基础配置消息的任务
     */
    @XxlJob("sendConfigNotifyMsgTask")
    public void sendConfigNotifyMsgTask()  {
        List<NotifyConfig> notifyConfigs = notifyConfigService.list();
        if(!CollectionUtils.isEmpty(notifyConfigs)){
            notifyConfigs.forEach(config->{
                //Integer nums  = RedisUtil.get(REDIS_MSG_TYPE+config.getId());
                //全部未注册的好友
                if(config.getMsgType()== NotityTypeEunm.UNREGISTER_MEMBER.getCode()){
                    /*if(checkTime(config.getPushTime())){
                        if(nums==null){
                            //RedisUtil.set(REDIS_MSG_TYPE+config.getId(),1,getExpireTimeSecond());

                        }
                    }*/
                    ServerResponseEntity<List<CountNotMemberUsersVO>> response =  userStaffCpRelationFeignClient.countNotMemberUsers();
                    if(response!=null && !CollectionUtils.isEmpty(response.getData())){
                        List<CountNotMemberUsersVO> list = response.getData();
                        list.forEach(item ->{
                            wxCpPushService.unRegisterMemberNotify(item.getNums(),item.getStaffId().toString());
                        });
                    }
                }
                //未添加好友的会员
                if(config.getMsgType()== NotityTypeEunm.UNADD_QW.getCode()){
                    ServerResponseEntity<List<CountNotMemberUsersVO>> response =  userStaffCpRelationFeignClient.countNotQiWeiUsers();
                    if(response!=null && !CollectionUtils.isEmpty(response.getData())){
                        List<CountNotMemberUsersVO> list = response.getData();
                        list.forEach(item ->{
                            wxCpPushService.unAddQwNotify(item.getNums(),item.getStaffId().toString());
                        });
                    }
                }

                //新加的未注册好友
                if(config.getMsgType()== NotityTypeEunm.INVITE_USER.getCode()){

                }
            });
        }
    }



    //---------------------------群发任务的消息发送定时任务-----------------------------------------------------
    /**
     * 群发任务的消息发送定时任务
     */
    @XxlJob("sendGroupTask")
    public void sendGroupTask()  {
        List<TaskSendDetail> list  =  taskSendDetailService.listSendTask();
        if(!CollectionUtils.isEmpty(list)){
            for (TaskSendDetail detail : list) {
                sendTaskService.send(detail);
            }
        }
    }


    //---------------------------客户接替定时任务-----------------------------------------------------
    /**
     * 在职户接替状态查询定时任务
     */
    @XxlJob("custExtendStatusQueryTask")
    public void custExtendStatusQueryTask() {
        log.info("==============在职户接替状态查询定时任务 开始===================");
        long start=new Date().getTime();
        List<ResignAssignLog> logs = logService.sycnCustList(PushStatusEunm.SUCCESS);
        if(!CollectionUtils.isEmpty(logs)){
            for (ResignAssignLog assignLog : logs) {
                try {
                    if(AssignTypeEunm.CUST.getCode()== assignLog.getAssignType()){
                        logService.custReplayResult(assignLog,AssignTypeEunm.CUST);
                    }
                    if(AssignTypeEunm.DIS_CUST.getCode()== assignLog.getAssignType()){
                        logService.custReplayResult(assignLog,AssignTypeEunm.DIS_CUST);
                    }
                }catch (Exception e){log.error("",e);}
            }
        }
        long end=new Date().getTime();
        log.info("==============在职户接替状态查询定时任务 结束=================== 耗时：{}", ((end - start) / 1000.0) );
    }
    //---------------------------客户离职时任务-----------------------------------------------------
    /**
     * 客户继承定时任务
     */
    @XxlJob("staffExtendsTask")
    public void staffExtendsTask()  {
        log.info("==============客户继承定时任务 开始===================");
        List<ResignAssignLog> logs = logService.sycnCustList(PushStatusEunm.CREATE);
        long start=new Date().getTime();
        if(!CollectionUtils.isEmpty(logs)){
            for (ResignAssignLog assignLog : logs) {
                try {
                    if(AssignTypeEunm.CUST.getCode()== assignLog.getAssignType()){
                        logService.custExtends(assignLog);
                    }
                    if(AssignTypeEunm.DIS_CUST.getCode()== assignLog.getAssignType()){
                        logService.custDimissionExtends(assignLog);
                    }
                    if(AssignTypeEunm.DIS_GROUP.getCode()== assignLog.getAssignType()){
                        logService.groupDimissionExtends(assignLog);
                    }
                    if(AssignTypeEunm.CUST_GROUP.getCode()== assignLog.getAssignType()){
                        logService.groupDimissionExtends(assignLog);
//                        logService.groupExtends(assignLog);
                    }
                }catch (Exception e){log.error("",e);}
            }
        }
        long end=new Date().getTime();
        log.info("==============客群继承定时任务 结束=================== 耗时: {}", ((end - start) / 1000.0) );
    }

    @Async
    public void staffExtendsTaskAsync()  {
        this.staffExtendsTask();
    }


}

