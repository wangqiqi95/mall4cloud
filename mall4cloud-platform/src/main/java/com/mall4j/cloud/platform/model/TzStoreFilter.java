package com.mall4j.cloud.platform.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 门店过滤表
 *
 * @author Casey
 * @date
 */
@Data
@ApiModel("门店过滤表")
public class TzStoreFilter extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private Long storeId;

    /**
     * 第三方门店编码
     */
    @ApiModelProperty("第三方门店编码")
    private String storeCode;

    /**
     * 过滤员工
     */
    @ApiModelProperty("过滤员工")
    private Integer isFilterStaff;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Integer getIsFilterStaff() {
        return isFilterStaff;
    }

    public void setIsFilterStaff(Integer isFilterStaff) {
        this.isFilterStaff = isFilterStaff;
    }

    @Override
    public String toString() {
        return "TzStoreFilter{" +
                "storeId=" + storeId +
                ",storeCode=" + storeCode +
                ",isFilterStaff=" + isFilterStaff +
                '}';
    }

}
