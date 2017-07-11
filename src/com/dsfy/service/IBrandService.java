package com.dsfy.service;

import com.dsfy.entity.equipment.Brand;

public interface IBrandService extends IBaseService {

    /**
     * 更新品牌并更新,引用了该品牌的数据
     *
     * @param brand
     */
    Brand update(Brand brand);

}
