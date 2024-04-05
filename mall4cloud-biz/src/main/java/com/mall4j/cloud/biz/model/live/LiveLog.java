

package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author lhd
 * @date 2020-08-12 16:05:26
 */
@Data
public class LiveLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 商家直播操作记录
     */
    @TableId
    private Long liveLogId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 操作次数
     */
    private Integer num;
    /**
     * 操作接口类型 参考liveType
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;

}
