package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.vo.WeixinActoinReplyLogsVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinActoinReplyLogs;
import com.mall4j.cloud.biz.mapper.WeixinActoinReplyLogsMapper;
import com.mall4j.cloud.biz.service.WeixinActoinReplyLogsService;
import com.mall4j.cloud.common.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * 公众号事件推送回复日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:36
 */
@Service
public class WeixinActoinReplyLogsServiceImpl implements WeixinActoinReplyLogsService {

    @Autowired
    private WeixinActoinReplyLogsMapper weixinActoinReplyLogsMapper;

    @Override
    public PageVO<WeixinActoinReplyLogs> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> weixinActoinReplyLogsMapper.list());
    }

    @Override
    public WeixinActoinReplyLogs getById(Long id) {
        return weixinActoinReplyLogsMapper.getById(id);
    }

    @Override
    public void save(WeixinActoinReplyLogs weixinActoinReplyLogs) {
        weixinActoinReplyLogsMapper.save(weixinActoinReplyLogs);
    }

    @Override
    public void update(WeixinActoinReplyLogs weixinActoinReplyLogs) {
        weixinActoinReplyLogsMapper.update(weixinActoinReplyLogs);
    }

    @Override
    public void deleteById(Long id) {
        weixinActoinReplyLogsMapper.deleteById(id);
    }

    @Override
    public void saveWeixinActoinReplyLogs(WeixinActoinReplyLogsVO weixinActoinReplyLogsVO) {

        WeixinActoinReplyLogs weixinActoinReplyLogs=new WeixinActoinReplyLogs();
        BeanUtils.copyProperties(weixinActoinReplyLogsVO,weixinActoinReplyLogs);

        weixinActoinReplyLogs.setId(RandomUtil.getUniqueNumStr());
        weixinActoinReplyLogs.setDelFlag(0);
        weixinActoinReplyLogs.setCreateTime(new Date());
        this.save(weixinActoinReplyLogs);
    }
}
