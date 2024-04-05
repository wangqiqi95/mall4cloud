package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.EdBaseShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据中心_基础_渠道信息
 * <p>
 * 品牌下属的线上下门店需要先维护
 * <p>
 * 所属商圈，
 *
 * @author FrozenWatermelon
 * @date 2022-04-10 10:16:40
 */
public interface EdBaseShopMapper {

    /**
     * 获取数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，列表
     *
     * @return 数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，列表
     */
    List<EdBaseShop> list();

    /**
     * 根据数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，id获取数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，
     *
     * @param id 数据中心_基础_渠道信息
     *           <p>
     *           品牌下属的线上下门店需要先维护
     *           <p>
     *           所属商圈，id
     * @return 数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，
     */
    EdBaseShop getById(@Param("id") Long id);

    /**
     * 保存数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，
     *
     * @param edBaseShop 数据中心_基础_渠道信息
     *                   <p>
     *                   品牌下属的线上下门店需要先维护
     *                   <p>
     *                   所属商圈，
     */
    void save(@Param("edBaseShop") EdBaseShop edBaseShop);

    /**
     * 更新数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，
     *
     * @param edBaseShop 数据中心_基础_渠道信息
     *                   <p>
     *                   品牌下属的线上下门店需要先维护
     *                   <p>
     *                   所属商圈，
     */
    void update(@Param("edBaseShop") EdBaseShop edBaseShop);

    /**
     * 根据数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，id删除数据中心_基础_渠道信息
     * <p>
     * 品牌下属的线上下门店需要先维护
     * <p>
     * 所属商圈，
     *
     * @param id
     */
    void deleteById(@Param("id") Long id);
}
