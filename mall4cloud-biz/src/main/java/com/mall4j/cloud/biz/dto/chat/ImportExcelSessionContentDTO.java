package com.mall4j.cloud.biz.dto.chat;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImportExcelSessionContentDTO implements Serializable {

    private String content;
    private String title;
    private String description;
    private String username;
    private String displayname;

    private String corpname;
    private String userid;

    private String pre_msgid;

    private String md5sum;

    private String sdkfileid;
    private String filesize;

    private String voice_size;
    private String play_length;
}
