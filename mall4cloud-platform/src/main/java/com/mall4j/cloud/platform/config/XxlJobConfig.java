package com.mall4j.cloud.platform.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author FrozenWatermelon
 * @date 2021/1/18
 */
@Configuration
public class XxlJobConfig {
    private final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${mall4cloud.job.admin.addresses}")
    private String adminAddresses;

    @Value("${mall4cloud.job.accessToken}")
    private String accessToken;

    @Value("${spring.application.name}")
    private String appname;

    @Value("${server.port}")
    private int port;

    @Autowired
    private InetUtils inetUtils;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {

        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        // 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP
        xxlJobSpringExecutor.setIp(inetUtils.findFirstNonLoopbackAddress().getHostAddress());
        xxlJobSpringExecutor.setPort(port + 1000);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        return xxlJobSpringExecutor;
    }

}
