package com.dsfy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dsfy.entity.Camera_Category;
import com.dsfy.entity.Category;
import com.dsfy.entity.equipment.Camera;
import com.dsfy.service.ICamera_CategoryService;

@Service("Camera_CategoryService")
public class Camera_CategoryService extends BaseService implements ICamera_CategoryService {

    @Override
    public List<Category> getCategoryByCamera(int cameraId) {//
        String jpql = "from Category  p where EXISTS ( select up.categoryId from Camera_Category up where up.categoryId = p.categoryId and up.cameraId = ?)";
        return baseDao.getByJpql(jpql, cameraId);
    }

    @Override
    public List<Camera> getCameraByCategory(int categoryId) {
        String jpql = "from Camera u where EXISTS ( select up.cameraId from Camera_Category up where up.cameraId = u.cameraId and up.categoryId = ?)";
        return baseDao.getByJpql(jpql, categoryId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRelation(int cameraId, List<Category> categories) {
        if (cameraId != 0) {
            // 删除类别关联
            baseDao.executeJpql("delete from Camera_Category where cameraId = ?", cameraId);
            // 重新添加关联关系
            if (categories != null && categories.size() > 0) {
                // 循环
                List<Camera_Category> list = new ArrayList<>();
                for (Category category : categories) {
                    Camera_Category cc = new Camera_Category();
                    cc.setCameraId(cameraId);
                    cc.setCategoryId(category.getCategoryId());
                    list.add(cc);
                }
                baseDao.batchSave(list);
            }
        }
    }
}
