package cn.jc.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 访问数据的DAO接口
 * @author Administrator
 * @param T: DAO处理的实体类的类型
 */
public interface DaoUtilsInterface<T> {
			
	/**
	 * 批量处理的方法
	 * @param connection
	 * @param sql
	 * @param args
	 */
	void batch(Connection connection, String sql, Object[]...args);
	
	/**
	 * 返回一个具体的值
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	<E> E getForValue(Connection connection, String sql, Object...args);
	
	/**
	 * 返回T的集合
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	List<T> getForList(Connection connection, String sql, Object...args);
	
	/**
	 * 返回一个T对象
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	T get(Connection connection, String sql, Object...args) throws SQLException;
	
	/**
	 * INSERT,UPDATE,DELETE
	 * @param connection
	 * @param sql
	 * @param args
	 */
	void update(Connection connection, String sql, Object... args);
}
