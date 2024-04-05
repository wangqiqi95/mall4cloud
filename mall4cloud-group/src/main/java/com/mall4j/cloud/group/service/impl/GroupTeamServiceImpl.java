package com.mall4j.cloud.group.service.impl;

import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AppGroupActivityDTO;
import com.mall4j.cloud.group.mapper.GroupTeamMapper;
import com.mall4j.cloud.group.model.GroupTeam;
import com.mall4j.cloud.group.service.GroupTeamService;
import com.mall4j.cloud.group.vo.GroupTeamVO;
import com.mall4j.cloud.group.vo.app.AppGroupTeamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拼团团队表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
@Service
public class GroupTeamServiceImpl implements GroupTeamService {

    @Autowired
    private GroupTeamMapper groupTeamMapper;

    @Autowired
    private UserFeignClient userFeignClient;


    @Override
    public PageVO<GroupTeam> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> groupTeamMapper.list());
    }

    @Override
    public GroupTeamVO getByGroupTeamId(Long groupTeamId) {
        return groupTeamMapper.getByGroupTeamId(groupTeamId);
    }

    @Override
    public void save(GroupTeam groupTeam) {
        groupTeamMapper.save(groupTeam);
    }

    @Override
    public void update(GroupTeam groupTeam) {
        groupTeamMapper.update(groupTeam);
    }

    @Override
    public void deleteById(Long groupTeamId) {
        groupTeamMapper.deleteById(groupTeamId);
    }

    @Override
    public List<AppGroupTeamVO> listJoinGroup(Long groupActivityId, Integer showSize) {
        List<AppGroupTeamVO> appGroupTeamList = groupTeamMapper.listJoinGroup(groupActivityId, showSize);
        Set<Long> userIds = appGroupTeamList.stream().map(AppGroupTeamVO::getShareUserId).collect(Collectors.toSet());
        ServerResponseEntity<List<UserApiVO>> userResponse = userFeignClient.getUserByUserIds(new ArrayList<>(userIds));
        Map<Long, UserApiVO> userMap = userResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, u -> u));
        for (AppGroupTeamVO appGroupTeamVO : appGroupTeamList) {
            UserApiVO userApiVO = userMap.get(appGroupTeamVO.getShareUserId());
            appGroupTeamVO.setShareNickName(userApiVO.getNickName());
            appGroupTeamVO.setSharePic(userApiVO.getPic());
        }
        return appGroupTeamList;
    }

    @Override
    public AppGroupTeamVO getAppGroupTeam(Long groupTeamId) {
        return groupTeamMapper.getAppGroupTeam(groupTeamId);
    }

}
