package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.product.dto.ErpSkuPriceDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.user.dto.ReqScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.dto.ScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.dto.ScoreTimeDiscountActivityItemDTO;
import com.mall4j.cloud.user.mapper.ScoreTimeDiscountActivityItemMapper;
import com.mall4j.cloud.user.mapper.scoreConvert.ScoreConvertCouponMapper;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivity;
import com.mall4j.cloud.user.mapper.ScoreTimeDiscountActivityMapper;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivityItem;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvertCoupon;
import com.mall4j.cloud.user.service.ScoreTimeDiscountActivityItemService;
import com.mall4j.cloud.user.service.ScoreTimeDiscountActivityService;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountActivityItemVO;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountActivityVO;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 积分限时折扣
 *
 * @author gmq
 * @date 2022-07-11 15:11:39
 */
@Slf4j
@Service
public class ScoreTimeDiscountActivityServiceImpl extends ServiceImpl<ScoreTimeDiscountActivityMapper, ScoreTimeDiscountActivity> implements ScoreTimeDiscountActivityService {

    @Autowired
    private ScoreTimeDiscountActivityMapper scoreTimeDiscountActivityMapper;

    @Autowired
    private ScoreTimeDiscountActivityItemMapper activityItemMapper;

    @Autowired
    private ScoreTimeDiscountActivityItemService scoreTimeDiscountActivityItemService;

    @Autowired
    private MapperFacade mapperFacade;

    @Resource
    private ScoreTimeDiscountActivityItemMapper scoreTimeDiscountActivityItemMapper;

    @Resource
    private ScoreConvertCouponMapper scoreConvertCouponMapper;

    @Override
    public PageVO<ScoreTimeDiscountActivityVO> pageData(ReqScoreTimeDiscountActivityDTO pageDTO) {
        PageVO<ScoreTimeDiscountActivityVO> pageVO=PageUtil.doPage(pageDTO, () -> scoreTimeDiscountActivityMapper.getList(pageDTO));

        pageVO.getList().stream().forEach(discountActivityVO -> {

            //获取兑换券信息
//            addItemData(discountActivityVO);

            formatDate(discountActivityVO);

        });

        return pageVO;
    }

    @Override
    public ScoreTimeDiscountActivityVO getDetailById(Long id) {
        ScoreTimeDiscountActivityVO discountActivityVO=scoreTimeDiscountActivityMapper.getDetailById(id);
        if(Objects.nonNull(discountActivityVO)){
            //兑换券信息
            addItemData(discountActivityVO);

            formatDate(discountActivityVO);

            return discountActivityVO;
        }
        return null;
    }

    private void addItemData(ScoreTimeDiscountActivityVO discountActivityVO){
        List<ScoreTimeDiscountActivityItemVO> activityItemVOS=scoreTimeDiscountActivityItemMapper.getItemListByActivityId(discountActivityVO.getId());
        if(CollectionUtil.isNotEmpty(activityItemVOS) && activityItemVOS.size()>0){

            activityItemVOS.stream().forEach(itemVO->{
                //优惠券名称
                List<ScoreConvertCoupon> coupons = scoreConvertCouponMapper.selectList(new LambdaQueryWrapper<ScoreConvertCoupon>().eq(ScoreConvertCoupon::getConvertId, itemVO.getConvertId()));
                List<Long> couponIds = coupons.stream().map(ScoreConvertCoupon::getCouponId).collect(Collectors.toList());
            });

            discountActivityVO.setActivityItemVOS(activityItemVOS);
        }
    }

