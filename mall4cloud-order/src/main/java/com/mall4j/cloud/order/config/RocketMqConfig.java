package com.mall4j.cloud.order.config;

import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqAdapter;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author FrozenWatermelon
 * @date 2021/3/30
 */
@RefreshScope
@Configuration
public class RocketMqConfig {

    @Autowired
    private RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate stockMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.STOCK_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate couponMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.COUPON_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderCancelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_CANCEL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderNotifyShopTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_SHOP_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderNotifyCouponTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_COUPON_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderNotifyStockTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_STOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate orderReceiptTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.ORDER_RECEIPT_TOPIC);
        return template;
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate orderSettledShopTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.ORDER_SETTLED_SHOP_TOPIC);
        return template;
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTransactionTemplate orderRefundTemplate() {
        OnsMQTransactionTemplate template = rocketMqAdapter.getTransactionTemplateByTopicName(RocketMqConstant.ORDER_REFUND_TOPIC);
        return template;
    }


    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundPaymentTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_PAYMENT_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundShopTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SHOP_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundSuccessSettlementTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_SETTLEMENT_TOPIC);
    }


    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundSuccessCouponTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_COUPON_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundSuccessGrowthTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_GROWTH_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundSuccessScoreTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_SCORE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderRefundSuccessStockTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_REFUND_SUCCESS_STOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate groupOrderCancelMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.GROUP_ORDER_CANCEL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderNotifyGroupTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_GROUP_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate seckillOrderCancelMqTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SECKILL_ORDER_CANCEL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderNotifySeckillTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_SECKILL_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendNotifyToShopTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_NOTIFY_TO_SHOP_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendNotifyToUserTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_NOTIFY_TO_USER_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate userScoreTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SCORE_UNLOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendNotifyToUserExtensionTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_NOTIFY_TO_USER_EXTENSION_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate stdOrderNotifyTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_STD_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate stdOrderRefundTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_STD_TOPIC);
    }



    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate soldUploadExcelTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SOLD_UPLOAD_EXCEL);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate sendMaSubcriptMessageTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.SEND_MA_SUBCRIPT_MESSAGE_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate orderNotifyDistributionTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.ORDER_NOTIFY_DISTRIBUTION_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate ecOrderNotifyStockTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.EC_ORDER_NOTIFY_STOCK_TOPIC);
    }

    @Lazy
    @Bean(destroyMethod = "destroy")
    public OnsMQTemplate ecOrderRebuildDistributionTemplate() {
        return rocketMqAdapter.getTemplateByTopicName(RocketMqConstant.EC_ORDER_REBUILD_DISTRIBUTION_TOP);
    }

}
