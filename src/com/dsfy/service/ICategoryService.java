package com.dsfy.service;

import java.util.List;

import com.dsfy.entity.Category;

public interface ICategoryService extends IBaseService {
	List<Category> getByPrentId(int categoryId);

	void deleteCategory(int categoryId);
}
