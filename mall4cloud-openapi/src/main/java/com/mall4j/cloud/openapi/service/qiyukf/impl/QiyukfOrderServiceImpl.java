package com.mall4j.cloud.openapi.service.qiyukf.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.openapi.dto.qiyukf.QiyukfQueryDTO;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfOrderServerResponseEntity;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfTokenVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.openapi.config.QiyukfConfigProperties;
import com.mall4j.cloud.openapi.manager.QiyukfOrderManager;
import com.mall4j.cloud.openapi.service.qiyukf.QiyukfOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Service
public class QiyukfOrderServiceImpl implements QiyukfOrderService {

    @Autowired
    private QiyukfConfigProperties qiyukfConfigProperties;

    @Autowired
    private QiyukfOrderManager qiyukfOrderManager;


    private final static String QIYU_KEFU_TOKEN = "qiyu_kefu_token:";


    @Override
    public QiyukfTokenVO getToken(String appid, String appsecret) {

        QiyukfTokenVO qiyukfTokenVO;

        if (RedisUtil.hasKey(QIYU_KEFU_TOKEN+appid)
                && Objects.nonNull(qiyukfTokenVO = RedisUtil.get(QIYU_KEFU_TOKEN+appid))){
            return qiyukfTokenVO;
        }

        if (qiyukfConfigProperties.getAppId().equals(appid)
                && qiyukfConfigProperties.getAppSecret().equals(appsecret)){
            qiyukfTokenVO = new QiyukfTokenVO("0", encryptToken(), qiyukfConfigProperties.getExpiresIn());
            RedisUtil.set(QIYU_KEFU_TOKEN+appid,qiyukfTokenVO,qiyukfConfigProperties.getExpiresIn()/1000L-60L);
            return qiyukfTokenVO;
        }


        return new QiyukfTokenVO("1", null, null);

    }

    @Override
    public QiyukfOrderServerResponseEntity getTheOrderPageByUser(QiyukfQueryDTO qiyukfQueryDTO, HttpServletRequest request) {

        try {
//            if (!request.getHeader("Referer").equals(qiyukfConfigProperties.getUrl())){
//                return null;
//             }

            if (qiyukfQueryDTO.getAppid().equals(qiyukfConfigProperties.getAppId())
                    && !decryptToken(qiyukfQueryDTO.getToken())){
                log.info("REQUEST REFERER IS：{}",request.getHeader("Referer"));

                log.info("REQUEST PARAM IS：{}", JSONObject.toJSONString(qiyukfQueryDTO));

                return qiyukfOrderManager.getTheOrderListByUserId(qiyukfQueryDTO);
//                String s = "{\"rlt\":0,\"count\":2,\"orders\":[{\"index\":0,\"blocks\":[{\"index\":0,\"is_title\":true,\"data\":[{\"index\":0,\"key\":\"orderid\",\"label\":\"订单号\",\"value\":\"00001\",\"href\":\"url\"}]},{\"index\":1,\"data\":[{\"index\":0,\"key\":\"product\",\"label\":\"产品名称\",\"value\":\"易钱包\",\"href\":\"url\"},{\"index\":1,\"key\":\"amount\",\"label\":\"支付金额\",\"value\":\"2000.00元\"},{\"index\":2,\"key\":\"basic_proc\",\"label\":\"基础产品\",\"value\":\"渤海人寿保险\",\"href\":\"url\"},{\"index\":3,\"key\":\"revenue_yesterday\",\"label\":\"昨日收益\",\"value\":\"1.00元\"},{\"index\":4,\"key\":\"revenue_amount\",\"label\":\"累计收益\",\"value\":\"2.00元\"}]}]},{\"index\":1,\"blocks\":[{\"index\":0,\"is_title\":true,\"data\":[{\"index\":0,\"key\":\"orderid\",\"label\":\"订单号\",\"value\":\"00002\",\"href\":\"url\"}]},{\"index\":1,\"data\":[{\"index\":0,\"key\":\"product\",\"label\":\"产品名称\",\"value\":\"易钱包\",\"href\":\"url\"},{\"index\":1,\"key\":\"amount\",\"label\":\"支付金额\",\"value\":\"2000.00元\"},{\"index\":2,\"key\":\"basic_proc\",\"label\":\"基础产品\",\"value\":\"渤海人寿保险\",\"href\":\"url\"},{\"index\":3,\"key\":\"revenue_yesterday\",\"label\":\"昨日收益\",\"value\":\"1.00元\"},{\"index\":4,\"key\":\"revenue_amount\",\"label\":\"累计收益\",\"value\":\"2.00元\"}]}]}]}";
//
//                QiyukfServerResponseEntity qiyukfServerResponseEntity = JSONObject.parseObject(s, QiyukfServerResponseEntity.class);
//
//                return qiyukfServerResponseEntity;
            }

            return null;

        }catch (Exception e){
            log.error("QIYU KEFU GET ORDER PAGE DATA IS FAIL, MESSAGE IS: {}",e);
            return null;
        }

    }



    private String encryptToken(){
        String accessToken = IdUtil.simpleUUID();
        AES aes = new AES(qiyukfConfigProperties.getSignKey().getBytes(StandardCharsets.UTF_8));
        return aes.encryptBase64(accessToken + System.currentTimeMillis());
    }


    private Boolean decryptToken(String token){
        AES aes = new AES(qiyukfConfigProperties.getSignKey().getBytes(StandardCharsets.UTF_8));
        String decryptStr;

        decryptStr = aes.decryptStr(token);
        // 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
        long createTokenTime = Long.parseLong(decryptStr.substring(32, 45));
        return System.currentTimeMillis() - createTokenTime > qiyukfConfigProperties.getExpiresIn();
    }
}
