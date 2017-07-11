package com.dsfy.service.impl;

import com.dsfy.entity.equipment.Camera;
import com.dsfy.exception.JsonException;
import com.dsfy.service.ICameraService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("CameraService")
public class CameraService extends BaseService implements ICameraService {

    @Override
    public List<Camera> getByBrand(int brandId) {
        return getByJpql("from Camera where brandId = ?", brandId);
    }

    @Override
    public List<Camera> query(Camera camera) {
        String jpql = "from Camera c where 1=1 ";

        if (camera.getCategoryId() != 0) {// 所属类别
            jpql += " and EXISTS ( select up.cameraId from Camera_Category up where up.cameraId = c.id and up.categoryId = "
                    + camera.getCategoryId() + ")";
        }

        if (camera.getBrandId() != 0) {//品牌ID
            jpql += " and c.brandId = " + camera.getBrandId();
        }
        if (!ValidateUtil.isEmpty(camera.getBrand())) {//品牌名称
            jpql += " and c.brand like '%" + camera.getBrand() + "%'";
        }
        if (!ValidateUtil.isEmpty(camera.getModel())) {//型号
            jpql += " and c.model like '%" + camera.getModel() + "%'";
        }
        return getByJpql(jpql);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Camera update(Camera camera) {
        Camera temp = baseDao.getById(Camera.class, camera.getCameraId());
        if (temp == null) {
            throw new JsonException("更新的型号不存在");
        }
        // 如果相机的品牌,型号有变更
        if (!temp.getBrand().equals(camera.getBrand()) || !temp.getModel().equals(camera.getModel())) {
            String sql = "update t_RentalInfo set brandId=?, brand = ? ,model = ? where cameraId = ?";
            // 注意第三个参数必须是这个相机的ID
            baseDao.executeBySQL(sql, camera.getBrandId(), camera.getBrand(), camera.getModel(), temp.getCameraId());
        }

        baseDao.update(camera);
        return temp;
    }
}
