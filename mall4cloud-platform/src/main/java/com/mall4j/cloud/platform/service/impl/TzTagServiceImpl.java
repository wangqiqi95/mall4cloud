package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.platform.vo.TzTagDetailVO;
import com.mall4j.cloud.api.platform.vo.TzTagStoreDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.CodeUtil;
import com.mall4j.cloud.platform.dto.TzTagDTO;
import com.mall4j.cloud.platform.dto.TzTagQueryParamDTO;
import com.mall4j.cloud.platform.mapper.TzTagStaffMapper;
import com.mall4j.cloud.platform.model.TzTag;
import com.mall4j.cloud.platform.mapper.TzTagMapper;
import com.mall4j.cloud.platform.model.TzTagStaff;
import com.mall4j.cloud.platform.model.TzTagStore;
import com.mall4j.cloud.platform.service.TzStoreService;
import com.mall4j.cloud.platform.service.TzTagService;
import com.mall4j.cloud.platform.service.TzTagStaffService;
import com.mall4j.cloud.platform.service.TzTagStoreService;
import com.mall4j.cloud.platform.vo.TzTagStoreVO;
import com.mall4j.cloud.platform.vo.TzTagVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.LongToDoubleFunction;

/**
 * 门店标签
 *
 * @author gmq
 * @date 2022-09-13 12:00:57
 */
@Service
public class TzTagServiceImpl extends ServiceImpl<TzTagMapper, TzTag> implements TzTagService {

    @Autowired
    private TzTagMapper tzTagMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private TzTagStaffService tzTagStaffService;
    @Autowired
    private TzTagStoreService tzTagStoreService;
    @Resource
    private TzTagStaffMapper tzTagStaffMapper;

    @Override
    public PageVO<TzTagVO> page(PageDTO pageDTO, TzTagQueryParamDTO paramDTO) {

        PageVO<TzTagVO> pageVO=PageUtil.doPage(pageDTO, () -> tzTagMapper.listByParam(paramDTO));

        pageVO.getList().forEach(item ->{

            //关联员工、门店
            addRelStaffStore(item);

        });

        return pageVO;
    }

    @Override
    public TzTagVO getByTagId(Long tagId) {
        TzTag tzTag=this.getById(tagId);
        if(Objects.nonNull(tzTag)){
            TzTagVO tagVO = mapperFacade.map(tzTag, TzTagVO.class);

            //关联员工、门店
            addRelStaffStore(tagVO);
            return tagVO;
        }
        return null;
    }

    @Override
    public void saveTo(TzTagDTO tzTagDTO) {
        TzTag tzTag = mapperFacade.map(tzTagDTO, TzTag.class);
        tzTag.setTagId(null);
        tzTag.setCreateTime(new Date());
        tzTag.setCreateBy(AuthUserContext.get().getUsername());

        String[] codeArray = generateOrgCode(tzTagDTO.getParentId());
        tzTag.setTagCode(codeArray[0]);
        tzTag.setTagType(codeArray[1]);

        this.save(tzTag);

        tzTagDTO.setTagId(tzTag.getTagId());
        //保存关联员工、门店
        saveRelStaffStoreTo(tzTagDTO);
    }

    @Override
    public void updateTo(TzTagDTO tzTagDTO) {
        TzTag tzTag = mapperFacade.map(tzTagDTO, TzTag.class);
        tzTag.setUpdateTime(new Date());
        tzTag.setUpdateBy(AuthUserContext.get().getUsername());
        this.updateById(tzTag);

        Long tagId=tzTagDTO.getTagId();
        //删除关联员工
        tzTagStaffService.deleteByTagId(tagId);
        //删除关联门店
        tzTagStoreService.deleteByTagId(tagId);

        //保存关联员工、门店
        saveRelStaffStoreTo(tzTagDTO);
    }

    @Override
    public void updateTagStatus(Long tagId, Integer status) {
        this.update(new LambdaUpdateWrapper<TzTag>().set(TzTag::getStatus,status).eq(TzTag::getTagId,tagId));
    }

    //查询标签关联员工、门店
    private void addRelStaffStore(TzTagVO tagVO){
        //关联员工
        List<Long> staffIds=tzTagStaffService.listByTagId(tagVO.getTagId());
        tagVO.setStaffCount(CollectionUtil.isNotEmpty(staffIds)?staffIds.size():0);
        tagVO.setStaffIds(staffIds);
        //关联门店
        List<Long> storeIds=tzTagStoreService.listByTagId(tagVO.getTagId());
        tagVO.setStoreCount(CollectionUtil.isNotEmpty(storeIds)?storeIds.size():0);
        tagVO.setStoreIds(storeIds);
    }

