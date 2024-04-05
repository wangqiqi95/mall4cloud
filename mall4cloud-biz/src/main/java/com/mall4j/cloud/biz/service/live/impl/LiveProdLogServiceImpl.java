
package com.mall4j.cloud.biz.service.live.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.live.LiveProdLogMapper;
import com.mall4j.cloud.biz.model.live.LiveProdLog;
import com.mall4j.cloud.biz.service.live.LiveProdLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@Service
@AllArgsConstructor
public class LiveProdLogServiceImpl extends ServiceImpl<LiveProdLogMapper, LiveProdLog> implements LiveProdLogService {

    private final LiveProdLogMapper liveProdLogMapper;
}
