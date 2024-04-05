package com.mall4j.cloud.group.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.group.bo.QuestionnaireActivityNotifyBo;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecord;
import com.mall4j.cloud.group.mapper.QuestionnaireUserAnswerRecordMapper;
import com.mall4j.cloud.group.service.QuestionnaireUserAnswerRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * 问卷 会员答题记录
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Slf4j
@Service
public class QuestionnaireUserAnswerRecordServiceImpl extends ServiceImpl<QuestionnaireUserAnswerRecordMapper, QuestionnaireUserAnswerRecord> implements QuestionnaireUserAnswerRecordService {

    @Autowired
    private QuestionnaireUserAnswerRecordMapper questionnaireUserAnswerRecordMapper;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    OnsMQTemplate questionnaireActivityBeginTemplate;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    public PageVO<QuestionnaireUserAnswerRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> questionnaireUserAnswerRecordMapper.list());
    }

    @Override
    public QuestionnaireUserAnswerRecord getById(Long id) {
        return questionnaireUserAnswerRecordMapper.getById(id);
    }


    @Override
    public void update(QuestionnaireUserAnswerRecord questionnaireUserAnswerRecord) {
        questionnaireUserAnswerRecordMapper.update(questionnaireUserAnswerRecord);
    }

    @Override
    public void deleteById(Long id) {
        questionnaireUserAnswerRecordMapper.deleteById(id);
    }

    @Override
    public QuestionnaireUserAnswerRecord getOrSave(Long activityId, Long userId, Long storeId, Boolean isIncreaseBrowse, Boolean isSendSubscribe) {
        QuestionnaireUserAnswerRecord userAnswerRecord = this.getOne(new LambdaQueryWrapper<QuestionnaireUserAnswerRecord>()
                .eq(QuestionnaireUserAnswerRecord::getActivityId,activityId)
                .eq(QuestionnaireUserAnswerRecord::getUserId,userId),false);
        if(userAnswerRecord==null){
            ServerResponseEntity<UserApiVO> userServerResponse = userFeignClient.getInsiderUserData(userId);
            if(userServerResponse==null || userServerResponse.isFail() || userServerResponse.getData()==null){
                Assert.faild("用户id错误。");
            }

            StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
            if (Objects.isNull(byStoreId)){
                Assert.faild("门店不存在");
            }
            UserApiVO userApiVO = userServerResponse.getData();

            QuestionnaireUserAnswerRecord newUserAnswerRecord = new QuestionnaireUserAnswerRecord();
            newUserAnswerRecord.setUserId(userId);
            newUserAnswerRecord.setActivityId(activityId);
            newUserAnswerRecord.setVipcode(userApiVO.getVipcode());
            newUserAnswerRecord.setNickName(userApiVO.getName());
            newUserAnswerRecord.setPhone(userApiVO.getPhone());
            newUserAnswerRecord.setUnionId(userApiVO.getUnionId());
            newUserAnswerRecord.setStoreId(byStoreId.getStoreId());
            newUserAnswerRecord.setStoreCode(byStoreId.getStoreCode());
            newUserAnswerRecord.setStoreName(byStoreId.getName());
            this.save(newUserAnswerRecord);

            userAnswerRecord = newUserAnswerRecord;

            if (isSendSubscribe) {
                //TODO 发送延时消息，半小时后检查用户是否提交问卷，如果没有提交问卷则发送订阅消息提醒用户提交问卷
                QuestionnaireActivityNotifyBo message = new QuestionnaireActivityNotifyBo();
                message.setActivityId(activityId);
                message.setUserAnswerRecordId(userAnswerRecord.getId());
                SendResult sendResult = questionnaireActivityBeginTemplate.syncSend(message, RocketMqConstant.QUESTIONNAIREACTIVITYBEGIN_DELAY_LEVEL);
                log.info("问卷延迟检查是否提交问卷消息发送成功，执行参数:{},执行结果:{}", JSONObject.toJSONString(message), JSONObject.toJSONString(sendResult));
            }
        } else if (isIncreaseBrowse){
            // 浏览次数增加
            questionnaireUserAnswerRecordMapper.increaseBrowse(userAnswerRecord.getId());
        }
        return userAnswerRecord;
    }

    @Override
    public void pointBrowseCount(Long userId, Long activityId) {
        if (Objects.isNull(userId)) {
            Assert.faild("用户不存在");
        }
        QuestionnaireUserAnswerRecord userAnswerRecord = this.getOne(new LambdaQueryWrapper<QuestionnaireUserAnswerRecord>()
                .eq(QuestionnaireUserAnswerRecord::getActivityId,activityId)
                .eq(QuestionnaireUserAnswerRecord::getUserId,userId),false);

        if (Objects.isNull(userAnswerRecord)) {
            Assert.faild("记录不存在");
        }

        // 浏览次数增加
        questionnaireUserAnswerRecordMapper.increaseBrowse(userAnswerRecord.getId());
    }

}
