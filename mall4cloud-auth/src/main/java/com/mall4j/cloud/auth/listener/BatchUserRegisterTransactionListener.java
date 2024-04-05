package com.mall4j.cloud.auth.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.mall4j.cloud.api.auth.bo.UserRegisterNotifyBO;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.auth.model.AuthAccount;
import com.mall4j.cloud.auth.service.AuthAccountService;
import com.mall4j.cloud.common.util.Json;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 确认注册事务监听
 *
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "userNotifyRegisterTemplate")
public class BatchUserRegisterTransactionListener implements LocalTransactionExecuter, LocalTransactionChecker {

    @Autowired
    private AuthAccountService authAccountService;

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        int row = authAccountService.batchSaveAccounts((List<AuthAccount>)arg);
        if (row == 0) {
            check(msg);
        }
        return TransactionStatus.CommitTransaction;
    }

    @Override
    public TransactionStatus check(Message msg) {
        UserRegisterNotifyBO userRegisterNotifyBO = Json.parseObject( msg.getBody(), UserRegisterNotifyBO.class);
        List<Long> userIds = userRegisterNotifyBO.getAccountDTOList().stream().map(AuthAccountDTO::getUserId).collect(Collectors.toList());
        // 如果插入的数据为0，这个时候我们是不需要插入数据的，则不用提交了
        int row = authAccountService.countByUserIds(userIds, SysTypeEnum.ORDINARY.value());
        if (row == 0) {
            return TransactionStatus.RollbackTransaction;
        }
        return TransactionStatus.CommitTransaction;
    }
}
