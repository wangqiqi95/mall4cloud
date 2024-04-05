package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AdvanceActivityDTO;
import com.mall4j.cloud.group.dto.AdvanceActivityPageDTO;
import com.mall4j.cloud.group.dto.AdvanceCommodityDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.model.AdvanceActivity;
import com.mall4j.cloud.group.model.AdvanceCommodity;
import com.mall4j.cloud.group.model.OpenScreenAd;
import com.mall4j.cloud.group.model.RegisterActivity;
import com.mall4j.cloud.group.service.AdvanceActivityBizService;
import com.mall4j.cloud.group.service.AdvanceActivityService;
import com.mall4j.cloud.group.service.AdvanceCommodityService;
import com.mall4j.cloud.group.vo.AdvanceActivityListVO;
import com.mall4j.cloud.group.vo.AdvanceActivityVO;
import com.mall4j.cloud.group.vo.AdvanceCommodityVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AdvanceActivityBizServiceImpl implements AdvanceActivityBizService {
    @Resource
    private AdvanceActivityService advanceActivityService;
    @Resource
    private AdvanceCommodityService advanceCommodityService;
    @Override
    public ServerResponseEntity<Void> saveOrUpdateAdvanceActivity(AdvanceActivityDTO param) {
        //todo 活动商品加入商品池
        AdvanceActivity advanceActivity = BeanUtil.copyProperties(param, AdvanceActivity.class);
        List<AdvanceCommodityDTO> commodities = param.getCommodities();

        List<AdvanceCommodity> advanceCommodities = Convert.toList(AdvanceCommodity.class, commodities);
        AdvanceCommodity max = advanceCommodities.stream().max(Comparator.comparing(AdvanceCommodity::getActivityPrice)).get();
        AdvanceCommodity min = advanceCommodities.stream().min(Comparator.comparing(AdvanceCommodity::getActivityPrice)).get();
        advanceActivity.setActivityMaxPrice(max.getActivityPrice());
        advanceActivity.setActivityMinPrice(min.getActivityPrice());

        advanceActivityService.save(advanceActivity);
        Integer id = advanceActivity.getId();

        List<AdvanceCommodity> commodityList = advanceCommodities.stream().peek(temp -> temp.setAdvanceId(id)).collect(Collectors.toList());
        advanceCommodityService.saveBatch(commodityList);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<AdvanceActivityVO> detail(Integer id) {
        AdvanceActivity advanceActivity = advanceActivityService.getById(id);
        AdvanceActivityVO advanceActivityVO = BeanUtil.copyProperties(advanceActivity, AdvanceActivityVO.class);

        List<AdvanceCommodity> list = advanceCommodityService.list(new LambdaQueryWrapper<AdvanceCommodity>().eq(AdvanceCommodity::getAdvanceId, id));
        List<AdvanceCommodityVO> advanceCommodityVOS = Convert.toList(AdvanceCommodityVO.class, list);

        advanceActivityVO.setCommodities(advanceCommodityVOS);
        return ServerResponseEntity.success(advanceActivityVO);
    }

    @Override
    public ServerResponseEntity<PageVO<AdvanceActivityListVO>> page(AdvanceActivityPageDTO param) {
        Integer activityStatus = param.getStatus();
        Long commodityId = param.getCommodityId();
        Long shopId = param.getShopId();
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        Date date = new Date();

        LambdaQueryWrapper<AdvanceActivity> wrapper = new LambdaQueryWrapper<>();
        if (null != commodityId){
            wrapper.like(AdvanceActivity::getCommodityId, commodityId);
        }
        if (null != shopId){
            wrapper.and(wrapper1->wrapper1.likeRight(AdvanceActivity::getApplyShopIds,shopId+",")
                    .or(wrapper2->wrapper2.like(AdvanceActivity::getApplyShopIds,","+shopId+","))
                    .or(wrapper3->wrapper3.eq(AdvanceActivity::getApplyShopIds,"-1"))
                    .or(wrapper4->wrapper4.eq(AdvanceActivity::getApplyShopIds,shopId)));
        }
        if (null != activityStatus){
            if (ActivityStatusEnums.NOT_ENABLED.getCode().equals(activityStatus)) {
                wrapper.eq(AdvanceActivity::getStatus, activityStatus);
            } else if (ActivityStatusEnums.IN_PROGRESS.getCode().equals(activityStatus)) {
                wrapper.le(AdvanceActivity::getAdvanceBeginTime, date).gt(AdvanceActivity::getAdvanceEndTime, date).ne(AdvanceActivity::getStatus, ActivityStatusEnums.NOT_ENABLED.getCode());
            } else if (ActivityStatusEnums.NOT_START.getCode().equals(activityStatus)) {
                wrapper.gt(AdvanceActivity::getAdvanceBeginTime, date).ne(AdvanceActivity::getStatus, ActivityStatusEnums.NOT_ENABLED.getCode());
            } else if (ActivityStatusEnums.END.getCode().equals(activityStatus)) {
                wrapper.le(AdvanceActivity::getAdvanceEndTime, date).ne(AdvanceActivity::getStatus, ActivityStatusEnums.NOT_ENABLED.getCode());
            }
        }
        wrapper.eq(AdvanceActivity::getDeleted,0);
        wrapper.orderByDesc(AdvanceActivity::getCreateTime);

        IPage<AdvanceActivity> page = new Page<>(pageNum, pageSize);

        IPage<AdvanceActivity> openScreenAdIPage = advanceActivityService.page(page, wrapper);

        List<AdvanceActivity> records = openScreenAdIPage.getRecords();

        List<AdvanceActivityListVO> list = Convert.toList(AdvanceActivityListVO.class, records);

        List<AdvanceActivityListVO> resultList = list.stream().peek(temp -> {
                    Date activityEndTime = temp.getAdvanceEndTime();
                    Integer tempActivityStatus = temp.getStatus();
                    Date activityBeginTime = temp.getAdvanceBeginTime();
                    //todo 商品信息添加

                    if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                        if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                            temp.setStatus(ActivityStatusEnums.IN_PROGRESS.getCode());
                        } else if (date.compareTo(activityBeginTime) < 0) {
                            temp.setStatus(ActivityStatusEnums.NOT_START.getCode());
                        } else if (date.compareTo(activityEndTime) > 0) {
                            temp.setStatus(ActivityStatusEnums.END.getCode());
                        }
                    }
                }
        ).collect(Collectors.toList());

        PageVO<AdvanceActivityListVO> pageVO = new PageVO<>();
        pageVO.setPages((int)openScreenAdIPage.getPages());
        pageVO.setList(resultList);
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<Void> enable(Integer id) {
        advanceActivityService.update(new LambdaUpdateWrapper<AdvanceActivity>()
                .set(AdvanceActivity::getStatus,1)
                .eq(AdvanceActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> disable(Integer id) {
        advanceActivityService.update(new LambdaUpdateWrapper<AdvanceActivity>()
                .set(AdvanceActivity::getStatus,1)
                .eq(AdvanceActivity::getId,id));
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> delete(Integer id) {
        advanceActivityService.update(new LambdaUpdateWrapper<AdvanceActivity>()
                .set(AdvanceActivity::getDeleted,1)
                .eq(AdvanceActivity::getId,id));
        return ServerResponseEntity.success();
    }
}
