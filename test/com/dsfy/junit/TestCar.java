package com.dsfy.junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dsfy.entity.equipment.Camera;
import com.dsfy.service.IBaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext*.xml", "classpath:spring-mvc.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class TestCar extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource(name = "BaseService")
	private IBaseService baseService;

	@Test
	public void add() {
		Camera car = new Camera();
		baseService.add(car);
		System.out.println(car.getCameraId());
	}

	@Test
	public void get() {
		Camera car = baseService.getById(Camera.class, 1);
		System.out.println(car.getAntiShake());
	}
}
