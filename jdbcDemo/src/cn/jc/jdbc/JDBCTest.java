package cn.jc.jdbc;

import java.io.InputStream;
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
	
	/**
	 * 编写一个通用方法，不修改源程序的情况下，获取任何数据库的连接
	 * 解决方案：把数据库驱动Driver实现类的全类名、url、user、password放入配置文件中，
	 * 通过修改配置文件的方式实现和具体数据库解耦
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		String driverClass = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;
		//读取类路径下的jdbc.properties文件
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		driverClass = properties.getProperty("driver");
		jdbcUrl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		
		//反射获取Driver
		Driver driver = (Driver)Class.forName(driverClass).newInstance();
		Properties info = new Properties();
		info.put("user", user);
		info.put("password", password);
		
		Connection connection = driver.connect(jdbcUrl, info);
		
		return connection;
	}
	
	@Test
	public void testGetConnetcion() throws Exception {
		System.out.println(getConnection());
	}
}
