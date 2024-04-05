package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.VeekerApplyDTO;
import com.mall4j.cloud.user.dto.VeekerAuditDTO;
import com.mall4j.cloud.user.dto.VeekerQueryDTO;
import com.mall4j.cloud.user.dto.VeekerStatusUpdateDTO;
import com.mall4j.cloud.user.vo.VeekerApplyVO;
import com.mall4j.cloud.user.vo.VeekerStatus;
import com.mall4j.cloud.user.vo.VeekerVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface VeekerService {

    PageVO<VeekerVO> page(PageDTO pageDTO, VeekerQueryDTO veekerQueryDTO);

    PageVO<VeekerVO> staffPage(PageDTO pageDTO, VeekerQueryDTO veekerQueryDTO);

    void batchUpdateVeekerStatusByUserIds(VeekerStatusUpdateDTO veekerStatusUpdateDTO);

    void batchUpdateVeekerAuditStatusByUserIds(VeekerAuditDTO veekerAuditDTO);

    void unbindStaff(Long userId);

    void export(HttpServletResponse response, VeekerQueryDTO veekerQueryDTO);

    VeekerApplyVO apply(VeekerApplyDTO veekerApplyDTO);

    boolean isVeeker(Long userId);

    VeekerStatus getVeekerStatus(Long userId);

    Integer countStaffWeeker(UserQueryDTO userQueryDTO);

    List<UserApiVO> listStaffWeeker(UserQueryDTO userQueryDTO);

}
