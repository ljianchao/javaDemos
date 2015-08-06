package cn.jc.jdbc;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class DAO {
	
	//Insert,Update,Delete操作
	public void update(String sql, Object... args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JDBCTools.getConnection();			
			preparedStatement = connection.prepareStatement(sql);
			
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(null,  preparedStatement, connection);
		}
	}
	
	//查询一条记录，返回对应的对象
	public <T> T get(Class<T> clazz, String sql, Object... args){
		T entity = null;
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			//1. 获取connection
			connection = JDBCTools.getConnection();
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
				Map<String, Object> values = new HashMap<String, Object>();
				
				//6. 获取ResultSetMetaData对象
				ResultSetMetaData rsmd = resultSet.getMetaData();
				
				//7. 处理ResultSet，把指针向下移动一个单位				
				
				//8. 获取结果集共有多少列
				//9. 获取每一列的值
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object columnValue = resultSet.getObject(columnLabel);
					
					//10. 填充Map对象
					values.put(columnLabel, columnValue);
				}							
				
				//11. 用反射创建Class对应的对象
				entity = clazz.newInstance();
				
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					
					//ReflectionUtils.setFieldValue(entity, propertyName, value);
					BeanUtils.setProperty(entity, propertyName, value);
					
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(resultSet, preparedStatement, connection);			
		}
		
		return entity;
	}
	
	//查询多条记录，返回对应的对象的集合
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args){
		List<T> list = new ArrayList<T>();
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			//1. 获取connection
			connection = JDBCTools.getConnection();
			//2. 获取PreparedStatement
			preparedStatement = connection.prepareStatement(sql);
			//3. 填充占位符
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			//4. 进行查询，得到ResultSet
			resultSet = preparedStatement.executeQuery();
			
			//6. 获取ResultSetMetaData对象
			List<Map<String, Object>> values = new ArrayList<Map<String,Object>>();
			
			ResultSetMetaData rsmd = resultSet.getMetaData();
			Map<String, Object> map = null;
			
			//5. 若ResultSet中有记录，准备Map<String,Object>
			//7. 处理ResultSet，把指针向下移动一个单位	
			while (resultSet.next()) {
				map = new HashMap<String, Object>();
				
				//8. 获取结果集共有多少列
				//9. 获取每一列的值
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object columnValue = resultSet.getObject(columnLabel);
					
					//10. 填充Map对象
					map.put(columnLabel, columnValue);
				}							
				
				values.add(map);				
			}			
			
			T bean = null;
			
			if (values.size() > 0) {
				for (Map<String, Object> m : values) {
					bean = clazz.newInstance();
					for (Map.Entry<String, Object> entry : m.entrySet()) {
						String propertyName = entry.getKey();
						Object value = entry.getValue();
						
						BeanUtils.setProperty(bean, propertyName, value);
					}
					
					list.add(bean);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.release(resultSet, preparedStatement, connection);			
		}
		
		return list;
	}
	
	//返回某条记录的某个字段或一个统计的值
	public <E> E getForValue(String sql, Object... args){
		return null;
	}
}
