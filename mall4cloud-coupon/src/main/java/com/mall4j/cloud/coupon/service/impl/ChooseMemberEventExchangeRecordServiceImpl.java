package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.dto.ChooseMemberEventRecordDTO;
import com.mall4j.cloud.coupon.manager.ChooseMemberEventExchangeRecordManager;
import com.mall4j.cloud.coupon.model.ChooseMemberEventExchangeRecord;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventExchangeRecordMapper;
import com.mall4j.cloud.coupon.service.ChooseMemberEventExchangeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Service
public class ChooseMemberEventExchangeRecordServiceImpl extends ServiceImpl<ChooseMemberEventExchangeRecordMapper, ChooseMemberEventExchangeRecord> implements ChooseMemberEventExchangeRecordService {

    @Autowired
    private ChooseMemberEventExchangeRecordManager chooseMemberEventExchangeRecordManager;
    @Override
    public ServerResponseEntity<PageInfo<ChooseMemberEventExchangeRecordVO>> recordList(ChooseMemberEventExchangeRecordPageDTO pageDTO) {
        PageInfo<ChooseMemberEventExchangeRecordVO> recordPage = PageHelper.startPage(pageDTO.getPageNo(), pageDTO.getPageSize()).doSelectPageInfo(() ->
                baseMapper.recordList(pageDTO)
        );
        return ServerResponseEntity.success(recordPage);
    }

    @Override
    public ServerResponseEntity<Void> addLogistics(ChooseMemberEventRecordDTO recordDTO) {
        chooseMemberEventExchangeRecordManager.addLogistics(recordDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> confirmExport(List<Long> ids) {
        chooseMemberEventExchangeRecordManager.confirmExport(ids);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Map<String, Integer>> importRecord(MultipartFile file) {
        Map<String, Integer> result = chooseMemberEventExchangeRecordManager.importRecord(file);
        return ServerResponseEntity.success(result);
    }

    @Override
    public void export(ChooseMemberEventExchangeRecordPageDTO param, HttpServletResponse response) {
        chooseMemberEventExchangeRecordManager.export(param,response);
    }


}
