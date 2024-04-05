package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.platform.model.Staff;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @Author: chaoge
 * @CreateTime: 2022-09-13  11:37
 * @Description: 关闭门店时展示
 */
@Data
public class CloseStoreStaffVO {
    @ApiModelProperty("结果：0-修改成功 | 1-门店有在职员工，无法关店 | 2-门店有会员，请变更新的服务门店")
    private Integer result;
}
