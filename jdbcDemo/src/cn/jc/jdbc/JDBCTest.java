package cn.jc.jdbc;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCTest {
	
	/**
	 * 测试存储过程
	 */
	@Test
	public void testCallableStatement() {
		Connection connection = null;
		CallableStatement callableStatement = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "{?= call sum_salary[(?,?)]}";
			callableStatement = connection.prepareCall(sql);
			//注册out参数
			callableStatement.registerOutParameter(1, Types.NUMERIC);
			callableStatement.registerOutParameter(3, Types.NUMERIC);
			// 赋值in参数
			callableStatement.setInt(2, 80);
			
			callableStatement.execute();
			
			//获取返回值
			double sumSalary = callableStatement.getDouble(1);
			double empCount = callableStatement.getDouble(3);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, callableStatement, connection);
		}
	}
	
	/**
	 * 测试c3p0数据库连接池（配置文件）
	 * @throws SQLException 
	 */
	@Test
	public void testC3P0WithConfigFile() throws SQLException {
		DataSource dataSource = new ComboPooledDataSource("helloc3p0");
		
		System.out.println(dataSource.getConnection());
		
		ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource)dataSource;
		System.out.println(comboPooledDataSource.getMaxPoolSize());
	}
	
	/**
	 * 测试c3p0数据库连接池
	 * @throws PropertyVetoException 
	 * @throws SQLException 
	 */
	@Test
	public void testC3P0() throws PropertyVetoException, SQLException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/demo");
		cpds.setUser("root");
		cpds.setPassword("root");
		
		System.out.println(cpds.getConnection());
	}
	
	/**
	 * 测试DataSourceFactory
	 * @throws Exception
	 */
	@Test
	public void testDBCPWithDataSourceFactory() throws Exception {
		Properties properties = new Properties();
		InputStream inputStream = JDBCTest.class.getClassLoader().
				getResourceAsStream("dbcp.properties");
		properties.load(inputStream);
		DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
		
		System.out.println(dataSource.getConnection());
		
//		BasicDataSource basicDataSource = (BasicDataSource)dataSource;
//		
//		System.out.println(basicDataSource.getMaxWait());
	}
	
	/**
	 * 使用DBCP数据库连接池
	 * 依赖于：commons-pool.jar
	 * @throws SQLException 
	 */
	@Test
	public void testDBCP() throws SQLException {
		BasicDataSource dataSource = null;
		//创建DBCP数据源实例
		dataSource = new BasicDataSource();
		
		//2. 为数据库实例指定必须的属性
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setUrl("jdbc:mysql://localhost:3306/demo");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		
		//3. 指定数据源的一些可选的属性
		dataSource.setInitialSize(10);	//指定连接数
		dataSource.setMaxActive(50);	//最大连接数，同一时刻可以想数据库申请的连接数
		dataSource.setMinIdle(5);	//在数据库空闲状态下，连接池中最少有多少个连接
		dataSource.setMaxWait(1000 * 5);	//等待数据库连接池分配连接的最长时间，单位毫秒，超时将抛异常
		
		//4. 从数据源中获取数据库连接
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		
		
	}
	
	/**
	 * 使用batch批量插入
	 */
	@Test
	public void batch() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		
		try {
			connection = JDBCTools.getConnection();
			sql = "INSERT INTO customers VALUES(?,?)";
			JDBCTools.beginTx(connection);
			
			preparedStatement = connection.prepareStatement(sql);
			
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				preparedStatement.setInt(1, i+1);
				preparedStatement.setString(2, "name_" + i);
				
				//累计SQL语句
				preparedStatement.addBatch();
				//累计300执行1次，并清空先前累计SQL
				if ((i + 1) % 300 == 0) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
				}
			}
			
			if (100000 % 300 != 0) {
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
			}
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time:" + (end - begin));
			
			JDBCTools.commite(connection);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(preparedStatement, connection);
		}
	}
	
	/**
	 * preparedStatement批量插入
	 */
	@Test
	public void testBatchWithPreparedStatement() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		
		try {
			connection = JDBCTools.getConnection();
			sql = "INSERT INTO customers VALUES(?,?)";
			JDBCTools.beginTx(connection);
			
			preparedStatement = connection.prepareStatement(sql);
			
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				preparedStatement.setInt(1, i+1);
				preparedStatement.setString(2, "name_" + i);
				
				preparedStatement.executeUpdate();
			}
			long end = System.currentTimeMillis();
			
			System.out.println("Time:" + (end - begin));
			
			JDBCTools.commite(connection);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(preparedStatement, connection);
		}
	}
	
	/**
	 * 向Oracle的customers数据表中插入10W条记录
	 * 测试如何用时最短
	 * statement批量插入
	 */
	@Test
	public void testBatchWithStatement() {
		Connection connection = null;
		Statement statement = null;
		String sql = null;
		
		try {
			connection = JDBCTools.getConnection();
			JDBCTools.beginTx(connection);
			
			statement = connection.createStatement();
			
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				sql = "INSERT INTO customers VALUES("+(i + 1)+",'name_"+ i+")";
				statement.executeUpdate(sql);
			}
			long end = System.currentTimeMillis();
			
			System.out.println("Time:" + (end - begin));
			
			JDBCTools.commite(connection);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(statement, connection);
		}
	}
	
	/**
	 * 读取blob数据
	 * 1. 使用getBlob方法读取到Blob对象
	 * 2. 调用Blob的getBinaryStream()方法得到输入流。再使用IO操作
	 */
	@Test
	public void readBlob() {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			//1. 获取connection		
			conn = JDBCTools.getConnection();
			String sql = "SELECT * FROM customers where id = ?";
			//2. 获取Statment
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, 9);
			
			//执行查询，得到ResultSet
			rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				
				System.out.println(id + "," + name + "," + email);
				
				Blob picture = rs.getBlob(5);
				InputStream in = picture.getBinaryStream();
				OutputStream out = new FileOutputStream("out.jpg");
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);					
				}
				
				out.close();
				
			}

			//6. 关闭数据库资源
		} catch (Exception e) {
 
			e.printStackTrace();
		} finally{
			JDBCTools.release(rs, preparedStatement, conn);
		}
	}
	
	/**
	 * 插入BLOB类型的数据必须使用PreparedStatement，因为BLOG类型的数据无法使用字符串拼接
	 * 
	 * 调用setBlob(int index, InputStream inputStream)
	 */
	@Test
	public void testInsertBlob(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "INSERT INTO customers(name,email,birth,picture) VALUES(?,?,?,?)";
			
			//使用重载的prepareStatement(sql,flag)
			preparedStatement = connection.prepareStatement(sql);
			
			//索引从1开始
			preparedStatement.setString(1, "testBLOB");
			preparedStatement.setString(2, "PreparedStatement@163.com");
			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
			InputStream inputStream = new FileInputStream("7.jpg");
			preparedStatement.setBlob(4, inputStream);
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, preparedStatement, connection);
		}
	}
	
	/**
	 * 获取数据库自动生成的主键
	 */
	@Test
	public void testGetKeyValue() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "INSERT INTO customers(name,email,birth) VALUES(?,?,?)";
			
			//使用重载的prepareStatement(sql,flag)
			preparedStatement = connection.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			
			//索引从1开始
			preparedStatement.setString(1, "getKey");
			preparedStatement.setString(2, "PreparedStatement@163.com");
			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
			
			preparedStatement.executeUpdate();
			
			//获取新生成的主键的ResultSet
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				System.out.println(resultSet.getObject(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, preparedStatement, connection);
		}
	}
	
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
