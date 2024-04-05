package com.mall4j.cloud.biz.mapper.chat;

import com.mall4j.cloud.biz.model.chat.SessionFile;
import org.springframework.stereotype.Repository;


@Repository
public class SessionSearchDbDao extends MongoDbDao {

    @Override
    protected Class<SessionFile> getEntityClass() {
        return SessionFile.class;
    }



}
