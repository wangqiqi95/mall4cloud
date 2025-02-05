package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "pay_activity_record")
public class PayActivityRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer activityId;
    private Long userId;
    private Long orderId;
    private BigDecimal orderAmount;
    private Integer drawType;
    private Date createTime;
}
