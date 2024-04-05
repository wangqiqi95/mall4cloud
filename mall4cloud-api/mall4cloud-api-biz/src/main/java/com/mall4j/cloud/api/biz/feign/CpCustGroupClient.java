package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.CpCustGroupCountDTO;
import com.mall4j.cloud.api.biz.vo.CustGroupStaffCountVO;
import com.mall4j.cloud.api.biz.vo.CustGroupVO;
import com.mall4j.cloud.api.biz.vo.TaskSonGroupVO;
import com.mall4j.cloud.api.biz.vo.UserJoinCustGroupVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "mall4cloud-biz", contextId = "cpCustGroupApi")
public interface CpCustGroupClient {

    /**
     * 获取对应员工的群聊id
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cust/group")
    ServerResponseEntity<List<CustGroupVO>> getGroupByStaffIdList(@RequestParam("staffIdList") List<Long> staffIdList);


    /**
     * 通过客户的userid查询当前会员加入的群聊列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/findCustGroupByQwUserId")
    ServerResponseEntity<List<UserJoinCustGroupVO>> findCustGroupByQwUserId(@RequestParam("qwUserId") String qwUserId);

    /**
     * 通过客户的userid查询当前会员加入的群聊列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/countCustGroupByQwUserId")
    ServerResponseEntity<Integer> countCustGroupByQwUserId(@RequestParam("qwUserId") String qwUserId);

    /**
     * 通过群id查询群聊列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/findCustGroupByIds")
    ServerResponseEntity<List<TaskSonGroupVO>> findCustGroupByIds(@RequestParam("id") List<String> id);

    /**
     * 通过群id查询群聊列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/groupCountByStaff")
    ServerResponseEntity<List<CustGroupStaffCountVO>> groupCountByStaff(@RequestBody CpCustGroupCountDTO dto);
}
