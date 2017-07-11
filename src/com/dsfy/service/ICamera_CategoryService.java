package com.dsfy.service;

import java.util.List;

import com.dsfy.entity.Category;
import com.dsfy.entity.equipment.Camera;

/**
 * 用户和停车场关系维护
 * 
 * @author toutoumu
 */
public interface ICamera_CategoryService extends IBaseService {
	/**
	 * 根据相机ID获取相机所属类别
	 * 
	 * @param cameraId
	 * @return
	 */
	List<Category> getCategoryByCamera(int cameraId);

	/**
	 * 更新相机与类别的关联关系
	 * 
	 * @param cameraId
	 * @param categories
	 */
	public void updateRelation(int cameraId, List<Category> categories);

	/**
	 * 根据类别ID获取相机
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<Camera> getCameraByCategory(int categoryId);
}
