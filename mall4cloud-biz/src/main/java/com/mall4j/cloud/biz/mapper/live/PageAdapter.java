

package com.mall4j.cloud.biz.mapper.live;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 分页适配
 * @author LGH
 */
@Data
public class PageAdapter {

    private int begin;

    private int size;

    public PageAdapter(Page page) {
        int[] startEnd = PageUtil.transToStartEnd((int) page.getCurrent() - 1, (int) page.getSize());
        this.begin = startEnd[0];
        this.size = (int)page.getSize();
    }
}
