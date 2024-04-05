package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationPhoneVO;
import com.mall4j.cloud.api.user.vo.cp.CountStaffRelByStatesVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mall4cloud-user",contextId = "userStaffCpRelation")
public interface UserStaffCpRelationFeignClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/bindStaffId")
    ServerResponseEntity<Void> bindStaffId(@RequestParam("qiWeiStaffId") String qiWeiStaffId, @RequestParam("staffId") Long staffId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/page")
    ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> pageWithStaff(@RequestBody UserStaffCpRelationSearchDTO searchDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/getUserInfoByQiWeiUserId")
    ServerResponseEntity<UserStaffCpRelationListVO> getUserInfoByQiWeiUserId(@RequestParam("qiWeiStaffId") String qiWeiStaffId, @RequestParam("qiWeiUserId") String qiWeiUserId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/countNotMemberUsers")
    ServerResponseEntity<List<CountNotMemberUsersVO>> countNotMemberUsers();

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/countNotQiWeiUsers")
    ServerResponseEntity<List<CountNotMemberUsersVO>> countNotQiWeiUsers();

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/qiWeiFriendsChange")
    ServerResponseEntity<Void> qiWeiFriendsChange(@RequestBody UserStaffCpRelationDTO relationDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/updateUserStaffCpRelationStatus")
    ServerResponseEntity<Void> updateUserStaffCpRelationStatus(@RequestBody UserStaffCpRelationDTO relationDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/updateUserStaffCpRelationRemark")
    ServerResponseEntity<Void> updateUserStaffCpRelationRemark(@RequestBody UserStaffCpRelationDTO relationDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/updateUserStaffCpRelationAutoType")
    ServerResponseEntity<Void> updateUserStaffCpRelationAutoType(@RequestBody UserStaffCpRelationDTO relationDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/staffDimission")
    ServerResponseEntity<Void> staffDimission(@RequestBody List<Long> staffIds);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/getUserStaffRelBy")
    ServerResponseEntity<List<UserStaffCpRelationListVO>> getUserStaffRelBy(@RequestBody UserStaffCpRelationSearchDTO relationDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/getUserStaffRelAll")
    ServerResponseEntity<List<UserStaffCpRelationListVO>> getUserStaffRelAll(@RequestBody UserStaffCpRelationSearchDTO relationDTO);

    /**
     * 根据渠道统计添加好友数
     * @param states
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/getCountByStates")
    ServerResponseEntity<List<CountStaffRelByStatesVO>> getCountByStates(@RequestBody List<String> states);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userStaffCpRelation/getRelationPhone")
    ServerResponseEntity<List<UserStaffCpRelationPhoneVO>> getRelationPhone(@RequestBody List<String> phones);



}
