package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.biz.vo.cp.CustGroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustGroupDetailDTO {
    @ApiModelProperty("群信息")
    private CustGroupVO custGroup;
    @ApiModelProperty("标签信息")
    private List<Tag> tagList;
    public  CustGroupDetailDTO(CustGroupVO custGroup, List<Tag> tagList){
        this.custGroup = custGroup;
        this.tagList = tagList;
    }
}
