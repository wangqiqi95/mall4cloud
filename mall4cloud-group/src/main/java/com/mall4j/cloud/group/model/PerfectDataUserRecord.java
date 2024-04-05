package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "perfect_data_user_record")
public class PerfectDataUserRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private Integer activityId;
    private Date receiveTime;
}
