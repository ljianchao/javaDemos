package cn.jc.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * 操作jdbc的方法
 * @author Administrator
 *
 */
public class JDBCTools {
	
	/**
	 * 关闭Statement, Connection
	 * @param statement
	 * @param conn
	 */
	public static void release(ResultSet rs,Statement statement, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		release(statement, conn);
	}
	
	/**
	 * 关闭Statement, Connection
	 * @param statement
	 * @param conn
	 */
	public static void release(Statement statement, Connection conn) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * DriverManager 驱动管理类
	 * 1). 可以通过重载的getConnection()方法获取数据库连接，较为方便
	 * 2). 可以同时管理多个驱动程序: 若注册了多个数据库连接，则调用getConnection()
	 * 方法时传入的参数不同，即返回不同的数据库连接
	 */
	public static Connection getConnection() throws Exception {
		//1. 准备数据库连接的4个字符串
		String driverClass = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;
		//读取类路径下的jdbc.properties文件
		InputStream inputStream = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		driverClass = properties.getProperty("driver");
		jdbcUrl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		
		//2. 加载数据库驱动程序(注册驱动)
		//mysql的Driver中的静态构造函数中已注册，所以不需显示注册
		//DriverManager.registerDriver((Driver)Class.forName(driverClass).newInstance());
		Class.forName(driverClass);
		
		//3. 通过DriverManager的getConnection()方法获取数据库连接
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		//System.out.println(connection);
		return connection;
	}
}
