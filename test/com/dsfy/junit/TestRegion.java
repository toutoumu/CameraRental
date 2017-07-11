package com.dsfy.junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dsfy.entity.Region;
import com.dsfy.service.IRegionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext*.xml", "classpath:spring-mvc.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class TestRegion extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource(name = "RegionService")
	private IRegionService regionService;

	@Test
	public void initRegionData() {
		Region region = null;
		region = new Region();
		region.setName("广东");
		region.setParentId(0);
		region.setRegionType(1);
		regionService.add(region);

		int id = region.getRegionId();
		region = new Region();
		region.setName("深圳");
		region.setParentId(id);
		region.setRegionType(1);
		regionService.add(region);

		region = new Region();
		region.setName("广州");
		region.setParentId(id);
		region.setRegionType(1);
		regionService.add(region);

		region = new Region();
		region.setName("佛山");
		region.setParentId(id);
		region.setRegionType(1);
		regionService.add(region);

		region = new Region();
		region.setName("湖南");
		region.setParentId(0);
		region.setRegionType(1);
		regionService.add(region);

		id = region.getRegionId();
		region = new Region();
		region.setName("长沙");
		region.setParentId(id);
		region.setRegionType(1);
		regionService.add(region);

		region = new Region();
		region.setName("湘潭");
		region.setParentId(id);
		region.setRegionType(1);
		regionService.add(region);

		region = new Region();
		region.setName("株洲");
		region.setParentId(id);
		region.setRegionType(1);
		regionService.add(region);
	}

}
