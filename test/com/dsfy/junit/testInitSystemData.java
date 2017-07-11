package com.dsfy.junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dsfy.service.IBaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext*.xml", "classpath:spring-mvc.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class testInitSystemData extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource(name = "BaseService")
	private IBaseService baseService;

	@Test
	public void initPermission() throws Exception {
	}

	@Test
	public void initAdminRole() throws Exception {
	}

	@Test
	public void initAdminUser() {
	}

}
