package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.WeixinCpExternalContactMsgStatusEnum;
import com.mall4j.cloud.user.dto.QueryGroupPushSonTaskPageDetailDTO;
import com.mall4j.cloud.user.dto.TerminateGroupPushSonTaskDTO;
import com.mall4j.cloud.user.manager.StaffBatchSendCpMsgManager;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMapper;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import com.mall4j.cloud.user.model.GroupSonTaskSendRecord;
import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.mall4j.cloud.user.mapper.StaffBatchSendCpMsgMapper;
import com.mall4j.cloud.user.service.StaffBatchSendCpMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.vo.StaffBatchSendCpMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-03-18
 */
@Slf4j
@Service
public class StaffBatchSendCpMsgServiceImpl extends ServiceImpl<StaffBatchSendCpMsgMapper, StaffBatchSendCpMsg> implements StaffBatchSendCpMsgService {


    @Autowired
    private StaffBatchSendCpMsgManager staffBatchSendCpMsgManager;

    @Resource
    private GroupPushSonTaskMapper groupPushSonTaskMapper;


    private final static String FAIL = "FAIL";

    @Override
    public void cancelExternalContactCpMsg(List<Long> sonTaskIdList) {

        //只需要处理未发送的数据
        List<StaffBatchSendCpMsg> list = lambdaQuery()
                .in(StaffBatchSendCpMsg::getPushSonTaskId, sonTaskIdList)
                .eq(StaffBatchSendCpMsg::getSendStatus, WeixinCpExternalContactMsgStatusEnum.NOT_SEND.getSendStatus())
                .list();

        if (CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(msg -> {
                //当企微接口调用失败，不需要做任何操作,不需要回滚，因为每次只会获取未发送数据
                ServerResponseEntity serverResponseEntity = staffBatchSendCpMsgManager.cancelExternalContactMsgTemplate(msg.getMsgId());
                if (serverResponseEntity.isSuccess()){
                    msg.setSendStatus(WeixinCpExternalContactMsgStatusEnum.CANCEL_SEND.getSendStatus());
                    updateById(msg);
                }else {
                    throw new LuckException("REQUEST CP API CANCEL EXTERNAL CONTACT MSG IS FAIL, MESSAGE IS: {}",serverResponseEntity.getMsg());
                }
            });
        }


    }

    @Override
    public List<StaffBatchSendCpMsg> getBySonTaskId(Long sonTaskId) {
        return list(new LambdaQueryWrapper<StaffBatchSendCpMsg>().eq(StaffBatchSendCpMsg::getStaffId, sonTaskId));
    }

    @Override
    public void editFailSend(StartGroupPushDTO startGroupPushDTO) {
       /* StaffBatchSendCpMsgVO msgVO = staffBatchSendCpMsgManager.getBySonTaskIdAndStaffId(startGroupPushDTO.getSonTaskId(), startGroupPushDTO.getStaffId());
        if (Objects.nonNull(msgVO)){
            staffBatchSendCpMsgManager.updateMsgIdById(msgVO.getStaffBatchSendCpMsgId(), FAIL);
        }*/
    }

    /**
     * 终止字任务
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void terminateSonTask(TerminateGroupPushSonTaskDTO detailDTO) {
        Long groupPushSonTaskId=detailDTO.getSonTaskId();
        GroupPushSonTask groupPushSonTask=groupPushSonTaskMapper.selectById(groupPushSonTaskId);
        if(Objects.isNull(groupPushSonTask)){
            throw new LuckException("操作失败，未获取到任务信息");
        }
        if(Objects.isNull(groupPushSonTask.getStatus()) && groupPushSonTask.getStatus()==2){
            throw new LuckException("操作失败，任务已中止");
        }
        LambdaQueryWrapper<StaffBatchSendCpMsg> lambdaQueryWrapper=new LambdaQueryWrapper<StaffBatchSendCpMsg>();
        lambdaQueryWrapper.eq(StaffBatchSendCpMsg::getPushSonTaskId,groupPushSonTaskId);
//        lambdaQueryWrapper.eq(GroupSonTaskSendRecord::getSendStatus,1);
        List<StaffBatchSendCpMsg> sendCpMsgs=this.list(lambdaQueryWrapper).stream()
                .filter(item->item.getSendStatus()!=2)
                .collect(Collectors.toList());;
        if(CollUtil.isEmpty(sendCpMsgs)){
            throw new LuckException("操作失败，未获取到相应记录");
        }
        List<StaffBatchSendCpMsg> terminates=sendCpMsgs.stream()
                .filter(item-> StrUtil.isNotEmpty(item.getMsgId()))
                .collect(Collectors.toList());
        if(CollUtil.isNotEmpty(terminates)){
            for (StaffBatchSendCpMsg terminate : terminates) {
                ServerResponseEntity responseEntity=staffBatchSendCpMsgManager.cancelExternalContactMsgTemplate(terminate.getMsgId());
                log.info("终止字任务参数 taskId:{} msgid:{} 执行结果：{}", groupPushSonTaskId,terminate.getMsgId(),JSON.toJSONString(responseEntity));
                if(responseEntity.isFail()){
                    throw new LuckException("中止任务失败");
                }
            }
        }

        for (StaffBatchSendCpMsg sendCpMsg : sendCpMsgs) {
            sendCpMsg.setSendStatus(2);
            sendCpMsg.setUpdateTime(new Date());
            sendCpMsg.setUpdateBy(AuthUserContext.get().getUsername()+"中止任务");
        }
        this.updateBatchById(sendCpMsgs);

        groupPushSonTask.setStatus(2);
        groupPushSonTask.setUpdateTime(new Date());
        groupPushSonTask.setUpdateBy(AuthUserContext.get().getUsername()+"中止任务");
        groupPushSonTaskMapper.updateById(groupPushSonTask);
    }
}
