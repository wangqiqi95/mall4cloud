package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.dto.UserRightsDTO;
import com.mall4j.cloud.user.mapper.UserLevelMapper;
import com.mall4j.cloud.user.mapper.UserLevelRightsMapper;
import com.mall4j.cloud.user.mapper.UserRightsCouponMapper;
import com.mall4j.cloud.user.mapper.UserRightsMapper;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserLevel;
import com.mall4j.cloud.user.model.UserRights;
import com.mall4j.cloud.user.model.UserRightsCoupon;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.UserLevelVO;
import com.mall4j.cloud.user.vo.UserRightsVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户权益信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserRightsServiceImpl implements UserRightsService {

    @Autowired
    private UserRightsMapper userRightsMapper;

    @Autowired
    private UserRightsCouponMapper userRightsCouponMapper;

    @Autowired
    private UserLevelRightsMapper userLevelRightsMapper;

    @Autowired
    private UserLevelMapper userLevelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private UserLevelRightsService userLevelRightsService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private UserExtensionService userExtensionService;

    @Override
    public PageVO<UserRightsVO> page(PageDTO pageDTO, UserRightsDTO userRightsDTO) {
        return PageUtil.doPage(pageDTO, () -> userRightsMapper.list(userRightsDTO));
    }

    @Override
    @Cacheable(cacheNames = CacheNames.RIGHT_BY_RIGHTS_ID_KEY, key = "#rightsId")
    public UserRightsVO getByRightsId(Long rightsId) {
        return userRightsMapper.getByRightsId(rightsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserRightsDTO userRightsDTO) {
        //  checkIsRepeat(userRightsDTO);
        UserRights userRights = mapperFacade.map(userRightsDTO, UserRights.class);
        userRights.setStatus(StatusEnum.ENABLE.value());
        userRightsMapper.save(userRights);
        Long rightsId = userRights.getRightsId();
        List<Long> couponIds = userRightsDTO.getCouponIds();
        for (Long couponId : couponIds) {
            UserRightsCoupon userRightsCoupon = new UserRightsCoupon();
            userRightsCoupon.setCouponId(couponId);
            userRightsCoupon.setRightsId(rightsId);
            userRightsCouponMapper.save(userRightsCoupon);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserRightsDTO userRightsDTO) {
        //  checkIsRepeat(userRightsDTO);
        UserRights userRights = mapperFacade.map(userRightsDTO, UserRights.class);
        userRights.setStatus(StatusEnum.ENABLE.value());
        Long rightsId = userRights.getRightsId();
        userRightsCouponMapper.deleteByRightsId(rightsId);
        List<Long> couponIds = userRightsDTO.getCouponIds();
        for (Long couponId : couponIds) {
            UserRightsCoupon userRightsCoupon = new UserRightsCoupon();
            userRightsCoupon.setCouponId(couponId);
            userRightsCoupon.setRightsId(rightsId);
            userRightsCouponMapper.save(userRightsCoupon);
        }
        userRightsMapper.update(userRights);
        userLevelService.removeLevelListCache(0);
        userLevelService.removeLevelListCache(1);
        //删除有该权益的所有等级的权益列表缓存
        List<UserLevel> userLevelList = userLevelRightsService.listUserLevelIdByRightId(userRightsDTO.getRightsId());
        for (UserLevel userLevel : userLevelList) {
            userLevelService.removeLevelById(userLevel.getUserLevelId());
            userLevelService.removeLevelCache(userLevel.getUserLevelId(), userLevel.getLevelType(), userLevel.getLevel());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long rightsId) {
        //删除有该权益的所有等级的权益列表缓存
        List<UserLevel> userLevelList = userLevelRightsService.listUserLevelIdByRightId(rightsId);
        for (UserLevel userLevel : userLevelList) {
            userLevelService.removeLevelById(userLevel.getUserLevelId());
            userLevelService.removeLevelCache(userLevel.getUserLevelId(), userLevel.getLevelType(), userLevel.getLevel());
        }
        UserRights userRights = new UserRights();
        userRights.setRightsId(rightsId);
        userRights.setStatus(StatusEnum.DELETE.value());
        userRightsCouponMapper.deleteByRightsId(rightsId);
        userRightsMapper.deleteById(rightsId);
        userLevelRightsMapper.deleteByRightId(rightsId);
        userLevelService.removeLevelListCache(0);
        userLevelService.removeLevelListCache(1);
    }

    @Override
    public List<UserRightsVO> list(UserRightsDTO userRights) {
        return userRightsMapper.list(userRights);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.RIGHT_BY_RIGHTS_ID_KEY, key = "#rightsId")
    public void removeRightsCache(Long rightsId) {

    }

    @Override
    @CacheEvict(cacheNames = CacheNames.RIGHTS_BY_LEVEL_TYPE, key = "#levelType")
    public void removeRightsByLevelTypeCache(Integer levelType) {

    }

    @Override
    public List<UserRightsVO> listRightsByLevelType(Integer levelType) {
        Long userId = AuthUserContext.get().getUserId();
        return listRightsByLevelType(levelType, userId);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.RIGHTS_BY_LEVEL_TYPE, key = "#levelType")
    public List<UserRightsVO> listRightsByLevelType(Integer levelType, Long userId) {
        UserApiVO user = userService.getByUserId(userId);
        int nowLevel;
        if (Objects.equals(LevelTypeEnum.PAY_USER.value(), user.getLevelType()) && Objects.equals(LevelTypeEnum.ORDINARY_USER.value(), levelType)) {
            //如果用户是付费会员，要查看普通会员的权益
            UserExtension extension = userExtensionService.getByUserId(userId);
            nowLevel = userLevelService.getUserNormalLevel(extension.getGrowth());
        } else if (Objects.equals(LevelTypeEnum.ORDINARY_USER.value(), user.getLevelType()) && Objects.equals(LevelTypeEnum.PAY_USER.value(), levelType)) {
            //如果用户是普通会员，要查看付费会员的权益，查看最高等级的
            List<UserLevelVO> levelList = userLevelService.list(LevelTypeEnum.PAY_USER.value());
            if (levelList.size() >= 1){
                nowLevel = levelList.size();
            } else {
                throw new LuckException("未配置付费会员");
            }
        } else {
            nowLevel = user.getLevel();
        }
        List<UserRightsVO> total = userRightsMapper.listRightsByLevelType(levelType);
        // set存放处理过的rightsType
        Set<Integer> haveReadType = new HashSet<>();
        Set<String> haveReadName = new HashSet<>();
        //会员等级为key，权益内容为value
        Map<Integer, List<UserRightsVO>> res = new HashMap<>(total.size());
        for (UserRightsVO userRightsVO : total) {
            //非自定义权益根据类型去重
            if (userRightsVO.getLevel() >= nowLevel) {
                if (!Objects.isNull(userRightsVO.getRightsType()) && userRightsVO.getRightsType() != 0) {
                    if (haveReadType.contains(userRightsVO.getRightsType())) {
                        continue;
                    }
                    Integer level = userRightsVO.getLevel();
                    List<UserRightsVO> list = res.getOrDefault(level, new ArrayList<>());
                    list.add(userRightsVO);
                    res.put(level, list);
                    haveReadType.add(userRightsVO.getRightsType());
                } else {
                    //自定义类型根据名称去重
                    if (haveReadName.contains(userRightsVO.getRightsName())) {
                        continue;
                    }
                    Integer level = userRightsVO.getLevel();
                    List<UserRightsVO> list = res.getOrDefault(level, new ArrayList<>());
                    list.add(userRightsVO);
                    res.put(level, list);
                    haveReadName.add(userRightsVO.getRightsName());
                }
            }
        }
        Set<Map.Entry<Integer, List<UserRightsVO>>> entries = res.entrySet();
        List<UserRightsVO> returnResult = new ArrayList<>();
        for (Map.Entry<Integer, List<UserRightsVO>> entry : entries) {
            List<UserRightsVO> value = entry.getValue();
            returnResult.addAll(value);
        }
        return returnResult;
    }

    @Override
    public void cancelBindingCoupons(List<Long> couponIds) {
        List<Long> rightsIds = userRightsCouponMapper.listRightsIdByCouponIds(couponIds);
        // 没有要移除过期优惠券的权益
        if (CollUtil.isEmpty(rightsIds)) {
            return;
        }
        // 移除权益中过期的优惠券
        userRightsCouponMapper.deleteByCouponIds(couponIds);
        List<String> keys = new ArrayList<>();
        // 清除权益缓存
        for (Long rightsId : rightsIds) {
            keys.add(CacheNames.RIGHT_BY_RIGHTS_ID_KEY + CacheNames.UNION + rightsId);
        }
        keys.add(CacheNames.RIGHTS_BY_LEVEL_TYPE + CacheNames.UNION + LevelTypeEnum.ORDINARY_USER.value());
        keys.add(CacheNames.RIGHTS_BY_LEVEL_TYPE + CacheNames.UNION + LevelTypeEnum.PAY_USER.value());

        // 清除等级缓存
        List<UserLevel> userLevels = userLevelMapper.listByCouponIds(rightsIds);
        if (CollUtil.isNotEmpty(userLevels)) {
            for (UserLevel userLevel : userLevels) {
                keys.add(CacheNames.LEVEL_GET_KEY + CacheNames.UNION + userLevel.getUserLevelId());
                keys.add(CacheNames.LEVEL_GET_LIST_KEY + CacheNames.UNION + userLevel.getLevelType() + Constant.CATEGORY_INTERVAL + userLevel.getLevel());
            }
            keys.add(CacheNames.LEVEL_LIST_KEY + CacheNames.UNION + LevelTypeEnum.ORDINARY_USER.value());
            keys.add(CacheNames.LEVEL_LIST_KEY + CacheNames.UNION + LevelTypeEnum.PAY_USER.value());
        }
        RedisUtil.deleteBatch(keys);

    }

//    private void checkIsRepeat(UserRightsDTO userRightsDTO){
//       int count = userRightsMapper.countByRightsName(userRightsDTO.getRightsName(),userRightsDTO.getRightsId());
//       if (count > 0 ){
//           throw new LuckException("权益名称已存在，请重新输入");
//       }
//    }

}
