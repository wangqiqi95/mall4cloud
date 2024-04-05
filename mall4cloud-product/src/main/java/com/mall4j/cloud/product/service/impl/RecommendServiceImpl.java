package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.app.RecommendDetailVO;
import com.mall4j.cloud.common.product.vo.app.RecommendResourceVO;
import com.mall4j.cloud.common.product.vo.app.RecommendVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.RecommendAdminDTO;
import com.mall4j.cloud.product.dto.RecommendAdminPageParamsDTO;
import com.mall4j.cloud.product.dto.RecommendDTO;
import com.mall4j.cloud.product.mapper.RecommendCollectMapper;
import com.mall4j.cloud.product.mapper.RecommendMapper;
import com.mall4j.cloud.product.model.Recommend;
import com.mall4j.cloud.product.model.RecommendCate;
import com.mall4j.cloud.product.model.RecommendCollect;
import com.mall4j.cloud.product.model.RecommendPraise;
import com.mall4j.cloud.product.model.RecommendResource;
import com.mall4j.cloud.product.service.RecommendCateService;
import com.mall4j.cloud.product.service.RecommendCollectService;
import com.mall4j.cloud.product.service.RecommendPraiseService;
import com.mall4j.cloud.product.service.RecommendResourceService;
import com.mall4j.cloud.product.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 种草信息
 *
 * @author cg
 */
