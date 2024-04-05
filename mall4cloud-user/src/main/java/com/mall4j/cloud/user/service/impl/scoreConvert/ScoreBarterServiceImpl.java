package com.mall4j.cloud.user.service.impl.scoreConvert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.scoreConvert.ScoreConvertDelEnum;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreBarterLogListDTO;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreBarterSaveDTO;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreBarterUpdateDTO;
import com.mall4j.cloud.user.dto.scoreConvert.ScoreConvertListDTO;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreBarterLogMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreConvertMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreConvertShopMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.UserScoreCouponNoticeMapper;
import com.mall4j.cloud.user.model.scoreConvert.ScoreBarterLog;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvert;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvertShop;
import com.mall4j.cloud.user.model.scoreConvert.UserScoreCouponNotice;
import com.mall4j.cloud.user.service.scoreConvert.ScoreBarterService;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterListVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterLogVO;
import com.mall4j.cloud.user.vo.scoreConvert.ScoreBarterVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.mall4j.cloud.user.constant.scoreConvert.*;

/**
 * 积分换物
 *
 * @author shijing
 */

@Slf4j
@Service
@Transactional
public class ScoreBarterServiceImpl implements ScoreBarterService {

    @Resource
    private ScoreConvertMapper scoreConvertMapper;
    @Resource
    private ScoreConvertShopMapper shopMapper;
    @Resource
    private ScoreBarterLogMapper scoreBarterLogMapper;
    @Resource
    private UserScoreCouponNoticeMapper userScoreCouponNoticeMapper;


