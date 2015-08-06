package cn.jc.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;

public class JDBCTest {
	
	/**
	 * ResultSetMetaData是描述ResultSet的元数据对象，可以获取到结果集中的的列名、列值等信息
	 */
	@Test
	public void testResultSetMetaData() {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			//1. 获取connection		
			conn = JDBCTools.getConnection();
			String sql = "SELECT * FROM customers where id = ?";
			//2. 获取Statment
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, 1);
			
			
			Map<String, Object> values = new HashMap<String, Object>();
			
			//执行查询，得到ResultSet
			rs = preparedStatement.executeQuery();
			
			//获取ResultSetMetaData对象
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				//打印每一列的列名
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String columnLabel = rsmd.getColumnLabel(i+1);
					Object columnValue = rs.getObject(columnLabel);
					
					values.put(columnLabel, columnValue);
				}
				
			}
			
			//System.out.println(values);		
			
			Class clazz = Customer.class;
			
			Object customer = clazz.newInstance();
			
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				String fieldName = entry.getKey();
				Object fieldValue = entry.getValue();
				
				System.out.println(fieldName + ":" + fieldValue);
			}
			
			//6. 关闭数据库资源
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCTools.release(rs, preparedStatement, conn);
		}
	}
	
	@Test
	public void testGet() {
		String sql = "SELECT * FROM customers where id = ?";
		Customer customer = JDBCTools.get(Customer.class, sql, 1);
		
		System.out.println(customer);
	}
	
	/**
	 * ResultSet：结果集，封装了使用JDBC进行查询的结果
	 * 1. 调用Statement对象的executeQuery(sql)获取结果集
	 * 2. ResultSet返回的是一张数据表，有一个指针指向数据表的第一行的前面，
	 * 可以使用next()方法检测下一行是否有效，有效返回true，且指针执行下一行，
	 * 相当于Iterator对象的hasNext()和next()方法的结合
	 * 3. 当指针对位到一行时，可以通过调用getXxx(index)或getXxx(columnName)
	 * 获取每一列的值，例如getIndex(1),getString("name")
	 * 4. 关闭ResultSet
	 */
	@Test
	public void testResultSet(){
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			//1. 获取connection		
			conn = JDBCTools.getConnection();
			//2. 获取Statment
			statement = conn.createStatement();
			
			//3. 准备SQL
			String sql = "SELECT * FROM customers";
			
			//4. 执行查询，得到ResultSet
			rs = statement.executeQuery(sql);
			
			//5. 处理ResultSet
			while (rs.next()) {
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getString(3));
				System.out.println(rs.getDate(4));
			}
			
			//6. 关闭数据库资源
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCTools.release(rs, statement, conn);
		}
	}	
	
	@Test 
	public void testPreparedStatement(){
		Connection conn = null;
		//PreparedStatement是Statement的子接口
		//可以传入带占位符的SQL语句，并且提供了补充占位符变量的方法
		//可以有效的制止SQL注入
		PreparedStatement ps = null;
		
		try {
			conn = JDBCTools.getConnection();
			//2. 准备插入的SQL语句
			String sql = "INSERT INTO customers(name,email,birth) VALUES(?,?,?)";
			
			ps = conn.prepareStatement(sql);
			
			//索引从1开始
			ps.setString(1, "test PreparedStatement");
			ps.setString(2, "PreparedStatement@163.com");
			ps.setDate(3, new Date(new java.util.Date().getTime()));
			
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, ps, conn);			
		}
 	}
	
	/**
	 * 插入一条记录
	 * @throws Exception 
	 */
	@Test
	public void testStatement() throws Exception {
		//1. 获取数据库连接
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = getConnection2();
			
			//2. 准备插入的SQL语句
			String sql = "INSERT INTO customers(name,email,birth) VALUES('ljch','ljch@163.com','1988-01-01')";
			
			//3. 执行插入
			// 3.1 获取操作SQL语句的Statement对象
			statement = connection.createStatement();
			
			// 3.2 调用Statement对象的executeUpdate(sql)执行SQL语句进行插入
			statement.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			//4. 关闭Statement对象
			statement.close();
			//5. 关闭连接
			connection.close();
		}		
	}
	
	@Test
	public void testDriverManager() throws Exception {
		System.out.println(getConnection2());
	}
	
	/**
	 * DriverManager 驱动管理类
	 * 1). 可以通过重载的getConnection()方法获取数据库连接，较为方便
	 * 2). 可以同时管理多个驱动程序: 若注册了多个数据库连接，则调用getConnection()
	 * 方法时传入的参数不同，即返回不同的数据库连接
	 */
	private Connection getConnection2() throws Exception {
		//1. 准备数据库连接的4个字符串
		String driverClass = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;
		//读取类路径下的jdbc.properties文件
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
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
	
	/**
	 * Driver是一个接口：数据库厂商必须提供实现的接口
	 * @throws SQLException 
	 */
	@Test
	public void testDriver() throws SQLException {
		//1. 创建一个Driver实现类的对象
		Driver driver = new com.mysql.jdbc.Driver();		
		
		String url = "jdbc:mysql://localhost:3306/demo";
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
