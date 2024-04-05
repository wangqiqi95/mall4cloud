package com.mall4j.cloud.biz.model.channels;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu 修改记录
 * @TableName channels_spu_update_log
 */
@TableName(value ="channels_spu_update_log")
@Data
public class ChannelsSpuUpdateLog implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * channels_spu_id
     */
    private Long channelsSpuId;

    /**
     * 更新类型 1参数更新 2库存更新
     */
    private Integer updateType;

    /**
     * 更新入参
     */
    private String updateParam;

    /**
     * 创建人
     */
    private String craeteBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChannelsSpuUpdateLog other = (ChannelsSpuUpdateLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getChannelsSpuId() == null ? other.getChannelsSpuId() == null : this.getChannelsSpuId().equals(other.getChannelsSpuId()))
            && (this.getUpdateType() == null ? other.getUpdateType() == null : this.getUpdateType().equals(other.getUpdateType()))
            && (this.getUpdateParam() == null ? other.getUpdateParam() == null : this.getUpdateParam().equals(other.getUpdateParam()))
            && (this.getCraeteBy() == null ? other.getCraeteBy() == null : this.getCraeteBy().equals(other.getCraeteBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getChannelsSpuId() == null) ? 0 : getChannelsSpuId().hashCode());
        result = prime * result + ((getUpdateType() == null) ? 0 : getUpdateType().hashCode());
        result = prime * result + ((getUpdateParam() == null) ? 0 : getUpdateParam().hashCode());
        result = prime * result + ((getCraeteBy() == null) ? 0 : getCraeteBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", channelsSpuId=").append(channelsSpuId);
        sb.append(", updateType=").append(updateType);
        sb.append(", updateParam=").append(updateParam);
        sb.append(", craeteBy=").append(craeteBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}