package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserLevelTermDTO;
import com.mall4j.cloud.user.mapper.UserLevelTermMapper;
import com.mall4j.cloud.user.model.UserLevelTerm;
import com.mall4j.cloud.user.service.UserLevelTermService;
import com.mall4j.cloud.user.vo.UserLevelTermVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
@Service
public class UserLevelTermServiceImpl implements UserLevelTermService {

    @Autowired
    private UserLevelTermMapper userLevelTermMapper;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<UserLevelTerm> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userLevelTermMapper.list());
    }

    @Override
    public UserLevelTerm getByLevelTermId(Long levelTermId) {
        return userLevelTermMapper.getByLevelTermId(levelTermId);
    }

    @Override
    public void updateBatch(Long userLevelId, List<UserLevelTermDTO> userLevelTermDTO) {
        List<Long> userLevelTermIdsDb = userLevelTermDTO.stream().map(UserLevelTermDTO::getLevelTermId).collect(Collectors.toList());
        List<UserLevelTerm> updateList = new ArrayList<>();
        List<UserLevelTerm> saveList = new ArrayList<>();
        List<Long> userLevelTermIds = new ArrayList<>();
        for (UserLevelTermDTO levelTermDTO : userLevelTermDTO) {
            UserLevelTerm userLevelTerm = mapperFacade.map(levelTermDTO, UserLevelTerm.class);
            userLevelTerm.setUserLevelId(userLevelId);
            if (userLevelTerm.getLevelTermId() != null && userLevelTermIdsDb.contains(userLevelTerm.getLevelTermId())) {
                if (Objects.nonNull(userLevelTerm.getTermType()) || Objects.nonNull(userLevelTerm.getNeedAmount())) {
                    updateList.add(userLevelTerm);
                }
                userLevelTermIds.add(userLevelTerm.getLevelTermId());
                continue;
            }
            saveList.add(userLevelTerm);
        }
        //保存新增
        if (CollUtil.isNotEmpty(saveList)) {
            saveBatch(userLevelId, saveList);
        }
        //更新
        if (CollUtil.isNotEmpty(updateList)) {
            for (UserLevelTerm userLevelTerm : updateList) {
                if (userLevelTerm.getTermType() != null) {
                    userLevelTermMapper.update(userLevelTerm);
                }
            }
        }
        //删除
        userLevelTermIdsDb.removeAll(userLevelTermIds);
        if (CollUtil.isNotEmpty(userLevelTermIdsDb)) {
            userLevelTermMapper.deleteBatch(userLevelTermIdsDb);
        }
    }

    @Override
    public void saveBatch(Long userLevelId, List<UserLevelTerm> userLevelTerms) {
        if (CollUtil.isEmpty(userLevelTerms)) {
            return;
        }
        for (UserLevelTerm userLevelTerm : userLevelTerms) {
            if (Objects.isNull(userLevelTerm.getLevelTermId())) {
                userLevelTerm.setLevelTermId(Constant.DEFAULT_ID);
            }
            if (userLevelTerm.getTermType() != null) {
                userLevelTermMapper.save(userLevelTerm);
            }
        }
           // userLevelTermMapper.saveBatch(userLevelTerms);
    }

    @Override
    public void save(UserLevelTerm userLevelTerm) {
        userLevelTermMapper.save(userLevelTerm);
    }

    @Override
    public void update(UserLevelTerm userLevelTerm) {
        userLevelTermMapper.update(userLevelTerm);
    }

    @Override
    public void deleteById(Long levelTermId) {
        userLevelTermMapper.deleteById(levelTermId);
    }

    @Override
    public void deleteBatch(Long userLevelId) {
        List<UserLevelTerm> userLevelTerms = userLevelTermMapper.getListByUserLevelId(userLevelId);
        ArrayList<Long> levelTermIds = new ArrayList<>();
        for (UserLevelTerm userLevelTerm : userLevelTerms) {
            levelTermIds.add(userLevelTerm.getLevelTermId());
        }
        userLevelTermMapper.deleteBatch(levelTermIds);
    }

    @Override
    public List<UserLevelTerm> getListByUserLevelId(Long userLevelId) {
        return userLevelTermMapper.getListByUserLevelId(userLevelId);
    }

    @Override
    public List<UserLevelTermVO> getAmountAndTypeByUserLevelId(Long userLevelId) {
        return userLevelTermMapper.getAmountAndTypeByUserLevelId(userLevelId);
    }

}
