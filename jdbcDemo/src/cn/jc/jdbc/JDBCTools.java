package cn.jc.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 操作jdbc的方法
 * @author Administrator
 *
 */
public class JDBCTools {
	
	private static DataSource dataSource = null;
	
	//数据库连接池应只被初始化一次
	static{
		dataSource =new ComboPooledDataSource("helloc3p0");
	}
	
	/**
	 * 提交事务
	 * @param connection
	 */
	public static void commite(Connection connection) {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 回滚事务
	 * @param connection
	 */
	public static void rollback(Connection connection) {
		if(connection != null){
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 开始事务
	 * @param connection
	 */
	public static void beginTx(Connection connection) {
		if (connection != null) {
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	/**
	 * 数据库连接池获取数据源
	 */
	public static Connection getConnectionByPool() throws Exception {		
		return dataSource.getConnection();
	}
	
	public static <T> T get(Class<T> clazz, String sql, Object...args) {
		T entity = null;
		
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			//1. 获取connection		
			conn = JDBCTools.getConnection();
			//2. 获取Statment
			preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			
			
			//4. 执行查询，得到ResultSet
			rs = preparedStatement.executeQuery();
			
			// 得到ResultSetMetaData对象
			ResultSetMetaData rsmd = rs.getMetaData();
			
			// 创建Map<Sting, Object>对象
			Map<String, Object> values = new HashMap<String, Object>();
			//5. 处理ResultSet
			if (rs.next()) {
				//利用反射创建对象
				entity = clazz.newInstance();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String columnLabel =rsmd.getColumnLabel(i + 1);
					Object columnValue = rs.getObject(i + 1);
					
					values.put(columnLabel, columnValue);
				}
				
				//通过解析SQL语句判断到底选择哪些列，以及需要为entity对象的哪些属性赋值				
			}
			
			if (values.size() > 0) {
				entity = clazz.newInstance();
				
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					String fieldName = entry.getKey();
					Object value = entry.getValue();
					
					ReflectionUtils.setFieldValue(entity, fieldName, value);
				}
			}
			
			//6. 关闭数据库资源
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCTools.release(rs, preparedStatement, conn);
		}
		return entity;
	}
	
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
				//数据库连接池的Connection对象进行close时，
				//并不是真的进行关闭，而是把该连接归还到数据库连接池中
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 执行SQL语句，使用PreparedStatement
	 * @param sql
	 * @param args：填写SQL占位符的可变参数
	 */
	public static void update(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			conn = getConnection();
			preparedStatement = conn.prepareStatement(sql);
			
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			release(preparedStatement, conn);
		}
	}
	
	/**
	 * 通用更新方法，包括INSERT、UPDATE、DELETE
	 * 采用Statement
	 */
	public static void update(String sql) {
		Connection conn = null;
		Statement statement = null;
		
		try {
			conn = getConnection();
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			// TODO: handle exception
		} finally{
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
	}
	
	
}
