package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallFxPersonIdentityThird;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销员身份信息表_对接第三方
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public interface MallFxPersonIdentityThirdMapper {

	/**
	 * 获取分销员身份信息表_对接第三方列表
	 * @return 分销员身份信息表_对接第三方列表
	 */
	List<MallFxPersonIdentityThird> list();

	/**
	 * 根据分销员身份信息表_对接第三方id获取分销员身份信息表_对接第三方
	 *
	 * @param id 分销员身份信息表_对接第三方id
	 * @return 分销员身份信息表_对接第三方
	 */
	MallFxPersonIdentityThird getById(@Param("id") Long id);

	/**
	 * 保存分销员身份信息表_对接第三方
	 * @param mallFxPersonIdentityThird 分销员身份信息表_对接第三方
	 */
	void save(@Param("mallFxPersonIdentityThird") MallFxPersonIdentityThird mallFxPersonIdentityThird);

	/**
	 * 更新分销员身份信息表_对接第三方
	 * @param mallFxPersonIdentityThird 分销员身份信息表_对接第三方
	 */
	void update(@Param("mallFxPersonIdentityThird") MallFxPersonIdentityThird mallFxPersonIdentityThird);

	/**
	 * 根据分销员身份信息表_对接第三方id删除分销员身份信息表_对接第三方
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
