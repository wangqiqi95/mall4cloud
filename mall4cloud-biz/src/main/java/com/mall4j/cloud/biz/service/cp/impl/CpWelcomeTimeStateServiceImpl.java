package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.mall4j.cloud.biz.dto.cp.CpWelcomeTimeStateDTO;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.mapper.cp.CpWelcomeTimeStateMapper;
import com.mall4j.cloud.biz.model.cp.CpWelcomeTimeState;
import com.mall4j.cloud.biz.service.cp.CpWelcomeTimeStateService;
import com.mall4j.cloud.biz.vo.cp.CpWelcomeTimeStateVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.common.util.LambdaUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 个人欢迎语 分时段欢迎语列表
 *
 * @author FrozenWatermelon
 * @date 2023-11-06 17:02:34
 */
@Service
public class CpWelcomeTimeStateServiceImpl implements CpWelcomeTimeStateService {

    @Autowired
    private CpWelcomeTimeStateMapper cpWelcomeTimeStateMapper;

    @Override
    public PageVO<CpWelcomeTimeState> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpWelcomeTimeStateMapper.list());
    }

    @Override
    public CpWelcomeTimeState getById(Long id) {
        return cpWelcomeTimeStateMapper.getById(id);
    }

    @Override
    public void save(CpWelcomeTimeState cpWelcomeTimeState) {
        cpWelcomeTimeStateMapper.save(cpWelcomeTimeState);
    }

    @Override
    public void update(CpWelcomeTimeState cpWelcomeTimeState) {
        cpWelcomeTimeStateMapper.update(cpWelcomeTimeState);
    }

    @Override
    public void deleteById(Long id) {
        cpWelcomeTimeStateMapper.deleteById(id);
    }

    @Override
    public List<CpWelcomeTimeStateVO> listByWellId(Long id) {
        return cpWelcomeTimeStateMapper.listByWellId(id);
    }

    @Override
    public void deleteByWelId(Long id) {
        cpWelcomeTimeStateMapper.deleteByWelId(id);
    }

    /**
     * 校验接待欢迎语分时段冲突
     * @param filterAttachMents
     */
    @Override
    public void checkAttachMentTime(List<CpWelcomeTimeStateDTO>  filterAttachMents){
        for (int i = 0; i < filterAttachMents.size() - 1; i++) {
            for (int j = i + 1; j < filterAttachMents.size(); j++) {
                int finalJ = j;
                List<String> weeks= Arrays.asList(filterAttachMents.get(i).getTimeCycle().split(","));
                List<String> weeks1=Arrays.asList(filterAttachMents.get(finalJ).getTimeCycle().split(","));
                long workCycleSum = weeks.stream()
                        .filter(one -> weeks1.stream()
                                .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                if (workCycleSum > 0) {
                    String beginTime1 = filterAttachMents.get(i).getTimeStart();
                    String endTime1 = filterAttachMents.get(i).getTimeEnd();
                    String beginTime2 = filterAttachMents.get(finalJ).getTimeStart();
                    String endTime2 = filterAttachMents.get(finalJ).getTimeEnd();
                    if (DateUtils.match(beginTime1, endTime1, beginTime2, endTime2)) {
                        throw new LuckException("分时段欢迎语冲突，时间段："+(beginTime1+"~"+endTime1));
                    }
                }
            }
        }
    }



    public static void main(String[] s ){
        String json="[{\"attachMentBaseDTOS\":[],\"index\":1,\"timeStart\":\"00:00\",\"timeEnd\":\"23:30\",\"id\":\"\",\"slogan\":\"${客户昵称}${员工姓名}周一周二${客户昵称}${员工姓名}\",\"timeCycle\":\"2,3\",\"currentTab\":0},{\"attachMentBaseDTOS\":[{\"msgType\":\"video\",\"index\":1,\"url\":\"\",\"video\":\"\",\"appId\":\"\",\"page\":\"\",\"miniProgramTitle\":\"\",\"localUrl\":\"/2023/12/13/047b45c7da4f40d4bf475d12c3d8dd54\",\"mediaId\":\"30k1jwOeUMbkd-EIcMQLIcHpw1nzP-184sAaFdxxTZblWvD8ft5KD-qzXyLGnity4\",\"materialId\":\"\"}],\"index\":2,\"timeStart\":\"00:00\",\"timeEnd\":\"22:30\",\"id\":\"\",\"slogan\":\"${客户昵称}${员工姓名}周三周四\",\"timeCycle\":\"4,5\",\"currentTab\":0},{\"attachMentBaseDTOS\":[{\"msgType\":\"link\",\"index\":1,\"url\":\"https://baijiahao.baidu.com/s?id=1789770865282030461\",\"video\":\"\",\"appId\":\"\",\"page\":\"\",\"miniProgramTitle\":\"出现降雪致多地高速交\",\"localUrl\":\"/2023/12/05/5bd81da2083149d9a1f778d04718cf4e\",\"mediaId\":\"\",\"materialId\":\"\",\"desc\":\"气象资料显示，截至2月2日8时，青海全省268个测站出现降雪，西宁及海东大部地区出现小到中雪，民和、乐都部分乡镇出现大到暴雪，降雪中心在民和县李二堡镇张家湾村，降雪量为7.5毫米，积雪深度5厘米。此次降雪主要集中在西宁和海东、海北、海南等地。\\n此次降雪也导致青海省多地高速公路交通管制，据青海路网显示，G6(北京至西藏)高速马平路段、平西路段，西过境路段、扎倒路段、G0611(张掖至汶川)高速平阿路段K335+600至K378+800双向、G0612(西宁至和田)高速南绕城等多个路段因降雪路面积雪结冰，不具备通行条件，公路路政、养护部门正在现场清雪保通。公安交警部门对(马场垣匝道收费站入口)双向、(民和收费站入口)双向、(乐都收费站入口)等多个收费站双向均实行交通管制。\",\"picUrl\":\"https://wework.qpic.cn/wwpic3az/13222__HrJDJfGQ9-iTpF_1706866144/0\"}],\"index\":3,\"timeStart\":\"04:30\",\"timeEnd\":\"17:00\",\"id\":\"\",\"slogan\":\"${客户昵称}${员工姓名}周五周六17点前\",\"timeCycle\":\"6,7\",\"currentTab\":0},{\"attachMentBaseDTOS\":[{\"index\":1,\"msgType\":\"miniprogram\",\"url\":\"\",\"video\":\"\",\"appId\":\"wx56536688c9fb8986\",\"page\":\"pages/home/home\",\"miniProgramTitle\":\"小程序复试阶段\",\"localUrl\":\"/2024/01/15/464326e204034cc5952abec761d38f6a\",\"mediaId\":\"3XL6lv_tktRzpYZhnAFnBfru6BjH3mgX4r-Ua3KzkwhRvdATeSiR8MgpCE1QsOkbZ\",\"materialId\":\"\"}],\"index\":4,\"timeStart\":\"17:30\",\"timeEnd\":\"20:30\",\"id\":\"\",\"slogan\":\"${客户昵称}${员工姓名}周五周六17:30后\",\"timeCycle\":\"6,7\",\"currentTab\":0}]";

        List<CpWelcomeTimeStateDTO>  filterAttachMents= JSONArray.parseArray(json,CpWelcomeTimeStateDTO.class);

        CpWelcomeTimeStateServiceImpl cpWelcomeTimeStateService=new CpWelcomeTimeStateServiceImpl();
        cpWelcomeTimeStateService.checkAttachMentTime(filterAttachMents);


    }


}
