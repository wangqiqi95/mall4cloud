package com.mall4j.cloud.group.service.async.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.group.bo.ExportPopUpAdOperateBO;
import com.mall4j.cloud.group.constant.PopUpAdUserOperateEnum;
import com.mall4j.cloud.group.manager.PopUpAdUserOperateManager;
import com.mall4j.cloud.group.model.PopUpAdUserOperate;
import com.mall4j.cloud.group.service.PopUpAdUserOperateService;
import com.mall4j.cloud.group.service.async.AsyncPopUpAdOperateService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsyncPopUpAdOperateServiceImpl implements AsyncPopUpAdOperateService {

    @Autowired
    private PopUpAdUserOperateManager popUpAdUserOperateManager;

    @Autowired
    private PopUpAdUserOperateService popUpAdUserOperateService;

    @Autowired
    private MapperFacade mapperFacade;


    private static Integer PAGE_NUM = 0;
    private static Long PAGES = 1L;
    private static Integer PAGE_SIZE = 100;

//    private final Map<Long, StoreVO> storeVOCache = new HashMap<>();

//    private final Map<String, UserApiVO> userApiVOCache = new HashMap<>();

    @Async
    @Override
    public void AsyncExportData(Long adId, FinishDownLoadDTO finishDownLoadDTO, String popUpAdName) {

        Map<Long, StoreVO> storeVOCache = new HashMap<>();

//        因为广告场景用户数量太大，所以将该map放入循环中，避免循环的时间内长时间有大量UserApiVO对象无法回收
//        Map<String, UserApiVO> userApiVOCache = new HashMap<>();

        //生成本地相关文件
        File excelFile = popUpAdUserOperateManager.createExcelFile(finishDownLoadDTO.getFileName());
        //生成本地文件excel写入器
        ExcelWriter excelWriter = popUpAdUserOperateManager.createExcelWriter(excelFile);

        //进行数据查询准备功能
        IPage<PopUpAdUserOperate> page;

        Integer pageNum = PAGE_NUM;
        Long pages = PAGES;
        Integer pageSize = PAGE_SIZE;

        Integer size = 0;

        //遍历获取所有数据
        while(pageNum < pages){
            pageNum += 1;
            page = new Page<>(pageNum, pageSize);
            page = popUpAdUserOperateService.lambdaQuery()
                    .eq(PopUpAdUserOperate::getPopUpAdId, adId)
                    .page(page);

            pages = page.getPages();

            //复制数据
            List<ExportPopUpAdOperateBO> exportPopUpAdOperateBOS = mapperFacade.mapAsList(page.getRecords(), ExportPopUpAdOperateBO.class);

            //聚合所有门店ID
            List<Long> storeIdList = exportPopUpAdOperateBOS.stream()
                    .map(ExportPopUpAdOperateBO::getStoreId)
                    .distinct()
                    .filter(storeId -> Objects.isNull(storeVOCache.get(storeId)))//如果店铺缓存中已存在
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(storeIdList)){
                List<StoreVO> storeVOList = popUpAdUserOperateManager.getStoreByIdList(storeIdList);

                if (CollectionUtil.isNotEmpty(storeVOList)){
                    Map<Long, StoreVO> storeVOMap = storeVOList.stream()
                            .collect(Collectors.toMap(StoreVO::getStoreId, storeVO -> storeVO));
                    storeVOCache.putAll(storeVOMap);

                }
            }


            //聚合用户ID
            List<String> unionIdList = exportPopUpAdOperateBOS.stream()
                    .map(ExportPopUpAdOperateBO::getUnionId)
                    .distinct()
//                    .filter(unionId -> Objects.isNull(userApiVOCache.get(unionId)))
                    .collect(Collectors.toList());

            Map<String, UserApiVO> userApiVOCache = new HashMap<>();

            if (CollectionUtil.isNotEmpty(unionIdList)){
                List<UserApiVO> userApiVOList = popUpAdUserOperateManager.getUserApiVOByUnionIdList(unionIdList);
                if (CollectionUtil.isNotEmpty(userApiVOList)){
                    Map<String, UserApiVO> userApiVOMap = userApiVOList.stream()
                            .collect(Collectors.toMap(UserApiVO::getUnionId, userApiVO -> userApiVO));
                    userApiVOCache.putAll(userApiVOMap);
                }
            }

            exportPopUpAdOperateBOS.stream()
                    .forEach(operate -> {
                        //校验行为方式并设置相关属性
                        if (operate.getOperate().equals(PopUpAdUserOperateEnum.BROWSE.getOperate())){
                            operate.setOperateStr(PopUpAdUserOperateEnum.BROWSE.getOperateStr());
                        }

                        if (operate.getOperate().equals(PopUpAdUserOperateEnum.CLICK.getOperate())){
                            operate.setOperateStr(PopUpAdUserOperateEnum.CLICK.getOperateStr());
                        }
                        //获取门店信息
                        StoreVO storeVO;
                        if (MapUtil.isNotEmpty(storeVOCache)
                                && Objects.nonNull(storeVO = storeVOCache.get(operate.getStoreId()))){
                            operate.setStoreCode(storeVO.getStoreCode());
                            operate.setStoreName(storeVO.getName());
                        }
                        //获取用户信息
                        UserApiVO userApiVO;
                        if (MapUtil.isNotEmpty(userApiVOCache)
                                && Objects.nonNull(userApiVO = userApiVOCache.get(operate.getUnionId()))){
                            operate.setVipCode(userApiVO.getVipcode());
                            operate.setVipNickName(userApiVO.getNickName());
                        }
                        operate.setPopUpAdName(popUpAdName);
                    });

            //记录文件数据总数
            size += exportPopUpAdOperateBOS.size();
            //写入数据到本地文件
            popUpAdUserOperateManager.excelWriter(0, ExportPopUpAdOperateBO.SHEET_NAME,
                    ExportPopUpAdOperateBO.class, excelWriter, exportPopUpAdOperateBOS);
        }

        //关闭流
        excelWriter.finish();
        //文件上传到下载中心并删除本地文件
        popUpAdUserOperateManager.uploadFile(excelFile, size, finishDownLoadDTO);
    }
}
