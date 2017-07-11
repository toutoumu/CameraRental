package com.dsfy.service.impl;

import com.dsfy.entity.Category;
import com.dsfy.entity.equipment.CameraLens;
import com.dsfy.service.ICameraLensService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CameraLensService")
public class CameraLensService extends BaseService implements ICameraLensService {

    @Override
    public List<CameraLens> query(CameraLens camera) {

        String jpql = "from CameraLens c where 1=1 ";

        if (camera.getCategoryId() != 0) {
            jpql += " and EXISTS ( select up.lensId from Lens_Category up where up.lensId = c.lensId and up.categoryId = "
                    + camera.getCategoryId() + ")";
        }

        if (camera.getBrandId() != 0) {//品牌
            jpql += " and c.brandId = " + camera.getBrandId();
        }
        if (!ValidateUtil.isEmpty(camera.getBrand())) {//型号
            jpql += " and c.brand like '%" + camera.getBrand() + "%'";
        }
        if (!ValidateUtil.isEmpty(camera.getModel())) {//型号
            jpql += " and c.model like '%" + camera.getModel() + "%'";
        }
        return getByJpql(jpql);
    }

    @Override
    public List<CameraLens> getByBrand(int brandId) {
        return getByJpql("from CameraLens where brandId = ?", brandId);
    }

    @Override
    public List<Category> getCategoryByCameraLens(int lensId) {
        String jpql = "from Category  p where EXISTS ( select up.categoryId from Lens_Category up where up.categoryId = p.categoryId and up.lensId = ?)";
        return baseDao.getByJpql(jpql, lensId);
    }

}
