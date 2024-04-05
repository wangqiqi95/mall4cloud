package com.mall4j.cloud.product.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.delivery.vo.ShopTransportVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.event.SimpleSoldSpuEvent;
import com.mall4j.cloud.product.event.SoldSpuEvent;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.SkuService;
import com.mall4j.cloud.product.service.SpuExcelService;
import com.mall4j.cloud.product.service.SpuService;
import com.mall4j.cloud.product.vo.SimpleSpuExcelVO;
import com.mall4j.cloud.product.vo.SpuExcelVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Component("SoldAllSpuListener")
@RefreshScope
public class SoldAllSpuListener {

    @Value("${mall4cloud.product.exportSpupageSize:50000}")
    @Setter
    private Integer exportSpupageSize;

    /**
     * 商品显示信息
     */
    public static final String ENABLE = "上架";
    public static final String DISABLE = "下架";
    public static final String OFFLINE = "违规下架";
    public static final String WAIT_AUDIT = "等待审核";
    public static final String[] DELIVERY_MODE = {DeliveryType.SAME_CITY.description(), DeliveryType.STATION.description(), "用户自提+同城配送"};
    public static final String[] STATUS_MODE = {ENABLE,DISABLE};

    @Autowired
    private SpuExcelService spuExcelService;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private SkuService skuService;
    @Resource
    private SpuMapper spuMapper;

