package com.lsy.code.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import com.lsy.code.database.domain.User;
import com.lsy.code.database.utils.C3P0Utils;
import com.lsy.code.database.utils.JDBCUtils;

public class TestDemo {
	@Test
	public void test2_jdbc() {
		try {
			// 获取连接
			Connection connection = C3P0Utils.getConnection();
			String sql_query = "SELECT * FROM user LIMIT ?,?";
			QueryRunner runner = new QueryRunner();
			List<User> list = runner.query(connection, sql_query, new ResultSetHandler<List<User>>() {

				@Override
				public List<User> handle(ResultSet rs) throws SQLException {
					List<User> list = new ArrayList<User>();
					ResultSetMetaData metaData = rs.getMetaData();
					int count = metaData.getColumnCount();
					while (rs.next()) {
						User user = new User();
						for (int i = 1; i <= count; i++) {
							setProperty(user, metaData.getColumnLabel(i), rs.getObject(metaData.getColumnLabel(i)));
						}
						list.add(user);

					}
					return list;
				}

			}, 0, 10);
			System.out.println(list);

			// 释放资源
			JDBCUtils.closeQuietly(connection, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setProperty(Object bean, String name, Object value) {
		try {
			Class<? extends Object> clazz = bean.getClass();
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);// 将private 修改为public
			field.set(bean, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test1_jdbc() {
		String sql = "SELECT * FROM user LIMIT ?,?";

		try {
			// 获取连接
			Connection connection = JDBCUtils.getConnection();
			QueryRunner runner = new QueryRunner();
			List<User> list = runner.query(connection, sql, new BeanListHandler<User>(User.class), 0, 10);
			System.out.println(list);
			// 释放资源
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