    //保存表填关联员工、门店
    private void saveRelStaffStoreTo(TzTagDTO tzTagDTO){
        //保存关联员工
        if(CollectionUtil.isNotEmpty(tzTagDTO.getStaffIds())){
            List<TzTagStaff> tzTagStaffList=new ArrayList<>();
            tzTagDTO.getStaffIds().forEach(staffId ->{
                TzTagStaff tzTagStaff=new TzTagStaff();
                tzTagStaff.setId(null);
                tzTagStaff.setTagId(tzTagDTO.getTagId());
                tzTagStaff.setStaffId(staffId);
                tzTagStaff.setDelFlag(0);
                tzTagStaffList.add(tzTagStaff);
            });
            tzTagStaffService.saveBatch(tzTagStaffList);
        }

        //保存关联门店
        if(CollectionUtil.isNotEmpty(tzTagDTO.getStoreIds())){
            List<TzTagStore> tzTagStores=new ArrayList<>();
            tzTagDTO.getStoreIds().forEach(storeId ->{
                TzTagStore tzTagStore=new TzTagStore();
                tzTagStore.setId(null);
                tzTagStore.setTagId(tzTagDTO.getTagId());
                tzTagStore.setStoreId(storeId);
                tzTagStore.setDelFlag(0);
                tzTagStores.add(tzTagStore);
            });
            tzTagStoreService.saveBatch(tzTagStores);
        }

    }

    @Override
    public void deleteById(Long tagId) {
        //删除标签
        this.update(new LambdaUpdateWrapper<TzTag>().set(TzTag::getDelFlag,1).eq(TzTag::getTagId,tagId));
        //删除关联员工
        tzTagStaffService.deleteByTagId(tagId);
        //删除关联门店
        tzTagStoreService.deleteByTagId(tagId);
    }

    @Override
    public List<TzTagDetailVO> listTagByStaffId(Long staffId) {
        List<TzTagDetailVO> tzTagVOS=tzTagStaffMapper.listTagAndStoreByStaffId(staffId);
//        tzTagVOS.stream().forEach(item ->{
//            List<TzTagStoreDetailVO> tzTagStoreVOS=tzTagStoreService.listDetailByTagId(item.getTagId());
//            item.setStores(tzTagStoreVOS);
//        });
        return tzTagVOS;
    }

    private String[] generateOrgCode(Long parentId) {
        LambdaQueryWrapper<TzTag> query = new LambdaQueryWrapper<TzTag>();
        LambdaQueryWrapper<TzTag> query1 = new LambdaQueryWrapper<TzTag>();
        String[] strArray = new String[2];
        // 创建一个List集合,存储查询返回的所有TzTag对象
        List<TzTag> tagtList = new ArrayList<>();
        // 定义新编码字符串
        String newOrgCode = "";
        // 定义旧编码字符串
        String oldOrgCode = "";
        // 定义部门类型
        String orgType = "";
        // 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
        if (Objects.isNull(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
            query1.eq(TzTag::getParentId, "").or(wrapper3->wrapper3.isNull(TzTag::getParentId));
            query1.orderByDesc(TzTag::getTagCode);
            tagtList = this.list(query1);
            if(tagtList == null || tagtList.size() == 0) {
                strArray[0] = CodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            }else {
                TzTag depart = tagtList.get(0);
                oldOrgCode = depart.getTagCode();
                orgType = depart.getTagType();
                newOrgCode = CodeUtil.getNextYouBianCode(oldOrgCode);
            }
        } else { // 反之则查询出所有同级,获取结果后有两种情况,有同级和没有同级
            // 封装查询同级的条件
            query.eq(TzTag::getParentId, parentId);
            // 降序排序
            query.orderByDesc(TzTag::getTagCode);
            // 查询出同级的集合
            List<TzTag> parentList = this.list(query);
            // 查询出父级
            TzTag tag = this.getById(parentId);
            // 获取父级的Code
            String parentCode = tag.getTagCode();
            // 根据父级类型算出当前部门的类型
            orgType = String.valueOf(Integer.valueOf(tag.getTagType()) + 1);
            // 处理同级部门为null的情况
            if (parentList == null || parentList.size() == 0) {
                // 直接生成当前的编码并返回
                newOrgCode = CodeUtil.getSubYouBianCode(parentCode, null);
            } else { //处理有同级的情况
                // 获取同级的编码,利用工具类
                String subCode = parentList.get(0).getTagCode();
                // 返回生成的当前编码
                newOrgCode = CodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // 返回最终封装了编码和类型的数组
        strArray[0] = newOrgCode;
        strArray[1] = orgType;
        return strArray;
    }
}
