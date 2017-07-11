package com.dsfy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dsfy.entity.Category;
import com.dsfy.service.ICategoryService;

@Service("CategoryService")
public class CategoryService extends BaseService implements ICategoryService {

	@Override
	public List<Category> getByPrentId(int categoryId) {
		return getByJpql("from Category where parentId = ?", categoryId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCategory(int categoryId) {
		executeBySQL("delete from t_category where categoryId = ? or parentId = ?", categoryId, categoryId);
	}
}
