package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.feign.WxMpApiFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinWebAppInfoVo;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowSelectDTO;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowsDTO;
import com.mall4j.cloud.api.user.vo.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.mapper.UserWeixinAccountFollowMapper;
import com.mall4j.cloud.user.model.UserWeixinAccountFollow;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserWeixinAccountFollowService;
import com.mall4j.cloud.user.vo.UserWeixinAccountFollowVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @luzhengxiang
 * @create 2022-04-01 4:24 PM
 **/
@Slf4j
@Service
public class UserWeixinAccountFollowServiceImpl extends ServiceImpl<UserWeixinAccountFollowMapper,UserWeixinAccountFollow> implements UserWeixinAccountFollowService {

    @Autowired
    private UserWeixinAccountFollowMapper userWeixinAccountFollowMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private WxMpApiFeignClient wxMpApiFeignClient;


    /**
     * 粉丝列表
     * @param pageDTO
     * @param dto
     * @return
     */
    @Override
    public PageVO<UserWeixinAccountFollowVo> pageUserFollowList(PageDTO pageDTO, UserWeixinccountFollowSelectDTO dto) {
        PageVO<UserWeixinAccountFollowVo> pageVO= PageUtil.doPage(pageDTO, () -> userWeixinAccountFollowMapper.list(dto));
        for (UserWeixinAccountFollowVo followVo : pageVO.getList()) {
            followVo.setUnFollowTime(Objects.nonNull(followVo.getUpdateTime())?followVo.getUpdateTime():null);
            if (StrUtil.isNotBlank(followVo.getAppId())) {
                ServerResponseEntity<WeixinWebAppInfoVo> response = wxMpApiFeignClient.getWxMpInfo(followVo.getAppId());
                if (response != null && response.isSuccess() && response.getData() != null) {
                    followVo.setWxmpname(response.getData().getName());
                }
            }
        }
        return pageVO;
    }

    /**
     * 粉丝分析
     * @param dto
     * @return
     */
    @Override
    public UserWeixinAccountFollowDataVo userFollowData(UserWeixinccountFollowSelectDTO dto) {
        List<UserWeixinAccountFollowDataListVo> follows=userWeixinAccountFollowMapper.followData(dto);//关注
        follows.stream().forEach(item->{item.setStatus(1);});
        Map<String,UserWeixinAccountFollowDataListVo> followsMap= LambdaUtils.toMap(follows,UserWeixinAccountFollowDataListVo::getTime);

        List<UserWeixinAccountFollowDataListVo> unFollows=userWeixinAccountFollowMapper.unFollowData(dto);//未关注
        unFollows.stream().forEach(item->{item.setStatus(2);});
        Map<String,UserWeixinAccountFollowDataListVo> unFollowsMap= LambdaUtils.toMap(unFollows,UserWeixinAccountFollowDataListVo::getTime);

        //TODO 粉丝活跃数据待确认
        ServerResponseEntity<List<UserWeixinAccountFollowDataListVo>> responseEntity=wxMpApiFeignClient.fansDataByAppId(dto.getStartTime(),dto.getEndTime(),dto.getAppId());
        ServerResponseEntity.checkResponse(responseEntity);
        responseEntity.getData().stream().forEach(item->{item.setStatus(3);});
        Map<String,UserWeixinAccountFollowDataListVo> activeMaps= LambdaUtils.toMap(responseEntity.getData(),UserWeixinAccountFollowDataListVo::getTime);

        List<UserWeixinAccountFollowDataListVo> back=getBack(dto);

        for (UserWeixinAccountFollowDataListVo followDataVo : back) {
            followDataVo.setAppId(dto.getAppId());
            //关注
            if(followsMap.containsKey(followDataVo.getTime()) && followDataVo.getStatus()==1){
                followDataVo.setCount(followsMap.get(followDataVo.getTime()).getCount());
            }
            //取消关注
            if(unFollowsMap.containsKey(followDataVo.getTime()) && followDataVo.getStatus()==2){
                followDataVo.setCount(unFollowsMap.get(followDataVo.getTime()).getCount());
            }
            //粉丝【scrm_biz.weixin_actoin_logs】
            if(activeMaps.containsKey(followDataVo.getTime()) && followDataVo.getStatus()==3){
                followDataVo.setCount(activeMaps.get(followDataVo.getTime()).getCount());
            }
        }

        //返回数据
        UserWeixinAccountFollowDataVo followDataVo=new UserWeixinAccountFollowDataVo();
        followDataVo.setFollows(back);
        followDataVo.setFollowCount(0);
        followDataVo.setUnFollowCount(0);
        //TODO 粉丝活跃数据待确认
        followDataVo.setActiveCount(0);
        followDataVo.setActiveRatio("0");//粉丝总数/关注人数

        if(CollUtil.isNotEmpty(follows)){
            int followCount = follows.stream()
                    .mapToInt(UserWeixinAccountFollowDataListVo::getCount)
                    .sum();
            followDataVo.setFollowCount(followCount);
        }
        if(CollUtil.isNotEmpty(unFollows)){
            int unFollowCount = unFollows.stream()
                    .mapToInt(UserWeixinAccountFollowDataListVo::getCount)
                    .sum();
            followDataVo.setUnFollowCount(unFollowCount);
        }
        if(CollUtil.isNotEmpty(responseEntity.getData())){
            int activeCount = responseEntity.getData().stream()
                    .mapToInt(UserWeixinAccountFollowDataListVo::getCount)
                    .sum();
            followDataVo.setActiveCount(activeCount);

            //计算活跃度
            if(Objects.nonNull(followDataVo.getActiveCount())
                    && Objects.nonNull(followDataVo.getFollowCount())
                    && followDataVo.getActiveCount()>0
                    && followDataVo.getFollowCount()>0
            ){
                BigDecimal activeRatio=NumberUtil.div(followDataVo.getActiveCount(),followDataVo.getFollowCount());
                double ratio=NumberUtil.mul(activeRatio.floatValue(),100);
                if(ratio>100){
                    ratio=100;
                }
                followDataVo.setActiveRatio(NumberUtil.roundStr(ratio,1));
            }else{
                followDataVo.setActiveRatio("0");
            }

        }

        return followDataVo;
    }

