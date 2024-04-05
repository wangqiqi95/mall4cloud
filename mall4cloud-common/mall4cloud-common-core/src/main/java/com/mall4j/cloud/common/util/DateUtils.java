package com.mall4j.cloud.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.vo.DateVO;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 时间工具类
 * @author lhd
 */
@Slf4j
public class DateUtils {

    public static Long dateToNumber(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);
        Long value = 0L;
        try {
            value = Long.valueOf(format);
        } catch (Exception e) {
            return value;
        }
        return value;
    }
    public static String dateToStrYmd(Date date) {
        String format = "yyyy-MM-dd";
        return DateUtil.format(date,format);
    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar cBegin = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cBegin.setTime(dBegin);
        cEnd.setTime(dEnd);

        cEnd.add(Calendar.DAY_OF_MONTH,-2);
        // 测试此日期是否在指定日期之后
        while (cEnd.getTime().after(cBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(dateToLast(cBegin.getTime()));
        }
        return lDate;
    }

    public static List<DateVO> findEveryDays(Date dBegin, Date dEnd) {
        List<DateVO> params = new ArrayList();
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        calEnd.add(Calendar.DAY_OF_MONTH,-2);
        // 判断两个时间是否为同一天
        if (!DateUtil.isSameDay(dBegin, dEnd)) {
            DateVO first = new DateVO();
            first.setStartTime(dBegin);
            first.setEndTime(dateToLast(dBegin));
            params.add(first);
        }
        // 测试此日期是否在指定日期之后
        while (calEnd.getTime().after(calBegin.getTime())) {
            DateVO res = new DateVO();
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            res.setEndTime(dateToLast(calBegin.getTime()));
            res.setStartTime(dateToFast(calBegin.getTime()));
            params.add(res);
        }
        DateVO last = new DateVO();
        last.setStartTime(dateToFast(dEnd));
        last.setEndTime(dEnd);
        params.add(last);
        return params;
    }
    public static Date dateToFast(Date date) {
        return dateToFastOrLast(date,1);
    }
    public static Date dateToLast(Date date) {
       return dateToFastOrLast(date,2);
    }

    /**
     * 获取传入时间的前一天的初始和结束时间
     */
    public static Date getBeforeDate(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DATE, -1);
        return instance.getTime();
    }
    /**
     * 获取传入时间的前n年时间点
     * @param n 例如: n = -1 表示当前时间的前一年， n = 1 表示获取后一年
     */
    public static Date getBeforeYear(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, n);
        return calendar.getTime();
    }
    /**
     * 获取传入时间的前n月时间点
     * @param n 例如: n = -1 表示当前时间的前一月， n = 1 表示获取后一月
     */
    public static Date getBeforeOrNextNumMonth(Date date, int n) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH, n);
        return instance.getTime();
    }
    /**
     * 获取传入时间的前n天时间点
     * @param n 例如: n = -1 表示当前时间的前一天， n = 1 表示获取后一天
     */
    public static Date getBeforeDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        return calendar.getTime();
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    /**
     *
     * @param date 2020-06-12 15:00:00
     * @param type 1 date 的开始时间例如： 2020-06-12 00:00:00
     *             2 date 的结束时间例如： 2020-06-12 23:59:59
     * @return
     */
    public static Date dateToFastOrLast(Date date, Integer type) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH ) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        String dateStr = "";
        if (Objects.equals(1,type)){
            dateStr = y + "-" + m + "-" + d +" " + 00 + ":"+ 00 + ":" + 00;
        } else {
            dateStr = y + "-" + m + "-" + d +" " + 23 + ":"+ 59 + ":" + 59;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = sdf.parse(dateStr);
            c.setTime(parse);
        }catch (ParseException e) {
            System.out.println("解析异常");
        }
        return c.getTime();
    }

    public static List<DateVO> getMonthBetweenStartTimeAndEndTime(Date startTime, Date endTime) {
        List<DateVO> monthBetween = getMonthBetween(startTime, endTime);
        if (monthBetween.size() == 0) {
            DateVO param = new DateVO();
            param.setStartTime(startTime);
            param.setEndTime(endTime);
            monthBetween.add(param);
            return monthBetween;
        }
        if (monthBetween.size() == 1) {
            DateVO param = monthBetween.get(0);
            param.setEndTime(endTime);
            monthBetween.set(0,param);
            return monthBetween;
        }
        DateVO param = monthBetween.get(0);
        param.setStartTime(startTime);
        DateVO last = monthBetween.get(monthBetween.size() - 1);
        last.setEndTime(endTime);
        monthBetween.set(0,param);
        monthBetween.set(monthBetween.size()-1,last);
        return monthBetween;
    }

    public static List<DateVO> getMonthBetween(Date minDate, Date maxDate){
        List<DateVO> res = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            DateVO param = new DateVO();
            Date time = curr.getTime();
            param.setStartTime(getMonthFirstOrLastDay(time,0));
            param.setEndTime(getMonthFirstOrLastDay(time,1));
            res.add(param);
            curr.add(Calendar.MONTH, 1);
        }
        return res;
    }

    public static int countMonthNumBetweenDate(Date startTime, Date endTime) {
        Calendar min = Calendar.getInstance();
        min.setTime(startTime);
        Calendar max = Calendar.getInstance();
        max.setTime(endTime);
        int year =max.get(Calendar.YEAR) - min.get(Calendar.YEAR);
        //开始日期若小月结束日期
        if(year < 0){
            year = -year;
            return year*12 + min.get(Calendar.MONTH) - max.get(Calendar.MONTH);
        }
        return year*12 + max.get(Calendar.MONTH) - min.get(Calendar.MONTH);
    }

