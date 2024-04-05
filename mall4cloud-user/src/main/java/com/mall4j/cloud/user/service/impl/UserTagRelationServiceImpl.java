package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.bo.ExcelMarkingUserPageBO;
import com.mall4j.cloud.user.bo.UserGroupTagRelationBO;
import com.mall4j.cloud.user.bo.UserTagOperationBO;
import com.mall4j.cloud.user.bo.UserTagRelationBO;
import com.mall4j.cloud.user.constant.ImportTagUserStatusEnum;
import com.mall4j.cloud.user.constant.TagOperationEnum;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.manager.*;
import com.mall4j.cloud.user.model.UserTagRelation;
import com.mall4j.cloud.user.mapper.UserTagRelationMapper;
import com.mall4j.cloud.user.service.UserTagRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.user.service.async.AsyncUserTagRelationService;
import com.mall4j.cloud.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户与标签关联表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-08
 */

@Slf4j
@Service
public class UserTagRelationServiceImpl extends ServiceImpl<UserTagRelationMapper, UserTagRelation> implements UserTagRelationService {

    @Autowired
    private UserTagOperationManager userTagOperationManager;

    @Autowired
    private UserTagRelationManager userTagRelationManager;

    @Autowired
    private UserTagRelationMapper userTagRelationMapper;

    @Autowired
    private TagGroupManager tagGroupManager;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private AsyncUserTagRelationService asyncUserTagRelationService;

    private static final Long PAGE_NUM = 1L;

    private static final Long PAGE_SIZE = 500L;

    private static final Integer REMOVE_SIZE = 500;