    public static void main(String[] strings){
        BigDecimal activeRatio=NumberUtil.div(new BigDecimal(18),new BigDecimal(42));
        double ratio=NumberUtil.mul(activeRatio.floatValue(),100);
        System.out.println(NumberUtil.roundStr(ratio,1));
    }

    private List<UserWeixinAccountFollowDataListVo> getBack(UserWeixinccountFollowSelectDTO dto){
        List<UserWeixinAccountFollowDataListVo> back=new ArrayList<>();
        if(StrUtil.isEmpty(dto.getStartTime()) || StrUtil.isEmpty(dto.getEndTime()) ){
         return back;
        }
        Date startTime=DateUtil.parse(dto.getStartTime(),"yyyy-MM-dd HH:mm:ss");
        Date endTime=DateUtil.parse(dto.getEndTime(),"yyyy-MM-dd HH:mm:ss");
        List<DateTime> dateTimes=DateUtil.rangeToList(startTime,endTime, DateField.DAY_OF_YEAR);//按天
        for (DateTime dateTime : dateTimes) {
            String time=DateUtil.format(dateTime,"yyyy-MM-dd");
            UserWeixinAccountFollowDataListVo follow=new UserWeixinAccountFollowDataListVo();
            follow.setTime(time);
            follow.setCount(0);
            follow.setStatus(1);
            back.add(follow);

            UserWeixinAccountFollowDataListVo unFollow=new UserWeixinAccountFollowDataListVo();
            unFollow.setTime(time);
            unFollow.setCount(0);
            unFollow.setStatus(2);
            back.add(unFollow);

            UserWeixinAccountFollowDataListVo activeFollow=new UserWeixinAccountFollowDataListVo();
            activeFollow.setTime(time);
            activeFollow.setCount(0);
            activeFollow.setStatus(3);
            back.add(activeFollow);

        }
        return back;
    }


    @Override
    public void follow(UserWeixinccountFollowDTO param) {
        UserWeixinAccountFollow dbAccountFollow = userWeixinAccountFollowMapper.getByOpenId(param.getOpenId());

        //判断openid是否存在，不存在新增。 存在的话更新状态
        if (dbAccountFollow == null) {
            UserWeixinAccountFollow userWeixinAccountFollow = mapperFacade.map(param, UserWeixinAccountFollow.class);
            userWeixinAccountFollow.setCreateTime(new Date());
            userWeixinAccountFollowMapper.save(userWeixinAccountFollow);
        } else {
            Date creatTime=new Date();
            Date updateTime=new Date();
            //TODO 取消关注再次关注是否需要更新关注时间
//            if(param.getStatus()==1){
//                creatTime=new Date();
//            }
            userWeixinAccountFollowMapper.updateStatusByOpneId(param.getOpenId(), param.getStatus(), param.getUnionId(),param.getUnFollowTime(),creatTime,updateTime);
        }
        //判断是否有传unionId ,没有传值取数据库中的。
        String unionId = param.getUnionId();
        if (StrUtil.isEmpty(param.getUnionId()) && dbAccountFollow != null && StrUtil.isNotEmpty(dbAccountFollow.getUnionId())) {
            unionId = dbAccountFollow.getUnionId();
        }

        //如果unionId不等于空
//        if (StrUtil.isNotEmpty(unionId)) {
//            syncFollw(unionId);
//        }

    }

