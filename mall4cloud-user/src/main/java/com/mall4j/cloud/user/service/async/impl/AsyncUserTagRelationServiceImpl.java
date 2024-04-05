package com.mall4j.cloud.user.service.async.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.user.bo.ExcelMarkingUserPageBO;
import com.mall4j.cloud.user.bo.ImportTagUserFailBO;
import com.mall4j.cloud.user.bo.UserTagRelationBO;
import com.mall4j.cloud.user.constant.ImportTagUserStatusEnum;
import com.mall4j.cloud.user.constant.TagGroupSingleStateEnum;
import com.mall4j.cloud.user.dto.ExportUserTagRelationDTO;
import com.mall4j.cloud.user.dto.QueryMarkingUserPageDTO;
import com.mall4j.cloud.user.manager.*;
import com.mall4j.cloud.user.mapper.UserTagRelationMapper;
import com.mall4j.cloud.user.model.UserTagRelation;
import com.mall4j.cloud.user.service.UserTagRelationService;
import com.mall4j.cloud.user.service.async.AsyncUserTagRelationService;
import com.mall4j.cloud.user.service.impl.UserTagRelationServiceImpl;
import com.mall4j.cloud.user.vo.MarkingUserPageVO;
import com.mall4j.cloud.user.vo.TagGroupVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsyncUserTagRelationServiceImpl implements AsyncUserTagRelationService {

    @Autowired
    private UserTagOperationManager userTagOperationManager;

    @Autowired
    private TagGroupManager tagGroupManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserTagRelationManager userTagRelationManager;


    @Autowired
    private TagManager tagManager;

    @Autowired
    private UserTagRelationMapper userTagRelationMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private UserTagRelationService userTagRelationService;



    private static final Long PAGE_SIZE = 500L;


    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void asyncImport(ExportUserTagRelationDTO exportUserTagRelationDTO, List<UserTagRelationBO> userTagRelationBOList, Long createUserId) {
        this.startImport(exportUserTagRelationDTO, userTagRelationBOList, createUserId);
    }

    @Async
    @Override
    public void asyncExport(Long tagId, FinishDownLoadDTO finishDownLoadDTO) {
        try {

            Long pages = 1L;
            Integer pageNum = 0;
            Integer pageSize = 1000;

            List<ExcelMarkingUserPageBO> excelMarkingUserPageBOList = new ArrayList<>();

            while (pageNum.longValue() < pages){

                pageNum += 1;

                QueryMarkingUserPageDTO pageDTO = new QueryMarkingUserPageDTO();

                pageDTO.setPageNum(pageNum);
                pageDTO.setPageSize(pageSize);
                pageDTO.setTagId(tagId);

                IPage<MarkingUserPageVO> userByTagPage = userTagRelationManager.getMarkingUserByTagPage(pageDTO);

                userTagRelationService.wrapperStoreAndStaff(userByTagPage.getRecords());

                pages = userByTagPage.getPages();

                List<ExcelMarkingUserPageBO> exportUserToCreateEventBOS = mapperFacade.mapAsList(userByTagPage.getRecords(), ExcelMarkingUserPageBO.class);

                excelMarkingUserPageBOList.addAll(exportUserToCreateEventBOS);
            }

            String excelPath = userTagRelationManager
                    .createExcelFile(excelMarkingUserPageBOList, finishDownLoadDTO.getFileName());

            //填充数据到下载中心
            File file = new File(excelPath);
            if(file.isFile()){
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                FileInputStream fileInputStream = new FileInputStream(excelPath);
                String contentType = "application/vnd.ms-excel";
                MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                        contentType, fileInputStream);
                String originalFilename = multipartFile.getOriginalFilename();
                String mimoPath = "excel/" + time + "/" + originalFilename;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                if(responseEntity.isSuccess()){
                    log.info("---ExcelUploadService---" + responseEntity.toString());
                    //下载中心记录
                    finishDownLoadDTO.setCalCount(excelMarkingUserPageBOList.size());
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(excelPath);
            }


        }catch (Exception e){
            log.error("标签会员名单导出异常，message is:{}",e);

            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("标签会员名单导出，excel生成zip失败");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }
    }



    public void startImport(ExportUserTagRelationDTO exportUserTagRelationDTO, List<UserTagRelationBO> userTagRelationBOList, Long createUserId){
        try {

            String dateTimeNow = DateUtil.dateTimeNow("yyyy-MM-dd HH:mm:ss");
            List<ImportTagUserFailBO> failBOList = new ArrayList<>();

            TagVO tagVO = tagManager.tagById(exportUserTagRelationDTO.getTagId());

            TagGroupVO group = tagGroupManager.getTagGroupById(exportUserTagRelationDTO.getGroupId());
            boolean single = group.getSingleState().equals(TagGroupSingleStateEnum.YES.getSingleState());

            Map<Long, TagVO> theTagMap = getTheTagMap(exportUserTagRelationDTO.getGroupId());

            List<List<UserTagRelationBO>> partitionUserList = Lists.partition(userTagRelationBOList, PAGE_SIZE.intValue());
            partitionUserList.stream()
                    .forEach(vipList -> {
                        List<String> vipCodeList = vipList.stream()
                                .map(UserTagRelationBO::getVipCode)
                                .collect(Collectors.toList());

                        //1过滤用户
                        //1.1、根据VIPCODE列表查询用户数据
                        List<String> filterList = userManager.filterVipCodeList(vipCodeList);
                        //1.2、聚合不存在的用户Code
                        vipCodeList = vipCodeList.stream()
                                .filter(code -> !filterList.contains(code))
                                .collect(Collectors.toList());

                        //1.3、查询已绑定的用户
                        List<UserTagRelation> relationList = userTagRelationMapper.selectList(
                                        new LambdaQueryWrapper<UserTagRelation>()
                                                .eq(UserTagRelation::getTagId, exportUserTagRelationDTO.getTagId())
                                                .in(UserTagRelation::getVipCode, filterList)
                        );
                        List<String> tagVipCodeList = relationList.stream()
                                .map(UserTagRelation::getVipCode)
                                .collect(Collectors.toList());
                        //1.4、把存在的且还未绑定的用户聚合起来
                        List<UserTagRelationBO> filterVip = vipList.stream()
                                .filter(vip -> filterList.contains(vip.getVipCode()))
                                .filter(vip -> !tagVipCodeList.contains(vip.getVipCode()))
                                .collect(Collectors.toList());

                        Map<String, UserTagRelation> filterMap = new HashMap<>();

                        //判断是否单选组，同事过滤已绑定过组内其他标签的会员
                        if (single){
                            List<UserTagRelation> bindingVipList = userTagRelationMapper.selectList(
                                    new LambdaQueryWrapper<UserTagRelation>()
                                            .eq(UserTagRelation::getGroupId, exportUserTagRelationDTO.getGroupId())
                                            .ne(UserTagRelation::getTagId, exportUserTagRelationDTO.getTagId())
                                            .in(UserTagRelation::getVipCode, filterList)
                            );


                            if (CollectionUtil.isNotEmpty(bindingVipList)){
                                List<ImportTagUserFailBO> currentFailBOList = bindingVipList.stream().map(binding -> {
                                    ImportTagUserFailBO importTagUserFailBO = new ImportTagUserFailBO();
                                    importTagUserFailBO.setVipCode(binding.getVipCode());
                                    String tagName = theTagMap.get(binding.getTagId()).getTagName();
                                    importTagUserFailBO.setMessage("用户已被标记同组单选标签:{" + tagName + "}");
                                    return importTagUserFailBO;
                                }).collect(Collectors.toList());
                                failBOList.addAll(currentFailBOList);
                            }


                            List<String> bindingCodeList = bindingVipList.stream()
                                    .map(UserTagRelation::getVipCode)
                                    .collect(Collectors.toList());

                            filterVip = filterVip.stream()
                                    .filter(vip -> !bindingCodeList.contains(vip.getVipCode()))
                                    .collect(Collectors.toList());

                        }

                        //去筛选后得到需要新绑定标签关系的会员
                        if (CollectionUtil.isNotEmpty(filterVip)){
                            //新增标签会员绑定关系
                            List<String> filterVipCodeList = filterVip.stream()
                                    .map(UserTagRelationBO::getVipCode)
                                    .distinct()
                                    .collect(Collectors.toList());

                            userTagRelationManager.addListForImport(filterVipCodeList, exportUserTagRelationDTO.getTagId(),
                                    exportUserTagRelationDTO.getGroupId(),
                                    exportUserTagRelationDTO.getGroupTagRelationId(), createUserId);

                            //批量新增操作日志
                            userTagOperationManager
                                    .addSaveBatch(exportUserTagRelationDTO.getTagId(), createUserId,
                                            exportUserTagRelationDTO.getGroupId(), filterVipCodeList);
                        }

                        if (CollectionUtil.isNotEmpty(vipCodeList)){
                            List<ImportTagUserFailBO> nothingness = vipCodeList.stream().map(code -> {
                                ImportTagUserFailBO importTagUserFailBO = new ImportTagUserFailBO();
                                importTagUserFailBO.setVipCode(code);
                                importTagUserFailBO.setMessage("会员卡号未绑定本系统用户");
                                return importTagUserFailBO;
                            }).collect(Collectors.toList());
                            failBOList.addAll(nothingness);
                        }
                    });

            if (CollectionUtil.isNotEmpty(failBOList)){
                exportFailBO(tagVO.getTagName(), exportUserTagRelationDTO.getTagId(), dateTimeNow, failBOList);
                tagManager.updateImportStatus(ImportTagUserStatusEnum.WARN, exportUserTagRelationDTO.getTagId());
            }else {
                tagManager.updateImportStatus(ImportTagUserStatusEnum.FINISH, exportUserTagRelationDTO.getTagId());
            }

//            log.info("END IMPORT TAG USER");
        }catch (Exception e){
            log.error("IMPORT TAG USER IS FAIL,MESSAGE IS:{}",e);
            new Thread(()->
                tagManager.updateImportStatus(ImportTagUserStatusEnum.ERROR, exportUserTagRelationDTO.getTagId())
            ).start();
            throw new LuckException("导入失败");
        }
    }

    public Map<Long, TagVO> getTheTagMap(Long groupId){
        List<TagVO> tagList = tagManager.getTagListByGroup(groupId);
        Map<Long, TagVO> tagVOMap = tagList.stream().collect(Collectors.toMap(TagVO::getTagId, tag -> tag));
        return tagVOMap;
    }


    public void exportFailBO(String tagName, Long tagId, String time, List<ImportTagUserFailBO> failBOList){
        FinishDownLoadDTO finishDownLoadDTO;
        StringBuffer fileName = new StringBuffer();
        fileName.append("标签-");
        fileName.append(tagName);
        fileName.append("(" + tagId + ")");
        fileName.append("-" + ImportTagUserFailBO.EXCEL_NAME);
        //获取下载中心文件对应ID
        finishDownLoadDTO = userTagRelationManager.wrapperDownLoadCenterParam(fileName.toString());

        if (Objects.isNull(finishDownLoadDTO)){
            return;
        }

        try{
            String excelPath = userTagRelationManager
                    .createFailBOExcelFile(failBOList, finishDownLoadDTO.getFileName());

            //填充数据到下载中心
            File file = new File(excelPath);
            if(file.isFile()){
                String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                FileInputStream fileInputStream = new FileInputStream(excelPath);
                String contentType = "application/vnd.ms-excel";
                MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                        contentType, fileInputStream);
                String originalFilename = multipartFile.getOriginalFilename();
                String mimoPath = "excel/" + time + "/" + originalFilename;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                if(responseEntity.isSuccess()){
                    log.info("---ExcelUploadService---" + responseEntity.toString());
                    //下载中心记录
                    finishDownLoadDTO.setCalCount(failBOList.size());
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(excelPath);
            }


        }catch (Exception e){
            log.error("标签会员名单导出异常，message is:{}",e);

            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("标签会员名单导出，excel生成zip失败");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }
    }

}