    /**
     * 打标
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity marking(MarkingTagDTO markingTagDTO) {

        //获取操作用户信息
        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        //查询用户相关标签列表
        List<UserTagListVO> vipTagList = userTagRelationManager.getTagByVipCode(markingTagDTO.getVipCode());

        //聚合当前参数中的标签，获取用户不需要删除的标签
        List<Long> hasGroupIdList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> vipTagList.contains(dto.getGroupTagRelationId())
                        && Objects.nonNull(dto.getUserTagRelationId()))
                .map(TagGroupRelationDTO::getGroupId)
                .collect(Collectors.toList());

        List<Long> checkList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> Objects.nonNull(dto.getUserTagRelationId()))
                .map(TagGroupRelationDTO::getUserTagRelationId)
                .collect(Collectors.toList());

        List<UserTagListVO> removeList = vipTagList.stream()
                .filter(tag -> !checkList.contains(tag.getUserTagRelationId()))
                .collect(Collectors.toList());

        //聚合需要删除的标签关系
        List<UserGroupTagRelationBO> userGroupTagRelationBOList;
        if (CollectionUtil.isNotEmpty(removeList)){

            hasGroupIdList = hasGroupIdList.stream()
                    .filter(tag -> !removeList.contains(tag))
                    .collect(Collectors.toList());

            userGroupTagRelationBOList = removeList.stream().map(add -> {
                UserGroupTagRelationBO relationBO = new UserGroupTagRelationBO();
                relationBO.setTagId(add.getTagId());
                relationBO.setGroupId(add.getGroupId());
                return relationBO;
            }).collect(Collectors.toList());
            //新增删除操作记录
            userTagOperationManager.addRemoveBatchByTagIds(userGroupTagRelationBOList, tokenUser.getUserId(), markingTagDTO.getVipCode());

            removeByIds(removeList.stream().map(UserTagListVO::getUserTagRelationId).collect(Collectors.toList()));
        }

        //聚合当前用户新增的标签
        List<Long> addGroupIdList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> Objects.isNull(dto.getUserTagRelationId()))
                .map(TagGroupRelationDTO::getGroupId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(addGroupIdList)){
            //聚合不需要删除的标签和新增的标签的groupId
            hasGroupIdList.addAll(addGroupIdList);
            //进行去重
            hasGroupIdList.stream().distinct();
            CheckVipCodeSingleTagCountDTO checkVipCodeSingleTagCountDTO = new CheckVipCodeSingleTagCountDTO();
            checkVipCodeSingleTagCountDTO.setVipCode(markingTagDTO.getVipCode());
            checkVipCodeSingleTagCountDTO.setTagGroupIdList(hasGroupIdList);
            //查询用户本次新增的标签是否存在不可多选的标签组内的标签
            List<CheckVipCodeSingleTagCountVO> singleCountByVipCode = tagGroupManager.getSingleCountByVipCode(checkVipCodeSingleTagCountDTO);
            if (CollectionUtil.isNotEmpty(singleCountByVipCode)){
                CheckVipCodeSingleTagCountVO checkVipCodeSingleTagCountVO = singleCountByVipCode.stream().findAny().get();
                return ServerResponseEntity.showFailMsg("标签组-"+checkVipCodeSingleTagCountVO.getGroupName()+"-组内标签为单选标签");
            }
        }




        //聚合新增的标签关系数据
        List<UserTagRelationBO> addList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> Objects.isNull(dto.getUserTagRelationId()))
                .map(dto -> {
            UserTagRelationBO userTagRelation = new UserTagRelationBO();

            userTagRelation.setTagId(dto.getTagId());
            userTagRelation.setGroupId(dto.getGroupId());
            userTagRelation.setGroupTagRelationId(dto.getGroupTagRelationId());
            userTagRelation.setCreateUser(tokenUser.getUserId());
            userTagRelation.setVipCode(markingTagDTO.getVipCode());
            return userTagRelation;
        }).collect(Collectors.toList());

        if(CollectionUtil.isNotEmpty(addList)){
            userTagRelationManager.addList(addList);
        }

        //新增操作记录
        userGroupTagRelationBOList = addList.stream().map(add -> {
            UserGroupTagRelationBO relationBO = new UserGroupTagRelationBO();
            relationBO.setTagId(add.getTagId());
            relationBO.setGroupId(add.getGroupId());
            return relationBO;
        }).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(userGroupTagRelationBOList)){
            //插入标签关系新增操作记录
            userTagOperationManager.addSaveBatchByTagIds(userGroupTagRelationBOList, tokenUser.getUserId(), markingTagDTO.getVipCode());
        }


        return ServerResponseEntity.success();
    }

    /**
     * 取消打标
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity cancelMarking(Long relationId) {

        //获取操作用户信息
        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        //查询用户标签关系表
        UserTagRelation userTagRelation = getById(relationId);

        //组装操作日志
        UserTagOperationBO userTagOperationBO = new UserTagOperationBO();

        userTagOperationBO.setDoUser(tokenUser.getUserId());

        userTagOperationBO.setBeVipCode(userTagRelation.getVipCode());

        userTagOperationBO.setOperationState(TagOperationEnum.REMOVE.getOperationState());

        userTagOperationBO.setTagId(userTagRelation.getTagId());

        userTagOperationBO.setGroupId(userTagRelation.getGroupId());

        //新增操作日志
        userTagOperationManager.add(userTagOperationBO);

        //删除标签绑定记录
        removeById(relationId);

        return ServerResponseEntity.success();
    }

    /**
     * 按照导入文件进行会员导入
     * */
    @Override
    public ServerResponseEntity importAdd(ExportUserTagRelationDTO exportUserTagRelationDTO) {

        //获取当前用户
        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        TagVO tagVO = tagManager.tagById(exportUserTagRelationDTO.getTagId());
        if (Objects.nonNull(tagVO.getImportStatus()) && tagVO.getImportStatus().equals(ImportTagUserStatusEnum.ING.getImportStatus())){
            return ServerResponseEntity.showFailMsg("标签导入任务正在进行中，请稍后再试");
        }

        //导入文件读取
        List<UserTagRelationBO> userTagRelationBOList = userTagRelationManager.exportVipCodes(exportUserTagRelationDTO.getFile());

        if (CollectionUtil.isEmpty(userTagRelationBOList)){
            return ServerResponseEntity.showFailMsg("至少需要导入一个会员!");
        }
        tagManager.updateImportStatus(ImportTagUserStatusEnum.ING,exportUserTagRelationDTO.getTagId());

        asyncUserTagRelationService.asyncImport(exportUserTagRelationDTO, userTagRelationBOList, tokenUser.getUserId());

        return ServerResponseEntity.success();
    }

