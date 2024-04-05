package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.CpMaterialLable;
import com.mall4j.cloud.biz.model.cp.Material;
import com.mall4j.cloud.biz.model.cp.MaterialMsg;
import com.mall4j.cloud.biz.model.cp.MaterialStore;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 素材表VO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialDetailVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("素材信息对象")
    private Material material;

	@ApiModelProperty("关联商店列表对象")
	private List<MaterialStore> stores;

	@ApiModelProperty("关联消息列表对象")
	private List<MaterialMsg> msgs;

	@ApiModelProperty("关联消息列表对象")
	private List<CpMaterialLable> lables;

	public  MaterialDetailVO(Material material,List<MaterialMsg> msgs,List<MaterialStore> stores,List<CpMaterialLable> lables){
		this.material =material;
		this.msgs = msgs;
		this.stores = stores;
		this.lables = lables;
	}

	public  MaterialDetailVO(Material material,List<MaterialMsg> msgs,List<MaterialStore> stores){
		this.material =material;
		this.msgs = msgs;
		this.stores = stores;
	}
}
