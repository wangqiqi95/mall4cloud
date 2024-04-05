package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.feign.StaffTaskFeignClient;
import com.mall4j.cloud.api.biz.vo.WaitMatterCountVO;
import com.mall4j.cloud.biz.mapper.cp.CpPhoneTaskStaffMapper;
import com.mall4j.cloud.biz.mapper.cp.TaskStaffRefMapper;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author gmq
 * @date 2021-08-25
 */
@RestController
@Slf4j
public class StaffTaskFeignController implements StaffTaskFeignClient {

    @Autowired
    private TaskStaffRefMapper taskStaffRefMapper;
    @Autowired
    private CpPhoneTaskStaffMapper phoneTaskStaffMapper;

    @Override
    public ServerResponseEntity<WaitMatterCountVO> waitMatterCount() {
        Long staffId= AuthUserContext.get().getUserId();
        WaitMatterCountVO countVO=new WaitMatterCountVO();
        Integer tagGroupCount=taskStaffRefMapper.waitMatterCountByStaffId(staffId);
        Integer taskPhoneCount=phoneTaskStaffMapper.waitMatterCountByStaffId(staffId);
        countVO.setTagGroupCount(Objects.nonNull(tagGroupCount)?tagGroupCount:0);
        countVO.setTaskPhoneCount(Objects.nonNull(taskPhoneCount)?taskPhoneCount:0);
        return ServerResponseEntity.success(countVO);
    }
}