    @Override
    public ServerResponseEntity<PageInfo<ScoreBarterListVO>> list(ScoreConvertListDTO param) {
        log.info("积分换物列表的搜索条件为 :{}", param);
        PageInfo<ScoreBarterListVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                scoreConvertMapper.scoreBarterList(param)
        );
        //处理返回值
        List<ScoreBarterListVO> list = result.getList();
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(temp ->{
                //活动状态
                if(temp.getConvertStatus().equals(ScoreActivityStatusEnum.DISABLE.value())){
                    temp.setActivityStatus(ScoreActivityStatusEnum.DISABLE.desc());
                    temp.setActivityStatusKey(ScoreActivityStatusEnum.DISABLE.value());
                }
                if(temp.getConvertStatus().equals(ScoreActivityStatusEnum.ENABLE.value())){
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
                }
                //适用门店数量
                if (!temp.getIsAllShop()){
                    Integer shopNum = shopNum(temp.getId(), ScoreShopTypeEnum.APPLY.value().shortValue());
                    temp.setShopNum(shopNum);
                }
                //剩余库存
//                Integer count = scoreBarterLogMapper.selectCount(new LambdaQueryWrapper<ScoreBarterLog>().eq(ScoreBarterLog::getConvertId,temp.getId()));
//                long surplusAmount = temp.getMaxAmount() - count.longValue();
//                temp.setSurplusAmount(surplusAmount);
            });
        }
        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> save(ScoreBarterSaveDTO param) {
        log.info("新增积分换物的参数为 :{}", param);
        //插入基础信息
        ScoreConvert scoreConvert = BeanUtil.copyProperties(param, ScoreConvert.class);
        scoreConvert.setConvertType(ScoreConvertTypeEnum.BARTER.value().shortValue());
        scoreConvert.setStocks(param.getMaxAmount());
        scoreConvert.setCreateId(AuthUserContext.get().getUserId());
        scoreConvert.setCreateName(AuthUserContext.get().getUsername());
        scoreConvert.setUpdateId(AuthUserContext.get().getUserId());
        scoreConvert.setUpdateName(AuthUserContext.get().getUsername());
        scoreConvertMapper.insert(scoreConvert);

        //组装关联门店信息
        List<Long> shops = param.getShops();
        List<Long> convertShops = param.getConvertShops();
        addShops(shops,convertShops,scoreConvert.getConvertId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(ScoreBarterUpdateDTO param) {
        //修改基本信息
        ScoreConvert scoreConvert = BeanUtil.copyProperties(param, ScoreConvert.class);
        scoreConvert.setUpdateId(AuthUserContext.get().getUserId());
        scoreConvert.setUpdateName(AuthUserContext.get().getUsername());
        scoreConvertMapper.updateById(scoreConvert);

        //删除原有门店信息
        shopMapper.delete(new LambdaUpdateWrapper<ScoreConvertShop>().eq(ScoreConvertShop::getConvertId,param.getConvertId()));
        //新增关联门店信息
        List<Long> shops = param.getShops();
        List<Long> convertShops = param.getConvertShops();
        addShops(shops,convertShops,scoreConvert.getConvertId());

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<ScoreBarterVO> selectDetail(Long convertId) {
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(convertId);
        ScoreBarterVO result = BeanUtil.copyProperties(scoreConvert, ScoreBarterVO.class);
        if (!result.getIsAllShop()){
            //适用门店数量
            Integer shopNum = shopNum(result.getConvertId(), ScoreShopTypeEnum.APPLY.value().shortValue());
            result.setShopNum(shopNum);
            //适用门店
            List<Long> shops = shops(result.getConvertId(), ScoreShopTypeEnum.APPLY.value().shortValue());
            result.setShops(shops);
        }
        //兑换门店数量
        if (null != result.getIsAllConvertShop()){
            if (!result.getIsAllConvertShop()){
                Integer convertShopNum = shopNum(result.getConvertId(), ScoreShopTypeEnum.EXCHANGE.value().shortValue());
                result.setConvertShopNum(convertShopNum);
                //兑换门店
                List<Long> convertShops = shops(result.getConvertId(), ScoreShopTypeEnum.EXCHANGE.value().shortValue());
                result.setConvertShops(convertShops);
            }
        }

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> updateConvertStatus(Long convertId, Integer status) {
        ScoreConvert updateParam = new ScoreConvert();
        updateParam.setConvertId(convertId);
        updateParam.setConvertStatus(status);
        updateParam.setUpdateId(AuthUserContext.get().getUserId());
        scoreConvertMapper.updateById(updateParam);

        ScoreConvert scoreConvert = scoreConvertMapper.selectById(convertId);
        //活动启用且生效，调用上新通知
        if (status == 0 && System.currentTimeMillis() > scoreConvert.getStartTime().getTime() && System.currentTimeMillis() < scoreConvert.getEndTime().getTime()) {
            //积分换券上新推送
            if (scoreConvert.getConvertType().equals(ScoreConvertTypeEnum.COUPON.value().shortValue())){
                List<UserScoreCouponNotice> userScoreCouponNotices = userScoreCouponNoticeMapper.selectList(new LambdaQueryWrapper<UserScoreCouponNotice>().eq(UserScoreCouponNotice::getNoticeSwitch, 0));
                // todo 调用推送
            }
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteConvert(Long convertId) {
        ScoreConvert scoreConvert = new ScoreConvert();
        scoreConvert.setConvertId(convertId);
        scoreConvert.setDel(ScoreConvertDelEnum.DEL.value().shortValue());
        scoreConvert.setUpdateId(AuthUserContext.get().getUserId());
        scoreConvertMapper.updateById(scoreConvert);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> addInventory(Long convertId, Long num) {
        ScoreConvert scoreConvert = scoreConvertMapper.selectById(convertId);
        ScoreConvert param = new ScoreConvert();
        param.setMaxAmount(scoreConvert.getMaxAmount() + num);
        param.setStocks(scoreConvert.getStocks() + num);
        param.setVersion(scoreConvert.getVersion() + 1);
        param.setUpdateId(AuthUserContext.get().getUserId());

        int row = scoreConvertMapper.update(param, new LambdaUpdateWrapper<ScoreConvert>()
                .eq(ScoreConvert::getConvertId, convertId)
                .eq(ScoreConvert::getVersion, scoreConvert.getVersion()));
        if (row < 1){
            throw new LuckException("增加库存失败，请重新操作");
        }

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageInfo<ScoreBarterLogVO>> logList(ScoreBarterLogListDTO param) {
        PageInfo<ScoreBarterLogVO> result = PageHelper.startPage(param.getPageNo(), param.getPageSize()).doSelectPageInfo(() ->
                scoreBarterLogMapper.logList(param)
        );

        return ServerResponseEntity.success(result);
    }

    @Override
    public ServerResponseEntity<Void> addCourierCode(Long id,String courierCode,String note) {
        ScoreBarterLog scoreBarterLog = new ScoreBarterLog();
        scoreBarterLog.setLogId(id);
        scoreBarterLog.setCourierCode(courierCode);
        scoreBarterLog.setNote(note);
        scoreBarterLog.setUpdateId(AuthUserContext.get().getUserId());
        scoreBarterLogMapper.updateById(scoreBarterLog);

        return ServerResponseEntity.success();
    }

    @Override
    public void export(ScoreBarterLogListDTO param, HttpServletResponse response) {
        log.info("------------------------------------导出文件开始");
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.setSheet(0);
        //参数为true时只导出有别名的
        writer.setOnlyAlias(true);
        Workbook workbook = writer.getWorkbook();
        StyleSet styleSet = writer.getStyleSet();
        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle cellStyleForDate = styleSet.getCellStyleForDate();
        cellStyleForDate.setDataFormat(dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));

        //自定义标题别名
        //列名和对象属性名一致
        writer.addHeaderAlias("id", "序号");
        writer.addHeaderAlias("userCardNumber", "用户卡号");
        writer.addHeaderAlias("userPhone", "会员手机号");
        writer.addHeaderAlias("convertScore", "兑换积分数");
        writer.addHeaderAlias("deliveryType", "发货方式");
        writer.addHeaderAlias("shopName", "兑换门店");
        writer.addHeaderAlias("convertAddress", "兑换地址");
        writer.addHeaderAlias("description", "兑换时间");
        writer.addHeaderAlias("courierCode", "物流信息");
        writer.addHeaderAlias("note", "备注");

        // 一次性写出内容，使用默认样式，强制输出标题
        List<ScoreBarterLogVO> result = scoreBarterLogMapper.logList(param);

        writer.write(result, true);//response为HttpServletResponse对象

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String timeNow = sdf.format(date);
        String fileName = "兑换记录_" +  timeNow + ".xls";
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);

        //写出倒流
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("获取输出流异常");
            e.printStackTrace();
        }
        writer.flush(out);
        // 关闭writer，释放内存
        writer.close();
        IoUtil.close(out);

    }

    public Integer shopNum(Long convertId,Short type){
        //适用门店数量
        List<ScoreConvertShop> shops = shopMapper.selectList(new LambdaQueryWrapper<ScoreConvertShop>().eq(ScoreConvertShop::getConvertId,convertId).eq(ScoreConvertShop::getType,type));
        return shops.size();
    }

    public void addShops(List<Long> shops, List<Long> convertShops, Long convertId){
        List<ScoreConvertShop> scoreConvertShops = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shops)){
            shops.forEach(temp ->{
                ScoreConvertShop scoreConvertShop = new ScoreConvertShop();
                scoreConvertShop.setConvertId(convertId);
                scoreConvertShop.setShopId(temp);
                scoreConvertShop.setType(ScoreShopTypeEnum.APPLY.value().shortValue());
                scoreConvertShops.add(scoreConvertShop);
            });
        }
        if (!CollectionUtils.isEmpty(convertShops)){
            convertShops.forEach(temp ->{
                ScoreConvertShop scoreConvertShop = new ScoreConvertShop();
                scoreConvertShop.setConvertId(convertId);
                scoreConvertShop.setShopId(temp);
                scoreConvertShop.setType(ScoreShopTypeEnum.EXCHANGE.value().shortValue());
                scoreConvertShops.add(scoreConvertShop);
            });
        }
        if (!CollectionUtils.isEmpty(scoreConvertShops)){
            shopMapper.insertBatch(scoreConvertShops);
        }
    }

    public List<Long> shops(Long convertId,Short type){
        List<ScoreConvertShop> shops = shopMapper.selectList(new LambdaQueryWrapper<ScoreConvertShop>().eq(ScoreConvertShop::getConvertId,convertId).eq(ScoreConvertShop::getType,type));
        List<Long> result = shops.stream().map(ScoreConvertShop::getShopId).collect(Collectors.toList());

        return result;
    }

}
