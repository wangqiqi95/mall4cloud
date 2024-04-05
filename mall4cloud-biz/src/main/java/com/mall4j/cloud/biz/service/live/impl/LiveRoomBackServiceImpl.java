

package com.mall4j.cloud.biz.service.live.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.live.LiveRoomBackMapper;
import com.mall4j.cloud.biz.model.live.LiveRoomBack;
import com.mall4j.cloud.biz.service.live.LiveRoomBackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author lhd
 * @date 2020-08-12 10:04:53
 */
@Service
@AllArgsConstructor
public class LiveRoomBackServiceImpl extends ServiceImpl<LiveRoomBackMapper, LiveRoomBack> implements LiveRoomBackService {

    private final LiveRoomBackMapper liveRoomBackMapper;
}
