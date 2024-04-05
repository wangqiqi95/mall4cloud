package com.mall4j.cloud.flow.config;


import cn.hutool.core.io.IoUtil;
import org.lionsoul.ip2region.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * 获取IP
 * @author YXF
 * @date 2021-06-02
 */
@Configuration
public class Ip2RegionConfig {

    private static final Logger log = LoggerFactory.getLogger(Ip2RegionConfig.class);

    @Bean
    public DbSearcher dbSearcher() throws DbMakerConfigException, IOException {
        DbConfig dbConfig = new DbConfig();

        // 获取ip库路径
        ClassPathResource classPathResource = new ClassPathResource("ip2region.db");
        if (classPathResource.getClassLoader() == null){
            log.error("存储路径发生错误，没有被发现");
            return null;
        }
        InputStream inputStream = classPathResource.getInputStream();
        byte[] bytes = IoUtil.readBytes(inputStream);
        DbSearcher dbSearcher = new DbSearcher(new DbConfig(), bytes);
        return dbSearcher;
    }
}