@Slf4j
@Service
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend> implements RecommendService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private RecommendResourceService recommendResourceService;

    @Autowired
    private RecommendPraiseService recommendPraiseService;

    @Autowired
    private RecommendCollectService recommendCollectService;

    @Resource
    private RecommendCollectMapper recommendCollectMapper;

    @Autowired
    private RecommendCateService recommendCateService;

    @Resource
    private RecommendMapper recommendMapper;

    @Override
    public PageVO<RecommendVO> pageList(PageDTO pageDTO, Integer type, String title) {
        if (type != 1 && type != 2) {
            throw new LuckException("tab类型参数异常");
        }
        Long userId = AuthUserContext.get().getUserId();
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        List<RecommendVO> result = recommendMapper.pageList(pageAdapter, type, userId, title);
        if (!CollectionUtils.isEmpty(result)) {
            // 填充是否点赞是否收藏字段
            fillIsPraise(result, userId);
            fillIsCollect(result, userId);
        }

        PageVO<RecommendVO> pageVO = new PageVO<>();
        pageVO.setList(result);
        pageVO.setTotal(CollectionUtils.isEmpty(result) ? 0 : recommendMapper.pageListCount());
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public PageVO<RecommendVO> adminPageList(PageDTO pageDTO, RecommendAdminPageParamsDTO params) {
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        List<RecommendVO> result = recommendMapper.adminPageList(pageAdapter, params);
        PageVO<RecommendVO> pageVO = new PageVO<>();
        pageVO.setList(result);
        pageVO.setTotal(CollectionUtils.isEmpty(result) ? 0 : recommendMapper.adminPageListCount(params));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }


    private void fillIsPraise(List<RecommendVO> result, Long userId) {
        List<Long> collect = result.stream().map(RecommendVO::getRecommendId).collect(Collectors.toList());
        List<RecommendPraise> list = recommendPraiseService.list(new LambdaQueryWrapper<RecommendPraise>().eq(RecommendPraise::getUserId, userId)
                .in(RecommendPraise::getRecommendId, collect).eq(RecommendPraise::getCancel, 0));
        if (!CollectionUtils.isEmpty(list)) {
            // 匹配对应的种草
            for (RecommendPraise recommendPraise : list) {
                for (RecommendVO recommendVO : result) {
                    if (recommendVO.getRecommendId().equals(recommendPraise.getRecommendId())) {
                        recommendVO.setIsPraise(1);
                    }
                }
            }
        }
    }

    private void fillIsCollect(List<RecommendVO> result, Long userId) {
        List<Long> collect = result.stream().map(RecommendVO::getRecommendId).collect(Collectors.toList());
        List<RecommendCollect> list = recommendCollectService.list(new LambdaQueryWrapper<RecommendCollect>().eq(RecommendCollect::getUserId, userId)
                .in(RecommendCollect::getRecommendId, collect).eq(RecommendCollect::getCancel, 0));
        if (!CollectionUtils.isEmpty(list)) {
            // 匹配对应的种草
            for (RecommendCollect recommendCollect : list) {
                for (RecommendVO recommendVO : result) {
                    if (recommendVO.getRecommendId().equals(recommendCollect.getRecommendId())) {
                        recommendVO.setIsCollect(1);
                    }
                }
            }
        }
    }

    private void fillIsPraise(RecommendDetailVO result, Long userId) {
        Long recommendId = result.getRecommendId();
        RecommendPraise praise = recommendPraiseService.getOne(new LambdaQueryWrapper<RecommendPraise>().eq(RecommendPraise::getUserId, userId)
                .eq(RecommendPraise::getRecommendId, recommendId).eq(RecommendPraise::getCancel, 0));
        if (praise != null) {
            result.setIsPraise(1);
        }
    }

    private void fillIsCollect(RecommendDetailVO result, Long userId) {
        Long recommendId = result.getRecommendId();
        RecommendCollect collect = recommendCollectService.getOne(new LambdaQueryWrapper<RecommendCollect>().eq(RecommendCollect::getUserId, userId)
                .eq(RecommendCollect::getRecommendId, recommendId).eq(RecommendCollect::getCancel, 0));
        if (collect != null) {
            result.setIsCollect(1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void praise(Long recommendId, Integer type) {
        Long userId = AuthUserContext.get().getUserId();
        RecommendPraise recommendPraise = recommendPraiseService.getOne(new LambdaQueryWrapper<RecommendPraise>()
                .eq(RecommendPraise::getRecommendId, recommendId).eq(RecommendPraise::getUserId, userId));

        LambdaUpdateWrapper<Recommend> wrapper = new LambdaUpdateWrapper<>();
        if (type == 1) {
            // 点赞总数+1
            wrapper.eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                    .setSql("praise_count = praise_count + 1");
            // 入库点赞记录
            if (recommendPraise == null) {
                recommendPraise = new RecommendPraise();
                recommendPraise.setCancel(0);
                recommendPraise.setUserId(userId);
                recommendPraise.setRecommendId(recommendId);
                recommendPraiseService.save(recommendPraise);
            } else {
                // 修改点赞记录
                recommendPraiseService.update(new LambdaUpdateWrapper<RecommendPraise>()
                        .eq(RecommendPraise::getRecommendPraiseId, recommendPraise.getRecommendPraiseId())
                        .set(RecommendPraise::getCancel, 0));
            }
        } else if (type == 0) {
            if (recommendPraise == null) {
                // 无点赞记录 不存在取消点赞
                log.error("取消点赞异常 userId = {} recommendId = {}", userId, recommendId);
                return;
            }
            // 点赞总数-1
            wrapper.eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                    .setSql("praise_count = praise_count - 1");
            // 修改点赞记录
            recommendPraiseService.update(new LambdaUpdateWrapper<RecommendPraise>()
                    .eq(RecommendPraise::getRecommendPraiseId, recommendPraise.getRecommendPraiseId())
                    .set(RecommendPraise::getCancel, 1));
        }
        update(wrapper);
    }

    @Override
    public void read(Long recommendId) {
        LambdaUpdateWrapper<Recommend> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                .setSql("read_count = read_count + 1");
        update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void appUpdate(RecommendDTO recommendDTO) {
        Long recommendId = recommendDTO.getRecommendId();
        if (recommendId == null) {
            throw new LuckException("参数异常");
        }
        Long userId = AuthUserContext.get().getUserId();
        Recommend recommend = getOne(new LambdaQueryWrapper<Recommend>().eq(Recommend::getRecommendId, recommendId)
                .eq(Recommend::getUserId, userId).eq(Recommend::getDisable, 0));
        if (recommend == null) {
            log.error("app修改种草信息异常 recommendId = {} userId = {} ", recommendId, userId);
            throw new LuckException("参数异常");
        }

        // 清理旧信息
        recommendResourceService.update(new LambdaUpdateWrapper<RecommendResource>()
                .eq(RecommendResource::getRecommendId, recommendId).set(RecommendResource::getDisable, 1));

        // 更新种草主信息
        Recommend update = new Recommend();
        BeanUtils.copyProperties(recommendDTO, update);
        update.setStatus(1);

        // 图片视频更新
        List<RecommendResource> recommendResources = getResources(recommendDTO);
        recommendResources.forEach(recommendResource -> recommendResource.setRecommendId(update.getRecommendId()));
        recommendResourceService.saveBatch(recommendResources);

        updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(RecommendAdminDTO recommendDTO) {
        Long recommendId = recommendDTO.getRecommendId();
        if (recommendId == null) {
            throw new LuckException("参数异常");
        }

        // 清理旧信息
        recommendResourceService.update(new LambdaUpdateWrapper<RecommendResource>()
                .eq(RecommendResource::getRecommendId, recommendId).set(RecommendResource::getDisable, 1));

        // 更新种草主信息 - 不需要变更状态
        Recommend update = new Recommend();
        BeanUtils.copyProperties(recommendDTO, update);
        update.setStatus(2);

        // 种草分类name
        RecommendCate byId = recommendCateService.getById(recommendDTO.getRecommendCateId());
        if (byId != null) {
            update.setCateName(byId.getName());
        }

        // 图片视频更新
        List<RecommendResource> recommendResources = getResources(recommendDTO);
        recommendResources.forEach(recommendResource -> recommendResource.setRecommendId(update.getRecommendId()));
        recommendResourceService.saveBatch(recommendResources);

        updateById(update);
    }

    @Override
    public void examine(Long recommendId, Integer status) {
        if (status != 2 && status != 3) {
            throw new LuckException("审核种草异常");
        }
        this.update(new LambdaUpdateWrapper<Recommend>().eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                .set(Recommend::getStatus, status));
    }

    @Override
    public void adminDelete(Long recommendId) {
        Long userId = AuthUserContext.get().getUserId();
        log.info("admin用户删除种草 recommendId = {} userId = {}", recommendId, userId);
        this.update(new LambdaUpdateWrapper<Recommend>().eq(Recommend::getRecommendId, recommendId)
                .eq(Recommend::getUserType, 1).set(Recommend::getDisable, 1));
    }

    @Override
    public void appDelete(Long recommendId) {
        Long userId = AuthUserContext.get().getUserId();
        Recommend recommend = getOne(new LambdaQueryWrapper<Recommend>().eq(Recommend::getRecommendId, recommendId).eq(Recommend::getUserId, userId)
                .eq(Recommend::getUserType, 2).eq(Recommend::getDisable, 0));
        if (recommend == null) {
            log.error("app用户删除种草异常 recommendId = {} userId = {}", recommendId, userId);
            return;
        }
        this.update(new LambdaUpdateWrapper<Recommend>().eq(Recommend::getRecommendId, recommendId).eq(Recommend::getUserId, userId)
                .eq(Recommend::getUserType, 2).set(Recommend::getDisable, 1));
    }

    @Override
    public RecommendDetailVO detail(Long recommendId) {
        Recommend recommend = this.getOne(new LambdaQueryWrapper<Recommend>().eq(Recommend::getRecommendId, recommendId)
                .eq(Recommend::getDisable, 0));
        RecommendDetailVO result = new RecommendDetailVO();
        if (recommend == null) {
            return result;
        }

        // 填充分类属性
        if (recommend.getRecommendCateId() != null) {
            RecommendCate cate = recommendCateService.getById(recommend.getRecommendCateId());
            if (cate != null) {
                result.setCateName(cate.getName());
            }
        }

        // 填充是否点赞是否收藏字段
        BeanUtils.copyProperties(recommend, result);
        Long userId = AuthUserContext.get().getUserId();
        fillIsPraise(result, userId);
        fillIsCollect(result, userId);

        // 填充关联图片视频
        List<RecommendResource> resources = recommendResourceService.list(new LambdaQueryWrapper<RecommendResource>()
                .eq(RecommendResource::getRecommendId, recommend.getRecommendId()).eq(RecommendResource::getDisable, 0));
        List<RecommendResourceVO> resourceList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resources)) {
            resources.forEach(recommendResource -> {
                RecommendResourceVO recommendResourceVO = new RecommendResourceVO();
                BeanUtils.copyProperties(recommendResource, recommendResourceVO);
                resourceList.add(recommendResourceVO);
            });
        }
        result.setResourceList(resourceList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageVO<RecommendVO> collectPage(PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        // 查询用户收藏记录
        List<RecommendCollect> collects = recommendCollectService.list(new LambdaQueryWrapper<RecommendCollect>()
                .eq(RecommendCollect::getUserId, userId).eq(RecommendCollect::getCancel, 0).orderByDesc(RecommendCollect::getCreateTime)
                .last("limit " + pageAdapter.getBegin() + " ," + pageAdapter.getSize()));

        List<RecommendVO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(collects)) {
            // 通过种草id 反查种草信息
            List<Long> ids = collects.stream().map(RecommendCollect::getRecommendId).collect(Collectors.toList());
            List<Recommend> recommends = this.list(new LambdaQueryWrapper<Recommend>().in(Recommend::getRecommendId, ids)
                    .eq(Recommend::getStatus, 2).eq(Recommend::getDisable, 0));
            recommends.forEach(recommend -> {
                RecommendVO recommendVO = new RecommendVO();
                BeanUtils.copyProperties(recommend, recommendVO);
                result.add(recommendVO);
            });
        }
        if (!CollectionUtils.isEmpty(result)) {
            // 填充是否点赞是否收藏字段
            fillIsPraise(result, userId);
            //fillIsCollect(result, userId);
            result.forEach(recommendVO -> recommendVO.setIsCollect(1));
        }

        PageVO<RecommendVO> pageVO = new PageVO<>();
        pageVO.setList(result);
        if (CollectionUtils.isEmpty(collects)) {
            pageVO.setTotal(0L);
        } else {
            Integer count = recommendCollectMapper.selectCount(new LambdaQueryWrapper<RecommendCollect>()
                    .eq(RecommendCollect::getUserId, userId).eq(RecommendCollect::getCancel, 0));
            pageVO.setTotal(count.longValue());
        }
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public void share(Long recommendId) {
        LambdaUpdateWrapper<Recommend> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                .setSql("share_count = share_count + 1");
        update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collect(Long recommendId, Integer type) {
        Long userId = AuthUserContext.get().getUserId();
        RecommendCollect recommendCollect = recommendCollectService.getOne(new LambdaQueryWrapper<RecommendCollect>()
                .eq(RecommendCollect::getRecommendId, recommendId).eq(RecommendCollect::getUserId, userId));

        LambdaUpdateWrapper<Recommend> wrapper = new LambdaUpdateWrapper<>();
        if (type == 1) {
            // 收藏总数+1
            wrapper.eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                    .setSql("collect_count = collect_count + 1");
            // 入库收藏记录
            if (recommendCollect == null) {
                recommendCollect = new RecommendCollect();
                recommendCollect.setCancel(0);
                recommendCollect.setUserId(userId);
                recommendCollect.setRecommendId(recommendId);
                recommendCollectService.save(recommendCollect);
            } else {
                // 修改收藏记录
                recommendCollectService.update(new LambdaUpdateWrapper<RecommendCollect>()
                        .eq(RecommendCollect::getRecommendCollectId, recommendCollect.getRecommendCollectId())
                        .set(RecommendCollect::getCancel, 0));
            }
        } else if (type == 0) {
            if (recommendCollect == null) {
                // 无收藏记录 不存在取消收藏
                log.error("取消收藏异常 userId = {} recommendId = {}", userId, recommendId);
                return;
            }
            // 收藏总数-1
            wrapper.eq(Recommend::getRecommendId, recommendId).eq(Recommend::getDisable, 0)
                    .setSql("collect_count = collect_count - 1");
            // 修改收藏记录
            recommendCollectService.update(new LambdaUpdateWrapper<RecommendCollect>()
                    .eq(RecommendCollect::getRecommendCollectId, recommendCollect.getRecommendCollectId())
                    .set(RecommendCollect::getCancel, 1));
        }
        update(wrapper);
    }

    @Override
    public PageVO<RecommendVO> myPage(PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        PageAdapter pageAdapter = new PageAdapter(pageDTO);
        List<RecommendVO> result = recommendMapper.myPage(pageAdapter, userId);
        if (!CollectionUtils.isEmpty(result)) {
            // 填充是否点赞是否收藏字段
            fillIsPraise(result, userId);
            fillIsCollect(result, userId);
        }
        PageVO<RecommendVO> pageVO = new PageVO<>();
        pageVO.setList(result);
        pageVO.setTotal(CollectionUtils.isEmpty(result) ? 0 : recommendMapper.myPageCount(userId));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(RecommendDTO recommendDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        UserApiVO userData = userFeignClient.getUserData(userInfoInTokenBO.getUserId()).getData();
        // 种草信息
        Recommend recommend = Recommend.builder().userType(2).userId(userData.getUserId()).userNick(userData.getNickName())
                .userPic(userData.getPic()).title(recommendDTO.getTitle()).content(recommendDTO.getContent())
                .productIds(recommendDTO.getProductIds()).status(1).coverUrl(recommendDTO.getCoverUrl()).build();

        // 获取相关资源
        List<RecommendResource> recommendResources = getResources(recommendDTO);
        // 入库
        this.save(recommend);
        if (!CollectionUtils.isEmpty(recommendResources)) {
            recommendResources.forEach(recommendResource -> recommendResource.setRecommendId(recommend.getRecommendId()));
            recommendResourceService.saveBatch(recommendResources);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(RecommendAdminDTO recommendDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        //UserApiVO userData = userFeignClient.getUserData(userInfoInTokenBO.getUserId()).getData();
        // 种草信息-admin用户免审核
        Recommend recommend = Recommend.builder().userType(1).userId(userInfoInTokenBO.getUserId()).userNick(userInfoInTokenBO.getUsername())
                .title(recommendDTO.getTitle()).content(recommendDTO.getContent())
                .weight(recommendDTO.getWeight()).recommendCateId(recommendDTO.getRecommendCateId()).productIds(recommendDTO.getProductIds())
                .status(2).coverUrl(recommendDTO.getCoverUrl()).disable(0).build();

        // 获取相关资源
        List<RecommendResource> recommendResources = getResources(recommendDTO);
        // 种草分类name
        RecommendCate byId = recommendCateService.getById(recommendDTO.getRecommendCateId());
        if (byId != null) {
            recommend.setCateName(byId.getName());
        }
        // 入库 获取主键
        this.save(recommend);
        if (!CollectionUtils.isEmpty(recommendResources)) {
            recommendResources.forEach(recommendResource -> recommendResource.setRecommendId(recommend.getRecommendId()));
            recommendResourceService.saveBatch(recommendResources);
        }
        if (recommendDTO.getRecommendCateId() != null) {
            //分类关联种草数+1
            LambdaUpdateWrapper<RecommendCate> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(RecommendCate::getRecommendCateId, recommendDTO.getRecommendCateId())
                    .setSql("recommend_count = recommend_count + 1");
            recommendCateService.update(wrapper);
        }
    }

    private List<RecommendResource> getResources(RecommendDTO recommendDTO) {
        String video = recommendDTO.getVideo();
        List<String> images = recommendDTO.getImages();
        List<RecommendResource> recommendResources = new ArrayList<>();
        if (StringUtils.isNotBlank(video)) {
            RecommendResource build = RecommendResource.builder().type(2).url(video).disable(0).build();
            recommendResources.add(build);
        }
        if (!CollectionUtils.isEmpty(images)) {
            if (images.size() > 9) {
                throw new LuckException("关联图片过多");
            }
            for (String url : images) {
                RecommendResource build = RecommendResource.builder().type(1).url(url).disable(0).build();
                recommendResources.add(build);
            }
        }
        return recommendResources;
    }


}
