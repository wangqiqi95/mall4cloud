package com.mall4j.cloud.api.docking.dto.zhls.product;

import com.mall4j.cloud.api.docking.dto.zhls.product.validation.AddSku;
import com.mall4j.cloud.api.docking.dto.zhls.product.validation.UpdateSku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-03-15 11:48
 **/
@Data
public class SaveSkuDto extends BaseProductReqDto {
    @ApiModelProperty(value = "商品SKU列表 数组最大长度 50")
    @NotEmpty(message = "sku不能为空",groups = {AddSku.class, UpdateSku.class})
    @Size(min = 1,max = 50,message = "SKU数组长度不正确",groups = {AddSku.class, UpdateSku.class})
    private List<Sku> skus;
}
