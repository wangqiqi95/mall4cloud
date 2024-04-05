package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.platform.vo.TentacleVo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.mapper.TentacleContentMapper;
import com.mall4j.cloud.platform.model.Tentacle;
import com.mall4j.cloud.platform.model.TentacleContent;
import com.mall4j.cloud.platform.service.TentacleContentService;
import com.mall4j.cloud.platform.service.TentacleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 触点内容信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Service
public class TentacleContentServiceImpl implements TentacleContentService {

    @Autowired
    private TentacleContentMapper tentacleContentMapper;

    @Autowired
    private TentacleService tentacleService;


    @Override
    public PageVO<TentacleContent> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> tentacleContentMapper.list());
    }

    @Override
    public TentacleContent getById(Long id) {
        return tentacleContentMapper.getById(id);
    }

    @Override
    public void save(TentacleContent tentacleContent) {
        tentacleContentMapper.save(tentacleContent);
    }

    @Override
    public void update(TentacleContent tentacleContent) {
        tentacleContentMapper.update(tentacleContent);
    }

    @Override
    public void deleteById(Long id) {
        tentacleContentMapper.deleteById(id);
    }

    @Override
    public TentacleContentVO findOrCreateByContent(TentacleContentDTO dto) {
        Tentacle tentacle = tentacleService.findOrCreate(dto.getBusinessId(), dto.getTentacleType());

        TentacleContent tentacleContent = tentacleContentMapper.findByTentacleIdAndContentType(tentacle.getId(), dto.getContentType());
        if (null != tentacleContent && tentacleContent.getCreateTime().getTime() > DateUtil.beginOfDay(new Date()).getTime()){
            TentacleContentVO vo = new TentacleContentVO();
            BeanUtils.copyProperties(tentacleContent, vo);
            return vo;
        }
        tentacleContent = new TentacleContent();
        BeanUtils.copyProperties(dto, tentacleContent);
        return saveTentacleContent(tentacleContent, tentacle);
    }

    @Override
    public TentacleContentVO findByTentacleNo(String tentacleNo) {
        TentacleContent tentacleContent = tentacleContentMapper.findByTentacleNo(tentacleNo);
        if (null == tentacleContent){
            return null;
        }
        Tentacle tentacle = tentacleService.getById(tentacleContent.getTentacleId());
        TentacleContentVO vo = new TentacleContentVO();
        BeanUtils.copyProperties(tentacleContent, vo);
        TentacleVo tentacleVo = new TentacleVo();
        BeanUtils.copyProperties(tentacle, tentacleVo);
        vo.setTentacle(tentacleVo);
        return vo;
    }

    private TentacleContentVO saveTentacleContent(TentacleContent tc, Tentacle tentacle){
        tc.setOrgId(tentacle.getOrgId());
        tc.setTentacleId(tentacle.getId());
        tc.setContentTitle(tentacle.getTentacleName());
        tc.setStatus(1);
        tc.setTentacleNo(generateTentacleNo(tentacle.getBusinessId()));
        tentacleContentMapper.save(tc);
        TentacleContentVO vo = new TentacleContentVO();
        BeanUtils.copyProperties(tc, vo);
        return vo;
    }

    private String generateTentacleNo(Long uid){
        String head = "";
//        switch (tentacleType){
//            case 1:
//                head = "MD";
//                break;
//            case 2:
//                head = "HY";
//                break;
//            case 3:
//                head = "WK";
//                break;
//            case 4:
//                head = "DG";
//                break;
//            default:
//                break;
//        }
        return head + DateUtil.format(new Date(), "yyyyMMddHHmmss").substring(2) + String.valueOf(10000000 + uid).substring(1);
    }


}
