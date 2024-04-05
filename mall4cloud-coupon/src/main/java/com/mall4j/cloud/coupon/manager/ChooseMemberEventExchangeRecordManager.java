package com.mall4j.cloud.coupon.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.coupon.bo.AddChooseMemberEventExchangeRecordBO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventRecordDTO;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventExchangeRecordMapper;
import com.mall4j.cloud.coupon.model.ChooseMemberEventExchangeRecord;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordExcelVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ChooseMemberEventExchangeRecordManager {

    @Autowired
    private ChooseMemberEventExchangeRecordMapper chooseMemberEventExchangeRecordMapper;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;


    @Transactional(rollbackFor = Exception.class)
    public void addExchangeRecord(AddChooseMemberEventExchangeRecordBO eventExchangeRecordBO){


//        record.setEventId(eventExchangeRecordBO.getEventId());
//        record.setUserId(eventExchangeRecordBO.getUserId());
//        record.setBelongShopId(eventExchangeRecordBO.getBelongShopId());
//        record.setBelongShopName(eventExchangeRecordBO.getBelongShopName());
//        record.setExchangeNum(eventExchangeRecordBO.getExchangeNum());
//        record.setConsignee(eventExchangeRecordBO.getConsignee());
//        record.setDeliveryAddress(eventExchangeRecordBO.getDeliveryAddress());
//        record.setMobile(eventExchangeRecordBO.getMobile());
//        record.setDeliveryMobile(eventExchangeRecordBO.getDeliveryMobile());

        ChooseMemberEventExchangeRecord record = new ChooseMemberEventExchangeRecord();
        BeanUtils.copyProperties(eventExchangeRecordBO, record);
        if( Objects.nonNull(eventExchangeRecordBO.getBelongShopId()) && ( StrUtil.isEmpty(record.getBelongShopName()) || StrUtil.isEmpty(record.getBelongShopCode()) ) ){
            StoreVO storeVO = storeFeignClient.findByStoreId(record.getBelongShopId());
            if (Objects.nonNull(storeVO)) {
                record.setBelongShopName(storeVO.getName());
                record.setBelongShopCode(storeVO.getStoreCode());
            }
        }

        chooseMemberEventExchangeRecordMapper.insert(record);

    }

    public Integer getTheUserEventExchangeNum(Long userId, Long eventId){
        Integer theUserEventExchangeNum = chooseMemberEventExchangeRecordMapper
                .getTheUserEventExchangeNum(userId, eventId);

        return theUserEventExchangeNum;
    }

//    public PageVO<ChooseMemberEventExchangeRecordVO> getRecordByEventId(Long eventId){
//        PageVO<ChooseMemberEventExchangeRecordVO> recordPage = PageUtil.doPage()
//    }


    public Integer getExchangeMemberCountByEvent(@Param("eventId") Long eventId){
        return chooseMemberEventExchangeRecordMapper.getExchangeMemberCountByEvent(eventId);
    }

    /**
     * 添加物流信息
     * @param recordDTO
     */
    public void addLogistics(ChooseMemberEventRecordDTO recordDTO) {
        ChooseMemberEventExchangeRecord record = BeanUtil.copyProperties(recordDTO, ChooseMemberEventExchangeRecord.class);
        record.setExchangeRecordId(recordDTO.getId());
        record.setExportStatus(0);
        record.setDeliveryStatus(1);
        chooseMemberEventExchangeRecordMapper.updateById(record);
    }

    public void confirmExport(List<Long> ids) {
        chooseMemberEventExchangeRecordMapper.confirmExport(ids);
    }

    /**
     * 兑换记录导出
     * @param param
     * @param response
     */
    public void export(ChooseMemberEventExchangeRecordPageDTO param, HttpServletResponse response) {
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(param.getDownLoadHisId());
        String fileName="高价值会员兑换活动ID"+param.getEventId()+"-"+ ChooseMemberEventExchangeRecordExcelVO.EXCEL_NAME;
        finishDownLoadDTO.setFileName(fileName);

        // 写出内容
        List<ChooseMemberEventExchangeRecordVO> dbResult = chooseMemberEventExchangeRecordMapper.recordList(param);
        if(CollUtil.isEmpty(dbResult)) {
            finishDownLoadDTO.setRemarks("无兑换数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无兑换数据导出");
            return;
        }

        List<ChooseMemberEventExchangeRecordExcelVO> resultData = new ArrayList<>();
        dbResult.forEach( result -> {
            ChooseMemberEventExchangeRecordExcelVO excelVO = BeanUtil.copyProperties(result, ChooseMemberEventExchangeRecordExcelVO.class);
            if (result.getDeliveryStatus() != null) {
                if (result.getDeliveryStatus().equals(0)) {
                    excelVO.setDeliveryStatus("未发货");
                }
                if (result.getDeliveryStatus().equals(1)) {
                    excelVO.setDeliveryStatus("待发放");
                }
                if (result.getDeliveryStatus().equals(2)) {
                    excelVO.setDeliveryStatus("已发放");
                }
            }

            if (result.getExchangeType() != null) {
                if (result.getExchangeType() == 0) {
                    excelVO.setExchangeType("兑礼到店");
                }
                if (result.getExchangeType() == 1) {
                    excelVO.setExchangeType("兑礼到家");
                }
            }

            if (StrUtil.isNotBlank(result.getDeliveryAddress()) && StrUtil.isNotBlank(result.getDeliveryMobile()) && StrUtil.isNotBlank(result.getConsignee())) {
                excelVO.setDeliveryInfo(result.getConsignee()+","+result.getDeliveryMobile()+","+result.getDeliveryAddress());
            }
            resultData.add(excelVO);
        });

        //生成excel文件
        String excelPath=createExcelFile(resultData,fileName);

        try {
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
                    finishDownLoadDTO.setCalCount(dbResult.size());
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(excelPath);
            }
        }catch (Exception e){
            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("兑换数据导出，excel生成zip失败");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }

    }

    private String createExcelFile(List<ChooseMemberEventExchangeRecordExcelVO> recordExcelVOList, String  fileName){
        String file=null;
        try {
            String pathExport= SkqUtils.getExcelFilePath()+"/"+fileName+".xls";
            EasyExcel.write(pathExport, ChooseMemberEventExchangeRecordExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(recordExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 导入物流信息
     * @param file
     * @return
     */
    public Map<String, Integer> importRecord(MultipartFile file) {
        log.info("导入文件为：{}",file);

        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            log.error("获取输入流异常");
            e.printStackTrace();
        }

        //调用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //校验模板的正确性
        List<Object> objects = excelReader.readRow(0);
        log.info("模板表头信息：{}", JSONObject.toJSONString(objects));

        //列名和对象属性名一致
        excelReader.addHeaderAlias("兑换记录ID","exchangeRecordId");
        excelReader.addHeaderAlias("物流信息","trackingNumber");
        excelReader.addHeaderAlias("物流公司名称","logisticsCompany");

        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
        List<ChooseMemberEventExchangeRecordExcelVO> importData = excelReader.readAll(ChooseMemberEventExchangeRecordExcelVO.class);
        log.info("导入的记录信息为："+ JSONObject.toJSONString(importData));
        Integer succ = 0;
        Integer total = 0;
        if (importData != null) {
            total = importData.size();
            for (int i = 0; i < importData.size(); i++) {
                ChooseMemberEventExchangeRecordExcelVO record = importData.get(i);
                if (record.getExchangeRecordId() != null && StrUtil.isNotBlank(record.getLogisticsCompany())
                        && StrUtil.isNotBlank(record.getTrackingNumber()) ) {
                    ChooseMemberEventExchangeRecord dbRecord = chooseMemberEventExchangeRecordMapper.selectById(record.getExchangeRecordId());
                    if (dbRecord != null) {
                        dbRecord.setExportStatus(0);
                        dbRecord.setDeliveryStatus(1);
                        dbRecord.setTrackingNumber(record.getTrackingNumber());
                        dbRecord.setLogisticsCompany(record.getLogisticsCompany());
                        succ += chooseMemberEventExchangeRecordMapper.updateById(dbRecord);
                    }
                }
            }
        }

        Map<String,Integer> res = new HashMap<>();
        res.put("total",total);
        res.put("succ",succ);
        return res;
    }

    /**
     * 根据UserId查询用户兑换记录
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> queryUserRecord(Integer pageNo, Integer pageSize) {
        PageInfo<ChooseMemberEventExchangeRecordVO> result = PageHelper.startPage(pageNo, pageSize).doSelectPageInfo(() ->
                chooseMemberEventExchangeRecordMapper.queryUserRecord(AuthUserContext.get().getUserId())
        );

        return ServerResponseEntity.success(result);
    }
}
