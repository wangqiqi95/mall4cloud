package com.mall4j.cloud.biz.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.dto.RenovationBurialPointDTO;
import com.mall4j.cloud.api.platform.feign.StoreRenovationClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.MicroPageBurialPointRecordDTO;
import com.mall4j.cloud.biz.dto.MicroPageBurialPointRecordPageDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.MicroPageBurialPointStatistics;
import com.mall4j.cloud.biz.service.MicroPageBurialPointRecordService;
import com.mall4j.cloud.biz.vo.BurialPointStatisticsVO;
import com.mall4j.cloud.biz.vo.MicroPageBurialPointRecordVO;
import com.mall4j.cloud.common.database.dto.TimeBetweenDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 微页面数据埋点服务实现类
 */
@Service
@Slf4j
@AllArgsConstructor
public class MicroPageBurialPointRecordServiceImpl extends ServiceImpl<MicroPageBurialPointRecordMapper, MicroPageBurialPointRecord> implements MicroPageBurialPointRecordService {

    private final WxConfig wxConfig;
    private final UserFeignClient userFeignClient;
    private final StoreRenovationClient storeRenovationClient;
    private final MapperFacade mapperFacade;

    @Override
    public PageVO<MicroPageBurialPointRecordVO> page(MicroPageBurialPointRecordPageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> baseMapper.selectBurialPointRecordPage(pageDTO));
    }

    @Override
    public List<BurialPointStatisticsVO> statisticsBurialPointRecords(Long renovationId, TimeBetweenDTO dto) {
        List<MicroPageBurialPointStatistics> records = baseMapper.selectBurialPointRecords(renovationId, dto);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
        Set<String> dates = new HashSet<>();
        records.forEach(record -> {
            record.setDate(dateFormat.format(record.getBrowseTime()));
            dates.add(record.getDate());
        });

        Map<String, Long> browseNumberMap = LambdaUtils.group(records, MicroPageBurialPointStatistics::getDate, Collectors.counting());
        Map<String, Map<String, List<MicroPageBurialPointStatistics>>> browsePeopleNumberMap = LambdaUtils.group(records, MicroPageBurialPointStatistics::getDate,
                Collectors.groupingBy(MicroPageBurialPointStatistics::getUnionId));

        return dates.stream().sorted().map(date -> {
            BurialPointStatisticsVO statisticsVO = new BurialPointStatisticsVO();
            statisticsVO.setDate(date);

            Long browseNumber = browseNumberMap.get(date);
            statisticsVO.setBrowseNum(Objects.isNull(browseNumber) ? 0L : browseNumber);

            Map<String, List<MicroPageBurialPointStatistics>> browsePeopleMap = browsePeopleNumberMap.get(date);
            statisticsVO.setBrowsePeopleNum(Objects.isNull(browsePeopleMap) ? 0 : browsePeopleMap.keySet().size());
            return statisticsVO;
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void saveBurialPoint(MicroPageBurialPointRecordDTO dto) throws WxErrorException {
        String code = dto.getCode();
        log.info("上报微页面埋点数据，微信Code：【{}】", code);

        int incrBrowsePeopleNum = 0;
        MicroPageBurialPointRecord burialPointRecord = mapperFacade.map(dto, MicroPageBurialPointRecord.class);
        if (StrUtil.isNotBlank(code)) {
            code = code.replace("\"", "");
            WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(code);
            log.info("小程序登录，获取用户Session：【{}】", Objects.nonNull(session) ? JSON.toJSONString(session) : "未获取到Session信息");
            String unionId = session.getUnionid();
            UserApiVO user = userFeignClient.getUserByUnionId(unionId).getData();
            log.info("UnionID解析到的用户消息为：{}", Json.toJsonString(user));
            burialPointRecord.setUnionId(unionId);
            if (Objects.nonNull(user)) {
                burialPointRecord.setNikeName(user.getNickName());
                burialPointRecord.setNotesName(user.getCustomerName());
                burialPointRecord.setMobile(user.getUserMobile());
            }

            // 根据UnionID来判断是否需要累加浏览人数
            Integer count = baseMapper.selectCountByUnionId(dto.getRenovationId(), unionId);
            if (count == 0) {
                incrBrowsePeopleNum = 1;
            }
        }
        burialPointRecord.setBrowseTime(new Date());

        int rows = baseMapper.insert(burialPointRecord);
        if (rows == 0) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 处理微页面主信息上的埋点数据
        RenovationBurialPointDTO renovationDTO = new RenovationBurialPointDTO();
        renovationDTO.setRenovationId(dto.getRenovationId());
        renovationDTO.setIncrBrowseNum(1);
        renovationDTO.setIncrBrowsePeopleNum(incrBrowsePeopleNum);
        storeRenovationClient.editRenovationBurialPoint(renovationDTO);
    }
}
