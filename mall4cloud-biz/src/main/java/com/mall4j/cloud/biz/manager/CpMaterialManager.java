package com.mall4j.cloud.biz.manager;

import com.mall4j.cloud.biz.mapper.cp.MaterialMapper;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CpMaterialManager {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MapperFacade mapperFacade;

    public Integer getMaterialCountByMatTypeId(List<Integer> matTypeIdList,Integer status){
        return materialMapper.getMaterialCountByMatTypeId(matTypeIdList,status);
    }

}
