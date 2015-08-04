package cn.jc.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class JDBCTest {
	
	/**
	 * Driver是一个接口：数据库厂商必须提供实现的接口
	 * @throws SQLException 
	 */
	@Test
	public void testDriver() throws SQLException {
		//1. 创建一个Driver实现类的对象
		Driver driver = new com.mysql.jdbc.Driver();		
		
		String url = "jdbc:mysql://localhost:3306/cfs";
		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "root");
		
		//2. 调用Driver接口的connect(url,info)获取数据连接
		Connection connection = driver.connect(url, info);
		System.out.println(connection);
	}
}
