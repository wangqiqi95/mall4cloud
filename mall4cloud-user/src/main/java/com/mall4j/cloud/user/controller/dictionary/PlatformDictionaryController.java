package com.mall4j.cloud.user.controller.dictionary;

import com.mall4j.cloud.common.constant.DictionaryEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.service.dictionary.DictionaryService;
import com.mall4j.cloud.user.vo.dictionary.DictionaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ben
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/ua/dictionary")
@Api(tags = "字典查询API")
public class PlatformDictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation("查询高价值会员活动兑换类型字典接口")
    @GetMapping("choose/member/event/exchange/type/dictionary")
    public ServerResponseEntity<List<DictionaryVO>> getChooseMemberEventExchangeTypeDictionary(){
        return dictionaryService.getDictionaryVOList(DictionaryEnum.CHOOSE_MEMBER_EVENT_EXCHANGE_TYPE);
    }


    @ApiOperation("查询高价值会员活动兑换记录发货状态字典接口")
    @GetMapping("choose/member/event/exchange/record/delivery/status/dictionary")
    public ServerResponseEntity<List<DictionaryVO>> getChooseMemberEventExchangeRecordDeliveryStatusDictionary(){
        return dictionaryService.getDictionaryVOList(DictionaryEnum.CHOOSE_MEMBER_EVENT_EXCHANGE_RECORD_DELIVERY_STATUS);
    }

    @ApiOperation("查询标签组分类字典接口")
    @GetMapping("tag/group/relation/dictionary")
    public ServerResponseEntity<List<DictionaryVO>> getTagGroupType(){
        return dictionaryService.getDictionaryVOList(DictionaryEnum.TAG_GROUP_TYPE);
    }

}
