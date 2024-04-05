package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.user.dto.UserQueryDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.user.constant.VeekerAuditStatusEnum;
import com.mall4j.cloud.user.constant.VeekerStatusEnum;
import com.mall4j.cloud.user.dto.VeekerApplyDTO;
import com.mall4j.cloud.user.dto.VeekerAuditDTO;
import com.mall4j.cloud.user.dto.VeekerQueryDTO;
import com.mall4j.cloud.user.dto.VeekerStatusUpdateDTO;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.VeekerService;
import com.mall4j.cloud.user.vo.VeekerApplyVO;
import com.mall4j.cloud.user.vo.VeekerExcelVO;
import com.mall4j.cloud.user.vo.VeekerStatus;
import com.mall4j.cloud.user.vo.VeekerVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VeekerServiceImpl implements VeekerService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;
    @Autowired
    private UserStaffCpRelationMapper userStaffCpRelationMapper;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Override
    public PageVO<VeekerVO> page(PageDTO pageDTO, VeekerQueryDTO veekerQueryDTO) {
        PageVO<VeekerVO> pageVO = new PageVO<>();
        List<Long> staffIdList = getQueryStaffIdList(veekerQueryDTO);
        veekerQueryDTO.setStaffIdList(staffIdList);
        Long total = !CollectionUtils.isEmpty(staffIdList) && staffIdList.size() == 0 ? 0l : userMapper
                .countVeekerPageByParam(veekerQueryDTO);
        pageVO.setTotal(total);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        if (Objects.equals(0L, total)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }
        pageVO.setList(getVeekerVOList(veekerQueryDTO, pageDTO));
        return pageVO;
    }

    @Override
    public PageVO<VeekerVO> staffPage(PageDTO pageDTO, VeekerQueryDTO veekerQueryDTO) {
        PageVO<VeekerVO> pageVO = new PageVO<>();
        Long total = userMapper.countVeekerPageByParam(veekerQueryDTO);
        pageVO.setTotal(total);
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        if (Objects.equals(0L, total)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }
        pageVO.setList(getVeekerVOList(veekerQueryDTO, pageDTO));
        return pageVO;
    }

    @Override
    public void batchUpdateVeekerStatusByUserIds(VeekerStatusUpdateDTO veekerStatusUpdateDTO) {
        if (CollectionUtils.isEmpty(veekerStatusUpdateDTO.getIdList())) {
            throw new LuckException("用户id不能为空");
        }
        // 启用微客 校验当前用户是否是导购 如果是导购 不能启用
        if (veekerStatusUpdateDTO.getStatus() == 1) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("user_id", veekerStatusUpdateDTO.getIdList());
            List<User> users = userMapper.selectList(queryWrapper);
            List<String> mobiles = users.stream().map(User :: getPhone).collect(Collectors.toList());
            ServerResponseEntity<List<StaffVO>> staffListResp = staffFeignClient.getStaffListByMobiles(mobiles);
            if (staffListResp.isSuccess() && !CollectionUtils.isEmpty(staffListResp.getData())) {
                StaffVO staffVO = staffListResp.getData().stream().filter(s -> s.getStatus() == 0).findAny().orElse(null);
                if (Objects.nonNull(staffVO)) {
                    throw new LuckException("微客和导购身份只允许存在一个");
                }
            }
        }
        userMapper.batchUpdateVeekerStatusByUserIds(veekerStatusUpdateDTO.getIdList(),
                veekerStatusUpdateDTO.getStatus());
    }

    @Override
    public void batchUpdateVeekerAuditStatusByUserIds(VeekerAuditDTO veekerAuditDTO) {
        if (CollectionUtils.isEmpty(veekerAuditDTO.getIdList())) {
            throw new LuckException("微客ID集合不能为空");
        }
        userMapper.batchUpdateVeekerAuditStatusByUserIds(veekerAuditDTO.getIdList(),
                veekerAuditDTO.getStatus());
        if (veekerAuditDTO.getStatus() == 1) {
            userMapper.batchUpdateVeekerStatusByUserIds(veekerAuditDTO.getIdList(),
                    1);
            veekerAuditDTO.getIdList().forEach(userId -> {
                User user = userMapper.selectById(userId);
                if (null == user){
                    return;
                }
            });
        }
    }




    @Override
    public void unbindStaff(Long userId) {
        userMapper.unbindStaff(userId);
    }

    @Override
    public void export(HttpServletResponse response, VeekerQueryDTO veekerQueryDTO) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(PageDTO.DEFAULT_PAGE_NUM);
        pageDTO.setPageSize(PageDTO.MAX_PAGE_SIZE);
        PageVO<VeekerVO> pageVO = page(pageDTO, veekerQueryDTO);
        long total = pageVO.getTotal();
        if (total == 0) {
            throw new LuckException("无微客数据导出");
        }
        try {
            List<VeekerExcelVO> excelVOList = pageVO.getList().stream().map(this :: buildVeekerExcelVO)
                    .collect(Collectors.toList());
            if (total > PageDTO.MAX_PAGE_SIZE) {
                // 总共可以分多少页
                Integer pages = pageVO.getPages();
                for (int i = 2; i <= pages; i++) {
                    PageDTO page = new PageDTO();
                    page.setPageNum(i);
                    page.setPageSize(PageDTO.MAX_PAGE_SIZE);
                    excelVOList.addAll(getVeekerVOList(veekerQueryDTO, page).stream().map(this :: buildVeekerExcelVO)
                            .collect(Collectors.toList()));
                }
            }
            ExcelUtil.soleExcel(response, excelVOList, VeekerExcelVO.EXCEL_NAME, VeekerExcelVO.MERGE_ROW_INDEX,
                    VeekerExcelVO.MERGE_COLUMN_INDEX, VeekerExcelVO.class);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public VeekerApplyVO apply(VeekerApplyDTO veekerApplyDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = userInfoInTokenBO.getUserId();
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        if (userApiVO.getVeekerAuditStatus() == 0) {
            throw new LuckException("您申请的微客正在审核中，不能重复申请");
        }
        if (userApiVO.getVeekerAuditStatus() == 1) {
            throw new LuckException("您已经是微客，不需要申请");
        }
        ServerResponseEntity<StaffVO> checkUserIsStaff = staffFeignClient.getStaffByMobile(userApiVO.getPhone());
        if (checkUserIsStaff.isSuccess()) {
            StaffVO staffVO = checkUserIsStaff.getData();
            if (Objects.nonNull(staffVO) && staffVO.getStatus() == 0) {
                throw new LuckException("导购不能申请成为微客");
            }
        }
        VeekerApplyVO veekerApplyVO = new VeekerApplyVO();
        veekerApplyVO.setApplyStatus(1);
        // 当前会员所属导购非当前邀请码的导购
        Long applyStaffId = veekerApplyDTO.getStaffId() != null ? veekerApplyDTO.getStaffId() : 0l;
        Long staffId = userApiVO.getStaffId() != null ? userApiVO.getStaffId() : 0l;
        log.info("applyStaffId : {} staffId: {} ", applyStaffId, staffId);
        if (staffId > 0 && applyStaffId.longValue() != staffId.longValue()) {
            TentacleContentDTO tentacleContent = new TentacleContentDTO();
            tentacleContent.setBusinessId(userApiVO.getStaffId());
            tentacleContent.setTentacleType(4);
            ServerResponseEntity<TentacleContentVO> tentacleContentVO = tentacleContentFeignClient.findOrCreateByContent(tentacleContent);
            if (!tentacleContentVO.isSuccess()) {
                throw new LuckException("触点生成失败");
            }
            ServerResponseEntity<StaffVO> staffVO = staffFeignClient.getStaffById(userApiVO.getStaffId());
            if (!staffVO.isSuccess()) {
                throw new LuckException("导购获取失败");
            }
            log.info("VeekerApplyVO apply 导购获取信息：{}", Objects.nonNull(staffVO.getData())?JSON.toJSONString(staffVO.getData()):null);
            veekerApplyVO.setApplyStatus(0);
            veekerApplyVO.setStoreId(staffVO.getData().getStoreId());
            veekerApplyVO.setStoreName(staffVO.getData().getStoreName());
            veekerApplyVO.setStaffName(staffVO.getData().getStaffName());
            veekerApplyVO.setTentacleNo(tentacleContentVO.getData().getTentacleNo());
            return veekerApplyVO;
        }
        // 导购邀请
        if (applyStaffId > 0) {
            staffInvite(userApiVO, veekerApplyDTO);
            return veekerApplyVO;
        }
        // 门店邀请
        storeInvite(userApiVO);
        return veekerApplyVO;
    }

    @Override
    public boolean isVeeker(Long userId) {
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        if (userApiVO.getVeekerStatus() != null && userApiVO.getVeekerStatus() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public VeekerStatus getVeekerStatus(Long userId) {
        UserApiVO userApiVO = userMapper.getByUserId(userId);
        VeekerStatus veekerStatus = new VeekerStatus();
        veekerStatus.setVeekerStatus(userApiVO.getVeekerStatus());
        veekerStatus.setVeekerAuditStatus(userApiVO.getVeekerAuditStatus());
        return veekerStatus;
    }

    @Override
    public Integer countStaffWeeker(UserQueryDTO userQueryDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", userQueryDTO.getStaffId());
        queryWrapper.eq("veeker_status", 1);
        if (null != userQueryDTO.getStartTime() && null != userQueryDTO.getEndTime()){
            queryWrapper.between("create_time", userQueryDTO.getStartTime(), userQueryDTO.getEndTime());
        }
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public List<UserApiVO> listStaffWeeker(UserQueryDTO userQueryDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staff_id", userQueryDTO.getStaffId());
        queryWrapper.eq("veeker_status", 1);
        if (null != userQueryDTO.getStartTime() && null != userQueryDTO.getEndTime()){
            queryWrapper.between("create_time", userQueryDTO.getStartTime(), userQueryDTO.getEndTime());
        }
        List<User> users = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(users)) {
            return new ArrayList<>();
        }
        return users.stream().map(user -> {
            UserApiVO vo = new UserApiVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 员工微客
     * @param userApiVO
     * @param veekerApplyDTO
     * @return
     */
    private boolean staffInvite(UserApiVO userApiVO, VeekerApplyDTO veekerApplyDTO) {
        log.info("员工微客: {}", JSONObject.toJSONString(veekerApplyDTO));
        Long staffId = userApiVO.getStaffId();
        Long inviteStaffId = veekerApplyDTO.getStaffId();
        if (staffId != null && staffId > 0) {
            // 邀请导购为该会员的所属导购
            if (staffId.longValue() == inviteStaffId.longValue()) {
                User user = new User();
                user.setUserId(userApiVO.getUserId());
                user.setVeekerAuditStatus(0);
                user.setVeekerApplyTime(new Date());
                userMapper.updateUser(user);
                return true;
            }
        } else {
            User user = new User();
            user.setUserId(userApiVO.getUserId());
            user.setStaffId(inviteStaffId);
            user.setVeekerAuditStatus(0);
            user.setVeekerApplyTime(new Date());
            userMapper.updateUser(user);
            return true;
        }
        return false;
    }

    /**
     * 门店微客
     * @param userApiVO
     * @return
     */
    private boolean storeInvite(UserApiVO userApiVO) {
        log.info("门店微客: {}", JSONObject.toJSONString(userApiVO));
        Long staffId = userApiVO.getStaffId();
        if (staffId != null && staffId > 0) {
            return false;
        }
        User user = new User();
        user.setUserId(userApiVO.getUserId());
        user.setVeekerAuditStatus(0);
        user.setVeekerApplyTime(new Date());
        userMapper.updateUser(user);
        return true;
    }

    private VeekerExcelVO buildVeekerExcelVO(VeekerVO veekerVO) {
        VeekerExcelVO veekerExcelVO = mapperFacade.map(veekerVO, VeekerExcelVO.class);
        veekerExcelVO.setVeekerStatus(VeekerStatusEnum.desc(veekerVO.getVeekerStatus()));
        veekerExcelVO.setVeekerAuditStatus(VeekerAuditStatusEnum.desc(veekerVO.getVeekerAuditStatus()));
        if (veekerVO.getAddWechat() != null) {
            veekerExcelVO.setAddWechat(veekerVO.getAddWechat() == 1 ? "是" : "否");
        }
        if (veekerVO.getStaffStatus() != null) {
            veekerExcelVO.setStaffStatus(veekerVO.getStaffStatus() == 1 ? "注销" : "正常");
        }
        return veekerExcelVO;
    }

    private List<Long> getQueryStaffIdList(VeekerQueryDTO veekerQueryDTO) {
        boolean flag = false;
        StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
        if (StrUtil.isNotBlank(veekerQueryDTO.getStaffName())) {
            staffQueryDTO.setStaffName(veekerQueryDTO.getStaffName());
            flag = true;
        }
        if (StrUtil.isNotBlank(veekerQueryDTO.getStaffMobile())) {
            staffQueryDTO.setMobile(veekerQueryDTO.getStaffMobile());
            flag = true;
        }
        if (StrUtil.isNotBlank(veekerQueryDTO.getStaffNo())) {
            staffQueryDTO.setStaffNo(veekerQueryDTO.getStaffNo());
            flag = true;
        }
        if (veekerQueryDTO.getStaffStatus() != null && veekerQueryDTO.getStaffStatus() != -1) {
            staffQueryDTO.setStatus(veekerQueryDTO.getStaffStatus());
            flag = true;
        }
        if (!CollectionUtils.isEmpty(veekerQueryDTO.getStaffStoreIdList())) {
            staffQueryDTO.setStoreIdList(veekerQueryDTO.getStaffStoreIdList());
            flag = true;
        }
        if (flag) {
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (!staffData.isSuccess() || CollectionUtils.isEmpty(staffData.getData())){
                return null;
            }
            return staffData.getData().stream().map(StaffVO :: getId).collect(Collectors.toList());
        }
        return null;
    }

    private List<VeekerVO> getVeekerVOList(VeekerQueryDTO veekerQueryDTO, PageDTO pageDTO) {
        List<VeekerVO> veekerVOList = userMapper.listVeekerByParam(veekerQueryDTO, new PageAdapter(pageDTO));
        if (!CollectionUtils.isEmpty(veekerVOList)) {
            List<Long> userIdList = veekerVOList.stream().map(VeekerVO :: getUserId).collect(Collectors.toList());
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffIdList(veekerVOList.stream().map(VeekerVO :: getStaffId).collect(Collectors.toList()));
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            Map<Long, StaffVO> staffVOMap = new HashMap<>();
            if (staffData.isSuccess() && !CollectionUtils.isEmpty(staffData.getData())){
                staffVOMap = staffData.getData().stream().collect(Collectors.toMap(StaffVO :: getId, Function.identity()));
            }
            List<Long> storeIdList = veekerVOList.stream().filter(s -> s.getStoreId() != null).map(VeekerVO :: getStoreId)
                    .collect(Collectors.toList());
            Map<Long, StoreVO> storeVOMap = new HashMap<>();
            ServerResponseEntity<List<StoreVO>> storeResp = storeFeignClient.listBypByStoreIdList(storeIdList);
            if (storeResp.isSuccess()) {
                storeVOMap = storeResp.getData().stream().collect(Collectors.toMap(StoreVO :: getStoreId, Function.identity()));
            }
            Map<Long, List<UserStaffCpRelation>> usRelationMap = userStaffCpRelationMapper.listByUserIdList(userIdList).stream()
                    .collect(Collectors.groupingBy(UserStaffCpRelation :: getUserId));
            Map<Long, StoreVO> finalStoreVOMap = storeVOMap;
            Map<Long, StaffVO> finalStaffVOMap = staffVOMap;
            veekerVOList.stream().forEach(v -> {
                StaffVO staffVO = finalStaffVOMap.get(v.getStaffId());
                if (Objects.nonNull(staffVO)) {
                    v.setStaffName(staffVO.getStaffName());
                    v.setStaffNo(staffVO.getStaffNo());
                    v.setStaffMobile(staffVO.getMobile());
                    v.setStaffStatus(staffVO.getStatus());
                    v.setStaffStoreId(staffVO.getStoreId());
                    v.setStaffStoreName(staffVO.getStoreName());
                    v.setStaffStoreCode(staffVO.getStoreCode());
                }
                StoreVO storeVO = finalStoreVOMap.get(v.getStoreId());
                if (Objects.nonNull(storeVO)) {
                    v.setStoreCode(storeVO.getStoreCode());
                    v.setStoreName(storeVO.getName());
                }
                v.setAddWechat(usRelationMap.containsKey(v.getUserId()) ? 1 : 0);
            });
        }
        return veekerVOList;
    }
}
