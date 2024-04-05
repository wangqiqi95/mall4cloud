package com.mall4j.cloud.product.task;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.util.PrincipalUtil;
import com.mall4j.cloud.product.service.SpuChannelPriceService;
import com.mall4j.cloud.product.service.SpuPriceService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 商品渠道价定时任务
 *
 */
@Component
@RestController
@RequestMapping("/spu/channel/price")
public class ProductChannelPirceTask {

    private static final Logger logger = LoggerFactory.getLogger(ProductChannelPirceTask.class);

    private static final String LOG_TAG="定时任务设置渠道价";

    @Autowired
    private SpuChannelPriceService spuChannelPriceService;

    /**
     * 设置商品渠道价
     */
    @XxlJob("spuChannelPrice")
    @PostMapping("spuChannelPrice")
    public void spuChannelPrice(@RequestParam(value = "spuCodes",defaultValue = "") String spuCodes){

        logger.info(LOG_TAG+"{}","-----开始设置渠道价任务------");
        Long startTime=System.currentTimeMillis();

        String param = XxlJobHelper.getJobParam();
        logger.info(LOG_TAG+"开始设置渠道价任务---> 接收調度中心参数...[{}]",param);
        logger.info(LOG_TAG+"开始设置渠道价任务---> 接口参数...[{}]",spuCodes);
        if(StrUtil.isNotBlank(param)){
            spuCodes=param;
        }

        /**
         * 设置渠道价
         */
        spuChannelPriceService.channelPriceTask(spuCodes,"系统定时任务");
        logger.info(LOG_TAG+"-----结束设置渠道价任务------耗时：{}ms", System.currentTimeMillis() - startTime);


        logger.info(LOG_TAG+"{}","-----开始取消渠道价任务------");
        startTime=System.currentTimeMillis();
        /**
         * 取消渠道价 更换渠道价
         */
        spuChannelPriceService.cancelChannelPriceTask(spuCodes,"系统定时任务");
        logger.info(LOG_TAG+"-----结束取消渠道价任务------耗时：{}ms", System.currentTimeMillis() - startTime);


    }

    private static boolean getDiscount(String spuCode,String discount){
        logger.info(LOG_TAG+" 商品货号【{}】 渠道折扣等级【{}】",spuCode,discount);
        if(StrUtil.isBlank(discount)){
            logger.info(LOG_TAG+" {}","折扣等级为空");
            return false;
        }
        if (!PrincipalUtil.isMaximumOfTwoDecimal(discount) && !discount.equals("0")) {
            logger.error(LOG_TAG+" {}","最多是保留2位小数的数值");
            return false;
        }
        if(NumberUtil.parseDouble(discount)<0 || NumberUtil.parseDouble(discount)<0.99){
            logger.error(LOG_TAG+" {}","小于0，默认为0");
            return false;
        }
        logger.info(LOG_TAG+" 商品货号【{}】 渠道折扣等级【{}】",spuCode,discount);
        return true;
    }

    private static String conversionPrices (String num) {
        if (StrUtil.isBlank(num)) {
            return num;
        }
        BigDecimal b1 = new BigDecimal(num);
        BigDecimal b2 = new BigDecimal(Constant.PRICE_MAGNIFICATION);
        double price = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(price);
    }


    public static void main(String[] s){
        String channeldiscount="0.00";
        System.out.println(getDiscount("123",channeldiscount));
        Long marketPirceFee=59900L;
        Double discount=Double.parseDouble(channeldiscount);
        Double d = marketPirceFee * discount / 10;
        System.out.println(d);
        System.out.println(d.longValue());
        System.out.println(conversionPrices(d.toString()));

    }
}
