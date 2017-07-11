package com.dsfy.service;

import java.util.List;

import com.dsfy.entity.Region;

public interface IRegionService extends IBaseService {

	/**
	 * 根据父节点的主键查询地区信息 selecte * from region where parentId = id
	 * 
	 * @param regionId
	 *            父节点的id
	 * @return
	 */
	List<Region> getByPrentId(int regionId);

}
