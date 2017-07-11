package com.dsfy.service;

import com.dsfy.entity.Category;
import com.dsfy.entity.equipment.CameraLens;

import java.util.List;

public interface ICameraLensService extends IBaseService {
    /**
     * 根据镜头 类别,品牌id,品牌名称,型号名称查询镜头
     *
     * @param lens
     * @return
     */
    List<CameraLens> query(CameraLens lens);

    /**
     * 根据品牌ID查询镜头
     *
     * @param brandId
     * @return
     */
    List<CameraLens> getByBrand(int brandId);

    /**
     * 根据相机ID获取相机所属类别
     *
     * @param lensId
     * @return
     */
    List<Category> getCategoryByCameraLens(int lensId);

}
