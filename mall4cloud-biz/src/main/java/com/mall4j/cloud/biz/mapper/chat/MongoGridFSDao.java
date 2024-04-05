/*
 * work_wx
 * wuhen 2020/1/16.
 * Copyright (c) 2020  jianfengwuhen@126.com All Rights Reserved.
 */

package com.mall4j.cloud.biz.mapper.chat;

import com.mall4j.cloud.biz.model.chat.FileModel;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.io.OutputStream;


@Repository
public class MongoGridFSDao {

    protected Logger logger = LoggerFactory.getLogger(MongoGridFSDao.class);

    private GridFsTemplate gridFsTemplate;

    private GridFSBucket gridFSBucket;

    @Autowired
    public void setGridFSBucket(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }

    @Autowired
    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public GridFSUploadStream save(FileModel fileModel) {
        /*GridFSUploadOptions gridFSUploadOptions = new GridFSUploadOptions().chunkSizeBytes(fileModel.getLength().intValue()).
                metadata(new Document("contentType",fileModel.getFileType()));*/
        GridFSUploadOptions gridFSUploadOptions = new GridFSUploadOptions().chunkSizeBytes(15728640).
                metadata(new Document("contentType",fileModel.getFileType()));
        return gridFSBucket.openUploadStream(fileModel.getFileName(),gridFSUploadOptions);
    }


    public InputStream getFile(String fileName) throws Exception {
        return gridFSBucket.openDownloadStream(fileName);
    }


    public void getFile(String fileName, OutputStream outputStream) throws Exception {
        gridFSBucket.downloadToStream(fileName, outputStream);
    }



}

