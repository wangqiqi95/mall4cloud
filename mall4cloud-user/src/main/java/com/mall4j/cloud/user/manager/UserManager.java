package com.mall4j.cloud.user.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.user.dto.QueryTagPageDTO;
import com.mall4j.cloud.user.dto.StaffSelectUserForTaskDTO;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.vo.UserVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserManager {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MapperFacade mapperFacade;

    public List<String> filterVipCodeList(List<String> vipCodeList){
        List<User> userList = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .in(User::getVipcode, vipCodeList)
                        .select(User::getVipcode)
        );

        List<String> vipCodes = userList.stream().map(User::getVipcode).collect(Collectors.toList());
        return vipCodes;
    }

    public IPage<UserApiVO> staffScreenUserForTask(StaffSelectUserForTaskDTO StaffSelectUserForTaskDTO){

        IPage<UserApiVO> userApiVOPage = new Page<>(StaffSelectUserForTaskDTO.getPageNum().longValue(), StaffSelectUserForTaskDTO.getPageSize().longValue());

        userApiVOPage = userMapper.staffScreenUserForTask(userApiVOPage, StaffSelectUserForTaskDTO);

        return userApiVOPage;
    }


    public String getCodeById(Long userId){
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserId, userId)
                        .select(User::getVipcode)
        );
        return user.getVipcode();
    }

    public IPage<UserApiVO> getTagPageByGroup(StaffSelectUserForTaskDTO staffSelectUserForTaskDTO){

        IPage<UserApiVO> userPage = new Page<>(staffSelectUserForTaskDTO.getPageNum().longValue(), staffSelectUserForTaskDTO.getPageSize().longValue());

        userPage = userMapper.staffScreenUserForTask(userPage, staffSelectUserForTaskDTO);

        return userPage;
    }

}
