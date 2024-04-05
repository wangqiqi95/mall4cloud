package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.dto.cp.NoticeDTO;
import com.mall4j.cloud.biz.mapper.chat.SessionSearchDbDao;
import com.mall4j.cloud.biz.mapper.cp.NoticeMapper;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.model.cp.Notice;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.service.cp.NoticeService;
import com.mall4j.cloud.biz.vo.cp.NoticeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知消息实现类
 *
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper mapper;

    @Override
    public PageVO<NoticeVO> page(PageDTO pageDTO, NoticeDTO dto) {
        return PageUtil.doPage(pageDTO, () -> mapper.list(dto));
    }

    @Override
    public void update(Notice notice) {
        mapper.update(notice);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public void saveNotice(Notice notice) {
        mapper.save(notice);
    }

}
