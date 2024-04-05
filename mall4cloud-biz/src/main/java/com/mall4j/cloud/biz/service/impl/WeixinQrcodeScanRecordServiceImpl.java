package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinQrcodeScanRecordLogVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.dto.WeixinQrcodeScanRecordDTO;
import com.mall4j.cloud.biz.mapper.WeixinQrcodeScanRecordMapper;
import com.mall4j.cloud.biz.model.WeixinQrcodeScanRecord;
import com.mall4j.cloud.biz.service.WeixinQrcodeScanRecordService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeScanRecordVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信二维码扫码记录表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
@Slf4j
@Service
@Transactional
public class WeixinQrcodeScanRecordServiceImpl implements WeixinQrcodeScanRecordService {

    @Autowired
    private WeixinQrcodeScanRecordMapper weixinQrcodeScanRecordMapper;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
//    @Autowired
//    private OnsMQTemplate soldUploadExcelTemplate;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    /***
     * 通用查询
     * @param pageDTO 分页参数
     * @param isScanSubscribe
     * @param sceneId
     * @param sceneSrc
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public PageVO<WeixinQrcodeScanRecordVO> pageList(PageDTO pageDTO, String isScanSubscribe, String sceneId, String autoScanId,Integer sceneSrc, String openid,String startTime, String endTime) {
        Date startDate= !StringUtils.isEmpty(startTime)? DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss"):null;
        Date endDate= !StringUtils.isEmpty(endTime)?DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss"):null;
        return PageUtil.doPage(pageDTO, () -> weixinQrcodeScanRecordMapper.getList(isScanSubscribe,sceneId,autoScanId,sceneSrc,openid,startDate,endDate));
    }

    @Override
    public List<WeixinQrcodeScanRecordVO> exportList(String isScanSubscribe, String qrcodeId, String autoScanId,Integer sceneSrc,String openid, String startTime, String endTime) {
        Date startDate= !StringUtils.isEmpty(startTime)? DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss"):null;
        Date endDate= !StringUtils.isEmpty(endTime)?DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss"):null;
        return weixinQrcodeScanRecordMapper.getList(isScanSubscribe,qrcodeId,autoScanId,sceneSrc,openid,startDate,endDate);
    }

    @Override
    public PageVO<WeixinQrcodeScanRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinQrcodeScanRecordMapper.list());
    }

    @Override
    public WeixinQrcodeScanRecord getById(Long id) {
        return weixinQrcodeScanRecordMapper.getById(id);
    }

    @Override
    public void save(WeixinQrcodeScanRecord weixinQrcodeScanRecord) {
        weixinQrcodeScanRecordMapper.save(weixinQrcodeScanRecord);
    }

    @Override
    public void update(WeixinQrcodeScanRecord weixinQrcodeScanRecord) {
        weixinQrcodeScanRecordMapper.update(weixinQrcodeScanRecord);
    }

    @Override
    public void deleteById(Long id) {
        weixinQrcodeScanRecordMapper.deleteById(id);
    }

    /**
     * 通用保存
     * @param scanRecordDTO
     */
    @Override
    public void saveQrcodeScanRecord(WeixinQrcodeScanRecordDTO scanRecordDTO) {
        if(scanRecordDTO!=null){
            WeixinQrcodeScanRecord scanRecord=new WeixinQrcodeScanRecord();
            BeanUtils.copyProperties(scanRecordDTO,scanRecord);

            scanRecord.setId(RandomUtil.getUniqueNumStr());
            scanRecord.setCreateTime(new Date());
            scanRecord.setDelFlag(0);
            this.save(scanRecord);
        }
    }

    /**
     * 导出扫码自动回复(数据详情)
     * @param isScanSubscribe
     * @param sceneSrc
     * @param openid
     * @param startTime
     * @param endTime
     */
    @Override
    public List<WeixinQrcodeScanRecordLogVO> soldScanRecs(String isScanSubscribe, String qrcodeId, String autoScanId,Integer sceneSrc,
                             String openid, String startTime, String endTime, Long downLoadHisId,HttpServletResponse response) {
        List<WeixinQrcodeScanRecordVO> scanRecordVOList = this.exportList(isScanSubscribe,
                qrcodeId,
                autoScanId,
                sceneSrc,openid,
                startTime,
                endTime);

        if (scanRecordVOList==null || scanRecordVOList.size() <= 0) {
            throw new LuckException("无数据导出");
        }

        //下载中心记录
//        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
//        finishDownLoadDTO.setId(downLoadHisId);

        int seq=0;
        List<WeixinQrcodeScanRecordLogVO> result=new ArrayList<>();
        for (WeixinQrcodeScanRecordVO item : scanRecordVOList) {
            WeixinQrcodeScanRecordLogVO scanRecordLogVO=new WeixinQrcodeScanRecordLogVO();
            BeanUtils.copyProperties(item,scanRecordLogVO);
            scanRecordLogVO.setId(seq+"");
            seq=seq+1;
            result.add(scanRecordLogVO);
        }

        return result;



//        try {
//            //生成excel文件
//            String exlcePath=createExcelFile(result);
//            File file=new File(exlcePath);
//            if(file.isFile()){
//                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                FileInputStream fileInputStream = new FileInputStream(exlcePath);
//                String contentType = "application/vnd.ms-excel";
//                MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
//                        contentType, fileInputStream);
//                String originalFilename = multipartFile.getOriginalFilename();
//                String mimoPath = "excel/" + time + "/" + originalFilename;
//                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
//                if(responseEntity.isSuccess()){
//                    log.info("---ExcelUploadService---" + responseEntity.toString());
//                    //下载中心记录
//                    String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ WeixinQrcodeScanRecordLogVO.EXCEL_NAME;
//                    finishDownLoadDTO.setFileName(fileName);
//                    finishDownLoadDTO.setCalCount(result.size());
//                    finishDownLoadDTO.setStatus(1);
//                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
//                    finishDownLoadDTO.setRemarks("导出成功");
//                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
//                }
//                //删除本地临时文件
//                cn.hutool.core.io.FileUtil.del(exlcePath);
//            }
//        }catch (Exception e){
//            //下载中心记录
//            if(finishDownLoadDTO!=null){
//                finishDownLoadDTO.setStatus(2);
//                finishDownLoadDTO.setRemarks("兑券数据导出，excel生成zip失败");
//                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
//            }
//        }
    }

    private String createExcelFile(List<WeixinQrcodeScanRecordLogVO> userExcelVOList){
        String file=null;
        try {
            String pathExport= SystemUtils.getExcelFilePath()+"/"+ SystemUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, WeixinQrcodeScanRecordLogVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(userExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }
}
