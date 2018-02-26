package com.shtydic.neo4j.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class JDBCUtil {
	public Boolean selectdatabysql(String sql){
		Connection conn = getConn();
		Statement st;
		Boolean i=false;
		try {
			st = conn.createStatement();
			i=st.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	public ResultSet executeSQL(String sql) throws SQLException {
		Connection conn = getConn();
		Statement stmtt = null;
		ResultSet rs = null;
		try {
			stmtt = conn.createStatement();
			rs = stmtt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();  
			int columnCount = rsmd.getColumnCount();  
			
//			JSONArray fArray = new JSONArray();
//			
//			for (int i = 0; i < columnCount; i++) {
//				JSONArray maxArray = new JSONArray();
//			}
			while(rs.next()){
		        int id = rs.getInt(1);
		        String sfzhm = rs.getString(2);
		        String fssj = rs.getString(3);
		        System.out.println("id:"+id+" 身份证号码："+sfzhm+" 发生时间："+fssj);
		    }
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
					stmtt = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// closeConnection();
		}
		return rs;
	}
	
	
	/** 
     * @method getConn() 获取数据库的连接 
     * @return Connection 
     */  
    public static Connection getConn() {  
        String driver = Constants.oracle_driver; 
        String url = Constants.oracle_url;
        String username = Constants.oracle_username;  
        String password = Constants.oracle_password;  
        Connection conn = null;  
        try {  
            Class.forName(driver); // classLoader,加载对应驱动  
            conn = (Connection) DriverManager.getConnection(url, username, password);  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return conn;  
    }
    
    /**
     * 根据sql返回Map
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
	public Map<String,  Map<String, String>> getMapBySQL(String sql) throws SQLException {
		Connection conn = getConn();
		Statement stmtt = null;
		ResultSet rs = null;
		Map<String, Map<String, String>> resMap = new HashMap<String, Map<String, String>>();
		try {
			stmtt = conn.createStatement();
			rs = stmtt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, String> resMap2 = new HashMap<String, String>();
				for (int i = 1; i < columnCount; i++) {
					resMap2.put(rsmd.getColumnName(i), rs.getString(i));
				}
				resMap.put(rs.getString("id"), resMap2);
				
			}
			System.out.println(resMap);
			return resMap;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
					stmtt = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// closeConnection();
		}
		return resMap;
	}
	
	/**
     * 根据sql返回数组
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
	public ArrayList<Map<String, String>> getArrBySQL(String sql) throws SQLException {
		Connection conn = getConn();
		Statement stmtt = null;
		ResultSet rs = null;
		
		ArrayList<Map<String, String>> resArr = new ArrayList<Map<String,String>>();
		try {
			stmtt = conn.createStatement();
			rs = stmtt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, String> resMap = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					resMap.put(rsmd.getColumnName(i), rs.getString(i));
				}
				resArr.add(resMap);
				
			}
			return resArr;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtt != null) {
				try {
					stmtt.close();
					stmtt = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// closeConnection();
		}
		return resArr;
	}
}
