package cn.jc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class TransacitonTest {

	/**
	 * 测试事务的隔离级别
	 */
	@Test
	public void testTransactionIsolationUpdate() {
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			connection.setAutoCommit(false);
			
			String sql = "UPDATE users SET balace = " +
					"balace - 500 WHERE id = 1";
			update(connection,sql);
			
			connection.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//回滚事务
			try {				
				connection.rollback();
			} catch (SQLException e1) { 
				e1.printStackTrace();
			}
		} finally{
			JDBCTools.release(null, connection);
		}
	}
	
	/**
	 * 测试事务的隔离级别
	 */
	@Test
	public void testTransactionIsolationRead() {
		String sql = "SELECT balace FROM users WHERE id = 1";
		
		Float balace = getForValue(sql);
		System.out.println(balace);
	}
	
	/**
	 * 关于事务：
	 * 1. 如果多个操作，每个操作使用的是自己的单独的连接，则无法保证事务
	 * 2. 具体步骤	
	 */
	@Test
	public void testTransation(){
		Connection connection = null;
		
		try {
			
			connection = JDBCTools.getConnection();
			System.out.println(connection.getAutoCommit());
			
			//开始事务，取消默认提示
			connection.setAutoCommit(false);
			System.out.println(connection.getAutoCommit());
			
			
			String sql = "UPDATE users SET balace = " +
			"balace - 500 WHERE id = 1";
	
//			int i = 10 / 0;
//			System.out.println(i);
			
			update(connection,sql);
	
			sql = "UPDATE users SET balace = " +
					"balace + 500 WHERE id = 2";
			
			update(connection,sql);
			
			//提交事务
			connection.commit();
			
			System.out.println(connection.getAutoCommit());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//回滚事务
			try {				
				connection.rollback();
			} catch (SQLException e1) { 
				e1.printStackTrace();
			}
			
		} finally{
			JDBCTools.release(null, null, connection);
		}
//		DAO dao = new DAO();
//		String sql = "UPDATE users SET balace = " +
//				"balace - 500 WHERE id = 1";
//		
//		dao.update(sql);
//		
//		sql = "UPDATE users SET balace = " +
//				"balace + 500 WHERE id = 2";
//		
//		dao.update(sql);
	}
	
	public void update(Connection connection, String sql, Object... args){
		PreparedStatement preparedStatement = null;
		
		try {			
			preparedStatement = connection.prepareStatement(sql);
			
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null,  preparedStatement, null);
		}
	}
	
	//返回某条记录的某个字段或一个统计的值
		public <E> E getForValue(String sql, Object... args){		
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			try {
				//1. 获取connection
				connection = JDBCTools.getConnection();
				// 设置事务的隔离级别
				connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				
				//2. 获取PreparedStatement
				preparedStatement = connection.prepareStatement(sql);
				//3. 填充占位符
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
				//4. 进行查询，得到ResultSet
				resultSet = preparedStatement.executeQuery();
				
				//5. 若ResultSet中有记录，准备一个Map<String,Object>
				if (resultSet.next()) {
					return (E)resultSet.getObject(1);
				}			
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				JDBCTools.release(resultSet, preparedStatement, connection);			
			}
			
			return null;
		}
}
