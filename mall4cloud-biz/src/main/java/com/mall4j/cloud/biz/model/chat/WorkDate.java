package com.mall4j.cloud.biz.model.chat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 工作时间表
 *
 */
@Data
public class WorkDate{

    //@TableId(type= IdType.AUTO)
    private String id;
    private String startTime;
    private String endTime;
    private Long sessionId;
}
