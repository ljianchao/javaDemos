package cn.jc.jdbc;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import org.junit.Test;

public class DAOTest {
	
	DAO dao = new DAO();
	
	@Test
	public void testUpdate() {
		String sql = "INSERT INTO customers(name,email,birth) VALUES(?,?,?)";
		
		dao.update(sql, "test dao update", "dao@email.com", new Date(new java.util.Date().getTime()));
	}

	@Test
	public void testGet() {
		String sql = "SELECT id as id,name,email,birth FROM customers WHERE id = ? ";
		
		Customer customer = dao.get(Customer.class, sql, 1);
		System.out.println(customer);
	}

	@Test
	public void testGetForList() {
		String sql = "SELECT id as id,name,email,birth FROM customers WHERE id > ?";
		
		List<Customer> customerList = dao.getForList(Customer.class, sql, 2);
		System.out.println(customerList);
	}

	@Test
	public void testGetForValue() {
		String sql = "SELECT name FROM customers WHERE id > ?";
		
		String name = dao.getForValue(sql, 2);
		System.out.println(name);
	}

}
