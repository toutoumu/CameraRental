package com.dsfy.service;

import com.dsfy.entity.equipment.Camera;

import java.util.List;

public interface ICameraService extends IBaseService {

    /**
     * 根据品牌ID查询相机
     *
     * @param brandId
     * @return
     */
    List<Camera> getByBrand(int brandId);

    /**
     * 根据 类别,品牌id,品牌名称,型号名称查询相机
     *
     * @param camera
     * @return
     */
    List<Camera> query(Camera camera);

    /**
     * 更新相机数据, 并同时更新租赁信息中的品牌信息
     *
     * @param camera
     * @return
     */
    Camera update(Camera camera);

}
