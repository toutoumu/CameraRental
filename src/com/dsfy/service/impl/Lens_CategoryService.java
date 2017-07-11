package com.dsfy.service.impl;

import com.dsfy.entity.Category;
import com.dsfy.entity.Lens_Category;
import com.dsfy.entity.equipment.CameraLens;
import com.dsfy.service.ILens_CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("Lens_CategoryService")
public class Lens_CategoryService extends BaseService implements ILens_CategoryService {

    @Override
    public List<Category> getCategoryByCamera(int lensId) {//
        String jpql = "from Category  p where EXISTS ( select up.categoryId from Camera_Category up where up.categoryId = p.categoryId and up.lensId = ?)";
        return baseDao.getByJpql(jpql, lensId);
    }

    @Override
    public List<CameraLens> getCameraByCategory(int categoryId) {
        String jpql = "from CameraLens u where EXISTS ( select up.lensId from Camera_Category up where up.cameraId = u.cameraId and up.categoryId = ?)";
        return baseDao.getByJpql(jpql, categoryId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRelation(int lensId, List<Category> categories) {
        if (lensId != 0) {
            // 删除类别关联
            executeJpql("delete from Lens_Category where lensId = ?", lensId);
            // 重新添加关联关系
            if (categories != null && categories.size() > 0) {
                // 循环
                List<Lens_Category> list = new ArrayList<>();
                for (Category category : categories) {
                    Lens_Category cc = new Lens_Category();
                    cc.setLensId(lensId);
                    cc.setCategoryId(category.getCategoryId());
                    list.add(cc);
                }
                batchSave(list);
            }
        }
    }
}
