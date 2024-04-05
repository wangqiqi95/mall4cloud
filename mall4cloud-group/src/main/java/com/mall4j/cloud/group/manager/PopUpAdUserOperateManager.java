package com.mall4j.cloud.group.manager;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.auth.feign.AuthSocialFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.coupon.dto.BatchReceiveCouponDTO;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.group.constant.PopUpAdUserOperateEnum;
import com.mall4j.cloud.group.mapper.PopUpAdUserOperateMapper;
import com.mall4j.cloud.group.model.PopUpAdUserOperate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PopUpAdUserOperateManager {

    @Autowired
    public PopUpAdUserOperateMapper popUpAdUserOperateMapper;

    @Autowired
    private AuthSocialFeignClient authSocialFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private TCouponFeignClient tCouponFeignClient;


    public Integer getCountByUserAndAd(Long userId, Long adId, PopUpAdUserOperateEnum operateEnum){
        Integer count = popUpAdUserOperateMapper.selectCount(
                new LambdaQueryWrapper<PopUpAdUserOperate>()
                        .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                        .eq(PopUpAdUserOperate::getVipUserId, userId)
                        .eq(PopUpAdUserOperate::getOperate, operateEnum.getOperate())
        );

        return count;
    }

    @Async
    public void asyncAddBatch(String userUnionId, Long userId, List<Long> adIdList, Long storeId, String entrance){


        addBatch(adIdList, userUnionId, userId, PopUpAdUserOperateEnum.BROWSE.getOperate(),storeId, entrance);

    }

    public void addClickBatch(String userUnionId, Long userId, List<Long> adIdList, Long storeId, String entrance){


        addBatch(adIdList, userUnionId, userId, PopUpAdUserOperateEnum.CLICK.getOperate(),storeId, entrance);

    }



    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<Long> adIdList, String unionId, Long userId, Integer operate, Long storeId, String entrance){
        popUpAdUserOperateMapper.insertBatch(unionId, userId, adIdList, operate, storeId, entrance);
    }

    public String getUserUnionId(String tempUid){
        ServerResponseEntity<AuthSocialVO> response = authSocialFeignClient.getByTempUid(tempUid);
        AuthSocialVO authSocialVO;
        if (response.isSuccess() && Objects.nonNull(authSocialVO = response.getData())){
            return authSocialVO.getBizUnionid();
        }

        return null;
    }

    public UserApiVO getUserVOById(Long userId){
        ServerResponseEntity<UserApiVO> response = userFeignClient.getUserById(userId);
        UserApiVO userApiVO;
        if (response.isSuccess() && Objects.nonNull(userApiVO = response.getData())){
            return userApiVO;
        }

        return null;
    }


    public Integer getBrowseCountByAdId(Long adId){
        Integer browseCount = popUpAdUserOperateMapper.selectCount(
                new LambdaQueryWrapper<PopUpAdUserOperate>()
                        .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                        .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.BROWSE.getOperate())
        );

        return browseCount;
    }

    public Integer getBrowsePeopleCountByAdId(Long adId){
        Integer browsePeopleCount = popUpAdUserOperateMapper.getBrowsePeopleCountByAdId(adId);

        return browsePeopleCount;
    }

    public Integer getClickCountByAdId(Long adId){
        Integer clickCount = popUpAdUserOperateMapper.selectCount(
                new LambdaQueryWrapper<PopUpAdUserOperate>()
                        .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                        .eq(PopUpAdUserOperate::getOperate, PopUpAdUserOperateEnum.CLICK.getOperate())
        );

        return clickCount;
    }

    public Integer getClickPeopleCountByAdId(Long adId){
        Integer clickPeopleCount = popUpAdUserOperateMapper.getClickPeopleCountByAdId(adId);

        return clickPeopleCount;
    }

    public List<StoreVO> getStoreByIdList(List<Long> storeIdList){
        ServerResponseEntity<List<StoreVO>> response = storeFeignClient.listByStoreIdList(storeIdList);
        List<StoreVO> storeVOList;
        if (response.isSuccess() && CollectionUtil.isNotEmpty(storeVOList = response.getData())){
            return storeVOList;
        }

        return null;
    }

    public List<UserApiVO> getUserApiVOByUserIdList(List<Long> userIdList){
        ServerResponseEntity<List<UserApiVO>> response = userFeignClient.getUserByUserIds(userIdList);
        List<UserApiVO> userApiVOList;
        if (response.isSuccess() && Objects.nonNull(userApiVOList = response.getData())){
            return userApiVOList;
        }

        return null;
    }


    public List<UserApiVO> getUserApiVOByUnionIdList(List<String> unionIdList){
        ServerResponseEntity<List<UserApiVO>> response = userFeignClient.getUserByUnionIdList(unionIdList);
        List<UserApiVO> userApiVOList;
        if (response.isSuccess() && Objects.nonNull(userApiVOList = response.getData())){
            return userApiVOList;
        }

        return null;
    }

    public File createExcelFile(String  fileName){
        String filePath = SkqUtils.getExcelFilePath() + "/" + fileName + ".xlsx";
        File realFile = new File(filePath);
        return realFile;
    }

    public ExcelWriter createExcelWriter(File file){
        try {
            OutputStream outputStream = new FileOutputStream(file);
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            return excelWriter;
        } catch (FileNotFoundException e) {
            log.error("CREATE FILE IS FAIL:{}",e);
        }
        return null;
    }

    public void excelWriter(Integer sheetIndex, String sheetName, Class clazz, ExcelWriter excelWriter, List data){

        if (CollectionUtil.isNotEmpty(data) && data.get(0).getClass().equals(clazz)){
            //生成第一个sheet保存任务统计数据
            WriteSheet groupPushTaskStatisticSheet = EasyExcel.writerSheet(sheetIndex, sheetName)
                    .head(clazz).build();
            excelWriter.write(data, groupPushTaskStatisticSheet);
        }

    }


    public void uploadFile(File excelFile, Integer size, FinishDownLoadDTO finishDownLoadDTO){
        try {
            if(excelFile.isFile()){
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                FileInputStream fileInputStream = new FileInputStream(excelFile);


                String contentType = "application/vnd.ms-excel";
                MultipartFile multipartFile = new MultipartFileDto(excelFile.getName(), excelFile.getName(),
                        contentType, fileInputStream);
                String originalFilename = multipartFile.getOriginalFilename();
                String mimoPath = "excel/" + time + "/" + originalFilename;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                if(responseEntity.isSuccess()){
                    log.info("---ExcelUploadService---" + responseEntity.toString());
                    //下载中心记录
                    finishDownLoadDTO.setCalCount(size);
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(excelFile.getPath());
            }
        } catch (Exception e) {
            log.error("开屏广告会员行为明细导出异常，message is:{}",e);

            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("触达数据导出，excel生成zip失败");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }

    }


    public FinishDownLoadDTO createFinishDownLoadDTO(String fileName){
        CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
        downloadRecordDTO.setDownloadTime(new Date());
        downloadRecordDTO.setFileName(fileName);
        downloadRecordDTO.setCalCount(0);
        downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
        downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
        ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);

        if(serverResponseEntity.isSuccess()){
            Long downLoadHisId = Long.parseLong(serverResponseEntity.getData().toString());
            FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
            finishDownLoadDTO.setId(downLoadHisId);
            finishDownLoadDTO.setFileName(fileName);
            return finishDownLoadDTO;
        }


        return null;
    }


    @Async
    public void batchReceive(BatchReceiveCouponDTO batchReceiveCouponDTO){

        try {
//
            ServerResponseEntity<Void> serverResponse = tCouponFeignClient.batchReceive(batchReceiveCouponDTO);
            if (serverResponse.isFail()){
                log.error("BATCH RECEIVE COUPON IS FAIL, MESSAGE IS {}",serverResponse.getMsg());
            }
//            return serverResponse.isSuccess();

        }catch (Exception e){

            log.error("BATCH RECEIVE COUPON IS FAIL, MESSAGE IS {}",e);
        }

    }


    public boolean validationOperation(Long adId) {
        UserInfoInTokenBO user = AuthUserContext.get();

        if(Objects.nonNull(user)){
            Integer count = this.getCountByUserAndAd(user.getUserId(), adId, PopUpAdUserOperateEnum.CLICK);

            if (Objects.nonNull(count) && count > 0){
                return false;
            }
        }

        return true;
    }
}
