package com.mall4j.cloud.user.service.impl.scoreConvert;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.constant.ConvertTypeEnum;
import com.mall4j.cloud.api.user.constant.ScoreActivityEnum;
import com.mall4j.cloud.api.user.dto.UpdateScoreDTO;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.api.user.vo.ScoreConvertVO;
import com.mall4j.cloud.api.user.vo.SoldScoreCouponLogVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.user.constant.scoreConvert.*;
import com.mall4j.cloud.user.dto.scoreConvert.*;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.*;
import com.mall4j.cloud.user.model.scoreConvert.*;
import com.mall4j.cloud.user.service.ScoreTimeDiscountActivityService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBannerService;
import com.mall4j.cloud.user.service.scoreConvert.ScoreCouponService;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountVO;
import com.mall4j.cloud.user.vo.scoreConvert.*;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 积分换券
 *
 * @author shijing
 * @date 2021-12-10 18:07:21
 */

@Slf4j
@Service
@Transactional
public class ScoreCouponServiceImpl extends ServiceImpl<ScoreConvertMapper,ScoreConvert> implements ScoreCouponService {

    @Resource
    private ScoreCouponLogMapper scoreCouponLogMapper;

    @Resource
    private ScoreConvertMapper scoreConvertMapper;

    @Resource
    private ScoreConvertShopMapper shopMapper;

    @Resource
    private ScoreConvertPhoneMapper scoreConvertPhoneMapper;

    @Resource
    private ScoreBannerService scoreBannerService;

    @Resource
    private ScoreConvertCouponMapper scoreConvertCouponMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Resource
    private CrmUserFeignClient crmUserFeignClient;

    @Resource
    private UserScoreCouponNoticeMapper userScoreCouponNoticeMapper;

    @Resource
    private UserScoreCouponOverdueNoticeMapper userScoreCouponOverdueNoticeMapper;
    @Autowired
    private ScoreTimeDiscountActivityService discountActivityService;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;

//    @Resource
//    private OnsMQTemplate sendSyncPointConvertDataTemplate;
//    @Value("${score.clearing.convertId}")
    private Integer convertId;

    @Override
    public ServerResponseEntity<PageInfo<ScoreCouponListVO>> list(ScoreConvertListDTO param) {
        log.info("换券列表的搜索条件为 :{}", param);
        PageInfo<ScoreCouponListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                scoreConvertMapper.scoreCouponList(param)
        );

