package com.wnt.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlInserDemo {

	 public static void main(String[] args) throws ClassNotFoundException {
		 Connection conn = null;
		 CallableStatement callStmt = null;
		 try{
			String url = "jdbc:mysql://10.100.253.78:3306/wnt_db?user=wnt&password=gtjaqh&useUnicode=true&characterEncoding=utf8";
			Class.forName("com.mysql.jdbc.Driver");//
		 // conn =SingleGetCon.getConnection();
		  conn = DriverManager.getConnection(url);
		 System.out.println(conn.toString());
		 
		 
		 String sql = "INSERT INTO rbac_userextendinfo (userid,username,mobile,email,telephone,creatorId,createTime) VALUES (\"1005\",\"测试\",\"12321\",\"31231231\",\"3123131\",\"sysadmin\",NOW());";
		 
		 sql = "CALL p_insertUserExtend(\"1005\",\"测试\",\"12321\",\"1\",\"123123\",\"31231\",\"dfasdfas\",\"\",\"sysadmin\")";
		 System.out.println(sql);
		 
		 callStmt = conn.prepareCall(sql);
		 callStmt.execute(); 
		 
	 }	catch (SQLException e) {
		 
	 }finally {
		 try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
		 
	 }
}