    /**
     * 按照导入文件进行相应会员删除
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity importRemove(ExportUserTagRelationDTO exportUserTagRelationDTO) {
        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        //导入文件读取
        List<UserTagRelationBO> userTagRelationBOList = userTagRelationManager.exportVipCodes(exportUserTagRelationDTO.getFile());

        List<String> vipCodeList = userTagRelationBOList.stream()
                .map(UserTagRelationBO::getVipCode)
                .collect(Collectors.toList());

        List<List<String>> vipCodeListCollect = Lists.partition(vipCodeList, REMOVE_SIZE);
        vipCodeListCollect.stream().forEach(codeList -> {

            //根据关系批量删除用户标签绑定记录
            userTagRelationManager.removeBatchByTagIdAndVipCodeList(exportUserTagRelationDTO.getTagId(), codeList);

            //批量新增操作日志
            userTagOperationManager
                    .addRemoveBatch(exportUserTagRelationDTO.getTagId(), tokenUser.getUserId(),
                            exportUserTagRelationDTO.getGroupId(), codeList);

        });


        return ServerResponseEntity.success();
    }


    /**
     * 删除tag下的所有会员
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity removeByTag(Long tagId) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

//        UserInfoInTokenBO tokenUser = new UserInfoInTokenBO();
//        tokenUser.setUserId(1L);

        boolean hasPagesFlag = true;

        //持续查询，直到最后一页查询结束
        while (hasPagesFlag) {

            Page<UserTagRelation> page = this.getPage(null, tagId, PAGE_NUM, PAGE_SIZE);

            if (page.getPages()==1){
                hasPagesFlag = false;
            }


            List<List<UserTagRelation>> partition = Lists.partition(page.getRecords(), REMOVE_SIZE);
            partition.stream().forEach(relationList -> {

                List<Long> relationIdList = relationList.stream()
                        .map(UserTagRelation::getUserTagRelationId)
                        .collect(Collectors.toList());

                List<String> codeList = relationList.stream()
                        .map(UserTagRelation::getVipCode)
                        .collect(Collectors.toList());

                //根据关系批量删除用户标签绑定记录
                removeByIds(relationIdList);

                //批量新增操作日志
                userTagOperationManager
                        .addRemoveBatch(tagId, tokenUser.getUserId(),
                                page.getRecords().get(0).getGroupId(), codeList);
            });

        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageVO<MarkingUserPageVO>> getMarkingUserByTagPage(QueryMarkingUserPageDTO pageDTO) {

        IPage<MarkingUserPageVO> userByTagPage = userTagRelationManager.getMarkingUserByTagPage(pageDTO);

        this.wrapperStoreAndStaff(userByTagPage.getRecords());

        PageVO<MarkingUserPageVO> pageVO = new PageVO<>();
        pageVO.setPages((int)userByTagPage.getPages());
        pageVO.setTotal(userByTagPage.getTotal());
        pageVO.setList(userByTagPage.getRecords());

        return ServerResponseEntity.success(pageVO);
    }


    private Page<UserTagRelation> getPage(Long groupId, Long tagId, Long pageNum, Long pageSize){
        Page<UserTagRelation> page = new Page<>(pageNum, pageSize);

        LambdaQueryChainWrapper<UserTagRelation> lambdaQuery = lambdaQuery();

        if (Objects.nonNull(groupId)){
            lambdaQuery
                    .eq(UserTagRelation::getGroupId, groupId);
        }

        page = lambdaQuery
                .eq(UserTagRelation::getTagId, tagId)
                .page(page);

        return page;
    }

    /**
     * 导购获取可对用户进行打标的标签信息【标签所在组状态包含导购可用】
     * @param staffGetVisibleUserTagDTO
     * @return
     */
    @Override
    public List<StaffGetVisibleUserTagVO> staffGetVisibleUserTag(StaffGetVisibleUserTagDTO staffGetVisibleUserTagDTO) {
        List<StaffGetVisibleUserTagVO> staffGetVisibleUserTagVOS = userTagRelationMapper.staffGetVisibleUserTag(staffGetVisibleUserTagDTO);
        staffGetVisibleUserTagVOS.forEach(tagVO -> {
            if(StringUtils.isNotEmpty(tagVO.getAuthFlag())){
                if(tagVO.getAuthFlag().contains("1")){
                    tagVO.setIsShowDel(1);
                }else {
                    tagVO.setIsShowDel(0);
                }
            }
        });
        return staffGetVisibleUserTagVOS;
    }

