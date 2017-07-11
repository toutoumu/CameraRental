package com.dsfy.junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dsfy.service.ICamera_CategoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext*.xml", "classpath:spring-mvc.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class TestUser_Parking extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource(name = "User_ParkingService")
	private ICamera_CategoryService user_ParkingService;

	@Test
	public void ddd() {
		// user_ParkingService.getParkingByUser(1);
		// user_ParkingService.getUserByParking(1);
	}
}
