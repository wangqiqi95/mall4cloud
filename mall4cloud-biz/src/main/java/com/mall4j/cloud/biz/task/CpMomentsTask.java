package com.mall4j.cloud.biz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.DistributionMomentsCommentsDTO;
import com.mall4j.cloud.biz.mapper.cp.DistributionMomentsMapper;
import com.mall4j.cloud.biz.mapper.cp.DistributionMomentsSendRecordMapper;
import com.mall4j.cloud.biz.model.cp.DistributionMoments;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsSendRecord;
import com.mall4j.cloud.biz.service.cp.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpGetMomentComments;
import me.chanjar.weixin.cp.bean.external.WxCpGetMomentTask;
import me.chanjar.weixin.cp.bean.external.WxCpGetMomentTaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CpMomentsTask {

    @Autowired
    private DistributionMomentsMapper distributionMomentsMapper;
    @Autowired
    private DistributionMomentsStoreService distributionMomentsStoreService;
    @Autowired
    private DistributionMomentsService distributionMomentsService;
    @Autowired
    WelcomeAttachmentService welcomeAttachmentService;
    @Autowired
    StaffFeignClient staffFeignClient;
    @Autowired
    DistributionMomentsSendRecordService distributionMomentsSendRecordService;
    @Autowired
    DistributionMomentsSendRecordMapper distributionMomentsSendRecordMapper;
    @Autowired
    private DistributionMomentsCommentsService distributionMomentsCommentsService;


    /**
     * 获取朋友圈任务创建结果
     */
    @XxlJob("getMomentTaskResult")
    public void getMomentTaskResult()  {
        log.info("==============获取朋友圈任务创建结果 开始===================");
        //查询待更新记录的状态
        List<DistributionMoments> moments = distributionMomentsMapper.getMomentTaskResult();

        for (DistributionMoments moment : moments) {
            WxCpGetMomentTaskResult result = null;
            try {
                result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                        .getMomentTaskResult(moment.getQwJobId());
            } catch (WxErrorException e) {
                e.printStackTrace();
                log.error("通过jobid获取朋友圈发送状态失败，参数:{},执行结果:{}", moment.getQwJobId(),JSONObject.toJSONString(result));
            } finally {
                log.error("通过jobid获取朋友圈执行结束，参数:{},执行结果:{}", moment.getQwJobId(),JSONObject.toJSONString(result));
            }

            if(result.getErrcode()==0 && result.getResult()!=null && StrUtil.isNotEmpty(result.getResult().getMomentId())){
                //修改朋友圈的id已经发送状态
                distributionMomentsMapper.updateTaskResult(moment.getId(),result.getResult().getMomentId(),JSONObject.toJSONString(result));

                List<Long> sendSccessIds = new ArrayList<>();
                List<DistributionMomentsSendRecord> sendRecords = distributionMomentsSendRecordMapper.getByMomentId(moment.getId());
                if(result.getResult().getInvalidSenderList()==null){
                    sendRecords.stream().map(DistributionMomentsSendRecord::getId).collect(Collectors.toList());
                }else{
                    //发送失败的记录
                    List<String> faildLists = result.getResult().getInvalidSenderList().getUserList();
                    //保留下发送成功的记录
                    sendSccessIds = sendRecords.stream().filter(s -> !faildLists.contains(s.getQiweiUserId()))
                            .map(DistributionMomentsSendRecord::getId).collect(Collectors.toList());
                }

                //更新朋友圈发送记录状态， 除了发送失败的记录外，其他全部修改状态为发送成功。
                if(CollUtil.isNotEmpty(sendSccessIds)){
                    distributionMomentsSendRecordMapper.sendSccess(moment.getId(),sendSccessIds);
                }
            }
        }
        log.info("==============获取朋友圈任务创建结果 结束===================");
    }


    /**
     * 获取员工朋友圈发表状态
     * https://developer.work.weixin.qq.com/document/path/93333#%E8%8E%B7%E5%8F%96%E5%AE%A2%E6%88%B7%E6%9C%8B%E5%8F%8B%E5%9C%88%E4%BC%81%E4%B8%9A%E5%8F%91%E8%A1%A8%E7%9A%84%E5%88%97%E8%A1%A8
     */
    @XxlJob("getMomentPublishStatus")
    public void getMomentPublishStatus()  {
        log.info("==============获取朋友圈员工朋友圈发表状态 开始===================");
        List<DistributionMoments> moments = distributionMomentsMapper.getMomentPublishStatus();
        for (DistributionMoments moment : moments) {
            WxCpGetMomentTask momentTask = null;
            try {
                momentTask = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                        .getMomentTask(moment.getQwMomentsId(),null,null);
            } catch (WxErrorException e) {
                e.printStackTrace();
                log.error("获取朋友圈员工朋友圈发表状态失败，参数:{},执行结果:{}", moment.getQwJobId(),JSONObject.toJSONString(momentTask));
            } finally {
                log.error("获取朋友圈员工朋友圈发表状态结束，参数:{},执行结果:{}", moment.getQwJobId(),JSONObject.toJSONString(momentTask));
            }

            if(momentTask!=null && CollUtil.isNotEmpty(momentTask.getTaskList())){
                List<String> userIds = momentTask.getTaskList().stream().filter(s->StrUtil.equals(s.getPublishStatus(),"1"))
                        .map(WxCpGetMomentTask.MomentTaskItem::getUserId).collect(Collectors.toList());
                for (String userId : userIds) {
                    //更新会员的朋友圈发送状态为已发送
                    distributionMomentsSendRecordMapper.publish(userId,moment.getId());
                }
            }
        }

        log.info("==============获取朋友圈员工朋友圈发表状态 结束===================");
    }

    /**
     * 获取朋友圈互动数据
     */
    @XxlJob("getMomentComments")
    public void getMomentComments()  {
        log.info("==============获取朋友圈互动数据 开始===================");
        //查询待更新记录的状态
        List<DistributionMomentsSendRecord> sendRecords = distributionMomentsSendRecordService.getMomentCommentsList();
        log.info("==============获取朋友圈互动数据 开始 获取到的数据条数==================={}",sendRecords.size());
        for (DistributionMomentsSendRecord sendRecord : sendRecords) {
            DistributionMoments distributionMoments =  distributionMomentsMapper.getById(sendRecord.getMomentsId());
            try {
                WxCpGetMomentComments result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                        .getMomentComments(distributionMoments.getQwMomentsId(),sendRecord.getQiweiUserId());
                log.error("获取朋友圈互动数据执行结束，参数:{},执行结果:{}", distributionMoments.getQwMomentsId(),JSONObject.toJSONString(result));
                if(result.getErrcode()==0){
                    int commentNum = result.getCommentList().size();
                    int likeNum = result.getLikeList().size();
                    distributionMomentsSendRecordService.updateMomentComment(sendRecord.getId(),commentNum,likeNum);

                    //保存互动数据明细
                    DistributionMomentsCommentsDTO commentsDTO=new DistributionMomentsCommentsDTO();
                    commentsDTO.setSendRecord(sendRecord);
                    commentsDTO.setCommentList(result.getCommentList());
                    commentsDTO.setLikeList(result.getLikeList());
                    distributionMomentsCommentsService.saveTo(commentsDTO);
                }
            } catch (WxErrorException e) {
                log.error("获取朋友圈互动数据执行失败，参数:{},执行结果:{}", e.toString());
            }
        }
        log.info("==============获取朋友圈互动数据 结束===================");
    }

    /**
     * 定时任务处理超时未发送的朋友圈
     */
    @XxlJob("taskTimeOut")
    public void taskTimeOut()  {
        Long startTime=System.currentTimeMillis();
        log.info("==============定时任务处理超时未发送的朋友圈 开始===================");
        distributionMomentsService.taskTimeOut();
        log.info("==============定时任务处理超时未发送的朋友圈 开始===================耗时：{}ms",System.currentTimeMillis() - startTime);
    }

}
