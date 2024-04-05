/*
 * work_wx
 * wuhen 2020/2/11.
 * Copyright (c) 2020  jianfengwuhen@126.com All Rights Reserved.
 */

package com.mall4j.cloud.biz.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    /*@Value("${spring.data.mongodb.database}")
    String db;*/
    @Value("${spring.data.mongodb.uri}")
    String db;

    @Value("${spring.data.mongodb.grid-fs-database}")
    String bucketName;


    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient){
        MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase,bucketName);
        return bucket;
    }

    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create(db);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongoClient(),"admin");
    }

}