    private void formatDate(ScoreTimeDiscountActivityVO discountActivityVO){
        if(Objects.nonNull(discountActivityVO)){
            //周期类型：0-单周 1-每周 2-每月
            if(discountActivityVO.getTimeType()==0){
                discountActivityVO.setStartTime(DateUtil.format(DateUtil.parse(discountActivityVO.getStartTime(),
                        DatePattern.NORM_DATETIME_PATTERN),DatePattern.NORM_DATETIME_PATTERN));
                discountActivityVO.setEndTime(DateUtil.format(DateUtil.parse(discountActivityVO.getEndTime(),
                        DatePattern.NORM_DATETIME_PATTERN),DatePattern.NORM_DATETIME_PATTERN));
            }
            if(discountActivityVO.getTimeType()==1 || discountActivityVO.getTimeType()==2){
                discountActivityVO.setStartTime(DateUtil.format(DateUtil.parse(discountActivityVO.getStartTime(),
                        DatePattern.NORM_DATETIME_FORMAT),"HH:mm"));
                discountActivityVO.setEndTime(DateUtil.format(DateUtil.parse(discountActivityVO.getEndTime(),
                        DatePattern.NORM_DATETIME_FORMAT),"HH:mm"));
            }
        }
    }

    private void parseDate(ScoreTimeDiscountActivity discountActivity,ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO){
        if(Objects.nonNull(discountActivity)){
            //周期类型：0-单周 1-每周 2-每月
            if(discountActivity.getTimeType()==0){
                discountActivity.setStartTime(DateUtil.parse(scoreTimeDiscountActivityDTO.getStartTimeStr(),
                        DatePattern.NORM_DATETIME_FORMAT));
                discountActivity.setEndTime(DateUtil.parse(scoreTimeDiscountActivityDTO.getEndTimeStr(),
                        DatePattern.NORM_DATETIME_FORMAT));
            }
            if(discountActivity.getTimeType()==1 || discountActivity.getTimeType()==2){
                discountActivity.setStartTime(DateUtil.parse(scoreTimeDiscountActivityDTO.getStartTimeStr(),
                        "HH:mm"));
                discountActivity.setEndTime(DateUtil.parse(scoreTimeDiscountActivityDTO.getEndTimeStr(),
                        "HH:mm"));
            }
        }
    }

    @Override
    public void saveTo(ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO) {

        //校验冲突
        if(scoreTimeDiscountActivityDTO.isBtnSaveOen()){
            List<Long> converIds = scoreTimeDiscountActivityDTO.getItemDTOS().stream().map(ScoreTimeDiscountActivityItemDTO::getConvertId).collect(Collectors.toList());
            checkContent(converIds);
        }

        //保存活动信息
        ScoreTimeDiscountActivity scoreTimeDiscountActivity = mapperFacade.map(scoreTimeDiscountActivityDTO, ScoreTimeDiscountActivity.class);
        if(scoreTimeDiscountActivityDTO.isBtnSaveOen()){
            scoreTimeDiscountActivity.setStatus(1);
        }else{
            scoreTimeDiscountActivity.setStatus(0);
        }
        scoreTimeDiscountActivity.setCreateTime(new Date());
        scoreTimeDiscountActivity.setCreateBy(AuthUserContext.get().getUsername());
        parseDate(scoreTimeDiscountActivity,scoreTimeDiscountActivityDTO);
        scoreTimeDiscountActivity.setId(null);
        this.save(scoreTimeDiscountActivity);
//        scoreTimeDiscountActivityMapper.saveTo(scoreTimeDiscountActivity);
        Long activityId=scoreTimeDiscountActivity.getId();

        //保存活动关联兑换券
        if(CollectionUtil.isNotEmpty(scoreTimeDiscountActivityDTO.getItemDTOS())){
            List<ScoreTimeDiscountActivityItem> activityItems=new ArrayList<>();
            scoreTimeDiscountActivityDTO.getItemDTOS().forEach(scoreTimeDiscountActivityItemDTO -> {
                ScoreTimeDiscountActivityItem activityItem=mapperFacade.map(scoreTimeDiscountActivityItemDTO,ScoreTimeDiscountActivityItem.class);
                activityItem.setActivityId(activityId);
                activityItems.add(activityItem);
            });

            scoreTimeDiscountActivityItemService.saveBatch(activityItems);
        }

    }

