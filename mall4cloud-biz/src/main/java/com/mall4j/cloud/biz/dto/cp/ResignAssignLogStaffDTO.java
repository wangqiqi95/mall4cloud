package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResignAssignLogStaffDTO {
    public ResignAssignLogStaffDTO(StaffVO staffVO,StaffVO replaceByStaff){
        this.replaceByStaff = replaceByStaff;
        this.staffVO = staffVO;
        this.custList = new ArrayList<>();
    }


    private StaffVO staffVO;
    private StaffVO replaceByStaff;
    private List<CustDTO> custList ;


    public ResignAssignLogStaffDTO addCust(Long custId,String custName,String userId,Integer totalCust){
        CustDTO custDTO = new CustDTO();
        custDTO.setId(custId);
        custDTO.setCustName(custName);
        custDTO.setUserId(userId);
        custDTO.setTotalCust(totalCust);
        this.custList.add(custDTO);
        return this;
    }
    public ResignAssignLogStaffDTO addCust(UserStaffCpRelationListVO userStaffCpRelation){
        CustDTO custDTO = new CustDTO();
        custDTO.setId(userStaffCpRelation.getUserId());
//        custDTO.setCustName(userStaffCpRelation.getUserName());
        custDTO.setUserId(userStaffCpRelation.getQiWeiUserId());
//        custDTO.setMobile(userStaffCpRelation.getUserPhone());
//        custDTO.setLevel(userStaffCpRelation.getUserLevelName());
        this.custList.add(custDTO);
        return this;
    }

}