    @Override
    public List<String> listUserFollowUpAppId(String unionId) {


        return null;
    }

    @Override
    public void syncFollw(String unionId) {

        UserApiVO userApiVO = userService.getByUnionId(unionId);
        //通过unionId查询当前用户是否注册过。 如果注册过则更新用户的关注信息到crm
        if (userApiVO == null) {
            log.info("通过unionid查询用户信息失败，或者当前用户没有注册。不调用crm同步的任务。");
            return;
        }

        List<UserWeixinAccountFollow> accountFollows = userWeixinAccountFollowMapper.listByUnionId(unionId);

        Map<Integer, UserWeixinAccountFollow> studentMap = accountFollows.stream().collect(Collectors.toMap(UserWeixinAccountFollow::getType, f -> f));

    }

    /**
     * 会员关注获取取消关注的公众号列表
     *
     * @param unionId
     * @return
     */
    @Override
    public List<UserWeixinAccountFollowVo> getUserFollowList(String unionId) {
        if (StrUtil.isBlank(unionId)) return null;
        List<UserWeixinAccountFollowVo> list = userWeixinAccountFollowMapper.getUserFollowList(unionId);
        for (UserWeixinAccountFollowVo followVo : list) {
            if (StrUtil.isNotBlank(followVo.getAppId())) {
                ServerResponseEntity<WeixinWebAppInfoVo> response = wxMpApiFeignClient.getWxMpInfo(followVo.getAppId());
                if (response != null && response.isSuccess() && response.getData() != null) {
                    followVo.setWxmpname(response.getData().getName());
                }
            }
        }
        return list;
    }

    @Override
    public UserWeixinAccountFollowVO getUserFollowByUnionIdAndAppid(String unionId, String appId) {
        return userWeixinAccountFollowMapper.getUserFollowByUnionIdAndAppid(unionId, appId);
    }

    @Override
    public List<String> listFollowUpAppIdByUnionId(String unionId) {
        return userWeixinAccountFollowMapper.listFollowUpAppIdByUnionId(unionId);
    }

    /**
     * 批量处理公众号关注粉丝数据
     */
    @Override
    public void followData(UserWeixinccountFollowsDTO followsDTO) {
        if(CollUtil.isEmpty(followsDTO.getFollowDTOList())){
            log.info("批量处理公众号关注粉丝数据失败，入参数据为空");
            return;
        }
        String appId=followsDTO.getAppId();
        List<UserWeixinAccountFollow> follows=userWeixinAccountFollowMapper.getListByAppId(appId);
        if(CollUtil.isEmpty(follows)){
            //全量
            List<UserWeixinAccountFollow> addDatas=new ArrayList<>();
            for (UserWeixinccountFollowDTO followDTO : followsDTO.getFollowDTOList()) {
                UserWeixinAccountFollow follow = mapperFacade.map(followDTO, UserWeixinAccountFollow.class);
                if(Objects.isNull(follow.getCreateTime())){
                    follow.setCreateTime(new Date());
                }
                follow.setIsDelete(0);
                addDatas.add(follow);
            }
            log.info("全量-批量处理公众号关注粉丝数据,appid:{} 条数:{}",appId,addDatas.size());
            this.saveBatch(addDatas);
        }else{
            //增量
            Map<String,UserWeixinAccountFollow> followMaps=follows.stream().collect(Collectors.toMap(UserWeixinAccountFollow::getOpenId, s->s, (v1, v2)->v2));
            List<UserWeixinAccountFollow> addDatas=new ArrayList<>();
            for (UserWeixinccountFollowDTO followDTO : followsDTO.getFollowDTOList()) {
                if(!followMaps.containsKey(followDTO.getOpenId())){
                    UserWeixinAccountFollow follow = mapperFacade.map(followDTO, UserWeixinAccountFollow.class);
                    if(Objects.isNull(follow.getCreateTime())){
                        follow.setCreateTime(new Date());
                    }
                    follow.setIsDelete(0);
                    addDatas.add(follow);
                }
            }
            log.info("增量-批量处理公众号关注粉丝数据,appid:{} 条数:{}",appId,addDatas.size());
            if(CollUtil.isNotEmpty(addDatas)){
                this.saveBatch(addDatas);
            }
        }
    }
}
