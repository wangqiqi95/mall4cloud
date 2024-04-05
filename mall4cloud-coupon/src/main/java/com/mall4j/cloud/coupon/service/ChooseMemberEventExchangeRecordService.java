package com.mall4j.cloud.coupon.service;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventRecordDTO;
import com.mall4j.cloud.coupon.model.ChooseMemberEventExchangeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventExchangeRecordService extends IService<ChooseMemberEventExchangeRecord> {

    ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> recordList(ChooseMemberEventExchangeRecordPageDTO pageDTO);
    void export(ChooseMemberEventExchangeRecordPageDTO param, HttpServletResponse response);

    ServerResponseEntity<Void> addLogistics(ChooseMemberEventRecordDTO recordDTO);

    ServerResponseEntity<Void> confirmExport(List<Long> ids);

    ServerResponseEntity<Map<String, Integer>> importRecord(MultipartFile file);
}
