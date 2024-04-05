package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("order_gift_stock")
public class OrderGiftStock implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderGiftId;
    private Long commodityId;
    private Integer commodityStock;
    private Integer commodityMaxStock;
}
