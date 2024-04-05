package com.mall4j.cloud.order.dto.multishop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchreturnAndRefundAuditDTO {

    @ApiModelProperty(value = "退单编号集合", required = true)
    @NotNull(message = "退单编号集合")
    private List<Long> refundIds;

}
