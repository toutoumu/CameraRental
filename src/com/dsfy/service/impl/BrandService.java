package com.dsfy.service.impl;

import com.dsfy.entity.equipment.Brand;
import com.dsfy.exception.JsonException;
import com.dsfy.service.IBrandService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("BrandService")
public class BrandService extends BaseService implements IBrandService {
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Brand update(Brand brand) {
        Brand temp = baseDao.getById(Brand.class, brand.getBrandId());
        if (temp == null) {
            throw new JsonException("更新的品牌不存在");
        }
        // 如果品牌名称更新
        if (!temp.getName().equals(brand.getName())) {
            String camerasql = "update t_camera SET brand = ? where brandId = ?";
            String lenssql = "update t_CameraLens SET brand = ? where brandId = ?";
            String rentalsql = "update t_RentalInfo SET brand = ? where brandId = ?";
            baseDao.executeBySQL(camerasql, brand.getName(), brand.getBrandId());//相机
            baseDao.executeBySQL(lenssql, brand.getName(), brand.getBrandId());//镜头
            baseDao.executeBySQL(rentalsql, brand.getName(), brand.getBrandId());//租赁信息
        }
        temp.setName(brand.getName());// 名称
        if (!ValidateUtil.isEmpty(brand.getMark())) {
            temp.setMark(brand.getMark());//备注
        }
        baseDao.update(temp);
        return temp;
    }
}