    @Override
    public void updateTo(ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO) {

        //校验冲突
        if(scoreTimeDiscountActivityDTO.isBtnSaveOen()){
            List<Long> converIds = scoreTimeDiscountActivityDTO.getItemDTOS().stream().map(ScoreTimeDiscountActivityItemDTO::getConvertId).collect(Collectors.toList());
            checkContent(converIds);
        }

        //更新活动信息
        ScoreTimeDiscountActivity scoreTimeDiscountActivity = mapperFacade.map(scoreTimeDiscountActivityDTO, ScoreTimeDiscountActivity.class);
        parseDate(scoreTimeDiscountActivity,scoreTimeDiscountActivityDTO);
        if(scoreTimeDiscountActivityDTO.isBtnSaveOen()){
            scoreTimeDiscountActivity.setStatus(1);
        }else{
            scoreTimeDiscountActivity.setStatus(0);
        }
        scoreTimeDiscountActivity.setUpdateBy(AuthUserContext.get().getUsername());
        scoreTimeDiscountActivity.setUpdateTime(new Date());
        this.updateById(scoreTimeDiscountActivity);
        Long activityId=scoreTimeDiscountActivity.getId();


        /**
         * 更新活动关联兑换券
         */
        //删除关联兑换券
        scoreTimeDiscountActivityItemService.remove(new LambdaQueryWrapper<ScoreTimeDiscountActivityItem>()
                .eq(ScoreTimeDiscountActivityItem::getActivityId,activityId));

        if(CollectionUtil.isNotEmpty(scoreTimeDiscountActivityDTO.getItemDTOS())){
            List<ScoreTimeDiscountActivityItem> activityItems=new ArrayList<>();
            scoreTimeDiscountActivityDTO.getItemDTOS().forEach(scoreTimeDiscountActivityItemDTO -> {
                ScoreTimeDiscountActivityItem activityItem=mapperFacade.map(scoreTimeDiscountActivityItemDTO,ScoreTimeDiscountActivityItem.class);
                activityItem.setActivityId(activityId);
                activityItems.add(activityItem);
            });

            scoreTimeDiscountActivityItemService.saveBatch(activityItems);
        }
    }

    @Override
    public ServerResponseEntity<Void> openOrClose(Long id, Integer status) {
        ScoreTimeDiscountActivity discountActivity=this.getById(id);
        if(Objects.isNull(discountActivity)){
            return ServerResponseEntity.showFailMsg("未获取到实体数据");
        }

        List<ScoreTimeDiscountActivityItem> activityItems=scoreTimeDiscountActivityItemService
                .list(new LambdaQueryWrapper<ScoreTimeDiscountActivityItem>()
                .eq(ScoreTimeDiscountActivityItem::getActivityId,id));

        if(CollectionUtil.isEmpty(activityItems) || activityItems.size()<=0){
            return ServerResponseEntity.showFailMsg("未获取到实体数据");
        }
        if(status==1){
            //校验冲突
            List<Long> converIds = activityItems.stream().map(ScoreTimeDiscountActivityItem::getConvertId).collect(Collectors.toList());
            checkContent(converIds);
        }

        discountActivity.setStatus(status);
        discountActivity.setUpdateBy(AuthUserContext.get().getUsername());
        discountActivity.setUpdateTime(new Date());
        this.updateById(discountActivity);

        return ServerResponseEntity.success();
    }


    @Override
    public void deleteToById(Long id) {

        scoreTimeDiscountActivityMapper.deleteToById(id);

        //删除关联兑换券
        scoreTimeDiscountActivityItemService.remove(new LambdaQueryWrapper<ScoreTimeDiscountActivityItem>().eq(ScoreTimeDiscountActivityItem::getActivityId,id));
    }

    @Override
    public List<ScoreTimeDiscountVO> getConvertScoreCoupons(List<Long> convertIds) {
        List<ScoreTimeDiscountVO> discountVOS=activityItemMapper.getContentItems(convertIds);
        List<ScoreTimeDiscountVO> backs=new ArrayList<>();
        if(CollectionUtil.isEmpty(discountVOS) || discountVOS.size()<=0){
            return backs;
        }
        Map<Long,ScoreTimeDiscountVO> backMaps=new HashMap<>();
        discountVOS.forEach(discountVO->{
            if(parseDateTime(discountVO.getTimeType(),discountVO.getStartTime(),discountVO.getEndTime()
                    ,discountVO.getWeeks(), discountVO.getStartDay(), discountVO.getEndDay())){
                if(!backMaps.containsKey(discountVO.getConvertId())){
                    backMaps.put(discountVO.getConvertId(),discountVO);
                }
            }
        });

        backs=new ArrayList<>(backMaps.values());

        return backs;
    }

