package com.mall4j.cloud.biz.service.chat.impl;

import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.biz.dto.chat.SessionTimeOutDTO;
import com.mall4j.cloud.biz.mapper.chat.EndStatementMapper;
import com.mall4j.cloud.biz.mapper.chat.SessionTimeOutMapper;
import com.mall4j.cloud.biz.mapper.chat.WorkDateMapper;
import com.mall4j.cloud.biz.model.chat.EndStatement;
import com.mall4j.cloud.biz.model.chat.SessionTimeOut;
import com.mall4j.cloud.biz.model.chat.WorkDate;
import com.mall4j.cloud.biz.service.chat.SessionTimeOutService;
import com.mall4j.cloud.biz.vo.chat.SessionTimeOutVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会话超时规则实现类
 *
 */
@Service
public class SessionTimeOutServiceImpl implements SessionTimeOutService {

   @Autowired
   SessionTimeOutMapper sessionTimeOutMapper;
   @Autowired
   WorkDateMapper dateMapper;
   @Autowired
    EndStatementMapper statementMapper;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<SessionTimeOutVO> page(PageDTO pageDTO, SessionTimeOutDTO timeOutDTO) {
        PageVO<SessionTimeOutVO> outVOS = PageUtil.doPage(pageDTO, () -> sessionTimeOutMapper.list(timeOutDTO));
        List<Long> outId = outVOS.getList().stream().map(SessionTimeOutVO :: getId).collect(Collectors.toList());
        Map<Long,List<WorkDate>> dateMap = new HashMap<>();
        Map<Long,List<EndStatement>> listMap = new HashMap<>();
        if(outId != null && outId.size()>0){
            List<WorkDate> workDateList = dateMapper.getBySessionId(outId);
            List<EndStatement> endStatementList = statementMapper.getBySessionId(outId);
            dateMap = workDateList.stream().collect(Collectors.groupingBy(WorkDate::getSessionId));
            listMap = endStatementList.stream().collect(Collectors.groupingBy(EndStatement::getSessionId));
        }
        for (SessionTimeOutVO vo:outVOS.getList()) {
            vo.setWorkDateList(dateMap.get(vo.getId()));
            vo.setStatementList(listMap.get(vo.getId()));
        }
        return PageUtil.mongodbPage(pageDTO, outVOS.getList(),outVOS.getTotal());
        //return PageUtil.doPage(pageDTO, () -> sessionTimeOutMapper.list(timeOutDTO));
    }

    @Override
    @Transactional
    public void save(SessionTimeOutDTO timeOut) {
        SessionTimeOut out = mapperFacade.map(timeOut, SessionTimeOut.class);
        List<EndStatement> statementList =  timeOut.getStatementList();
        List<WorkDate> dates = timeOut.getWorkDateList();
        out.setCreateName(AuthUserContext.get().getUsername());
        sessionTimeOutMapper.save(out);
        if(statementList != null && statementList.size()>0){
            for (EndStatement endStatement:statementList) {
                endStatement.setSessionId(out.getId());
            }
            statementMapper.batchInsert(statementList);
        }
        if (dates != null && dates.size()>0){
            for (WorkDate d:dates) {
                d.setSessionId(out.getId());
            }
            dateMapper.batchInsert(dates);
        }
    }

    @Override
    @Transactional
    public void update(SessionTimeOutDTO timeOut) {
        SessionTimeOut out = mapperFacade.map(timeOut, SessionTimeOut.class);
        out.setCreateName(AuthUserContext.get().getUsername());
        sessionTimeOutMapper.update(out);
        dateMapper.deleteById(out.getId());
        statementMapper.deleteById(out.getId());
        List<EndStatement> statementList =  timeOut.getStatementList();
        List<WorkDate> dates = timeOut.getWorkDateList();
        if(statementList != null && statementList.size()>0){
            for (EndStatement endStatement:statementList) {
                endStatement.setSessionId(out.getId());
            }
            statementMapper.batchInsert(statementList);
        }
        if (dates != null && dates.size()>0){
            for (WorkDate d:dates) {
                d.setSessionId(out.getId());
            }
            dateMapper.batchInsert(dates);
        }
    }

    @Override
    public void deleteById(Long id) {
        sessionTimeOutMapper.deleteById(id);
        dateMapper.deleteById(id);
        statementMapper.deleteById(id);
    }

    @Override
    public SessionTimeOutVO getById(Long id) {
        SessionTimeOutVO vo = sessionTimeOutMapper.getById(id);
        if(vo != null){
            List<Long> outId = new ArrayList<>();
            outId.add(vo.getId());
            List<WorkDate> workDateList = dateMapper.getBySessionId(outId);
            List<EndStatement> endStatementList = statementMapper.getBySessionId(outId);
            Map<Long,List<WorkDate>> dateMap = workDateList.stream().collect(Collectors.groupingBy(WorkDate::getSessionId));
            Map<Long,List<EndStatement>> listMap = endStatementList.stream().collect(Collectors.groupingBy(EndStatement::getSessionId));
            vo.setWorkDateList(dateMap.get(vo.getId()));
            vo.setStatementList(listMap.get(vo.getId()));
        }
        return vo;
    }

}
