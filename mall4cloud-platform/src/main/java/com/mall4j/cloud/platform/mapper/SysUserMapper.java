package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.platform.dto.SysUserDTO;
import com.mall4j.cloud.platform.dto.SysUserPageDTO;
import com.mall4j.cloud.platform.model.SysUser;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.platform.vo.SysUserSimpleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lhd
 * @date 2020/12/22
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id获取当前登陆的商家用户信息
     *
     * @param userId 用户id
     * @return 商家用户信息
     */
    SysUserSimpleVO getSimpleByUserId(@Param("userId") Long userId);

    SysUserVO getByPhoneNum(@Param("phone") String phone);

    /**
     * 获取平台用户列表
     *
     * @param sysUserDTO 搜索参数
     * @return 平台用户列表
     */
    List<SysUserVO> listByShopId(@Param("sysUser") SysUserPageDTO sysUserDTO);

    /**
     * 根据用户id获取商家用户信息
     *
     * @param userId 用户id
     * @return 商家用户信息
     */
    SysUserVO getByUserId(@Param("userId") Long userId);

    /**
     * 保存商家用户信息
     *
     * @param sysUser
     */
    void save(@Param("sysUser") SysUser sysUser);

    /**
     * 更新平台用户信息
     *
     * @param sysUser
     */
    void update(@Param("sysUser") SysUser sysUser);

    /**
     * 根据平台用户id删除平台用户
     *
     * @param sysUserId
     */
    void deleteById(@Param("sysUserId") Long sysUserId);

    /**
     * 根据昵称统计已有的数量
     * @param nickName
     * @param sysUserId
     * @return
     */
    int countNickName(@Param("nickName") String nickName, @Param("sysUserId") Long sysUserId);

    /**
     * 根据用户id列表查名称
     * @param userIds 用户id列表
     * @return
     */
    List<SysUserVO> getUserName(@Param("userIds") List<Long> userIds);
}
