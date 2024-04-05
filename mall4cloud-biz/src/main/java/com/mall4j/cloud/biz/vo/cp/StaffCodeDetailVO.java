package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.StaffCode;
import lombok.Data;

import java.util.List;

@Data
public class StaffCodeDetailVO {

    private List<CpStaffCodeRef> staffs;

    private CpWelcomeAttachment attachments;

    private StaffCode staffCode;

    public StaffCodeDetailVO(StaffCode staffCode, CpWelcomeAttachment attachments, List<CpStaffCodeRef> staffs){
        this.attachments = attachments;
        this.staffs = staffs;
        this.staffCode = staffCode;
    }

}
