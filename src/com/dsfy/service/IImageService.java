package com.dsfy.service;

import com.dsfy.entity.ImageInfo;

import java.util.List;

public interface IImageService extends IBaseService {

    /**
     * 根据条件查询
     *
     * @param image
     * @return
     */
    List<ImageInfo> query(ImageInfo image);

    /**
     * 根据条件查询图片信息
     *
     * @param category 图片类型 {@link com.dsfy.entity.ImageInfo.Category}
     * @param fk       外键
     * @return {@link List<ImageInfo>}
     */
    List<ImageInfo> query(int category, String fk);
}
