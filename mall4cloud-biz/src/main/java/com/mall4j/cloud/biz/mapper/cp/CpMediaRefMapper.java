package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpMediaRef;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-27 11:29:05
 */
public interface CpMediaRefMapper extends BaseMapper<CpMediaRef> {


    List<CpMediaRef> listAfterThreeDayPicMediaIds();

}
