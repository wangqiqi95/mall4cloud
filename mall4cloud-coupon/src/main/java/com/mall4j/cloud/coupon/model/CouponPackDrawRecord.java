package com.mall4j.cloud.coupon.model;

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
@TableName(value = "coupon_pack_draw_record")
public class CouponPackDrawRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private Integer activityId;
    private Long shopId;
    private Date receiveTime;
}