        //处理返回值
        List<ScoreCouponListVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(temp -> {
                //活动状态
                if(temp.getConvertStatus().equals(ScoreActivityStatusEnum.DISABLE.value())){
                    temp.setActivityStatus(ScoreActivityStatusEnum.DISABLE.desc());
                    temp.setActivityStatusKey(ScoreActivityStatusEnum.DISABLE.value());
                }
                if(temp.getConvertStatus().equals(ScoreActivityStatusEnum.ENABLE.value())){
                    if (System.currentTimeMillis() < temp.getStartTime().getTime()) {
                        temp.setActivityStatus(ScoreActivityStatusEnum.NOT_START.desc());
                        temp.setActivityStatusKey(ScoreActivityStatusEnum.NOT_START.value());
                    } else if (System.currentTimeMillis() > temp.getStartTime().getTime() && System.currentTimeMillis() < temp.getEndTime().getTime()) {
                        temp.setActivityStatus(ScoreActivityStatusEnum.START.desc());
                        temp.setActivityStatusKey(ScoreActivityStatusEnum.START.value());
                    } else if (System.currentTimeMillis() > temp.getEndTime().getTime()) {
                        temp.setActivityStatus(ScoreActivityStatusEnum.OVER.desc());
                        temp.setActivityStatusKey(ScoreActivityStatusEnum.OVER.value());
                    }
                }
                //适用门店数量
                if (!temp.getIsAllShop()) {
                    Integer shopNum = shopNum(temp.getId(),ScoreShopTypeEnum.APPLY.value().shortValue());
                    temp.setShopNum(shopNum);
                }
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> save(ScoreCouponSaveDTO param) {
        ScoreConvert scoreConvert = BeanUtil.copyProperties(param, ScoreConvert.class);
        scoreConvert.setConvertType(ScoreConvertTypeEnum.COUPON.value().shortValue());
        scoreConvert.setStocks(param.getMaxAmount());
        scoreConvert.setCreateId(AuthUserContext.get().getUserId());
        scoreConvert.setCreateName(AuthUserContext.get().getUsername());
        scoreConvert.setUpdateId(AuthUserContext.get().getUserId());
        scoreConvert.setUpdateName(AuthUserContext.get().getUsername());

        if (param.getNewRemind()!= null ) {
            LocalDateTime localDateTime = param.getStartTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime().plusHours(param.getNewRemind() * 1);
            scoreConvert.setNewRemindTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }

        scoreConvertMapper.insert(scoreConvert);

        //组装活动关联门店信息
        List<Long> shops = param.getShops();
        if (!CollectionUtils.isEmpty(shops)) {
            addShops(shops, scoreConvert.getConvertId(),ScoreShopTypeEnum.APPLY.value().shortValue());
        }

        //组装优惠券关联门店信息
        List<Long> couponShops = param.getCouponShops();
        if (!CollectionUtils.isEmpty(couponShops)) {
            addShops(couponShops, scoreConvert.getConvertId(),ScoreShopTypeEnum.COUPON.value().shortValue());
        }

        //组装优惠券集合信息
        List<Long> couponIds = param.getCouponIds();
        if (!CollectionUtils.isEmpty(couponIds)) {
            addCoupons(couponIds, scoreConvert.getConvertId());
        }
        //组装禁用人员信息
        addPhoneNum(param.getPhoneNums(), scoreConvert.getConvertId());
        //活动启用且生效，调用上新通知
        if (param.getConvertStatus() == 0 && System.currentTimeMillis() > param.getStartTime().getTime() && System.currentTimeMillis() < param.getEndTime().getTime()) {
            List<UserScoreCouponNotice> userScoreCouponNotices = userScoreCouponNoticeMapper.selectList(new LambdaQueryWrapper<UserScoreCouponNotice>().eq(UserScoreCouponNotice::getNoticeSwitch, 0));
            // todo 调用推送
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(ScoreCouponUpdateDTO param) {
        //修改基本信息
        ScoreConvert scoreConvert = BeanUtil.copyProperties(param, ScoreConvert.class);
        scoreConvert.setUpdateId(AuthUserContext.get().getUserId());
        scoreConvert.setUpdateName(AuthUserContext.get().getUsername());
        scoreConvert.setUpdateTime(new Date());

        if (param.getNewRemind()!= null ) {
            LocalDateTime localDateTime = param.getStartTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime().plusHours(param.getNewRemind() * 1);
            scoreConvert.setNewRemindTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
        scoreConvertMapper.updateById(scoreConvert);

        //删除原有活动门店信息
        shopMapper.delete(new LambdaUpdateWrapper<ScoreConvertShop>()
                .eq(ScoreConvertShop::getConvertId, param.getConvertId())
                .eq(ScoreConvertShop::getType,ScoreShopTypeEnum.APPLY.value()));
        //新增关联门店信息
        List<Long> shops = param.getShops();
        if (!CollectionUtils.isEmpty(shops)) {
            addShops(shops, scoreConvert.getConvertId(),ScoreShopTypeEnum.APPLY.value().shortValue());
        }

        //删除原有优惠券门店信息
        shopMapper.delete(new LambdaUpdateWrapper<ScoreConvertShop>()
                .eq(ScoreConvertShop::getConvertId, param.getConvertId())
                .eq(ScoreConvertShop::getType,ScoreShopTypeEnum.COUPON.value()));
        //组装优惠券关联门店信息
        List<Long> couponShops = param.getCouponShops();
        if (!CollectionUtils.isEmpty(couponShops)) {
            addShops(couponShops, scoreConvert.getConvertId(),ScoreShopTypeEnum.COUPON.value().shortValue());
        }

        //删除原有优惠券信息
        scoreConvertCouponMapper.delete(new LambdaUpdateWrapper<ScoreConvertCoupon>().eq(ScoreConvertCoupon::getConvertId, param.getConvertId()));
        //新增关联优惠券信息
        List<Long> couponIds = param.getCouponIds();
        if (!CollectionUtils.isEmpty(couponIds)) {
            addCoupons(couponIds, scoreConvert.getConvertId());
        }

        //删除禁用人员信息
        scoreConvertPhoneMapper.delete(new LambdaUpdateWrapper<ScoreConvertPhone>().eq(ScoreConvertPhone::getConvertId, param.getConvertId()));
        //组装禁用人员信息
        addPhoneNum(param.getPhoneNums(), scoreConvert.getConvertId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<ScoreCouponVO> selectDetail(Long convertId) {
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(convertId);
        ScoreCouponVO result = BeanUtil.copyProperties(scoreConvert, ScoreCouponVO.class);

        if (!result.getIsAllShop()) {
            //适用门店数量
            Integer shopNum = shopNum(result.getConvertId(),ScoreShopTypeEnum.APPLY.value().shortValue());
            result.setShopNum(shopNum);
            //适用门店
            List<Long> shops = shops(convertId,ScoreShopTypeEnum.APPLY.value().shortValue());
            result.setShops(shops);
        }

        if (!result.getIsAllCouponShop()) {
            //优惠券适用门店数量
            Integer couponShopNum = shopNum(result.getConvertId(),ScoreShopTypeEnum.COUPON.value().shortValue());
            result.setCouponShopNum(couponShopNum);
            //优惠券适用门店
            List<Long> couponShops = shops(convertId,ScoreShopTypeEnum.COUPON.value().shortValue());
            result.setCouponShops(couponShops);
        }

        //关联优惠券
        List<Long> coupons = coupons(convertId);
        result.setCouponIds(coupons);

        List<ScoreConvertPhone> scoreConvertPhones = scoreConvertPhoneMapper.selectList(new LambdaQueryWrapper<ScoreConvertPhone>().eq(ScoreConvertPhone::getConvertId, convertId));
        List<String> phoneList = scoreConvertPhones.stream().map(ScoreConvertPhone::getPhoneNum).collect(Collectors.toList());
        result.setPhoneNums(phoneList);
        result.setPhoneNumSum(scoreConvertPhones.size());

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<List<String>> importPhoneNum(MultipartFile file) {
        log.info("导入文件为：{}", file);
        if (file == null) {
            throw new LuckException("导入文件不能为空！");
        }

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
        excelReader.addHeaderAlias("手机号", "phoneNum");

        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
        List<ScoreConvertPhone> importObjects = excelReader.readAll(ScoreConvertPhone.class);
        List<String> result = importObjects.stream().map(ScoreConvertPhone::getPhoneNum).collect(Collectors.toList());

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<PageInfo<String>> phoneNumList(PhoneNumListDTO param) {
        PageInfo<String> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                scoreConvertPhoneMapper.list(param.getConvertId(), param.getPhoneNum())
        );
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> deletePhone(Long id, String phoneNum) {
        scoreConvertPhoneMapper.delete(new LambdaUpdateWrapper<ScoreConvertPhone>()
                .eq(ScoreConvertPhone::getConvertId,id)
                .eq(ScoreConvertPhone::getPhoneNum,phoneNum));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteBatchPhone(Long id, List<String> phoneNums) {
        scoreConvertPhoneMapper.deleteBatch(id,phoneNums);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<ScoreCouponLogVO>> logList(ScoreBarterLogListDTO param) {
        PageInfo<ScoreCouponLogVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                scoreCouponLogMapper.logList(param)
        );
        if (!CollectionUtils.isEmpty(result.getList())) {
            result.getList().forEach(temp -> {
                if (!temp.getType().equals(2)) {
                    List<ScoreConvertCoupon> list = scoreConvertCouponMapper.selectList(new LambdaQueryWrapper<ScoreConvertCoupon>().eq(ScoreConvertCoupon::getConvertId, temp.getConvertId()));
                    if (!CollectionUtils.isEmpty(list)) {
                        List<Long> couponIds = list.stream().map(ScoreConvertCoupon::getCouponId).collect(Collectors.toList());
                        temp.setCouponIds(couponIds);
                    }
                }
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> addLogistics(@RequestBody ScoreLogDTO param){
        ScoreCouponLog scoreCouponLog = BeanUtil.copyProperties(param, ScoreCouponLog.class);
        scoreCouponLog.setLogId(param.getId());
        scoreCouponLog.setExportStatus(0);
        scoreCouponLog.setStatus(1);
        scoreCouponLogMapper.updateById(scoreCouponLog);

//        SendResult sendResult = sendSyncPointConvertDataTemplate.syncSend(scoreCouponLog.getLogId(), 2);
//        log.info("添加物流信息同步crm,logId:{},msgId:{}",scoreCouponLog.getLogId(),sendResult.getMessageId());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> confirmExprot(List<Long> ids) {
        if(CollectionUtil.isNotEmpty(ids)) {
        scoreCouponLogMapper.confirmExprot(ids);

//            ids.forEach(id -> {
//                SendResult sendResult = sendSyncPointConvertDataTemplate.syncSend(id,2);
//                log.info("添加物流信息同步crm,logId:{},msgId:{}", id, sendResult.getMessageId());
//            });
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Map<String,Integer>> importLog(MultipartFile file) {
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
        excelReader.addHeaderAlias("兑换记录ID","logId");

        excelReader.addHeaderAlias("物流信息","logisticsNo");
        excelReader.addHeaderAlias("物流公司名称","company");

        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
        List<ScoreCouponLog> importObjects = excelReader.readAll(ScoreCouponLog.class);
        Integer succ = 0;
        Integer total = 0;
        if (importObjects != null) {
            total = importObjects.size();
            for (int i = 0; i < importObjects.size(); i++) {
                ScoreCouponLog l = importObjects.get(i);
                l.setExportStatus(0);
                if (l.getLogId() != null && StrUtil.isNotBlank(l.getCompany()) && StrUtil.isNotBlank(l.getLogisticsNo())) {
                    ScoreCouponLog scoreCouponLog = scoreCouponLogMapper.selectById(l.getLogId());
                    if (scoreCouponLog != null && scoreCouponLog.getStatus().equals(1)) {
                        int flag = scoreCouponLogMapper.updateById(l);
                        if (flag == 1) {
                            succ++;
                        }

//                        SendResult sendResult = sendSyncPointConvertDataTemplate.syncSend(scoreCouponLog.getLogId(), 2);
//                        log.info("导入信息同步crm,logId:{},msgId:{}", scoreCouponLog.getLogId(), sendResult.getMessageId());
                    }
                }
            }
        }

        Map<String,Integer> res = new HashMap<>();
        res.put("total",total);
        res.put("succ",succ);
        return ServerResponseEntity.success(res);
    }

    @Async
    @Override
    public void export(ScoreBarterLogListDTO param, HttpServletResponse response) {
        //
//        ScoreConvert scoreConvert = scoreConvertMapper.selectById(param.getConvertId());

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(param.getDownLoadHisId());
        String fileName="积分兑换活动ID"+param.getConvertId()+"-"+SoldScoreCouponLogVO.EXCEL_NAME;
        finishDownLoadDTO.setFileName(fileName);

        // 一次性写出内容，使用默认样式，强制输出标题
        List<ScoreCouponLogVO> result = scoreCouponLogMapper.logList(param);
        if(CollUtil.isEmpty(result)) {
            finishDownLoadDTO.setRemarks("无兑换数据导出");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("无兑换数据导出");
            return;
        }

        List<SoldScoreCouponLogVO> resultData = new ArrayList<>();
        result.forEach( res -> {
            SoldScoreCouponLogVO logVO = BeanUtil.copyProperties(res, SoldScoreCouponLogVO.class);
            if (res.getStatus() != null) {
                if (res.getStatus().equals(1)) {
                    logVO.setStatus("待发货");
                }
                if (res.getStatus().equals(2)) {
                    logVO.setStatus("已发货");
                }
            }
            if (res.getType() != null) {
                if (res.getCouponIds() != null && res.getCouponIds().size() > 0) {
                    logVO.setPrizeName(res.getCouponIds().get(0).toString());
                }
                if (res.getType() == 0) {
                    logVO.setType("积分兑礼");
                }
                if (res.getType() == 1) {
                    logVO.setType("积分换券");
                }
                if (res.getType() == 2) {
                    logVO.setType("兑礼到家");
                    logVO.setPrizeName(res.getCommodityName());
                }
            }
            if (StrUtil.isNotBlank(res.getUserAddr()) && StrUtil.isNotBlank(res.getPhone()) && StrUtil.isNotBlank(res.getUserName())) {
                logVO.setAddr(res.getUserName()+","+res.getPhone()+","+res.getUserAddr());
            }
            resultData.add(logVO);
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
                    finishDownLoadDTO.setCalCount(result.size());
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

    private String createExcelFile(List<SoldScoreCouponLogVO> userExcelVOList, String  fileName){
        String file=null;
        try {
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String pathExport= SystemUtils.getExcelFilePath()+"/"+fileName+".xls";
//            String pathExport= SkqUtils.getExcelFilePath()+"/"+SkqUtils.getExcelName()+".xls";
            EasyExcel.write(pathExport, SoldScoreCouponLogVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(userExcelVOList);
            return pathExport;//返回文件生成路径
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }
//    @Override
//    public void export(ScoreBarterLogListDTO param, HttpServletResponse response) {
//        log.info("==================== 导出文件开始 ============================");
//        // 通过工具类创建writer
//        ExcelWriter writer = ExcelUtil.getWriter();
//        writer.setSheet(0);
//        //参数为true时只导出有别名的
//        writer.setOnlyAlias(true);
//        Workbook workbook = writer.getWorkbook();
//        StyleSet styleSet = writer.getStyleSet();
//        DataFormat dataFormat = workbook.createDataFormat();
//        CellStyle cellStyleForDate = styleSet.getCellStyleForDate();
//        cellStyleForDate.setDataFormat(dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));
//
//        //自定义标题别名
//        //列名和对象属性名一致
//        writer.addHeaderAlias("id", "序号");
//        writer.addHeaderAlias("userCardNumber", "用户卡号");
//        writer.addHeaderAlias("userPhone", "会员手机号");
//        writer.addHeaderAlias("convertScore", "兑换积分数");
//        writer.addHeaderAlias("description", "兑换时间");
//        writer.addHeaderAlias("note", "备注");
//
//        // 一次性写出内容，使用默认样式，强制输出标题
//        List<ScoreCouponLogVO> result = scoreCouponLogMapper.logList(param);
//
//        writer.write(result, true);
//
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String timeNow = sdf.format(date);
//        String fileName = "兑换记录_" + timeNow + ".xls";
//        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//        //写出倒流
//        OutputStream out = null;
//        try {
//            out = response.getOutputStream();
//        } catch (IOException e) {
//            log.error("获取输出流异常");
//            e.printStackTrace();
//        }
//        writer.flush(out);
//        // 关闭writer，释放内存
//        writer.close();
//        IoUtil.close(out);
//    }

    @Override
    public ServerResponseEntity<ScoreMallHomeVO> listForApp(Long shopId) {
        ScoreMallHomeVO result = new ScoreMallHomeVO();
        //banner信息
        ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerUrls = scoreBannerService.bannerListForApp();
        result.setBannerList(bannerUrls.getData());
        //积分换券信息
        List<ScoreCouponAppVO> scoreCouponAppVOS = scoreConvertMapper.listForApp(shopId);

        //处理限时折扣积分逻辑
        if(CollectionUtil.isNotEmpty(scoreCouponAppVOS) && scoreCouponAppVOS.size()>0){
            List<Long> converIds = scoreCouponAppVOS.stream().map(ScoreCouponAppVO::getId).collect(Collectors.toList());
            List<ScoreTimeDiscountVO> scoreTimeDiscountVOS=discountActivityService.getConvertScoreCoupons(converIds);
            if(CollectionUtil.isNotEmpty(scoreTimeDiscountVOS) && scoreTimeDiscountVOS.size()>0){
                Map<Long,ScoreTimeDiscountVO> discountVOMaps=scoreTimeDiscountVOS.stream().collect(Collectors.toMap(ScoreTimeDiscountVO::getConvertId,scoreTimeDiscountVO -> scoreTimeDiscountVO));

                scoreCouponAppVOS.forEach(scoreCouponAppVO -> {
                    if(discountVOMaps.containsKey(scoreCouponAppVO.getId())){
                        Double discount=Arith.div((double) discountVOMaps.get(scoreCouponAppVO.getId()).getDiscount(),100,2);
                        Double discountScore= Arith.mul((double) scoreCouponAppVO.getConvertScore(),discount);
                        scoreCouponAppVO.setDiscountScore(Arith.ceil(discountScore).longValue());
                        scoreCouponAppVO.setScoreDiscount(discountVOMaps.get(scoreCouponAppVO.getId()).getDiscount());
                        scoreCouponAppVO.setScoreTimeDisActivityId(discountVOMaps.get(scoreCouponAppVO.getId()).getActivityId());
                    }
                });
            }
        }

        result.setCouponList(scoreCouponAppVOS);

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<List<ScoreBannerUrlDTO>> bannerData(Long shopId,Integer type) {
        //banner信息
       return scoreBannerService.bannerData(shopId,type);
    }

    @Override
    public ServerResponseEntity<ScoreCouponAppDetailVO> detailForApp(Long id) {
        //查询活动信息
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(id);
        ScoreCouponAppDetailVO result = BeanUtil.copyProperties(scoreConvert, ScoreCouponAppDetailVO.class);
        result.setId(scoreConvert.getConvertId());

        //是否支持兑礼到店
        result.setIsToShop(1);
        if (scoreConvert.getType().intValue() == ConvertTypeEnum.TO_SHOP.getType()){
            result.setIsToShop(ConvertTypeEnum.TO_SHOP.getType());
        }
        //查询门店列表
        if (!result.getIsAllShop()) {
            //适用门店数量
            Integer shopNum = shopNum(result.getId(),ScoreShopTypeEnum.APPLY.value().shortValue());
            result.setShopNum(shopNum);
            //适用门店
            List<Long> shops = shops(result.getId(),ScoreShopTypeEnum.APPLY.value().shortValue());
            result.setShops(shops);
        }

        //处理限时折扣积分逻辑
        List<Long> converIds = new ArrayList<>();
        converIds.add(scoreConvert.getConvertId());
        List<ScoreTimeDiscountVO> scoreTimeDiscountVOS=discountActivityService.getConvertScoreCoupons(converIds);
        if(CollectionUtil.isNotEmpty(scoreTimeDiscountVOS) && scoreTimeDiscountVOS.size()>0){
            ScoreTimeDiscountVO scoreTimeDiscountVO=scoreTimeDiscountVOS.get(0);
            Double discount=Arith.div((double) scoreTimeDiscountVO.getDiscount(),100,2);
            Double discountScore= Arith.mul((double) result.getConvertScore(),discount);
            result.setDiscountScore(Arith.ceil(discountScore).longValue());
            result.setScoreDiscount(scoreTimeDiscountVO.getDiscount());
            result.setScoreTimeDisActivityId(scoreTimeDiscountVO.getActivityId());
        }

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Boolean> activityFlag(Long id,Long storeId) {
        //校验活动状态（是否存在，活动时间，状态，库存，每月每人限制领取，禁用人员）
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(id);
        if (ObjectUtil.isEmpty(scoreConvert)) {
            throw new LuckException("礼品已兑罄，请您兑换其他礼品。");
        }
        if (scoreConvert.getConvertStatus().equals(ScoreActivityStatusEnum.DISABLE.value())) {
            throw new LuckException("礼品已兑罄，请您兑换其他礼品。");
        }
        if (System.currentTimeMillis() < scoreConvert.getStartTime().getTime()) {
            throw new LuckException("本礼品兑换尚未开始，请在指定时间内兑换。");
        }
        if (System.currentTimeMillis() > scoreConvert.getEndTime().getTime()) {
            throw new LuckException("本礼品兑换已结束，请您兑换其他礼品。");
        }
        if (scoreConvert.getMaxAmount() != 0 && scoreConvert.getStocks() < 1) {
            throw new LuckException("本礼品已告罄，请您兑换其他礼品。");
        }
        if (!scoreConvert.getIsAllShop()) {
            ServerResponseEntity<List<Long>> shops = getShops(scoreConvert.getConvertId());
            if (shops != null && shops.isSuccess() && shops.getData() != null && shops.getData().size() > 0) {
                if (! shops.getData().contains(storeId)) {
                    throw new LuckException("当前访问门店不在活动范围内！");
                }
            }
        }
        //校验每月每人限制领取
        Long personMaxAmount = scoreConvert.getPersonMaxAmount();
        if (personMaxAmount != 0) {
            // 获取前月的第一天
            String firstDay = DateUtil.beginOfMonth(new Date()).toString();
            // 获取前月的最后一天
            String lastDay = DateUtil.endOfMonth(new Date()).toString();
            Integer sum = scoreCouponLogMapper.selectCount(new LambdaQueryWrapper<ScoreCouponLog>()
                    .between(ScoreCouponLog::getCreateTime, firstDay, lastDay)
                    .eq(ScoreCouponLog::getConvertId,id)
                    .eq(ScoreCouponLog::getUserId, AuthUserContext.get().getUserId()));
            if (!(sum < personMaxAmount)) {
                throw new LuckException("兑礼次数超出每月限制");
            }
        }
        //查询当前用户信息
        clearUserPermissionsCache(AuthUserContext.get().getUserId());
        UserApiVO user = userService.getByUserId(AuthUserContext.get().getUserId());
        if (ObjectUtil.isEmpty(user)) {
            throw new LuckException("请您先注册成为会员");
        }

        String fanLevels = scoreConvert.getFanLevels();
        if (StringUtils.isNotEmpty(fanLevels)) {
            List<String> strings = Arrays.asList(fanLevels.split(StringPool.COMMA));
            List<Integer> userLevelList = Convert.toList(Integer.class, strings);
            if (!userLevelList.contains(user.getLevel())) {
                if (StrUtil.isNotBlank(scoreConvert.getFanTips())) {
                    throw new LuckException(scoreConvert.getFanTips());
                } else {
                    throw new LuckException("未达到参与活动的条件");
                }
            }
        }

        Integer orderSwitch = scoreConvert.getOrderSwitch();
        Long userId = user.getUserId();
        // 判断用户订单消费金额是否满足条件
        if (orderSwitch == 1) {
            Long orderNum = scoreConvert.getOrderNum();
            if (orderNum != null && orderNum >= 0) {
                orderNum = orderNum * 100;
                String startTime = DateUtil.formatDateTime(scoreConvert.getOrderStartTime());
                String endTime = DateUtil.formatDateTime(scoreConvert.getOrderEndTime());
                String orderType = scoreConvert.getOrderType();
                log.info("判断用户消金额查询条件:{},{},{},{}",userId,startTime,endTime,orderType);
            }
        }
        Long convertScore=scoreConvert.getConvertScore();//实际兑换积分数分
        //处理限时折扣积分逻辑
        List<Long> converIds = new ArrayList<>();
        converIds.add(scoreConvert.getConvertId());

        //获取活动的禁用人员手机号
        ScoreConvertPhone phone = scoreConvertPhoneMapper.selectOne(new LambdaQueryWrapper<ScoreConvertPhone>()
                .eq(ScoreConvertPhone::getConvertId, id)
                .eq(ScoreConvertPhone::getPhoneNum, user.getPhone()));
        if (ObjectUtil.isNotEmpty(phone)) {
            throw new LuckException("内部渠道人员无法兑换积分礼品。");
        }
        return ServerResponseEntity.success(true);
    }

    @Override
    public ServerResponseEntity<Void>
    userConvert(Long id,Long storeId,String userAddr,String userPhone,
                String userName) {
        log.info("会员积分兑换入参,活动Id:【{}】,门店Id:【{}】,地址:【{}】,手机号:【{}】,用户名:【{}】",id ,storeId ,userAddr ,userPhone ,userName);
        //校验活动状态（是否存在，活动时间，状态，库存，每月每人限制领取，禁用人员）
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(id);
        log.info("积分兑换数据:{}",JSONObject.toJSONString(scoreConvert));
        if (ObjectUtil.isEmpty(scoreConvert)) {
            throw new LuckException("礼品已兑罄，请您兑换其他礼品。");
        }
        if (scoreConvert.getConvertStatus().equals(ScoreActivityStatusEnum.DISABLE.value())) {
            throw new LuckException("礼品已兑罄，请您兑换其他礼品。");
        }
        if (System.currentTimeMillis() < scoreConvert.getStartTime().getTime()) {
            throw new LuckException("本礼品兑换尚未开始，请在指定时间内兑换。");
        }
        if (System.currentTimeMillis() > scoreConvert.getEndTime().getTime()) {
            throw new LuckException("本礼品兑换已结束，请您兑换其他礼品。");
        }
        if (scoreConvert.getMaxAmount() != 0 && scoreConvert.getStocks() < 1) {
            throw new LuckException("本礼品已告罄，请您兑换其他礼品。");
        }
        if (!scoreConvert.getIsAllShop()) {
            ServerResponseEntity<List<Long>> shops = getShops(scoreConvert.getConvertId());
            if (shops != null && shops.isSuccess() && shops.getData() != null && shops.getData().size() > 0) {
                if (! shops.getData().contains(storeId)) {
                    throw new LuckException("当前访问门店不在活动范围内！");
                }
            }
        }
        if (scoreConvert.getType() == 2) {
            if (StrUtil.isBlank(userAddr) || StrUtil.isBlank(userPhone) || StrUtil.isBlank(userName)) {
                throw new LuckException("当前兑奖类型需要填写收货信息！");
            }
        }

        //校验每月每人限制领取
        Long personMaxAmount = scoreConvert.getPersonMaxAmount();
        if (personMaxAmount != 0) {
            // 获取前月的第一天
            String firstDay = DateUtil.beginOfMonth(new Date()).toString();
            // 获取前月的最后一天
            String lastDay = DateUtil.endOfMonth(new Date()).toString();
            Integer sum = scoreCouponLogMapper.selectCount(new LambdaQueryWrapper<ScoreCouponLog>()
                    .between(ScoreCouponLog::getCreateTime, firstDay, lastDay)
                    .eq(ScoreCouponLog::getConvertId,id)
                    .eq(ScoreCouponLog::getUserId, AuthUserContext.get().getUserId()));
            if (!(sum < personMaxAmount)) {
                throw new LuckException("兑礼次数超出每月限制");
            }
        }
        //查询当前用户信息
        clearUserPermissionsCache(AuthUserContext.get().getUserId());
        UserApiVO user = userService.getByUserId(AuthUserContext.get().getUserId());
        if (ObjectUtil.isEmpty(user)) {
            throw new LuckException("请您先注册成为会员");
        }

        Integer orderSwitch = scoreConvert.getOrderSwitch();

        Long convertScore=scoreConvert.getConvertScore();//实际兑换积分数
        //crm变动积分
        Integer point_value=-scoreConvert.getConvertScore().intValue();//扣除积分
        Integer reply_point_value=scoreConvert.getConvertScore().intValue();//恢复积分
        Long discountScorePoint=null;//折扣后积分
        Long scoreActivityId=null;//积分折扣活动
        Integer scoreActivitySrc=null;//积分折扣活动来源
        //处理限时折扣积分逻辑
        List<Long> converIds = new ArrayList<>();
        converIds.add(scoreConvert.getConvertId());
        List<ScoreTimeDiscountVO> scoreTimeDiscountVOS=discountActivityService.getConvertScoreCoupons(converIds);
        if(CollectionUtil.isNotEmpty(scoreTimeDiscountVOS) && scoreTimeDiscountVOS.size()>0){
            ScoreTimeDiscountVO scoreTimeDiscountVO=scoreTimeDiscountVOS.get(0);
            scoreActivityId=scoreTimeDiscountVO.getActivityId();
            Double discount=Arith.div((double) scoreTimeDiscountVO.getDiscount(),100,2);
            Double discountScore1= Arith.mul((double) scoreConvert.getConvertScore(),discount);
            Long discountScore=Arith.ceil(discountScore1).longValue();
            log.info("用户领取积分券 匹配到积分限时折扣1:{}  折扣后积分:{} 原始兑换券积分:{}", JSON.toJSONString(scoreTimeDiscountVO),discountScore,convertScore);
            point_value=-discountScore.intValue();
            reply_point_value=discountScore.intValue();
            convertScore=discountScore.longValue();
            discountScorePoint=discountScore.longValue();
            scoreActivitySrc= ScoreActivityEnum.ACTIVITY_TIME_SCORE.getCode();
            log.info("用户领取积分券 匹配到积分限时折扣2: discount:{} discountScore:{} point_value:{} reply_point_value:{} convertScore:{} discountScorePoint:{}",
                    discount,discountScore,point_value,reply_point_value,convertScore,discountScorePoint);
        }

        //获取活动的禁用人员手机号
        ScoreConvertPhone phone = scoreConvertPhoneMapper.selectOne(new LambdaQueryWrapper<ScoreConvertPhone>()
                .eq(ScoreConvertPhone::getConvertId, id)
                .eq(ScoreConvertPhone::getPhoneNum, user.getPhone()));
        if (ObjectUtil.isNotEmpty(phone)) {
            throw new LuckException("内部渠道人员无法兑换积分礼品。");
        }

        //扣减库存
        if (scoreConvert.getMaxAmount() != 0){
            if (scoreConvertMapper.updateStocks(scoreConvert.getConvertId(),scoreConvert.getVersion()) < 1) {
                throw new LuckException("积分兑换失败，请重新提交");
            }
        }
        String storeName = null;
        StoreVO byStoreId = storeFeignClient.findByStoreId(storeId);
        if (byStoreId != null) {
            storeName = byStoreId.getName();
        }

        //CRM调用查询积分，扣减积分
        UpdateScoreDTO updateScoreDTO = new UpdateScoreDTO();
        updateScoreDTO.setUserId(AuthUserContext.get().getUserId());
        updateScoreDTO.setPoint_value(point_value);
        updateScoreDTO.setSource(ScoreSourceEnum.SCORE_COUPON.desc());
        updateScoreDTO.setPoint_channel("wechat");
        updateScoreDTO.setPoint_type("SKX_JFDH");
        updateScoreDTO.setRemark("积分换券扣减积分");
        updateScoreDTO.setIoType(0);
//        crmUserFeignClient.updateScore(updateScoreDTO);
        //记录活动兑换信息
        ScoreCouponLog scoreCouponLog = new ScoreCouponLog();
        scoreCouponLog.setConvertId(id);
        scoreCouponLog.setType(scoreConvert.getType());
        scoreCouponLog.setConvertScore(convertScore);//兑换积分数
        scoreCouponLog.setOriginalConvertScore(scoreConvert.getConvertScore());//原始兑换积分数
        scoreCouponLog.setDiscountConvertScore(discountScorePoint);//折扣兑换积分数
        scoreCouponLog.setScoreActivityId(scoreActivityId);//积分活动id
        scoreCouponLog.setScoreActivitySrc(scoreActivitySrc);//积分活动来源
        scoreCouponLog.setUserId(AuthUserContext.get().getUserId());
        scoreCouponLog.setUserCardNumber(user.getVipcode());
        scoreCouponLog.setUserPhone(user.getPhone());
        scoreCouponLog.setCreateUserId(AuthUserContext.get().getUserId());
        if (StrUtil.isNotBlank(storeName)) {
            scoreCouponLog.setStoreName(storeName);
        }
        if (scoreConvert.getType() == 2) {
            scoreCouponLog.setStatus(1);
            scoreCouponLog.setUserAddr(userAddr);
            scoreCouponLog.setPhone(userPhone);
            scoreCouponLog.setUserName(userName);
        } else {
            scoreCouponLog.setStatus(2);
        }
        log.info("积分兑换礼品记录:{}",JSONObject.toJSONString(scoreCouponLog));
        scoreCouponLogMapper.insert(scoreCouponLog);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<UserCouponLogVO>> userConvertLog(Integer pageNo, Integer pageSize, Short type) {
        PageInfo<UserCouponLogVO> result = PageHelper.startPage(pageNo, pageSize).doSelectPageInfo(() ->
                scoreCouponLogMapper.appLogList(AuthUserContext.get().getUserId(),type)
        );
        if (!CollectionUtils.isEmpty(result.getList())) {
            result.getList().forEach(temp -> {
                if (!temp.getType().equals(2)) {
                    List<ScoreConvertCoupon> list = scoreConvertCouponMapper.selectList(new LambdaQueryWrapper<ScoreConvertCoupon>().eq(ScoreConvertCoupon::getConvertId, temp.getConvertId()));
                    if (!CollectionUtils.isEmpty(list)) {
                        List<Long> couponIds = list.stream().map(ScoreConvertCoupon::getCouponId).collect(Collectors.toList());
                        temp.setCouponIds(couponIds);
                    }
                }
            });
        }

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> enableNotice(String formId) {
        UserScoreCouponNotice userScoreCouponNotice = userScoreCouponNoticeMapper.selectOne(new LambdaQueryWrapper<UserScoreCouponNotice>().eq(UserScoreCouponNotice::getUserId, AuthUserContext.get().getUserId()));
        if (ObjectUtil.isEmpty(userScoreCouponNotice)) {
            UserScoreCouponNotice insertParam = new UserScoreCouponNotice();
            insertParam.setUserId(AuthUserContext.get().getUserId());
            insertParam.setNoticeSwitch(0);
            insertParam.setFormId(formId);
            userScoreCouponNoticeMapper.insert(insertParam);
        } else {
            if (userScoreCouponNotice.getNoticeSwitch() == 1) {
                userScoreCouponNotice.setNoticeSwitch(0);
                userScoreCouponNoticeMapper.updateById(userScoreCouponNotice);
            }
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disableNotice(String formId) {
        UserScoreCouponNotice userScoreCouponNotice = userScoreCouponNoticeMapper.selectOne(new LambdaQueryWrapper<UserScoreCouponNotice>().eq(UserScoreCouponNotice::getUserId, AuthUserContext.get().getUserId()));
        if (ObjectUtil.isEmpty(userScoreCouponNotice)) {
            UserScoreCouponNotice insertParam = new UserScoreCouponNotice();
            insertParam.setUserId(AuthUserContext.get().getUserId());
            insertParam.setNoticeSwitch(1);
            insertParam.setFormId(formId);
            userScoreCouponNoticeMapper.insert(insertParam);
        } else {
            if (userScoreCouponNotice.getNoticeSwitch() == 0) {
                userScoreCouponNotice.setNoticeSwitch(1);
                userScoreCouponNoticeMapper.updateById(userScoreCouponNotice);
            }
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> enableCouponNotice(String formId, Long activityId) {
        UserScoreCouponOverdueNotice notice = userScoreCouponOverdueNoticeMapper.selectOne(new LambdaQueryWrapper<UserScoreCouponOverdueNotice>()
                .eq(UserScoreCouponOverdueNotice::getUserId, AuthUserContext.get().getUserId())
                .eq(UserScoreCouponOverdueNotice::getActivityId, activityId));
        if (ObjectUtil.isEmpty(notice)) {
            UserScoreCouponOverdueNotice insertParam = new UserScoreCouponOverdueNotice();
            insertParam.setUserId(AuthUserContext.get().getUserId());
            insertParam.setActivityId(activityId);
            insertParam.setNoticeSwitch(0);
            insertParam.setFormId(formId);
            userScoreCouponOverdueNoticeMapper.insert(insertParam);
        } else {
            if (notice.getNoticeSwitch() == 1) {
                notice.setNoticeSwitch(0);
                userScoreCouponOverdueNoticeMapper.updateById(notice);
            }
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disableCouponNotice(String formId, Long activityId) {
        UserScoreCouponOverdueNotice notice = userScoreCouponOverdueNoticeMapper.selectOne(new LambdaQueryWrapper<UserScoreCouponOverdueNotice>()
                .eq(UserScoreCouponOverdueNotice::getUserId, AuthUserContext.get().getUserId())
                .eq(UserScoreCouponOverdueNotice::getActivityId, activityId));
        if (ObjectUtil.isEmpty(notice)) {
            UserScoreCouponOverdueNotice insertParam = new UserScoreCouponOverdueNotice();
            insertParam.setUserId(AuthUserContext.get().getUserId());
            insertParam.setActivityId(activityId);
            insertParam.setNoticeSwitch(1);
            insertParam.setFormId(formId);
            userScoreCouponOverdueNoticeMapper.insert(insertParam);
        } else {
            if (notice.getNoticeSwitch() == 0) {
                notice.setNoticeSwitch(1);
                userScoreCouponOverdueNoticeMapper.updateById(notice);
            }
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<Long>> getShops(Long activityId) {
        List<Long> shops = new ArrayList<>();
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(activityId);
        LambdaQueryWrapper<ScoreConvertShop> scoreConvertShopLambdaQueryWrapper = new LambdaQueryWrapper<>();
        scoreConvertShopLambdaQueryWrapper.eq(ScoreConvertShop::getConvertId, activityId);
        if (!scoreConvert.getIsAllCouponShop() && scoreConvert.getType() != 2){
            scoreConvertShopLambdaQueryWrapper.eq(ScoreConvertShop::getType,ScoreShopTypeEnum.COUPON.value().shortValue());
        }
        if (!scoreConvert.getIsAllShop() && scoreConvert.getType() == 2){
            scoreConvertShopLambdaQueryWrapper.eq(ScoreConvertShop::getType,ScoreShopTypeEnum.APPLY.value().shortValue());
        }
        List<ScoreConvertShop> scoreConvertShops = shopMapper.selectList(scoreConvertShopLambdaQueryWrapper);
        shops = scoreConvertShops.stream().map(ScoreConvertShop::getShopId).collect(Collectors.toList());
        return ServerResponseEntity.success(shops);
    }

    @Override
    public ServerResponseEntity<List<ScoreConvertVO>> checkScoreConvertByCoupon(Long couponId) {
        //根据优惠券ID查询关联了该优惠券的兑礼到店类型活动
        List<ScoreConvertVO> couponConvertListByToShop = this.getCouponConvertListByToShop(couponId);
        return ServerResponseEntity.success(couponConvertListByToShop);
    }

    @Override
    public ServerResponseEntity<ScoreConvertVO> getScoreConvertVO(Long convertId) {
        ScoreConvertVO scoreConvert = this.selectConvert(convertId);
        return ServerResponseEntity.success(scoreConvert);
    }

    /**
     * 查询当前优惠券是否绑定了兑礼到店的积分活动和高价值会员活动
     * @param couponId 优惠券ID
     * */
    @Override
    public ServerResponseEntity<Integer> isToShop(Long couponId) {

        List<ScoreConvertCoupon> scoreConvertCoupons = scoreConvertCouponMapper.selectList(
                new LambdaQueryWrapper<ScoreConvertCoupon>()
                        .eq(ScoreConvertCoupon::getCouponId, couponId)
        );

        Integer isToShop = 1;
        if (CollectionUtil.isNotEmpty(scoreConvertCoupons)){
            List<Long> convertIdList = scoreConvertCoupons.stream()
                    .map(ScoreConvertCoupon::getConvertId)
                    .collect(Collectors.toList());
            List<ScoreConvert> scoreConvertList = scoreConvertMapper.selectList(
                    new LambdaQueryWrapper<ScoreConvert>()
                            .in(ScoreConvert::getConvertId, convertIdList)
                            .eq(ScoreConvert::getType, ConvertTypeEnum.TO_SHOP.getType())
            );
            if (CollectionUtil.isNotEmpty(scoreConvertList)){
                isToShop = 0;
            }
        }

        return ServerResponseEntity.success(isToShop);
    }

    @Override
    public ServerResponseEntity<List<Long>> getCouponIdListByConvertId(Long convertId) {
        List<Long> couponIdList = this.selectCouponIdList(convertId);
        return ServerResponseEntity.success(couponIdList);
    }

    @Override
    public ServerResponseEntity<ScoreConvertVO> getScoreConvertAndCouponList(Long convertId) {
        ScoreConvertVO scoreConvertVO = this.selectConvert(convertId);
        List<Long> couponIdList = this.selectCouponIdList(convertId);
        scoreConvertVO.setCouponIds(couponIdList);
        return ServerResponseEntity.success(scoreConvertVO);
    }



    @Override
    public ServerResponseEntity<ScoreConvertAppListVO> selectScoreConvertList(Long userScore, Integer type, Long shopId) {
        ScoreConvertAppListVO scoreConvertAppListVO = new ScoreConvertAppListVO();
        // 判断是否是指定活动进行查询，如果不是那么就按全部（不同活动类型）进行查询
        if(0 != convertId || null != convertId){
            ScoreConvert scoreConvert = scoreConvertMapper.selectOne(new QueryWrapper<ScoreConvert>().lambda()
                    .eq(ScoreConvert::getConvertId, convertId)
            );
            scoreConvertAppListVO.setAppointConvert(scoreConvert);
        }
        // 查询用户所拥有积分能够兑换的积分活动（积分兑礼或者积分兑券）
        List<ScoreConvertListVO> isExchangeList = scoreConvertMapper.selectUserScoreGeScoreConvertList(userScore, type, shopId);
        // 查询用户积分不能兑换的积分活动
        List<ScoreConvertListVO> isNotExchangeList = scoreConvertMapper.selectUserScoreLtScoreConvertList(userScore, type, shopId);
        scoreConvertAppListVO.setIsExchangeList(isExchangeList);
        scoreConvertAppListVO.setIsNotExchangeList(isNotExchangeList);

        return ServerResponseEntity.success(scoreConvertAppListVO);
    }

    private Integer shopNum(Long convertId,short type) {
        //适用门店数量
        QueryWrapper<ScoreConvertShop> shopParam = new QueryWrapper<>();
        shopParam.eq("convert_id", convertId);
        shopParam.eq("type", type);
        List<ScoreConvertShop> shops = shopMapper.selectList(shopParam);
        return shops.size();
    }

    private void addShops(List<Long> shops, Long convertId, short type) {
        List<ScoreConvertShop> scoreConvertShops = new ArrayList<>();
        shops.forEach(temp -> {
            ScoreConvertShop scoreConvertShop = new ScoreConvertShop();
            scoreConvertShop.setConvertId(convertId);
            scoreConvertShop.setShopId(temp);
            scoreConvertShop.setType(type);
            scoreConvertShops.add(scoreConvertShop);
        });
        shopMapper.insertBatch(scoreConvertShops);
    }

    private void addCoupons(List<Long> couponIds, Long convertId) {
        ArrayList<ScoreConvertCoupon> scoreConvertCoupons = new ArrayList<>();
        couponIds.forEach(temp -> {
            ScoreConvertCoupon scoreConvertCoupon = new ScoreConvertCoupon();
            scoreConvertCoupon.setConvertId(convertId);
            scoreConvertCoupon.setCouponId(temp);
            scoreConvertCoupons.add(scoreConvertCoupon);
        });
        scoreConvertCouponMapper.insertBatch(scoreConvertCoupons);
    }

    private List<Long> shops(Long convertId,short type) {
        List<ScoreConvertShop> shops = shopMapper.selectList(new LambdaQueryWrapper<ScoreConvertShop>()
                .eq(ScoreConvertShop::getConvertId, convertId)
                .eq(ScoreConvertShop::getType, type));
        return shops.stream().map(ScoreConvertShop::getShopId).collect(Collectors.toList());
    }

    private List<Long> coupons(Long convertId) {
        //关联优惠券
        List<ScoreConvertCoupon> scoreConvertCoupons = scoreConvertCouponMapper.selectList(new LambdaQueryWrapper<ScoreConvertCoupon>().eq(ScoreConvertCoupon::getConvertId, convertId));
        List<Long> result = scoreConvertCoupons.stream().map(ScoreConvertCoupon::getCouponId).collect(Collectors.toList());
        return result;
    }

    private void addPhoneNum(List<String> phoneNums, Long convertId) {
        if (!CollectionUtils.isEmpty(phoneNums)) {
            List<ScoreConvertPhone> list = new ArrayList<>();
            phoneNums.forEach(temp -> {
                ScoreConvertPhone scoreConvertPhone = new ScoreConvertPhone();
                scoreConvertPhone.setConvertId(convertId);
                scoreConvertPhone.setPhoneNum(temp);
                list.add(scoreConvertPhone);
            });
            scoreConvertPhoneMapper.insertBatch(list);
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userId", condition = "#userId!=null")
    })
    public void clearUserPermissionsCache(Long userId) {
    }


    List<ScoreConvertVO> getCouponConvertListByToShop(Long couponId){
        //根据优惠券ID查询关联了该优惠券的兑礼到店类型活动
        List<ScoreConvertCoupon> scoreConvertCoupons = scoreConvertCouponMapper.selectList(
                new LambdaQueryWrapper<ScoreConvertCoupon>()
                        .eq(ScoreConvertCoupon::getCouponId, couponId)
        );

        List<ScoreConvertVO> convertVOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(scoreConvertCoupons)){
            List<Long> convertIdList = scoreConvertCoupons.stream()
                    .map(ScoreConvertCoupon::getConvertId)
                    .collect(Collectors.toList());
            List<ScoreConvert> scoreConverts = lambdaQuery()
                    .in(ScoreConvert::getConvertId, convertIdList)
                    .eq(ScoreConvert::getDel, ScoreConvertDelEnum.NOT_DEL.value())
                    .list();
            convertVOList = scoreConverts.stream().map(convert -> {
                ScoreConvertVO scoreConvertVO = new ScoreConvertVO();

                BeanUtils.copyProperties(convert, scoreConvertVO);

                return scoreConvertVO;
            }).collect(Collectors.toList());
        }

        return convertVOList;
    }


    List<Long> selectCouponIdList(Long convertId){
        List<ScoreConvertCoupon> scoreConvertCouponList = scoreConvertCouponMapper.selectList(
                new LambdaQueryWrapper<ScoreConvertCoupon>()
                        .eq(ScoreConvertCoupon::getConvertId, convertId)
        );
        List<Long> couponIdList = scoreConvertCouponList.stream()
                .map(ScoreConvertCoupon::getCouponId)
                .collect(Collectors.toList());
        return couponIdList;
    }

    ScoreConvertVO selectConvert(Long convertId){
        ScoreConvert scoreConvert = baseMapper.selectById(convertId);
        ScoreConvertVO scoreConvertVO = new ScoreConvertVO();
        BeanUtils.copyProperties(scoreConvert, scoreConvertVO);
        return scoreConvertVO;
    }
}
