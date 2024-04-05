package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.bo.ExportUserToCreateEventBO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventMobileRelationPageDTO;
import com.mall4j.cloud.coupon.dto.DeleteChooseMemberEventMobileRelationDTO;
import com.mall4j.cloud.coupon.manager.ChooseMemberEventManager;
import com.mall4j.cloud.coupon.manager.ChooseMemberEventMobileRelationManager;
import com.mall4j.cloud.coupon.model.ChooseMemberEvent;
import com.mall4j.cloud.coupon.model.ChooseMemberEventMobileRelation;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventMobileRelationMapper;
import com.mall4j.cloud.coupon.service.ChooseMemberEventMobileRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordExcelVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventMobileRelationExcelVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventMobileRelationVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 指定会员活动关系表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Slf4j
@Service
public class ChooseMemberEventMobileRelationServiceImpl extends ServiceImpl<ChooseMemberEventMobileRelationMapper, ChooseMemberEventMobileRelation> implements ChooseMemberEventMobileRelationService {

    @Autowired
    private ChooseMemberEventMobileRelationManager chooseMemberEventMobileRelationManager;

    @Autowired
    private ChooseMemberEventManager chooseMemberEventManager;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Override
    public ServerResponseEntity<PageVO<ChooseMemberEventMobileRelationVO>> getTheMobilePage(ChooseMemberEventMobileRelationPageDTO pageDTO) {
        PageVO<ChooseMemberEventMobileRelationVO> pageVO = chooseMemberEventMobileRelationManager.getMobileRelationList(pageDTO);
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity removeRelationByIdList(DeleteChooseMemberEventMobileRelationDTO deleteMobileRelationDTO) {
        chooseMemberEventMobileRelationManager.removeRelationByIdListAndEventId(deleteMobileRelationDTO.getRelationIdList(), deleteMobileRelationDTO.getEventId());

        return ServerResponseEntity.success();
    }

    public ServerResponseEntity<String> export(ChooseMemberEventMobileRelationPageDTO param, HttpServletResponse response) {

        FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
        try {
            //拼接文件名称
            ChooseMemberEventVO chooseMemberEventVO = chooseMemberEventManager.getEventById(param.getEventId());
            StringBuffer fileName = new StringBuffer();
            fileName.append("会员礼品活动-");
            fileName.append(chooseMemberEventVO.getEventTitle());
            fileName.append("(" + param.getEventId() + ")");
            fileName.append("-" + ChooseMemberEventMobileRelationExcelVO.EXCEL_NAME);
            //获取下载中心文件对应ID
            finishDownLoadDTO = chooseMemberEventMobileRelationManager.wrapperDownLoadCenterParam(fileName.toString());

            if (Objects.isNull(finishDownLoadDTO)){
                return ServerResponseEntity.showFailMsg("文件下载失败，请重试");
            }

            List<ChooseMemberEventMobileRelation> memberEventMobileRelations = lambdaQuery()
                    .eq(ChooseMemberEventMobileRelation::getChooseMemberEventId, param.getEventId())
                    .list();

            //校验是否不存在可导出数据
            if (chooseMemberEventMobileRelationManager.checkDownloadData(memberEventMobileRelations,finishDownLoadDTO)){
                return ServerResponseEntity.success("操作成功，请转至下载中心下载");
            }

            List<ExportUserToCreateEventBO> exportUserToCreateEventBOS = mapperFacade.mapAsList(memberEventMobileRelations, ExportUserToCreateEventBO.class);

            String excelPath = chooseMemberEventMobileRelationManager
                    .createExcelFile(exportUserToCreateEventBOS, fileName.toString());

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
                    finishDownLoadDTO.setCalCount(memberEventMobileRelations.size());
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(excelPath);
            }

            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("会员礼物活动会员名单导出异常，message is:{}",e);

            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("兑换数据导出，excel生成zip失败");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
            return ServerResponseEntity.showFailMsg("操作失败");
        }


    }
}