    @Async
    @EventListener(SimpleSoldSpuEvent.class)
    public void soldSpuEvent(SimpleSoldSpuEvent event) {

        log.info("--全量商品数据导出来了 预警----");

        int currentPage = 1;
        int pageSize = exportSpupageSize;
        Long downLoadHisId= event.getDownLoadHisId();
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        Long storeId= Constant.MAIN_SHOP;
        List<SpuPageVO> spulist = spuMapper.listAllSpus();
        if(CollUtil.isEmpty(spulist)) {
            finishDownLoadDTO.setRemarks("无全量商品数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无全量商品数据导出");
            return;
        }
        ServerResponseEntity<List<ShopTransportVO>> response = deliveryFeignClient.listTransportByShopIdInsider(null);
        if (!Objects.equals(response.getCode(), ResponseEnum.OK.value())) {
            finishDownLoadDTO.setRemarks("运费模板获取失败");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("运费模板获取失败");
            return;
        }

        List<Long> spuIds = spulist.stream().map(SpuPageVO::getSpuId).collect(Collectors.toList());
        int totalCount = skuService.count(new QueryWrapper<Sku>().lambda().in(Sku::getSpuId,spuIds));
        log.info("全量商品数据导出，spu总数【{}】 sku总条数【{}】",spuIds.size(),totalCount);
        if (totalCount == 0) {
            finishDownLoadDTO.setRemarks("无全量商品数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无全量商品数据导出");
            return;
        }
        long startTime = System.currentTimeMillis();
        log.info("全量商品数据导出，开始执行导出任务--------->>>>>");

        //商品主数据
        Map<Long, SpuPageVO> spuMap = spulist.stream().collect(Collectors.toMap(SpuPageVO::getSpuId,s->s));
        //运费模板
        Map<Long, String> transportMap = response.getData().stream().collect(Collectors.toMap(ShopTransportVO::getTransportId, ShopTransportVO::getTransName));
        //本次导出总条数
        finishDownLoadDTO.setCalCount(totalCount);
        //计算本次导出总页数
        int totalPage = (totalCount + pageSize -1) / pageSize;
        log.info("全量商品数据导出，spu总数【{}】 sku总条数【{}】 sku总页数【{}】",spuIds.size(),totalCount,totalPage);

        // 分页查询数据
        PageAdapter pageAdapter=new PageAdapter(currentPage,pageSize);

        Boolean isInviteStore=false;
        //存放全部导出excel文件
        List<String> filePaths=new ArrayList<>();
        int seq=1;//excel表格序号
        //先导出第一页数据，生成第一个excel
        List<SoldSpuExcelVO> soldSpus = skuService.excelSkuList(spuIds,storeId,pageAdapter,true);
        List<SimpleSpuExcelVO> soldSpuExcelList1=mapperFacade.mapAsList(soldSpus,SimpleSpuExcelVO.class);
        //设置商品相关信息
        initDate(soldSpuExcelList1,null,transportMap,spuMap,isInviteStore);
        //生成excel文件
        String exlcePath1=createExcelFile(soldSpuExcelList1,seq);

        if(StrUtil.isNotBlank(exlcePath1)){
            filePaths.add(exlcePath1);
        }

        //存在多页数据，循环处理
        if(totalPage>1){
            for (int i = 2; i <= totalPage; i++) {
                currentPage = currentPage + 1;
                seq++;
                pageAdapter=new PageAdapter(currentPage,pageSize);
                List<SoldSpuExcelVO> soldSpus2 = skuService.excelSkuList(spuIds,storeId,pageAdapter,true);
                List<SimpleSpuExcelVO> soldSpuExcelList2=mapperFacade.mapAsList(soldSpus2,SimpleSpuExcelVO.class);
                log.info("全量商品数据导出--->循环处理中 当前页为第【{}】页  单页限制最大条数【{}】 获取到数据总条数【{}】",pageAdapter.getBegin(),pageAdapter.getSize(),soldSpuExcelList2.size());
                if(CollectionUtil.isNotEmpty(soldSpuExcelList2)){
                    //设置商品相关信息
                    initDate(soldSpuExcelList2,null,transportMap,spuMap,isInviteStore);
                    //生成excel文件
                    String exlcePath2=createExcelFile(soldSpuExcelList2,seq);

                    if(StrUtil.isNotBlank(exlcePath2)){
                        filePaths.add(exlcePath2);
                    }
                }
            }
        }


        if(CollectionUtil.isNotEmpty(filePaths)){
            try {

                log.info("全量商品数据导出，导出excel文件总数 【{}】",filePaths.size());

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";

                File copyFile=new File(zipFilePathExport);
                copyFile.mkdirs();

                List<File> fromFileList=new ArrayList<>();
                List<File> backFileList=new ArrayList<>();

                filePaths.forEach(item->{
                    File file=new File(item);
                    fromFileList.add(file);
                    log.info("全量商品数据导出，单个excel文件信息 文件名【{}】 文件大小【{}】",file.getName(),cn.hutool.core.io.FileUtil.size(file));
                });
                //文件存放统一目录
                FileUtil.copyCodeToFile(fromFileList,zipFilePathExport,backFileList);
                //压缩统一文件目录
                String zipPath= CompressUtil.zipFile(copyFile,"zip");

                if(new File(zipPath).isFile()){

                    FileInputStream fileInputStream = new FileInputStream(zipPath);

                    String zipFileName="soldAllSpu_"+ AuthUserContext.get().getUserId()+"_"+time+".zip";
                    MultipartFile multipartFile = new MultipartFileDto(zipFileName, zipFileName,
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "excel/" + time + "/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if(responseEntity.isSuccess()){
                        log.info("---ExcelUploadService---" + responseEntity.toString());
                        //下载中心记录
                        String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ SimpleSpuExcelVO.EXCEL_NAME;
                        finishDownLoadDTO.setFileName(fileName);
                        finishDownLoadDTO.setStatus(1);
                        finishDownLoadDTO.setFileUrl(responseEntity.getData());
                        finishDownLoadDTO.setRemarks("导出成功");
                        downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    }
                    //删除本地临时文件
                    cn.hutool.core.io.FileUtil.del(zipPath);
                    cn.hutool.core.io.FileUtil.del(copyFile);
                }
            }catch (Exception e){
                log.info("全量商品数据导出，excel生成zip失败",e);
                //下载中心记录
                if(finishDownLoadDTO!=null){
                    finishDownLoadDTO.setStatus(2);
                    finishDownLoadDTO.setRemarks("全量商品数据导出，excel生成zip失败");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
            }

        }else{
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("全量商品数据导出，没有可导出的文件");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }

        log.info("全量商品数据导出，结束执行任务------>>耗时：{}ms", System.currentTimeMillis() - startTime);

    }

    private void initDate(List<SimpleSpuExcelVO> skuList, StoreVO storeVO, Map<Long, String> transportMap,Map<Long, SpuPageVO> spuPageVOMap,Boolean isInviteStore){
        if(CollectionUtil.isNotEmpty(skuList)){
            int index=0;
            for (SimpleSpuExcelVO spuExcelVO : skuList) {
                index++;
                //spu信息
                spuExcelVO.setSpuName(spuPageVOMap.get(spuExcelVO.getSpuId()).getSpuName());
                spuExcelVO.setSpuCode(spuPageVOMap.get(spuExcelVO.getSpuId()).getSpuCode());
                spuExcelVO.setPlatformCategory(spuPageVOMap.get(spuExcelVO.getSpuId()).getPlatformCategory());
                spuExcelVO.setShopCategory(spuPageVOMap.get(spuExcelVO.getSpuId()).getShopCategory());
                spuExcelVO.setSpuStatus(spuPageVOMap.get(spuExcelVO.getSpuId()).getStatus().toString());
                spuExcelVO.setDeliveryTemplateId(spuPageVOMap.get(spuExcelVO.getSpuId()).getDeliveryTemplateId());
                spuExcelVO.setSaleNum(spuPageVOMap.get(spuExcelVO.getSpuId()).getSaleNum());
                spuExcelVO.setCreateTime(spuPageVOMap.get(spuExcelVO.getSpuId()).getCreateTime());
                spuExcelVO.setUpdateTime(spuPageVOMap.get(spuExcelVO.getSpuId()).getUpdateTime());
                spuExcelVO.setTransName(transportMap.get(spuExcelVO.getDeliveryTemplateId()));
                // spu状态
                if (Objects.equals(spuExcelVO.getSpuStatus(), StatusEnum.ENABLE.value().toString())) {
                    spuExcelVO.setSpuStatus(ENABLE);
                } else if (Objects.equals(spuExcelVO.getSpuStatus(), StatusEnum.DISABLE.value().toString())) {
                    spuExcelVO.setSpuStatus(DISABLE);
                } else if (Objects.equals(spuExcelVO.getSpuStatus(), StatusEnum.OFFLINE.value().toString())) {
                    spuExcelVO.setSpuStatus(OFFLINE);
                } else if (Objects.equals(spuExcelVO.getSpuStatus(), StatusEnum.WAIT_AUDIT.value().toString())) {
                    spuExcelVO.setSpuStatus(WAIT_AUDIT);
                }
                //小程序路径(详情页)
                String scene="?id="+spuExcelVO.getSpuId()+"&s=1";
                spuExcelVO.setSpuUrl("pages/detail/detail"+scene);
            }
        }
    }

    private String createExcelFile(List<SimpleSpuExcelVO> userExcelVOList,int seq){
        String file=null;
        try {
            int calCount=userExcelVOList.size();
            long startTime = System.currentTimeMillis();
            log.info("开始执行全量商品数据生成excel 总条数【{}】",calCount);
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String pathExport= SkqUtils.getExcelFilePath()+"/"+seq+"_"+time+"_"+SkqUtils.getExcelName()+".xlsx";
            EasyExcel.write(pathExport, SimpleSpuExcelVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(userExcelVOList);
            log.info("结束执行全量商品数据生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,userExcelVOList.size(),pathExport);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }
}
