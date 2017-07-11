package com.dsfy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dsfy.entity.Region;
import com.dsfy.service.IRegionService;

@Service("RegionService")
public class RegionService extends BaseService implements IRegionService {

	@Override
	public List<Region> getByPrentId(int regionId) {
		return getByJpql("from Region where parentId = ?", regionId);
	}
}
