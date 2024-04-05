package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.cp.CpUserCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.WxCpUserDTO;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.api.biz.feign.CrmFeignClient;
import com.mall4j.cloud.api.platform.constant.StaffRoleTypeEnum;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.StaffBindQiWeiDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.vo.*;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.ImportStaffWeChatDto;
import com.mall4j.cloud.platform.manager.WxCpStaffManager;
import com.mall4j.cloud.platform.mapper.OrganizationMapper;
import com.mall4j.cloud.platform.mapper.StaffMapper;
import com.mall4j.cloud.platform.mapper.SysUserMapper;
import com.mall4j.cloud.platform.model.*;
import com.mall4j.cloud.platform.service.*;

import javax.annotation.Resource;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.error.YAMLException;

import javax.servlet.http.HttpServletResponse;
import java.io.UTFDataFormatException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 员工信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Service
@Slf4j
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Resource
    private StaffMapper staffMapper;
    @Autowired
    private MapperFacade mapperFacade;
//    @Autowired
//    private TzStoreService tzStoreService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private UserFeignClient userFeignClient;

//    @Autowired
//    private OnsMQTemplate soldUploadExcelTemplate;
    @Resource
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private SysUserAccountService SysUserAccountService;

    @Autowired
    TzTagService tzTagService;
    @Autowired
    private WxCpStaffManager wxCpStaffManager;
    @Autowired
    private StaffOrgRelService staffOrgRelService;
    @Autowired
    private CrmFeignClient crmFeignClient;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;

    @Override
    public PageVO<StaffVO> page(PageDTO pageDTO, StaffQueryDTO queryDTO) {

        PageVO<StaffVO> pageVO = new PageVO();
        pageVO.setTotal(0L);
        pageVO.setPages(0);
        pageVO.setList(Collections.emptyList());
        if(queryDTO.getSort()==null){
            queryDTO.setSort(0);
        }
        if(StrUtil.isNotEmpty(queryDTO.getPath())){
            queryDTO.setOrgIds(organizationMapper.getStoreOrgIdByPath(queryDTO.getPath()));
        }
        if(Objects.nonNull(queryDTO.getQiweiUserStatus()) && queryDTO.getQiweiUserStatus()==5){
            queryDTO.setIsDelete(null);
            queryDTO.setStatus(null);
            queryDTO.setQiweiUserStatus(null);
            queryDTO.setQiweiUserStatusList(Arrays.asList(QiweiUserStatus.DEL.getCode(),QiweiUserStatus.REMOVE.getCode()));
        }
        PageVO<Staff> staffPageVO = PageUtil.doPage(pageDTO, () -> staffMapper.listByStaffQueryDTO(queryDTO));
        List<Staff> staffList = staffPageVO.getList();
        if (!CollectionUtils.isEmpty(staffList)) {
            List<StaffVO> staffVOList = mapperFacade.mapAsList(staffList,StaffVO.class);

            List<Long> staffIds = staffList.stream().map(Staff :: getId).collect(Collectors.toList());
            List<StaffOrgVO> staffOrgVOS = staffOrgRelService.getStaffAndOrgs(staffIds);
            Map<Long, List<StaffOrgVO>> staffOrgVOMaps = staffOrgVOS.stream().collect(Collectors.groupingBy(StaffOrgVO::getStaffId));
            for (StaffVO staffVO : staffVOList) {
                List<StaffOrgVO> staffOrgVOS1 = staffOrgVOMaps.get(staffVO.getId());
                staffVO.setOrgs(staffOrgVOS1);
            }
            pageVO.setList(staffVOList);
            pageVO.setPages(staffPageVO.getPages());
            pageVO.setTotal(staffPageVO.getTotal());
        }
        return pageVO;
    }

    @Override
    public List<StaffVO> selectList(StaffQueryDTO queryDTO) {
        List<Staff> staffList = staffMapper.listByStaffQueryDTO(queryDTO);
        if (!CollectionUtils.isEmpty(staffList)) {
            List<StaffVO> staffVOS=staffList.stream().map(staff -> mapperFacade.map(staff, StaffVO.class)).collect(Collectors.toList());
            return staffVOS;
        }
        return Collections.emptyList();
    }


    /**
     * 批量粘贴(手机号、工号)
     * @param queryDTO
     * @return
     */
    @Override
    public List<StaffVO> staffListByMN(StaffQueryDTO queryDTO) {
        List<Staff> staffList = staffMapper.listByStaffQueryDTO(queryDTO);
        if (!CollectionUtils.isEmpty(staffList)) {
            return staffList.stream().map(staff -> mapperFacade.map(staff, StaffVO.class)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public StaffVO getById(Long id) {
        Staff staff = staffMapper.getById(id);
        return buildStaffVO(staff);
    }

    @Override
    public StaffInfoVO info(Long id) {
        StaffInfoVO staffInfoVO = new StaffInfoVO();
        staffInfoVO.setStaffVO(getById(id));

        List<TzTagDetailVO> tzTagDetailVOS = tzTagService.listTagByStaffId(id);
        staffInfoVO.setTagDetailVOList(tzTagDetailVOS);

        List<Long> storeIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(tzTagDetailVOS)){
            for (TzTagDetailVO tzTagDetailVO : tzTagDetailVOS) {
                if (CollectionUtil.isNotEmpty(tzTagDetailVO.getStores())){
                    storeIds.addAll(tzTagDetailVO.getStores().stream().map(TzTagStoreDetailVO::getStoreId).collect(Collectors.toList()));
                }
            }
            storeIds.stream().distinct();
        }
        staffInfoVO.setTagStoreIds(storeIds);

        return staffInfoVO;
    }

    @Override
    public List<StaffVO> getByIds(List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        return staffMapper.getByIds(ids);
    }

    @Override
    public StaffVO getByQiWeiUserId(String qiWeiUserId) {
        Staff staff = staffMapper.getByQiWeiUserId(qiWeiUserId);
        StaffVO staffVO=buildStaffVO(staff);
        if (staffVO == null) {
            return null;
        }

        if(Objects.nonNull(staffVO.getOrgId())){
//            Organization organization=organizationService.getByOrgId(staffVO.getOrgId());
//            if(Objects.nonNull(organization)){
//                staffVO.setOrgName(organization.getShortName());
//            }
        }
        return staffVO;
    }

    @Override
    public StaffVO getByMobile(String mobile) {
        List<Staff> staffs = staffMapper.getByMobile(mobile);
        if (CollectionUtil.isNotEmpty(staffs)) {
            if (staffs.size() > 1) {
                for (int i = 0; i < staffs.size(); i++) {
                    if (staffs.get(i).getStatus() == 0) {
                        return buildStaffVO(staffs.get(i));
                    }
                }
            }
            return buildStaffVO(staffs.get(0));
        }
        return null;
    }

    @Override
    public StaffVO getByStaffCode(String staffCode) {
        Staff staff = staffMapper.getByStaffCode(staffCode);
        return buildStaffVO(staff);
    }

    /**
     * 保存员工信息
     * @param staff 员工信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Staff staff) {

        this.save(staff);

        //员工关联部门
        List<Long> orgIds=Arrays.stream(staff.getOrgId().split(",")).map(s->Long.parseLong(s)).collect(Collectors.toList());
        List<StaffOrgRel> orgRels=new ArrayList<>();
        for (Long orgId : orgIds) {
            StaffOrgRel orgRel=new StaffOrgRel();
            orgRel.setOrgId(orgId);
            orgRel.setStaffId(staff.getId());
            orgRel.setCreateBy(AuthUserContext.get().getUsername());
            orgRel.setCreateDate(new Date());
            orgRel.setIsDelete(0);
            orgRels.add(orgRel);
        }
        staffOrgRelService.saveBatch(orgRels);

//        if(staff.getOpenPlatform()==1){
//            //TODO将员工信息同步至企微
//            ServerResponseEntity<WxCpUserDTO> responseEntity=wxCpStaffManager.pushQiWeiUser(CpUserCreateDTO.builder()
//                    .email(staff.getEmail())
//                    .mobile(staff.getMobile())
//                    .orgIds(staff.getOrgId())
//                    .staffId(staff.getId().toString())
//                    .staffNo(staff.getStaffNo())
//                    .staffName(staff.getStaffName())
//                    .build());
//            if(responseEntity.isFail()){
//                log.info("将员工信息同步至企微: {}",responseEntity.getMsg());
//                //删除账号信息
//                SysUserAccountService.removeByPhone(staff.getMobile());
//
//                throw new LuckException(responseEntity.getMsg());
//            }else{
//                staff.setQiWeiUserId(responseEntity.getData().getUserid());
//                //激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
//                staff.setQiWeiUserStatus(Objects.nonNull(responseEntity.getData().getStatus())?responseEntity.getData().getStatus():4);
//                staff.setAvatar(responseEntity.getData().getAvatar());
//                this.updateById(staff);
//
//                //TODO 发送邀请成员加入
//
//                //TODO 推送数云
//                PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
//                dto.setMsgType("2");
//                dto.setChangetype("create_user");
//                dto.setStaffUserId(staff.getQiWeiUserId());
//                crmFeignClient.pushCDPStaffCpMsg(dto);
//            }
//        }
    }

    @Override
    public void openPlatform(Long sysUserId) {
        Staff staff = staffMapper.getBySysUserId(sysUserId.toString());
        if(Objects.isNull(staff)){
            throw new LuckException("员工未找到");
        }
        if(Objects.nonNull(staff.getOpenPlatform()) && staff.getOpenPlatform()==1){
            throw new LuckException("员工已开通导购");
        }
        log.info("pushQiWeiUser-->{}",JSON.toJSONString(staff));
        //TODO将员工信息同步至企微
        ServerResponseEntity<WxCpUserDTO> responseEntity=wxCpStaffManager.pushQiWeiUser(CpUserCreateDTO.builder()
                .email(staff.getEmail())
                .mobile(staff.getMobile())
                .orgIds(staff.getOrgId())
                .staffId(staff.getId().toString())
                .staffNo(staff.getStaffNo())
                .staffName(staff.getStaffName())
                .build());
        if(responseEntity.isFail()){
            log.info("将员工信息同步至企微: {}",responseEntity.getMsg());
            throw new LuckException(ResponseEnum.EXCEPTION,responseEntity.getMsg());
        }else{
            staff.setQiWeiUserId(responseEntity.getData().getUserid());
            staff.setQiWeiUserStatus(responseEntity.getData().getStatus());
            staff.setAvatar(responseEntity.getData().getAvatar());
            staff.setOpenPlatform(1);
            this.updateById(staff);

            //修改用户表为已开通导购
            SysUser sysUser=new SysUser();
            sysUser.setOpenPlatform(1);
            sysUser.setSysUserId(sysUserId);
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateBy(AuthUserContext.get().getUsername()+"开通导购");
            sysUserMapper.updateById(sysUser);

            //TODO 推送数云
            PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
            dto.setMsgType("2");
            dto.setChangetype("update_user");
            dto.setStaffUserId(staff.getQiWeiUserId());
            crmFeignClient.pushCDPStaffCpMsg(dto);
        }
    }

    @Transactional
    @Override
    public void saveBatch(List<Staff> staffs) {
        staffMapper.saveBatch(staffs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Staff staffUpdate,boolean pushWx) {
        Staff staff = staffMapper.getBySysUserId(staffUpdate.getSysUserId());
        if(Objects.isNull(staff)){
            throw new LuckException("员工未找到");
        }
        Long id=staff.getId();
        String staffNo=staff.getStaffNo();
        staff=mapperFacade.map(staffUpdate,Staff.class);
        staff.setId(id);
        this.updateById(staffUpdate);
        staff.setStaffNo(staffNo);

        //员工关联部门
        List<Long> orgIds=Arrays.stream(staff.getOrgId().split(",")).map(s->Long.parseLong(s)).collect(Collectors.toList());
        List<StaffOrgRel> orgRels=new ArrayList<>();
        for (Long orgId : orgIds) {
            StaffOrgRel orgRel=new StaffOrgRel();
            orgRel.setOrgId(orgId);
            orgRel.setStaffId(staff.getId());
            orgRel.setCreateBy(AuthUserContext.get().getUsername());
            orgRel.setCreateDate(new Date());
            orgRel.setIsDelete(0);
            orgRels.add(orgRel);
        }
        staffOrgRelService.deleteByStaffId(id);
        staffOrgRelService.saveBatch(orgRels);

        if(staff.getOpenPlatform()==1 && pushWx){
            log.info("pushQiWeiUser-->{}",JSON.toJSONString(staff));
            //TODO将员工信息同步至企微
            ServerResponseEntity<WxCpUserDTO> responseEntity=wxCpStaffManager.pushQiWeiUser(CpUserCreateDTO.builder()
                    .email(staff.getEmail())
                    .mobile(staff.getMobile())
                    .orgIds(staff.getOrgId())
                    .staffId(staff.getId().toString())
                    .staffNo(staff.getStaffNo())
                    .staffName(staff.getStaffName())
                    .build());
            if(responseEntity.isFail()){
                log.info("将员工信息同步至企微: {}",responseEntity.getMsg());
                throw new LuckException(ResponseEnum.EXCEPTION,responseEntity.getMsg());
            }else{
                staff.setQiWeiUserId(responseEntity.getData().getUserid());
                staff.setQiWeiUserStatus(responseEntity.getData().getStatus());
                staff.setAvatar(responseEntity.getData().getAvatar());
                this.updateById(staff);

                //TODO 推送数云
                PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
                dto.setMsgType("2");
                dto.setChangetype("update_user");
                dto.setStaffUserId(staff.getQiWeiUserId());
                crmFeignClient.pushCDPStaffCpMsg(dto);
            }
        }
    }


    @Override
    public void deleteSysUserId(String sysUserId) {
        Staff staff = staffMapper.getBySysUserId(sysUserId);
        if(Objects.isNull(staff)){
            throw new LuckException("员工未找到");
        }
        Integer qiWeiUserStatus=staff.getQiWeiUserStatus();
        staff.setIsDelete(1);
        staff.setQiWeiUserStatus(5);
        staff.setUpdateTime(new Date());
        staff.setUpdateBy(AuthUserContext.get().getUsername());
        staff.setUpdateTime(new Date());
        this.updateById(staff);

        //TODO 删除企微员工
        if(CharSequenceUtil.isNotEmpty(staff.getQiWeiUserId()) && (Objects.nonNull(qiWeiUserStatus) && qiWeiUserStatus!=5)){
            try {
                //删除需要先获取到员工信息用于推送数云
                WxCpUser wxCpUser=wxCpStaffManager.getWxCpUserById(staff.getQiWeiUserId());
                ServerResponseEntity<Void> responseEntity=wxCpStaffManager.deleteByUserId(staff.getQiWeiUserId());
                log.info("删除企微员工执行结果：{}",responseEntity.toString());
                if(responseEntity.isSuccess()){
                    //TODO 推送数云
                    PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
                    dto.setMsgType("2");
                    dto.setChangetype("delete_user");
                    dto.setStaffUserId(staff.getQiWeiUserId());
                    dto.setWxCpUser(wxCpUser);
                    crmFeignClient.pushCDPStaffCpMsg(dto);
                }
            }catch (Exception e){
                log.info("删除企微员工失败：{}",e);
            }
        }

        //更新好友关系表数据
        userStaffCpRelationFeignClient.staffDimission(Arrays.asList(staff.getId()));
    }

    @Override
    public StaffVO getBySysUserId(String sysUserId) {
        LambdaQueryWrapper<Staff> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Staff::getSysUserId,sysUserId);
        Staff staff=this.getOne(lambdaQueryWrapper,false);
        return mapperFacade.map(staff,StaffVO.class);
    }

    @Override
    public void updateCpMsgAuditByUserId(String qiWeiUserId,Integer cpMsgAudit) {
        Staff staff = staffMapper.getByQiWeiUserId(qiWeiUserId);
        if(Objects.isNull(staff)){
            throw new LuckException("员工未找到");
        }
        staff.setCpMsgAudit(cpMsgAudit);
        staff.setUpdateTime(new Date());
        this.updateById(staff);
    }

    @Override
    public int updateBatchWeChatNoByStaffNo(List<ImportStaffWeChatDto> staffs) {
        List<Staff> list = staffMapper.getStaffByWeChatNo(staffs.stream().map(ImportStaffWeChatDto::getWeChatNo).collect(Collectors.toList()));
        if(CollectionUtil.isNotEmpty(list)){
            Map<String, Staff> staffMap = list.stream().collect(Collectors.toMap(Staff::getWeChatNo, Function.identity(),(oldValue, newValue) -> newValue));
            staffs.removeIf(staff -> staffMap.containsKey(staff.getWeChatNo()));
        }
        List<ImportStaffWeChatDto> staffWeChatDtos = staffs.stream().distinct().collect(Collectors.toList());
        log.info("----批量导入修改微信号:{}", JSON.toJSONString(staffWeChatDtos));
        return CollectionUtil.isNotEmpty(staffs)?staffMapper.updateBatchWeChatNoByStaffNo(staffWeChatDtos):0;
    }


    @Override
    public List<StaffVO> findByStaffQueryDTO(StaffQueryDTO staffQueryDTO) {
        List<Staff> staffList = staffMapper.listByStaffQueryDTO(staffQueryDTO);
        List<Long> staffIds = staffList.stream().map(Staff :: getId).collect(Collectors.toList());
        List<StaffOrgVO> staffOrgVOS = staffOrgRelService.getStaffAndOrgs(staffIds);
        Map<Long, List<StaffOrgVO>> staffOrgVOMaps = staffOrgVOS.stream().collect(Collectors.groupingBy(StaffOrgVO::getStaffId));
//        Map<Long,Organization> tzStoreMap = organizationService.getListByIds(staffIds).stream().collect(Collectors.toMap(Organization :: getOrgId,
//                Function.identity()));
//        Map<Long,TzStore> tzStoreMap = tzStoreService.listByStoreIdList(storeIdList).stream().collect(Collectors.toMap(TzStore :: getStoreId,
//                Function.identity()));
        return staffList.stream().map(staff -> {
            StaffVO staffVO = mapperFacade.map(staff, StaffVO.class);
            List<StaffOrgVO> staffOrgVOS1 = staffOrgVOMaps.get(staff.getId());
            staffVO.setOrgs(staffOrgVOS1);
            return staffVO;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer countByStaffNum(StaffQueryDTO staffQueryDTO) {
        return staffMapper.countByStaffNum(staffQueryDTO);
    }

    @Override
    public void bindStaffQiWeiUserId(StaffBindQiWeiDTO staffBindQiWeiDTO) {
        Staff staff = staffMapper.getById(staffBindQiWeiDTO.getStaffId());
        if (Objects.nonNull(staff)) {
            staff.setQiWeiUserId(staffBindQiWeiDTO.getQiWeiUserId());
            staff.setQiWeiUserStatus(staffBindQiWeiDTO.getQiWeiUserStatus());
            staff.setAvatar(staffBindQiWeiDTO.getAvatar());
            staff.setGender(staffBindQiWeiDTO.getGender());
            staff.setQrCode(staffBindQiWeiDTO.getQrCode());
        }
        staffMapper.update(staff);
    }

    @Override
    public List<Staff> getStaffListByMobiles(List<String> mobiles) {
        return staffMapper.getStaffListByMobiles(mobiles);
    }

    @Override
    public List<Staff> getStaffListByStaffNos(List<String> staffNos) {
        return staffMapper.getStaffListByStaffNos(staffNos);
    }

    private StaffVO buildStaffVO(Staff staff) {
        if (Objects.nonNull(staff)) {
            StaffVO staffVO = mapperFacade.map(staff, StaffVO.class);
            List<StaffOrgVO> staffOrgVOS = staffOrgRelService.getStaffAndOrgs(Lists.newArrayList(staff.getId()));
            staffVO.setOrgs(staffOrgVOS);
            return staffVO;
        }
        return null;
    }


    /**
     * 导出员工信息
     * @param queryDTO
     * @param response
     */
    @Override
    public void soldStaffs(StaffQueryDTO queryDTO, HttpServletResponse response) {
        if (queryDTO.getOrgId() != null) {
//            List<TzStore> tzStoreList =  tzStoreService.listByOrgId(queryDTO.getOrgId());
//            if (CollectionUtils.isEmpty(tzStoreList)) {
//                throw new YAMLException("没有可导出的员工信息");
//            }
//            queryDTO.setStoreIdList(tzStoreList.stream().map(TzStore :: getStoreId).collect(Collectors.toList()));
        }
        if(queryDTO.getSort()==null){
            queryDTO.setSort(0);
        }
        List<Staff> staffList=staffMapper.listByStaffQueryDTO(queryDTO);
        if (CollectionUtils.isEmpty(staffList)) {
            throw new YAMLException("没有可导出的员工信息");
        }

        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(SoldStaffsVO.EXCEL_NAME);
        downloadRecordDTO.setCalCount(staffList.size());
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
        Long downLoadHisId=null;
        if(serverResponseEntity.isSuccess()){
            downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
        }

        if (!CollectionUtils.isEmpty(staffList)) {
            List<Long> storeIdList = staffList.stream().map(Staff::getStoreId).collect(Collectors.toList());
//            Map<Long, TzStore> tzStoreMap = tzStoreService.listByStoreIdList(storeIdList).stream()
//                    .collect(Collectors.toMap(TzStore :: getStoreId, Function.identity()));
            List<SoldStaffsVO> staffVOList = staffList.stream().map(staff -> {
                SoldStaffsVO soldStaffsVO = mapperFacade.map(staff, SoldStaffsVO.class);
//                TzStore tzStore = tzStoreMap.get(staff.getStoreId());
//                if (Objects.nonNull(tzStore)) {
//                    soldStaffsVO.setStoreName(tzStore.getStationName());
//                    soldStaffsVO.setStoreCode(tzStore.getStoreCode());
//                }
                //角色类型 1-导购 2-店长 3-店务
                if(staff.getRoleType()!=null){
                    if(staff.getRoleType()==1){
                        soldStaffsVO.setRoleType("导购");
                    }else if(staff.getRoleType()==2){
                        soldStaffsVO.setRoleType("店长");
                    }else if(staff.getRoleType()==3){
                        soldStaffsVO.setRoleType("店务");
                    }
                }
                //状态 0 正常 1注销
                if(staff.getStatus()!=null){
                    if(staff.getStatus()==0){
                        soldStaffsVO.setStatus("正常");
                    }else if(staff.getStatus()==1){
                        soldStaffsVO.setStatus("离职");
                    }
                }
                return soldStaffsVO;
            }).collect(Collectors.toList());

            //导出
//            ExcelUtil.soleExcel(response, staffVOList,
//                    SoldStaffsVO.EXCEL_NAME,
//                    SoldStaffsVO.MERGE_ROW_INDEX,
//                    SoldStaffsVO.MERGE_COLUMN_INDEX,
//                    SoldStaffsVO.class);

            ExcelUploadDTO excelUploadDTO=new ExcelUploadDTO(downLoadHisId,
                    staffVOList,
                    SoldStaffsVO.EXCEL_NAME,
                    SoldStaffsVO.MERGE_ROW_INDEX,
                    SoldStaffsVO.MERGE_COLUMN_INDEX,
                    SoldStaffsVO.class);
//            soldUploadExcelTemplate.syncSend(excelUploadDTO);

        }
    }

    @Override
    public String importExcelStaffs(MultipartFile file) {


        return null;
    }

    @Override
    public List<StaffVO> listStaffByStatus(StaffQueryDTO staffQueryDTO) {
        return staffMapper.listStaffByStatus(staffQueryDTO);
    }

    /**
     * 获取员工部门信息
     * @param staffIds
     * @return
     */
    @Override
    public List<StaffOrgVO> getStaffAndOrgs(List<Long> staffIds) {
        if(CollUtil.isEmpty(staffIds)){
            return ListUtil.empty();
        }
        List<StaffOrgVO> staffOrgVOS=staffOrgRelService.getStaffAndOrgs(staffIds);
        return CollUtil.isNotEmpty(staffOrgVOS)?staffOrgVOS:ListUtil.empty();
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getByStaffNOOrNickName(String staff) {

        List<Staff> staffList = lambdaQuery()
                .eq(Staff::getStaffNo, staff)
                .or()
                .like(Staff::getStaffName, staff)
                .list();

        List<StaffVO> staffVOList = mapperFacade.mapAsList(staffList, StaffVO.class);

        return ServerResponseEntity.success(staffVOList);
    }

    @Override
    public ServerResponseEntity<List<StaffVO>> getStoreManagerByStoreIdList(List<Long> serviceStoreIdList) {
        List<Staff> staffList = lambdaQuery()
                .eq(Staff::getRoleType, StaffRoleTypeEnum.MANAGER.getValue())
                .eq(Staff::getStatus, 0)
                .in(Staff::getStoreId, serviceStoreIdList)
                .groupBy(Staff::getStoreId)
                .list();

        log.info("GET THE STORE MANAGER DATA IS :{}",JSON.toJSONString(staffList));

        List<StaffVO> staffVOList = mapperFacade.mapAsList(staffList, StaffVO.class);

        return ServerResponseEntity.success(staffVOList);
    }
}
