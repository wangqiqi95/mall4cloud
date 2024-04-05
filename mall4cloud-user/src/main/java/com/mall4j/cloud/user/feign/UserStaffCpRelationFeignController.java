package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.CountNotMemberUsersVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationPhoneVO;
import com.mall4j.cloud.api.user.vo.cp.CountStaffRelByStatesVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.manager.QiWeiFriendsChangeManager;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.UserStaffCpRelationPhone;
import com.mall4j.cloud.user.service.UserStaffCpRelationPhoneService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
public class UserStaffCpRelationFeignController implements UserStaffCpRelationFeignClient {

    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;
    @Autowired
    private UserStaffCpRelationMapper userStaffCpRelationMapper;
    @Autowired
    private QiWeiFriendsChangeManager qiWeiFriendsChangeManager;
    @Autowired
    private UserStaffCpRelationPhoneService userStaffCpRelationPhoneService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<Void> bindStaffId(String qiWeiStaffId, Long staffId) {
        userStaffCpRelationService.bindStaffId(qiWeiStaffId, staffId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageVO<UserStaffCpRelationListVO>> pageWithStaff(UserStaffCpRelationSearchDTO searchDTO) {
        return ServerResponseEntity.success(userStaffCpRelationService.pageWithStaff(searchDTO));
    }

    @Override
    public ServerResponseEntity<UserStaffCpRelationListVO> getUserInfoByQiWeiUserId(String qiWeiStaffId, String qiWeiUserId) {
        return ServerResponseEntity.success(userStaffCpRelationService.getUserInfoByQiWeiUserId(qiWeiStaffId,qiWeiUserId));
    }

    @Override
    public ServerResponseEntity<List<CountNotMemberUsersVO>> countNotMemberUsers() {
        return ServerResponseEntity.success(userStaffCpRelationService.countNotMemberUsers());
    }

    @Override
    public ServerResponseEntity<List<CountNotMemberUsersVO>> countNotQiWeiUsers() {
        return ServerResponseEntity.success(userStaffCpRelationService.countNotQiWeiUsers());
    }

    @Override
    public ServerResponseEntity<Void> qiWeiFriendsChange(UserStaffCpRelationDTO relationDTO) {
        qiWeiFriendsChangeManager.doMessage(relationDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateUserStaffCpRelationStatus(UserStaffCpRelationDTO relationDTO) {
        qiWeiFriendsChangeManager.updateUserStaffCpRelationStatus(relationDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateUserStaffCpRelationRemark(UserStaffCpRelationDTO relationDTO) {
        qiWeiFriendsChangeManager.updateUserStaffCpRelationRemark(relationDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateUserStaffCpRelationAutoType(UserStaffCpRelationDTO relationDTO) {
        qiWeiFriendsChangeManager.updateUserStaffCpRelationAutoType(relationDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> staffDimission(List<Long> staffIds) {
        userStaffCpRelationService.staffDimission(staffIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<UserStaffCpRelationListVO>> getUserStaffRelBy(UserStaffCpRelationSearchDTO dto) {
        return ServerResponseEntity.success(userStaffCpRelationService.getUserStaffRelBy(dto));
    }

    @Override
    public ServerResponseEntity<List<UserStaffCpRelationListVO>> getUserStaffRelAll(UserStaffCpRelationSearchDTO dto) {
        return ServerResponseEntity.success(userStaffCpRelationService.getUserStaffRelAll(dto));
    }

    @Override
    public ServerResponseEntity<List<CountStaffRelByStatesVO>> getCountByStates(List<String> states) {
        return ServerResponseEntity.success(userStaffCpRelationMapper.selectCountByStates(states));
    }

    @Override
    public ServerResponseEntity<List<UserStaffCpRelationPhoneVO>> getRelationPhone(List<String> phones) {
        List<UserStaffCpRelationPhone> phoneList=userStaffCpRelationPhoneService.getListByPhone(phones);
        return ServerResponseEntity.success(mapperFacade.mapAsList(phoneList,UserStaffCpRelationPhoneVO.class));
    }

}
