package cn.jc.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

/**
 * 测试DBUtils工具类
 * @author Administrator
 *
 */
public class DBUtilsTest {
	
	//线程安全的，可以定义全局变量?
	QueryRunner queryRunner = new QueryRunner();
	
	/**
	 * 把结果集转为一个数值（可以是任意基本数据类型）
	 */
	@Test
	public void testScalarHandler() {
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT name FROM customers WHERE id = ?";
			String result = queryRunner.query(connection, sql, new ScalarHandler<String>(), 5);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
	}
	
	@Test
	public void testMapListHandler(){
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth FROM customers WHERE id >= ?";
			List<Map<String, Object>> result = queryRunner.query(connection, sql, new MapListHandler(), 5);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
	}
	
	/**
	 * 只返回一条记录
	 */
	@Test
	public void testMapHandler() {
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth FROM customers WHERE id >= ?";
			Map<String, Object> result = queryRunner.query(connection, sql, new MapHandler(), 5);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
		
	}
	
	@Test
	public void testBeanListHandler() {
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth FROM customers WHERE id >= ?";
			List<Customer> customer = queryRunner.query(connection, sql, new BeanListHandler<Customer>(Customer.class), 5);
			System.out.println(customer);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
		
	}
	
	/**
	 * BeanHandler把结果集的第一条记录转为创建BeanHandler对象时传入的Class参数对应的对象
	 */
	@Test
	public void testBeanHandler() {
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth FROM customers WHERE id = ?";
			Customer customer = queryRunner.query(connection, sql, new BeanHandler<Customer>(Customer.class), 5);
			System.out.println(customer);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
	}
	
	
	class MyResultSetHanlder implements ResultSetHandler{

		@Override
		public Object handle(ResultSet rs) throws SQLException {
//			System.out.println("handle...");
//			return "jc";
			List<Customer> customers = new ArrayList<Customer>();
			
			while(rs.next()){
				Integer id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				Date birth = rs.getDate(4);
				
				Customer customer = new Customer(id, name, email, birth);
				
				customers.add(customer);				
			}
			
			return customers;
		}
		
	}
	
	/**
	 * QueryRunner的query方法的返回值取决于其ResultSetHanler参数的handler的返回值
	 */
	@Test
	public void testQuery() {
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth FROM customers";
			@SuppressWarnings("unchecked")
			Object obj = queryRunner.query(connection, sql,new MyResultSetHanlder());
			
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
	}
	
	/**
	 * 测试QueryRunner类的update方法
	 */
	@Test
	public void testQueryRunnerUpdate() {
		//1. 创建QueryRunner的实现类
		//QueryRunner queryRunner = new QueryRunner();
		
		String sql = "DELETE FROM customers WHERE id in (?,?)";
		
		Connection connection = null;
		
		try {
			connection = JDBCTools.getConnection();
			
			queryRunner.update(connection, sql, 12,13);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null, null, connection);
		}
	}
}