//    public static int countDayBetweenDate(Date startTime, Date endTime) {
//        long day = DateUtil.betweenDay(startTime, endTime, true);
//        return Integer.valueOf(String.valueOf(day));
//    }
    /**
     * @param date 传入的时间点  2020-03-12 15:30:00
     * @param type 0 获取传入时间的月份开始时间 2020-03-01 00:00:00
     *             1 获取传入时间的月份最后一天的时刻 2020-03-31 23:59:59
     * @return date
     */
    public static  Date getMonthFirstOrLastDay(Date date,Integer type) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (Objects.equals(0,type)){
            // 获取传入时间的月份的第一天 yyyy-MM-dd 00:00:00
            c.add(Calendar.MONTH,0);
            c.set(Calendar.DAY_OF_MONTH,1);
            c.set(Calendar.HOUR_OF_DAY,00);
            c.set(Calendar.MINUTE,00);
            c.set(Calendar.SECOND,00);
        } else {
            // 获取传入时间的月份的最后一天 yyyy-MM-dd 23:59:59
            c.add(Calendar.MONTH,1);
            c.set(Calendar.DAY_OF_MONTH,0);
            c.set(Calendar.HOUR_OF_DAY,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
        }
        return c.getTime();
    }


    /**
     * 获取上个月的最后一天
     */
    public static Date getBeforeLastMonthdate(){
        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static int getTheDayByWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekIdx = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekIdx;
    }

    public static int getTheDayByMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthIdx = calendar.get(Calendar.DAY_OF_MONTH);
        return monthIdx;
    }


    public static long getTheMills(LocalDateTime time){
        long epochMilli = time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        return epochMilli;
    }

    public static Integer getRemainSecondsOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                        ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    public static Integer getTheBetweenSeconds(LocalDateTime begin, LocalDateTime end){
        Duration duration = Duration.between(begin,end);
        long seconds = duration.getSeconds();
        return (int) seconds;
    }

    public static boolean match(String startTime1, String endTime1, String startTime2, String endTime2) {
        DateTime parseStartTime1 = DateUtil.parse(startTime1, "HH:mm");
        DateTime parseEndTime1 = DateUtil.parse(endTime1, "HH:mm");

        DateTime parseStartTime2 = DateUtil.parse(startTime2, "HH:mm");
        parseStartTime2.setTime(parseStartTime2.getTime()+ 60*1000);//第二个开始时间需要加上一分钟，否则还是整点范围导致时间冲突
        DateTime parseEndTime2 = DateUtil.parse(endTime2, "HH:mm");
//        log.info("parseStartTime1:{} parseEndTime1:{} parseStartTime2:{} parseEndTime2:{}",parseStartTime1,parseEndTime1,parseStartTime2,parseEndTime2);
        return !(parseStartTime2.isAfter(parseEndTime1) || parseStartTime1.isAfter(parseEndTime2));
    }
}
