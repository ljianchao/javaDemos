package cn.jc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

public class TransacitonTest {

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
}
