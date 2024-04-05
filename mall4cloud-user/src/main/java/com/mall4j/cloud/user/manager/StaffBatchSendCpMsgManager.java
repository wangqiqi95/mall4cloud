package com.mall4j.cloud.user.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.feign.WeixinCpExternalContactFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.AddStaffBatchSendCpMsgBO;
import com.mall4j.cloud.user.dto.StaffBatchSendCpMsgSaveDTO;
import com.mall4j.cloud.user.mapper.StaffBatchSendCpMsgMapper;
import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.mall4j.cloud.user.vo.StaffBatchSendCpMsgVO;
import java.util.Date;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
public class StaffBatchSendCpMsgManager {

    @Autowired
    private StaffBatchSendCpMsgMapper staffBatchSendCpMsgMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private WeixinCpExternalContactFeignClient weixinCpExternalContactFeignClient;

    @Transactional(rollbackFor = Exception.class)
    public void add(AddStaffBatchSendCpMsgBO addStaffBatchSendCpMsgBO){
        StaffBatchSendCpMsg staffBatchSendCpMsg = new StaffBatchSendCpMsg();

        BeanUtils.copyProperties(addStaffBatchSendCpMsgBO, staffBatchSendCpMsg);

        staffBatchSendCpMsgMapper.insert(staffBatchSendCpMsg);
    }

    /**
     * 取消群发任务
     * */
    public ServerResponseEntity cancelExternalContactMsgTemplate(String msgId){
        ServerResponseEntity<String> responseEntity = weixinCpExternalContactFeignClient.cancelExternalContactMsgTemplate(msgId);
        return responseEntity;
    }

    // 获取所有未发送的群发任务信息
    public List<StaffBatchSendCpMsgVO> getCpDataBySendStatus(){

        List<StaffBatchSendCpMsgVO> staffBatchSendCpMsgVOS = staffBatchSendCpMsgMapper.getCpDataBySendStatus();

        return staffBatchSendCpMsgVOS;
    }

    /**
     * 根据taskVipRelationVO里的 pushSonTaskId 和 staffId 去查询 t_group_son_task_send_record 表中成功的数据总数
     * @param sonTaskId 子任务ID
     * @param staffId 导购ID
     * @return
     */
    public Integer getCpMsgSendCountBySonPushTaskIdAndStaffId(Long sonTaskId, Long staffId, boolean createFlag){
        LambdaQueryWrapper<StaffBatchSendCpMsg> queryWrapper = new LambdaQueryWrapper<StaffBatchSendCpMsg>()
                .eq(StaffBatchSendCpMsg::getPushSonTaskId, sonTaskId)
                .eq(StaffBatchSendCpMsg::getStaffId, staffId);
        //如果传入该状态
        if (createFlag){
            queryWrapper.isNotNull(StaffBatchSendCpMsg::getMsgId);
        }
        Integer sendCpMsgCount = staffBatchSendCpMsgMapper.selectCount(
                queryWrapper
        );
        return sendCpMsgCount;
    }

    public StaffBatchSendCpMsgVO getBySonTaskIdAndStaffId(Long sonTaskId, Long staffId){

        StaffBatchSendCpMsg staffBatchSendCpMsg = staffBatchSendCpMsgMapper.selectOne(
                new LambdaQueryWrapper<StaffBatchSendCpMsg>()
                        .eq(StaffBatchSendCpMsg::getPushSonTaskId, sonTaskId)
                        .eq(StaffBatchSendCpMsg::getStaffId, staffId)
        );

        if (Objects.nonNull(staffBatchSendCpMsg)){
            StaffBatchSendCpMsgVO staffBatchSendCpMsgVO = new StaffBatchSendCpMsgVO();
            BeanUtils.copyProperties(staffBatchSendCpMsg, staffBatchSendCpMsgVO);
            return staffBatchSendCpMsgVO;
        }
        return null;
    }

    public void updateMsgIdById(Long id, String cpMsgId,Integer headCount) {
        StaffBatchSendCpMsg staffBatchSendCpMsg = staffBatchSendCpMsgMapper.selectById(id);
        staffBatchSendCpMsg.setMsgId(cpMsgId);
        staffBatchSendCpMsg.setSendStatus(1);
        staffBatchSendCpMsg.setReachCount(0);
        if(Objects.nonNull(headCount)){
            staffBatchSendCpMsg.setReachCount(0);
        }
        staffBatchSendCpMsgMapper.updateById(staffBatchSendCpMsg);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(StaffBatchSendCpMsgSaveDTO staffBatchSendCpMsgSaveDTO){
        StaffBatchSendCpMsg staffBatchSendCpMsg = new StaffBatchSendCpMsg();
        BeanUtils.copyProperties(staffBatchSendCpMsgSaveDTO, staffBatchSendCpMsg);
        staffBatchSendCpMsgMapper.updateById(staffBatchSendCpMsg);
    }

}
