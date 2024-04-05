package com.mall4j.cloud.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.ChooseMemberEvent;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventStatisticsVO;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 指定会员活动表（提供最具价值会员活动表） 服务类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventService extends IService<ChooseMemberEvent> {

    ServerResponseEntity add(AddChooseMemberEventDTO addChooseMemberEventDTO);

    ServerResponseEntity<PageVO<ChooseMemberEventVO>> getChooseMemberEventPage(QueryChooseMemberEventPageDTO pageDTO);

    ServerResponseEntity<PageVO<ChooseMemberEventVO>> exchange(ExchangeChooseMemberEventDTO exchangeChooseMemberEventDTO);

    ServerResponseEntity<List<String>> exportUser(MultipartFile file);

    ServerResponseEntity<ChooseMemberEventStatisticsVO> eventStatistics(Long eventId);

    ServerResponseEntity addStockNum(UpdateChooseMemberEventStockDTO addStockDTO);

    ServerResponseEntity enableOrDisable(Long eventId);

    void exportEventStatistics(HttpServletResponse response, Long eventId);

    ServerResponseEntity edit(EditChooseMemberEventDTO editChooseMemberEventDTO);

    ServerResponseEntity<ChooseMemberEventVO> detail(Long eventId);


    ServerResponseEntity eventMemberValid(Long eventId);

    ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> queryUserRecord(Integer pageNo, Integer pageSize);
}
