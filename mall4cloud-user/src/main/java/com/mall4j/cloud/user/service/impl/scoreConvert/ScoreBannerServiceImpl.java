package com.mall4j.cloud.user.service.impl.scoreConvert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreCodeVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.vo.SoldScoreBannerShopVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.user.constant.scoreConvert.ScoreActivityStatusEnum;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreBannerMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreBannerShopMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreBannerUrlMapper;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBanner;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBannerShop;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBannerUrl;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBannerService;
import com.mall4j.cloud.user.vo.scoreConvert.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分Banner
 *
 * @author shijing
 * @date 2022-1-23
 */

@Slf4j
@Service
@Transactional
public class ScoreBannerServiceImpl implements ScoreBannerService {
    @Resource
    private ScoreBannerMapper scoreBannerMapper;
    @Resource
    private ScoreBannerShopMapper scoreBannerShopMapper;
    @Resource
    private ScoreBannerUrlMapper scoreBannerUrlMapper;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;


    @Override
    public ServerResponseEntity<PageInfo<ScoreBannerListVO>> list(ScoreConvertListDTO param) {
        log.info("积分Banner列表的搜索条件为 :{}", param);
        PageInfo<ScoreBannerListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                scoreBannerMapper.list(param)
        );

        //处理返回值
        List<ScoreBannerListVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                if (temp.getStatus().equals(0)){
                    //活动状态
                    if (System.currentTimeMillis() < temp.getStartTime().getTime()){
                        temp.setActivityStatus(ScoreActivityStatusEnum.NOT_START.desc());
                        temp.setActivityStatusKey(ScoreActivityStatusEnum.NOT_START.value());
                    }else if (System.currentTimeMillis() > temp.getStartTime().getTime() && System.currentTimeMillis() < temp.getEndTime().getTime()){
                        temp.setActivityStatus(ScoreActivityStatusEnum.START.desc());
                        temp.setActivityStatusKey(ScoreActivityStatusEnum.START.value());
                    }else if (System.currentTimeMillis() > temp.getEndTime().getTime()){
                        temp.setActivityStatus(ScoreActivityStatusEnum.OVER.desc());
                        temp.setActivityStatusKey(ScoreActivityStatusEnum.OVER.value());
                    }
                }else {
                    temp.setActivityStatus(ScoreActivityStatusEnum.DISABLE.desc());
                    temp.setActivityStatusKey(ScoreActivityStatusEnum.DISABLE.value());
                }
                //适用门店数量
                if (!temp.getIsAllShop()){
                    List<ScoreBannerShop> bannerShopList = scoreBannerShopMapper.selectList(new LambdaQueryWrapper<ScoreBannerShop>().eq(ScoreBannerShop::getBannerId, temp.getId()));
                    temp.setShopNum(bannerShopList.size());
                }
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> save(BannerSaveOrUpdateDTO param) {
        ScoreBanner scoreBanner = BeanUtil.copyProperties(param, ScoreBanner.class);
        scoreBanner.setType(param.getBannerType());
        scoreBanner.setCreateId(AuthUserContext.get().getUserId());
        scoreBanner.setCreateName(AuthUserContext.get().getUsername());
        scoreBanner.setUpdateId(AuthUserContext.get().getUserId());
        scoreBanner.setUpdateName(AuthUserContext.get().getUsername());
        scoreBannerMapper.insert(scoreBanner);

        //新建门店关联
        if (!param.getIsAllShop()){
            addShops(param.getShops(),scoreBanner.getId());
        }

        //新建图片关联
        addUrls(param.getBannerUrlList(),scoreBanner.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<BannerShopDTO>> selectActivityShop(BannerSaveOrUpdateDTO param) {
        List<BannerShopDTO> res = new ArrayList<>();
        log.info("查询重复门店参数:{}",JSONObject.toJSONString(param));
        if (param.getBannerType() == null || param.getStartTime() == null || param.getEndTime() == null) {
            return ServerResponseEntity.success();
        }
        LambdaQueryWrapper<ScoreBanner> scoreBannerLambdaQueryWrapper = new LambdaQueryWrapper<ScoreBanner>().eq(ScoreBanner::getType, param.getBannerType())
                .and(s -> s.between(ScoreBanner::getEndTime, param.getStartTime(), param.getEndTime()).or(i->i.le(ScoreBanner::getStartTime,param.getEndTime()).ge(ScoreBanner::getEndTime,param.getEndTime())))
                .eq(ScoreBanner::getStatus, 0)
                .gt(ScoreBanner::getEndTime,new Date())
                .orderByAsc(ScoreBanner::getWeight)
                .orderByDesc(ScoreBanner::getCreateTime);
        if (param.getId() != null) {
            scoreBannerLambdaQueryWrapper.ne(ScoreBanner::getId,param.getId());
        }
        List<ScoreBanner> scoreBanners = scoreBannerMapper.selectList(scoreBannerLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(scoreBanners)) {
            ScoreBanner scoreBanner = scoreBanners.get(0);
            log.info("重复活动数据:{}", JSONObject.toJSONString(scoreBanner));
            Long activityId = scoreBanner.getId();
            String activityName = scoreBanner.getName();
            if (scoreBanner.getIsAllShop() && param.getIsAllShop()) {
                ServerResponseEntity<List<StoreCodeVO>> responseEntity = storeFeignClient.allStoreListIsShow();
                if (responseEntity != null && responseEntity.isSuccess() && !CollectionUtils.isEmpty(responseEntity.getData())) {
                    responseEntity.getData().forEach( storeCodeVO -> {
                        BannerShopDTO bannerShopDTO = new BannerShopDTO();
                        bannerShopDTO.setActivityId(activityId);
                        bannerShopDTO.setActivityName(activityName);
                        bannerShopDTO.setStoreCode(storeCodeVO.getStoreCode());
                        bannerShopDTO.setStoreName(storeCodeVO.getStationName());
                        res.add(bannerShopDTO);
                    });
                }
            } else if(!scoreBanner.getIsAllShop() && !param.getIsAllShop() && !CollectionUtils.isEmpty(param.getShops())) {
                List<ScoreBannerShop> scoreBannerShops = scoreBannerShopMapper.selectList(new LambdaQueryWrapper<ScoreBannerShop>().eq(ScoreBannerShop::getBannerId, activityId));
                if (!CollectionUtils.isEmpty(scoreBannerShops)) {
                    List<Long> shopIds = scoreBannerShops.stream().map(ScoreBannerShop::getShopId).collect(Collectors.toList());
                    List<Long> ids = param.getShops().stream().filter(id -> shopIds.contains(id)).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(ids)) {
                        return ServerResponseEntity.success();
                    }
                    ServerResponseEntity<List<StoreVO>> responseEntity = storeFeignClient.listBypByStoreIdList(ids);
                    if (responseEntity != null && responseEntity.isSuccess() && !CollectionUtils.isEmpty(responseEntity.getData())) {
                        responseEntity.getData().forEach( storeVO -> {
                            BannerShopDTO bannerShopDTO = new BannerShopDTO();
                            bannerShopDTO.setActivityId(activityId);
                            bannerShopDTO.setActivityName(activityName);
                            bannerShopDTO.setStoreCode(storeVO.getStoreCode());
                            bannerShopDTO.setStoreName(storeVO.getName());
                            res.add(bannerShopDTO);
                        });
                    }
                }
            } else if (scoreBanner.getIsAllShop() && !param.getIsAllShop() && !CollectionUtils.isEmpty(param.getShops())) {
                ServerResponseEntity<List<StoreVO>> responseEntity = storeFeignClient.listBypByStoreIdList(param.getShops());
                if (responseEntity != null && responseEntity.isSuccess() && !CollectionUtils.isEmpty(responseEntity.getData())) {
                    responseEntity.getData().forEach( storeVO -> {
                        BannerShopDTO bannerShopDTO = new BannerShopDTO();
                        bannerShopDTO.setActivityId(activityId);
                        bannerShopDTO.setActivityName(activityName);
                        bannerShopDTO.setStoreCode(storeVO.getStoreCode());
                        bannerShopDTO.setStoreName(storeVO.getName());
                        res.add(bannerShopDTO);
                    });
                }
            }else if (!scoreBanner.getIsAllShop() && param.getIsAllShop()) {
                List<ScoreBannerShop> scoreBannerShops = scoreBannerShopMapper.selectList(new LambdaQueryWrapper<ScoreBannerShop>().eq(ScoreBannerShop::getBannerId, activityId));
                if (!CollectionUtils.isEmpty(scoreBannerShops)) {
                    List<Long> shopIds = scoreBannerShops.stream().map(ScoreBannerShop::getShopId).collect(Collectors.toList());
                    ServerResponseEntity<List<StoreVO>> responseEntity = storeFeignClient.listBypByStoreIdList(shopIds);
                    if (responseEntity != null && responseEntity.isSuccess() && !CollectionUtils.isEmpty(responseEntity.getData())) {
                        responseEntity.getData().forEach( storeVO -> {
                            BannerShopDTO bannerShopDTO = new BannerShopDTO();
                            bannerShopDTO.setActivityId(activityId);
                            bannerShopDTO.setActivityName(activityName);
                            bannerShopDTO.setStoreCode(storeVO.getStoreCode());
                            bannerShopDTO.setStoreName(storeVO.getName());
                            res.add(bannerShopDTO);
                        });
                    }
                }
            }
        }

        return ServerResponseEntity.success(res);
    }


    @Async
    @Override
    public void export(List<BannerShopDTO> list,Long downLoadHisId, HttpServletResponse response) {
        //
//        ScoreConvert scoreConvert = scoreConvertMapper.selectById(param.getConvertId());

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);
        String fileName="Banner图门店重复数据-"+ SoldScoreBannerShopVO.EXCEL_NAME;
        finishDownLoadDTO.setFileName(fileName);


        if(CollUtil.isEmpty(list)) {
            finishDownLoadDTO.setRemarks("无兑换数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无兑换数据导出");
            return;
        }

        List<SoldScoreBannerShopVO> resultData = new ArrayList<>();
        list.forEach( res -> {
            SoldScoreBannerShopVO soldScoreBannerShopVO = new SoldScoreBannerShopVO();
            soldScoreBannerShopVO.setActivityId(res.getActivityId());
            soldScoreBannerShopVO.setActivityName(res.getActivityName());
            soldScoreBannerShopVO.setStoreCode(res.getStoreCode());
            soldScoreBannerShopVO.setStoreName(res.getStoreName());
            resultData.add(soldScoreBannerShopVO);
        });
        //生成excel文件
        String exlcePath=createExcelFile(resultData,fileName);

        try {
            File file=new File(exlcePath);
            if(file.isFile()){
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                FileInputStream fileInputStream = new FileInputStream(exlcePath);
                String contentType = "application/vnd.ms-excel";
                MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                        contentType, fileInputStream);
                String originalFilename = multipartFile.getOriginalFilename();
                String mimoPath = "excel/" + time + "/" + originalFilename;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                if(responseEntity.isSuccess()){
                    log.info("---ExcelUploadService---" + responseEntity.toString());
                    //下载中心记录
//                    String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+ SoldScoreCouponLogVO.EXCEL_NAME;
                    finishDownLoadDTO.setCalCount(list.size());
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(exlcePath);
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

    private String createExcelFile(List<SoldScoreBannerShopVO> userExcelVOList, String  fileName){
        String file=null;
        try {
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String pathExport= SystemUtils.getExcelFilePath()+"/"+fileName+".xls";
//            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, SoldScoreBannerShopVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(userExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public ServerResponseEntity<Void> update(BannerSaveOrUpdateDTO param) {
        //修改banner活动数据
        ScoreBanner scoreBanner = BeanUtil.copyProperties(param, ScoreBanner.class);
        scoreBanner.setUpdateId(AuthUserContext.get().getUserId());
        scoreBanner.setUpdateName(AuthUserContext.get().getUsername());
        scoreBannerMapper.updateById(scoreBanner);

        //删除原来门店关联，新建关联
        scoreBannerShopMapper.delete(new LambdaUpdateWrapper<ScoreBannerShop>().eq(ScoreBannerShop::getBannerId, param.getId()));
        addShops(param.getShops(),param.getId());

        //删除原来图片关联，新建关联
        scoreBannerUrlMapper.delete(new LambdaUpdateWrapper<ScoreBannerUrl>().eq(ScoreBannerUrl::getBannerId, param.getId()));
        addUrls(param.getBannerUrlList(),param.getId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateWeight(BannerSaveOrUpdateDTO param) {
        //修改banner活动数据
        ScoreBanner scoreBanner = new ScoreBanner();
        scoreBanner.setId(param.getId());
        scoreBanner.setWeight(param.getWeight());
        scoreBanner.setStatus(param.getStatus());
        scoreBanner.setUpdateId(AuthUserContext.get().getUserId());
        scoreBanner.setUpdateName(AuthUserContext.get().getUsername());
        scoreBannerMapper.updateById(scoreBanner);
        return ServerResponseEntity.success();
    }


    @Override
    public ServerResponseEntity<Void> updateStatus(Long id, Integer status) {
        ScoreBanner scoreBanner = new ScoreBanner();
        scoreBanner.setId(id);
        scoreBanner.setStatus(status);
        scoreBannerMapper.updateById(scoreBanner);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Long id) {
        scoreBannerMapper.deleteById(id);
        scoreBannerShopMapper.delete(new LambdaUpdateWrapper<ScoreBannerShop>().eq(ScoreBannerShop::getBannerId,id));
        scoreBannerUrlMapper.delete(new LambdaUpdateWrapper<ScoreBannerUrl>().eq(ScoreBannerUrl::getBannerId,id));

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<BannerDetailVO> selectDetail(Long id) {
        ScoreBanner scoreBanner = scoreBannerMapper.selectById(id);
        BannerDetailVO result = BeanUtil.copyProperties(scoreBanner, BannerDetailVO.class);

        if (!result.getIsAllShop()) {
            List<ScoreBannerShop> bannerShopList = scoreBannerShopMapper.selectList(new LambdaQueryWrapper<ScoreBannerShop>().eq(ScoreBannerShop::getBannerId, id));
            List<Long> shops = bannerShopList.stream().map(ScoreBannerShop::getShopId).collect(Collectors.toList());
            result.setShopNum(shops.size());
            result.setShops(shops);
        }

        List<ScoreBannerUrl> scoreBannerUrls = scoreBannerUrlMapper.selectList(new LambdaQueryWrapper<ScoreBannerUrl>().eq(ScoreBannerUrl::getBannerId, id));
        List<ScoreBannerUrlDTO> bannerUrlList = Convert.toList(ScoreBannerUrlDTO.class, scoreBannerUrls);
        result.setBannerUrlList(bannerUrlList);

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerListForApp() {
        Long bannerId = scoreBannerMapper.appActivity();
        List<ScoreBannerUrl> scoreBannerUrls = scoreBannerUrlMapper.selectList(new LambdaQueryWrapper<ScoreBannerUrl>().eq(ScoreBannerUrl::getBannerId, bannerId));
        List<ScoreBannerUrlDTO> result = Convert.toList(ScoreBannerUrlDTO.class, scoreBannerUrls);
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerData(Long shopId,Integer type) {
        Long bannerId = scoreBannerMapper.bannerData(shopId,type);
        if (bannerId == null) {
            return ServerResponseEntity.success();
        }
        List<ScoreBannerUrl> scoreBannerUrls = scoreBannerUrlMapper.selectList(new LambdaQueryWrapper<ScoreBannerUrl>().eq(ScoreBannerUrl::getBannerId, bannerId));
        List<ScoreBannerUrlDTO> result = Convert.toList(ScoreBannerUrlDTO.class, scoreBannerUrls);
        return ServerResponseEntity.success(result);
    }

    private void addShops(List<Long> shops, Long bannerId) {
        List<ScoreBannerShop> scoreBannerShops = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)) {
            shops.forEach(temp -> {
                ScoreBannerShop scoreBannerShop = new ScoreBannerShop();
                scoreBannerShop.setBannerId(bannerId);
                scoreBannerShop.setShopId(temp);
                scoreBannerShops.add(scoreBannerShop);
            });
            scoreBannerShopMapper.insertBatch(scoreBannerShops);
        }
    }

    private void addUrls(List<ScoreBannerUrlDTO> urls, Long bannerId) {
        List<ScoreBannerUrl> scoreBannerUrls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(urls)) {
            urls.forEach(temp -> {
                ScoreBannerUrl scoreBannerUrl = BeanUtil.copyProperties(temp, ScoreBannerUrl.class);
                scoreBannerUrl.setBannerId(bannerId);
                scoreBannerUrls.add(scoreBannerUrl);
            });
            scoreBannerUrlMapper.insertBatch(scoreBannerUrls);
        }
    }

}
