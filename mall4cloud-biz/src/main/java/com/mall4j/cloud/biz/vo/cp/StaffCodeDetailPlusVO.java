package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.dto.cp.CpStaffCodeTimeDTO;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodePlus;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class StaffCodeDetailPlusVO {


    @ApiModelProperty(value = "备用员工")
    private List<CpStaffCodeRef> standbyStaffs;

    @ApiModelProperty(value = "接待员工")
    private List<CpStaffCodeTimeDTO> codeTimestaffs;

    @ApiModelProperty("欢迎语附件")
    @NotEmpty(message = "附件不能为空")
    private List<ChannelCodeWelcomeDTO> attachMents;

    private CpStaffCodePlus staffCode;

}