    /**
     * 导购对单个会员进行打标,如果该标签已经打过标那就进行取消打标
     * @param markingTagDTO
     * @return
     */
    @Transactional
    @Override
    public ServerResponseEntity staffSaveUserTag(MarkingTagDTO markingTagDTO) {
        log.info("进入导购对单个会员进行打标，当前方法入参为：{}", markingTagDTO);
        // 获取操作用户信息
        Long userId = AuthUserContext.get().getUserId();
        String vipCode = markingTagDTO.getVipCode();

        if(CollectionUtil.isEmpty(markingTagDTO.getGroupRelationDTOList())){
            log.info("导购取消用户所有标签,取消标签的用户为:{},进行操作的导购为:{}", vipCode, userId);
            userTagRelationManager.removeListByVipCode(vipCode);
            // 导购没有选中任何标签信息，默认删除该用户的所有标签数据
            return ServerResponseEntity.success();
        }

        //查询用户相关标签列表
        List<UserTagListVO> vipTagList = userTagRelationManager.getTagByVipCode(vipCode);
        log.info("查询用户原本拥有标签,当前用户为:{},标签信息为:{}", vipCode, vipTagList);

        //聚合当前参数中的标签，获取用户不需要删除的标签
        List<Long> hasGroupIdList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> vipTagList.contains(dto.getGroupTagRelationId())
                        && Objects.nonNull(dto.getUserTagRelationId()))
                .map(TagGroupRelationDTO::getGroupId)
                .collect(Collectors.toList());
        // 过滤出需要校验的标签信息
        List<Long> checkList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> Objects.nonNull(dto.getUserTagRelationId()))
                .map(TagGroupRelationDTO::getUserTagRelationId)
                .collect(Collectors.toList());
        // 过滤出需要删除的用户信息
        List<UserTagListVO> removeList = vipTagList.stream()
                .filter(tag -> !checkList.contains(tag.getUserTagRelationId()))
                .collect(Collectors.toList());

        //聚合需要删除的标签关系
        List<UserGroupTagRelationBO> userGroupTagRelationBOList;
        if (CollectionUtil.isNotEmpty(removeList)){
            hasGroupIdList = hasGroupIdList.stream()
                    .filter(tag -> !removeList.contains(tag))
                    .collect(Collectors.toList());
            userGroupTagRelationBOList = removeList.stream().map(add -> {
                UserGroupTagRelationBO relationBO = new UserGroupTagRelationBO();
                relationBO.setTagId(add.getTagId());
                relationBO.setGroupId(add.getGroupId());
                return relationBO;
            }).collect(Collectors.toList());
            //新增删除操作记录
            userTagOperationManager.addRemoveBatchByTagIds(userGroupTagRelationBOList, userId, vipCode);
            removeByIds(removeList.stream().map(UserTagListVO::getUserTagRelationId).collect(Collectors.toList()));
        }

        //聚合当前用户新增的标签
        List<Long> addGroupIdList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> Objects.isNull(dto.getUserTagRelationId()))
                .map(TagGroupRelationDTO::getGroupId)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(addGroupIdList)) {
            //聚合不需要删除的标签和新增的标签的groupId
            hasGroupIdList.addAll(addGroupIdList);
            //进行去重
            hasGroupIdList.stream().distinct();
            CheckVipCodeSingleTagCountDTO checkVipCodeSingleTagCountDTO = new CheckVipCodeSingleTagCountDTO();
            checkVipCodeSingleTagCountDTO.setVipCode(vipCode);
            checkVipCodeSingleTagCountDTO.setTagGroupIdList(hasGroupIdList);
            //查询用户本次新增的标签是否存在不可多选的标签组内的标签
            List<CheckVipCodeSingleTagCountVO> singleCountByVipCode = tagGroupManager.getSingleCountByVipCode(checkVipCodeSingleTagCountDTO);
            if (CollectionUtil.isNotEmpty(singleCountByVipCode)) {
                CheckVipCodeSingleTagCountVO checkVipCodeSingleTagCountVO = singleCountByVipCode.stream().findAny().get();
                return ServerResponseEntity.showFailMsg("标签组-" + checkVipCodeSingleTagCountVO.getGroupName() + "-组内标签为单选标签");
            }
        }

        //聚合新增的标签关系数据
        List<UserTagRelationBO> addList = markingTagDTO.getGroupRelationDTOList().stream()
                .filter(dto -> Objects.isNull(dto.getUserTagRelationId()))
                .map(dto -> {
                    UserTagRelationBO userTagRelation = new UserTagRelationBO();
                    userTagRelation.setTagId(dto.getTagId());
                    userTagRelation.setGroupId(dto.getGroupId());
                    userTagRelation.setGroupTagRelationId(dto.getGroupTagRelationId());
                    userTagRelation.setCreateUser(userId);
                    userTagRelation.setVipCode(vipCode);
                    return userTagRelation;
                }).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(addList)){
            userTagRelationManager.addList(addList);
        }

        //新增操作记录
        userGroupTagRelationBOList = addList.stream().map(add -> {
            UserGroupTagRelationBO relationBO = new UserGroupTagRelationBO();
            relationBO.setTagId(add.getTagId());
            relationBO.setGroupId(add.getGroupId());
            return relationBO;
        }).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(userGroupTagRelationBOList)){
            //插入标签关系新增操作记录
            userTagOperationManager.addSaveBatchByTagIds(userGroupTagRelationBOList, userId, vipCode);
        }

        return ServerResponseEntity.success();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity removeBySelectList(RemoveUserTagRelationBatchDTO removeBatchDTO) {

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        List<UserTagRelation> userTagRelationList = lambdaQuery()
                .in(UserTagRelation::getUserTagRelationId, removeBatchDTO.getUserTagRelationIdList())
                .list();

        List<String> vipCodeList = userTagRelationList.stream()
                .map(UserTagRelation::getVipCode)
                .collect(Collectors.toList());

        userTagOperationManager.addRemoveBatch(removeBatchDTO.getTagId(), userInfoInTokenBO.getUserId(), removeBatchDTO.getGroupId(), vipCodeList);

        removeByIds(removeBatchDTO.getUserTagRelationIdList());

        return ServerResponseEntity.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity removeByUserTagRelationId(Long userTagRelationId) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();
//        UserInfoInTokenBO tokenUser = new UserInfoInTokenBO();
//        tokenUser.setUserId(1L);

        UserTagRelation userTagRelation = getById(userTagRelationId);

        List<String> vipCodes = Arrays.asList(userTagRelation.getVipCode());

        userTagOperationManager.addRemoveBatch(userTagRelation.getTagId(), tokenUser.getUserId(),
                userTagRelation.getGroupId(), vipCodes);

        removeById(userTagRelationId);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity exportMarkingUser(Long tagId, HttpServletResponse response) {
        FinishDownLoadDTO finishDownLoadDTO;

        //拼接文件名称
        TagVO tagVO = tagManager.tagById(tagId);
        StringBuffer fileName = new StringBuffer();
        fileName.append("标签-");
        fileName.append(tagVO.getTagName());
        fileName.append("(" + tagVO.getTagId() + ")");
        fileName.append("-" + ExcelMarkingUserPageBO.EXCEL_NAME);
        //获取下载中心文件对应ID
        finishDownLoadDTO = userTagRelationManager.wrapperDownLoadCenterParam(fileName.toString());

        if (Objects.isNull(finishDownLoadDTO)){
            return ServerResponseEntity.showFailMsg("文件下载失败，请重试");
        }

        asyncUserTagRelationService.asyncExport(tagId,finishDownLoadDTO);
        return ServerResponseEntity.success("操作成功，请转至下载中心下载");
    }

    @Override
    public ServerResponseEntity<List<UserTagListVO>> getTheTagByVipCode(String vipCode) {
        List<UserTagListVO> vipTagList = userTagRelationManager.getTagByVipCode(vipCode);
        return ServerResponseEntity.success(vipTagList);
    }

    @Override
    public boolean isInTag(Long tagId, String vipcode) {
        List<UserTagRelation> relations = this.list(new LambdaQueryWrapper<UserTagRelation>()
                .eq(UserTagRelation::getTagId, tagId).eq(UserTagRelation::getVipCode, vipcode));
        return CollectionUtil.isNotEmpty(relations);
    }

    @Override
    public boolean isInTags(List<Long> tagIds, String vipcode) {
        List<UserTagRelation> relations = this.list(new LambdaQueryWrapper<UserTagRelation>()
                .in(UserTagRelation::getTagId, tagIds).eq(UserTagRelation::getVipCode, vipcode));
        return CollectionUtil.isNotEmpty(relations);
    }

    @Override
    public Integer countByTagIds(List<Long> tagIds) {
        return userTagRelationMapper.selectCountByTagId(tagIds);
    }

    @Override
    public List<Long> listUserIdByTagIds(List<Long> tagIds) {
        return userTagRelationMapper.selectListUserIdByTagIds(tagIds);
    }

    public void wrapperStoreAndStaff(List<MarkingUserPageVO> pageData){
        if (CollectionUtil.isNotEmpty(pageData)){

            TagVO tagVO = tagManager.tagById(pageData.get(0).getTagId());

            TagGroupVO tagGroupVO = tagGroupManager.getTagGroupById(pageData.get(0).getGroupId());

            TagGroupVO parent = tagGroupManager.getTagGroupById(tagGroupVO.getParentId());

            List<Long> serviceStoreIdList = pageData.stream()
                    .map(MarkingUserPageVO::getServiceStoreId)
                    .distinct()
                    .collect(Collectors.toList());

            Map<Long, StaffVO> staffVOMap = new HashMap<>();

            Map<Long, StoreVO> storeVOMap = new HashMap<>();

            //查询并组装服务门店名称
            if (CollectionUtil.isNotEmpty(serviceStoreIdList)){
                List<StoreVO> serviceStore = userTagRelationManager.getServiceStoreList(serviceStoreIdList);
                if (CollectionUtil.isNotEmpty(serviceStore)){
                    storeVOMap = serviceStore.stream().collect(Collectors.toMap(StoreVO::getStoreId, store -> store));
                }
            }


            //查询并组装导购名称
            List<Long> staffIdList = pageData.stream()
                    .map(MarkingUserPageVO::getStaffId)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(staffIdList)) {
                List<StaffVO> staffList = userTagRelationManager.getStaffList(staffIdList);
                if (CollectionUtil.isNotEmpty(staffList)) {
                    staffVOMap = staffList.stream().collect(Collectors.toMap(StaffVO::getId, staff -> staff));
                }

            }

            Map<Long, StoreVO> finalStoreVOMap = storeVOMap;
            Map<Long, StaffVO> finalStaffVOMap = staffVOMap;
            pageData.stream().forEach(detail -> {

                detail.setGroupName(tagGroupVO.getGroupName());
                detail.setTagName(tagVO.getTagName());
                detail.setParentGroupName(parent.getGroupName());

                StoreVO storeVO;
                if (Objects.nonNull(storeVO = finalStoreVOMap.get(detail.getServiceStoreId()))){
                    detail.setServiceStoreName(storeVO.getName());
                    detail.setServiceStoreCode(storeVO.getStoreCode());
                }

                StaffVO staffVO;
                if (Objects.nonNull(staffVO = finalStaffVOMap.get(detail.getStaffId()))) {
                    detail.setStaffNickName(staffVO.getStaffName());
                    detail.setStaffNo(staffVO.getStaffNo());
                }
            });
        }
    }

    @Override
    public ServerResponseEntity<List<Long>> checkUserTagRelationByTagIdList(List<Long> tagIdList) {

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();


        String code = userManager.getCodeById(userInfoInTokenBO.getUserId());

        List<UserTagRelation> list = lambdaQuery()
                .eq(UserTagRelation::getVipCode, code)
                .in(UserTagRelation::getTagId, tagIdList)
                .list();

        List<Long> targetTagIdList = list.stream()
                .map(UserTagRelation::getTagId)
                .collect(Collectors.toList());

        return ServerResponseEntity.success(targetTagIdList);
    }

}
