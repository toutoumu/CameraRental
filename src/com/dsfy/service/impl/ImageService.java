package com.dsfy.service.impl;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.ImageInfo;
import com.dsfy.service.IImageService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ImageService")
public class ImageService extends BaseService implements IImageService {

    @Override
    public List<ImageInfo> query(ImageInfo Image) {
        if (Image == null) {
            return null;
        }
        List<QueryCondition> conditions = new ArrayList<QueryCondition>();
        QueryCondition condition = null;
        // 图片类别
        if (Image.getCategory() != 0) {
            condition = new QueryCondition("category", QueryCondition.EQ, Image.getCategory());
            conditions.add(condition);
        }
        // 外键
        if (!ValidateUtil.isEmpty(Image.getForeignKey())) {
            condition = new QueryCondition("foreignKey", QueryCondition.EQ, Image.getForeignKey());
            conditions.add(condition);
        }
        // 默认查询小图
        condition = new QueryCondition("size", QueryCondition.EQ, ImageInfo.Size.small);
        conditions.add(condition);
        return baseDao.get(ImageInfo.class, conditions);
    }

    @Override
    public List<ImageInfo> query(int category, String fk) {
        ImageInfo Image = new ImageInfo();
        Image.setCategory(category);
        Image.setForeignKey(fk);
        List<QueryCondition> conditions = new ArrayList<QueryCondition>();
        QueryCondition condition = null;
        // 图片类别
        if (Image.getCategory() != 0) {
            condition = new QueryCondition("category", QueryCondition.EQ, Image.getCategory());
            conditions.add(condition);
        }
        // 外键
        if (!ValidateUtil.isEmpty(Image.getForeignKey())) {
            condition = new QueryCondition("foreignKey", QueryCondition.EQ, Image.getForeignKey());
            conditions.add(condition);
        }
        // 默认查询小图
        condition = new QueryCondition("size", QueryCondition.EQ, ImageInfo.Size.small);
        conditions.add(condition);
        return baseDao.get(ImageInfo.class, conditions);
    }

}
