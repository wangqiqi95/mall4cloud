package com.mall4j.cloud.biz.service.chat.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.dto.chat.ImportExcelSessionContentDTO;
import com.mall4j.cloud.biz.dto.chat.ImportExcelSessionDTO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.service.chat.HistorySessionService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.csvExport.ExcelListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 会话存档历史数据处理
 *
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HistorySessionServiceImpl implements HistorySessionService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void importExcel(MultipartFile file) {
        ExcelListener<ImportExcelSessionDTO> excelListener = new ExcelListener<>(ImportExcelSessionDTO.class);
        try {
            EasyExcelFactory.read(file.getInputStream(), ImportExcelSessionDTO.class, excelListener).sheet().doRead();
        } catch (Exception e) {
            log.info("导入会话存档记录细失败 {} {}", e, e.getMessage());
            throw new LuckException("操作失败，请检查模板及内容是否正确或者有为空的内容");
        }
        List<ImportExcelSessionDTO> list = excelListener.getList();
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("操作失败，未解析到内容");
        }
        if (list.size() > 500) {
            throw new LuckException("操作失败，数据不可超过500行");
        }
        //
        list=cleanExcelData(list);

        //需要保存会话存档数据
        List<SessionFile> sessionFiles = new ArrayList();

        for (ImportExcelSessionDTO sessionDTO : list) {
            log.info("导入会话存档记录信息：{}", JSON.toJSONString(sessionDTO));
            //TODO 除文本消息外其它媒体消息处理还在与客户沟通
            if(!sessionDTO.getMsgType().equals("text")){
                continue;
            }
            SessionFile sessionFile = new SessionFile();
            sessionFile.setMsgid(sessionDTO.getMsgId());
            sessionFile.setAction(sessionDTO.getMsgAction());
            sessionFile.setCreateTime(new Date(Long.parseLong(sessionDTO.getMsgTime())));
            sessionFile.setFrom(sessionDTO.getSendUserId());
            sessionFile.setTolist(JSON.toJSONString(Arrays.asList(sessionDTO.getReceiveUserIds())));
            sessionFile.setRoomid(sessionDTO.getChatId());
            sessionFile.setMsgtype(sessionDTO.getMsgType());
            String msgContent=UnicodeUtil.toString(sessionDTO.getMsgContent());
            sessionFile.setContent(msgContent);
            if(sessionDTO.getMsgType().equals("text") && StrUtil.isNotEmpty(msgContent)){
                JSONObject content=JSONObject.parseObject(msgContent);
                sessionFile.setContent(content.getString("content"));
            }
            sessionFile.setUpdateTime(new Date());
            sessionFiles.add(sessionFile);
        }

        log.info("保存会话存档数据：{}", JSON.toJSONString(sessionFiles));
        if(sessionFiles != null && sessionFiles.size()>0){
            try {
                long start = new Date().getTime();
                mongoTemplate.insertAll(sessionFiles);
                long end = new Date().getTime();
                log.info("保存会话存档数据成功 =======>end, 耗时：{}s",((end - start) / 1000.0) );
            }catch (Exception e){
                e.printStackTrace();
                log.info("保存会话存档数据失败：{}", e);
            }

        }
    }

    private List<ImportExcelSessionDTO> cleanExcelData(List<ImportExcelSessionDTO> list) {
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("请检查内容是否正确，或者有为空的内容");
        }
        // 集合去重复
        list = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ImportExcelSessionDTO::getMsgId))), ArrayList::new));
        return list;
    }

    public static void main(String[] strings){
        String unicode="\\ud83d\\ude07\\ud83d\\ude09\\ud83d\\ude09\\ud83d\\ude17";
        // 去除前面的反斜杠并进行Unicode解码
//        String decodedText = StrUtil.removePrefixIgnoreCase(unicode, "\\u");
//        byte[] bytes = CharsetUtil.CHARSET_UTF_8.decode(decodedText);

        // 使用Hutool提供的工具类将byte数组转换为字符串
        String result = UnicodeUtil.toString(unicode);

        System.out.println("结果：" + result);

        System.out.printf(JSON.toJSONString(Arrays.asList("wmhwEuDQAAMhSI9newoA1qBamDcffFZg")));
    }
}
