package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.dto.StoreRenovationMouduldParamDTO;
import com.mall4j.cloud.platform.dto.StoreRenovationSearchDTO;
import com.mall4j.cloud.platform.mapper.StoreRenovationMapper;
import com.mall4j.cloud.platform.model.StoreRenovation;
import com.mall4j.cloud.platform.service.RenoApplyService;
import com.mall4j.cloud.platform.service.StoreRenovationService;
import com.mall4j.cloud.platform.vo.StoreRenovationMouduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 店铺装修信息
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:46:32
 */
@Slf4j
@Service
public class StoreRenovationServiceImpl extends ServiceImpl<StoreRenovationMapper, StoreRenovation> implements StoreRenovationService {

    @Autowired
    private StoreRenovationMapper storeRenovationMapper;

    @Autowired
    private RenoApplyService renoApplyService;

    @Override
    public PageVO<StoreRenovation> page(StoreRenovationSearchDTO searchDTO) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(searchDTO.getPageNum());
        pageDTO.setPageSize(searchDTO.getPageSize());
        PageVO<StoreRenovation> pageVO = PageUtil.doPage(pageDTO, () -> storeRenovationMapper.listBySearchDTO(searchDTO));
        pageVO.getList().forEach(storeRenovation -> {
            storeRenovation.setStoreIdList(renoApplyService.listByRenoId(storeRenovation.getRenovationId()));
        });
        return pageVO;

    }

    @Override
    public StoreRenovation getByRenovationId(Long renovationId) {
        StoreRenovation byRenovationId = storeRenovationMapper.getByRenovationId(renovationId);
        byRenovationId.setStoreIdList(renoApplyService.listByRenoId(renovationId));
        return byRenovationId;
    }

//    @Override
//    public void save(StoreRenovation storeRenovation) {
//        storeRenovationMapper.save(storeRenovation);
//    }

    @Override
    public void update(StoreRenovation storeRenovation) {
        storeRenovationMapper.update(storeRenovation);
    }

    @Override
    public void deleteById(Long renovationId) {
        storeRenovationMapper.deleteById(renovationId);
    }

    @Override
    public PageVO<StoreRenovationMouduleVO> modulePage(PageDTO pageDTO, StoreRenovationMouduldParamDTO paramDTO) {
        PageVO<StoreRenovationMouduleVO> objectPageVO = PageUtil.doPage(pageDTO, () -> storeRenovationMapper.moduleList(paramDTO));
        ;
        //封装适用门店信息
        objectPageVO.getList().forEach(storeRenovationMouduleVO -> {
            storeRenovationMouduleVO.setRenoApplyStoreList(renoApplyService.listByRenoId(storeRenovationMouduleVO.getRenovationId()));
        });
        return objectPageVO;
    }

    @Override
    public StoreRenovation getHomepage(Long storeId) {

        //获取当前门店配置
        StoreRenovation storeRenovation = storeRenovationMapper.getHomePage(storeId);
        if (Objects.isNull(storeRenovation)) {
            storeRenovation = this.getOne(new LambdaQueryWrapper<StoreRenovation>().eq(StoreRenovation::getStoreId, Constant.MAIN_SHOP).eq(StoreRenovation::getStatus, 1).eq(StoreRenovation::getHomeStatus, 1), false);
        }
        return storeRenovation;
    }

    @Override
    public Boolean online(Long renovationId) {
        return storeRenovationMapper.updateStatus(renovationId, 1);
    }

    @Override
    public Boolean offline(Long renovationId) {
        return storeRenovationMapper.updateStatus(renovationId, 0);
    }

    @Override
    public StoreRenovation getRenovation(Integer homeStatus) {
        return storeRenovationMapper.getRenovation(homeStatus);
    }

    @Override
    public void pushStoreRenovation() {
        Date now=DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd HH"+":00:00"),"yyyy-MM-dd HH:mm:ss");
        List<StoreRenovation> storeRenovationList=this.list(new LambdaQueryWrapper<StoreRenovation>()
                .eq(StoreRenovation::getStatus,0)
                .between(StoreRenovation::getMakePushTime,now,now));
        log.info("pushStoreRenovation--> size:{}",storeRenovationList.size());
        if(CollectionUtil.isNotEmpty(storeRenovationList)){
            List<StoreRenovation> pushList=new ArrayList<>();
            storeRenovationList.forEach(item ->{
                StoreRenovation storeRenovation=new StoreRenovation();
                storeRenovation.setRenovationId(item.getRenovationId());
                storeRenovation.setStatus(1);
                storeRenovation.setPushStatus(1);
                storeRenovation.setPushBy("系统定时任务");
                storeRenovation.setPushTime(new Date());
                storeRenovation.setPushType(1);
                pushList.add(storeRenovation);
            });
            this.updateBatchById(pushList);
        }
    }

    @Override
    public ServerResponseEntity<String> saveStoreRenovationItem(Long renovationId) {
        Long userId = AuthUserContext.get().getUserId();
        int result = storeRenovationMapper.saveStoreRenovationItem(renovationId, userId);
        if (result != 0) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        // 当详情表新增数据成功之后，店铺装修记录表中的点击人数和次数要进行递增
        int num = storeRenovationMapper.selectStoreRenovationItemOfUserId(userId);
        StoreRenovation storeRenovation = storeRenovationMapper.selectById(renovationId);
        if(storeRenovation == null){
            return ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID, "数据异常，并未找到对应的店铺装修记录");
        }
        storeRenovation.setOpenNumber(storeRenovation.getOpenNumber() + 1);
        if(num < 1){
            storeRenovation.setOpenPeople(storeRenovation.getOpenPeople() + 1);
        }
        storeRenovationMapper.updateById(storeRenovation);
        return ServerResponseEntity.success("店铺装修详情记录新增成功");
    }

}
