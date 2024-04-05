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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "advance_commodity")
public class AdvanceCommodity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer advanceId;
    private String barCode;
    private BigDecimal activityPrice;
}