    private void checkContent(List<Long> convertIds){
        StringBuilder sb=new StringBuilder();
        List<ScoreTimeDiscountVO> discountVOS=activityItemMapper.getContentItems(convertIds);
        Map<Long,List<Long>> contentMap=new HashMap<>();
        if(CollectionUtil.isNotEmpty(discountVOS) && discountVOS.size()>0){
            discountVOS.forEach(discountVO ->{
                if(!contentMap.containsKey(discountVO.getConvertId())){
                    List<Long> activityIds=new ArrayList<>();
                    activityIds.add(discountVO.getActivityId());
                    contentMap.put(discountVO.getConvertId(),activityIds);
                }else{
                    contentMap.get(discountVO.getConvertId()).add(discountVO.getActivityId());
                }
            });

            for (Map.Entry<Long, List<Long>> entry : contentMap.entrySet()) {
                sb.append("积分兑换券活动id:").append(entry.getKey()).append(",已存在于积分限时折扣活动:").append(entry.getValue());
            }

            throw new LuckException("当前活动中的部分券与其它活动冲突,请重新编辑。"+sb.toString());
        }
    }

    private static boolean parseDateTime(Integer timeType,String startTimeStr,String endTimeStr,String week,Integer dayStart,Integer dayEnd){

        boolean flag=false;

        Date now=new Date();
        if(timeType==0){
//            Date startTime=DateUtil.beginOfDay(DateUtil.parse(startTimeStr,
//                    DatePattern.NORM_DATE_PATTERN));
//            Date endTime=DateUtil.endOfDay(DateUtil.parse(endTimeStr,
//                    DatePattern.NORM_DATETIME_FORMAT));

            Date startTime=DateUtil.parse(startTimeStr,
                    DatePattern.NORM_DATETIME_FORMAT);
            Date endTime=DateUtil.parse(endTimeStr,
                    DatePattern.NORM_DATETIME_FORMAT);

            log.info("单周期 -> 开始时间{} 结束时间{} 当前时间{}",
                    DateUtil.format(startTime,DatePattern.NORM_DATETIME_FORMAT)
                    ,DateUtil.format(endTime,DatePattern.NORM_DATETIME_FORMAT)
                    ,DateUtil.format(new Date(),DatePattern.NORM_DATETIME_FORMAT));

            if(startTime.getTime()<now.getTime() && now.getTime()<endTime.getTime()){
                flag=true;
                log.info("匹配到单周期 {}",flag);
            }
        }else if(timeType==1){
            String[] weeks=week.split(",");
            int weekDay=(DateUtil.dayOfWeek(now)-1);

            log.info("每周 -> 循环周期{} 今天周{}",week,weekDay);

            if(StringUtils.startsWithAny(String.valueOf(weekDay),weeks)){
                String timeStart=DateUtil.format(DateUtil.parse(startTimeStr,
                        DatePattern.NORM_DATETIME_FORMAT),"HH:mm");
                String timeEnd=DateUtil.format(DateUtil.parse(endTimeStr,
                        DatePattern.NORM_DATETIME_FORMAT),"HH:mm");

                Date startTime=DateUtil.parse(timeStart,
                        "HH:mm");
                Date endTime=DateUtil.parse(timeEnd,
                        "HH:mm");
                now=DateUtil.parse(DateUtil.format(now,"HH:mm"),
                        "HH:mm");
                if(startTime.getTime()<now.getTime() && now.getTime()<endTime.getTime()){
                    flag=true;
                    log.info("匹配到每周周期 {}",flag);
                }
            }

        }else if(timeType==2){
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DATE,dayStart);
            String timeStart=DateUtil.format(calendar.getTime(),"yyyy-MM-dd")+" "+DateUtil.format(DateUtil.parse(startTimeStr,
                    DatePattern.NORM_DATETIME_FORMAT),"HH:mm:ss");

            calendar.set(Calendar.DATE,dayEnd);
            String timeEnd=DateUtil.format(calendar.getTime(),"yyyy-MM-dd")+" "+DateUtil.format(DateUtil.parse(endTimeStr,
                    DatePattern.NORM_DATETIME_FORMAT),"HH:mm:ss");

            int daysOfMounth=com.mall4j.cloud.common.util.DateUtil.getDaysOfMonth(new Date());
            int monthDay = DateUtil.dayOfMonth(new Date());

            log.info("每月 -> 第{}天开始->日期({})  第{}天结束->日期({}) 当前是第{}天->日期({}) 当月总天数{}",
                    dayStart,timeStart,
                    dayEnd,timeEnd,
                    monthDay,DateUtil.format(now,"yyyy-MM-dd HH:mm:ss"),
                    daysOfMounth);

            if(dayStart<=monthDay && monthDay<=dayEnd && dayEnd<=daysOfMounth){
                Date startTime=DateUtil.parse(timeStart,"yyyy-MM-dd HH:mm:ss");
                Date endTime=DateUtil.parse(timeEnd,"yyyy-MM-dd HH:mm:ss");
                if(startTime.getTime()<now.getTime() && now.getTime()<endTime.getTime()){
                    flag=true;
                    log.info("匹配到每月周期 {}",flag);
                }
            }
        }
        return flag;
    }

    public static void main(String[] strings){
        //周期类型：0-单周 1-每周 2-每月

        String startTimeStr="2022-07-21 00:00:00";
        String endTimeStr="2022-07-21 00:00:00";


//        startTimeStr=DateUtil.format(DateUtil.parse(startTimeStr,
//                DatePattern.NORM_DATE_PATTERN),DatePattern.NORM_DATE_PATTERN)+" 00:00:00";
//        Date startTime=DateUtil.parse(startTimeStr,
//                DatePattern.NORM_DATETIME_FORMAT);

//        endTimeStr=DateUtil.format(DateUtil.parse(endTimeStr,
//                DatePattern.NORM_DATE_PATTERN),DatePattern.NORM_DATE_PATTERN)+" 23:59:59";

        Date startTime=DateUtil.beginOfDay(DateUtil.parse(startTimeStr,
                DatePattern.NORM_DATE_PATTERN));
        Date endTime=DateUtil.endOfDay(DateUtil.parse(endTimeStr,
                DatePattern.NORM_DATETIME_FORMAT));

        log.info("单周期 -> 开始时间{} 结束时间{} 当前时间{}",
                DateUtil.format(startTime,DatePattern.NORM_DATETIME_FORMAT)
                ,DateUtil.format(endTime,DatePattern.NORM_DATETIME_FORMAT)
                ,DateUtil.format(new Date(),DatePattern.NORM_DATETIME_FORMAT));

//        int timeType=2;
//        String week="1,3,4,5";
//        int dayStart=10,dayEnd=20;
//
//        System.out.println("是否匹配活动成功 "+parseDateTime(timeType,startTimeStr,endTimeStr,week,dayStart,dayEnd));
//        Integer discountS=70;
//        Integer conbertScore=350;

//        Double discount= Arith.div((double) discountS,100,2);
//        Double discountScore= Arith.mul((double) conbertScore,discount);
////        Long discountScore=new BigDecimal(conbertScore).multiply(new BigDecimal(discount)).longValue();
//        Integer point_value=-discountScore.intValue();
//        System.out.println("discount--->"+discount);
//        System.out.println("discountScore--->"+discountScore);
//        System.out.println("discountScore--->"+discountScore.longValue());
//        System.out.println("point_value--->"+point_value);
//        System.out.println(Arith.setScale(50.5,0).longValue());
//
//        System.out.println("当前月总天数："+ com.mall4j.cloud.wx.util.DateUtil.getDaysOfMonth(new Date()));
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE,15);
//        System.out.println(DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
//        calendar.set(Calendar.DATE,25);
//        System.out.println(DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));

//        System.out.println(Arith.ceil(434.32).longValue());
    }

}
