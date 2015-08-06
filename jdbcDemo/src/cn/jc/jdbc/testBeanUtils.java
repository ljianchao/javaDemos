package cn.jc.jdbc;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class testBeanUtils {

	@Test
	public void testSetProperty() throws IllegalAccessException, InvocationTargetException {
		Object object = new Customer();
		System.out.println(object);
		
		BeanUtils.setProperty(object, "name", "test BeanUtils");
		System.out.println(object);
	}
}
